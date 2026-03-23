# 2. 계획 (Plan)

## 🎯 목표
API 스펙을 확정하고, 애플리케이션 아키텍처와 데이터베이스 설계를 완료합니다.

## ✅ 체크리스트
- [ ] **API 및 DTO 설계**
  - [ ] RESTful 엔드포인트 설계 및 HTTP Method 정의 (GET/POST/PUT/DELETE + 적절한 HTTP 상태 코드)
  - [ ] Request / Response DTO 클래스 구조 설계 (Jakarta Validation 포함)
  - [ ] Nuxt 3 SSR 에러 핸들링을 고려한 표준 `ApiResponse<T>` 및 `ErrorResponse` 규격 확정
  - [ ] **[🔒필수]** 민감 데이터 필드에 `@JsonIgnore` 또는 별도 Response DTO 적용 계획 (비밀번호, 토큰 등은 응답에서 제외)
  - [ ] **[🔒필수]** Rate Limiting 및 API 인증 요구사항 설계 (무제한 호출 방지)
- [ ] **DB 및 MyBatis 설계**
  - [ ] MSSQL 테이블 DDL 및 인덱스(Clustered/Non-Clustered) 설계
  - [ ] 대용량 조회 API의 경우 MSSQL 페이징(`OFFSET ... FETCH NEXT`) 및 `WITH (NOLOCK)` 적용 여부 계획
  - [ ] MyBatis Mapper 인터페이스 및 XML `resultMap` / 동적 쿼리 설계
  - [ ] **[🔒필수]** 동적 정렬(`ORDER BY`)에 `${}` 사용 시 **허용 컬럼 화이트리스트** 검증 로직 설계
- [ ] **도메인 및 서비스 설계**
  - [ ] 비즈니스 로직 처리 흐름도 작성
  - [ ] 외부 연동(API 등) 및 트랜잭션 경계(`@Transactional` 옵션) 설정 계획
  - [ ] `ErrorCode` enum 및 `BusinessException` 사용 계획 (공통 모듈 활용)

---

## 📋 백엔드 코드 작성 규칙

### 패키지 네이밍 규칙
- **기본 구조**: `com.dit.<domain>.<layer>`
- **계층별 명명**:
  - `controller` — REST 컨트롤러
  - `service` — 비즈니스 로직
  - `persistence.mapper` — MyBatis Mapper
  - `domain` — 도메인 엔티티
  - `dto` — 데이터 전송 객체 (Request/Response)
  - `common` — 공통 유틸/예외/상수

**예시**:
> com.dit.menu.controller.MenuController
> com.dit.menu.service.MenuService
> com.dit.menu.persistence.mapper.MenuMapper
> com.dit.menu.domain.Menu
> com.dit.menu.dto.CreateMenuReq
> com.dit.common.exception.BusinessException

### MyBatis 설계
- **Mapper는 XML 파일 기반**으로 작성
- **위치**: `backend/apps/app/src/main/resources/mappers/**/*.xml`
- **resultMap 설계**: 컬럼명 → 자바 프로퍼티명 매핑 (`map-underscore-to-camel-case: true` 기본 적용)
  - **[🚨주의]** DB 컬럼명(`CREATE_DTTM`)과 Java 필드명(`createdAt`)의 구조가 다를 경우, 반드시 XML 내부 `<resultMap>` 태그를 이용해 명시적으로 매핑할 것.
- **동적 쿼리**: `<if>`, `<choose>`, `<foreach>` 등으로 조건부 쿼리 작성
- **[🔒필수] SQL Injection 방지**: `#{}` (Prepared Statement) 사용. `${}` 사용은 동적 정렬(`ORDER BY`) 등 극히 제한적인 경우에만 허용하며, 반드시 아래와 같이 서비스 계층에서 화이트리스트 검증 후 사용:

```java
// Service 계층: ORDER BY 컬럼 화이트리스트 검증
private static final Set<String> ALLOWED_SORT_COLUMNS =
    Set.of("userId", "userName", "createDttm", "modifyDttm");

private String validateSortColumn(String column) {
    if (!ALLOWED_SORT_COLUMNS.contains(column)) {
        throw new BusinessException(ErrorCode.INVALID_SORT_COLUMN);
    }
    return column; // 검증 통과 후 Mapper에 전달
}
```

### 계층형 아키텍처

| 계층 | 책임 | 예시 |
|------|------|------|
| **Controller** | HTTP 요청 처리, 입력값 검증, DTO 변환만 담당 (비즈니스 로직 금지) | `@RestController`, `@PostMapping` |
| **Service** | 트랜잭션 경계 설정, 비즈니스 규칙/검증, 여러 Mapper 조합 | `@Service`, `@Transactional` |
| **Mapper** | DB 접근 전용 (쿼리 실행만 담당) | `@Mapper`, XML 쿼리 |
| **Domain** | 도메인 데이터 표현 (비즈니스 로직 포함 가능) | 엔티티 클래스 |
| **DTO** | 외부 요청/응답 데이터 포맷 (Controller 계층 전용) | `Req`, `Res` suffixed 클래스 |

### 공통 모듈 규칙
- **패키지**: `backend/modules/common/`
- **포함 내용**:
  - 공통 예외 클래스 (`BusinessException`, 커스텀 에러코드 enum)
  - 공통 응답 래퍼 (`ApiResponse<T>`) — 프론트엔드 연동을 위해 성공/실패 JSON 포맷 통일
  - 도메인 간 공유되는 유틸, 상수, 검증 로직

### 데이터베이스 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| **DB** | Microsoft SQL Server (MSSQL) | `USE [AiCodeTestDB]` |
| **테이블 접두어** | `TBL_` | `TBL_ORDER_INFO`, `TBL_USER` |
| **컬럼 네이밍** | 소문자 스네이크케이스 (공통컬럼은 대문자 혼용) | `order_id`, `CREATE_DTTM` |
| **타임스탬프** | `VARCHAR(14)` 형식 `yyyyMMddHHmmss` 문자열 저장 (기존 프로젝트 규칙 준수) | `20260323092414` |
| **사용자 추적** | `CREATOR_ID` (수정 불가), `MODIFIER_ID` (최종 수정자) | - |
| **외래키** | `FK_<자식테이블>_<부모테이블>` | `FK_ORDER_USER` |
| **인덱스** | 클러스터 + 논클러스터 혼용 | PK는 Clustered Index |

> **[🚨주의]** 이 프로젝트의 타임스탬프 컬럼(`CREATE_DTTM`, `MODIFY_DTTM`)은 `VARCHAR(14)` 문자열(`yyyyMMddHHmmss`) 형식입니다.
> Java 도메인에서 `LocalDateTime`이 아닌 `String` 타입으로 받아야 자동 매핑 오류가 발생하지 않습니다.

```java
// ✅ 올바른 패턴 (이 프로젝트 기준)
public class UserDomain {
    private String userId;
    private String userName;
    private String createDttm;  // "20260323092414" 형식 문자열
    private String modifyDttm;
}

// ❌ 잘못된 패턴 (LocalDateTime은 VARCHAR와 자동 매핑 불가)
// private LocalDateTime createdAt; // DB: CREATE_DTTM
```
