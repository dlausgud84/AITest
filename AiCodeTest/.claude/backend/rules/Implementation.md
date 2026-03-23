# 4. 구현 (Implementation)

## 🎯 목표
Java 21, Spring Boot, MyBatis를 활용하여 실제 비즈니스 로직을 안전하고 효율적으로 구현합니다.

## ✅ 체크리스트
- [ ] **Data Access 계층 구현**
  - [ ] Domain / Entity / DTO 클래스 생성 **[💡추가]** (DTO는 Java 21 `record` 적극 활용)
  - [ ] Mapper 인터페이스 및 XML 쿼리 작성 (SQL Injection 방지 `#{}` 사용 확인)
  - [ ] **[🔒보안추가]** 민감 데이터 필드에 `@JsonIgnore` 적용하여 JSON 응답에서 제외
- [ ] **Business 로직 계층 구현 (Service)**
  - [ ] 트랜잭션(`@Transactional`) 범위 및 예외 롤백 설정 적용 
  - [ ] **[💡추가]** 단순 조회 메서드에는 `@Transactional(readOnly = true)` 적용하여 성능 최적화
  - [ ] 비즈니스 로직 구현 및 의존성 주입(생성자 주입, `@RequiredArgsConstructor` 활용)
- [ ] **Presentation 계층 구현 (Controller)**
  - [ ] REST Controller 구현 및 Swagger(Springdoc) 어노테이션 작성
  - [ ] 입력값 검증(`@Valid`, `@Validated`) 적용
- [ ] **공통 처리 및 로깅**
  - [ ] Global Exception Handler(`@RestControllerAdvice`)에 커스텀 예외 및 Validation 예외 추가
  - [ ] **[💡추가]** 주요 비즈니스 분기점 및 에러 발생 시 SLF4J(`@Slf4j`)를 활용한 로그 작성

---

## 📋 백엔드 코드 패턴 및 규칙

### 1. Java 21 Record를 활용한 DTO [💡신규 추가]
불변(Immutable) 객체인 `record`를 사용하여 코드를 간결하게 유지하고 데이터 안정성을 높입니다.

```java
// CreateMenuReq.java
public record CreateMenuReq(
    @NotBlank(message = "메뉴명은 필수입니다.")
    String name,
    
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    int price
) {}