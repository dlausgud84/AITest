# 4. 구현 (Implementation)

## 🎯 목표
설계된 내용을 바탕으로 TypeScript 타입 정의부터 UI 조립까지 실제 코드를 작성합니다.

## ✅ 체크리스트
- [ ] **타입 정의 (TypeScript)**
  - [ ] API Request / Response 타입 및 Interface 정의 (`types/` 또는 연관 파일 내)
- [ ] **로직 구현**
  - [ ] `composables/` 및 API 호출 로직 구현
  - [ ] 전역 상태(Pinia) Store 구현 (필요시)
- [ ] **UI 및 라우팅 구현**
  - [ ] `components/` UI 마크업 및 스타일링 (SCSS/Tailwind 등 적용)
  - [ ] `pages/` 컴포넌트 조립 및 라우팅 연결
- [ ] **UX 처리 및 SEO**
  - [ ] 로딩 상태(Skeleton, Spinner) 및 에러 화면(Fallback UI) 구현
  - [ ] `useHead` 또는 `useSeoMeta`를 활용한 SEO 태그 적용

---

## 📋 프론트엔드 코드 작성 규칙

### Vue 3 Composition API 필수 패턴

```vue
<script setup lang="ts">
// ✅ 필수: defineOptions로 컴포넌트명 명시
defineOptions({ name: 'MyComponent' })

// ✅ Props: defineProps (TypeScript 구문)
const props = defineProps<{ 
  isDisabled?: boolean 
}>()

// ✅ Emits: defineEmits 로 명시적 선언
const emit = defineEmits<{
  confirm: [],
  cancel: []
}>()

// ✅ 반응성: ref, reactive, computed
const count = ref(0)
const state = reactive({ name: '' })
const doubled = computed(() => count.value * 2)

// ✅ 생명주기: onMounted, onUnmounted 등
onMounted(() => { ... })

// ✅ 이벤트 핸들러
const handleClick = () => {}
</script>
```

### Composable 작성 규칙

```typescript
// composables/useMenuAPI.ts
// ✅ 'use' 접두어 필수
export const useMenuAPI = () => {
  // API 호출 로직
  const fetchMenus = async () => { ... }
  
  // 반응형 상태
  const menus = ref<Menu[]>([])
  const isLoading = ref(false)
  
  // 반환: 상태 + 메서드
  return { menus, isLoading, fetchMenus }
}
```

**VueUse 활용**: 신규 작성 전 VueUse에 구현된 패턴 확인 (중복 구현 방지)

### Props & Emits 명명 규칙

```typescript
// Props
defineProps<{
  isDisabled?: boolean      // Boolean: 'is' 접두어
  hasError?: boolean        // Boolean: 'has' 접두어
  canDelete?: boolean       // Boolean: 'can' 접두어
  size?: 'sm' | 'md' | 'lg' // Enum: 옵션명
  items?: any[]             // Array: 복수형
}>()

// Emits (camelCase)
defineEmits<{
  'update:modelValue': [value: string],
  'confirm': [data: any],
  'cancel': [],
}>()
```

### 템플릿 내 Props 바인딩

```vue
<!-- Props는 camelCase로 정의, 템플릿에서는 kebab-case로 사용 -->
<BaseButton
  btn-type="create"
  :is-disabled="isSubmitting"
  :is-loading="isLoading"
  @click="handleCreate"
/>
```

---

## 📋 프론트엔드 에러 처리 패턴

### Composable 내 에러 처리

```typescript
export const useMenuAPI = () => {
  const error = ref<string | null>(null)
  
  const fetchMenus = async () => {
    try {
      // API 호출
      const response = await $fetch('/api/menus')
      menus.value = response.data
    } catch (err) {
      // 에러 메시지 저장
      error.value = err.message || '데이터 조회 실패'
      console.error('메뉴 조회 오류:', err)
    }
  }
  
  return { menus, error, fetchMenus }
}
```

### 컴포넌트 내 에러 표시

```vue
<script setup lang="ts">
const { menus, error, fetchMenus } = useMenuAPI()

onMounted(() => {
  fetchMenus()
})
</script>

<template>
  <!-- 에러 메시지 표시 -->
  <div v-if="error" class="alert alert-error">
    {{ error }}
  </div>
  
  <!-- 정상 데이터 표시 -->
  <div v-else>
    <div v-for="menu in menus" :key="menu.id">
      {{ menu.name }}
    </div>
  </div>
</template>
```

### 전역 에러 핸들러

```typescript
// plugins/errorHandler.ts
export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.hook('app:error', (error) => {
    console.error('전역 에러:', error)
    // 전역 토스트/알럿 표시
    showGlobalAlert(error.message)
  })
})
```

---

## 📋 프론트엔드 스킬 사용 지침

### Vue 파일 작성 시
- **스킬**: `vue-best-practices`
- **규칙**: Vue 3 공식 스타일 가이드, `<script setup>` 패턴 필수
- **예**: 반응시스템 이해, Composition API 사용, Props/Emits 명시 선언

### Nuxt 파일 작업 시
- **스킬**: `nuxt`
- **범위**: pages/, composables/, plugins/, middleware/, nuxt.config.ts
- **주의**: SSR/CSR 구분, auto-imports 활용, 하이브리드 렌더링 이슈 확인

### Composable 신규 작성 전
- **스킬**: `vueuse`
- **원칙**: VueUse에 이미 구현된 패턴 먼저 확인 (중복 구현 방지)
- **예**: `useClipboard`, `useLocal Storage`, `useFetch` 등

### 라우팅/미들웨어 작업 시
- **스킬**: `vue-router-best-practices`
- **내용**: navigation guards, route params, auth middleware
- **프로젝트 예**: `middleware/auth.global.ts` (전역 인증)

### 테스트 코드 작성 시
- **스킬**: `vitest` + `vue-testing-best-practices`
- **도구**: Vitest (번들러 독립) + Vue Test Utils
- **범위**: 단위 테스트 (composables/components) + Edge Cases

### UI/UX 검토 요청 시
- **스킬**: `web-design-guidelines`
- **체크**: 접근성(a11y), 반응형 디자인, UX 가이드라인