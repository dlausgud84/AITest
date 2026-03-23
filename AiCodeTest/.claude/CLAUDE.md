# CLAUDE.md
This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 스킬 사용 지침 (Claude Code Skills)

이 프로젝트에서 코드 작업 시 아래 스킬을 상황에 맞게 반드시 적용한다.

| 상황 | 적용 스킬 | 참조 파일 |
|------|-----------|----------|
| `.vue` 파일 작성/수정 시 | `vue-best-practices` | `.claude/frontend/rules/Implementation.md` |
| Nuxt 파일 작업 시 (pages/, composables/, plugins/, middleware/, nuxt.config.ts) | `nuxt` | `.claude/frontend/rules/Implementation.md` |
| Vue Composition API 작성 시 | `vue` | `.claude/frontend/rules/Implementation.md` |
| 라우팅/미들웨어 작업 시 | `vue-router-best-practices` | `.claude/frontend/rules/Implementation.md` |
| composable 신규 작성 전 | `vueuse` | `.claude/frontend/rules/Implementation.md` |
| 기존 composable 개선 시 | `vueuse-functions` | `.claude/frontend/rules/Implementation.md` |
| 테스트 코드 작성 시 | `vitest` + `vue-testing-best-practices` | `.claude/frontend/rules/TestCodeGeneration.md` |
| UI/UX 검토 요청 시 | `web-design-guidelines` | `.claude/frontend/rules/Plan.md` |

**적용 제외 스킬**: nuxt-ui, nuxt-content, reka-ui, unocss, pinia (composable 패턴 사용), ts-library, turborepo, pnpm 등

---

## 참조 문서 자동 학습 규칙

세션 시작 시 아래 폴더의 **모든 `.md` 파일**을 꼼꼼히 읽고 내용을 완전히 학습한 뒤 작업을 시작한다.

| 폴더 | 용도 | 학습 우선순위 |
|------|------|--------------|
| `.claude/backend/rules/` | 개발 단계별 세부 규칙 (조사/계획/테스트/구현/검증/최적화) | **필수 · 최우선** |
| `.claude/frontend/rules/` | 개발 단계별 세부 규칙 (조사/계획/테스트/구현/검증/최적화) | **필수 · 최우선** |
| `.claude/batch/` | 실행 명령어, 스크립트 가이드 | **필수** |
| `.claude/frontend/common/` | 공통 컴포넌트 상세 가이드 (버튼/그리드/폼/팝업) | 프론트 작업 시 필수 |

> **학습 원칙**: 파일을 "읽은 척"하지 않는다. 각 파일의 규칙이 현재 작업에 어떻게 적용되는지 판단하여 실제로 반영한다.

---

## 개발 단계별 frontend Rules

frontend 작업은 아래 순서로 진행한다. 각 단계의 상세 규칙은 `.claude/frontend/rules/` 폴더를 참조한다.

| 단계 | 파일 | 내용 |
|---|---|---|
| 1. 조사 | `.claude/frontend/rules/Research.md` | 요구사항 분석, API 연동 조사, 기존 리소스 파악 |
| 2. 계획 | `.claude/frontend/rules/Plan.md` | 라우팅 및 뷰 설계, 컴포넌트 설계, 상태 관리 및 데이터 패칭 설계 |
| 3. 테스트 작성 | `.claude/frontend/rules/TestCodeGeneration.md` | Composables 단위 테스트, UI 컴포넌트 단위 테스트, Edge Case 커버리지 |
| 4. 구현 | `.claude/frontend/rules/Implementation.md` | 타입 정의, 로직 구현, UI 및 라우팅 구현, UX 처리 및 SEO |
| 5. 검증 | `.claude/frontend/rules/Verification.md` | 기능 검증, 코드 품질 및 정적 분석, 빌드 검증 |
| 6. 최적화 | `.claude/frontend/rules/Optimization.md` | 렌더링 성능 및 청크 최적화, 에셋 최적화, 웹 접근성 |

## 개발 단계별 backend Rules

backend 작업은 아래 순서로 진행한다. 각 단계의 상세 규칙은 `.claude/backend/rules/` 폴더를 참조한다.

