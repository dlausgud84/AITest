# 2. 계획 (Plan)

## 🎯 목표
Nuxt 3 환경에 맞는 디렉토리 구조, 라우팅, 상태 관리 및 데이터 패칭 전략을 설계합니다.

## ✅ 체크리스트
- [ ] **라우팅 및 뷰 설계**
  - [ ] Nuxt 파일 기반 라우팅 구조 설계 (`pages/` 내부 폴더/파일 트리 작성)
  - [ ] 적용할 레이아웃(`layouts/`) 결정 및 `<NuxtPage>` 연결 계획
- [ ] **컴포넌트 설계**
  - [ ] 페이지를 구성할 하위 컴포넌트 분리 계획 (Presentational / Container)
  - [ ] 공통 UI(버튼, 모달 등) 분리 및 스타일링 변수(CSS/SCSS) 계획
- [ ] **상태 관리 및 데이터 패칭 설계**
  - [ ] 전역 상태(Pinia - `stores/`) vs 로컬 상태(`ref`, `reactive`) 사용 기준 정리
  - [ ] SSR 적용 여부 결정 (초기 로드는 `useFetch`, 이벤트 기반 로드는 `$fetch` 활용)
  - [ ] SEO가 필요한 페이지의 경우 메타 태그(`useHead`, `useSeoMeta`) 적용 계획
- [ ] **보안 설계 [🔒신규 추가]**
  - [ ] 민감 데이터(토큰, 비밀번호) Pinia Store에 저장 시 암호화 적용 계획
  - [ ] XSS 방지를 위한 사용자 입력 데이터 이스케이프 처리 계획
  - [ ] CSRF 토큰 관리 및 API 요청 시 자동 포함 계획

---

## 📋 프론트엔드 컴포넌트 설계 규칙

### 컴포넌트 네이밍 원칙 & 문법
- **[💡추가] 기본 문법**: 모든 컴포넌트는 `<script setup lang="ts">`를 사용 (Composition API).
- **[💡추가] 자동 임포트**: `components/`, `composables/`, `utils/`는 Nuxt가 자동 임포트하므로 명시적 `import` 생략.

| 규칙 | 우선순위 | 설명 |
|------|----------|------|
| **PascalCase + 두 단어 이상** | 필수 (A) | `BaseButton.vue` ✅ / `Button.vue` ❌ |
| **Base 접두사 (공통 컴포넌트)** | 강력 권장 (B) | `BaseButton`, `BaseGrid`, `BaseModal` |
| **App 접두사 (레이아웃/전역)** | 강력 권장 (B) | `AppSidebar`, `AppHeader` (layouts 폴더와 구분) |
| **PascalCase 파일명** | 필수 (A) | `MenuManagement.vue` ✅