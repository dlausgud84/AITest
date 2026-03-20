# AiCodeTest Project - README

## 프로젝트 개요

**AiCodeTest**는 DIT JAVA Framework를 기반으로 구축된 Manufacturing Execution System(MES)입니다.
- **백엔드**: Java 21, Spring Boot 3.3.9, MyBatis, Gradle Kotlin DSL, SQL Server
- **프론트엔드**: Vue 3, Nuxt 3, TypeScript

---

## 문서 목차

1. [INSTALL_GUIDE.md](INSTALL_GUIDE.md) - 설치 및 실행 방법
2. [DIT_JAVA_Framework_DevGuide_v1.0.md](DIT_JAVA_Framework_DevGuide_v1.0.md) - 개발 가이드 및 규칙
3. [DEVELOPER_MANUAL.md](DEVELOPER_MANUAL.md) - 개발자 매뉴얼
4. [WORK_HISTORY.md](WORK_HISTORY.md) - 작업 이력 기록
5. [USER_MANUAL.md](USER_MANUAL.md) - 사용자 매뉴얼
6. [OPERATIONS_GUIDE.md](OPERATIONS_GUIDE.md) - 운영 가이드

---

## 빠른 시작

### 백엔드 (Spring Boot)
```bash
cd backend
./gradlew build
./gradlew bootRun
```
- 접속: http://localhost:8080

### 프론트엔드 (Nuxt 3)
```bash
cd frontend/noroo-mes-app
npm install
npm run dev
```
- 접속: http://localhost:3000

---

## 프로젝트 구조

```
AiCodeTest/
├── backend/
│   ├── apps/
│   │   └── app/                    # REST API 애플리케이션
│   │       ├── src/
│   │       │   ├── main/java/com/dit/
│   │       │   │   ├── AiCodeTestApplication.java
│   │       │   │   ├── config/
│   │       │   │   └── menu/controller/
│   │       │   └── resources/
│   │       │       ├── application.yml
│   │       │       └── mappers/
│   │       └── build.gradle.kts
│   ├── modules/
│   │   ├── common/                 # 공통 모듈 (ApiResponse, BusinessException)
│   │   ├── auth/                   # 인증 도메인 모듈
│   │   └── menu/                   # 메뉴 도메인 모듈
│   ├── settings.gradle.kts
│   └── build.gradle.kts
│
├── frontend/
│   └── noroo-mes-app/              # Nuxt 3 애플리케이션
│       ├── pages/                  # 자동 라우팅 페이지
│       │   ├── index.vue           # 대시보드 (메인)
│       │   ├── login.vue           # 로그인
│       │   ├── menu-management.vue # 메뉴 관리
│       │   └── settings.vue        # 설정
│       ├── components/             # 공통 컴포넌트
│       │   ├── AppLayout.vue       # 전체 레이아웃 래퍼
│       │   ├── Sidebar.vue         # 좌측 네비게이션
│       │   ├── PageHeader.vue      # 페이지 상단 타이틀
│       │   └── TablePagination.vue # 페이지네이션 UI
│       ├── composables/            # 재사용 가능한 로직
│       │   ├── useAuthAPI.ts       # 인증 API
│       │   ├── useMenuAPI.ts       # 메뉴 API
│       │   ├── usePagination.ts    # 페이지네이션
│       │   ├── useGridSort.ts      # 컬럼 정렬
│       │   ├── useResponsivePageSize.ts  # 반응형 페이지 크기
│       │   └── useSessionTimeout.ts      # 세션 타임아웃
│       ├── middleware/
│       │   └── auth.global.ts      # 전역 인증 미들웨어
│       ├── plugins/
│       │   └── sessionTimeout.client.ts # 세션 타임아웃 플러그인
│       ├── assets/css/             # 전역 스타일
│       ├── nuxt.config.ts
│       └── package.json
│
└── docs/                           # 프로젝트 문서
```

---

## 주요 기능

### 인증 (로그인/로그아웃)
- URL: `/login`
- SITE 선택 → 아이디/비밀번호 인증
- 토큰은 localStorage + 쿠키에 이중 저장 (SSR 미들웨어 호환)
- 세션 타임아웃: 30분 자동 만료

### 대시보드
- URL: `/`
- 로그인 후 접속 가능
- 사용자 정보 및 주요 메뉴 빠른 접근

### 메뉴 관리
- URL: `/menu-management`
- 메뉴 CRUD 기능
- 그룹별 조회, 검색, 정렬, 페이지네이션 지원
- RESTful API 연동 (`/api/menus`)

### 설정
- URL: `/settings`
- 시스템 설정 페이지

---

## API 엔드포인트

### 인증 API
```http
GET  /api/auth/sites              - MES 공장(SITE) 목록 조회
POST /api/auth/login              - 로그인 (userId, password)
```

### 메뉴 API
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

### 데이터베이스
- **타입**: Microsoft SQL Server
- **추천 설정**: `trustServerCertificate=true` (자체 서명 인증서 허용)

### 환경 변수
**프론트엔드**: `.env.development` 또는 `.env.production`
```
NUXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

---

## 포트 구성

| 서비스 | 포트 |
|--------|------|
| Spring Boot (백엔드) | 8080 |
| Nuxt Dev (프론트엔드) | 3000 |

---

## 개발 규칙

### 패키지 네이밍
```
com.dit.<domain>.<layer>
```
- `com.dit.menu.controller`
- `com.dit.menu.service`
- `com.dit.menu.persistence.mapper`
- `com.dit.menu.domain`
- `com.dit.menu.dto`

### 프론트엔드 Composable
```
use + 기능명
```
예: `useMenuAPI`, `usePagination`

---

## 도움말

- **가이드 문서**: [DIT_JAVA_Framework_DevGuide_v1.0.md](DIT_JAVA_Framework_DevGuide_v1.0.md)
- **설치 가이드**: [INSTALL_GUIDE.md](INSTALL_GUIDE.md)
- **개발자 매뉴얼**: [DEVELOPER_MANUAL.md](DEVELOPER_MANUAL.md)

---

마지막 업데이트: 2026-03-20
