# 4. 구현 (Implementation)

## 🎯 목표
Java 21, Spring Boot, MyBatis를 활용하여 실제 비즈니스 로직을 안전하고 효율적으로 구현합니다.

## ✅ 체크리스트
- [ ] **Data Access 계층 구현**
  - [ ] Domain / Entity / DTO 클래스 생성
  - [ ] Mapper 인터페이스 및 XML 쿼리 작성 (SQL Injection 방지 `#{}` 사용 확인)
- [ ] **Business 로직 계층 구현 (Service)**
  - [ ] 트랜잭션(`@Transactional`) 범위 및 예외 롤백 설정 적용
  - [ ] 비즈니스 로직 구현 및 의존성 주입(생성자 주입)
- [ ] **Presentation 계층 구현 (Controller)**
  - [ ] REST Controller 구현 및 Swagger(Springdoc) 어노테이션 작성
  - [ ] 입력값 검증(`@Valid`, `@Validated`) 적용
- [ ] **공통 처리**
  - [ ] Global Exception Handler(`@RestControllerAdvice`)에 커스텀 예외 추가

---

## 📋 백엔드 에러 처리 패턴

### BusinessException 사용

```java
// Service 계층에서 비즈니스 규칙 위반 시
if (menu == null) {
  throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
}
```

### Global Exception Handler

```java
// GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
    return ResponseEntity
      .status(e.getErrorCode().getHttpStatus())
      .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
  }
}
```

### 공통 응답 래퍼

```java
// ApiResponse.java
public class ApiResponse<T> {
  private String code;           // "SUCCESS" 또는 에러 코드
  private String message;        // 사용자 메시지
  private T data;                // 응답 데이터
  
  // 정적 팩토리 메서드
  public static <T> ApiResponse<T> success(T data) { ... }
  public static <T> ApiResponse<T> error(String code, String message) { ... }
}
```

### DB 연결 설정

```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=AiCodeTestDB;trustServerCertificate=true
    username: sa
    password: your_password
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
```

**중요**: `trustServerCertificate=true` 필수 (자체 서명 인증서 허용)