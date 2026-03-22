# 2. 계획 (Plan)

## 🎯 목표
API 스펙을 확정하고, 애플리케이션 아키텍처와 데이터베이스 설계를 완료합니다.

## ✅ 체크리스트
- [ ] **API 및 DTO 설계**
  - [ ] RESTful 엔드포인트 설계 및 HTTP Method 정의
  - [ ] Request / Response DTO 클래스 구조 설계 (Validation 포함)
- [ ] **DB 및 MyBatis 설계**
  - [ ] MSSQL 테이블 DDL 및 인덱스(Clustered/Non-Clustered) 추가/변경 계획
  - [ ] MyBatis Mapper 인터페이스 및 XML `resultMap` / 동적 쿼리 설계
- [ ] **도메인 및 서비스 설계**
  - [ ] 비즈니스 로직 처리 흐름도 작성
  - [ ] 외부 연동(API 등) 및 트랜잭션 경계 설정 계획

---

## 📋 백엔드 코드 작성 규칙

### 패키지 네이밍 규칙
- **기본 구조**: `com.dit.<domain>.<layer>`
- **계층별 명명**:
  - `controller` — REST 컨트롤러
  - `service` — 비즈니스 로직
  - `persistence.mapper` — MyBatis Mapper
  - `domain` — 도메인 엔티티
  - `dto` — 데이터 전송 객체
  - `common` — 공통 유틸/예외/상수

**예시**:
```
com.dit.menu.controller.MenuController
com.dit.menu.service.MenuService
com.dit.menu.persistence.mapper.MenuMapper
com.dit.menu.domain.Menu
com.dit.menu.dto.CreateMenuReq
com.dit.common.exception.BusinessException
```

### MyBatis 설계
- **Mapper는 XML 파일 기반**으로 작성
- **위치**: `backend/apps/app/src/main/resources/mappers/**/*.xml`
- **resultMap 설계**: 컬럼명 → 자바 프로퍼티명 매핑(`map-underscore-to-camel-case: true` 설정)
- **동적 쿼리**: `<if>`, `<choose>`, `<foreach>` 등으로 조건부 쿼리 작성
- **SQL Injection 방지**: `#{}` (준비된 문장) 사용, `${}` 사용 금지

### 계층형 아키텍처

| 계층 | 책임 | 예시 |
|------|------|------|
| **Controller** | HTTP 요청 처리, 입력값 검증, DTO 변환만 담당 (비즈니스 로직 금지) | `@RestController`, `@PostMapping` 등 |
| **Service** | 트랜잭션 경계, 비즈니스 규칙/검증, 여러 Mapper 조합 | `@Service`, `@Transactional` 등 |
| **Mapper** | DB 접근 전용 (쿼리 실행만 담당) | `@Mapper`, XML 쿼리 |
| **Domain** | 도메인 데이터 표현 (비즈니스 로직 포함 가능) | 엔티티 클래스 |
| **DTO** | 외부 요청/응답 데이터 포맷 | `Req`, `Res` suffixed 클래스 |

### 공통 모듈 규칙
- **패키지**: `backend/modules/common/`
- **포함 내용**:
  - 공통 예외 클래스 (`BusinessException`, 에러코드 enum)
  - 공통 응답 래퍼 (`ApiResponse<T>`) — 성공/실패 포맷 통일
  - 도메인 간 공유되는 유틸, 상수, 검증 로직

### 데이터베이스 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| **DB** | Microsoft SQL Server (MSSQL) | `USE [AiCodeTestDB]` |
| **테이블 접두어** | `TBL_` | `TBL_ORDER_INFO`, `TBL_USER` |
| **컬럼 네이밍** | 소문자 스네이크케이스 | `order_id`, `user_name`, `created_at` |
| **타임스탬프** | `CREATE_DTTM`, `MODIFY_DTTM` | 형식: `yyyyMMddHHmmss` |
| **사용자 추적** | `CREATOR_ID` (수정 불가), `MODIFIER_ID` (최종 수정자) | - |
| **외래키** | `FK_<부모테이블>_<자식테이블>` | `FK_USER_ORDER` |
| **인덱스** | 클러스터 + 논클러스터 혼용 | PK는 Clustered |

**자바 매핑**:
- 도메인 모델은 **camelCase** 사용
- MyBatis 설정에서 `map-underscore-to-camel-case: true` 적용으로 자동 매핑

```java
// Domain
public class Order {
  private String orderId;     // DB: order_id
  private String userName;    // DB: user_name
  private LocalDateTime createdAt; // DB: CREATE_DTTM (변환 필요)
}
```