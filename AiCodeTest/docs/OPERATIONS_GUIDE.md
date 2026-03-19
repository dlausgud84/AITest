# 운영 가이드

문서 버전: 1.0
작성일: 2026-03-19

---

## 목차

1. [서비스 기동 순서](#1-서비스-기동-순서)
2. [서비스 중지 순서](#2-서비스-중지-순서)
3. [배포 방법](#3-배포-방법)
4. [로그 확인](#4-로그-확인)
5. [장애 대응](#5-장애-대응)

---

## 1. 서비스 기동 순서

### 1) SQL Server 기동 확인

```bash
# SQL Server 서비스 상태 확인 (Windows)
sc query MSSQLSERVER
```

SQL Server가 실행 중인지 확인 후 다음 단계를 진행합니다.

### 2) 백엔드 (Spring Boot) 기동

```bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=dev'
```

- 기동 포트: **8080**
- 정상 기동 확인: `http://localhost:8080/actuator/health` → `{"status":"UP"}`

### 3) 프론트엔드 (Nuxt) 기동

```bash
cd frontend/noroo-mes-app
npm run dev
```

- 기동 포트: **3000**
- 정상 기동 확인: 브라우저에서 `http://localhost:3000` 접속

---

## 2. 서비스 중지 순서

1. 프론트엔드 터미널에서 `Ctrl + C`
2. 백엔드 터미널에서 `Ctrl + C`

---

## 3. 배포 방법

### 백엔드 빌드 및 배포

```bash
# 빌드
cd backend
./gradlew build -x test

# 실행 파일 위치
# build/libs/*.jar

# 운영 환경 실행
java -jar build/libs/app.jar --spring.profiles.active=prod
```

### 프론트엔드 빌드 및 배포

```bash
cd frontend/noroo-mes-app

# 빌드
npm run build

# 운영 실행
node .output/server/index.mjs
```

### 환경 변수 설정

| 환경   | 파일                       |
|--------|----------------------------|
| 개발   | `.env.development`         |
| 운영   | `.env.production`          |

운영 배포 전 `.env.production`의 `NUXT_PUBLIC_API_BASE_URL` 값을 실제 서버 주소로 변경합니다.

---

## 4. 로그 확인

### 백엔드 로그

```bash
# 콘솔 출력 또는 로그 파일 (application.yml 설정에 따름)
tail -f logs/app.log
```

### 프론트엔드 로그

- 브라우저 개발자 도구 → Console 탭에서 확인

---

## 5. 장애 대응

### 백엔드 응답 없음

| 확인 항목           | 방법                                           |
|---------------------|------------------------------------------------|
| 프로세스 실행 여부  | `netstat -an \| findstr 8080`                 |
| DB 연결 오류        | `application.yml`의 DB URL/계정 정보 확인      |
| 포트 충돌           | 8080 포트 사용 중인 프로세스 종료 후 재기동    |

### 프론트엔드 접속 불가

| 확인 항목           | 방법                                           |
|---------------------|------------------------------------------------|
| 프로세스 실행 여부  | `netstat -an \| findstr 3000`                 |
| API 연결 오류       | `.env` 파일의 `NUXT_PUBLIC_API_BASE_URL` 확인 |
| CORS 오류           | 백엔드 `CorsConfig.java`의 허용 오리진 확인   |

### DB 연결 오류

- 연결 문자열에 `trustServerCertificate=true` 포함 여부 확인
- SQL Server 서비스 실행 상태 확인
- 방화벽에서 1433 포트 허용 여부 확인

### 긴급 롤백

```bash
# 이전 JAR 파일로 재기동
java -jar build/libs/app-이전버전.jar --spring.profiles.active=prod
```
