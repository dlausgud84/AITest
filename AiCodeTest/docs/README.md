# AiCodeTest Project - README

## 프로젝트 개요

**AiCodeTest**는 DIT JAVA Framework를 기반으로 구축된 Manufacturing Execution System(MES)입니다.
- **백엔드**: Java 21, Spring Boot 4.x, MyBatis, Gradle Kotlin DSL, SQL Server
- **프론트엔드**: Vue 3, Nuxt 3, TypeScript

---

## 문서 목차

1. [INSTALL_GUIDE.md](INSTALL_GUIDE.md) - 설치 및 실행 방법
2. [DIT_JAVA_Framework_DevGuide_v1.0.md](../DIT_JAVA_Framework_DevGuide_v1.0.md) - 개발 가이드 및 규칙
3. [DEVELOPER_MANUAL.md](DEVELOPER_MANUAL.md) - 개발자 매뉴얼
4. [WORK_HISTORY.md](WORK_HISTORY.md) - 작업 이력 기록

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
│   │   ├── common/                 # 공통 모듈 (예외, 응답, 상수)
│   │   └── menu/                   # 메뉴 도메인 모듈
│   ├── settings.gradle.kts
│   └── build.gradle.kts
│
├── frontend/
│   └── noroo-mes-app/              # Nuxt 3 애플리케이션
│       ├── pages/                  # 페이지 컴포넌트
│       ├── components/             # 공통 컴포넌트
│       ├── composables/            # 재사용 가능한 로직
│       ├── middleware/             # 미들웨어 (인증 등)
│       ├── plugins/                # 플러그인 (세션 타임아웃 등)
│       ├── assets/css/             # 전역 스타일
│       ├── nuxt.config.ts
│       └── package.json
│
└── docs/                           # 프로젝트 문서
```

---

## 주요 기능

### 로그인
- URL: `/login`
- 사용자명과 비밀번호로 인증
- 세션 토큰은 localStorage에 저장

### 대시보드
- URL: `/`
- 로그인 후 접속 가능
- 주요 메뉴 빠른 접근

### 메뉴 관리
- URL: `/menu-management`
- 메뉴 CRUD 기능
- RESTful API 연동

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

- **가이드 문서**: [DIT_JAVA_Framework_DevGuide_v1.0.md](../DIT_JAVA_Framework_DevGuide_v1.0.md)
- **설치 가이드**: [INSTALL_GUIDE.md](INSTALL_GUIDE.md)
- **개발자 매뉴얼**: [DEVELOPER_MANUAL.md](DEVELOPER_MANUAL.md)

---

마지막 업데이트: 2026-03-18