| 단계 | 파일 | 내용 |
|---|---|---|
| 1. 조사 | `.claude/backend/rules/Research.md` | 요구사항 파악, 기존 코드 및 DB 분석 |
| 2. 계획 | `.claude/backend/rules/Plan.md` | API 및 DTO 설계, DB 및 MyBatis 설계, 도메인 및 서비스 설계 |
| 3. 테스트 작성 | `.claude/backend/rules/TestCodeGeneration.md` | 단위 테스트, 통합 테스트 |
| 4. 구현 | `.claude/backend/rules/Implementation.md` | Data Access 계층 구현, Business 로직 계층 구현, Presentation 계층 구현, 공통 처리 |
| 5. 검증 | `.claude/backend/rules/Verification.md` | API 기능 점검, 빌드 및 정적 분석, 문서 최신화 |
| 6. 최적화 | `.claude/backend/rules/Optimization.md` | MSSQL 쿼리 튜닝, 자원 관리 및 보안, 로깅 |


## 기본 규칙
**단순한 코드 생성기가 아닌, 현업에서 함께 문제를 해결하는 전문 개발 파트너로서 동작한다.**

1. 사용자는 단순한 정답이 아닌, 실질적인 분석과 옳고 그름, 적용/미적용을 판단하기 위해 시스템 수준의 설명을 원한다.
2. 질문은 대부분 구조 전체, 실행 흐름, 예외 상황, 확장성까지 포함되며 단편적이지 않다.
3. 모든 코드나 설정 변경 시 다음 흐름으로 설명한다.
   - **원리**: 해당 코드나 개념이 어떤 역할을 하는지?
   - **이유**: 왜 수정이 필요한지, 어떤 목적을 위한 것인지?
   - **수정 내용**: 구체적으로 어떤 변경이 필요한지?
   - **결과**: 변경 후 어떤 변화가 생기며, 테스트 포인트는 무엇인지?
4. 사용자는 프로젝트에서 사용하는 웹 언어에 대한 이해가 초급 수준이다. 따라서 컴포넌트 구조, 상태 관리, 라우팅 등의 개념이 함축되는 경우 충분한 설명과 예시로 이해를 돕는다.
5. 코드나 시스템 설명 시 다음 순서를 따른다.
   - **표 요약** (생략 가능, 있으면 좋음)
   - **단계별 흐름 요약**
   - **결론 요약**
   - **대안 제시**
6. 항상 "지금 해야 할 것"과 "그 다음 단계"를 분리해서 안내한다.
7. 실패 가능성이 있다면 반드시 그 원인과 우회 경로(Plan A/B/C)를 구조적으로 설명해야 하며, 단순히 "안됩니다"라고 말하지 않는다.
8. 사용자의 질문은 때때로 비판적일 수 있으나, 이는 실제 실패를 줄이기 위한 전략적 사고이므로 감정적으로 반응하지 말고, 철저히 기술적 관점에서 사고해야 한다.
9. 모든 작업이 완료시 " 
========== 모든 작업이 완료 되었습니다 ========== 
" 표기 한다.

## 언어 규칙
**모든 응답은 반드시 한국어로 작성한다.**
- 진행 메시지 (예: "파일을 수정합니다...", "빌드를 실행합니다...") → 한국어
- 처리 결과 (예: "수정 완료", "오류 발생", "적용되었습니다") → 한국어
- 코드 설명, 오류 분석, 제안 내용 → 한국어
- 코드 내 주석 → 한국어 (단, 기존 영문 주석은 유지 가능)

## 프로젝트 스택 컨텍스트
- **백엔드**: Java 21, Spring Boot 3.3.9, MyBatis, Gradle Kotlin DSL, SQL Server
- **프론트엔드**: Vue 3, Nuxt 3, TypeScript
- **패키지 베이스**: `com.dit`
## 개발 명령어

> 상세 명령어는 `.claude/batch/DEV_COMMANDS.md` 를 반드시 읽고 적용한다.

| 파일 | 내용 |
|------|------|
| `batch/DEV_COMMANDS.md` | 서버 실행, 빌드, 테스트, 포트 충돌 해결, 환경변수 파일 위치 |

### 빠른 참조

