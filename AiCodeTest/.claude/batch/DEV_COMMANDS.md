# 개발 명령어 가이드

> 프로젝트 서버 실행 및 빌드 명령어 모음
> 포트: 백엔드 `8080` | 프론트엔드 `3000`

---

## 개발 서버 동시 실행 (Windows)

```bash
start-dev.bat
```

포트 8080/3000의 기존 프로세스를 종료 후 별도 터미널에서 서버를 실행한다.

> **위치**: 프로젝트 루트 (`AiCodeTest/start-dev.bat`)

---

## 백엔드 (Spring Boot · 포트 8080)

```bash
cd backend

# 개발 서버 실행
./gradlew :apps:app:bootRun

# 전체 빌드
./gradlew build

# 특정 모듈만 빌드
./gradlew :modules:auth:build
./gradlew :modules:menu:build

# 테스트 실행
./gradlew test
./gradlew :modules:<domain>:test
```

| 명령어 | 설명 |
|--------|------|
| `:apps:app:bootRun` | 개발 서버 실행 (포트 8080, 핫리로드 포함) |
| `build` | 전체 빌드 + 테스트 |
| `test` | 전체 테스트 실행 |
| `:modules:<domain>:test` | 특정 도메인 모듈 테스트만 실행 |

---

## 프론트엔드 (Nuxt 3 · 포트 3000)

```bash
cd frontend/noroo-mes-app

# 패키지 설치
npm install

# 개발 서버 실행
npm run dev

# 프로덕션 빌드
npm run build

# 빌드 결과 미리보기
npm run preview

# 타입 체크
npm run typecheck
```

| 명령어 | 설명 |
|--------|------|
| `npm install` | 의존성 설치 (최초 또는 package.json 변경 후) |
| `npm run dev` | 개발 서버 실행 (포트 3000, HMR 포함) |
| `npm run build` | 프로덕션 빌드 (`.output/` 폴더 생성) |
| `npm run preview` | 빌드 결과물 로컬 미리보기 |
| `npm run typecheck` | TypeScript 타입 오류 전체 검사 |

---

## 포트 충돌 해결 (Windows)

```bash
# 특정 포트를 사용 중인 프로세스 확인
netstat -ano | findstr :8080
netstat -ano | findstr :3000

# PID로 프로세스 강제 종료
taskkill /F /PID <PID번호>
```

---

## 환경 변수 파일 위치

| 파일 | 용도 |
|------|------|
| `frontend/noroo-mes-app/.env.development` | 개발 환경 (`NUXT_PUBLIC_API_BASE_URL=http://localhost:8080`) |
| `frontend/noroo-mes-app/.env.production` | 운영 환경 (실제 서버 URL) |
| `backend/apps/app/src/main/resources/application-dev.yml` | 백엔드 개발 프로파일 |
| `backend/apps/app/src/main/resources/application-prod.yml` | 백엔드 운영 프로파일 |
