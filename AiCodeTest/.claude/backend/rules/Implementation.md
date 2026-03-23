# 4. 구현 (Implementation)

## 🎯 목표
Java 21, Spring Boot, MyBatis를 활용하여 실제 비즈니스 로직을 안전하고 효율적으로 구현합니다.

## ✅ 체크리스트
- [ ] **Data Access 계층 구현**
  - [ ] Domain / DTO 클래스 생성 (DTO는 필요한 필드만 포함, Java 21 `record` 활용 가능)
  - [ ] Mapper 인터페이스 및 XML 쿼리 작성 (`#{}` 사용 확인, `${}` 사용 시 화이트리스트 검증 여부 확인)
  - [ ] **[🔒필수]** 민감 데이터 필드에 `@JsonIgnore` 적용하여 JSON 응답에서 제외
- [ ] **Business 로직 계층 구현 (Service)**
  - [ ] 트랜잭션(`@Transactional`) 범위 및 예외 롤백 설정 적용
  - [ ] 단순 조회 메서드에는 `@Transactional(readOnly = true)` 적용 (성능 최적화)
  - [ ] 비즈니스 로직 구현 및 생성자 주입 (`@RequiredArgsConstructor` 활용)
  - [ ] **[🔒필수]** 비밀번호는 `BCryptPasswordEncoder`로 해싱 후 저장 — 평문 저장·비교 절대 금지
  - [ ] **[🔒필수]** 로그에 비밀번호, 토큰, 개인정보(주민번호 등) 절대 출력 금지
- [ ] **Presentation 계층 구현 (Controller)**
  - [ ] REST Controller 구현 및 적절한 HTTP 상태 코드 반환 (200 OK, 201 Created, 204 No Content 등)
  - [ ] 입력값 검증 (`@Valid`, `@Validated`) 적용
- [ ] **공통 처리 및 로깅**
  - [ ] Global Exception Handler(`@RestControllerAdvice`)에 커스텀 예외 및 Validation 예외 추가
  - [ ] 주요 비즈니스 분기점 및 에러 발생 시 SLF4J(`@Slf4j`)를 활용한 로그 작성

---

## 📋 백엔드 코드 패턴 및 규칙

### 1. DTO 설계 패턴

```java
// ✅ 요청 DTO: Jakarta Validation 적용
// 위치: com.dit.<domain>.dto.CreateUserReq
public class CreateUserReq {

    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Size(min = 3, max = 20, message = "사용자 ID는 3~20자여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String emailId;
}

// ✅ 응답 DTO: 민감 데이터 제외
// 비밀번호, 토큰 등은 응답 DTO에 포함하지 않거나 @JsonIgnore 적용
public class UserRes {
    private String userId;
    private String userName;
    private String emailId;

    @JsonIgnore // ← 절대 응답에 포함되지 않도록 강제
    private String encodePassword;
}
```

### 2. 비밀번호 해싱 (BCrypt) 🔒

```java
// ✅ Config: BCryptPasswordEncoder Bean 등록
@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // strength 기본값 10 (권장)
    }
}

// ✅ Service: 비밀번호 저장 시 해싱
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createUser(CreateUserReq req) {
        // 비밀번호 해싱 후 저장 — 평문 저장 금지
        String hashedPassword = passwordEncoder.encode(req.getPassword());
        UserDomain domain = new UserDomain();
        domain.setUserId(req.getUserId());
        domain.setEncodePassword(hashedPassword);
        userMapper.insert(domain);
    }

    public boolean verifyPassword(String rawPassword, String storedHash) {
        // 로그인 시 비교 — matches()는 내부에서 BCrypt 검증
        return passwordEncoder.matches(rawPassword, storedHash);
    }
}
```

