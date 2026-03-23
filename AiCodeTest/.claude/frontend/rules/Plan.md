# 2. 계획 (Plan)

## 🎯 목표
Nuxt 3 환경에 맞는 디렉토리 구조, 라우팅, 상태 관리 및 데이터 패칭 전략을 설계합니다.

## ✅ 체크리스트
- [ ] **라우팅 및 뷰 설계**
  - [ ] Nuxt 파일 기반 라우팅 구조 설계 (`pages/` 내부 폴더/파일 트리 작성)
  - [ ] 적용할 레이아웃(`layouts/`) 결정 및 `<NuxtPage>` 연결 계획
  - [ ] 인증이 필요한 페이지에 대한 라우트 가드(Nuxt Middleware) 설계
- [ ] **컴포넌트 설계**
  - [ ] 페이지를 구성할 하위 컴포넌트 분리 계획 (Presentational / Container)
  - [ ] 공통 UI(버튼, 모달 등) 분리 및 스타일링 변수(CSS 변수) 계획
- [ ] **상태 관리 및 데이터 패칭 설계**
  - [ ] 전역 상태(`useState`) vs 로컬 상태(`ref`, `reactive`) 사용 기준 정리
  - [ ] SSR 적용 여부 결정 (초기 로드는 `useFetch`, 이벤트 기반 로드는 `$fetch` 활용)
- [ ] **보안 설계 🔒**
  - [ ] **[🔒필수]** 인증 토큰은 `localStorage` 또는 Pinia Store에 저장 금지 — 반드시 `httpOnly` 쿠키 사용 (XSS 공격으로부터 토큰 탈취 방지)
  - [ ] **[🔒필수]** XSS 방지를 위해 사용자 입력 데이터를 `v-html`에 직접 바인딩 금지
  - [ ] CSRF 토큰 관리 및 API 요청 시 자동 포함 계획

---

## 📋 프론트엔드 컴포넌트 설계 규칙

### 컴포넌트 네이밍 원칙 & 문법

- **기본 문법**: 모든 컴포넌트는 `<script setup lang="ts">`를 사용 (Composition API)
- **자동 임포트**: `components/`, `composables/`, `utils/`는 Nuxt가 자동 임포트하므로 명시적 `import` 생략

| 규칙 | 우선순위 | 설명 |
|------|----------|------|
| **PascalCase + 두 단어 이상** | 필수 (A) | `BaseButton.vue` ✅ / `Button.vue` ❌ |
| **Base 접두사 (공통 컴포넌트)** | 강력 권장 (B) | `BaseButton`, `BaseGrid`, `BaseModal` |
| **App 접두사 (레이아웃/전역)** | 강력 권장 (B) | `AppSidebar`, `AppHeader` |
| **PascalCase 파일명** | 필수 (A) | `MenuManagement.vue` ✅ / `menu-management.vue` ❌ |
| **Boolean Props 접두사** | 권장 (C) | `isDisabled`, `isLoading`, `hasPagination` |

### Props 설계 규칙

```typescript
// ✅ TypeScript 기반 Props 선언 (기본값은 withDefaults 활용)
const props = withDefaults(defineProps<{
  isDisabled?: boolean
  size?: 'sm' | 'md' | 'lg'
  items: string[]
}>(), {
  isDisabled: false,
  size: 'md',
  items: () => []  // 배열/객체 기본값은 factory 함수로
})
```

### 라우트 가드 (Nuxt Middleware) 설계

```typescript
// middleware/auth.global.ts — 모든 페이지에 인증 검사 적용
export default defineNuxtRouteMiddleware((to) => {
  const publicPages = ['/login']  // 인증 없이 접근 가능한 페이지 목록

  // 공개 페이지는 통과
  if (publicPages.includes(to.path)) return

  // 인증 토큰 확인 (httpOnly 쿠키에서 확인 — useCookie 활용)
  const token = useCookie('auth-token')
  if (!token.value) {
    return navigateTo('/login')
  }
})
```

### 환경변수 사용 규칙 🔒

```typescript
// ✅ 올바른 패턴: 공개해도 되는 값만 NUXT_PUBLIC_ 사용
// .env.development
// NUXT_PUBLIC_API_BASE_URL=http://localhost:8080

// composables/useXxxAPI.ts
const config = useRuntimeConfig()
const baseUrl = config.public.apiBaseUrl  // ✅ 브라우저에서 접근 가능

// ❌ 절대 금지: 비밀 값에 NUXT_PUBLIC_ 접두사 사용
// NUXT_PUBLIC_SECRET_KEY=my-secret  // ← 이러면 클라이언트에 노출됨!
```
