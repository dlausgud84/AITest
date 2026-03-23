# 6. 최적화 (Optimization)

## 🎯 목표
서비스 런칭 전, Nuxt 3의 렌더링 전략을 극대화하여 초기 로딩 속도(LCP)를 단축하고 웹 접근성을 끌어올립니다.

## ✅ 체크리스트
- [ ] **렌더링 성능 및 청크 최적화**
  - [ ] 초기 렌더링에 당장 필요 없는 무거운 컴포넌트(모달, 차트 등)에 `Lazy` 접두사 적용
  - [ ] `npx nuxi analyze`를 실행하여 번들 사이즈 점검 및 무거운 패키지 분리
  - [ ] 프로덕션 빌드 시 불필요한 `console.log` 제거 설정 적용
  - [ ] 페이지 특성에 맞는 렌더링 전략(`routeRules`)이 `nuxt.config.ts`에 설정되어 있는지 확인
- [ ] **에셋 최적화**
  - [ ] 대용량 이미지 WebP 변환 및 `<NuxtImg>` 모듈 적용 (화면 밖 이미지는 `loading="lazy"`)
  - [ ] 불필요한 전역 CSS/폰트 로딩 제거
- [ ] **웹 접근성 (a11y) 및 SEO**
  - [ ] `img` 태그의 `alt` 속성 누락 확인
  - [ ] 시맨틱 태그(`main`, `nav`, `article`, `section`) 적절한 사용 확인
  - [ ] 마우스 없이 키보드(Tab 키) 네비게이션 작동 여부 및 포커스 상태 확인
- [ ] **보안 최적화 🔒**
  - [ ] **[🔒필수]** Content Security Policy(CSP) 헤더 설정으로 XSS 공격 방지
  - [ ] **[🔒필수]** HTTPS 강제 리다이렉트 및 보안 쿠키(`Secure`, `SameSite=Strict`) 설정 적용
  - [ ] **[🔒필수]** `npx nuxi analyze` 번들 분석으로 API Key·Secret이 클라이언트 번들에 포함되지 않았는지 최종 확인
  - [ ] 민감한 환경 변수 노출 방지 — `NUXT_PUBLIC_` 접두사가 붙은 변수에 비밀 정보 없는지 확인

---

## 🔧 렌더링 성능 최적화 기법

### 1. Nuxt 3 Lazy Loading (지연 로딩)

Nuxt 3는 `pages/` 디렉토리의 라우트를 자동으로 코드 스플리팅합니다.
일반 컴포넌트를 지연 로딩할 때는 템플릿에서 **`Lazy` 접두사**를 사용합니다.

```vue
<template>
  <div>
    <!-- ✅ 항상 필요한 컴포넌트는 일반 사용 -->
    <BaseGrid :data="items" />

    <!-- ✅ 조건부/대형 컴포넌트는 Lazy 접두사로 지연 로딩 -->
    <LazyLanguagePickerPopup v-if="showPicker" @close="showPicker = false" />
    <LazyBaseModal v-if="isModalOpen" @close="isModalOpen = false" />
  </div>
</template>
```

### 2. 렌더링 전략 설정 (nuxt.config.ts)

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  routeRules: {
    '/':          { prerender: true },   // 메인 페이지: 정적 사전 렌더링
    '/login':     { prerender: true },   // 로그인 페이지: 정적
    '/settings/**': { ssr: false },      // 설정 페이지: CSR (인증 후 접근)
    '/api/**':    { cors: true },        // API 경로: CORS 허용
  }
})
```

### 3. 프로덕션 빌드에서 console.log 제거

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  vite: {
    build: {
      // 프로덕션 빌드 시 console.log, console.debug 제거
      terserOptions: {
        compress: {
          drop_console: true,      // console.log 등 제거
          drop_debugger: true,     // debugger 구문 제거
          pure_funcs: ['console.info', 'console.debug', 'console.warn']
        }
      }
    }
  }
})
```

### 4. Content Security Policy (CSP) 설정 🔒

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  nitro: {
    routeRules: {
      '/**': {
        headers: {
          // ✅ CSP 헤더: XSS 공격으로 인한 스크립트 실행 차단
          'Content-Security-Policy':
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline'; " +  // 인라인 스크립트 필요 시
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self'; " +
            "connect-src 'self' http://localhost:8080",  // API 서버 허용

          // ✅ 기타 보안 헤더
          'X-Content-Type-Options': 'nosniff',
          'X-Frame-Options': 'DENY',
          'X-XSS-Protection': '1; mode=block',
          'Referrer-Policy': 'strict-origin-when-cross-origin',
        }
      }
    }
  }
})
```

### 5. API 도메인 사전 연결 (성능 최적화)

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  app: {
    head: {
      link: [
        // ✅ 백엔드 API 서버에 사전 연결하여 첫 API 요청 지연 단축
        { rel: 'preconnect', href: 'http://localhost:8080' },
        // 운영 환경에서는 실제 API URL로 변경
        // { rel: 'preconnect', href: 'https://api.your-domain.com' },
      ]
    }
  }
})
```
