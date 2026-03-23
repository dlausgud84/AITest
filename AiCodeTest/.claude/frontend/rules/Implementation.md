# 4. 구현 (Implementation)

## 🎯 목표
설계된 내용을 바탕으로 TypeScript 타입 정의부터 UI 조립, SSR을 고려한 데이터 패칭까지 실제 코드를 작성합니다.

## ✅ 체크리스트
- [ ] **타입 정의 (TypeScript)**
  - [ ] API Request / Response 타입 및 Interface 정의 (`composables/` 또는 `types/` 폴더)
- [ ] **로직 구현 (Composables & State)**
  - [ ] `composables/` 내 API 호출 및 비즈니스 로직 구현 (SSR 캐싱 고려)
  - [ ] **[🔒필수]** 인증 토큰은 `localStorage` 저장 금지 — `httpOnly` 쿠키(`useCookie`) 사용
  - [ ] **[🔒필수]** 에러 상태 처리: 빈 `catch` 블록 금지, 반드시 사용자에게 에러 메시지 표시
- [ ] **UI 및 라우팅 구현**
  - [ ] `components/` UI 마크업 및 스타일링 (`defineModel` 적극 활용)
  - [ ] `pages/` 컴포넌트 조립 및 라우팅 연결
  - [ ] **[🔒필수]** `v-html` 디렉티브에 사용자 입력 데이터 직접 바인딩 절대 금지 (XSS 취약점)
- [ ] **UX 처리 및 SEO**
  - [ ] 로딩 상태(Skeleton, Spinner) 및 에러 화면(Fallback UI) 구현
  - [ ] `useHead` 또는 `useSeoMeta`를 활용한 페이지별 동적 SEO 태그 적용 (필요 시)

---

## 📋 프론트엔드 코드 작성 규칙

### 1. Vue 3 Composition API 필수 패턴

```vue
<script setup lang="ts">
// ✅ 컴포넌트명 명시 (Vue DevTools 디버깅 용이)
defineOptions({ name: 'BaseInput' })

// ✅ Props: TypeScript 기반 선언
const props = withDefaults(defineProps<{
  isDisabled?: boolean
  size?: 'sm' | 'md' | 'lg'
}>(), {
  isDisabled: false,
  size: 'md'
})

// ✅ v-model 양방향 바인딩: Vue 3.4+ defineModel 사용
const modelValue = defineModel<string>()

// ✅ Emits: 명시적 타입 선언
const emit = defineEmits<{
  search: [keyword: string]
  cancel: []
}>()
</script>

<template>
  <input
    v-model="modelValue"
    :disabled="isDisabled"
    :class="['base-input', `base-input--${size}`]"
  />
</template>
```

### 2. Composable (API 호출 로직) 패턴

```typescript
// composables/useUserAPI.ts

export interface UserRow {
  userId: string
  userName: string
  // ❌ 응답에 비밀번호 포함 금지 — 백엔드에서 @JsonIgnore 처리되어야 함
}

interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

export const useUserAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const userList  = ref<UserRow[]>([])
  const isLoading = ref(false)
  const errorMsg  = ref('')

  const fetchUserList = async () => {
    isLoading.value = true
    errorMsg.value = ''
    try {
      const res = await $fetch<ApiResponse<UserRow[]>>(`${baseUrl}/api/users`)
      if (res.success) {
        userList.value = res.data ?? []
      } else {
        errorMsg.value = res.message  // ✅ 빈 catch 아님 — 에러 메시지 표시
      }
    } catch (e) {
      // ✅ 빈 catch 금지: 반드시 사용자에게 에러 상태 전달
      errorMsg.value = '목록 조회 중 오류가 발생했습니다.'
      console.error('fetchUserList error:', e) // 개발 디버깅용 (운영 배포 시 제거)
    } finally {
      isLoading.value = false
    }
  }

  return { userList, isLoading, errorMsg, fetchUserList }
}
```

### 3. XSS 방지 규칙 🔒

```vue
<template>
  <!-- ❌ 절대 금지: 사용자 입력 데이터를 v-html에 직접 바인딩 -->
  <!-- <div v-html="userInputContent"></div>  ← XSS 공격에 직접 노출! -->

  <!-- ✅ 올바른 방법: 텍스트는 {{ }} 보간법 사용 (자동 이스케이프) -->
  <div>{{ userInputContent }}</div>

  <!-- ✅ 만약 HTML 렌더링이 반드시 필요하다면: sanitize-html 등 라이브러리로 정제 후 사용 -->
  <!-- <div v-html="sanitizedContent"></div> -->
</template>

<script setup lang="ts">
// ✅ v-html이 반드시 필요한 경우: DOMPurify 등으로 정제
import DOMPurify from 'dompurify'

const userInputContent = ref('<script>alert("xss")</script>')
// Vue {{ }} 보간법은 자동으로 이스케이프하여 XSS 방지

// v-html 사용이 불가피한 경우 정제 후 사용
const sanitizedContent = computed(() =>
  DOMPurify.sanitize(userInputContent.value)
)
</script>
```

### 4. 인증 토큰 관리 (httpOnly 쿠키) 🔒

```typescript
// ✅ 올바른 패턴: httpOnly 쿠키 (XSS로부터 안전)
// → 서버에서 Set-Cookie 헤더로 설정하거나, Nuxt useCookie 사용
const token = useCookie('auth-token', {
  httpOnly: true,    // JavaScript에서 접근 불가 (XSS 방어)
  secure: true,      // HTTPS에서만 전송
  sameSite: 'strict' // CSRF 방어
})

// ❌ 절대 금지: localStorage에 토큰 저장
// localStorage.setItem('token', accessToken)  ← XSS로 토큰 탈취 가능

// ❌ 절대 금지: Pinia Store에 토큰 저장 (메모리에 평문 노출)
// authStore.token = accessToken  ← XSS로 탈취 가능
```

### 5. SSR 환경 주의사항

```typescript
// ❌ SSR에서 직접 접근 시 에러 발생
window.localStorage.getItem('key')  // window is not defined (서버 환경)
document.querySelector('.el')       // document is not defined (서버 환경)

// ✅ 올바른 패턴: onMounted 또는 process.client 가드
onMounted(() => {
  // 클라이언트에서만 실행 — window/document 접근 안전
  const value = window.localStorage.getItem('key')
})

// ✅ 또는 Nuxt의 useNuxtApp 훅 활용
if (process.client) {
  window.addEventListener('resize', handler)
}
```

### 6. 에러 바운더리 (error.vue)

```vue
<!-- error.vue: Nuxt 전역 에러 처리 페이지 -->
<template>
  <div class="error-page">
    <h1>{{ error.statusCode === 404 ? '페이지를 찾을 수 없습니다' : '오류가 발생했습니다' }}</h1>
    <p>{{ error.message }}</p>
    <button @click="handleError">홈으로 돌아가기</button>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{ error: { statusCode: number; message: string } }>()

function handleError() {
  clearError({ redirect: '/' })
}
</script>
```
