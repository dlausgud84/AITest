# 5. 검증 (Verification)

## 🎯 목표
구현된 기능이 정상적으로 동작하며, 코드 컨벤션, 타입 안정성, 그리고 빌드 환경(SSR/CSR)에 문제가 없는지 확인합니다.

## ✅ 체크리스트
- [ ] **기능 검증 및 브라우저 테스트**
  - [ ] 브라우저 개발자 도구 콘솔에 에러/경고가 없는지 확인
  - [ ] **[🚨필수]** 콘솔에 `Hydration node mismatch` 경고가 발생하지 않는지 확인 (SSR/CSR 불일치)
  - [ ] 크로스 브라우징 및 반응형(모바일/태블릿/PC) 화면 깨짐 테스트
  - [ ] Nuxt Devtools 패널을 열어 페이지 상태(State) 및 Payload가 의도대로 로드되었는지 시각적 점검
- [ ] **코드 품질 및 정적 분석**
  - [ ] `npm run lint` (ESLint/Prettier) 에러 및 경고 통과 확인
  - [ ] `npx vue-tsc --noEmit` 명령어로 TypeScript 타입 에러 완전 무결성 체크
- [ ] **빌드 및 렌더링 검증**
  - [ ] `npm run build` 명령어 정상 수행 및 번들 사이즈 경고 확인
  - [ ] 빌드 후 로컬 프리뷰(`npm run preview`)에서 SSR 정상 동작 확인
- [ ] **보안 검증 🔒**
  - [ ] 사용자 입력 데이터에 XSS 공격 패턴(`<script>alert(1)</script>`)을 넣었을 때 이스케이프 처리 확인
  - [ ] **[🔒필수]** 브라우저 개발자 도구 → Application → Local Storage에 인증 토큰이 저장되어 있지 않은지 확인
  - [ ] **[🔒필수]** `npx nuxi analyze` 빌드 번들 분석 후, 클라이언트 번들에 API Key·Secret 등 민감 정보가 포함되어 있지 않은지 확인
  - [ ] **[🔒필수]** `NUXT_PUBLIC_` 접두사가 붙은 환경변수에 비밀 정보가 없는지 최종 확인
  - [ ] HTTPS 프로토콜 적용 및 보안 쿠키(`Secure`, `SameSite=Strict`) 설정 확인

---

## 📋 통합 테스트 (백엔드 API 연동)

### API 연동 확인

> **🚨 주의:** Vitest에서 실제 백엔드 서버를 직접 호출하는 방식은 서버 구동 상태나 네트워크에 의존하므로 CI/CD 환경에서 Flaky Test 위험이 있습니다.
> 가급적 MSW(Mock Service Worker)를 사용하거나, 실제 연동 테스트는 Playwright 같은 E2E 도구를 사용하는 것을 권장합니다.

```typescript
// (로컬 개발 환경 전용 연동 테스트)
// composables/__tests__/useMenuAPI.integration.test.ts
import { describe, it, expect } from 'vitest'
import { useMenuAPI } from '../useMenuAPI'

describe('useMenuAPI 통합 테스트 (백엔드 실행 중일 때만 실행)', () => {
  it('실제 백엔드 API 메뉴 조회', async () => {
    // 주의: 이 테스트는 백엔드가 8080 포트에서 실행 중이어야만 통과합니다.
    const { menus, fetchMenusByGroup } = useMenuAPI()
    await fetchMenusByGroup('MAIN')
    expect(Array.isArray(menus.value)).toBe(true)
  })
})
```

### 번들 보안 검사 🔒

```bash
# 빌드 후 번들 파일에서 민감 정보 노출 여부 확인
npm run build

# .output/public/_nuxt/ 폴더에서 민감 정보 검색
grep -r "secret\|apikey\|password\|token" .output/public/_nuxt/ --include="*.js"

# 발견된 경우: NUXT_PUBLIC_ 접두사 제거 후 서버사이드 전용으로 변경
```

### 빌드 정상 동작 확인

```bash
# TypeScript 타입 체크
npx vue-tsc --noEmit

# ESLint 검사
npm run lint

# 빌드
npm run build

# 번들 분석
npx nuxi analyze

# 로컬 프리뷰 (SSR 포함)
npm run preview
```
