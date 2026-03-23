# 6. 최적화 (Optimization)

## 🎯 목표
서비스 런칭 전, Nuxt 3의 렌더링 전략을 극대화하여 초기 로딩 속도(LCP)를 단축하고 웹 접근성을 끌어올립니다.

## ✅ 체크리스트
- [ ] **렌더링 성능 및 청크 최적화**
  - [ ] 초기 렌더링에 당장 필요 없는 무거운 컴포넌트(모달, 차트 등)에 `Lazy` 접두사 적용
  - [ ] `npx nuxi analyze`를 실행하여 번들 사이즈 점검 및 무거운 패키지 분리
  - [ ] **[💡추가]** 프로덕션 빌드 시 불필요한 `console.log` 제거 설정 적용
  - [ ] **[💡추가]** 페이지 특성에 맞는 렌더링 전략(`routeRules`)이 `nuxt.config.ts`에 설정되어 있는지 확인
- [ ] **에셋 최적화**
  - [ ] 대용량 이미지 WebP 변환 및 `<NuxtImg>` 모듈 적용 (화면 밖 이미지는 `loading="lazy"` 속성 확인)
  - [ ] 불필요한 전역 CSS/폰트 로딩 제거
- [ ] **웹 접근성 (a11y) 및 SEO**
  - [ ] `img` 태그의 `alt` 속성 누락 확인
  - [ ] 시맨틱 태그(`main`, `nav`, `article`, `section`) 적절한 사용 확인
  - [ ] 마우스 없이 키보드(Tab 키) 네비게이션 작동 여부 및 포커스 상태 확인
- [ ] **보안 최적화 [🔒신규 추가]**
  - [ ] Content Security Policy(CSP) 헤더 설정으로 XSS 공격 방지
  - [ ] HTTPS 강제 리다이렉트 및 보안 쿠키 설정 적용
  - [ ] 민감한 환경 변수 노출 방지를 위한 런타임 검증

---

## 🔧 렌더링 성능 최적화 기법

### 1. Nuxt 3 전용 Lazy Loading (지연 로딩) [💡수정: 완벽한 자동화]
Nuxt 3는 `pages/` 디렉토리의 라우트를 자동으로 코드 스플리팅합니다. 수동으로 `import`할 필요가 없습니다. 일반 컴포넌트를 지연 로딩할 때는 템플릿에서 **`Lazy` 접두사**를 사용합니다.

```vue
<template>
  <div>
    <BaseGrid :data="items" />
    
    <LazyBaseModal v-if="isModalOpen" @close="isModalOpen = false" />
  </div>
</template>