| 상황 | 명령어 |
|------|--------|
| 서버 동시 실행 | `start-dev.bat` (루트 폴더) |
| 백엔드 개발 서버 | `cd backend && ./gradlew :apps:app:bootRun` |
| 프론트엔드 개발 서버 | `cd frontend/noroo-mes-app && npm run dev` |

## 프론트엔드 공통 컴포넌트 가이드

프론트엔드 개발 시 아래 가이드 파일과 `frontend/rules/Plan.md`를 반드시 참조하여 구현합니다.

| 가이드 파일 | 대상 컴포넌트 | 주요 내용 |
|---|---|---|
| [`.claude/frontend/common/common-button-guide.md`](.claude/frontend/common/common-button-guide.md) | `BaseButton.vue` | 9가지 btnType, Props, SVG 아이콘 |
| [`.claude/frontend/common/common-grid-guide.md`](.claude/frontend/common/common-grid-guide.md) | `BaseGrid.vue` | 3가지 모드 (페이지네이션/내부스크롤/무한스크롤), 컬럼 리사이즈 |
| [`.claude/frontend/common/common-components-guide.md`](.claude/frontend/common/common-components-guide.md) | Form 컴포넌트 | TextBox, ToggleGroup, ComboBox, SelectBox |
| [`.claude/frontend/common/common-popup-guide.md`](.claude/frontend/common/common-popup-guide.md) | `BaseModal.vue` | 4가지 모드, 5가지 크기, alert 타입 |

## 포트 구성

- **백엔드 (Spring Boot)**: 8080
- **프론트엔드 (Nuxt)**: 3000
- **MES 브릿지 (Nslon.JAVA_Bridge)**: 8290 (또는 18080)
- **MES 서버 (.NET WCF)**: 60100/WebProxy.asmx

## 환경 변수 규칙

- 프론트엔드 환경변수: `NUXT_PUBLIC_API_BASE_URL=http://localhost:8080`
- 환경별 파일: `.env.development`, `.env.production`
- 백엔드 프로파일: `application-dev.yml`, `application-prod.yml` 분리

## 프로젝트 디렉토리 구조

### 백엔드 구조
```
backend/
├── apps/
│   └── app/                            # 실행 앱 (Spring Boot)
│       └── src/main/
│           ├── java/com/dit/
│           │   ├── AiCodeTestApplication.java  # 메인 진입점
│           │   ├── config/
│           │   │   ├── CorsConfig.java         # CORS 설정
│           │   │   └── GlobalExceptionHandler.java
│           │   ├── auth/controller/
│           │   └── menu/controller/
│           └── resources/
│               ├── application.yml
│               ├── application-dev.yml
│               ├── application-prod.yml
│               └── mappers/
│                   ├── AuthMapper.xml
│                   └── MenuMapper.xml
└── modules/                            # 도메인 모듈
    ├── common/                         # 공통 모듈 (ApiResponse, BusinessException)
    ├── auth/                           # 인증 도메인
    └── menu/                           # 메뉴 도메인
        └── src/main/java/com/dit/menu/
            ├── controller/
            ├── service/
            ├── persistence/mapper/
            ├── domain/
            └── dto/
```

### 프론트엔드 구조
```
frontend/noroo-mes-app/
├── app.vue                         # 루트 컴포넌트
├── nuxt.config.ts
├── pages/                          # 자동 라우팅 페이지 (구현 완료)
│   ├── index.vue                   # 메인(대시보드)
│   ├── login.vue                   # 로그인
│   ├── menu-management.vue         # 메뉴 관리
│   └── settings.vue                # 설정
├── components/                     # 공통 컴포넌트
│   ├── AppLayout.vue               # 전체 레이아웃 래퍼
│   ├── Sidebar.vue                 # 좌측 네비게이션
│   ├── PageHeader.vue              # 페이지 상단 타이틀
│   └── TablePagination.vue         # 페이지네이션 UI
├── composables/                    # 재사용 가능한 로직
│   ├── useMenuAPI.ts
│   ├── usePagination.ts
│   ├── useGridSort.ts
│   ├── useResponsivePageSize.ts
│   └── useSessionTimeout.ts
├── middleware/
│   └── auth.global.ts              # 전역 인증 미들웨어 (모든 페이지에 적용)
├── plugins/
│   └── sessionTimeout.client.ts   # 세션 타임아웃 플러그인 (클라이언트 전용)
└── assets/css/
    ├── common.css                  # 그리드/테이블 공통 스타일
    └── main.css                    # 전역 스타일
```

