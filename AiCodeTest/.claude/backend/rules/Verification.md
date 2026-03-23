# 5. 검증 (Verification)

## 🎯 목표
코드의 정상 동작을 확인하고, 빌드 및 통합 환경에서의 오류를 방지하며, 프론트엔드 연동 준비를 마칩니다.

## ✅ 체크리스트
- [ ] **API 기능 및 로깅 점검**
  - [ ] Swagger 또는 Postman을 활용한 엔드포인트 직접 호출 및 응답(JSON 포맷) 검증
  - [ ] 예외 상황 발생 시 공통 Error Response DTO가 정상 반환되는지 확인
  - [ ] **[💡추가]** API 호출 시 콘솔에 MyBatis 쿼리 및 바인딩 파라미터가 정상적으로 로깅되는지 확인
- [ ] **보안 검증 [🔒신규 추가]**
  - [ ] 민감 데이터(비밀번호, 개인정보)가 Response JSON에 노출되지 않는지 확인
  - [ ] SQL Injection 공격 패턴 입력 시 적절한 방어 동작 확인
  - [ ] 보안 헤더(Content-Security-Policy, X-Frame-Options 등) 설정 확인
- [ ] **빌드 및 테스트 커버리지 분석**
  - [ ] Gradle Kotlin DSL (`./gradlew clean build`) 정상 통과 확인
  - [ ] **[💡추가]** 테스트 커버리지 리포트(Jacoco 등) 확인 및 목표 수치 달성 여부 점검
  - [ ] Checkstyle, SonarQube 등 코드 컨벤션/정적 분석 툴 경고 확인 및 수정
- [ ] **문서 최신화**
  - [ ] API 명세서(Swagger) 최신화 상태 확인 및 누락된 엔드포인트/파라미터 점검

---

## 📋 포트 및 환경 설정 확인

### 백엔드 포트 및 CORS 설정

| 항목 | 값 | 확인 방법 |
|------|-----|----------|
| **포트** | 8080 | `lsof -i :8080` (포트 사용 확인) |
| **CORS** | `CorsConfig.java` | 파일에서 `/api/**` + `http://localhost:3000` 설정 확인 |
| **환경 변수** | `application-dev.yml` | DB 연결 설정 및 로깅 레벨 확인 |
| **SSL 설정** | `trustServerCertificate=true` | MSSQL 자체 서명 인증서 허용 |

**확인 항목**:
- [ ] 서버 시작 로그에서 `Listening on port 8080` 메시지 확인
- [ ] `CorsConfig.java`의 내용 (Preflight 캐싱 적용 확인):

```java
@Configuration
public class CorsConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
          .