# Verification (검증 단계)

## 목적

1. 구현 완료 후 기능이 요구사항을 충족하는지, 기존 기능에 회귀 오류가 없는지 확인한다.
2. 렌더링 모드 검증 (Nuxt 3): 프론트엔드에서 하이브리드 렌더링(SSR/CSR) 충돌이 없는지, window나 document 객체 접근 시 오류가 발생하지 않는지 확인합니다.
3. End-to-End (E2E) 통합 검증: 프론트엔드 화면 조작부터 실제 백엔드 API 호출, DB 업데이트까지 전체 데이터 플로우가 정상적으로 작동하는지 통합 검증하는 절차를 명시합니다.
4. 린트(Lint) 및 정적 분석: 저장소의 컨벤션(ESLint, Prettier 등) 위반 사항이 없는지 자동화 툴이나 CI 검증 결과를 확인하는 단계를 추가합니다.
## 검증 체크리스트

### 1. 빌드 검증
```bash
# 백엔드 빌드 확인
cd backend && ./gradlew build

# 프론트엔드 빌드 확인
cd frontend/noroo-mes-app && npm run build
```
- 빌드 오류가 없어야 한다
- 컴파일 경고(특히 타입 오류)를 모두 해소한다

### 2. 기능 검증

#### 백엔드 API 검증
- 각 엔드포인트를 직접 호출하여 응답 확인 (Postman 또는 curl)
- 정상 케이스: `{ "success": true, "data": ... }` 형식 확인
- 오류 케이스: `{ "success": false, "errorCode": "...", "message": "..." }` 형식 확인
- HTTP 상태 코드가 적절한지 확인

```bash
# 예시: 목록 조회 API 검증
curl -X GET http://localhost:8080/api/xxx \
  -H "Authorization: Bearer <token>"

# 예시: 등록 API 검증
curl -X POST http://localhost:8080/api/xxx \
  -H "Content-Type: application/json" \
  -d '{"name": "테스트"}'
```

#### 프론트엔드 화면 검증
- 개발 서버에서 해당 페이지 직접 접근하여 UI 확인
- 데이터 로딩, 검색, 정렬, 페이지네이션 동작 확인
- 등록/수정/삭제 후 목록 갱신 확인
- 오류 메시지가 사용자에게 올바르게 표시되는지 확인

### 3. 예외 케이스 검증
- 필수 입력값 누락 시 적절한 오류 응답 반환 확인
- 존재하지 않는 리소스 접근 시 처리 확인
- 권한 없는 접근 시 인증 미들웨어 동작 확인
- 네트워크 오류 시 프론트엔드 오류 메시지 표시 확인

### 4. 회귀 테스트
```bash
# 백엔드 전체 테스트 실행
cd backend && ./gradlew test

# 특정 모듈 테스트
./gradlew :modules:<domain>:test

# 프론트엔드 테스트
cd frontend/noroo-mes-app && npm run test
```
- 기존 테스트가 모두 통과해야 한다
- 새로 작성한 테스트가 모두 통과해야 한다

### 5. 코드 규칙 준수 확인
- [ ] 패키지 네이밍: `com.dit.<domain>.<layer>` 형식
- [ ] Controller에 비즈니스 로직이 없는지 확인
- [ ] 모든 API 응답이 `ApiResponse<T>` 래퍼를 사용하는지 확인
- [ ] DB 테이블명 `TBL_` 접두어, 컬럼명 소문자 스네이크케이스 확인
- [ ] `CREATE_DTTM`, `MODIFY_DTTM`, `CREATOR_ID`, `MODIFIER_ID` 필드 포함 여부
- [ ] 2개 이상 도메인에서 사용하는 로직이 `common` 모듈에 있는지 확인
- [ ] 프론트엔드 그리드 페이지에 3개 composable 모두 적용 여부
- [ ] 하드코딩된 URL, 포트, 환경값이 없는지 확인
- [ ] 코드 주석이 한국어로 작성되었는지 확인

### 6. 문서 업데이트
- `docs/WORK_HISTORY.md`에 작업 내용 기록
  - 구현/수정한 기능
  - 변경된 파일 목록
  - 해결한 버그 (있는 경우)
  - 미완료 사항 (TODO)
- API가 변경된 경우 `docs/DEVELOPER_MANUAL.md` 업데이트
- 환경 설정이 변경된 경우 `docs/INSTALL_GUIDE.md` 업데이트

## 배포 전 최종 확인

- [ ] `application-prod.yml`의 환경변수가 올바르게 설정되어 있는지 확인
- [ ] `.env.production`의 API URL이 실제 서버 주소로 설정되어 있는지 확인
- [ ] 개발용 로그 레벨(`DEBUG`)이 운영용(`WARN`)으로 변경되었는지 확인
- [ ] DB 마이그레이션 스크립트가 준비되어 있는지 확인
