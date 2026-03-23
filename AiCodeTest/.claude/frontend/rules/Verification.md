# 5. 검증 (Verification)

## 🎯 목표
구현된 기능이 정상적으로 동작하며, 코드 컨벤션, 타입 안정성, 그리고 빌드 환경(SSR/CSR)에 문제가 없는지 확인합니다.

## ✅ 체크리스트
- [ ] **기능 검증 및 브라우저 테스트**
  - [ ] 브라우저 개발자 도구 콘솔에 에러/경고가 없는지 확인
  - [ ] **[💡추가 🚨필수]** 콘솔에 `Hydration node mismatch` 경고가 발생하지 않는지 확인 (SSR/CSR 불일치 점검)
  - [ ] 크로스 브라우징 및 반응형(모바일/태블릿/PC) 화면 깨짐 테스트
  - [ ] **[💡추가]** Nuxt Devtools 패널을 열어 페이지 상태(State) 및 Payload가 의도대로 로드되었는지 시각적 점검
- [ ] **코드 품질 및 정적 분석**
  - [ ] `npm run lint` (ESLint/Prettier) 에러 및 경고 통과 확인
  - [ ] `npx vue-tsc --noEmit` 명령어로 TypeScript 타입 에러 완전 무결성 체크
- [ ] **빌드 및 렌더링 검증**
  - [ ] `npm run build` 명령어 정상 수행 및 번들 사이즈 경고 확인
  - [ ] 빌드 후 로컬 프리뷰(`npm run preview`)에서 SSR 정상 동작 및 화면 깜빡임 여부 확인
- [ ] **보안 검증 [🔒신규 추가]**
  - [ ] 사용자 입력 데이터에 XSS 공격 패턴을 넣었을 때 적절한 이스케이프 처리 확인
  - [ ] 민감 데이터(토큰, 비밀번호)가 브라우저 콘솔이나 로컬 스토리지에 노출되지 않는지 확인
  - [ ] HTTPS 프로토콜 적용 및 보안 헤더 설정 확인

---

## 📋 통합 테스트 (백엔드 API 연동)

### API 연동 확인 [💡수정: Flaky Test 주의사항 추가]

> **🚨 주의:** Vitest에서 실제 백엔드 서버를 직접 호출하는 방식은 서버 구동 상태나 네트워크에 의존하므로 CI/CD 환경에서 테스트가 깨질(Flaky) 위험이 높습니다. 가급적 MSW(Mock Service Worker)를 사용하거나, 실제 연동 테스트는 Playwright 같은 E2E 도구를 사용하는 것을 권장합니다.

```typescript
// composables/__tests__/useMenuAPI.integration.test.ts
// (로컬 개발 환경 전용 연동 테스트)
import { describe, it, expect } from 'vitest'
import { useMenuAPI } from '../useMenuAPI'

describe('useMenuAPI 통합 테스트 (Local E2E)', () => {
  it('실제 백엔드 API 메뉴 조회', async () => {
    // 주의: 이 테스트는 백엔드가 8080 포트에서 실행 중이어야만 통과합니다.
    const { fetchMenus }