### 3. 로깅 규칙 (SLF4J) 🔒

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public void createUser(CreateUserReq req) {
        // ✅ 올바른 로그: 사용자 ID만 기록
        log.info("사용자 등록 시작: userId={}", req.getUserId());

        // ❌ 절대 금지: 비밀번호·개인정보를 로그에 출력
        // log.info("사용자 등록: userId={}, password={}", req.getUserId(), req.getPassword());

        // ✅ 에러 로그: 예외 스택 트레이스 포함
        try {
            userMapper.insert(domain);
            log.info("사용자 등록 완료: userId={}", req.getUserId());
        } catch (Exception e) {
            log.error("사용자 등록 실패: userId={}, error={}", req.getUserId(), e.getMessage(), e);
            throw new BusinessException(ErrorCode.USER_CREATE_FAILED);
        }
    }
}
```

### 4. Controller 계층 (HTTP 상태 코드)

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 목록 조회: 200 OK
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDomain>>> getList(@RequestParam(required = false) String userId) {
        return ResponseEntity.ok(ApiResponse.success("사용자 목록 조회 성공", userService.getList(userId)));
    }

    // ✅ 단건 조회: 200 OK (없으면 404는 GlobalExceptionHandler에서 처리)
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDomain>> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.success("사용자 조회 성공", userService.getUser(userId)));
    }

    // ✅ 등록: 201 Created
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody CreateUserReq req) {
        userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponse.success("사용자 등록 성공", null));
    }

    // ✅ 수정: 200 OK
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserReq req) {
        userService.updateUser(userId, req);
        return ResponseEntity.ok(ApiResponse.success("사용자 수정 성공", null));
    }

    // ✅ 삭제: 200 OK (또는 204 No Content)
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("사용자 삭제 성공", null));
    }
}
```

### 5. MyBatis XML 쿼리 패턴

```xml
<!-- ✅ SQL Injection 방지: #{} 사용 (Prepared Statement) -->
<select id="selectByUserId" resultType="com.dit.user.domain.UserDomain">
    SELECT USER_ID         AS userId,
           USER_NAME       AS userName,
           EMAIL_ID        AS emailId,
           ENCODE_PASSWORD AS encodePassword,
           CREATE_DTTM     AS createDttm
    FROM NB_USERS
    WHERE USER_ID = #{userId}         <!-- ✅ #{} 사용 -->
      AND DELETE_FLAG = 'N'
</select>

<!-- ✅ 동적 정렬: ${} 사용 시 반드시 서비스 계층에서 화이트리스트 검증 후 사용 -->
<select id="selectList" resultType="com.dit.user.domain.UserDomain">
    SELECT USER_ID AS userId, USER_NAME AS userName
    FROM NB_USERS WITH (NOLOCK)
    WHERE DELETE_FLAG = 'N'
    <if test="userId != null and userId != ''">
        AND USER_ID LIKE '%' + #{userId} + '%'  <!-- ✅ #{} 사용 -->
    </if>
    ORDER BY ${sortColumn} ${sortDirection}      <!-- ⚠️ ${}는 화이트리스트 검증 후에만 허용 -->
</select>

<!-- ✅ 등록: CREATE_DTTM은 DB 함수로 자동 설정 -->
<insert id="insert" parameterType="com.dit.user.domain.UserDomain">
    INSERT INTO NB_USERS (
        USER_ID, USER_NAME, ENCODE_PASSWORD,
        DELETE_FLAG, CREATE_DTTM, CREATOR_ID
    ) VALUES (
        #{userId}, #{userName}, #{encodePassword},
        'N',
        FORMAT(GETDATE(), 'yyyyMMddHHmmss'),
        #{creatorId}
    )
</insert>
```

### 6. Service 계층 패턴

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // ← 기본값: 읽기 전용 (성능 최적화)
public class UserService {

    private final UserMapper userMapper;

    // ✅ 조회: readOnly = true 상속 (트랜잭션 오버헤드 최소화)
    public List<UserDomain> getList(String userId) {
        return userMapper.selectList(userId);
    }

    // ✅ 변경: @Transactional 명시 (readOnly=false로 오버라이드)
    @Transactional
    public void createUser(CreateUserReq req) {
        // 중복 체크
        if (userMapper.existsByUserId(req.getUserId())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        // 비즈니스 로직 처리 후 저장
        UserDomain domain = buildDomain(req);
        userMapper.insert(domain);
    }
}
```
