# 공통 폼 컴포넌트 가이드

> Vue.js 공식 스타일 가이드 (https://vuejs.org/style-guide/) 기반으로 작성
> 버튼 컴포넌트는 `common-button-guide.md` 참조

---

## 1. 컴포넌트 목록

| 컴포넌트 | 파일명 | 핵심 옵션 |
|----------|--------|-----------|
| 텍스트박스 | `BaseTextBox.vue` | `allowSpecialChars` — 특수문자 입력 차단 |
| 토글 그룹 | `BaseToggleGroup.vue` | `count` — 토글 개수로 그룹 자동 생성 |
| 콤보박스 | `BaseComboBox.vue` | `isMultiple` — 다중 선택 |
| 셀렉트박스 | `BaseSelectBox.vue` | `isMultiple` + `selectFirstByDefault` — 다중 선택, 첫 번째 값 자동 선택 |

---

## 2. 파일 위치

```
components/
└── base/
    ├── BaseTextBox.vue         ← 텍스트박스
    ├── BaseToggleGroup.vue     ← 토글 그룹
    ├── BaseComboBox.vue        ← 콤보박스 (타이핑 검색 + 드롭다운)
    └── BaseSelectBox.vue       ← 셀렉트박스

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
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="isDisabled"
        :readonly="isReadonly"
        :maxlength="maxLength"
        :aria-label="label ?? placeholder"
        :aria-disabled="isDisabled"
        :class="inputClasses"
        v-bind="$attrs"
        @input="handleInput"
        @blur="emit('blur', $event)"
        @focus="emit('focus', $event)"
      />

      <!-- 글자 수 카운터 (maxLength 설정 시) -->
      <span v-if="maxLength" class="textbox__counter" aria-live="polite">
        {{ modelValue.length }} / {{ maxLength }}
      </span>
    </div>

    <!-- 특수문자 차단 안내 메시지 -->
    <p v-if="!allowSpecialChars && hasSpecialCharWarning" class="textbox__warning" role="alert">
      특수문자는 입력할 수 없습니다
    </p>

  </div>
</template>

<script setup lang="ts">
// 스타일 가이드: <script setup> 사용
import { ref, computed } from 'vue'
import type { FormSize } from '~/types/form'

// 특수문자 판별 정규식 — 영문·숫자·한글·공백 이외 모두 차단
const SPECIAL_CHAR_REGEX = /[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\s]/g

// 고유 ID 생성 (label의 for와 input의 id 연결)
let idCounter = 0
const inputId = `textbox-${++idCounter}`

// ─── Props ────────────────────────────────────────────────────────────────
// 스타일 가이드: Props는 타입과 validator를 상세하게 정의
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'text',
    validator: (v: string) => ['text', 'password', 'email', 'number'].includes(v)
  },
  placeholder: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    default: undefined
  },
  size: {
    type: String as () => FormSize,
    default: 'md',
    validator: (v: string) => ['sm', 'md', 'lg'].includes(v)
  },
  isDisabled: {
    type: Boolean,
    default: false
  },
  isReadonly: {
    type: Boolean,
    default: false
  },
  // false 시 특수문자 입력 차단
  allowSpecialChars: {
    type: Boolean,
    default: true
  },
  maxLength: {
    type: Number,
    default: undefined
  },
  block: {
    type: Boolean,
    default: false
  }
})

// ─── Emits ────────────────────────────────────────────────────────────────
// 스타일 가이드: Emit은 명시적으로 선언
const emit = defineEmits<{
  'update:modelValue': [value: string]
  'blur': [event: FocusEvent]
  'focus': [event: FocusEvent]
}>()

// ─── State ────────────────────────────────────────────────────────────────
// 특수문자 차단 경고 표시 여부
const hasSpecialCharWarning = ref(false)

// ─── Computed ─────────────────────────────────────────────────────────────
// 스타일 가이드: 복잡한 표현식은 computed로 분리

const wrapperClasses = computed(() => [
  'textbox',
  `textbox--${props.size}`,
  { 'textbox--block': props.block }
])

const inputClasses = computed(() => [
  'textbox__input',
  {
    'textbox__input--disabled': props.isDisabled,
    'textbox__input--readonly': props.isReadonly,
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

  emit('update:modelValue', value)
}
</script>

<style scoped>
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
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  color: #333;
  background: #fff;
  transition: border-color 0.2s, box-shadow 0.2s;
  outline: none;
  font-family: inherit;
}

.textbox__input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
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
  color: #ff4d4f;
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
// 스타일 가이드: <script setup> 사용
import { computed } from 'vue'
import type { FormOption, FormSize } from '~/types/form'

// ─── Props ────────────────────────────────────────────────────────────────
const props = defineProps({
  modelValue: {
    type: [String, Array] as unknown as () => string | string[],
    default: ''
  },
  // options 미설정 + count 설정 시 자동으로 1~N 토글 생성
  options: {
    type: Array as () => FormOption[],
    default: undefined
  },
  count: {
    type: Number,
    default: undefined
  },
  isMultiple: {
    type: Boolean,
    default: false
  },
  size: {
    type: String as () => FormSize,
    default: 'md',
    validator: (v: string) => ['sm', 'md', 'lg'].includes(v)
  },
  isDisabled: {
    type: Boolean,
    default: false
  },
  ariaLabel: {
    type: String,
    default: '토글 그룹'
  }
})

// ─── Emits ────────────────────────────────────────────────────────────────
const emit = defineEmits<{
  'update:modelValue': [value: string | string[]]
}>()

// ─── Computed ─────────────────────────────────────────────────────────────
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
    return Array.isArray(props.modelValue) && props.modelValue.includes(String(value))
  }
  return props.modelValue === String(value)
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
    const current = Array.isArray(props.modelValue) ? [...props.modelValue] : []
    const idx = current.indexOf(strValue)
    if (idx >= 0) {
      current.splice(idx, 1)  // 이미 선택됨 → 제거
    } else {
      current.push(strValue)  // 미선택 → 추가
    }
    emit('update:modelValue', current)
  } else {
    emit('update:modelValue', strValue)
  }
}
</script>

<style scoped>
/* ── 그룹 컨테이너 ── */
.toggle-group {
  display: inline-flex;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #d9d9d9;
}

/* ── 토글 버튼 공통 ── */
.toggle-group__btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-right: 1px solid #d9d9d9;
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
  background: #f0f3ff;
  color: #667eea;
}

.toggle-group__btn:focus-visible {
  box-shadow: inset 0 0 0 2px #667eea;
}

/* 선택된 상태 */
.toggle-group__btn--active {
  background: #667eea;
  color: #fff;
  font-weight: 600;
}

.toggle-group__btn--active:hover:not(:disabled) {
  background: #5568d3;
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
      <li v-if="isLoading" class="combo__option combo__option--loading">
        <span class="combo__spinner" />불러오는 중...
      </li>

      <!-- 검색 결과 없음 -->
      <li v-else-if="filteredOptions.length === 0" class="combo__option combo__option--empty">
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
          'combo__option--selected':  isOptionSelected(option.value),
          'combo__option--highlighted': index === highlightedIndex,
          'combo__option--disabled': option.isDisabled
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
// 스타일 가이드: <script setup> 사용
import { ref, computed, onMounted, onUnmounted } from 'vue'
import type { FormOption, FormSize } from '~/types/form'

// ─── Props ────────────────────────────────────────────────────────────────
const props = defineProps({
  modelValue: {
    type: [String, Array] as unknown as () => string | string[],
    default: ''
  },
  options: {
    type: Array as () => FormOption[],
    default: () => []
  },
  isMultiple: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: '검색...'
  },
  isDisabled: {
    type: Boolean,
    default: false
  },
  isLoading: {
    type: Boolean,
    default: false
  },
  noResultMessage: {
    type: String,
    default: '검색 결과 없음'
  },
  size: {
    type: String as () => FormSize,
    default: 'md',
    validator: (v: string) => ['sm', 'md', 'lg'].includes(v)
  }
})

// ─── Emits ────────────────────────────────────────────────────────────────
const emit = defineEmits<{
  'update:modelValue': [value: string | string[]]
}>()

// ─── Refs ─────────────────────────────────────────────────────────────────
const comboRef = ref<HTMLElement | null>(null)
const inputRef = ref<HTMLInputElement | null>(null)
const searchQuery = ref('')
const isOpen = ref(false)
const highlightedIndex = ref(-1)

// ─── Computed ─────────────────────────────────────────────────────────────

/** 검색어로 필터링된 옵션 목록 */
const filteredOptions = computed(() =>
  props.options.filter(opt =>
    opt.label.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
)

/** 다중 선택 chip 목록 */
const selectedChips = computed(() =>
  props.isMultiple && Array.isArray(props.modelValue) ? props.modelValue : []
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
    return Array.isArray(props.modelValue) && props.modelValue.includes(strVal)
  }
  return props.modelValue === strVal
}

const openDropdown = () => {
  if (props.isDisabled) return
  isOpen.value = true
  inputRef.value?.focus()
}

const closeDropdown = () => {
  isOpen.value = false
  searchQuery.value = ''
  highlightedIndex.value = -1
}

const selectOption = (option: FormOption) => {
  if (option.isDisabled) return
  const strVal = String(option.value)

  if (props.isMultiple) {
    const current = Array.isArray(props.modelValue) ? [...props.modelValue] : []
    const idx = current.indexOf(strVal)
    if (idx >= 0) {
      current.splice(idx, 1)
    } else {
      current.push(strVal)
    }
    emit('update:modelValue', current)
    searchQuery.value = ''
    inputRef.value?.focus()
  } else {
    emit('update:modelValue', strVal)
    closeDropdown()
  }
}

const removeChip = (value: string) => {
  if (!Array.isArray(props.modelValue)) return
  emit('update:modelValue', props.modelValue.filter(v => v !== value))
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

// 외부 클릭 시 드롭다운 닫기
const handleOutsideClick = (event: MouseEvent) => {
  if (comboRef.value && !comboRef.value.contains(event.target as Node)) {
    closeDropdown()
  }
}

onMounted(() => document.addEventListener('mousedown', handleOutsideClick))
onUnmounted(() => document.removeEventListener('mousedown', handleOutsideClick))
</script>

<style scoped>
/* ── Wrapper ── */
.combo { position: relative; display: inline-block; min-width: 200px; }

/* ── 컨트롤 영역 ── */
.combo__control {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  width: 100%;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background: #fff;
  cursor: text;
  transition: border-color 0.2s, box-shadow 0.2s;
  padding: 4px 36px 4px 8px;
  min-height: 40px;
  box-sizing: border-box;
}

.combo__control--focused { border-color: #667eea; box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15); }
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
  color: #667eea;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

.combo__chip-remove {
  border: none;
  background: none;
  color: #667eea;
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

.combo__option--selected { color: #667eea; font-weight: 500; }
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
  border-top-color: #667eea;
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
→ 마운트 시 emit('update:modelValue', options[0].value) 자동 발생
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
        'select__control--open': isOpen,
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
        <template v-if="isMultiple && Array.isArray(modelValue) && modelValue.length > 0">
          {{ selectedLabels.join(', ') }}
          <span class="select__badge">{{ modelValue.length }}</span>
        </template>
        <!-- 단일 선택: 선택된 레이블 표시 -->
        <template v-else-if="!isMultiple && modelValue">
          {{ getLabel(String(modelValue)) }}
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
// 스타일 가이드: <script setup> 사용
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import type { FormOption, FormSize } from '~/types/form'

// ─── Props ────────────────────────────────────────────────────────────────
const props = defineProps({
  modelValue: {
    type: [String, Array] as unknown as () => string | string[],
    default: ''
  },
  options: {
    type: Array as () => FormOption[],
    default: () => []
  },
  isMultiple: {
    type: Boolean,
    default: false
  },
  // true 시 마운트 시점에 options[0]을 자동 선택
  selectFirstByDefault: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: '선택하세요'
  },
  isDisabled: {
    type: Boolean,
    default: false
  },
  size: {
    type: String as () => FormSize,
    default: 'md',
    validator: (v: string) => ['sm', 'md', 'lg'].includes(v)
  }
})

// ─── Emits ────────────────────────────────────────────────────────────────
const emit = defineEmits<{
  'update:modelValue': [value: string | string[]]
}>()

// ─── Refs ─────────────────────────────────────────────────────────────────
const selectRef = ref<HTMLElement | null>(null)
const isOpen = ref(false)

// ─── Computed ─────────────────────────────────────────────────────────────

const wrapperClasses = computed(() => [
  'select',
  `select--${props.size}`,
  { 'select--disabled': props.isDisabled }
])

/** 다중 선택 시 선택된 레이블 배열 */
const selectedLabels = computed(() => {
  if (!Array.isArray(props.modelValue)) return []
  return props.modelValue.map(v => getLabel(v)).filter(Boolean)
})

// ─── Methods ──────────────────────────────────────────────────────────────

const getLabel = (value: string) =>
  props.options.find(o => String(o.value) === value)?.label ?? value

const isOptionSelected = (value: string | number): boolean => {
  const strVal = String(value)
  if (props.isMultiple) {
    return Array.isArray(props.modelValue) && props.modelValue.includes(strVal)
  }
  return props.modelValue === strVal
}

const toggleDropdown = () => {
  if (props.isDisabled) return
  isOpen.value = !isOpen.value
}

const closeDropdown = () => { isOpen.value = false }

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
    const current = Array.isArray(props.modelValue) ? [...props.modelValue] : []
    const idx = current.indexOf(strVal)
    if (idx >= 0) {
      current.splice(idx, 1)
    } else {
      current.push(strVal)
    }
    emit('update:modelValue', current)
  } else if (props.isMultiple && !isCtrl) {
    // 다중 선택 모드 + 일반 클릭: 해당 항목만 단독 선택
    emit('update:modelValue', [strVal])
    closeDropdown()
  } else {
    // 단일 선택
    emit('update:modelValue', strVal)
    closeDropdown()
  }
}

// 외부 클릭 시 닫기
const handleOutsideClick = (event: MouseEvent) => {
  if (selectRef.value && !selectRef.value.contains(event.target as Node)) {
    closeDropdown()
  }
}

// ─── Lifecycle ────────────────────────────────────────────────────────────

onMounted(() => {
  document.addEventListener('mousedown', handleOutsideClick)

  // selectFirstByDefault: 마운트 시 첫 번째 옵션 자동 선택
  if (props.selectFirstByDefault && props.options.length > 0) {
    const firstOption = props.options[0]
    if (props.isMultiple) {
      emit('update:modelValue', [String(firstOption.value)])
    } else {
      emit('update:modelValue', String(firstOption.value))
    }
  }
})

// options가 변경되고 selectFirstByDefault=true이면 재적용
watch(
  () => props.options,
  (newOptions) => {
    if (props.selectFirstByDefault && newOptions.length > 0) {
      const hasSelection = props.isMultiple
        ? Array.isArray(props.modelValue) && props.modelValue.length > 0
        : !!props.modelValue
      if (!hasSelection) {
        emit('update:modelValue',
          props.isMultiple
            ? [String(newOptions[0].value)]
            : String(newOptions[0].value)
        )
      }
    }
  }
)

onUnmounted(() => document.removeEventListener('mousedown', handleOutsideClick))
</script>

<style scoped>
/* ── Wrapper ── */
.select { position: relative; display: inline-block; min-width: 160px; }

/* ── 컨트롤 ── */
.select__control {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  border: 1px solid #d9d9d9;
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
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
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
  background: #667eea;
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
.select__control:hover .select__arrow { color: #667eea; }

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
.select__option--selected         { color: #667eea; font-weight: 500; background: #f0f3ff; }
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

## 8. 실제 MES 페이지 적용 예시 (검색 조건 영역)

```vue
<script setup lang="ts">
import { ref } from 'vue'
import type { FormOption } from '~/types/form'

// 검색 조건 상태
const searchName = ref('')
const searchGroup = ref('')
const selectedStatuses = ref<string[]>([])
const selectedShifts = ref<string[]>([])
const selectedMenus = ref<string[]>([])

// 옵션 목록
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

const handleSearch = () => {
  console.log({ searchName: searchName.value, searchGroup: searchGroup.value })
}
const handleReset = () => {
  searchName.value = ''
  searchGroup.value = ''
  selectedStatuses.value = []
  selectedShifts.value = []
  selectedMenus.value = []
}
</script>

<template>
  <div class="search-panel">

    <!-- 텍스트박스: 특수문자 차단 -->
    <BaseTextBox
      v-model="searchName"
      label="메뉴명"
      placeholder="메뉴명 검색 (특수문자 불가)"
      :allow-special-chars="false"
      :max-length="50"
      :block="true"
    />

    <!-- 셀렉트박스: 첫번째 값 자동 선택 -->
    <BaseSelectBox
      v-model="searchGroup"
      :options="groupOptions"
      :select-first-by-default="true"
    />

    <!-- 셀렉트박스: 다중 선택 -->
    <BaseSelectBox
      v-model="selectedStatuses"
      :options="statusOptions"
      :is-multiple="true"
      placeholder="상태 선택"
    />

    <!-- 콤보박스: 다중 선택 + 타이핑 검색 -->
    <BaseComboBox
      v-model="selectedMenus"
      :options="menuOptions"
      :is-multiple="true"
      placeholder="메뉴 검색 및 다중 선택..."
    />

    <!-- 토글 그룹: count로 개수 지정 (예: 라인 번호 선택) -->
    <div class="form-row">
      <span class="form-label">라인 선택</span>
      <BaseToggleGroup
        v-model="selectedShifts"
        :count="5"
        :is-multiple="true"
        aria-label="생산 라인 선택"
      />
    </div>

    <!-- 토글 그룹: options로 항목 직접 지정 -->
    <div class="form-row">
      <span class="form-label">근무 시프트</span>
      <BaseToggleGroup
        v-model="selectedShift"
        :options="[
          { value: 'day',   label: '주간' },
          { value: 'night', label: '야간' },
          { value: 'all',   label: '전체' }
        ]"
      />
    </div>

    <!-- 버튼 (BaseButton 참조) -->
    <div class="form-actions">
      <BaseButton btn-type="search" @click="handleSearch" />
      <BaseButton btn-type="reset"  @click="handleReset" />
    </div>

  </div>
