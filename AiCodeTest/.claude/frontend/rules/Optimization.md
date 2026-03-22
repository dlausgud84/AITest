# 6. 최적화 (Optimization)

## 🎯 목표
서비스 런칭 전, 프론트엔드 렌더링 성능 향상과 접근성을 끌어올립니다.

## ✅ 체크리스트
- [ ] **렌더링 성능 및 청크 최적화**
  - [ ] 초기 렌더링에 불필요한 컴포넌트 지연 로딩(`Lazy` 접두사) 적용
  - [ ] `npx nuxi analyze`를 실행하여 번들 사이즈 점검 및 무거운 패키지 분리/교체
- [ ] **에셋 최적화**
  - [ ] 대용량 이미지 WebP 변환 및 `@nuxt/image` 모듈 적용 (필요시)
  - [ ] 불필요한 전역 CSS/폰트 로딩 제거
- [ ] **웹 접근성 (a11y)**
  - [ ] `img` 태그의 `alt` 속성 누락 확인
  - [ ] 시맨틱 태그(`main`, `nav`, `article` 등) 적절한 사용 확인
  - [ ] 키보드(Tab 키) 네비게이션 작동 확인

---

## 🔧 렌더링 성능 최적화 기법

### Lazy Loading (지연 로딩) 적용

```typescript
// ❌ 단순 import (초기에 로드됨)
import MenuManagement from '~/pages/menu-management.vue'

// ✅ 지연 로딩
import { defineAsyncComponent } from 'vue'
const MenuManagement = defineAsyncComponent(() => 
  import('~/pages/menu-management.vue')
)
```

### 번들 분석 (최적화)

```bash
# 번들 크기 분석
npx nuxi analyze

# 출력: total 1234 KB
#   vendors/vue: 456 KB
#   vendors/nuxt: 234 KB
#   app: 544 KB

# 무거운 패키지 결정 및 교체
npm ls
npm update
```

### 이미지 최적화

```vue
<!-- WebP 포맷 사용 -->
<NuxtImg
  src="/menu-logo.png"
  format="webp"
  alt="메뉴 로고"
  width="100"
  height="100"
/>
```

### 전역 CSS 주의

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  css: [
    '~/assets/css/main.css',      // ✅ 필요한 것만
    // '~/assets/css/debug.css',  // ❌ 불필요한 것 제거
  ]
})
```

---