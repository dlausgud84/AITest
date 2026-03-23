# 4. 구현 (Implementation)

## 🎯 목표
설계된 내용을 바탕으로 TypeScript 타입 정의부터 UI 조립, SSR을 고려한 데이터 패칭까지 실제 코드를 작성합니다.

## ✅ 체크리스트
- [ ] **타입 정의 (TypeScript)**
  - [ ] API Request / Response 타입 및 Interface 정의 (`types/` 폴더)
- [ ] **로직 구현 (Composables & State)**
  - [ ] `composables/` 내 API 호출 및 비즈니스 로직 구현 (SSR 캐싱 고려)
  - [ ] 전역 상태(Pinia) Store 구현 및 사용
  - [ ] **[🔒보안추가]** 민감 데이터(토큰, 비밀번호)는 localStorage 대신 httpOnly 쿠키 사용
- [ ] **UI 및 라우팅 구현**
  - [ ] `components/` UI 마크업 및 스타일링 (`defineModel` 적극 활용)
  - [ ] `pages/` 컴포넌트 조립 및 라우팅 연결
- [ ] **UX 처리 및 SEO**
  - [ ] 로딩 상태(Skeleton, Spinner) 및 에러 화면(Fallback UI) 구현
  - [ ] `useHead` 또는 `useSeoMeta`를 활용한 페이지별 동적 SEO 태그 적용

---

## 📋 프론트엔드 코드 작성 규칙

### Vue 3 Composition API 필수 패턴

```vue
<script setup lang="ts">
// ✅ 필수: 컴포넌트명 명시 (디버깅 용이)
defineOptions({ name: 'BaseInput' })

// ✅ Props: TypeScript 기반 선언 (기본값 설정은 withDefaults 활용)
const props = withDefaults(defineProps<{ 
  isDisabled?: boolean 
  size?: 'sm' | 'md' | 'lg'
}>(), {
  isDisabled: false,
  size: 'md'
})

// [💡수정] ✅ v-model 양방향 바인딩: Vue 3.4+ defineModel 사용 (강력 권장)
const modelValue = defineModel<string>()

// ✅ Emits: 모델 업데이트 외의 커스텀 이벤트만 명시적 선언
const emit = defineEmits<{
  search: [keyword: string],
  cancel: []
}>()
</script>

<template>
  <input 
    v-model="modelValue" 
    :disabled="isDisabled