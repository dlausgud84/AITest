# 1. 조사 (Research)

## 🎯 목표
요구사항을 분석하고, 연관된 기존 시스템 로직, 데이터베이스 구조 및 인프라 설정 상태를 파악합니다.

## ✅ 체크리스트

### 1. 요구사항 및 API 스펙 파악
- [ ] 기획/비즈니스 요구사항 분석 및 엣지 케이스(예외 상황) 도출
- [ ] 프론트엔드와 협의할 API 스펙(Request/Response, URI 명명 규칙) 초안 확인
- [ ] 페이징, 정렬, 검색 조건 등 목록 조회 시 필요한 파라미터 요구사항 확인

### 2. 보안 및 권한 분석 🔒
- [ ] 해당 기능/API의 접근 권한(Role) 수준 파악 (비회원 / 일반회원 / 관리자 등)
- [ ] 민감 정보(개인정보, 비밀번호 등) 포함 여부 및 **해싱(BCrypt) / 마스킹** 처리 요구사항 확인
- [ ] **[🔒필수]** 비밀번호 필드는 반드시 BCrypt 해싱 저장 — 평문 저장 절대 금지
- [ ] **[🔒필수]** SQL Injection 위험성 평가 (동적 쿼리 사용 시 `#{}` Prepared Statement 필수, `${}` 최소화)
- [ ] **[🔒필수]** XSS(Cross-Site Scripting) 방지 요구사항 (프론트엔드 출력 시 HTML 이스케이프)
- [ ] **[🔒필수]** CSRF(Cross-Site Request Forgery) 토큰 필요 여부 확인
- [ ] **[🔒필수]** 민감 데이터 로깅 금지 — 비밀번호·토큰·개인정보를 로그에 출력하지 않도록 설계
- [ ] **[🔒필수]** 하드코딩된 비밀 값(DB 비밀번호, API Key, Secret Key) 소스코드 내 존재 여부 점검
- [ ] **[🔒추가]** OWASP Dependency-Check 또는 `./gradlew dependencyCheckAnalyze` 실행하여 알려진 취약점 라이브러리 존재 여부 확인
- [ ] 감사 로그(Audit Log) 요구사항 확인 (`CREATOR_ID`, `MODIFIER_ID`, `CREATE_DTTM`, `MODIFY_DTTM`)

### 3. 기존 코드 및 DB 분석
- [ ] `com.dit` 패키지 내 연관 도메인 및 레거시 코드 영향도 파악
- [ ] 재사용 가능한 기존 MyBatis `Mapper.xml` 쿼리 및 `ResultMap` 존재 여부 확인 (중복 개발 방지)
- [ ] MSSQL 관련 테이블 구조(DDL), 데이터 볼륨, 기존 관계(FK) 분석
- [ ] 연관된 MSSQL 저장 프로시저(Stored Procedure), 트리거(Trigger), 함수 존재 여부 파악

### 4. 외부 인터페이스 분석
- [ ] 타 시스템 또는 외부 API(결제, 알림톡, 메일 발송 등) 연동 필요 여부 확인
- [ ] 연동 시 필요한 인증 키(API Key 등) 발급 상태 확인 — **키는 `.yml` 환경변수로만 관리, 코드에 하드코딩 금지**

---

## 📋 포트 및 설정 확인

| 항목 | 값 | 확인 항목 |
|------|-----|----------|
| **백엔드 포트** | 8080 | 빌드/실행 포트 |
| **프론트엔드 포트** | 3000 | 프론트엔드 연동 주소 |
| **DB 포트 (MSSQL)** | 1433 | SQL Server 기본 포트 |
| **CORS 설정 파일** | `CorsConfig.java` | 허용 오리진 환경변수 기반 관리 여부 |
| **프로파일 파일** | `application-dev.yml` | `trustServerCertificate=true` 개발 전용 여부 |

**확인 항목**:
- [ ] `CorsConfig.java` 에서 `/api/**` 경로에 대해 허용 오리진이 환경변수(`@Value`)로 설정되어 있는지 확인
- [ ] `application-dev.yml` 에서 MSSQL 연결 정보 및 인증서 설정 확인
- [ ] **[🔒필수]** `trustServerCertificate=true` 는 `application-dev.yml`에만 존재하고, `application-prod.yml`에는 없는지 확인 (운영 환경에서는 공식 인증서 사용)
- [ ] **[🔒필수]** `application-prod.yml` 에는 실제 비밀값을 코드에 직접 쓰지 않고 환경변수(`${DB_PASSWORD}` 등) 참조 방식으로 관리하는지 확인
- [ ] 신규 기능 개발 시 `build.gradle.kts`에 추가해야 할 라이브러리(의존성) 여부 확인
- [ ] JWT 등 인증 토큰 설정이나 Secret Key가 프로파일별로 잘 분리되어 있는지 확인
