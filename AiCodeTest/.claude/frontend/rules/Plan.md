# 2. 계획 (Plan)

## 🎯 목표
Nuxt 3 환경에 맞는 디렉토리 구조, 라우팅, 상태 관리 및 데이터 패칭 전략을 설계합니다.

## ✅ 체크리스트
- [ ] **라우팅 및 뷰 설계**
  - [ ] Nuxt 라우팅 구조 설계 (`pages/` 내부 파일 트리 작성)
  - [ ] 사용할 레이아웃(`layouts/`) 결정
- [ ] **컴포넌트 설계**
  - [ ] 페이지를 구성할 하위 컴포넌트 분리 계획 (Presentational / Container)
  - [ ] 공통 UI(버튼, 모달 등) 분리 및 스타일링 변수 계획
- [ ] **상태 관리 및 데이터 패칭 설계**
  - [ ] 전역 상태(Pinia) vs 로컬 상태(`ref`, `reactive`) 사용 기준 정리
  - [ ] SSR 적용 여부 결정 (`useFetch`, `useAsyncData` 활용 계획)
  - [ ] SEO가 필요한 페이지의 경우 메타 태그(`useHead`) 적용 계획

---

## 📋 프론트엔드 컴포넌트 설계 규칙

### 컴포넌트 네이밍 원칙

| 규칙 | 우선순위 | 설명 |
|------|----------|------|
| **PascalCase + 두 단어 이상** | 필수 (A) | `BaseButton.vue` ✅ / `Button.vue` ❌ |
| **Base 접두사 (공통 컴포넌트)** | 강력 권장 (B) | `BaseButton`, `BaseGrid`, `BaseModal` |
| **App 접두사 (레이아웃)** | 강력 권장 (B) | `AppLayout`, `AppSidebar` |
| **PascalCase 파일명** | 필수 (A) | `MenuManagement.vue` ✅ / `menu-management.vue` ❌ |

### 공통 컴포넌트 가이드

#### 1. BaseButton.vue
- **9가지 타입**: search/execute/create/update/delete/excel/close/reset/custom
- **각 타입마다 고유 색상 + SVG 아이콘 자동 적용**
- **Props**: `btnType`, `size`, `isDisabled`, `isLoading`, `customLabel`, `customColor`, `customIconSvg`
- **참고**: `frontend/common/common-button-guide.md`

#### 2. BaseGrid.vue
- **3가지 모드**:
  1. `maxRows` 미설정 → 무한 스크롤 (IntersectionObserver)
  2. `maxRows` + `hasPagination=false` → 고정 높이 + 내부 스크롤
  3. `maxRows` + `hasPagination=true` → 페이지네이션 UI
- **Props**: `rows`, `columns`, `maxRows`, `hasPagination`, `isResizable`
- **참고**: `frontend/common/common-grid-guide.md`

#### 3. BaseModal.vue
- **4가지 모드**: custom / confirm / alert / loading
- **5가지 크기**: sm(360px) / md(560px) / lg(800px) / xl(1100px) / full(100%)
- **alert 타입**: success / error / warning / info
- **참고**: `frontend/common/common-popup-guide.md`

#### 4. 폼 컴포넌트
- **BaseTextBox.vue**: `allowSpecialChars` 옵션으로 특수문자 입력 차단
- **BaseToggleGroup.vue**: `count` 옵션으로 토글 그룹 자동 생성
- **BaseComboBox.vue**: `isMultiple` 옵션으로 다중 선택 지원 (타이핑 검색 + 드롭다운)
- **BaseSelectBox.vue**: `selectFirstByDefault` 옵션으로 첫 번째 값 자동 선택
- **참고**: `frontend/common/common-components-guide.md`

### Dir 구조

```
components/
├── AppLayout.vue           ← 레이아웃 래퍼
├── Sidebar.vue             ← 네비게이션
├── PageHeader.vue          ← 페이지 헤더
├── TablePagination.vue     ← 페이지네이션
└── base/                   ← 공통 컴포넌트
    ├── BaseButton.vue
    ├── BaseGrid.vue
    ├── BaseModal.vue
    ├── BaseTextBox.vue
    ├── BaseToggleGroup.vue
    ├── BaseComboBox.vue
    └── BaseSelectBox.vue

composables/
├── useMenuAPI.ts
├── usePagination.ts
├── useGridSort.ts
├── useResponsivePageSize.ts
├── useSessionTimeout.ts
└── (기타 API 호출 composables)

types/
├── grid.ts                 ← GridColumn, GridSort
└── form.ts                 ← FormOption, FormSize
```

### 공통 모듈 규칙

**필수 패턴**: 필터링 → 정렬 → 페이지네이션 순서

```typescript
// 그리드 페이지 기본 구성
const { items, totalCount } = usePagination(filteredItems);
const { sortedItems } = useGridSort(items);
const { pageSize } = useResponsivePageSize();
```

**테이블 HTML 구조**:
```html
<div class="table-container">
  <table>
    <thead>
      <th class="sortable">이름</th>  <!-- 정렬 가능 -->
    </thead>
  </table>
</div>
```

**액션 컬럼**: 수정/삭제 버튼은 항상 **테이블 마지막 컬럼**에 배치

**검색 디바운스 권장**: 대용량 데이터는 백엔드 페이지네이션 사용