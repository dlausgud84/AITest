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

## 2026-03-19

### 인증 모듈 수정 및 개발 규칙 초기 정비

#### ✅ 완료 사항

**백엔드**
- `AuthMapper.xml`: SQL 쿼리 수정
- `auth/domain/User.java`: 사용자 도메인 정리

**개발 규칙 문서**
- `.claude/CLAUDE.md`: 초기 작성
- `.claude/rules/Implementation.md`, `Plan.md`, `Research.md`, `TestCodeGeneration.md`, `Verification.md`: 백엔드/프론트엔드 공용 rules 초안 작성

---

## 2026-03-20

### 다크/라이트 테마 시스템 적용 + 개발환경 문서 정비

#### ✅ 완료 사항

**프론트엔드 — 테마 시스템**
- `composables/useTheme.ts` (신규): 다크/라이트 테마 토글 composable (localStorage 영속화)
- `assets/css/main.css`: CSS 변수 기반 테마 팔레트 (`--page-bg`, `--card-bg` 등) 전면 도입
- `assets/css/common.css`: 테마 변수 기반으로 공통 스타일 업데이트
- `pages/login.vue`: 테마 지원 및 UI 개선
- `pages/index.vue`: 대시보드 레이아웃 리팩터링
- `pages/menu-management.vue`: 테마 변수 적용
- `nuxt.config.ts`: 보안 헤더 추가 (CSP, X-Frame-Options 등), `data-theme` 초기화 스크립트 삽입
- `tsconfig.json`: 경로 별칭(`~/`) 추가

**개발 설정**
- `.claude/batch/DEV_COMMANDS.md` (신규): 서버 실행/빌드/포트 충돌 해결 명령어 가이드
- `.claude/CLAUDE.md`: 프로젝트 스택, 포트 구성, 문서 관리 규칙 전면 개정
- `.claude/settings.local.json`: Claude 권한 설정 업데이트
- `docs/README.md`: 프로젝트 개요 및 문서 색인 업데이트

**백엔드**
- `config/GlobalExceptionHandler.java`: 예외 응답 형식 개선
- `resources/application.yml`: 환경 프로파일 설정 정리
- `mappers/MenuMapper.xml`: SQL 쿼리 수정

#### 🎯 다음 작업 (당시 기준 TODO)
- 사용자 관리 페이지 구현
- 부서/권한 도메인 추가

---

## 2026-03-23

### 사용자 관리 기능 구현 + 보안 강화 + Claude Rules 전면 개정

#### ✅ 완료 사항

**개발 규칙 문서 전면 개정**
- `.claude/rules/` → `.claude/backend/rules/`, `.claude/frontend/rules/` 분리 재구성
- 백엔드/프론트엔드 rules 파일 6종씩 전면 재작성 (Research, Plan, TestCodeGeneration, Implementation, Verification, Optimization)
- `.claude/frontend/common/` 하위로 공통 컴포넌트 가이드 재구성
  - `common-button-guide.md`, `common-grid-guide.md`, `common-popup-guide.md`, `common-components-guide.md`

**백엔드 — 사용자 관리 도메인**
- `modules/user/` 신규 모듈 구성
  - `UserDomain.java`: 사용자 도메인 엔티티 (비밀번호 암호화 포함)
  - `UserDto.java`, `UserSearchDto.java`: 요청/응답 DTO
  - `UserMapper.java`: MyBatis Mapper 인터페이스
  - `UserService.java`: CRUD + 비밀번호 암호화 로직 (BCrypt)
- `UserMapper.xml`: 사용자 목록/상세/등록/수정/삭제 SQL (NB_USERS, NB_DEPARTMENTS 조인)
- `UserController.java`: REST API 엔드포인트 (`/api/users`)

**백엔드 — 보안 강화**
- `config/SecurityConfig.java` (신규): BCryptPasswordEncoder Bean 등록 (Spring Security crypto 모듈만 사용)
- `config/SecurityHeaderFilter.java` (신규): 보안 응답 헤더 필터 (HSTS, X-Frame-Options 등)

**백엔드 — 인증 모듈 개선**
- `auth/domain/User.java`, `auth/dto/LoginRequestDTO.java`: DTO 구조 개선
- `auth/persistence/mapper/AuthMapper.java`, `AuthMapper.xml`: 로그인 쿼리 수정
- `auth/service/AuthService.java`: BCrypt 비밀번호 검증 로직 적용
- `AuthController.java`: 응답 구조 통일

