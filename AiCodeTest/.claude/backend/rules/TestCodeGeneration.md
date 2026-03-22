# 3. 테스트 작성 (Test Code Generation)

## 🎯 목표
비즈니스 로직과 데이터 접근 계층(MyBatis)의 신뢰성을 보장하는 테스트를 작성합니다.

## ✅ 체크리스트
- [ ] **단위 테스트 (Unit Test - JUnit5 / Mockito)**
  - [ ] Service 계층의 핵심 비즈니스 로직 테스트 (Mocking 활용)
  - [ ] 예외 발생 조건(Validation 실패, 데이터 없음 등)에 대한 테스트 추가
- [ ] **통합 테스트 (Integration Test)**
  - [ ] Mapper + XML 쿼리 정상 동작 테스트 (필요시 Testcontainers/H2 등 활용)
  - [ ] 복잡한 동적 쿼리(if, choose 등) 조건별 정상 조회 여부 확인

---

## 📋 JUnit5 + Mockito 테스트 패턴

### Service 단위 테스트

```java
// MenuServiceTest.java
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
  
  @Mock
  private MenuMapper menuMapper;
  
  @InjectMocks
  private MenuService menuService;
  
  @Test
  void 메뉴_조회_성공() {
    // Given
    Menu mockMenu = new Menu(1L, "메뉴1");
    when(menuMapper.selectById(1L)).thenReturn(mockMenu);
    
    // When
    Menu result = menuService.findMenuById(1L);
    
    // Then
    assertThat(result.getName()).isEqualTo("메뉴1");
    verify(menuMapper, times(1)).selectById(1L);
  }
  
  @Test
  void 메뉴_없음_예외_발생() {
    // Given
    when(menuMapper.selectById(999L)).thenReturn(null);
    
    // When & Then
    assertThrows(BusinessException.class, () -> {
      menuService.findMenuById(999L);
    });
  }
}
```

### MyBatis Mapper 통합 테스트

```java
// MenuMapperTest.java
@SpringBootTest
class MenuMapperTest {
  
  @Autowired
  private MenuMapper menuMapper;
  
  @Autowired
  private TestDatabaseInitializer dbInitializer;
  
  @BeforeEach
  void setUp() {
    dbInitializer.clearAndInsertTestData();
  }
  
  @Test
  void 동적쿼리_name으로_조회() {
    // Given
    MenuSearchReq req = new MenuSearchReq();
    req.setName("메뉴");
    
    // When
    List<Menu> results = menuMapper.selectByConditions(req);
    
    // Then
    assertThat(results).isNotEmpty();
    results.forEach(m -> assertTrue(m.getName().contains("메뉴")));
  }
  
  @Test
  void 페이지네이션_조회() {
    // Given
    PageReq pageReq = new PageReq({offset: 0, limit: 10});
    
    // When
    PageRes<Menu> page = menuMapper.selectPage(pageReq);
    
    // Then
    assertThat(page.getTotal()).isGreaterThan(0);
    assertThat(page.getItems()).hasSize(10);
  }
}
```

### 예외 조건 테스트

```java
@Test
void 비즈니스_규칙_위반() {
  // 예: 중복된 메뉴명으로 생성 시도
  Menu duplicateMenu = new Menu();
  duplicateMenu.setName("기존메뉴");
  
  assertThrows(BusinessException.class, () -> {
    menuService.createMenu(duplicateMenu);
  }, "메뉴명 중복 시 예외 발생");
}
```

### 테스트 실행 명령어

```bash
# 모든 테스트 실행
./gradlew test

# 특정 클래스 테스트
./gradlew test --tests MenuServiceTest

# 특정 메서드 테스트
./gradlew test --tests MenuServiceTest.메뉴_조회_성공

# 커버리지 리포트
./gradlew test jacocoTestReport
```