## 인증 및 세션 규칙

> 상세 구현 규칙은 `.claude/frontend/rules/Implementation.md` 참조

- 전역 인증: `middleware/auth.global.ts`에서 모든 페이지 접근 시 인증 여부 검사
- 세션 타임아웃: `plugins/sessionTimeout.client.ts` + `composables/useSessionTimeout.ts` 조합으로 관리
- 로그인 페이지: `pages/login.vue` (인증 미들웨어 예외 처리 필요)

## CORS 설정 위치

> 상세 규칙은 `.claude/backend/rules/Implementation.md` 참조

- 백엔드 CORS 설정 파일: `backend/src/main/java/com/dit/config/CorsConfig.java`
- 허용 경로: `/api/**`, 허용 오리진: `http://localhost:3000`

## 프로젝트 문서 관리 규칙

모든 문서는 `docs/` 폴더에 작성하고 유지한다.

### 필수 작성 문서 목록

| 파일명 | 용도 | 작성 시점 |
|---|---|---|
| `docs/DIT_JAVA_Framework_DevGuide_v1.0.md` | 개발환경 가이드 (스택, 네이밍, 아키텍처 기준) | 프로젝트 초기 |
| `docs/INSTALL_GUIDE.md` | 신규 PC 환경 설정 및 실행 방법 | 프로젝트 초기 |
| `docs/DEVELOPER_MANUAL.md` | 백엔드/프론트엔드 코드 작성 패턴, 계층 역할, API 테스트 방법 | 개발 진행 중 |
| `docs/USER_MANUAL.md` | 화면별 사용법, 로그인/로그아웃, 메뉴 설명 | 기능 완성 후 |
| `docs/OPERATIONS_GUIDE.md` | 서비스 기동/중지 순서, 배포 방법, 장애 대응 | 운영 전 |
| `docs/WORK_HISTORY.md` | 일자별 작업 내역 기록 (기능 추가/수정/버그 이력) | 작업할 때마다 |
| `docs/README.md` | 프로젝트 개요, 전체 문서 색인 | 프로젝트 초기 |

### 선택 작성 문서 (기능별)

| 파일명 | 용도 |
|---|---|
| `docs/SESSION_TIMEOUT_README.md` | 세션 타임아웃 기능 동작 원리 및 설정값 설명 |
| `docs/MENU_QUERY_TROUBLESHOOTING.md` | 특정 기능의 쿼리 오류 원인 및 해결책 정리 |

### 문서 작성 규칙

- 문서명은 **대문자 스네이크케이스** + `.md` 확장자 사용
- 각 문서 상단에 `# 제목`, `문서 버전`, `작성일`, `목차` 포함
- `WORK_HISTORY.md`는 날짜별(`## YYYY-MM-DD`) 섹션으로 구분하여 누적 기록
- **`WORK_HISTORY.md` 작성 시점**: 매일 업무 시작 시 **전날 작업 내용**을 정리하여 기록
  - 당일 작업 중에는 메모 수준으로 임시 기록 후, 다음날 아침 정제하여 확정
  - 작업 완료 직후 기록이 어려운 경우 당일 업무 종료 전에 정리해도 무방
  - 기록 항목: 구현/수정한 기능, 해결한 버그, 변경된 파일, 미완료 사항(TODO)
- 기능 단위로 트러블슈팅 내용이 길어지면 별도 파일로 분리 (예: `MENU_QUERY_TROUBLESHOOTING.md`)
- **문서 편집 시 등폭(Monospace) 폰트 기준으로 작성** (권장 폰트: D2Coding, Consolas, Courier New)
  - 테이블 컬럼 구분자(`|`) 정렬을 등폭 폰트 기준으로 맞춤
  - 코드 블록(` ``` `)은 반드시 등폭 폰트로 표시되므로 들여쓰기 스페이스 기준으로 맞춤
  - VS Code 사용 시 `"editor.fontFamily": "D2Coding, Consolas, monospace"` 설정 권장
