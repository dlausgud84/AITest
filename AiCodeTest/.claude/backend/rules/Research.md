# 1. 조사 (Research)

## 🎯 목표
요구사항을 분석하고, 연관된 기존 시스템 로직과 데이터베이스 구조를 파악합니다.

## ✅ 체크리스트
- [ ] **요구사항 파악**
  - [ ] 기획/비즈니스 요구사항 분석 및 엣지 케이스(예외 상황) 도출
  - [ ] 프론트엔드와 협의할 API 스펙(Request/Response) 초안 확인
- [ ] **기존 코드 및 DB 분석**
  - [ ] `com.dit` 패키지 내 연관 도메인 및 레거시 코드 영향도 파악
  - [ ] MSSQL 관련 테이블 구조(DDL), 데이터 볼륨, 기존 관계(FK) 분석

---

## 📋 포트 및 설정 확인

| 항목 | 값 | 확인 항목 |
|------|-----|----------|
| **백엔드 포트** | 8080 | 빌드/실행 포트 |
| **프론트엔드 포트** | 3000 | 프론트엔드 연동 주소 |
| **DB 포트 (MSSQL)** | 1433 | SQL Server 기본 포트 |
| **CORS 설정 파일** | `CorsConfig.java` | 허용 오리진(`http://localhost:3000`) |
| **프로파일 파일** | `application-dev.yml` | `trustServerCertificate=true` 설정 |

**확인 항목**:
- [ ] `CorsConfig.java` 에서 `/api/**` 경로에 대해 `http://localhost:3000` 허용 설정 확인
- [ ] `application-dev.yml` 에서 MSSQL 연결 정보 및 인증서 설정 확인
- [ ] `application-prod.yml` 에서 실제 서버 데이터베이스 설정 확인