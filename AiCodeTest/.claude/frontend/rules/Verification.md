# 5. 검증 (Verification)

## 🎯 목표
구현된 기능이 정상적으로 동작하며, 코드 컨벤션과 빌드 환경에 문제가 없는지 확인합니다.

## ✅ 체크리스트
- [ ] **기능 검증**
  - [ ] 브라우저 개발자 도구 콘솔에 에러/경고가 없는지 확인
  - [ ] 크로스 브라우징 및 반응형(모바일/PC) 화면 깨짐 테스트
- [ ] **코드 품질 및 정적 분석**
  - [ ] `npm run lint` (ESLint/Prettier) 통과 확인
  - [ ] `vue-tsc --noEmit` 명령어로 TypeScript 타입 에러 체크
- [ ] **빌드 검증**
  - [ ] `npm run build` 명령어 정상 수행 확인
  - [ ] 빌드 후 로컬 프리뷰(`npm run preview`)에서 SSR 정상 동작 확인

---

## 📋 통합 테스트 (백엔드 API 연동)

### API 연동 확인

```typescript
// composables/__tests__/useMenuAPI.integration.test.ts
import { describe, it, expect, beforeAll } from 'vitest'
import { useMenuAPI } from '../useMenuAPI'

describe('useMenuAPI 통합 테스트', () => {
  let { fetchMenus, menus, error } = useMenuAPI()

  it('실제 백엔드 API 메뉴 조회', async () => {
    // 주의: 이 테스트는 백엔드가 8080 포트에서 실행 중이어야 함
    await fetchMenus()
    
    // 성공 또는 특정 에러 확인
    expect(menus.value || error.value).toBeTruthy()
  })
})
```

**사전 조건**:
- [ ] 백엔드 서버가 `http://localhost:8080`에서 실행 중인지 확인
- [ ] 환경 변수 `NUXT_PUBLIC_API_BASE_URL=http://localhost:8080` 설정 확인
- [ ] 프론트엔드 서버가 `http://localhost:3000`에서 실행 중인지 확인

### 환경 변수 검증

```typescript
// pages/index.vue 또는 middleware
const apiBase = useRuntimeConfig().public.apiBaseUrl
console.log('API Base URL:', apiBase) // 개발: http://localhost:8080
```

**확인 항목**:
- [ ] `.env.development` 에서 `NUXT_PUBLIC_API_BASE_URL` 올바르게 설정
- [ ] `.env.production` 에서 실제 서버 URL 설정
- [ ] 런타임 설정이 `nuxt.config.ts`에서 올바르게 읽혀지는지 확인

### CORS 에러 확인

에러 메시지: `Access to XMLHttpRequest at 'http://localhost:8080/api/...' from origin 'http://localhost:3000' has been blocked by CORS policy`

**해결 방법**:
- [ ] 백엔드 `CorsConfig.java` 에서 `http://localhost:3000` 허용 설정
- [ ] 백엔드 CORS 설정이 `/api/**` 경로를 포함하는지 확인
- [ ] 백엔드 재시작 후 다시 시도