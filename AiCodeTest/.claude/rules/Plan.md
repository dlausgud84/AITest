# Plan (계획 단계)

## 목적

Research 단계에서 수집한 정보를 바탕으로 구현 전략을 수립하고, 작업 순서와 파일 변경 범위를 확정한다.

## 수행 절차

### 1. 아키텍처 설계
- 계층형 아키텍처 원칙을 준수하여 설계한다
  - Controller: DTO 변환만 담당, 비즈니스 로직 금지
  - Service: 비즈니스 규칙/검증/트랜잭션
  - Mapper: DB 접근 전용 (MyBatis XML)
- 공통 모듈로 분리할 항목을 결정한다 (2개 이상 도메인에서 사용 시 필수)

### 2. DB 설계 (해당 시)
- 테이블명: `TBL_` 접두어 + 대문자 스네이크케이스
- 컬럼명: 소문자 스네이크케이스
- 필수 컬럼: `CREATE_DTTM`, `MODIFY_DTTM`, `CREATOR_ID`, `MODIFIER_ID`
- 타임스탬프 형식: `yyyyMMddHHmmss`

### 3. API 설계 (해당 시)
- RESTful 엔드포인트 경로 및 HTTP 메서드 결정
- 요청/응답 DTO 구조 정의
- 모든 응답은 `ApiResponse<T>` 래퍼 사용
- 오류 케이스 및 `ErrorCode` 정의

### 4. 프론트엔드 설계 (해당 시)
- 새 페이지 또는 컴포넌트 필요 여부 결정
- 필요한 composable 목록 확인 (`useMenuAPI`, `usePagination`, `useGridSort` 등)
- 그리드 페이지: 필터링 → 정렬 → 페이지네이션 순서 적용

### 5. 작업 순서 결정
1. DB 스키마 변경 (필요 시)
2. 백엔드 공통 모듈 (필요 시)
3. 백엔드 도메인 모듈 (Mapper XML → Service → Controller 순)
4. 프론트엔드 composable
5. 프론트엔드 페이지/컴포넌트

### 6. 변경 파일 목록 작성
구현 전에 변경/생성할 파일을 모두 나열한다.

```
신규 생성:
- backend/modules/<domain>/src/.../domain/XxxDomain.java
- backend/modules/<domain>/src/.../dto/XxxDto.java
- backend/modules/<domain>/src/.../service/XxxService.java
- backend/apps/app/src/.../controller/XxxController.java
- backend/apps/app/src/main/resources/mappers/XxxMapper.xml
- frontend/noroo-mes-app/composables/useXxxAPI.ts
- frontend/noroo-mes-app/pages/xxx.vue

수정:
- backend/settings.gradle.kts (신규 모듈 추가 시)
```

## 산출물

- 설계 다이어그램 또는 흐름 설명
- API 명세 (엔드포인트, DTO)
- 변경/생성 파일 목록
- 단계별 작업 순서
