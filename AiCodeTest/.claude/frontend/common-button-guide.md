# 공통 버튼 컴포넌트 가이드

> Vue.js 공식 스타일 가이드 (https://vuejs.org/style-guide/) 기반으로 작성

---

## 1. 버튼 타입 정의

공통 버튼은 업무 목적에 따라 5가지 타입으로 구분합니다.
각 타입은 **고유 색상**과 **SVG 아이콘**이 자동으로 적용되며,
`기타` 타입은 버튼명과 아이콘을 사용자가 직접 지정합니다.

| btnType | 한글명 | 색상 | 아이콘 | 사용 목적 |
|---------|--------|------|--------|-----------|
| `search` | 조회 | 파란색 `#1890ff` | 돋보기 | 데이터 검색·조회 |
| `save` | 저장 | 초록색 `#52c41a` | 플로피 디스크 | 데이터 저장·등록·수정 |
| `delete` | 삭제 | 빨간색 `#ff4d4f` | 휴지통 | 데이터 삭제 |
| `reset` | 초기화 | 주황색 `#fa8c16` | 새로고침 화살표 | 입력값 초기화·검색 조건 리셋 |
| `custom` | 기타 | 보라색 `#667eea` (기본) | 사용자 지정 | 위 4가지에 해당하지 않는 액션 |

---

## 2. 아이콘 미리보기

각 버튼 타입에 사용되는 인라인 SVG 아이콘입니다.

### 조회 — 돋보기

```
   ___
  /   \
 |     |   ← 원형 렌즈
  \___/
      \
       \   ← 손잡이
```

```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none">
  <circle cx="6.5" cy="6.5" r="4" stroke="currentColor" stroke-width="1.5"/>
  <path d="M10 10L13.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
</svg>
```

### 저장 — 플로피 디스크

```
┌──────────┐
│ ┌──────┐ │  ← 상단 레이블 슬롯
│ └──────┘ │
│          │
│ ┌──────┐ │  ← 하단 데이터 슬롯
│ └──────┘ │
└──────────┘
```

```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none">
  <path d="M2 2.5A.5.5 0 012.5 2H11l3 3v8.5a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11z"
        stroke="currentColor" stroke-width="1.5"/>
  <rect x="4.5" y="2" width="5" height="3.5" rx="0.5" stroke="currentColor" stroke-width="1.5"/>
  <rect x="3.5" y="9" width="9" height="4.5" rx="0.5" stroke="currentColor" stroke-width="1.5"/>
</svg>
```

### 삭제 — 휴지통

```
  ─────────   ← 뚜껑
  │       │
  │ | | | │   ← 선들 (삭제 의미)
  │_______│
```

```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none">
  <path d="M2 4h12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
  <path d="M5.5 4V2.5a.5.5 0 01.5-.5h4a.5.5 0 01.5.5V4"
        stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
  <path d="M3.5 4l.9 9a.5.5 0 00.5.5h7.2a.5.5 0 00.5-.5l.9-9"
        stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
  <path d="M6.5 7v4M9.5 7v4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
</svg>
```

### 초기화 — 새로고침 화살표

```
    ╭──────╮
    │      ↓
    ↑      │   ← 순환 화살표
    ╰──────╯
```

```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none">
  <path d="M13.5 8A5.5 5.5 0 112.8 4.8"
        stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
  <path d="M2 2v3.5H5.5"
        stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

---

## 3. 컴포넌트 파일 위치 및 네이밍

### 스타일 가이드 규칙

| 규칙 | 우선순위 | 내용 |
|------|----------|------|
| **싱글 파일 컴포넌트 파일명은 PascalCase** | A (필수) | `BaseButton.vue` ✅ / `basebutton.vue` ❌ |
| **베이스/공통 컴포넌트는 접두사 사용** | B (강력 권장) | `Base`, `App`, `V` 접두사 사용 |
| **단어 두 개 이상으로 구성** | A (필수) | `Button.vue` ❌ → `BaseButton.vue` ✅ |

### 파일 위치

```
components/
└── base/
    └── BaseButton.vue     ← 공통 버튼 컴포넌트
```

> Nuxt 3에서는 `components/base/BaseButton.vue` 파일이 자동으로 `<BaseButton>` 으로 등록됩니다.

---

## 4. Props 설계

### 스타일 가이드 규칙

| 규칙 | 우선순위 | 내용 |
|------|----------|------|
| **Props는 항상 상세하게 정의** | A (필수) | 타입, 기본값, validator 명시 |
| **Props 이름은 camelCase** | B (강력 권장) | `btnType` ✅ / `btn-type` ❌ (정의 시) |
| **Boolean props는 접두사 규칙** | B (강력 권장) | `is`, `has`, `can` 접두사 권장 |

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `btnType` | `String` | `'search'` | 버튼 종류 (search/save/delete/reset/custom) |
| `size` | `String` | `'md'` | 버튼 크기 (sm/md/lg) |
| `isDisabled` | `Boolean` | `false` | 비활성화 여부 |
| `isLoading` | `Boolean` | `false` | 로딩 상태 여부 |
| `type` | `String` | `'button'` | HTML button type (button/submit/reset) |
| `block` | `Boolean` | `false` | 전체 너비 여부 |
| `customLabel` | `String` | `undefined` | **기타 타입 전용** — 버튼 텍스트 |
| `customIconSrc` | `String` | `undefined` | **기타 타입 전용** — 이미지 URL (png/svg 파일 경로) |
| `customIconSvg` | `String` | `undefined` | **기타 타입 전용** — 인라인 SVG 문자열 |

> `customIconSrc`와 `customIconSvg`가 동시에 전달되면 `customIconSrc`가 우선 적용됩니다.

---

## 5. 컴포넌트 코드

### `components/base/BaseButton.vue`

```vue
<template>
  <!-- 스타일 가이드: 최상위 요소는 단순하게, 조건부 렌더링은 내부에서 처리 -->
  <button
    :type="type"
    :disabled="isDisabled || isLoading"
    :aria-disabled="isDisabled || isLoading"
    :aria-busy="isLoading"
    :class="buttonClasses"
    v-bind="$attrs"
    @click="handleClick"
  >
    <!-- 로딩 스피너 (로딩 중일 때만 표시) -->
    <span v-if="isLoading" class="btn__spinner" aria-hidden="true" />

    <!-- 아이콘 영역 (로딩 중에는 숨김) -->
    <span v-if="!isLoading" class="btn__icon" aria-hidden="true">

      <!-- 기타 타입: 이미지 URL 사용 -->
      <img
        v-if="btnType === 'custom' && customIconSrc"
        :src="customIconSrc"
        class="btn__icon-img"
        alt=""
      />

      <!-- 기타 타입: 인라인 SVG 사용 -->
      <!-- eslint-disable-next-line vue/no-v-html -->
      <span
        v-else-if="btnType === 'custom' && customIconSvg"
        class="btn__icon-svg"
        v-html="customIconSvg"
      />

      <!-- 조회/저장/삭제/초기화 타입: 내장 SVG 아이콘 -->
      <!-- eslint-disable-next-line vue/no-v-html -->
      <span
        v-else-if="btnType !== 'custom'"
        class="btn__icon-svg"
        v-html="resolvedIcon"
      />
    </span>

    <!-- 버튼 텍스트 -->
    <!-- 스타일 가이드: 슬롯 기본값을 활용해 유연성 확보 -->
    <span class="btn__label">
      <!-- 기타 타입: customLabel prop 또는 슬롯 -->
      <template v-if="btnType === 'custom'">
        <slot>{{ customLabel ?? '버튼' }}</slot>
      </template>
      <!-- 고정 타입: 타입별 고정 텍스트 -->
      <template v-else>{{ resolvedLabel }}</template>
    </span>
  </button>
</template>

<script setup lang="ts">
// 스타일 가이드: <script setup> 사용 (Vue 3 Composition API 권장 방식)
import { computed } from 'vue'

// ─── 아이콘 상수 (인라인 SVG) ─────────────────────────────────────────────
const ICONS = {
  // 돋보기
  search: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="6.5" cy="6.5" r="4" stroke="currentColor" stroke-width="1.5"/>
    <path d="M10 10L13.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
  </svg>`,

  // 플로피 디스크
  save: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M2 2.5A.5.5 0 012.5 2H11l3 3v8.5a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11z" stroke="currentColor" stroke-width="1.5"/>
    <rect x="4.5" y="2" width="5" height="3.5" rx="0.5" stroke="currentColor" stroke-width="1.5"/>
    <rect x="3.5" y="9" width="9" height="4.5" rx="0.5" stroke="currentColor" stroke-width="1.5"/>
  </svg>`,

  // 휴지통
  delete: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M2 4h12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
    <path d="M5.5 4V2.5a.5.5 0 01.5-.5h4a.5.5 0 01.5.5V4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
    <path d="M3.5 4l.9 9a.5.5 0 00.5.5h7.2a.5.5 0 00.5-.5l.9-9" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
    <path d="M6.5 7v4M9.5 7v4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
  </svg>`,

  // 새로고침 화살표
  reset: `<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M13.5 8A5.5 5.5 0 112.8 4.8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
    <path d="M2 2v3.5H5.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
  </svg>`
} as const

// ─── 버튼 타입별 기본 텍스트 ─────────────────────────────────────────────
const LABELS: Record<string, string> = {
  search: '조회',
  save: '저장',
  delete: '삭제',
  reset: '초기화'
}

// ─── Props ───────────────────────────────────────────────────────────────
// 스타일 가이드: Props는 타입과 validator를 상세하게 정의
const props = defineProps({
  btnType: {
    type: String,
    default: 'search',
    validator: (value: string) =>
      ['search', 'save', 'delete', 'reset', 'custom'].includes(value)
  },
  size: {
    type: String,
    default: 'md',
    validator: (value: string) => ['sm', 'md', 'lg'].includes(value)
  },
  type: {
    type: String,
    default: 'button',
    validator: (value: string) => ['button', 'submit', 'reset'].includes(value)
  },
  isDisabled: {
    type: Boolean,
    default: false
  },
  isLoading: {
    type: Boolean,
    default: false
  },
  block: {
    type: Boolean,
    default: false
  },
  // 기타 타입 전용
  customLabel: {
    type: String,
    default: undefined
  },
  customIconSrc: {
    type: String,
    default: undefined
  },
  customIconSvg: {
    type: String,
    default: undefined
  }
})

// ─── Emits ───────────────────────────────────────────────────────────────
// 스타일 가이드: Emit은 명시적으로 선언
const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

// ─── Computed ─────────────────────────────────────────────────────────────
// 스타일 가이드: 복잡한 표현식은 computed로 분리

/** 버튼 타입에 따른 CSS 클래스 목록 */
const buttonClasses = computed(() => [
  'btn',
  `btn--${props.btnType}`,
  `btn--${props.size}`,
  {
    'btn--disabled': props.isDisabled,
    'btn--loading': props.isLoading,
    'btn--block': props.block
  }
])

/** 버튼 타입에 따른 내장 SVG 아이콘 문자열 */
const resolvedIcon = computed(() =>
  ICONS[props.btnType as keyof typeof ICONS] ?? ''
)

/** 버튼 타입에 따른 기본 라벨 텍스트 */
const resolvedLabel = computed(() => LABELS[props.btnType] ?? '')

// ─── Methods ──────────────────────────────────────────────────────────────
// 스타일 가이드: 메서드명은 동사로 시작
const handleClick = (event: MouseEvent) => {
  if (props.isDisabled || props.isLoading) return
  emit('click', event)
}
</script>

<style scoped>
/* 스타일 가이드: scoped 스타일로 컴포넌트 격리 */

/* ── 기본 레이아웃 ── */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  border: none;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: filter 0.2s, opacity 0.2s;
  white-space: nowrap;
  user-select: none;
  outline: none;
  color: #fff;
}

.btn:focus-visible {
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.45);
}

/* ── 크기 ── */
.btn--sm { height: 32px; padding: 0 12px; font-size: 12px; }
.btn--md { height: 40px; padding: 0 16px; font-size: 14px; }
.btn--lg { height: 48px; padding: 0 24px; font-size: 16px; }

/* ── 타입별 색상 ── */
.btn--search { background-color: #1890ff; }
.btn--save   { background-color: #52c41a; }
.btn--delete { background-color: #ff4d4f; }
.btn--reset  { background-color: #fa8c16; }
.btn--custom { background-color: #667eea; } /* 기타: 기본값 (v-bind로 override 가능) */

/* ── hover ── */
.btn--search:hover:not(.btn--disabled):not(.btn--loading) { filter: brightness(1.12); }
.btn--save:hover:not(.btn--disabled):not(.btn--loading)   { filter: brightness(1.1);  }
.btn--delete:hover:not(.btn--disabled):not(.btn--loading) { filter: brightness(1.1);  }
.btn--reset:hover:not(.btn--disabled):not(.btn--loading)  { filter: brightness(1.1);  }
.btn--custom:hover:not(.btn--disabled):not(.btn--loading) { filter: brightness(1.1);  }

/* ── 비활성화 ── */
.btn--disabled,
.btn:disabled {
  opacity: 0.45;
  cursor: not-allowed;
  pointer-events: none;
}

/* ── 로딩 ── */
.btn--loading {
  cursor: wait;
  pointer-events: none;
}

/* ── 전체 너비 ── */
.btn--block { display: flex; width: 100%; }

/* ── 로딩 스피너 ── */
.btn__spinner {
  display: inline-block;
  width: 14px;
  height: 14px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  border-top-color: #fff;
  border-radius: 50%;
  animation: btn-spin 0.6s linear infinite;
  flex-shrink: 0;
}

@keyframes btn-spin { to { transform: rotate(360deg); } }

/* ── 아이콘 ── */
.btn__icon {
  display: inline-flex;
  align-items: center;
  flex-shrink: 0;
  line-height: 1;
}

.btn__icon-svg {
  display: inline-flex;
  align-items: center;
}

.btn__icon-img {
  width: 16px;
  height: 16px;
  object-fit: contain;
  display: block;
}

/* sm 크기에서 아이콘 축소 */
.btn--sm .btn__icon-img,
.btn--sm .btn__icon-svg svg {
  width: 13px;
  height: 13px;
}

/* lg 크기에서 아이콘 확대 */
.btn--lg .btn__icon-img,
.btn--lg .btn__icon-svg svg {
  width: 18px;
  height: 18px;
}
</style>
```

---

## 6. 사용 예시

### 고정 타입 버튼 (조회 / 저장 / 삭제 / 초기화)

```vue
<!-- 조회: 파란색 + 돋보기 아이콘 자동 적용 -->
<BaseButton btn-type="search" @click="handleSearch" />

<!-- 저장: 초록색 + 플로피 디스크 아이콘 자동 적용 -->
<BaseButton btn-type="save" @click="handleSave" />

<!-- 삭제: 빨간색 + 휴지통 아이콘 자동 적용 -->
<BaseButton btn-type="delete" @click="handleDelete" />

<!-- 초기화: 주황색 + 새로고침 아이콘 자동 적용 -->
<BaseButton btn-type="reset" @click="handleReset" />
```

### 기타 타입 — 텍스트만 변경

```vue
<!-- customLabel prop으로 버튼명 지정 -->
<BaseButton btn-type="custom" custom-label="엑셀 다운로드" @click="exportExcel" />

<!-- 슬롯으로도 동일하게 지정 가능 -->
<BaseButton btn-type="custom" @click="exportExcel">엑셀 다운로드</BaseButton>
```

### 기타 타입 — 이미지 파일(URL) 아이콘 변경

```vue
<!-- public 폴더의 이미지 사용 -->
<BaseButton
  btn-type="custom"
  custom-label="엑셀 다운로드"
  custom-icon-src="/icons/excel.png"
  @click="exportExcel"
/>

<!-- assets 폴더의 SVG 이미지 사용 -->
<BaseButton
  btn-type="custom"
  custom-label="인쇄"
  custom-icon-src="/icons/print.svg"
  @click="handlePrint"
/>
```

### 기타 타입 — 인라인 SVG 아이콘 변경

```vue
<script setup lang="ts">
// 사용자 정의 SVG 문자열
const downloadIcon = `
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M8 2v8M5 7l3 3 3-3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    <path d="M2 12h12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
  </svg>
`
</script>

<template>
  <BaseButton
    btn-type="custom"
    custom-label="다운로드"
    :custom-icon-svg="downloadIcon"
    @click="handleDownload"
  />
</template>
```

### 크기 조절

```vue
<BaseButton btn-type="search" size="sm" @click="handleSearch" />
<BaseButton btn-type="save"   size="md" @click="handleSave" />
<BaseButton btn-type="delete" size="lg" @click="handleDelete" />
```

### 상태

```vue
<!-- 비활성화 -->
<BaseButton btn-type="save" :is-disabled="!hasChanges" @click="handleSave" />

<!-- 로딩 (API 호출 중) -->
<BaseButton btn-type="save" :is-loading="isSaving" @click="handleSave" />

<!-- 전체 너비 -->
<BaseButton btn-type="search" :block="true" @click="handleSearch" />
```

### 실제 MES 페이지 적용 예시 (메뉴 관리)

```vue
<template>
  <div class="page">
    <!-- 검색 조건 영역 -->
    <div class="search-bar">
      <input v-model="searchKeyword" placeholder="메뉴명 검색" />
      <BaseButton btn-type="search" @click="fetchMenuList" />
      <BaseButton btn-type="reset"  @click="resetSearchCondition" />
    </div>

    <!-- 그리드 툴바 -->
    <div class="toolbar">
      <BaseButton
        btn-type="save"
        :is-loading="isSaving"
        :is-disabled="!hasChanges"
        @click="saveMenuList"
      />
      <BaseButton
        btn-type="delete"
        :is-disabled="selectedRows.length === 0"
        @click="deleteSelectedRows"
      />
      <!-- 기타: 엑셀 다운로드 버튼 -->
      <BaseButton
        btn-type="custom"
        custom-label="엑셀 다운로드"
        custom-icon-src="/icons/excel.svg"
        @click="exportToExcel"
      />
    </div>
  </div>
</template>
```

---

## 7. 아이콘 우선순위 (기타 타입)

`btnType="custom"` 일 때 아이콘 렌더링 우선순위:

```
1순위: customIconSrc  → <img src="..."> 로 렌더링 (PNG, SVG 파일 경로)
2순위: customIconSvg  → v-html로 인라인 SVG 렌더링
3순위: (없음)         → 아이콘 없이 텍스트만 표시
```

> `customIconSrc`와 `customIconSvg`를 동시에 전달하면 `customIconSrc`가 우선입니다.

---

## 8. 스타일 가이드 적용 체크리스트

### A 우선순위 (필수)

- [x] 컴포넌트 이름이 PascalCase 사용 (`BaseButton`)
- [x] 컴포넌트 이름이 두 단어 이상 (`Base` + `Button`)
- [x] Props 타입과 기본값 상세 정의
- [x] `v-if`와 `v-for` 동시 사용 금지
- [x] Scoped 스타일 사용

### B 우선순위 (강력 권장)

- [x] 공통 컴포넌트에 `Base` 접두사 사용
- [x] Props를 camelCase로 정의 (`btnType`, `customLabel`, `customIconSrc`)
- [x] Boolean props에 `is` 접두사 사용 (`isDisabled`, `isLoading`)
- [x] `<script setup>` Composition API 사용
- [x] Emit 이벤트 명시적 선언 (`defineEmits`)
- [x] 복잡한 표현식을 computed로 분리 (`buttonClasses`, `resolvedIcon`, `resolvedLabel`)
- [x] 메서드명 동사로 시작 (`handleClick`)

### C 우선순위 (권장)

- [x] Props validator로 유효한 값 제한 (`btnType`, `size`, `type`)
- [x] 접근성 속성 추가 (`aria-disabled`, `aria-busy`, `alt=""` on icon img)
- [x] `focus-visible`로 키보드 포커스 스타일 처리

---

## 9. 파일 구조

```
components/
└── base/
    └── BaseButton.vue          ← 공통 버튼 컴포넌트

public/
└── icons/                      ← 기타 타입 커스텀 아이콘 이미지 보관
    ├── excel.svg
    ├── print.svg
    └── ...

docs/
└── common-button-guide.md      ← 이 문서
```

---

## 참고 자료

- [Vue.js 공식 스타일 가이드](https://vuejs.org/style-guide/)
- [Vue 3 Composition API](https://vuejs.org/guide/extras/composition-api-faq)
- [Nuxt 3 컴포넌트 자동 임포트](https://nuxt.com/docs/guide/directory-structure/components)
- [MDN — SVG 인라인 사용법](https://developer.mozilla.org/ko/docs/Web/SVG/Tutorial/SVG_In_HTML_Introduction)
