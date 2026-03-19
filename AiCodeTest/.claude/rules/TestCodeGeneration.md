# Test Code Generation (테스트 코드 작성 단계)

## 목적

1. 구현 전 또는 구현과 병행하여 테스트 코드를 작성함으로써, 요구사항을 명확히 하고 회귀 오류를 방지한다.
2. 계층별 테스트 전략 명시 (Backend):Mapper Test: DB 쿼리가 의도대로 동작하는지 확인 (@MybatisTest 등 활용)
Service Test: Mockito 등을 활용하여 비즈니스 로직의 분기 및 예외(Custom Exception) 검증
Controller Test: MockMvc를 통해 HTTP 상태 코드 및 ApiResponse 포맷 검증

3. 프론트엔드 Mocking 전략 (Frontend): 백엔드 API가 완성되기 전에도 테스트할 수 있도록 MSW(Mock Service Worker)나 Nuxt 내부의 Mock 데이터를 세팅하는 방법을 추가합니다.

4. Composables/Store 테스트: UI 렌더링뿐만 아니라, 복잡한 로직이 담긴 Vue Composables와 Pinia Store에 대한 Vitest 단위 테스트 작성을 포함합니다.
## 백엔드 테스트

### 단위 테스트 (Service Layer)
- 위치: `backend/modules/<domain>/src/test/java/com/dit/<domain>/service/`
- 프레임워크: JUnit 5 + Mockito
- Mapper는 Mock 처리하여 Service 로직만 검증

```java
@ExtendWith(MockitoExtension.class)
class XxxServiceTest {

    @Mock
    private XxxMapper xxxMapper;

    @InjectMocks
    private XxxService xxxService;

    @Test
    @DisplayName("정상 케이스: [기능명] 성공")
    void 기능명_성공() {
        // given
        // when
        // then
    }

    @Test
    @DisplayName("예외 케이스: [조건]일 때 BusinessException 발생")
    void 기능명_예외() {
        // given
        // when & then
        assertThrows(BusinessException.class, () -> xxxService.method());
    }
}
```

### 통합 테스트 (Controller Layer)
- 위치: `backend/apps/app/src/test/java/com/dit/`
- 프레임워크: `@SpringBootTest` + `MockMvc`
- 실제 DB 연결 또는 H2 인메모리 DB 사용

```java
@SpringBootTest
@AutoConfigureMockMvc
class XxxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/xxx - 목록 조회 성공")
    void 목록조회_성공() throws Exception {
        mockMvc.perform(get("/api/xxx"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true));
    }
}
```

### 테스트 명명 규칙
- 메서드명: 한국어로 작성 (`기능명_조건_결과`)
- `@DisplayName`: 상세한 설명 포함

## 프론트엔드 테스트

### Composable 단위 테스트
- 위치: `frontend/noroo-mes-app/tests/composables/`
- 프레임워크: Vitest + `@vue/test-utils`

```typescript
import { describe, it, expect, vi } from 'vitest'
import { useXxxAPI } from '~/composables/useXxxAPI'

describe('useXxxAPI', () => {
  it('목록 조회 성공 시 데이터를 반환한다', async () => {
    // given
    // when
    // then
  })

  it('API 오류 시 에러 메시지를 설정한다', async () => {
    // given
    // when
    // then
  })
})
```

### E2E 테스트 (필요 시)
- 프레임워크: Playwright
- 위치: `frontend/noroo-mes-app/tests/e2e/`
- 로그인 → 기능 수행 → 결과 확인 흐름으로 작성

## 테스트 실행 명령어

```bash
# 백엔드 전체 테스트
cd backend && ./gradlew test

# 특정 모듈 테스트
./gradlew :modules:menu:test

# 프론트엔드 테스트
cd frontend/noroo-mes-app && npm run test
```

## 테스트 작성 원칙

- 정상 케이스와 예외 케이스를 모두 작성한다
- `BusinessException` 발생 여부와 `ErrorCode`를 검증한다
- `ApiResponse<T>`의 `success` 필드와 데이터 구조를 검증한다
- 테스트는 독립적으로 실행 가능해야 한다 (순서 의존성 금지)
