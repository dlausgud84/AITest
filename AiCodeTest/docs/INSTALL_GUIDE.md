# 설치 및 실행 가이드

## 1. 필수 프로그램 설치

### 1.1 Java & Spring Boot
- **JDK 21**: https://www.oracle.com/java/technologies/downloads/
- 설치 후 환경변수 `JAVA_HOME` 설정 필수

### 1.2 Gradle
- JDK 설치 시 함께 포함 (gradle wrapper 사용 가능)

### 1.3 Node.js & npm
- **Node.js 18+**: https://nodejs.org/
- npm은 Node.js 설치 시 자동 포함

### 1.4 SQL Server
- **SQL Server Express** 또는 설치된 SQL Server 인스턴스
- 필요한 데이터베이스: `AiCodeTest`
- 사용자: `sa`

### 1.5 Git
- 버전 관리 도구: https://git-scm.com/

---

## 2. 프로젝트 설정

### 2.1 프로젝트 클론 (또는 다운로드)
```powershell
git clone <repository-url>
cd AiCodeTest
```

### 2.2 데이터베이스 초기화

**SQL Server에서 실행**:
```sql
-- 데이터베이스 생성
CREATE DATABASE [AiCodeTest];

-- 사용할 데이터베이스로 전환
USE [AiCodeTest];

-- 메뉴 테이블 생성
CREATE TABLE [dbo].[TBL_MENU](
    [menu_id] [bigint] IDENTITY(1,1) PRIMARY KEY,
    [menu_name] [nvarchar](100) NOT NULL,
    [menu_url] [nvarchar](200) NOT NULL,
    [sort_order] [int] NOT NULL,
    [creator_id] [nvarchar](50) NULL,
    [create_dttm] [nvarchar](14) NULL,
    [modifier_id] [nvarchar](50) NULL,
    [modify_dttm] [nvarchar](14) NULL
);
```

---

## 3. 백엔드 실행

### 3.1 설정 파일 수정

`backend/apps/app/src/main/resources/application.yml` 수정:
```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=AiCodeTest;encrypt=true;trustServerCertificate=true
    username: sa
    password: your-password  # 실제 SQL Server 비밀번호로 변경
```

### 3.2 빌드 및 실행

**PowerShell에서**:
```powershell
cd backend
./gradlew clean build
./gradlew bootRun
```

또는

```powershell
cd backend
./gradlew bootRun
```

✅ 정상 실행 메시지:
```
...
Tomcat started on port(s): 8080
Started AiCodeTestApplication in X seconds
```

### 3.3 API 테스트
```
GET http://localhost:8080/api/menus
```

---

## 4. 프론트엔드 실행

### 4.1 의존성 설치
```powershell
cd frontend/noroo-mes-app
npm install
```

### 4.2 개발 서버 실행
```powershell
npm run dev
```

✅ 정상 실행 메시지:
```
LISTEN  http://localhost:3000
```

### 4.3 브라우저 접속
```
http://localhost:3000
```

---

## 5. 환경 변수 설정

### 5.1 프론트엔드 환경변수

`frontend/noroo-mes-app/.env.development` 생성:
```
NUXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

### 5.2 프로덕션 환경 (선택)

`frontend/noroo-mes-app/.env.production` 생성:
```
NUXT_PUBLIC_API_BASE_URL=https://your-api-domain.com
```

---

## 6. 문제 해결

### 6.1 포트 충돌

**포트 8080이 이미 사용 중인 경우**:
```yaml
# application.yml
server:
  port: 8081  # 다른 포트로 변경
```

**포트 3000이 이미 사용 중인 경우**:
```powershell
npm run dev -- -p 3001  # 다른 포트로 실행
```

### 6.2 SQL Server 연결 실패

1. SQL Server 서비스 실행 확인
2. `trustServerCertificate=true` URL에 포함되어 있는지 확인
3. 사용자명/비밀번호 확인

### 6.3 Gradle 빌드 오류

```powershell
# 캐시 초기화
./gradlew clean

# 의존성 재다운로드
./gradlew build --refresh-dependencies
```

### 6.4 npm 모듈 오류

```powershell
# node_modules 삭제 후 재설치
Remove-Item -Recurse -Force node_modules
npm install
```

---

## 7. 확인 체크리스트

- [ ] JDK 21 설치 완료 (`java -version` 확인)
- [ ] Node.js 18+ 설치 완료 (`node -v` 확인)
- [ ] SQL Server 실행 중
- [ ] 데이터베이스 `AiCodeTest` 생성 완료
- [ ] `application.yml` DB 설정 수정 완료
- [ ] 백엔드 포트 8080 실행 중
- [ ] 프론트엔드 포트 3000 실행 중
- [ ] 브라우저로 http://localhost:3000 접속 가능

---

마지막 업데이트: 2026-03-18
