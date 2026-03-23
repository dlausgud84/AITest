# 5. 검증 (Verification)

## 🎯 목표
코드의 정상 동작을 확인하고, 빌드 및 통합 환경에서의 오류를 방지하며, 프론트엔드 연동 준비를 마칩니다.

## ✅ 체크리스트
- [ ] **API 기능 및 로깅 점검**
  - [ ] Swagger 또는 Postman을 활용한 엔드포인트 직접 호출 및 응답(JSON 포맷) 검증
  - [ ] 예외 상황 발생 시 공통 Error Response DTO가 정상 반환되는지 확인
  - [ ] API 호출 시 콘솔에 MyBatis 쿼리 및 바인딩 파라미터가 정상적으로 로깅되는지 확인
- [ ] **보안 검증 🔒**
  - [ ] 민감 데이터(비밀번호, 개인정보)가 Response JSON에 노출되지 않는지 확인
  - [ ] **[🔒필수]** DB에 저장된 비밀번호가 BCrypt 해시(`$2a$10$...`) 형식인지 직접 확인
  - [ ] **[🔒필수]** 로그 콘솔에 비밀번호·토큰·개인정보가 출력되지 않는지 확인
  - [ ] SQL Injection 공격 패턴 입력 시 적절한 방어 동작 확인
  - [ ] 보안 헤더 설정 확인 (아래 상세 항목 참조)
  - [ ] **[🔒필수]** `trustServerCertificate=true` 설정이 `application-prod.yml`에는 없는지 확인 (운영 환경에서는 공식 인증서 사용)
  - [ ] **[🔒필수]** Spring Actuator 엔드포인트(`/actuator/**`)가 운영 환경에서 외부에 노출되지 않도록 설정 확인
- [ ] **빌드 및 테스트 커버리지 분석**
  - [ ] Gradle Kotlin DSL (`./gradlew clean build`) 정상 통과 확인
  - [ ] 테스트 커버리지 리포트(Jacoco 등) 확인 및 목표 수치 달성 여부 점검
  - [ ] Checkstyle, SonarQube 등 코드 컨벤션/정적 분석 툴 경고 확인 및 수정
- [ ] **문서 최신화**
  - [ ] API 명세서(Swagger) 최신화 상태 확인 및 누락된 엔드포인트/파라미터 점검

---

## 📋 포트 및 환경 설정 확인

### 백엔드 포트 및 CORS 설정

| 항목 | 값 | 확인 방법 |
|------|-----|----------|
| **포트** | 8080 | `lsof -i :8080` (포트 사용 확인) |
| **CORS** | `CorsConfig.java` | 파일에서 `/api/**` + 환경변수 기반 오리진 설정 확인 |
| **환경 변수** | `application-dev.yml` | DB 연결 설정 및 로깅 레벨 확인 |
| **SSL 설정** | `trustServerCertificate=true` | 개발 환경 전용 — 운영 환경 `application-prod.yml`에는 없어야 함 |

**확인 항목**:
- [ ] 서버 시작 로그에서 `Listening on port 8080` 메시지 확인
- [ ] `CorsConfig.java`의 내용 확인:

```java
// ✅ 올바른 CORS 설정 패턴 (환경변수 기반)
@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;  // 환경변수로 오리진 관리

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins.split(",")) // 환경변수로 분리
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600); // Preflight 캐싱 1시간
            }
        };
    }
}

// ✅ application-dev.yml
// cors:
//   allowed-origins: http://localhost:3000

// ✅ application-prod.yml
// cors:
//   allowed-origins: https://your-production-domain.com
```

### 보안 헤더 검증

```bash
# curl로 응답 헤더 확인
curl -I http://localhost:8080/api/users

# ✅ 권장 보안 헤더 확인 항목:
# X-Content-Type-Options: nosniff
# X-Frame-Options: DENY
# X-XSS-Protection: 1; mode=block
# Strict-Transport-Security: max-age=31536000; includeSubDomains (HTTPS 환경)
# Content-Security-Policy: default-src 'self'
```

Spring Security 미사용 시 `GlobalFilter` 또는 `HttpServletResponse`를 통해 수동으로 헤더를 추가합니다:

```java
// ✅ 보안 헤더 필터 예시
@Component
public class SecurityHeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        chain.doFilter(req, res);
    }
}
```

### Spring Actuator 보안 설정 확인

```yaml
# ✅ application-prod.yml: Actuator 노출 최소화
management:
  endpoints:
    web:
      exposure:
        include: health  # health만 노출 (info, metrics 등 민감 정보 제외)
  endpoint:
    health:
      show-details: never  # 운영 환경에서는 상세 정보 숨김
```
