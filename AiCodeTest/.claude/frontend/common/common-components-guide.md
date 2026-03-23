# 공통 폼 컴포넌트 가이드

> Vue.js 공식 스타일 가이드 (https://vuejs.org/style-guide/) 기반으로 작성
> Vue 3.4+ / Vue 3.5+ / Nuxt 3 auto-imports 트렌드 적용
> 버튼 컴포넌트는 `common-button-guide.md` 참조

---

## 1. 컴포넌트 목록

| 컴포넌트 | 파일명 | 핵심 옵션 |
|----------|--------|-----------|
| 텍스트박스 | `BaseTextBox.vue` | `allowSpecialChars` — 특수문자 입력 차단 |
| 토글 그룹 | `BaseToggleGroup.vue` | `count` — 토글 개수로 그룹 자동 생성 |
| 콤보박스 | `BaseComboBox.vue` | `isMultiple` — 다중 선택 |
| 셀렉트박스 | `BaseSelectBox.vue` | `isMultiple` + `selectFirstByDefault` — 다중 선택, 첫 번째 값 자동 선택 |
| 체크박스 | `BaseCheckBox.vue` | `isIndeterminate` — 부분 선택 상태, 그룹(배열) v-model 지원 |
| 라디오 그룹 | `BaseRadioGroup.vue` | `options` — 라디오 버튼 목록, 단일 값 선택 |
| 텍스트에어리어 | `BaseTextArea.vue` | `rows` + `maxLength` — 줄 수, 글자 수 제한 |
| 날짜 선택기 | `BaseDatePicker.vue` | `min` + `max` — 선택 가능 날짜 범위 제한 |
| 숫자 입력 | `BaseNumberInput.vue` | `useComma` + `suffix` — 천 단위 콤마, 단위 표시 |

---

## 2. 파일 위치

```
components/
└── base/
    ├── BaseTextBox.vue         ← 텍스트박스
    ├── BaseToggleGroup.vue     ← 토글 그룹
    ├── BaseComboBox.vue        ← 콤보박스 (타이핑 검색 + 드롭다운)
    ├── BaseSelectBox.vue       ← 셀렉트박스
    ├── BaseCheckBox.vue        ← 체크박스 (단일/그룹)
    ├── BaseRadioGroup.vue      ← 라디오 그룹
    ├── BaseTextArea.vue        ← 텍스트에어리어
    ├── BaseDatePicker.vue      ← 날짜 선택기
    └── BaseNumberInput.vue     ← 숫자 입력 (콤마 포맷, 단위)

types/
└── form.ts                     ← 공통 타입 정의
```

---

## 3. 공통 타입 정의

```typescript
// types/form.ts

/** 드롭다운/토글 옵션 항목 */
export interface FormOption {
  value: string | number
  label: string
  isDisabled?: boolean
}

/** 폼 컴포넌트 공통 크기 */
export type FormSize = 'sm' | 'md' | 'lg'
```

---

## 4. BaseTextBox — 텍스트박스

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `allowSpecialChars=false` | 특수문자 입력 차단 (영문·숫자·한글·공백만 허용) |

### 특수문자 차단 동작

```
allowSpecialChars=false 적용 시:
허용: a-z, A-Z, 0-9, 가-힣, ㄱ-ㅎ, ㅏ-ㅣ, 공백
차단: ! @ # $ % ^ & * ( ) _ + - = [ ] { } ; ' " , . / < > ? \

입력: "Hello! World@2025#"
출력: "Hello World2025"
```

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `String` | `''` | `v-model` 바인딩 값 |
| `type` | `String` | `'text'` | input type (text/password/email/number) |
| `placeholder` | `String` | `''` | placeholder 텍스트 |
| `label` | `String` | `undefined` | 레이블 텍스트 |
| `size` | `String` | `'md'` | sm / md / lg |
| `isDisabled` | `Boolean` | `false` | 비활성화 |
| `isReadonly` | `Boolean` | `false` | 읽기 전용 |
| `allowSpecialChars` | `Boolean` | `true` | **false 시 특수문자 입력 차단** |
| `maxLength` | `Number` | `undefined` | 최대 입력 글자 수 |
| `block` | `Boolean` | `false` | 전체 너비 |

### 컴포넌트 코드

```vue
<!-- components/base/BaseTextBox.vue -->
<template>
  <div :class="wrapperClasses">

    <!-- 레이블 -->
    <label v-if="label" :for="inputId" class="textbox__label">
      {{ label }}
    </label>

    <!-- 인풋 래퍼 (아이콘 배치용) -->
    <div class="textbox__input-wrap">
      <input
        :id="inputId"
        :type="type"
        :value="model"
        :placeholder="placeholder"
        :disabled="isDisabled"
        :readonly="isReadonly"
        :maxlength="maxLength"
        :aria-label="label ?? placeholder"
        :aria-disabled="isDisabled"
        :aria-describedby="!allowSpecialChars && hasSpecialCharWarning ? `${inputId}-warn` : undefined"
        :class="inputClasses"
        v-bind="$attrs"
        @input="handleInput"
        @blur="emit('blur', $event)"
        @focus="emit('focus', $event)"
      />

      <!-- 글자 수 카운터 (maxLength 설정 시) -->
      <span v-if="maxLength" class="textbox__counter" aria-live="polite">
        {{ model.length }} / {{ maxLength }}
      </span>
    </div>

    <!-- 특수문자 차단 안내 메시지 -->
    <p
      v-if="!allowSpecialChars && hasSpecialCharWarning"
      :id="`${inputId}-warn`"
      class="textbox__warning"
      role="alert"
    >
      특수문자는 입력할 수 없습니다
    </p>

  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseTextBox' })

import type { FormSize } from '~/types/form'

// 특수문자 판별 정규식 — 영문·숫자·한글·공백 이외 모두 차단
const SPECIAL_CHAR_REGEX = /[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\s]/g

// ─── Props (TypeScript 유니온 타입 방식 — Vue 3.4+) ───────────────────────
// ❌ 구식: modelValue prop + emit('update:modelValue') 분리 방식
// ✅ 현대: defineModel() 로 양방향 바인딩 단순화 (Vue 3.4+)
interface Props {
  type?:              'text' | 'password' | 'email' | 'number'
  placeholder?:       string
  label?:             string
  size?:              FormSize
  isDisabled?:        boolean
  isReadonly?:        boolean
  allowSpecialChars?: boolean
  maxLength?:         number
  block?:             boolean
}

const props = withDefaults(defineProps<Props>(), {
  type:              'text',
  placeholder:       '',
  size:              'md',
  isDisabled:        false,
  isReadonly:        false,
  allowSpecialChars: true,
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
// ❌ 구식: const props = defineProps({ modelValue: ... }) + emit('update:modelValue', val)
// ✅ 현대: defineModel()이 modelValue prop + update:modelValue emit을 자동 처리
const model = defineModel<string>({ default: '' })

// ─── Emits ────────────────────────────────────────────────────────────────
const emit = defineEmits<{
  'blur':  [event: FocusEvent]
  'focus': [event: FocusEvent]
}>()

// ─── useId (Vue 3.5+) — SSR 안전한 고유 ID ───────────────────────────────
// ❌ 구식: let idCounter = 0; const inputId = `textbox-${++idCounter}`
// ✅ 현대: useId()는 SSR 하이드레이션 충돌 없이 안전하게 고유 ID 생성
const inputId = useId()

// ─── State ────────────────────────────────────────────────────────────────
// Nuxt auto-imports: ref는 import 없이 사용 가능
// 특수문자 차단 경고 표시 여부
const hasSpecialCharWarning = ref(false)

// ─── Computed ─────────────────────────────────────────────────────────────
// Nuxt auto-imports: computed는 import 없이 사용 가능
// 스타일 가이드: 복잡한 표현식은 computed로 분리

const wrapperClasses = computed(() => [
  'textbox',
  `textbox--${props.size}`,
  { 'textbox--block': props.block }
])

const inputClasses = computed(() => [
  'textbox__input',
  {
    'textbox__input--disabled':    props.isDisabled,
    'textbox__input--readonly':    props.isReadonly,
    'textbox__input--has-counter': !!props.maxLength
  }
])

// ─── Methods ──────────────────────────────────────────────────────────────
// 스타일 가이드: 메서드명은 동사로 시작

const handleInput = (event: Event) => {
  const input = event.target as HTMLInputElement
  let value = input.value

  if (!props.allowSpecialChars) {
    const filtered = value.replace(SPECIAL_CHAR_REGEX, '')
    hasSpecialCharWarning.value = filtered !== value
    value = filtered
    // DOM 값도 직접 수정 (필터 결과 반영)
    input.value = value
  }

  // defineModel 사용: model.value에 직접 할당
  model.value = value
}
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
  --color-error:         #ff4d4f;
}

/* ── Wrapper ── */
.textbox { display: inline-flex; flex-direction: column; gap: 4px; }
.textbox--block { display: flex; width: 100%; }

/* ── 레이블 ── */
.textbox__label {
  font-size: 13px;
  font-weight: 500;
  color: #444;
}

/* ── 인풋 래퍼 ── */
.textbox__input-wrap {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

/* ── 인풋 ── */
.textbox__input {
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  color: #333;
  background: #fff;
  transition: border-color 0.2s, box-shadow 0.2s;
  outline: none;
  font-family: inherit;
}

.textbox__input:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* 크기 */
.textbox--sm .textbox__input { height: 32px; padding: 0 10px; font-size: 12px; }
.textbox--md .textbox__input { height: 40px; padding: 0 12px; font-size: 14px; }
.textbox--lg .textbox__input { height: 48px; padding: 0 16px; font-size: 16px; }

/* 카운터가 있을 때 우측 패딩 확보 */
.textbox__input--has-counter { padding-right: 70px; }

/* 상태 */
.textbox__input--disabled { background: #f5f5f5; color: #aaa; cursor: not-allowed; }
.textbox__input--readonly  { background: #fafafa; cursor: default; }

/* ── 글자 수 카운터 ── */
.textbox__counter {
  position: absolute;
  right: 10px;
  font-size: 11px;
  color: #aaa;
  white-space: nowrap;
  pointer-events: none;
}

/* ── 경고 메시지 ── */
.textbox__warning {
  font-size: 12px;
  color: var(--color-error);
  margin: 0;
}
</style>
```

### 사용 예시

```vue
<!-- 기본 사용 -->
<BaseTextBox v-model="name" label="이름" placeholder="이름을 입력하세요" />

<!-- 특수문자 차단 -->
<BaseTextBox
  v-model="menuId"
  label="메뉴 ID"
  :allow-special-chars="false"
  placeholder="영문·숫자·한글만 입력 가능"
/>

<!-- 글자 수 제한 + 카운터 표시 -->
<BaseTextBox v-model="memo" label="메모" :max-length="100" :block="true" />

<!-- 비밀번호 입력 -->
<BaseTextBox v-model="password" label="비밀번호" type="password" :allow-special-chars="false" />

<!-- 읽기 전용 -->
<BaseTextBox v-model="registeredAt" label="등록일" :is-readonly="true" />
```

---

## 5. BaseToggleGroup — 토글 그룹

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `count` | 토글 개수로 그룹 자동 생성 (options 미제공 시) |
| `options` | 직접 토글 항목 배열 제공 (label/value 지정) |
| `isMultiple` | 다중 선택 허용 여부 |

### count vs options 동작

```
count=3 사용 시 (options 미제공):
→ 자동 생성: [{ value: '1', label: '1' }, { value: '2', label: '2' }, { value: '3', label: '3' }]
→ [1] [2] [3]

options 직접 제공 시:
→ [{ value: 'on', label: '가동' }, { value: 'off', label: '중지' }]
→ [가동] [중지]
```

### 단일/다중 선택 동작

```
isMultiple=false (기본):
  modelValue = string   → 클릭 시 해당 value로 교체
  [가동] [중지*] [점검]  → '중지' 선택 중

isMultiple=true:
  modelValue = string[] → 클릭 시 추가/제거 토글
  [가동*] [중지] [점검*] → '가동', '점검' 동시 선택 중
```

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `String\|String[]` | `''` | 선택된 값 (`isMultiple=true`면 배열) |
| `options` | `FormOption[]` | `undefined` | 토글 항목 배열 — **미설정 시 count로 자동 생성** |
| `count` | `Number` | `undefined` | **options 미설정 시** 자동 생성할 토글 개수 |
| `isMultiple` | `Boolean` | `false` | 다중 선택 허용 |
| `size` | `String` | `'md'` | sm / md / lg |
| `isDisabled` | `Boolean` | `false` | 전체 그룹 비활성화 |

### 컴포넌트 코드

```vue
<!-- components/base/BaseToggleGroup.vue -->
<template>
  <div
    :class="groupClasses"
    role="group"
    :aria-label="ariaLabel"
  >
    <button
      v-for="item in resolvedOptions"
      :key="item.value"
      type="button"
      :class="getToggleClasses(item)"
      :disabled="isDisabled || item.isDisabled"
      :aria-pressed="isSelected(item.value)"
      @click="handleToggle(item.value)"
    >
      {{ item.label }}
    </button>
  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseToggleGroup' })

import type { FormOption, FormSize } from '~/types/form'

// ─── Props (TypeScript 유니온 타입 방식 — Vue 3.4+) ───────────────────────
interface Props {
  options?:   FormOption[]
  count?:     number
  isMultiple?: boolean
  size?:      FormSize
  isDisabled?: boolean
  ariaLabel?: string
}

const props = withDefaults(defineProps<Props>(), {
  isMultiple: false,
  size:       'md',
  isDisabled: false,
  ariaLabel:  '토글 그룹',
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
// ❌ 구식: modelValue prop + emit('update:modelValue')
// ✅ 현대: defineModel로 단순화
const model = defineModel<string | string[]>({ default: '' })

// ─── Computed ─────────────────────────────────────────────────────────────
// Nuxt auto-imports: computed는 import 없이 사용 가능
// 스타일 가이드: 복잡한 표현식은 computed로 분리

/**
 * 실제 렌더링할 옵션 목록 결정
 * 1순위: options prop (직접 제공)
 * 2순위: count prop → 1 ~ N 번호로 자동 생성
 * 3순위: 빈 배열
 */
const resolvedOptions = computed<FormOption[]>(() => {
  if (props.options) return props.options
  if (props.count && props.count > 0) {
    return Array.from({ length: props.count }, (_, i) => ({
      value: String(i + 1),
      label: String(i + 1)
    }))
  }
  return []
})

const groupClasses = computed(() => [
  'toggle-group',
  `toggle-group--${props.size}`,
  { 'toggle-group--disabled': props.isDisabled }
])

// ─── Methods ──────────────────────────────────────────────────────────────

/** 해당 value가 현재 선택 상태인지 확인 */
const isSelected = (value: string | number): boolean => {
  if (props.isMultiple) {
    return Array.isArray(model.value) && model.value.includes(String(value))
  }
  return model.value === String(value)
}

const getToggleClasses = (item: FormOption) => [
  'toggle-group__btn',
  { 'toggle-group__btn--active': isSelected(item.value) }
]

/**
 * 토글 클릭 처리
 * - isMultiple=false: 클릭한 값을 단일 선택 (같은 값 재클릭 시 해제 없음)
 * - isMultiple=true:  클릭한 값이 배열에 있으면 제거, 없으면 추가
 */
const handleToggle = (value: string | number) => {
  const strValue = String(value)

  if (props.isMultiple) {
    const current = Array.isArray(model.value) ? [...model.value] : []
    const idx = current.indexOf(strValue)
    if (idx >= 0) {
      current.splice(idx, 1)  // 이미 선택됨 → 제거
    } else {
      current.push(strValue)  // 미선택 → 추가
    }
    // defineModel: model.value에 직접 할당
    model.value = current
  } else {
    model.value = strValue
  }
}
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-dark:  #5568d3;
  --color-primary-hover: #f0f3ff;
  --color-border:        #d9d9d9;
}

/* ── 그룹 컨테이너 ── */
.toggle-group {
  display: inline-flex;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid var(--color-border);
}

/* ── 토글 버튼 공통 ── */
.toggle-group__btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-right: 1px solid var(--color-border);
  background: #fff;
  color: #555;
  cursor: pointer;
  font-family: inherit;
  transition: background 0.15s, color 0.15s;
  user-select: none;
  outline: none;
}

.toggle-group__btn:last-child {
  border-right: none;
}

.toggle-group__btn:hover:not(:disabled) {
  background: var(--color-primary-hover);
  color: var(--color-primary);
}

.toggle-group__btn:focus-visible {
  box-shadow: inset 0 0 0 2px var(--color-primary);
}

/* 선택된 상태 */
.toggle-group__btn--active {
  background: var(--color-primary);
  color: #fff;
  font-weight: 600;
}

.toggle-group__btn--active:hover:not(:disabled) {
  background: var(--color-primary-dark);
  color: #fff;
}

/* 비활성화 */
.toggle-group--disabled .toggle-group__btn,
.toggle-group__btn:disabled {
  opacity: 0.45;
  cursor: not-allowed;
  pointer-events: none;
}

/* 크기 */
.toggle-group--sm .toggle-group__btn { height: 32px; padding: 0 12px; font-size: 12px; }
.toggle-group--md .toggle-group__btn { height: 40px; padding: 0 16px; font-size: 14px; }
.toggle-group--lg .toggle-group__btn { height: 48px; padding: 0 20px; font-size: 16px; }
</style>
```

### 사용 예시

```vue
<!-- count로 개수만 지정 (자동 생성) -->
<BaseToggleGroup v-model="selected" :count="3" />
<!-- 렌더링: [1] [2] [3] -->

<!-- count=5 + 다중 선택 -->
<BaseToggleGroup v-model="selectedLines" :count="5" :is-multiple="true" />
<!-- 렌더링: [1] [2] [3] [4] [5], 여러 개 동시 선택 가능 -->

<!-- options 직접 지정 (단일 선택) -->
<BaseToggleGroup
  v-model="machineStatus"
  :options="[
    { value: 'run',   label: '가동' },
    { value: 'stop',  label: '중지' },
    { value: 'check', label: '점검' }
  ]"
/>

<!-- options 직접 지정 + 다중 선택 -->
<BaseToggleGroup
  v-model="selectedShifts"
  :options="shiftOptions"
  :is-multiple="true"
  aria-label="근무 시프트 선택"
/>
```

---

## 6. BaseComboBox — 콤보박스

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `isMultiple=true` | 여러 항목 선택 → 선택된 항목 태그(chip) 형태로 표시 |

### 동작 방식

```
기본 (isMultiple=false):
┌──────────────────────────┐
│ 타이핑으로 필터 → 목록 선택│  → modelValue = '가나다'
└──────────────────────────┘

isMultiple=true:
┌──────────────────────────────────────────┐
│ [가나다 ×] [마바사 ×]  입력...            │  → modelValue = ['가나다', '마바사']
└──────────────────────────────────────────┘
     ↑ 선택된 항목 chip (× 클릭 시 제거)
```

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `String\|String[]` | `''` | 선택된 값 |
| `options` | `FormOption[]` | `[]` | 드롭다운 항목 배열 |
| `isMultiple` | `Boolean` | `false` | **다중 선택 허용** |
| `placeholder` | `String` | `'검색...'` | 입력창 placeholder |
| `isDisabled` | `Boolean` | `false` | 비활성화 |
| `isLoading` | `Boolean` | `false` | 옵션 로딩 중 |
| `noResultMessage` | `String` | `'검색 결과 없음'` | 검색 결과 없을 때 메시지 |
| `size` | `String` | `'md'` | sm / md / lg |

### 컴포넌트 코드

```vue
<!-- components/base/BaseComboBox.vue -->
<template>
  <div :class="wrapperClasses" ref="comboRef">

    <!-- 선택 영역 (chip + 입력창) -->
    <div
      class="combo__control"
      :class="{ 'combo__control--focused': isOpen, 'combo__control--disabled': isDisabled }"
      @click="openDropdown"
    >
      <!-- 다중 선택 chip 목록 -->
      <span
        v-for="val in selectedChips"
        :key="val"
        class="combo__chip"
      >
        {{ getLabelByValue(val) }}
        <button
          type="button"
          class="combo__chip-remove"
          :aria-label="`${getLabelByValue(val)} 제거`"
          @click.stop="removeChip(val)"
        >×</button>
      </span>

      <!-- 검색 입력창 -->
      <input
        ref="inputRef"
        v-model="searchQuery"
        type="text"
        class="combo__input"
        :placeholder="inputPlaceholder"
        :disabled="isDisabled"
        :aria-expanded="isOpen"
        :aria-haspopup="'listbox'"
        autocomplete="off"
        @focus="openDropdown"
        @keydown.escape="closeDropdown"
        @keydown.enter.prevent="selectHighlighted"
        @keydown.arrow-down.prevent="moveHighlight(1)"
        @keydown.arrow-up.prevent="moveHighlight(-1)"
      />

      <!-- 우측 아이콘 -->
      <span class="combo__arrow" aria-hidden="true">
        <svg width="12" height="12" viewBox="0 0 12 12">
          <path
            :d="isOpen ? 'M1 8L6 3L11 8' : 'M1 4L6 9L11 4'"
            stroke="currentColor" stroke-width="1.5"
            stroke-linecap="round" stroke-linejoin="round"
            fill="none"
          />
        </svg>
      </span>
    </div>

    <!-- 드롭다운 목록 -->
    <ul
      v-if="isOpen"
      class="combo__dropdown"
      role="listbox"
      :aria-multiselectable="isMultiple"
    >
      <!-- 로딩 -->
      <li v-if="isLoading" class="combo__option combo__option--loading" role="status">
        <span class="combo__spinner" />불러오는 중...
      </li>

      <!-- 검색 결과 없음 -->
      <li v-else-if="filteredOptions.length === 0" class="combo__option combo__option--empty" aria-live="polite">
        {{ noResultMessage }}
      </li>

      <!-- 옵션 목록 -->
      <!-- 스타일 가이드: v-for에 :key 필수 -->
      <li
        v-else
        v-for="(option, index) in filteredOptions"
        :key="option.value"
        class="combo__option"
        :class="{
          'combo__option--selected':     isOptionSelected(option.value),
          'combo__option--highlighted':  index === highlightedIndex,
          'combo__option--disabled':     option.isDisabled
        }"
        role="option"
        :aria-selected="isOptionSelected(option.value)"
        @mousedown.prevent="selectOption(option)"
        @mouseover="highlightedIndex = index"
      >
        <!-- 다중 선택 체크박스 표시 -->
        <span v-if="isMultiple" class="combo__checkbox" aria-hidden="true">
          <svg v-if="isOptionSelected(option.value)" width="14" height="14" viewBox="0 0 14 14">
            <rect x="1" y="1" width="12" height="12" rx="2" fill="#667eea"/>
            <path d="M3.5 7L5.5 9L10.5 4.5" stroke="white" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
          <svg v-else width="14" height="14" viewBox="0 0 14 14">
            <rect x="1" y="1" width="12" height="12" rx="2" stroke="#d9d9d9" fill="none"/>
          </svg>
        </span>
        {{ option.label }}
      </li>
    </ul>

  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseComboBox' })

import type { FormOption, FormSize } from '~/types/form'

// ─── Props (TypeScript 유니온 타입 방식 — Vue 3.4+) ───────────────────────
interface Props {
  options?:         FormOption[]
  isMultiple?:      boolean
  placeholder?:     string
  isDisabled?:      boolean
  isLoading?:       boolean
  noResultMessage?: string
  size?:            FormSize
}

const props = withDefaults(defineProps<Props>(), {
  options:         () => [],
  isMultiple:      false,
  placeholder:     '검색...',
  isDisabled:      false,
  isLoading:       false,
  noResultMessage: '검색 결과 없음',
  size:            'md',
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
// ❌ 구식: modelValue prop + emit('update:modelValue')
// ✅ 현대: defineModel로 양방향 바인딩 단순화
const model = defineModel<string | string[]>({ default: '' })

// ─── useTemplateRef (Vue 3.5+) ────────────────────────────────────────────
// ❌ 구식: const comboRef = ref<HTMLElement | null>(null)  + template ref="comboRef"
// ✅ 현대: useTemplateRef()는 타입 안전하고 명시적
const comboRef  = useTemplateRef<HTMLElement>('comboRef')
const inputRef  = useTemplateRef<HTMLInputElement>('inputRef')

// ─── State ────────────────────────────────────────────────────────────────
// Nuxt auto-imports: ref는 import 없이 사용 가능
const searchQuery      = ref('')
const isOpen           = ref(false)
const highlightedIndex = ref(-1)

// ─── onClickOutside (VueUse) ──────────────────────────────────────────────
// ❌ 구식:
//   onMounted(() => document.addEventListener('mousedown', handleOutsideClick))
//   onUnmounted(() => document.removeEventListener('mousedown', handleOutsideClick))
// ✅ 현대: VueUse onClickOutside (Nuxt auto-imports로 import 불필요)
onClickOutside(comboRef, closeDropdown)

// ─── Computed ─────────────────────────────────────────────────────────────
// Nuxt auto-imports: computed는 import 없이 사용 가능

/** 검색어로 필터링된 옵션 목록 */
const filteredOptions = computed(() =>
  props.options.filter(opt =>
    opt.label.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
)

/** 다중 선택 chip 목록 */
const selectedChips = computed(() =>
  props.isMultiple && Array.isArray(model.value) ? model.value : []
)

/** 입력창 placeholder: 다중 선택에서 이미 선택된 항목이 있으면 숨김 */
const inputPlaceholder = computed(() =>
  selectedChips.value.length > 0 ? '' : props.placeholder
)

const wrapperClasses = computed(() => [
  'combo',
  `combo--${props.size}`,
  { 'combo--disabled': props.isDisabled }
])

// ─── Methods ──────────────────────────────────────────────────────────────

const getLabelByValue = (value: string) =>
  props.options.find(o => String(o.value) === value)?.label ?? value

const isOptionSelected = (value: string | number): boolean => {
  const strVal = String(value)
  if (props.isMultiple) {
    return Array.isArray(model.value) && model.value.includes(strVal)
  }
  return model.value === strVal
}

const openDropdown = () => {
  if (props.isDisabled) return
  isOpen.value = true
  inputRef.value?.focus()
}

function closeDropdown() {
  isOpen.value = false
  searchQuery.value = ''
  highlightedIndex.value = -1
}

const selectOption = (option: FormOption) => {
  if (option.isDisabled) return
  const strVal = String(option.value)

  if (props.isMultiple) {
    const current = Array.isArray(model.value) ? [...model.value] : []
    const idx = current.indexOf(strVal)
    if (idx >= 0) {
      current.splice(idx, 1)
    } else {
      current.push(strVal)
    }
    // defineModel: model.value에 직접 할당
    model.value = current
    searchQuery.value = ''
    inputRef.value?.focus()
  } else {
    model.value = strVal
    closeDropdown()
  }
}

const removeChip = (value: string) => {
  if (!Array.isArray(model.value)) return
  model.value = model.value.filter(v => v !== value)
}

const selectHighlighted = () => {
  if (highlightedIndex.value >= 0 && filteredOptions.value[highlightedIndex.value]) {
    selectOption(filteredOptions.value[highlightedIndex.value])
  }
}

const moveHighlight = (direction: 1 | -1) => {
  const max = filteredOptions.value.length - 1
  highlightedIndex.value = Math.max(0, Math.min(max, highlightedIndex.value + direction))
}
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
}

/* ── Wrapper ── */
.combo { position: relative; display: inline-block; min-width: 200px; }

/* ── 컨트롤 영역 ── */
.combo__control {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: #fff;
  cursor: text;
  transition: border-color 0.2s, box-shadow 0.2s;
  padding: 4px 36px 4px 8px;
  min-height: 40px;
  box-sizing: border-box;
}

.combo__control--focused { border-color: var(--color-primary); box-shadow: 0 0 0 3px var(--color-primary-light); }
.combo__control--disabled { background: #f5f5f5; cursor: not-allowed; }

/* 크기별 min-height */
.combo--sm .combo__control { min-height: 32px; padding: 2px 32px 2px 6px; }
.combo--lg .combo__control { min-height: 48px; padding: 6px 40px 6px 12px; }

/* ── 검색 입력창 ── */
.combo__input {
  border: none;
  outline: none;
  background: transparent;
  font-size: 14px;
  color: #333;
  min-width: 60px;
  flex: 1;
  padding: 0;
  font-family: inherit;
}

.combo--sm .combo__input { font-size: 12px; }
.combo--lg .combo__input { font-size: 16px; }
.combo--disabled .combo__input { cursor: not-allowed; }

/* ── 화살표 아이콘 ── */
.combo__arrow {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #aaa;
  pointer-events: none;
  display: flex;
}

/* ── chip ── */
.combo__chip {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 2px 8px;
  background: #eff2ff;
  color: var(--color-primary);
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

.combo__chip-remove {
  border: none;
  background: none;
  color: var(--color-primary);
  cursor: pointer;
  padding: 0;
  font-size: 14px;
  line-height: 1;
  display: flex;
  align-items: center;
}

.combo__chip-remove:hover { color: #ff4d4f; }

/* ── 드롭다운 ── */
.combo__dropdown {
  position: absolute;
  top: calc(100% + 4px);
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  z-index: 100;
  max-height: 240px;
  overflow-y: auto;
  list-style: none;
  margin: 0;
  padding: 4px 0;
}

/* ── 드롭다운 옵션 ── */
.combo__option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 14px;
  color: #333;
  transition: background 0.1s;
}

.combo__option:hover,
.combo__option--highlighted { background: #f5f7ff; }

.combo__option--selected { color: var(--color-primary); font-weight: 500; }
.combo__option--disabled  { color: #ccc; cursor: not-allowed; pointer-events: none; }
.combo__option--loading,
.combo__option--empty     { color: #aaa; cursor: default; justify-content: center; }

/* ── 체크박스 아이콘 ── */
.combo__checkbox { display: inline-flex; flex-shrink: 0; }

/* ── 로딩 스피너 ── */
.combo__spinner {
  display: inline-block;
  width: 14px;
  height: 14px;
  border: 2px solid #e8e8e8;
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: combo-spin 0.6s linear infinite;
  margin-right: 6px;
}
@keyframes combo-spin { to { transform: rotate(360deg); } }
</style>
```

### 사용 예시

```vue
<!-- 기본 단일 선택 -->
<BaseComboBox
  v-model="selectedMenu"
  :options="menuOptions"
  placeholder="메뉴 검색..."
/>

<!-- 다중 선택 -->
<BaseComboBox
  v-model="selectedMenus"
  :options="menuOptions"
  :is-multiple="true"
  placeholder="메뉴 다중 선택..."
/>

<!-- 비동기 옵션 로딩 -->
<BaseComboBox
  v-model="selectedItem"
  :options="asyncOptions"
  :is-loading="isLoadingOptions"
  no-result-message="검색 결과가 없습니다"
/>
```

---

## 7. BaseSelectBox — 셀렉트박스

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `isMultiple=true` | 다중 선택 — `Ctrl/Cmd` 클릭으로 항목 추가 선택 |
| `selectFirstByDefault=true` | 마운트 시 options[0]을 자동 선택 |

### selectFirstByDefault 동작

```
selectFirstByDefault=false (기본):
┌─────────────────────────────┐
│ 선택하세요           ▼      │  ← placeholder 표시
└─────────────────────────────┘

selectFirstByDefault=true:
┌─────────────────────────────┐
│ 전체                  ▼     │  ← options[0] 자동 선택됨
└─────────────────────────────┘
→ 마운트 시 model.value = options[0].value 자동 설정
```

### isMultiple 동작

```
isMultiple=false (기본):
  modelValue = string
  클릭 시 단일 값 선택

isMultiple=true:
  modelValue = string[]
  Ctrl(Mac: Cmd) + 클릭 → 다중 선택 가능
  ┌─────────────────────────────┐
  │ ▶ 전체                      │
  │ ▶ 메뉴 관리  ← 선택됨       │ (파란 배경)
  │ ▶ 설정      ← 선택됨       │ (파란 배경)
  │   대시보드                  │
  └─────────────────────────────┘
```

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `String\|String[]` | `''` | 선택된 값 |
| `options` | `FormOption[]` | `[]` | 옵션 항목 배열 |
| `isMultiple` | `Boolean` | `false` | **다중 선택** |
| `selectFirstByDefault` | `Boolean` | `false` | **마운트 시 첫 번째 값 자동 선택** |
| `placeholder` | `String` | `'선택하세요'` | 미선택 시 표시 텍스트 (`selectFirstByDefault=false` 시) |
| `isDisabled` | `Boolean` | `false` | 비활성화 |
| `size` | `String` | `'md'` | sm / md / lg |

### 컴포넌트 코드

```vue
<!-- components/base/BaseSelectBox.vue -->
<template>
  <div :class="wrapperClasses" ref="selectRef">

    <!-- 선택창 -->
    <div
      class="select__control"
      :class="{
        'select__control--open':     isOpen,
        'select__control--disabled': isDisabled
      }"
      :tabindex="isDisabled ? -1 : 0"
      :aria-expanded="isOpen"
      :aria-haspopup="'listbox'"
      :aria-disabled="isDisabled"
      @click="toggleDropdown"
      @keydown.enter.prevent="toggleDropdown"
      @keydown.space.prevent="toggleDropdown"
      @keydown.escape="closeDropdown"
    >
      <!-- 선택된 값 표시 -->
      <span class="select__value">
        <!-- 다중 선택: 개수 표시 -->
        <template v-if="isMultiple && Array.isArray(model) && model.length > 0">
          {{ selectedLabels.join(', ') }}
          <span class="select__badge">{{ model.length }}</span>
        </template>
        <!-- 단일 선택: 선택된 레이블 표시 -->
        <template v-else-if="!isMultiple && model">
          {{ getLabel(String(model)) }}
        </template>
        <!-- 미선택: placeholder -->
        <template v-else>
          <span class="select__placeholder">{{ placeholder }}</span>
        </template>
      </span>

      <!-- 화살표 -->
      <span class="select__arrow" aria-hidden="true">
        <svg width="12" height="12" viewBox="0 0 12 12">
          <path
            :d="isOpen ? 'M1 8L6 3L11 8' : 'M1 4L6 9L11 4'"
            stroke="currentColor" stroke-width="1.5"
            stroke-linecap="round" stroke-linejoin="round"
            fill="none"
          />
        </svg>
      </span>
    </div>

    <!-- 드롭다운 목록 -->
    <ul
      v-if="isOpen"
      class="select__dropdown"
      role="listbox"
      :aria-multiselectable="isMultiple"
    >
      <!-- 다중 선택 안내 -->
      <li v-if="isMultiple" class="select__hint">
        Ctrl(Mac: ⌘) + 클릭으로 다중 선택
      </li>

      <!-- 스타일 가이드: v-for에 :key 필수 -->
      <li
        v-for="option in options"
        :key="option.value"
        class="select__option"
        :class="{
          'select__option--selected': isOptionSelected(option.value),
          'select__option--disabled': option.isDisabled
        }"
        role="option"
        :aria-selected="isOptionSelected(option.value)"
        @click.exact="handleSelect(option, false)"
        @click.ctrl="handleSelect(option, true)"
        @click.meta="handleSelect(option, true)"
      >
        <!-- 다중 선택 체크 마크 -->
        <span v-if="isMultiple" class="select__check" aria-hidden="true">
          <svg v-if="isOptionSelected(option.value)" width="14" height="14" viewBox="0 0 14 14">
            <path d="M2.5 7L5.5 10L11.5 4" stroke="#667eea" stroke-width="2" stroke-linecap="round" fill="none"/>
          </svg>
        </span>
        {{ option.label }}
      </li>

      <li v-if="options.length === 0" class="select__option select__option--empty">
        옵션이 없습니다
      </li>
    </ul>

  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseSelectBox' })

import type { FormOption, FormSize } from '~/types/form'

// ─── Props (TypeScript 유니온 타입 방식 — Vue 3.4+) ───────────────────────
interface Props {
  options?:              FormOption[]
  isMultiple?:           boolean
  selectFirstByDefault?: boolean
  placeholder?:          string
  isDisabled?:           boolean
  size?:                 FormSize
}

const props = withDefaults(defineProps<Props>(), {
  options:              () => [],
  isMultiple:           false,
  selectFirstByDefault: false,
  placeholder:          '선택하세요',
  isDisabled:           false,
  size:                 'md',
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
// ❌ 구식: modelValue prop + emit('update:modelValue')
// ✅ 현대: defineModel로 양방향 바인딩 단순화
const model = defineModel<string | string[]>({ default: '' })

// ─── useTemplateRef (Vue 3.5+) ────────────────────────────────────────────
// ❌ 구식: const selectRef = ref<HTMLElement | null>(null)
// ✅ 현대: useTemplateRef()
const selectRef = useTemplateRef<HTMLElement>('selectRef')

// ─── State ────────────────────────────────────────────────────────────────
// Nuxt auto-imports: ref는 import 없이 사용 가능
const isOpen = ref(false)

// ─── onClickOutside (VueUse) ──────────────────────────────────────────────
// ❌ 구식:
//   onMounted(() => document.addEventListener('mousedown', handleOutsideClick))
//   onUnmounted(() => document.removeEventListener('mousedown', handleOutsideClick))
// ✅ 현대: VueUse onClickOutside (Nuxt auto-imports로 import 불필요)
onClickOutside(selectRef, closeDropdown)

// ─── Computed ─────────────────────────────────────────────────────────────
// Nuxt auto-imports: computed는 import 없이 사용 가능

const wrapperClasses = computed(() => [
  'select',
  `select--${props.size}`,
  { 'select--disabled': props.isDisabled }
])

/** 다중 선택 시 선택된 레이블 배열 */
const selectedLabels = computed(() => {
  if (!Array.isArray(model.value)) return []
  return model.value.map(v => getLabel(v)).filter(Boolean)
})

// ─── Methods ──────────────────────────────────────────────────────────────

const getLabel = (value: string) =>
  props.options.find(o => String(o.value) === value)?.label ?? value

const isOptionSelected = (value: string | number): boolean => {
  const strVal = String(value)
  if (props.isMultiple) {
    return Array.isArray(model.value) && model.value.includes(strVal)
  }
  return model.value === strVal
}

const toggleDropdown = () => {
  if (props.isDisabled) return
  isOpen.value = !isOpen.value
}

function closeDropdown() { isOpen.value = false }

/**
 * 항목 선택 처리
 * @param option  선택한 항목
 * @param isCtrl  Ctrl/Cmd 키 누름 여부 (다중 선택 트리거)
 */
const handleSelect = (option: FormOption, isCtrl: boolean) => {
  if (option.isDisabled) return
  const strVal = String(option.value)

  if (props.isMultiple && isCtrl) {
    // 다중 선택 모드 + Ctrl 클릭: 토글 추가/제거
    const current = Array.isArray(model.value) ? [...model.value] : []
    const idx = current.indexOf(strVal)
    if (idx >= 0) {
      current.splice(idx, 1)
    } else {
      current.push(strVal)
    }
    // defineModel: model.value에 직접 할당
    model.value = current
  } else if (props.isMultiple && !isCtrl) {
    // 다중 선택 모드 + 일반 클릭: 해당 항목만 단독 선택
    model.value = [strVal]
    closeDropdown()
  } else {
    // 단일 선택
    model.value = strVal
    closeDropdown()
  }
}

// ─── Lifecycle ────────────────────────────────────────────────────────────
// Nuxt auto-imports: onMounted, watch는 import 없이 사용 가능

onMounted(() => {
  // selectFirstByDefault: 마운트 시 첫 번째 옵션 자동 선택
  if (props.selectFirstByDefault && props.options.length > 0) {
    const firstOption = props.options[0]
    if (props.isMultiple) {
      model.value = [String(firstOption.value)]
    } else {
      model.value = String(firstOption.value)
    }
  }
})

// options가 변경되고 selectFirstByDefault=true이면 재적용
watch(
  () => props.options,
  (newOptions) => {
    if (props.selectFirstByDefault && newOptions.length > 0) {
      const hasSelection = props.isMultiple
        ? Array.isArray(model.value) && model.value.length > 0
        : !!model.value
      if (!hasSelection) {
        model.value = props.isMultiple
          ? [String(newOptions[0].value)]
          : String(newOptions[0].value)
      }
    }
  }
)
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
}

/* ── Wrapper ── */
.select { position: relative; display: inline-block; min-width: 160px; }

/* ── 컨트롤 ── */
.select__control {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
  user-select: none;
  gap: 8px;
}

.select__control:focus,
.select__control--open {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-light);
  outline: none;
}

.select__control--disabled { background: #f5f5f5; cursor: not-allowed; }

/* 크기 */
.select--sm .select__control { height: 32px; padding: 0 10px; font-size: 12px; }
.select--md .select__control { height: 40px; padding: 0 12px; font-size: 14px; }
.select--lg .select__control { height: 48px; padding: 0 16px; font-size: 16px; }

/* ── 값 표시 영역 ── */
.select__value {
  display: flex;
  align-items: center;
  gap: 6px;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #333;
}

.select__placeholder { color: #bbb; }

/* 다중 선택 개수 badge */
.select__badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  background: var(--color-primary);
  color: #fff;
  border-radius: 9px;
  font-size: 11px;
  font-weight: 600;
  flex-shrink: 0;
}

/* ── 화살표 ── */
.select__arrow {
  display: flex;
  align-items: center;
  color: #aaa;
  flex-shrink: 0;
  transition: color 0.15s;
}

.select__control--open .select__arrow,
.select__control:hover .select__arrow { color: var(--color-primary); }

/* ── 드롭다운 ── */
.select__dropdown {
  position: absolute;
  top: calc(100% + 4px);
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  z-index: 100;
  max-height: 240px;
  overflow-y: auto;
  list-style: none;
  margin: 0;
  padding: 4px 0;
}

/* 다중 선택 안내 텍스트 */
.select__hint {
  padding: 6px 12px;
  font-size: 11px;
  color: #aaa;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
  cursor: default;
}

/* ── 드롭다운 옵션 ── */
.select__option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 9px 12px;
  cursor: pointer;
  font-size: 14px;
  color: #333;
  transition: background 0.1s;
}

.select--sm .select__option { padding: 6px 10px; font-size: 12px; }
.select--lg .select__option { padding: 11px 16px; font-size: 16px; }

.select__option:hover             { background: #f5f7ff; }
.select__option--selected         { color: var(--color-primary); font-weight: 500; background: #f0f3ff; }
.select__option--disabled         { color: #ccc; cursor: not-allowed; pointer-events: none; }
.select__option--empty            { color: #aaa; cursor: default; justify-content: center; }

/* ── 체크 마크 ── */
.select__check {
  display: inline-flex;
  align-items: center;
  width: 14px;
  flex-shrink: 0;
}
</style>
```

### 사용 예시

```vue
<!-- 기본 단일 선택 + placeholder -->
<BaseSelectBox
  v-model="menuGroup"
  :options="[
    { value: 'ALL', label: '전체' },
    { value: 'ADMIN', label: 'ADMIN' },
    { value: 'USER', label: 'USER' }
  ]"
  placeholder="그룹 선택"
/>

<!-- 첫 번째 값 자동 선택 (= '전체' 자동 선택) -->
<BaseSelectBox
  v-model="menuGroup"
  :options="groupOptions"
  :select-first-by-default="true"
/>

<!-- 다중 선택 (Ctrl + 클릭) -->
<BaseSelectBox
  v-model="selectedGroups"
  :options="groupOptions"
  :is-multiple="true"
/>

<!-- 다중 선택 + 첫 번째 값 자동 선택 -->
<BaseSelectBox
  v-model="selectedGroups"
  :options="groupOptions"
  :is-multiple="true"
  :select-first-by-default="true"
/>
```

---

## 8. BaseCheckBox — 체크박스

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `isIndeterminate=true` | 부분 선택 상태 (- 아이콘 표시, 그룹 헤더 체크박스에 활용) |
| `value` prop + `String[]` v-model | 그룹 사용 — 배열에 value를 추가/제거 |

### 단일 vs 그룹 동작

```
단일 체크박스 (v-model: boolean):
  ☑ 사용 여부       → model = true / false

그룹 체크박스 (v-model: string[]):
  ☑ 가동            value="run"    → model = ['run', ...]
  ☐ 중지            value="stop"
  ☑ 점검            value="check"  → model = ['run', 'check']

isIndeterminate=true:
  ⊟ 전체 선택       → 일부만 선택된 헤더 체크박스 표시
```

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `Boolean\|String[]` | `false` | v-model 값 (단일: boolean, 그룹: string[]) |
| `value` | `String` | `undefined` | 그룹 사용 시 이 체크박스의 고유 값 |
| `label` | `String` | `undefined` | 체크박스 우측 레이블 |
| `size` | `String` | `'md'` | sm / md / lg |
| `isDisabled` | `Boolean` | `false` | 비활성화 |
| `isIndeterminate` | `Boolean` | `false` | 부분 선택 상태 표시 |

### 컴포넌트 코드

```vue
<!-- components/base/BaseCheckBox.vue -->
<template>
  <label :class="wrapperClasses">

    <!-- 숨겨진 네이티브 체크박스 (접근성 유지) -->
    <input
      :id="checkId"
      type="checkbox"
      :checked="isChecked"
      :disabled="isDisabled"
      :aria-label="label"
      :aria-checked="isIndeterminate ? 'mixed' : isChecked"
      class="checkbox__native"
      @change="handleChange"
    />

    <!-- 커스텀 박스 아이콘 -->
    <span class="checkbox__box" :class="boxClasses" aria-hidden="true">
      <!-- 부분 선택 (indeterminate) -->
      <svg v-if="isIndeterminate" width="10" height="2" viewBox="0 0 10 2">
        <rect width="10" height="2" rx="1" fill="currentColor"/>
      </svg>
      <!-- 선택됨 체크 아이콘 -->
      <svg v-else-if="isChecked" width="10" height="8" viewBox="0 0 10 8">
        <path d="M1 4L3.5 6.5L9 1" stroke="currentColor" stroke-width="1.8"
              stroke-linecap="round" stroke-linejoin="round" fill="none"/>
      </svg>
    </span>

    <!-- 레이블 -->
    <span v-if="label" class="checkbox__label">{{ label }}</span>

  </label>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseCheckBox' })

import type { FormSize } from '~/types/form'

// ─── Props ────────────────────────────────────────────────────────────────
interface Props {
  value?:           string    // 그룹 사용 시 이 체크박스의 식별 값
  label?:           string
  size?:            FormSize
  isDisabled?:      boolean
  isIndeterminate?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  size:             'md',
  isDisabled:       false,
  isIndeterminate:  false,
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
// 단일: boolean, 그룹: string[]
const model = defineModel<boolean | string[]>({ default: false })

// ─── useId (Vue 3.5+) — SSR 안전한 고유 ID ───────────────────────────────
const checkId = useId()

// ─── Computed ─────────────────────────────────────────────────────────────
const isChecked = computed((): boolean => {
  if (Array.isArray(model.value)) {
    // 그룹 모드: value prop이 배열에 포함되어 있는지 확인
    return props.value !== undefined && model.value.includes(props.value)
  }
  return model.value === true
})

const wrapperClasses = computed(() => [
  'checkbox',
  `checkbox--${props.size}`,
  {
    'checkbox--checked':       isChecked.value,
    'checkbox--indeterminate': props.isIndeterminate,
    'checkbox--disabled':      props.isDisabled,
  }
])

const boxClasses = computed(() => ({
  'checkbox__box--checked':       isChecked.value,
  'checkbox__box--indeterminate': props.isIndeterminate,
}))

// ─── Methods ──────────────────────────────────────────────────────────────
const handleChange = (event: Event) => {
  const checked = (event.target as HTMLInputElement).checked

  if (Array.isArray(model.value)) {
    // 그룹 모드: 배열에서 value 추가/제거
    const current = [...model.value]
    if (checked && props.value !== undefined) {
      current.push(props.value)
    } else if (!checked && props.value !== undefined) {
      const idx = current.indexOf(props.value)
      if (idx >= 0) current.splice(idx, 1)
    }
    model.value = current
  } else {
    // 단일 모드: boolean 토글
    model.value = checked
  }
}
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
}

/* ── 래퍼 (label 태그) ── */
.checkbox {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  user-select: none;
  line-height: 1;
}

.checkbox--disabled { opacity: 0.45; cursor: not-allowed; pointer-events: none; }

/* ── 네이티브 input 숨김 (접근성 유지) ── */
.checkbox__native {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  padding: 0;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}

/* ── 커스텀 박스 ── */
.checkbox__box {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border: 1.5px solid var(--color-border);
  border-radius: 3px;
  background: #fff;
  color: #fff;
  transition: background 0.15s, border-color 0.15s;
}

/* 크기 */
.checkbox--sm .checkbox__box { width: 14px; height: 14px; }
.checkbox--md .checkbox__box { width: 16px; height: 16px; }
.checkbox--lg .checkbox__box { width: 20px; height: 20px; }

/* 선택된 상태 */
.checkbox__box--checked,
.checkbox__box--indeterminate {
  background: var(--color-primary);
  border-color: var(--color-primary);
}

/* 포커스 표시 (키보드 접근성) */
.checkbox__native:focus-visible + .checkbox__box {
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* ── 레이블 ── */
.checkbox--sm .checkbox__label { font-size: 12px; color: #444; }
.checkbox--md .checkbox__label { font-size: 14px; color: #444; }
.checkbox--lg .checkbox__label { font-size: 16px; color: #444; }
</style>
```

### 사용 예시

```vue
<!-- 단일 체크박스 (boolean v-model) -->
<BaseCheckBox v-model="isActive" label="사용 여부" />

<!-- 그룹 체크박스 (string[] v-model) -->
<BaseCheckBox v-model="selectedStatuses" value="run"   label="가동" />
<BaseCheckBox v-model="selectedStatuses" value="stop"  label="중지" />
<BaseCheckBox v-model="selectedStatuses" value="check" label="점검" />

<!-- 부분 선택 상태 (헤더 체크박스) -->
<BaseCheckBox
  v-model="isAllSelected"
  label="전체 선택"
  :is-indeterminate="isSomeSelected && !isAllSelected"
/>

<!-- 비활성화 -->
<BaseCheckBox v-model="isFixed" label="고정값" :is-disabled="true" />
```

---

## 9. BaseRadioGroup — 라디오 그룹

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `direction='horizontal'` | 가로 배열 (기본값: `'vertical'`) |
| `options` | 라디오 항목 배열 — label/value/isDisabled |

### 동작 방식

```
vertical (기본):
  ● 가동
  ○ 중지
  ○ 점검

horizontal:
  ● 가동  ○ 중지  ○ 점검
```

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `String` | `''` | 선택된 값 |
| `options` | `FormOption[]` | `[]` | 라디오 항목 배열 |
| `direction` | `'vertical'\|'horizontal'` | `'vertical'` | 배열 방향 |
| `size` | `String` | `'md'` | sm / md / lg |
| `isDisabled` | `Boolean` | `false` | 전체 그룹 비활성화 |
| `ariaLabel` | `String` | `'라디오 그룹'` | WAI-ARIA 그룹 레이블 |

### 컴포넌트 코드

```vue
<!-- components/base/BaseRadioGroup.vue -->
<template>
  <div
    role="radiogroup"
    :aria-label="ariaLabel"
    :class="groupClasses"
  >
    <label
      v-for="item in options"
      :key="item.value"
      :class="itemClasses(item)"
    >
      <!-- 네이티브 라디오 (접근성 유지) -->
      <input
        type="radio"
        :value="item.value"
        :checked="model === String(item.value)"
        :disabled="isDisabled || item.isDisabled"
        :aria-label="item.label"
        class="radio__native"
        @change="model = String(item.value)"
      />

      <!-- 커스텀 라디오 점 -->
      <span class="radio__dot" aria-hidden="true">
        <span
          v-if="model === String(item.value)"
          class="radio__dot-inner"
        />
      </span>

      <!-- 레이블 -->
      <span class="radio__label">{{ item.label }}</span>
    </label>
  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseRadioGroup' })

import type { FormOption, FormSize } from '~/types/form'

// ─── Props ────────────────────────────────────────────────────────────────
interface Props {
  options?:    FormOption[]
  direction?:  'vertical' | 'horizontal'
  size?:       FormSize
  isDisabled?: boolean
  ariaLabel?:  string
}

const props = withDefaults(defineProps<Props>(), {
  options:    () => [],
  direction:  'vertical',
  size:       'md',
  isDisabled: false,
  ariaLabel:  '라디오 그룹',
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
const model = defineModel<string>({ default: '' })

// ─── Computed ─────────────────────────────────────────────────────────────
const groupClasses = computed(() => [
  'radio-group',
  `radio-group--${props.direction}`,
  `radio-group--${props.size}`,
  { 'radio-group--disabled': props.isDisabled }
])

const itemClasses = (item: FormOption) => [
  'radio-group__item',
  {
    'radio-group__item--checked':  model.value === String(item.value),
    'radio-group__item--disabled': props.isDisabled || item.isDisabled,
  }
]
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
}

/* ── 그룹 컨테이너 ── */
.radio-group { display: flex; gap: 8px; }
.radio-group--vertical    { flex-direction: column; }
.radio-group--horizontal  { flex-direction: row; flex-wrap: wrap; }
.radio-group--disabled    { opacity: 0.45; pointer-events: none; }

/* ── 항목 (label) ── */
.radio-group__item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  user-select: none;
}

.radio-group__item--disabled { cursor: not-allowed; }

/* ── 네이티브 input 숨김 ── */
.radio__native {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}

/* ── 커스텀 라디오 원 ── */
.radio__dot {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border-radius: 50%;
  border: 1.5px solid var(--color-border);
  background: #fff;
  transition: border-color 0.15s;
}

/* 크기 */
.radio-group--sm .radio__dot { width: 14px; height: 14px; }
.radio-group--md .radio__dot { width: 16px; height: 16px; }
.radio-group--lg .radio__dot { width: 20px; height: 20px; }

/* 선택된 항목의 원 */
.radio-group__item--checked .radio__dot {
  border-color: var(--color-primary);
}

/* ── 내부 채워진 점 ── */
.radio__dot-inner {
  display: block;
  border-radius: 50%;
  background: var(--color-primary);
}

.radio-group--sm .radio__dot-inner { width: 6px;  height: 6px;  }
.radio-group--md .radio__dot-inner { width: 8px;  height: 8px;  }
.radio-group--lg .radio__dot-inner { width: 10px; height: 10px; }

/* 포커스 표시 */
.radio__native:focus-visible + .radio__dot {
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* ── 레이블 ── */
.radio-group--sm .radio__label { font-size: 12px; color: #444; }
.radio-group--md .radio__label { font-size: 14px; color: #444; }
.radio-group--lg .radio__label { font-size: 16px; color: #444; }

.radio-group__item--checked .radio__label {
  color: var(--color-primary);
  font-weight: 500;
}
</style>
```

### 사용 예시

```vue
<!-- 가로 배열 -->
<BaseRadioGroup
  v-model="machineStatus"
  direction="horizontal"
  :options="[
    { value: 'run',   label: '가동' },
    { value: 'stop',  label: '중지' },
    { value: 'check', label: '점검' },
  ]"
/>

<!-- 세로 배열 (기본) -->
<BaseRadioGroup
  v-model="shiftType"
  :options="shiftOptions"
  aria-label="근무 유형 선택"
/>

<!-- 일부 비활성화 -->
<BaseRadioGroup
  v-model="priority"
  :options="[
    { value: 'high',   label: '높음' },
    { value: 'normal', label: '보통' },
    { value: 'low',    label: '낮음', isDisabled: true },
  ]"
/>
```

---

## 10. BaseTextArea — 텍스트에어리어

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `rows` | 기본 표시 줄 수 (기본값: `4`) |
| `maxLength` | 최대 입력 글자 수 + 카운터 표시 |
| `isResizable` | 사용자 세로 크기 조절 허용 여부 |

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `String` | `''` | v-model 값 |
| `placeholder` | `String` | `''` | placeholder 텍스트 |
| `label` | `String` | `undefined` | 레이블 텍스트 |
| `rows` | `Number` | `4` | 기본 표시 줄 수 |
| `maxLength` | `Number` | `undefined` | 최대 입력 글자 수 |
| `isDisabled` | `Boolean` | `false` | 비활성화 |
| `isReadonly` | `Boolean` | `false` | 읽기 전용 |
| `isResizable` | `Boolean` | `true` | 세로 크기 조절 허용 |
| `block` | `Boolean` | `false` | 전체 너비 |

### 컴포넌트 코드

```vue
<!-- components/base/BaseTextArea.vue -->
<template>
  <div :class="wrapperClasses">

    <!-- 레이블 -->
    <label v-if="label" :for="textareaId" class="textarea__label">
      {{ label }}
    </label>

    <!-- 입력 영역 래퍼 -->
    <div class="textarea__wrap">
      <textarea
        :id="textareaId"
        v-model="model"
        :placeholder="placeholder"
        :disabled="isDisabled"
        :readonly="isReadonly"
        :rows="rows"
        :maxlength="maxLength"
        :aria-label="label ?? placeholder"
        :aria-disabled="isDisabled"
        :class="inputClasses"
        @blur="emit('blur', $event)"
        @focus="emit('focus', $event)"
      />

      <!-- 글자 수 카운터 -->
      <span v-if="maxLength" class="textarea__counter" aria-live="polite">
        {{ model.length }} / {{ maxLength }}
      </span>
    </div>

  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseTextArea' })

// ─── Props ────────────────────────────────────────────────────────────────
interface Props {
  placeholder?: string
  label?:       string
  rows?:        number
  maxLength?:   number
  isDisabled?:  boolean
  isReadonly?:  boolean
  isResizable?: boolean
  block?:       boolean
}

const props = withDefaults(defineProps<Props>(), {
  placeholder: '',
  rows:        4,
  isDisabled:  false,
  isReadonly:  false,
  isResizable: true,
  block:       false,
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
const model = defineModel<string>({ default: '' })

// ─── Emits ────────────────────────────────────────────────────────────────
const emit = defineEmits<{
  'blur':  [event: FocusEvent]
  'focus': [event: FocusEvent]
}>()

// ─── useId (Vue 3.5+) ────────────────────────────────────────────────────
const textareaId = useId()

// ─── Computed ─────────────────────────────────────────────────────────────
const wrapperClasses = computed(() => [
  'textarea',
  { 'textarea--block': props.block }
])

const inputClasses = computed(() => [
  'textarea__input',
  {
    'textarea__input--disabled':    props.isDisabled,
    'textarea__input--readonly':    props.isReadonly,
    'textarea__input--no-resize':   !props.isResizable,
    'textarea__input--has-counter': !!props.maxLength,
  }
])
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
}

/* ── Wrapper ── */
.textarea       { display: inline-flex; flex-direction: column; gap: 4px; }
.textarea--block { display: flex; width: 100%; }

/* ── 레이블 ── */
.textarea__label {
  font-size: 13px;
  font-weight: 500;
  color: #444;
}

/* ── 입력 래퍼 ── */
.textarea__wrap {
  position: relative;
  display: flex;
  width: 100%;
}

/* ── textarea 본체 ── */
.textarea__input {
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  padding: 10px 12px;
  font-size: 14px;
  color: #333;
  background: #fff;
  font-family: inherit;
  line-height: 1.6;
  resize: vertical;
  transition: border-color 0.2s, box-shadow 0.2s;
  outline: none;
  box-sizing: border-box;
}

.textarea__input:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* 카운터가 있을 때 하단 패딩 확보 */
.textarea__input--has-counter { padding-bottom: 28px; }

/* 상태 */
.textarea__input--disabled { background: #f5f5f5; color: #aaa; cursor: not-allowed; }
.textarea__input--readonly  { background: #fafafa; cursor: default; }
.textarea__input--no-resize { resize: none; }

/* ── 글자 수 카운터 ── */
.textarea__counter {
  position: absolute;
  bottom: 8px;
  right: 10px;
  font-size: 11px;
  color: #aaa;
  pointer-events: none;
}
</style>
```

### 사용 예시

```vue
<!-- 기본 사용 -->
<BaseTextArea v-model="memo" label="메모" placeholder="메모를 입력하세요" />

<!-- 글자 수 제한 + 카운터 -->
<BaseTextArea v-model="description" label="설명" :max-length="500" :block="true" />

<!-- 크기 고정 (조절 불가) -->
<BaseTextArea
  v-model="remark"
  label="비고"
  :rows="3"
  :is-resizable="false"
  :block="true"
/>

<!-- 읽기 전용 -->
<BaseTextArea v-model="logContent" label="로그" :is-readonly="true" :rows="6" />
```

---

## 11. BaseDatePicker — 날짜 선택기

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `min` / `max` | 선택 가능 날짜 범위 제한 (`YYYY-MM-DD` 형식) |
| `outputFormat` | `'YYYY-MM-DD'`(기본) 또는 `'YYYYMMDD'` — MES DB 저장 형식 |

### 날짜 형식 동작

```
outputFormat='YYYY-MM-DD' (기본):
  화면 표시 및 v-model 값: '2025-03-23'

outputFormat='YYYYMMDD':
  v-model 값: '20250323'  (DB VARCHAR(8) 저장용)
  화면 표시:  '2025-03-23' (네이티브 날짜 input 형식 유지)
```

> **주의**: 이 컴포넌트는 브라우저 내장 날짜 선택기를 활용합니다.
> 브라우저별 UI 차이가 있을 수 있으며, 커스텀 캘린더가 필요하다면
> `v-calendar` 또는 `vue-datepicker` 라이브러리 도입을 검토하세요.

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `String` | `''` | v-model 값 (날짜 문자열) |
| `label` | `String` | `undefined` | 레이블 텍스트 |
| `placeholder` | `String` | `'날짜 선택'` | placeholder 텍스트 |
| `min` | `String` | `undefined` | 최소 날짜 (`YYYY-MM-DD`) |
| `max` | `String` | `undefined` | 최대 날짜 (`YYYY-MM-DD`) |
| `outputFormat` | `String` | `'YYYY-MM-DD'` | v-model 출력 형식 |
| `isDisabled` | `Boolean` | `false` | 비활성화 |
| `isReadonly` | `Boolean` | `false` | 읽기 전용 |
| `size` | `String` | `'md'` | sm / md / lg |
| `block` | `Boolean` | `false` | 전체 너비 |

### 컴포넌트 코드

```vue
<!-- components/base/BaseDatePicker.vue -->
<template>
  <div :class="wrapperClasses">

    <!-- 레이블 -->
    <label v-if="label" :for="dateId" class="datepicker__label">
      {{ label }}
    </label>

    <!-- 입력 래퍼 -->
    <div class="datepicker__wrap">
      <input
        :id="dateId"
        type="date"
        :value="internalValue"
        :min="min"
        :max="max"
        :disabled="isDisabled"
        :readonly="isReadonly"
        :aria-label="label ?? placeholder"
        :aria-disabled="isDisabled"
        :class="inputClasses"
        @change="handleChange"
      />

      <!-- 달력 아이콘 -->
      <span class="datepicker__icon" aria-hidden="true">
        <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
          <rect x="1" y="2" width="14" height="13" rx="2" stroke="currentColor" stroke-width="1.2"/>
          <path d="M5 1V3M11 1V3" stroke="currentColor" stroke-width="1.2" stroke-linecap="round"/>
          <path d="M1 6H15" stroke="currentColor" stroke-width="1.2"/>
          <rect x="4" y="9" width="2" height="2" rx="0.5" fill="currentColor"/>
          <rect x="7" y="9" width="2" height="2" rx="0.5" fill="currentColor"/>
          <rect x="10" y="9" width="2" height="2" rx="0.5" fill="currentColor"/>
        </svg>
      </span>
    </div>

  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseDatePicker' })

import type { FormSize } from '~/types/form'

// ─── Props ────────────────────────────────────────────────────────────────
interface Props {
  label?:        string
  placeholder?:  string
  min?:          string   // YYYY-MM-DD
  max?:          string   // YYYY-MM-DD
  outputFormat?: 'YYYY-MM-DD' | 'YYYYMMDD'
  isDisabled?:   boolean
  isReadonly?:   boolean
  size?:         FormSize
  block?:        boolean
}

const props = withDefaults(defineProps<Props>(), {
  placeholder:  '날짜 선택',
  outputFormat: 'YYYY-MM-DD',
  isDisabled:   false,
  isReadonly:   false,
  size:         'md',
  block:        false,
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
const model = defineModel<string>({ default: '' })

// ─── useId (Vue 3.5+) ────────────────────────────────────────────────────
const dateId = useId()

// ─── Computed ─────────────────────────────────────────────────────────────

/**
 * 네이티브 input[type=date]은 항상 YYYY-MM-DD 형식을 요구하므로
 * model 값이 YYYYMMDD 형식이라면 변환하여 input에 전달
 */
const internalValue = computed(() => {
  if (!model.value) return ''
  if (props.outputFormat === 'YYYYMMDD' && model.value.length === 8) {
    // '20250323' → '2025-03-23'
    return `${model.value.slice(0, 4)}-${model.value.slice(4, 6)}-${model.value.slice(6, 8)}`
  }
  return model.value
})

const wrapperClasses = computed(() => [
  'datepicker',
  `datepicker--${props.size}`,
  { 'datepicker--block': props.block }
])

const inputClasses = computed(() => [
  'datepicker__input',
  {
    'datepicker__input--disabled': props.isDisabled,
    'datepicker__input--readonly': props.isReadonly,
  }
])

// ─── Methods ──────────────────────────────────────────────────────────────
const handleChange = (event: Event) => {
  const rawValue = (event.target as HTMLInputElement).value  // 항상 YYYY-MM-DD

  if (!rawValue) {
    model.value = ''
    return
  }

  if (props.outputFormat === 'YYYYMMDD') {
    // 'YYYY-MM-DD' → 'YYYYMMDD'
    model.value = rawValue.replace(/-/g, '')
  } else {
    model.value = rawValue
  }
}
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
}

/* ── Wrapper ── */
.datepicker       { display: inline-flex; flex-direction: column; gap: 4px; }
.datepicker--block { display: flex; width: 100%; }

/* ── 레이블 ── */
.datepicker__label {
  font-size: 13px;
  font-weight: 500;
  color: #444;
}

/* ── 입력 래퍼 ── */
.datepicker__wrap {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

/* ── 날짜 input ── */
.datepicker__input {
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  padding-left: 12px;
  padding-right: 36px;
  color: #333;
  background: #fff;
  font-family: inherit;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
  cursor: pointer;
  appearance: none; /* 브라우저 기본 아이콘 숨김 (Webkit) */
}

.datepicker__input::-webkit-calendar-picker-indicator {
  opacity: 0;  /* 기본 달력 아이콘 숨기고 커스텀 아이콘 표시 */
  position: absolute;
  inset: 0;
  width: 100%;
  cursor: pointer;
}

.datepicker__input:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* 크기 */
.datepicker--sm .datepicker__input { height: 32px; font-size: 12px; }
.datepicker--md .datepicker__input { height: 40px; font-size: 14px; }
.datepicker--lg .datepicker__input { height: 48px; font-size: 16px; }

/* 상태 */
.datepicker__input--disabled { background: #f5f5f5; color: #aaa; cursor: not-allowed; }
.datepicker__input--readonly  { background: #fafafa; cursor: default; }

/* ── 달력 아이콘 ── */
.datepicker__icon {
  position: absolute;
  right: 10px;
  color: #aaa;
  pointer-events: none;
  display: flex;
  align-items: center;
}
</style>
```

### 사용 예시

```vue
<!-- 기본 사용 (YYYY-MM-DD) -->
<BaseDatePicker v-model="startDate" label="시작일" />

<!-- MES DB 저장용 YYYYMMDD 형식 -->
<BaseDatePicker v-model="prodDate" label="생산일자" output-format="YYYYMMDD" />

<!-- 날짜 범위 제한 -->
<BaseDatePicker
  v-model="searchDate"
  label="조회일자"
  :min="'2020-01-01'"
  :max="today"
/>

<!-- 기간 조회 (시작~종료) -->
<div style="display: flex; gap: 8px; align-items: center;">
  <BaseDatePicker v-model="fromDate" label="조회 기간" />
  <span>~</span>
  <BaseDatePicker v-model="toDate" :min="fromDate" />
</div>
```

---

## 12. BaseNumberInput — 숫자 입력

### 핵심 옵션

| 옵션 | 설명 |
|------|------|
| `useComma=true` | 천 단위 콤마 자동 포맷 (표시용, 실제 v-model은 숫자) |
| `suffix` | 단위 텍스트 표시 (예: `'원'`, `'개'`, `'kg'`) |
| `min` / `max` | 입력 가능 숫자 범위 제한 |
| `decimalPlaces` | 소수점 허용 자릿수 (기본값: `0` — 정수만) |

### 동작 방식

```
useComma=true, suffix='원':
  입력: 1500000
  화면: 1,500,000 원
  v-model: 1500000  (숫자형)

decimalPlaces=2:
  입력: 3.14159
  화면: 3.14  (소수점 2자리로 제한)
  v-model: 3.14

min=0, max=100:
  입력: -5  → 0으로 보정
  입력: 999 → 100으로 보정
```

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `modelValue` | `Number\|null` | `null` | v-model 숫자 값 |
| `label` | `String` | `undefined` | 레이블 텍스트 |
| `placeholder` | `String` | `'0'` | placeholder |
| `min` | `Number` | `undefined` | 최솟값 |
| `max` | `Number` | `undefined` | 최댓값 |
| `decimalPlaces` | `Number` | `0` | 소수점 허용 자릿수 |
| `useComma` | `Boolean` | `false` | 천 단위 콤마 포맷 |
| `suffix` | `String` | `undefined` | 단위 표시 (예: `'원'`, `'개'`) |
| `isDisabled` | `Boolean` | `false` | 비활성화 |
| `isReadonly` | `Boolean` | `false` | 읽기 전용 |
| `size` | `String` | `'md'` | sm / md / lg |
| `block` | `Boolean` | `false` | 전체 너비 |

### 컴포넌트 코드

```vue
<!-- components/base/BaseNumberInput.vue -->
<template>
  <div :class="wrapperClasses">

    <!-- 레이블 -->
    <label v-if="label" :for="inputId" class="number__label">
      {{ label }}
    </label>

    <!-- 입력 래퍼 -->
    <div class="number__wrap">
      <input
        :id="inputId"
        ref="inputRef"
        type="text"
        inputmode="decimal"
        :value="displayValue"
        :placeholder="placeholder"
        :disabled="isDisabled"
        :readonly="isReadonly"
        :aria-label="label ?? placeholder"
        :aria-disabled="isDisabled"
        :class="inputClasses"
        @focus="handleFocus"
        @blur="handleBlur"
        @input="handleInput"
        @keydown="handleKeydown"
      />

      <!-- 단위 -->
      <span v-if="suffix" class="number__suffix" aria-hidden="true">
        {{ suffix }}
      </span>
    </div>

  </div>
</template>

<script setup lang="ts">
// defineOptions: 컴포넌트 명시적 이름 부여 (Vue 3.3+)
defineOptions({ name: 'BaseNumberInput' })

import type { FormSize } from '~/types/form'

// ─── Props ────────────────────────────────────────────────────────────────
interface Props {
  label?:         string
  placeholder?:   string
  min?:           number
  max?:           number
  decimalPlaces?: number
  useComma?:      boolean
  suffix?:        string
  isDisabled?:    boolean
  isReadonly?:    boolean
  size?:          FormSize
  block?:         boolean
}

const props = withDefaults(defineProps<Props>(), {
  placeholder:   '0',
  decimalPlaces: 0,
  useComma:      false,
  isDisabled:    false,
  isReadonly:    false,
  size:          'md',
  block:         false,
})

// ─── defineModel (Vue 3.4+) ───────────────────────────────────────────────
const model = defineModel<number | null>({ default: null })

// ─── useId / useTemplateRef (Vue 3.5+) ────────────────────────────────────
const inputId  = useId()
const inputRef = useTemplateRef<HTMLInputElement>('inputRef')

// ─── State ────────────────────────────────────────────────────────────────
// 포커스 중 여부 (포커스 시 콤마 제거하여 편집 편의 제공)
const isFocused = ref(false)

// ─── Computed ─────────────────────────────────────────────────────────────
/**
 * 화면에 표시할 값
 * - 포커스 중: 콤마 없이 숫자만 (편집 편의)
 * - 포커스 아닐 때: useComma=true이면 천 단위 콤마 포맷
 */
const displayValue = computed(() => {
  if (model.value === null || model.value === undefined) return ''

  if (isFocused.value) {
    // 포커스 중: 원시 숫자 표시
    return String(model.value)
  }

  const num = model.value
  if (props.useComma) {
    // 천 단위 콤마 포맷
    return num.toLocaleString('ko-KR', {
      minimumFractionDigits: 0,
      maximumFractionDigits: props.decimalPlaces,
    })
  }
  return String(num)
})

const wrapperClasses = computed(() => [
  'number',
  `number--${props.size}`,
  { 'number--block': props.block }
])

const inputClasses = computed(() => [
  'number__input',
  {
    'number__input--disabled':    props.isDisabled,
    'number__input--readonly':    props.isReadonly,
    'number__input--has-suffix':  !!props.suffix,
  }
])

// ─── Methods ──────────────────────────────────────────────────────────────
const handleFocus = () => {
  isFocused.value = true
}

const handleBlur = () => {
  isFocused.value = false
  // blur 시 min/max 범위 보정
  if (model.value !== null) {
    let val = model.value
    if (props.min !== undefined) val = Math.max(props.min, val)
    if (props.max !== undefined) val = Math.min(props.max, val)
    model.value = val
  }
}

const handleInput = (event: Event) => {
  const raw = (event.target as HTMLInputElement).value

  // 빈 값 → null
  if (!raw || raw === '-') {
    model.value = null
    return
  }

  // 허용 문자: 숫자, 소수점, 음수 부호
  const decimalPattern = props.decimalPlaces > 0
    ? new RegExp(`^-?\\d*(\\.\\d{0,${props.decimalPlaces}})?$`)
    : /^-?\d*$/

  if (!decimalPattern.test(raw)) {
    // 유효하지 않은 입력: DOM 값 원복
    if (inputRef.value) {
      inputRef.value.value = model.value !== null ? String(model.value) : ''
    }
    return
  }

  const parsed = parseFloat(raw)
  model.value = isNaN(parsed) ? null : parsed
}

/**
 * 숫자 입력만 허용 (방향키, Backspace, Delete, Tab 등 기능 키 통과)
 */
const handleKeydown = (event: KeyboardEvent) => {
  const allowedKeys = [
    'Backspace', 'Delete', 'Tab', 'Escape', 'Enter',
    'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
    'Home', 'End',
  ]
  if (allowedKeys.includes(event.key)) return

  // Ctrl+A, Ctrl+C, Ctrl+V, Ctrl+X 허용
  if (event.ctrlKey || event.metaKey) return

  // 소수점: decimalPlaces > 0 일 때만 허용
  if (event.key === '.' && props.decimalPlaces > 0) return

  // 음수 부호: 첫 번째 위치에서만 허용
  if (event.key === '-' && props.min === undefined) {
    const input = event.target as HTMLInputElement
    if (input.selectionStart === 0 && !input.value.includes('-')) return
  }

  // 숫자가 아닌 입력 차단
  if (!/^\d$/.test(event.key)) {
    event.preventDefault()
  }
}
</script>

<style scoped>
/* ── CSS 디자인 토큰 ── */
:root {
  --color-primary:       #667eea;
  --color-primary-light: rgba(102, 126, 234, 0.15);
  --color-border:        #d9d9d9;
}

/* ── Wrapper ── */
.number       { display: inline-flex; flex-direction: column; gap: 4px; }
.number--block { display: flex; width: 100%; }

/* ── 레이블 ── */
.number__label {
  font-size: 13px;
  font-weight: 500;
  color: #444;
}

/* ── 입력 래퍼 ── */
.number__wrap {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

/* ── 숫자 input ── */
.number__input {
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  color: #333;
  background: #fff;
  font-family: inherit;
  text-align: right; /* 숫자는 우측 정렬 */
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
}

.number__input:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* 크기 */
.number--sm .number__input { height: 32px; padding: 0 10px; font-size: 12px; }
.number--md .number__input { height: 40px; padding: 0 12px; font-size: 14px; }
.number--lg .number__input { height: 48px; padding: 0 16px; font-size: 16px; }

/* 단위가 있을 때 우측 여백 */
.number--sm .number__input--has-suffix { padding-right: 32px; }
.number--md .number__input--has-suffix { padding-right: 36px; }
.number--lg .number__input--has-suffix { padding-right: 44px; }

/* 상태 */
.number__input--disabled { background: #f5f5f5; color: #aaa; cursor: not-allowed; }
.number__input--readonly  { background: #fafafa; cursor: default; }

/* ── 단위 표시 ── */
.number__suffix {
  position: absolute;
  right: 10px;
  font-size: 13px;
  color: #888;
  pointer-events: none;
  white-space: nowrap;
}
</style>
```

### 사용 예시

```vue
<!-- 정수 입력 -->
<BaseNumberInput v-model="quantity" label="수량" suffix="개" :min="0" :max="9999" />

<!-- 천 단위 콤마 + 단위 -->
<BaseNumberInput v-model="price" label="단가" :use-comma="true" suffix="원" :min="0" />

<!-- 소수점 2자리 허용 -->
<BaseNumberInput v-model="weight" label="중량" :decimal-places="2" suffix="kg" :min="0" />

<!-- 비율 (0~100) -->
<BaseNumberInput v-model="ratio" label="불량률" suffix="%" :min="0" :max="100" :decimal-places="1" />

<!-- 읽기 전용 금액 표시 -->
<BaseNumberInput v-model="totalAmount" label="합계금액" :use-comma="true" suffix="원" :is-readonly="true" :block="true" />
```

---

## 13. 실제 MES 페이지 적용 예시 (검색 조건 영역)

```vue
<script setup lang="ts">
// Nuxt auto-imports: ref는 import 없이 사용 가능
import type { FormOption } from '~/types/form'

// ── 검색 조건 상태 ──────────────────────────────────────────────────────
const searchName       = ref('')                // BaseTextBox
const searchGroup      = ref('')                // BaseSelectBox
const selectedStatuses = ref<string[]>([])      // BaseCheckBox 그룹
const selectedShifts   = ref<string[]>([])      // BaseToggleGroup
const selectedMenus    = ref<string[]>([])      // BaseComboBox 다중
const prodType         = ref('')                // BaseRadioGroup
const remark           = ref('')                // BaseTextArea
const fromDate         = ref('')                // BaseDatePicker
const toDate           = ref('')                // BaseDatePicker
const quantity         = ref<number | null>(null)  // BaseNumberInput

// ── 옵션 목록 ──────────────────────────────────────────────────────────
const groupOptions: FormOption[] = [
  { value: '',      label: '전체'   },
  { value: 'ADMIN', label: 'ADMIN' },
  { value: 'USER',  label: 'USER'  },
]

const statusOptions: FormOption[] = [
  { value: 'active',    label: '활성'   },
  { value: 'inactive',  label: '비활성' },
  { value: 'suspended', label: '정지'   },
]

const menuOptions: FormOption[] = [
  { value: 'dashboard', label: '대시보드'  },
  { value: 'menu-mgmt', label: '메뉴 관리' },
  { value: 'settings',  label: '설정'     },
]

const prodTypeOptions: FormOption[] = [
  { value: 'normal',  label: '일반 생산' },
  { value: 'rework',  label: '재작업'   },
  { value: 'sample',  label: '샘플'     },
]

// ── 핸들러 ──────────────────────────────────────────────────────────────
const handleSearch = () => {
  // 빈 catch 금지: 에러 발생 시 에러 메시지 표시
  console.log({
    searchName: searchName.value,
    searchGroup: searchGroup.value,
    fromDate: fromDate.value,
    toDate: toDate.value,
    quantity: quantity.value,
  })
}

const handleReset = () => {
  searchName.value       = ''
  searchGroup.value      = ''
  selectedStatuses.value = []
  selectedShifts.value   = []
  selectedMenus.value    = []
  prodType.value         = ''
  remark.value           = ''
  fromDate.value         = ''
  toDate.value           = ''
  quantity.value         = null
}
</script>

```

### 템플릿 적용 예시

```vue
<template>
  <div class="search-area">

    <!-- 텍스트박스: 이름 검색 -->
    <BaseTextBox v-model="searchName" label="이름" placeholder="이름 검색" />

    <!-- 셀렉트박스: 그룹 선택 -->
    <BaseSelectBox
      v-model="searchGroup"
      label="그룹"
      :options="groupOptions"
      :select-first-by-default="true"
    />

    <!-- 날짜 범위: 조회 기간 -->
    <BaseDatePicker v-model="fromDate" label="조회 기간" />
    <span>~</span>
    <BaseDatePicker v-model="toDate" :min="fromDate" />

    <!-- 숫자 입력: 수량 -->
    <BaseNumberInput v-model="quantity" label="수량" suffix="개" :min="0" :use-comma="true" />

    <!-- 체크박스 그룹: 상태 다중 선택 -->
    <div>
      <span class="label">상태</span>
      <BaseCheckBox v-model="selectedStatuses" value="active"    label="활성" />
      <BaseCheckBox v-model="selectedStatuses" value="inactive"  label="비활성" />
      <BaseCheckBox v-model="selectedStatuses" value="suspended" label="정지" />
    </div>

    <!-- 라디오 그룹: 생산 유형 단독 선택 -->
    <BaseRadioGroup
      v-model="prodType"
      label="생산 유형"
      direction="horizontal"
      :options="prodTypeOptions"
    />

    <!-- 토글 그룹: 시프트 선택 -->
    <BaseToggleGroup
      v-model="selectedShifts"
      label="시프트"
      :count="3"
      :is-multiple="true"
    />

    <!-- 콤보박스: 메뉴 다중 선택 -->
    <BaseComboBox
      v-model="selectedMenus"
      label="메뉴"
      :options="menuOptions"
      :is-multiple="true"
    />

    <!-- 텍스트에어리어: 비고 -->
    <BaseTextArea v-model="remark" label="비고" :rows="3" :max-length="200" :block="true" />

    <!-- 버튼 -->
    <BaseButton btn-type="search" @click="handleSearch" />
    <BaseButton btn-type="reset"  @click="handleReset" />

  </div>
</template>
```

---

## 14. 현대화 변경점 요약 (Vue 3.4+ / Vue 3.5+)

| 항목 | 구식 패턴 | 현대 패턴 | 적용 버전 |
|------|-----------|-----------|-----------|
| v-model 바인딩 | `modelValue` prop + `emit('update:modelValue')` | `defineModel()` | Vue 3.4+ |
| TypeScript Props | `defineProps({...})` with runtime validator | `withDefaults(defineProps<Props>(), {...})` | Vue 3.4+ |
| 고유 ID 생성 | 수동 카운터 (`let idCounter = 0`) | `useId()` | Vue 3.5+ |
| Template Ref | `ref<HTMLElement\|null>(null)` | `useTemplateRef<HTMLElement>('name')` | Vue 3.5+ |
| 외부 클릭 감지 | `addEventListener` / `removeEventListener` | `onClickOutside` (VueUse) | VueUse |
| Auto-imports | `import { ref, computed } from 'vue'` | import 없이 바로 사용 | Nuxt 3 |
| 체크박스 v-model | `modelValue: Boolean` + 그룹은 별도 컴포넌트 | `Boolean\|String[]` 유니온으로 단일/그룹 통합 | Vue 3.4+ |
| 날짜 형식 변환 | 템플릿 내 수동 replace 처리 | `computed(internalValue)` + `handleChange` 캡슐화 | Vue 3 |
| 숫자 포맷 표시 | `Intl.NumberFormat` 직접 호출 | `toLocaleString('ko-KR')` + `isFocused` 상태로 편집/표시 분리 | Vue 3 |
| CSS 색상 | 하드코딩 hex (`#667eea`) | CSS 변수 (`var(--color-primary)`) | 현대 CSS |
| 컴포넌트명 | (없음) | `defineOptions({ name: 'Base...' })` | Vue 3.3+ |

---

## 참고 자료

- [Vue.js 공식 스타일 가이드](https://vuejs.org/style-guide/)
- [Vue 3.4 defineModel](https://vuejs.org/guide/components/v-model)
- [Vue 3.5 useId](https://vuejs.org/api/composition-api-helpers#useid)
- [Vue 3.5 useTemplateRef](https://vuejs.org/api/composition-api-helpers#usetemplateref)
- [VueUse onClickOutside](https://vueuse.org/core/onClickOutside/)
- [Nuxt 3 컴포넌트 자동 임포트](https://nuxt.com/docs/guide/directory-structure/components)
