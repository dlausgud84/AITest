# 5. 검증 (Verification)

## 🎯 목표
코드의 정상 동작을 확인하고, 빌드 및 통합 환경에서의 오류를 방지합니다.

## ✅ 체크리스트
- [ ] **API 기능 점검**
  - [ ] Swagger 또는 Postman을 활용한 엔드포인트 직접 호출 및 응답 검증
  - [ ] 예외 상황 발생 시 공통 Error Response DTO가 정상 반환되는지 확인
- [ ] **빌드 및 정적 분석**
  - [ ] Gradle Kotlin DSL (`./gradlew clean build`) 정상 빌드 확인
  - [ ] Checkstyle, SonarQube 등 코드 컨벤션/정적 분석 툴 경고 확인 및 수정
- [ ] **문서 최신화**
  - [ ] API 명세서(Swagger 등) 최신화 상태 확인

---

## 📋 포트 및 환경 설정 확인

### 백엔드 포트 및 CORS 설정

| 항목 | 값 | 확인 방법 |
|------|-----|----------|
| **포트** | 8080 | `lsof -i :8080` (포트 사용 확인) |
| **CORS** | `CorsConfig.java` | 파일에서 `/api/**` + `http://localhost:3000` 설정 확인 |
| **환경 변수** | `application-dev.yml` | DB 연결 설정 확인 |
| **SSL 설정** | `trustServerCertificate=true` | MSSQL 자체 서명 인증서 허용 |

**확인 항목**:
- [ ] 서버 시작 로그에서 `Listening on port 8080` 메시지 확인
- [ ] `CorsConfig.java`의 내용:

```java
@Configuration
public class CorsConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
          .allowedOrigins("http://localhost:3000")
          .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
          .allowCredentials(true);
      }
    };
  }
}
```

### 환경 프로파일 확인

```yaml
# application-dev.yml 확인 항목
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=AiCodeTestDB;trustServerCertificate=true
    username: sa
    password: [설정됨]
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver

mybatis:
  type-aliases-package: com.dit
  mapper-locations: classpath:mappers/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
```

### Swagger 문서 확인

- [ ] 서버 실행 후 `http://localhost:8080/swagger-ui/index.html` 접속 가능한지 확인
- [ ] 모든 `@RestController` 엔드포인트가 Swagger에 노출되었는지 확인
- [ ] Request/Response DTO가 올바르게 문서화되었는지 확인

**Swagger 어노테이션 예시**:
```java
@RestController
@RequestMapping("/api/menus")
@Tag(name = "메뉴 관리", description = "메뉴 CRUD API")
public class MenuController {
  
  @GetMapping("/{id}")
  @Operation(summary = "메뉴 조회", description = "ID로 메뉴 정보 조회")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  public ApiResponse<MenuRes> getMenu(
    @PathVariable Long id,
    @ParameterObject @RequestParam(name = "memo") String memo
  ) {
    // ...
  }
}
```