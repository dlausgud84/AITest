# 3. 테스트 작성 (Test Code Generation)

## 🎯 목표
Vitest, Vue Test Utils, 그리고 `@nuxt/test-utils`를 활용하여 비즈니스 로직, 전역 상태, 핵심 UI의 안정성을 확보합니다.

## ✅ 체크리스트
- [ ] **Composables 단위 테스트 (Vitest)**
  - [ ] 상태 변경 및 핵심 데이터 가공 로직 테스트 작성
  - [ ] Vitest `vi.stubGlobal`을 활용한 `$fetch` API 호출 분기/에러 테스트
- [ ] **전역 상태(Pinia) 단위 테스트 [💡추가]**
  - [ ] Store의 `actions` 실행 후 `state` 변경 및 `getters` 계산 값 검증
- [ ] **UI 컴포넌트 단위 테스트 (Vue Test Utils)**
  - [ ] Props 전달에 따른 렌더링 결과(클래스, 속성 등) 확인
  - [ ] 사용자 이벤트(Click, Input 등) 발생 시 Emit 처리 확인
- [ ] **Edge Case 커버리지**
  - [ ] 빈 데이터, 에러 응답 등 예외 상황에 대한 테스트 케이스 추가
- [ ] **보안 테스트 [🔒신규 추가]**
  - [ ] XSS 공격 패턴 입력 시 적절한 이스케이프 처리 확인
  - [ ] 민감 데이터(토큰, 비밀번호)가 콘솔이나 DOM에 노출되지 않는지 확인
  - [ ] 인증 실패 시 적절한 리다이렉트 동작 확인

---

## 📋 Vitest + Vue Test Utils 테스트 패턴

### 1. Composable 테스트 (API Mocking) [💡수정: `vi.stubGlobal` 적용]

```typescript
// composables/__tests__/useMenuAPI.test.ts
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useMenuAPI } from '../useMenuAPI'

describe('useMenuAPI', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })
  
  afterEach(() => {
    vi.unstubAllGlobals() // [💡추가] 다른 테스트에 영향을 주지 않도록 초기화
  })

  it('메뉴 조회 성공', async () => {
    const { menus, fetchMenus } = useMenuAPI()
    
    // [💡수정] Vitest 권장 전역 Mock 방식
    vi.stubGlobal('$fetch', vi.fn().mockResolvedValue([
      { id: 1, name: '메뉴1' },
      { id: 2, name: '메뉴2' }
    ]))
    
    await fetchMenus()
    
    expect(menus.value).toHaveLength(2)
    expect(menus.value[0].name).toBe('메뉴1')
  })

  it('메뉴 조회 실패 시 에러 처리', async () => {
    const { error, fetchMenus } = useMenuAPI()