# 1. 조사 (Research)

## 🎯 목표
요구사항을 명확히 이해하고, 개발 전 필요한 API 명세와 UI/UX 요소, 그리고 Nuxt 3 환경의 렌더링 전략을 파악합니다.

## ✅ 체크리스트

### 1. 요구사항 및 UI/UX 분석
- [ ] 기획서 및 디자인 요구사항 확인 완료
- [ ] 반응형 웹(모바일/태블릿/PC) 적용 범위 및 브레이크포인트 확인
- [ ] 복잡한 폼(Form) 입력 시 필요한 Validation(유효성 검사) 규칙 파악 (예: 이메일 형식, 필수값 등)

### 2. API 연동 및 데이터 조사
- [ ] 백엔드 API 명세서(Swagger 등) 확인 및 엔드포인트 파악
- [ ] Request / Response 데이터 구조 파악 (TypeScript Interface/Type 설계용)
- [ ] API 호출 시 필요한 쿼리 파라미터(페이징, 정렬, 필터) 요구사항 확인
- [ ] **[🔒필수]** 민감 데이터(토큰, 비밀번호 등) 전송 시 HTTPS 암호화 요구사항 확인
- [ ] **[🔒필수]** API 응답에서 민감 데이터(비밀번호 해시 등)가 포함되어 내려오지 않는지 백엔드와 협의

### 3. 기존 리소스 및 상태 파악
- [ ] 재사용 가능한 UI 컴포넌트(`components/`) 존재 여부 확인
- [ ] 재사용 가능한 비즈니스 로직(`composables/`) 존재 여부 확인
- [ ] **[🔒필수]** 기존 composable이 에러 상태를 올바르게 처리하는지 확인 (빈 catch 블록 방지)
- [ ] **[🔒필수]** 쿠키 보안 속성 확인 (`httpOnly`, `SameSite=Strict`, `Secure`) — 인증 토큰은 반드시 httpOnly 쿠키 사용

### 4. Nuxt 3 렌더링 및 SEO 전략 파악
- [ ] 검색 엔진 최적화(SEO) 필요 여부 확인 (동적 Meta 태그 필요성)
- [ ] 초기 렌더링 시 서버 사이드 데이터 패칭(`useFetch`, `useAsyncData`) 적용 여부 판단
- [ ] **[🔒필수]** `window`, `document` 객체 직접 접근 시 `process.client` 또는 `onMounted` 내에서만 사용 (SSR Hydration mismatch 방지)

---

## 📋 포트 및 환경 변수 확인

| 항목 | 값 | 비고 |
|------|-----|------|
| **프론트엔드 포트** | 3000 | 로컬 개발 서버 포트 확인 |
| **백엔드 API 포트** | 8080 | API 엔드포인트 주소 확인 |
| **개발 환경 변수** | `.env.development` | `NUXT_PUBLIC_API_BASE_URL=http://localhost:8080` |
| **운영 환경 변수** | `.env.production` | 실제 운영 서버 API URL 설정 상태 |

**확인 항목**:
- [ ] `.env.development` 파일 존재 및 기본값 설정 확인
- [ ] `.env.production` 파일에서 하드코딩된 비밀값 없는지 확인 (API Key, Secret 등은 서버 환경변수로 관리)
- [ ] **[🔒필수]** `NUXT_PUBLIC_` 접두사가 붙은 환경변수는 브라우저에 노출됨 — 비밀 정보는 절대 `NUXT_PUBLIC_` 접두사 사용 금지
- [ ] `nuxt.config.ts`에서 `runtimeConfig.public`에 민감한 값이 포함되어 있지 않은지 확인
