# 3. 테스트 작성 (Test Code Generation)

## 🎯 목표
Vitest, Vue Test Utils, 그리고 `@nuxt/test-utils`를 활용하여 비즈니스 로직, 전역 상태, 핵심 UI의 안정성을 확보합니다.

## ✅ 체크리스트
- [ ] **Composables 단위 테스트 (Vitest)**
  - [ ] 상태 변경 및 핵심 데이터 가공 로직 테스트 작성
  - [ ] Vitest `vi.stubGlobal`을 활용한 `$fetch` API 호출 분기/에러 테스트
  - [ ] **[🔒필수]** 에러 상태(`errorMsg`)가 올바르게 설정되는지 테스트 (빈 catch 검증)
- [ ] **UI 컴포넌트 단위 테스트 (Vue Test Utils)**
  - [ ] Props 전달에 따른 렌더링 결과(클래스, 속성 등) 확인
  - [ ] 사용자 이벤트(Click, Input 등) 발생 시 Emit 처리 확인
- [ ] **Edge Case 커버리지**
  - [ ] 빈 데이터, 에러 응답 등 예외 상황에 대한 테스트 케이스 추가
  - [ ] 경계값(Boundary Value) 테스트 (빈 문자열, 특수문자, 최대 길이 등)
- [ ] **보안 테스트 🔒**
  - [ ] XSS 공격 패턴 입력 시 `{{ }}` 보간법으로 이스케이프되어 DOM에 스크립트가 실행되지 않는지 확인
  - [ ] 민감 데이터(토큰, 비밀번호)가 콘솔이나 DOM에 노출되지 않는지 확인
  - [ ] 인증 실패 시 `/login`으로 적절히 리다이렉트되는지 확인
  - [ ] **[🔒필수]** `localStorage`에 인증 토큰이 저장되지 않는지 확인 (httpOnly 쿠키 사용 여부)

---

## 📋 Vitest + Vue Test Utils 테스트 패턴

### 1. Composable 테스트 (API Mocking)

```typescript
// composables/__tests__/useUserAPI.test.ts
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useUserAPI } from '../useUserAPI'

describe('useUserAPI', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.unstubAllGlobals() // 다른 테스트에 영향을 주지 않도록 초기화
  })

  it('목록 조회 성공 시 userList에 데이터 설정', async () => {
    const { userList, fetchUserList } = useUserAPI()

    vi.stubGlobal('$fetch', vi.fn().mockResolvedValue({
      success: true,
      data: [
        { userId: 'user1', userName: '사용자1' },
        { userId: 'user2', userName: '사용자2' }
      ]
    }))

    await fetchUserList()

    expect(userList.value).toHaveLength(2)
    expect(userList.value[0].userId).toBe('user1')
  })

  it('API 오류 시 errorMsg에 메시지 설정 (빈 catch 금지 검증)', async () => {
    const { errorMsg, fetchUserList } = useUserAPI()

    vi.stubGlobal('$fetch', vi.fn().mockRejectedValue(new Error('Network Error')))

    await fetchUserList()

    // ✅ 핵심: 에러가 발생해도 errorMsg가 설정되어야 함 (빈 catch 블록 금지)
    expect(errorMsg.value).not.toBe('')
    expect(errorMsg.value).toContain('오류')
  })

  it('isLoading 상태 관리 검증', async () => {
    const { isLoading, fetchUserList } = useUserAPI()

    vi.stubGlobal('$fetch', vi.fn().mockResolvedValue({ success: true, data: [] }))

    const promise = fetchUserList()
    expect(isLoading.value).toBe(true) // 호출 중에는 true

    await promise
    expect(isLoading.value).toBe(false) // 완료 후 false
  })
})
```

### 2. 보안 테스트: XSS 방어 검증

```typescript
// components/__tests__/XssDefense.test.ts
import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import UserNameDisplay from '../UserNameDisplay.vue'

describe('XSS 방어 테스트', () => {
  it('악성 스크립트 입력 시 이스케이프되어 DOM에 실행되지 않음', () => {
    const maliciousInput = '<script>alert("XSS")</script>'

    const wrapper = mount(UserNameDisplay, {
      props: { userName: maliciousInput }
    })

    // ✅ {{ }} 보간법은 자동 이스케이프: 스크립트 태그가 텍스트로 표시되어야 함
    expect(wrapper.find('script').exists()).toBe(false)   // 실제 <script> 태그 없음
    expect(wrapper.text()).toContain('&lt;') // 이스케이프된 '<' 문자 포함 (선택)
  })
})
```

### 3. 보안 테스트: 인증 토큰 저장 위치 검증

```typescript
// composables/__tests__/authSecurity.test.ts
import { describe, it, expect, vi } from 'vitest'

describe('인증 토큰 보안 테스트', () => {
  it('로그인 후 localStorage에 토큰이 저장되지 않아야 함', async () => {
    const setItemSpy = vi.spyOn(window.localStorage, 'setItem')

    // 로그인 로직 실행 (useAuthAPI 등)
    // await login({ userId: 'test', password: 'pass' })

    // ✅ localStorage에 토큰 저장 없음 (httpOnly 쿠키 사용)
    expect(setItemSpy).not.toHaveBeenCalledWith('token', expect.any(String))
    expect(setItemSpy).not.toHaveBeenCalledWith('auth-token', expect.any(String))
  })
})
```

### 4. 라우트 가드 테스트

```typescript
// middleware/__tests__/auth.test.ts
import { describe, it, expect, vi } from 'vitest'

describe('인증 미들웨어', () => {
  it('인증 토큰 없을 시 /login으로 리다이렉트', async () => {
    // useCookie mock: 토큰 없음
    vi.mock('#app', () => ({
      useCookie: vi.fn().mockReturnValue({ value: null })
    }))

    const navigateTo = vi.fn()
    vi.stubGlobal('navigateTo', navigateTo)

    // 미들웨어 실행 시 /login으로 리다이렉트되어야 함
    // (실제 미들웨어 함수를 import하여 테스트)
    expect(navigateTo).toHaveBeenCalledWith('/login')
  })
})
```

### 5. 컴포넌트 테스트 (Props/Emit)

```typescript
// components/__tests__/BaseButton.test.ts
import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import BaseButton from '../BaseButton.vue'

describe('BaseButton', () => {
  it('isDisabled=true 시 버튼이 비활성화됨', () => {
    const wrapper = mount(BaseButton, {
      props: { isDisabled: true, label: '저장' }
    })
    expect(wrapper.find('button').attributes('disabled')).toBeDefined()
  })

  it('클릭 시 click 이벤트 emit', async () => {
    const wrapper = mount(BaseButton, {
      props: { isDisabled: false, label: '저장' }
    })
    await wrapper.find('button').trigger('click')
    expect(wrapper.emitted('click')).toBeTruthy()
  })
})
```
