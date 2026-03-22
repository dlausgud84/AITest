# 3. 테스트 작성 (Test Code Generation)

## 🎯 목표
Vitest와 Vue Test Utils를 활용하여 비즈니스 로직과 핵심 UI의 안정성을 확보합니다.

## ✅ 체크리스트
- [ ] **Composables 단위 테스트 (Vitest)**
  - [ ] 상태 변경 및 핵심 데이터 가공 로직 테스트 작성
  - [ ] Mock 데이터를 활용한 API 호출 분기 테스트
- [ ] **UI 컴포넌트 단위 테스트 (Vue Test Utils)**
  - [ ] Props 전달에 따른 렌더링 결과 확인
  - [ ] 사용자 이벤트(Click, Input 등) 발생 시 Emit 처리 확인
- [ ] **Edge Case 커버리지**
  - [ ] 빈 데이터, 에러 응답 등 예외 상황에 대한 테스트 케이스 추가

---

## 📋 Vitest + Vue Test Utils 테스트 패턴

### Composable 테스트 예시

```typescript
// composables/__tests__/useMenuAPI.test.ts
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { useMenuAPI } from '../useMenuAPI'

describe('useMenuAPI', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('메뉴 조회 성공', async () => {
    const { menus, fetchMenus } = useMenuAPI()
    
    // Mock API 응답
    global.$fetch = vi.fn().mockResolvedValue([
      { id: 1, name: '메뉴1' },
      { id: 2, name: '메뉴2' }
    ])
    
    await fetchMenus()
    
    expect(menus.value).toHaveLength(2)
    expect(menus.value[0].name).toBe('메뉴1')
  })

  it('메뉴 조회 실패 시 에러 처리', async () => {
    const { error, fetchMenus } = useMenuAPI()
    
    global.$fetch = vi.fn().mockRejectedValue(
      new Error('API 오류')
    )
    
    await fetchMenus()
    
    expect(error.value).toBe('API 오류')
  })
})
```

### 컴포넌트 테스트 예시

```typescript
// components/__tests__/BaseButton.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import BaseButton from '../base/BaseButton.vue'

describe('BaseButton', () => {
  it('Props 전달에 따른 렌더링', () => {
    const wrapper = mount(BaseButton, {
      props: {
        btnType: 'create',
        isDisabled: false
      }
    })
    
    expect(wrapper.find('button').classes()).toContain('btn-create')
    expect(wrapper.find('button').attributes('disabled')).toBeUndefined()
  })

  it('Click 이벤트 발생 시 emit 호출', async () => {
    const wrapper = mount(BaseButton, {
      props: { btnType: 'search' }
    })
    
    await wrapper.find('button').trigger('click')
    
    expect(wrapper.emits('click')).toHaveLength(1)
  })

  it('isDisabled=true 시 클릭 불가', async () => {
    const wrapper = mount(BaseButton, {
      props: { isDisabled: true }
    })
    
    expect(wrapper.find('button').attributes('disabled')).toBeDefined()
  })
})
```

### 테스트 실행 명령어

```bash
# 모든 테스트 실행
npm run test

# 특정 파일 테스트
npm run test useMenuAPI.test.ts

# Watch 모드 (파일 변경 시 자동 재실행)
npm run test:watch

# 커버리지 리포트
npm run test:coverage
```