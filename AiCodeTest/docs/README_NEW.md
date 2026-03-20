> **이 파일은 README.md로 통합되었습니다. `docs/README.md`를 참조하세요.**

# AiCodeTest Project - README

## 프로젝트 개요

**AiCodeTest**는 DIT JAVA Framework를 기반으로 구축된 Manufacturing Execution System(MES)입니다.

### 주요 특징
- **계층형 아키텍처**: Controller → Service → Mapper 패턴 준수
- **멀티모듈 구조**: 도메인별 독립적 모듈 관리
- **하이브리드 렌더링**: Nuxt 3의 SSR/CSR 병행 지원
- **공통 모듈**: 페이지네이션, 정렬, 반응형 UI 통합
- **SQL Server 연동**: MyBatis ORM 기반 데이터 접근

### 기술 스택
- **백엔드**: Java 21, Spring Boot 4.x, MyBatis, Gradle Kotlin DSL
- **프론트엔드**: Vue 3, Nuxt 3, TypeScript
- **데이터베이스**: Microsoft SQL Server (NOROO_MES)

---

## 문서 목차

1. **[INSTALL_GUIDE.md](INSTALL_GUIDE.md)** - 설치 및 실행 방법
2. **[DEVELOPER_MANUAL.md](DEVELOPER_MANUAL.md)** - 개발자 매뉴얼 및 가이드라인
3. **[WORK_HISTORY.md](WORK_HISTORY.md)** - 프로젝트 변경 이력
4. **[DIT_JAVA_Framework_DevGuide_v1.0.md](../DIT_JAVA_Framework_DevGuide_v1.0.md)** - DIT 표준 개발 가이드 (필독)

---

## 빠른 시작

### 1. 사전 요구사항 설치
- **JDK 21**: Java 개발 환경
- **Node.js 18+**: JavaScript 런타임
- **SQL Server 2019+**: 데이터베이스
- **Git**: 버전 관리

### 2. 데이터베이스 초기화
```bash
# SQL Server에서 실행
sqlcmd -S localhost -U sa -i docs/db_init.sql
```

### 3. 백엔드 실행
```bash
cd backend
./gradlew bootRun
```
✓ http://localhost:8080 에서 실행됨

### 4. 프론트엔드 실행
```bash
cd frontend/noroo-mes-app
npm install
npm run dev
```
✓ http://localhost:3000 에서 실행됨

### 5. 브라우저 접속
```
http://localhost:3000
```

---

## 프로젝트 구조

```
AiCodeTest/
├── backend/                          # Spring Boot 백엔드
│   ├── apps/
│   │   └── app/                     # REST API 애플리케이션
│   │       ├── src/main/java/com/dit/
│   │       ├── src/main/resources/
│   │       │   ├── application.yml
│   │       │   └── mappers/
│   │       └── build.gradle.kts
│   └── modules/
│       ├── common/                  # 공통 모듈 (예외, 응답)
│       └── menu/                    # 메뉴 도메인 모듈
│
├── frontend/noroo-mes-app/          # Nuxt 3 프론트엔드
│   ├── pages/                       # 자동 라우팅 페이지
│   ├── components/                  # 재사용 컴포넌트
│   ├── composables/                 # Hooks (업데이트 2026-03-18)
│   │   ├── usePagination.ts         # 페이지네이션
│   │   ├── useGridSort.ts            # 테이블 정렬
│   │   ├── useResponsivePageSize.ts # 반응형
│   │   └── useMenuAPI.ts            # API 통신
│   ├── middleware/
│   ├── plugins/
│   └── assets/css/
│
└── docs/                            # 프로젝트 문서
    ├── README.md
    ├── INSTALL_GUIDE.md
    ├── DEVELOPER_MANUAL.md
    ├── WORK_HISTORY.md
    └── db_init.sql                  # DB 초기화 스크립트 (신규)
```

---

## 주요 기능

### 🔐 인증 및 세션
- 로그인/로그아웃 기능
- 자동 세션 타임아웃 (30분)
- 전역 인증 미들웨어

### 📋 메뉴 관리
- 조회, 생성, 수정, 삭제
- 그룹별 조회 및 검색
- 정렬 및 페이지네이션

#### API 엔드포인트
```http
GET    /api/menus                  - 모든 메뉴 조회
GET    /api/menus/{menuId}         - 메뉴 상세 조회
GET    /api/menus/group/{group}    - 그룹별 메뉴 조회
GET    /api/menus/search?keyword=  - 메뉴 검색
POST   /api/menus                  - 메뉴 생성
PUT    /api/menus/{menuId}         - 메뉴 수정
DELETE /api/menus/{menuId}         - 메뉴 삭제
```

---

## 환경 설정

### 데이터베이스 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=NOROO_MES;...
    username: sa
    password: YourPassword123!
```

### 프론트엔드 (.env)
```bash
NUXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

---

## 포트 설정

| 서비스 | 포트 |
|--------|------|
| Spring Boot | 8080 |
| Nuxt Dev | 3000 |

---

## 패키지 네이밍 규칙

```
com.dit.<domain>.<layer>
```

예시:
- `com.dit.menu.controller`
- `com.dit.menu.service`
- `com.dit.menu.persistence.mapper`
- `com.dit.menu.domain`
- `com.dit.menu.dto`

---

## 2026-03-18 업데이트 사항

✅ **Composables 작성** (4개)
- `usePagination.ts`: 대용량 데이터 페이지 단위 표시
- `useGridSort.ts`: 테이블 컬럼 정렬 (자동 타입 인식)
- `useResponsivePageSize.ts`: 화면 크기별 페이지 크기 조정
- `useMenuAPI.ts`: 메뉴 API 통신

✅ **백엔드 업데이트**
- Menu 엔티티: MENU_GROUP, MENU_TYPE, PAGE_ID, ICON_IMAGE 컬럼 추가
- MenuMapper: 그룹별 조회, 검색 기능 추가
- MenuService: 검증 로직 강화
- MenuController: 6개 엔드포인트 구현
- MenuDTO: 확장된 필드 지원

✅ **프론트엔드 업데이트**
- menu-management.vue: 가이드 통합 예시 반영
  - 검색, 정렬, 페이지네이션 통합
  - 모달 폼 개선
  - 반응형 디자인

✅ **데이터베이스**
- NW_MENUS 테이블 스크립트 (docs/db_init.sql)
- 초기 메뉴 데이터 자동 생성

---

## 도움말

- **가이드 문서**: [DIT_JAVA_Framework_DevGuide_v1.0.md](../DIT_JAVA_Framework_DevGuide_v1.0.md)
- **설치 가이드**: [INSTALL_GUIDE.md](INSTALL_GUIDE.md)
- **개발자 매뉴얼**: [DEVELOPER_MANUAL.md](DEVELOPER_MANUAL.md)

---

**마지막 업데이트**: 2026-03-18  
**버전**: 1.0.0
