# 3. 테스트 작성 (Test Code Generation)

## 🎯 목표
비즈니스 로직과 데이터 접근 계층(MyBatis), 그리고 API 엔드포인트의 신뢰성을 보장하는 테스트를 작성합니다.

## ✅ 체크리스트
- [ ] **단위 테스트 (Unit Test - JUnit5 / Mockito)**
  - [ ] Service 계층의 핵심 비즈니스 로직 테스트 (Mocking 활용)
  - [ ] 예외 발생 조건(Validation 실패, 데이터 없음 등)에 대한 테스트 추가
  - [ ] 경계값(Boundary Value) 테스트 (최소/최대 길이, 0/음수/최대값 등)
- [ ] **API 계층 테스트 (WebMvcTest)**
  - [ ] Controller 엔드포인트(URL, HTTP Method, HTTP Status) 정상 응답 확인
  - [ ] Request DTO의 `@Valid` 검증 로직 작동 여부 확인
- [ ] **보안 테스트 (Security Test) 🔒**
  - [ ] SQL Injection 공격 시도에 대한 방어 테스트 (특수문자, SQL 키워드 입력)
  - [ ] XSS 공격 패턴 입력 시 적절한 이스케이프 처리 확인
  - [ ] 인증되지 않은 요청에 대한 401/403 응답 확인
  - [ ] 민감 데이터(비밀번호, 토큰)가 Response JSON에 노출되지 않는지 확인
  - [ ] **[🔒필수]** 비밀번호 BCrypt 해싱 검증 — 저장된 값이 평문이 아닌지 확인
- [ ] **통합 및 데이터 계층 테스트 (Integration Test)**
  - [ ] `@MybatisTest`를 활용한 Mapper + XML 동적 쿼리 정상 동작 확인
  - [ ] MSSQL 고유 문법(`OFFSET`, `NOLOCK`) 테스트 시 Testcontainers 활용 권장

---

## 📋 JUnit5 + Mockito 테스트 패턴

### 1. Controller 계층 테스트 (MockMvc)
API의 스펙과 HTTP 통신, 입력값 검증을 테스트합니다.

```java
// UserControllerTest.java
@WebMvcTest(UserController.class) // Controller 관련 빈만 로드하여 가볍고 빠름
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 정상 등록
    @Test
    @DisplayName("사용자 등록 성공 시 201 Created 반환")
    void 사용자_등록_성공() throws Exception {
        Map<String, String> req = Map.of(
            "userId", "testUser",
            "password", "Password1!",
            "userName", "테스트사용자"
        );

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isCreated())          // ← 201
               .andExpect(jsonPath("$.success").value(true));
    }

    // ✅ Validation 실패: 비밀번호 누락
    @Test
    @DisplayName("필수 필드 누락 시 400 Bad Request 반환")
    void 사용자_등록_필수값_누락() throws Exception {
        Map<String, String> req = Map.of("userId", "testUser"); // password 누락

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isBadRequest());
    }

    // ✅ 보안 테스트: 응답에 비밀번호 미포함 확인
    @Test
    @DisplayName("사용자 조회 응답에 비밀번호(encodePassword) 필드 미포함 확인")
    void 사용자_조회_비밀번호_미노출() throws Exception {
        UserDomain mockUser = new UserDomain();
        mockUser.setUserId("testUser");
        mockUser.setUserName("테스트사용자");
        // encodePassword는 응답 DTO에서 @JsonIgnore 처리되어 있어야 함

        given(userService.getUser("testUser")).willReturn(mockUser);

        mockMvc.perform(get("/api/users/testUser"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data.encodePassword").doesNotExist()); // ← 비밀번호 미노출
    }
}
```

### 2. Service 계층 테스트 (Mockito)

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // ✅ 정상 케이스
    @Test
    @DisplayName("사용자 등록 성공")
    void 사용자_등록_성공() {
        // given
        CreateUserReq req = new CreateUserReq("newUser", "Password1!", "test@test.com");
        given(userMapper.existsByUserId("newUser")).willReturn(false);
        given(passwordEncoder.encode("Password1!")).willReturn("$2a$10$hashedValue");

        // when
        assertDoesNotThrow(() -> userService.createUser(req));

        // then
        verify(userMapper, times(1)).insert(any(UserDomain.class));
    }

    // ✅ 중복 사용자 예외
    @Test
    @DisplayName("중복 userId 등록 시 BusinessException 발생")
    void 사용자_등록_중복_예외() {
        // given
        CreateUserReq req = new CreateUserReq("existingUser", "Password1!", "test@test.com");
        given(userMapper.existsByUserId("existingUser")).willReturn(true);

        // when & then
        BusinessException ex = assertThrows(BusinessException.class,
            () -> userService.createUser(req));
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_ALREADY_EXISTS);
    }

    // ✅ 보안 테스트: 비밀번호 BCrypt 해싱 검증
    @Test
    @DisplayName("비밀번호는 BCrypt 해싱 후 저장되어야 한다 (평문 저장 금지)")
    void 비밀번호_BCrypt_해싱_검증() {
        // given
        String rawPassword = "Password1!";
        CreateUserReq req = new CreateUserReq("testUser", rawPassword, "test@test.com");
        given(userMapper.existsByUserId("testUser")).willReturn(false);
        given(passwordEncoder.encode(rawPassword)).willReturn("$2a$10$hashedValue");

        // when
        userService.createUser(req);

        // then: 저장된 도메인의 비밀번호가 해싱된 값인지 확인
        ArgumentCaptor<UserDomain> captor = ArgumentCaptor.forClass(UserDomain.class);
        verify(userMapper).insert(captor.capture());
        assertThat(captor.getValue().getEncodePassword()).isNotEqualTo(rawPassword); // 평문이 아님
        assertThat(captor.getValue().getEncodePassword()).startsWith("$2a$"); // BCrypt 형식
    }

    // ✅ 경계값 테스트
    @Test
    @DisplayName("사용자 ID 최소 길이(3자) 경계값 테스트")
    void 사용자ID_최소_길이_경계값() {
        CreateUserReq req = new CreateUserReq("ab", "Password1!", "test@test.com"); // 2자 (허용 불가)
        // Validation이 Service 호출 전에 Controller에서 걸려야 함 → @Valid 테스트
        // 또는 Service 자체에 검증이 있다면:
        assertThrows(BusinessException.class, () -> userService.createUser(req));
    }
}
```

### 3. 보안 테스트: SQL Injection 방어

```java
@Test
@DisplayName("SQL Injection 패턴 입력 시 정상 처리 (에러 또는 빈 결과 반환)")
void SQL_Injection_방어_테스트() throws Exception {
    // SQL Injection 공격 패턴
    String maliciousInput = "' OR '1'='1"; // 또는 "1; DROP TABLE NB_USERS; --"

    mockMvc.perform(get("/api/users")
            .param("userId", maliciousInput))
           .andExpect(status().isOk()) // 서버 에러(500)가 아닌 정상 처리
           .andExpect(jsonPath("$.data").isArray()); // 빈 배열 또는 정상 결과 반환
    // ✅ 핵심: #{} Prepared Statement 사용 시 SQL Injection은 리터럴 문자열로 처리되어 방어됨
}
```