</template>
```

---

## 9. 스타일 가이드 적용 체크리스트

### A 우선순위 (필수)

- [x] 모든 컴포넌트 이름이 PascalCase (`BaseTextBox`, `BaseToggleGroup`, `BaseComboBox`, `BaseSelectBox`)
- [x] 모든 컴포넌트 이름이 두 단어 이상 (`Base` + 고유명)
- [x] Props 타입과 기본값 상세 정의 (4개 컴포넌트 공통)
- [x] `v-for`에 `:key` 필수 사용 (옵션 목록, chip 등)
- [x] `v-if`와 `v-for` 동시 사용 금지
- [x] Scoped 스타일 사용

### B 우선순위 (강력 권장)

- [x] 공통 컴포넌트에 `Base` 접두사
- [x] Props를 camelCase로 정의 (`allowSpecialChars`, `selectFirstByDefault`, `isMultiple`)
- [x] Boolean props에 `is` / `has` / `allow` 접두사 사용
- [x] `<script setup>` Composition API 사용 (4개 컴포넌트 공통)
- [x] Emit 이벤트 명시적 선언 (`defineEmits`)
- [x] 복잡한 표현식을 computed로 분리 (`filteredOptions`, `selectedLabels`, `resolvedOptions` 등)
- [x] 메서드명 동사로 시작 (`handleInput`, `handleSelect`, `handleToggle`)

### C 우선순위 (권장)

- [x] Props validator로 유효한 값 제한 (`size`, `type`)
- [x] `aria-label`, `aria-expanded`, `aria-haspopup`, `aria-selected`, `aria-pressed` 접근성 처리
- [x] `role="listbox"`, `role="option"`, `role="group"` 시맨틱 마크업
- [x] 키보드 네비게이션 지원 (Enter, Space, Escape, ArrowUp/Down)
- [x] `onUnmounted`에서 이벤트 리스너 정리 (메모리 누수 방지)

---

## 10. 파일 구조

```
components/
└── base/
    ├── BaseButton.vue          ← 공통 버튼       (common-button-guide.md)
    ├── BaseGrid.vue            ← 공통 그리드      (common-grid-guide.md)
    ├── BaseTextBox.vue         ← 텍스트박스       (이 문서)
    ├── BaseToggleGroup.vue     ← 토글 그룹        (이 문서)
    ├── BaseComboBox.vue        ← 콤보박스         (이 문서)
    └── BaseSelectBox.vue       ← 셀렉트박스       (이 문서)

types/
├── grid.ts                     ← 그리드 타입
└── form.ts                     ← 폼 공통 타입 (FormOption, FormSize)

docs/
├── common-button-guide.md
├── common-grid-guide.md
└── common-form-components-guide.md   ← 이 문서
```

---

## 참고 자료

- [Vue.js 공식 스타일 가이드](https://vuejs.org/style-guide/)
- [Vue 3 Composition API](https://vuejs.org/guide/extras/composition-api-faq)
- [Vue 3 v-model 공식 문서](https://vuejs.org/guide/components/v-model)
- [WAI-ARIA Listbox Pattern](https://www.w3.org/WAI/ARIA/apg/patterns/listbox/)
