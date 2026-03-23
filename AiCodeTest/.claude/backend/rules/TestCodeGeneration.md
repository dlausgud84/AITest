# 3. 테스트 작성 (Test Code Generation)

## 🎯 목표
비즈니스 로직과 데이터 접근 계층(MyBatis), 그리고 API 엔드포인트의 신뢰성을 보장하는 테스트를 작성합니다.

## ✅ 체크리스트
- [ ] **단위 테스트 (Unit Test - JUnit5 / Mockito)**
  - [ ] Service 계층의 핵심 비즈니스 로직 테스트 (Mocking 활용)
  - [ ] 예외 발생 조건(Validation 실패, 데이터 없음 등)에 대한 테스트 추가
- [ ] **API 계층 테스트 (WebMvcTest) [💡추가]**
  - [ ] Controller 엔드포인트(URL, HTTP Method, HTTP Status) 정상 응답 확인
  - [ ] Request DTO의 `@Valid` 검증 로직 작동 여부 확인
- [ ] **보안 테스트 (Security Test) [🔒신규 추가]**
  - [ ] SQL Injection 공격 시도에 대한 방어 테스트 (특수문자 입력)
  - [ ] XSS 공격 패턴 입력 시 적절한 이스케이프 처리 확인
  - [ ] 인증되지 않은 요청에 대한 401/403 응답 확인
  - [ ] 민감 데이터가 Response에 노출되지 않는지 확인
- [ ] **통합 및 데이터 계층 테스트 (Integration Test)**
  - [ ] `@MybatisTest`를 활용한 Mapper + XML 동적 쿼리 정상 동작 확인
  - [ ] **[💡추가]** MSSQL 고유 문법(`OFFSET`, `NOLOCK`) 테스트 시 Testcontainers 활용 권장

---

## 📋 JUnit5 + Mockito 테스트 패턴

### 1. Controller 계층 테스트 (MockMvc) [💡신규 추가]
API의 스펙과 HTTP 통신, 입력값 검증을 테스트합니다.

```java
// MenuControllerTest.java
@WebMvcTest(MenuController.class) // Controller 관련 빈만 로드하여 가볍고 빠름
class MenuControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean // Spring 컨텍스트에 Mock 객체 주입
  private MenuService menuService;

  @Test
  void 메뉴_생성_성공() throws Exception {
    // Given
    CreateMenuReq req = new CreateMenuReq("새로운메뉴");
    given(menuService.createMenu(any())).willReturn(1L);

    // When & Then
    mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"새로운메뉴\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value(1L)); // 공통 ApiResponse 검증
  }
}