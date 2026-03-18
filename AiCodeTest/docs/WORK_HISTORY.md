# 작업 이력 (Work History)

## 2026-03-18

### 프로젝트 초기화 및 기본 구조 생성

#### ✅ 완료 사항

**백엔드 (Backend)**
- Gradle Kotlin DSL 기반 멀티모듈 프로젝트 구조 생성
  - `settings.gradle.kts`: 모듈 포함 설정
  - `build.gradle.kts`: 프로젝트 의존성 관리
- 앱 모듈 구성 (`backend/apps/app/`)
  - Spring Boot 메인 애플리케이션 (`AiCodeTestApplication.java`)
  - CORS 설정 (`CorsConfig.java`)
  - `application.yml` SQL Server 연동 설정

**공통 모듈 (`modules/common/`)**
- 예외 처리 프레임워크
  - `BusinessException.java`: 비즈니스 로직 예외
  - `ErrorCode.java`: 에러 코드 enum 정의
- 공통 응답 래퍼
  - `ApiResponse<T>`: 표준 API 응답 포맷

**메뉴 도메인 모듈 (`modules/menu/`)**
- 계층형 아키텍처 구현
  - `Menu.java`: 메뉴 엔티티
  - `MenuDTO.java`: 전송용 DTO
  - `MenuMapper.java`: MyBatis Mapper 인터페이스
  - `MenuService.java`: 비즈니스 로직 및 트랜잭션 관리
  - `MenuController.java`: REST API 엔드포인트
  - `MenuMapper.xml`: SQL 맵핑

**프론트엔드 (Frontend - Nuxt 3)**
- 프로젝트 기본 설정
  - `nuxt.config.ts`: Nuxt 설정
  - `package.json`: npm 의존성
  - `app.vue`: 루트 컴포넌트

**페이지 (Pages)**
- `login.vue`: 로그인 페이지 (사용자 인증)
- `index.vue`: 대시보드 (메인 화면)
- `menu-management.vue`: 메뉴 CRUD 관리 페이지
- `settings.vue`: 설정 페이지

**공통 컴포넌트 (Components)**
- `AppLayout.vue`: 전체 레이아웃 래퍼
- `Sidebar.vue`: 좌측 네비게이션 메뉴
- `PageHeader.vue`: 페이지 상단 타이틀
- `TablePagination.vue`: 페이지네이션 UI 컴포넌트

**미들웨어 & 플러그인**
- `middleware/auth.global.ts`: 전역 인증 미들웨어 (모든 페이지 보호)
- `plugins/sessionTimeout.client.ts`: 세션 타임아웃 자동 로그아웃

**스타일시트**
- `assets/css/common.css`: 그리드/테이블 공통 스타일
- `assets/css/main.css`: 전역 스타일

**프로젝트 문서**
- `docs/README.md`: 프로젝트 개요 및 문서 색인
- `docs/INSTALL_GUIDE.md`: 설치 및 실행 가이드
- `docs/DEVELOPER_MANUAL.md`: 개발자 매뉴얼
- `docs/WORK_HISTORY.md`: 작업 이력 기록

#### 📋 생성된 파일 요약

```
AiCodeTest/
├── backend/
│   ├── settings.gradle.kts          ✅
│   ├── build.gradle.kts             ✅
│   ├── apps/app/
│   │   ├── build.gradle.kts         ✅
│   │   ├── src/main/java/com/dit/
│   │   │   ├── AiCodeTestApplication.java  ✅
│   │   │   ├── config/CorsConfig.java      ✅
│   │   │   └── menu/controller/MenuController.java  ✅
│   │   └── src/main/resources/
│   │       ├── application.yml      ✅
│   │       └── mappers/MenuMapper.xml  ✅
│   └── modules/
│       ├── common/
│       │   ├── build.gradle.kts     ✅
│       │   └── src/main/java/com/dit/common/
│       │       ├── exception/BusinessException.java  ✅
│       │       ├── exception/ErrorCode.java  ✅
│       │       └── response/ApiResponse.java  ✅
│       └── menu/
│           ├── build.gradle.kts     ✅
│           └── src/main/java/com/dit/menu/
│               ├── controller/MenuController.java  (위치 변경 필요)
│               ├── service/MenuService.java  ✅
│               ├── persistence/mapper/MenuMapper.java  ✅
│               ├── domain/Menu.java  ✅
│               └── dto/MenuDTO.java  ✅
│
├── frontend/noroo-mes-app/
│   ├── nuxt.config.ts               ✅
│   ├── package.json                 ✅
│   ├── app.vue                      ✅
│   ├── pages/
│   │   ├── login.vue                ✅
│   │   ├── index.vue                ✅
│   │   ├── menu-management.vue      ✅
│   │   └── settings.vue             ✅
│   ├── components/
│   │   ├── AppLayout.vue            ✅
│   │   ├── Sidebar.vue              ✅
│   │   ├── PageHeader.vue           ✅
│   │   └── TablePagination.vue      ✅
│   ├── middleware/
│   │   └── auth.global.ts           ✅
│   ├── plugins/
│   │   └── sessionTimeout.client.ts ✅
│   └── assets/css/
│       ├── common.css               ✅
│       └── main.css                 ✅
│
└── docs/
    ├── README.md                    ✅
    ├── INSTALL_GUIDE.md             ✅
    ├── DEVELOPER_MANUAL.md          ✅
    └── WORK_HISTORY.md              ✅
```

#### 🔧 주요 기능

**백엔드**
- REST API: GET, POST, PUT, DELETE 메뉴 관리
- MyBatis 기반 데이터베이스 연동
- SQL Server 지원 (trustServerCertificate=true)
- 공통 예외 처리 및 표준 응답 포맷
- CORS 설정 (localhost:3000 허용)

**프론트엔드**
- Nuxt 3 기반 SSR/CSR 하이브리드 렌더링
- 로그인 인증 및 세션 관리
- 메뉴 CRUD UI
- 레이아웃 시스템 및 네비게이션
- 페이지네이션 컴포넌트

#### 🎯 다음 작업 (TODO)

1. **데이터베이스 스크립트 작성**
   - `TBL_MENU` 테이블 생성 SQL
   - 샘플 데이터 insert 스크립트

2. **Composable 작성**
   - `composables/useMenuAPI.ts`: 메뉴 API 통합
   - `composables/usePagination.ts`: 페이지네이션 로직
   - `composables/useGridSort.ts`: 테이블 정렬 기능

3. **추가 페이지 구현**
   - 사용자 관리 페이지
   - 주문 관리 페이지
   - 생산 현황 페이지

4. **테스트**
   - 백엔드 API 단위 테스트
   - 프론트엔드 컴포넌트 테스트

5. **배포 준비**
   - Docker 설정
   - CI/CD 파이프라인
   - 환경별 설정 파일 (dev, staging, prod)

---

마지막 수정: 2026-03-18 