**백엔드 — 부서/권한/사이트 도메인 추가**
- `department/` 패키지 신규: Controller, Service, Mapper, DTO
- `role/` 패키지 신규: Controller, Service, Mapper, DTO
- `userrole/` 패키지 신규: Controller, Service, Mapper, DTO
- `mappers/DepartmentMapper.xml`, `RoleMapper.xml`, `UserRoleMapper.xml` 신규
- `mappers/SiteMapper.xml`: 사이트 목록 쿼리 추가
- `common/exception/ErrorCode.java`: 사용자 관련 에러코드 추가

**백엔드 — 빌드 설정**
- `backend/build.gradle.kts`: Spring Security crypto 모듈 의존성 추가
- `backend/modules/auth/build.gradle.kts`: 모듈 의존성 정리
- `backend/modules/user/build.gradle.kts`: user 모듈 Gradle 설정

**프론트엔드 — 사용자 관리 페이지**
- `pages/settings/basic/user.vue` (신규): 사용자 목록/등록/수정/삭제 전체 화면
  - BaseGrid + BaseModal + 공통 폼 컴포넌트 조합
  - 부서 선택 팝업, 사이트 선택 팝업, 권한 할당 UI 포함

**프론트엔드 — 팝업 컴포넌트 신규**
- `components/DepartmentPickerPopup.vue` (신규): 부서 선택 팝업
- `components/SitePickerPopup.vue` (신규): 사이트 선택 팝업

**프론트엔드 — Composable 신규**
- `composables/useDepartmentAPI.ts` (신규): 부서 목록 API
- `composables/useRoleAPI.ts` (신규): 권한 목록 API
- `composables/useUserRoleAPI.ts` (신규): 사용자 권한 할당/조회 API

**프론트엔드 — 기타 수정**
- `composables/useUserAPI.ts`: 사용자 CRUD API 전면 보강
- `middleware/auth.global.ts`: 인증 미들웨어 개선 (리다이렉트 로직 정교화)
- `components/Sidebar.vue`: 사용자 관리 메뉴 항목 추가
- `components/LanguagePickerPopup.vue`: 언어 선택 팝업 UI 개선
- `start-dev.bat`: 개발 서버 동시 실행 스크립트 개선

#### 🐛 오류 수정

| 파일 | 오류 유형 | 내용 |
|------|----------|------|
| `composables/useDepartmentAPI.ts` | TypeScript 타입 오류 | `fetchDepartmentList` 파라미터 `string` → `string \| null` 수정. `DepartmentPickerPopup`의 `props.siteId`(`string \| null \| undefined`)를 전달할 때 타입 불일치 발생 |
| `nuxt.config.ts` | 설정 무효 | `vite.build.terserOptions` 제거 → `vite.esbuild.drop`으로 교체. Vite 기본 minifier가 esbuild이므로 terserOptions 미작동 (terser 패키지 미설치) |
| `application-dev.yml` | 데드 컨피그 | `spring.jpa.hibernate.ddl-auto`, `spring.jpa.show-sql` 제거. 프로젝트가 MyBatis 스택이라 JPA 설정이 불필요하게 존재 |

#### 📋 변경된 주요 파일

```
backend/
├── apps/app/src/main/java/com/dit/
│   ├── config/
│   │   ├── SecurityConfig.java           ✅ 신규
│   │   └── SecurityHeaderFilter.java     ✅ 신규
│   ├── department/                       ✅ 신규 패키지
│   ├── role/                             ✅ 신규 패키지
│   └── userrole/                         ✅ 신규 패키지
├── apps/app/src/main/resources/mappers/
│   ├── DepartmentMapper.xml              ✅ 신규
│   ├── RoleMapper.xml                    ✅ 신규
│   └── UserRoleMapper.xml                ✅ 신규
└── modules/user/                         ✅ 신규 모듈

frontend/noroo-mes-app/
├── components/
│   ├── DepartmentPickerPopup.vue         ✅ 신규
│   └── SitePickerPopup.vue               ✅ 신규
├── composables/
│   ├── useDepartmentAPI.ts               ✅ 신규 (타입 오류 수정)
│   ├── useRoleAPI.ts                     ✅ 신규
│   └── useUserRoleAPI.ts                 ✅ 신규
└── pages/settings/basic/
    └── user.vue                          ✅ 신규 (사용자 관리 전체 화면)
```

#### 🎯 다음 작업 (TODO)

1. **데이터베이스 스크립트 확인**
   - `NB_DEPARTMENTS`, `NB_ROLES`, `NB_USER_ROLES` 테이블 스키마 확인
2. **사용자 관리 페이지 추가 기능**
   - 비밀번호 초기화 기능
   - 사용자 활성화/비활성화 토글
3. **권한 관리 페이지 구현** (`pages/settings/basic/role.vue`)
4. **통합 테스트**
   - 사용자 CRUD API 테스트
   - BCrypt 비밀번호 검증 테스트

---

마지막 수정: 2026-03-23
