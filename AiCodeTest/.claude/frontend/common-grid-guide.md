# 공통 그리드 컴포넌트 가이드

> Vue.js 공식 스타일 가이드 (https://vuejs.org/style-guide/) 기반으로 작성

---

## 1. 개요 및 핵심 옵션

### 세 가지 핵심 옵션

```
┌──────────────────────────────────────────────────────────────────┐
│  옵션 1: maxRows                                                  │
│                                                                  │
│  maxRows 설정 시   → 지정한 행 수 기준 동작 (hasPagination에 따라 분기)│
│  maxRows 미설정 시  → 무한 스크롤 (IntersectionObserver)           │
├──────────────────────────────────────────────────────────────────┤
│  옵션 2: hasPagination  (maxRows 설정 시에만 유효)                │
│                                                                  │
│  hasPagination=true  → maxRows = 페이지당 행 수 + 하단 페이지 UI  │
│  hasPagination=false → maxRows = 컨테이너 고정 높이 + 내부 스크롤  │
├──────────────────────────────────────────────────────────────────┤
│  옵션 3: isResizable                                             │
│                                                                  │
│  isResizable=true  → 컬럼 헤더 드래그로 너비 조절 가능            │
│  isResizable=false → 컬럼 너비 자동 계산 (기본값)                 │
└──────────────────────────────────────────────────────────────────┘
```

### 전체 동작 흐름

```
데이터 바인딩 (:rows, :columns)
        │
        ▼
┌────────────────────┐
│   maxRows 설정?    │
└────────────────────┘
    YES │           │ NO
        ▼           ▼
┌──────────────┐  무한 스크롤
│ hasPagination│  (IntersectionObserver)
│   설정?      │
└──────────────┘
  YES │     │ NO
      ▼     ▼
 페이지  고정 높이
 네이션  + 내부스크롤
 UI 표시

─────────────────────────────
모든 모드에서 공통 적용:

isResizable=true  → 드래그 핸들 + 너비 조절
isResizable=false → table-layout: auto (자동 너비)
```

---

## 2. maxRows + hasPagination 조합 비교

| maxRows | hasPagination | 동작 | 스크롤 | 페이지 UI |
|---------|---------------|------|--------|-----------|
| 미설정 | — | 무한 스크롤 | 페이지 스크롤 | 없음 |
| 설정 | `false` (기본) | 고정 높이 + 내부 스크롤 | 그리드 내부 | 없음 |
| 설정 | `true` | 페이지네이션 | 없음 | 하단 표시 |

```
────────────────────────────────────────────────────────
케이스 A: maxRows=10, hasPagination=false (내부 스크롤)
────────────────────────────────────────────────────────
┌────────────────────────────┐  ← maxHeight = 48×10+52px
│  헤더 (sticky)             │
├────────────────────────────┤
│  row 1                     │
│  ...                       │  ← 10행만 보임
│  row 10  ─────────────────────── 스크롤 경계
│  row 11  ← 내부 스크롤    │
│  ...                       │
└────────────────────────────┘

────────────────────────────────────────────────────────
케이스 B: maxRows=10, hasPagination=true (페이지네이션)
────────────────────────────────────────────────────────
┌────────────────────────────┐
│  헤더                      │
├────────────────────────────┤
│  row 1                     │
│  ...                       │  ← 현재 페이지 10행만 렌더링
│  row 10                    │
├────────────────────────────┤
│  총 150건  [이전] 1 2 3 4 5 [다음]  │  ← 페이지 UI
└────────────────────────────┘

────────────────────────────────────────────────────────
케이스 C: maxRows 미설정 (무한 스크롤)
────────────────────────────────────────────────────────
┌────────────────────────────┐
│  헤더 (sticky)             │
├────────────────────────────┤
│  row 1 ~ 30 (초기 로드)    │
│  [sentinel] ← 뷰포트 진입 시 load-more 발생
│  row 31 ~ 60 (추가 로드)   │
│  ...                       │
└────────────────────────────┘
```

---

## 3. 컴포넌트 파일 위치 및 네이밍

### 스타일 가이드 규칙

| 규칙 | 우선순위 | 내용 |
|------|----------|------|
| **싱글 파일 컴포넌트 파일명은 PascalCase** | A (필수) | `BaseGrid.vue` ✅ |
| **베이스/공통 컴포넌트는 접두사 사용** | B (강력 권장) | `Base` 접두사 |
| **단어 두 개 이상으로 구성** | A (필수) | `Grid.vue` ❌ → `BaseGrid.vue` ✅ |

### 파일 위치

```
components/
└── base/
    └── BaseGrid.vue            ← 공통 그리드 컴포넌트

composables/
├── useColumnResize.ts          ← 컬럼 크기 조절 로직
├── useInfiniteScroll.ts        ← 무한 스크롤 로직
└── useGridPagination.ts        ← 그리드 내장 페이지네이션 로직
```

> Nuxt 3에서는 자동으로 `<BaseGrid>` 로 등록됩니다.

---

## 4. 타입 정의

```typescript
// types/grid.ts

/** 컬럼 정의 */
export interface GridColumn {
  key: string                          // 데이터 필드명 (row 객체의 key)
  label: string                        // 헤더에 표시할 텍스트
  width?: number                       // 고정 너비(px) — 미설정 시 자동
  minWidth?: number                    // 최소 너비(px), 기본값: 60
  align?: 'left' | 'center' | 'right' // 셀 텍스트 정렬, 기본값: 'left'
  sortable?: boolean                   // 정렬 가능 여부, 기본값: false
  resizable?: boolean                  // 컬럼별 크기 조절 재정의 (전역 isResizable override)
}

/** 정렬 상태 */
export interface GridSort {
  key: string
  order: 'asc' | 'desc'
}

/** 페이지 변경 이벤트 페이로드 */
export interface GridPageChange {
  page: number
  pageSize: number
}
```

---

## 5. Props 설계

### 스타일 가이드 규칙

| 규칙 | 우선순위 | 내용 |
|------|----------|------|
| **Props는 항상 상세하게 정의** | A (필수) | 타입, 기본값, validator 명시 |
| **Props 이름은 camelCase** | B (강력 권장) | `maxRows`, `hasPagination` |
| **Boolean props는 접두사 규칙** | B (강력 권장) | `is`, `has`, `can` 접두사 |

### Props 목록

| Prop | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `columns` | `GridColumn[]` | `[]` | 컬럼 정의 배열 **(필수)** |
| `rows` | `object[]` | `[]` | 행 데이터 배열 **(필수)** |
| `rowKey` | `String` | `'id'` | 행 고유 식별 필드명 (`:key` 용) |
| `maxRows` | `Number` | `undefined` | 최대 표시 행 수 — **미설정 시 무한 스크롤** |
| `hasPagination` | `Boolean` | `false` | **maxRows 설정 시** 페이지네이션 UI 표시 여부 |
| `totalCount` | `Number` | `undefined` | **서버사이드 페이지네이션 전용** 전체 데이터 건수 |
| `rowHeight` | `Number` | `48` | 행 높이(px) — maxRows 내부 스크롤 모드에서 컨테이너 높이 계산 |
| `isResizable` | `Boolean` | `false` | 컬럼 너비 드래그 조절 — **false 시 자동 너비** |
| `isLoading` | `Boolean` | `false` | 로딩 상태 (스켈레톤 UI 표시) |
| `hasMoreData` | `Boolean` | `false` | 무한 스크롤 모드에서 추가 데이터 존재 여부 |
| `emptyMessage` | `String` | `'데이터가 없습니다'` | 빈 데이터 메시지 |
| `selectedRowKey` | `String\|Number` | `undefined` | 선택된 행의 key 값 |
| `currentSort` | `GridSort\|null` | `null` | 현재 정렬 상태 |

### Emits 목록

| 이벤트 | 페이로드 | 발생 시점 |
|--------|----------|-----------|
| `row-click` | `row: object` | 행 클릭 시 |
| `sort-change` | `sort: GridSort` | 헤더 클릭으로 정렬 변경 시 |
| `load-more` | — | 무한 스크롤 — 하단 sentinel 진입 시 |
| `page-change` | `{ page, pageSize }` | 페이지 변경 시 (서버사이드 연동용) |
| `column-resize` | `{ key, width }` | 컬럼 너비 변경 완료 시 |

---

## 6. Composable 코드

### `composables/useGridPagination.ts`

```typescript
// 스타일 가이드: 재사용 가능한 로직은 Composable로 분리
import { ref, computed, watch } from 'vue'

export const useGridPagination = (
  totalItems: () => number,  // 전체 건수를 반환하는 getter
  pageSize: number
) => {
  const currentPage = ref(1)

  /** 전체 페이지 수 */
  const totalPages = computed(() =>
    Math.max(1, Math.ceil(totalItems() / pageSize))
  )

  /**
   * 페이지 번호 목록 (최대 5개 표시)
   * 예: 현재 3페이지, 전체 10페이지 → [1, 2, 3, 4, 5]
   *     현재 8페이지, 전체 10페이지 → [6, 7, 8, 9, 10]
   */
  const pageNumbers = computed(() => {
    const total = totalPages.value
    const current = currentPage.value
    const maxVisible = 5

    let start = Math.max(1, current - Math.floor(maxVisible / 2))
    let end = start + maxVisible - 1

    if (end > total) {
      end = total
      start = Math.max(1, end - maxVisible + 1)
    }

    return Array.from({ length: end - start + 1 }, (_, i) => start + i)
  })

  /** 클라이언트사이드: rows 배열에서 현재 페이지 슬라이싱 */
  const getPagedRows = <T>(rows: T[]): T[] => {
    const start = (currentPage.value - 1) * pageSize
    return rows.slice(start, start + pageSize)
  }

  const goToPage = (page: number) => {
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page
    }
  }

  const prevPage = () => goToPage(currentPage.value - 1)
  const nextPage = () => goToPage(currentPage.value + 1)

  // 전체 건수 변경 시 1페이지로 리셋
  watch(totalItems, () => {
    currentPage.value = 1
  })

  return {
    currentPage,
    totalPages,
    pageNumbers,
    getPagedRows,
    goToPage,
    prevPage,
    nextPage
  }
}
```

### `composables/useColumnResize.ts`

```typescript
// 스타일 가이드: 재사용 가능한 로직은 Composable로 분리
import { ref, reactive } from 'vue'
import type { GridColumn } from '~/types/grid'

export const useColumnResize = (columns: GridColumn[]) => {
  const columnWidths = reactive<Record<string, number>>(
    Object.fromEntries(columns.map(col => [col.key, col.width ?? 0]))
  )

  const isDragging = ref(false)
  const draggingKey = ref<string | null>(null)

  let startX = 0
  let startWidth = 0

  const startResize = (event: MouseEvent, colKey: string) => {
    event.preventDefault()
    isDragging.value = true
    draggingKey.value = colKey
    startX = event.clientX
    startWidth = columnWidths[colKey] || 100

    document.addEventListener('mousemove', onMouseMove)
    document.addEventListener('mouseup', stopResize)
  }

  const onMouseMove = (event: MouseEvent) => {
    if (!isDragging.value || !draggingKey.value) return
    const diff = event.clientX - startX
    const col = columns.find(c => c.key === draggingKey.value)
    const minWidth = col?.minWidth ?? 60
    columnWidths[draggingKey.value] = Math.max(minWidth, startWidth + diff)
  }

  const stopResize = () => {
    isDragging.value = false
    draggingKey.value = null
    document.removeEventListener('mousemove', onMouseMove)
    document.removeEventListener('mouseup', stopResize)
  }

  return { columnWidths, isDragging, draggingKey, startResize }
}
```

### `composables/useInfiniteScroll.ts`

```typescript
// 스타일 가이드: 재사용 가능한 로직은 Composable로 분리
import { ref, onMounted, onUnmounted } from 'vue'

export const useInfiniteScroll = (
  onLoadMore: () => void,
  options = { threshold: 0.1 }
) => {
  const sentinelRef = ref<HTMLElement | null>(null)
  let observer: IntersectionObserver | null = null

  onMounted(() => {
    if (!sentinelRef.value) return
    observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) onLoadMore()
    }, options)
    observer.observe(sentinelRef.value)
  })

  onUnmounted(() => observer?.disconnect())

  return { sentinelRef }
}
```

---

## 7. 컴포넌트 코드

### `components/base/BaseGrid.vue`

```vue
<template>
  <!-- 스타일 가이드: 최상위 요소는 단순하게 -->
  <div :class="gridWrapperClasses">

    <!-- ── 스켈레톤 로딩 ── -->
    <div v-if="isLoading && rows.length === 0" class="grid__loading" role="status">
      <div v-for="n in (maxRows ?? 5)" :key="n" class="grid__skeleton-row">
        <div v-for="col in columns" :key="col.key" class="grid__skeleton-cell" />
      </div>
    </div>

    <!-- ── 메인 테이블 영역 ── -->
    <div v-else class="grid__scroll-container" :style="scrollContainerStyle">
      <table :class="tableClasses">

        <colgroup v-if="hasFixedWidths">
          <col
            v-for="col in columns"
            :key="col.key"
            :style="getColStyle(col)"
          />
        </colgroup>

        <!-- ── 헤더 ── -->
        <thead class="grid__thead">
          <tr>
            <th
              v-for="col in columns"
              :key="col.key"
              :class="getHeaderClasses(col)"
              :style="getHeaderStyle(col)"
              :aria-sort="getAriaSort(col)"
              @click="handleHeaderClick(col)"
            >
              <span class="grid__header-label">{{ col.label }}</span>

              <span v-if="col.sortable" class="grid__sort-icon" aria-hidden="true">
                <svg v-if="currentSort?.key === col.key" width="10" height="10" viewBox="0 0 10 10">
                  <path v-if="currentSort.order === 'asc'" d="M5 2L9 8H1L5 2Z" fill="currentColor"/>
                  <path v-else d="M5 8L1 2H9L5 8Z" fill="currentColor"/>
                </svg>
                <svg v-else width="10" height="10" viewBox="0 0 10 10" opacity="0.35">
                  <path d="M5 1L8 4H2L5 1Z" fill="currentColor"/>
                  <path d="M5 9L2 6H8L5 9Z" fill="currentColor"/>
                </svg>
              </span>

              <span
                v-if="isColumnResizable(col)"
                class="grid__resize-handle"
                :class="{ 'grid__resize-handle--active': draggingKey === col.key }"
                aria-hidden="true"
                @mousedown.stop="startResize($event, col.key)"
              />
            </th>
          </tr>
        </thead>

        <!-- ── 바디 ── -->
        <!-- 스타일 가이드: v-for에 :key 필수 -->
        <tbody class="grid__tbody">
          <tr
            v-for="row in displayedRows"
            :key="row[rowKey]"
            :class="getRowClasses(row)"
            tabindex="0"
            @click="handleRowClick(row)"
            @keydown.enter="handleRowClick(row)"
          >
            <td v-for="col in columns" :key="col.key" :class="getCellClasses(col)">
              <!-- 스타일 가이드: 슬롯으로 커스텀 렌더링 지원 -->
              <slot :name="`cell-${col.key}`" :row="row" :value="row[col.key]">
                {{ row[col.key] ?? '-' }}
              </slot>
            </td>
          </tr>

          <tr v-if="displayedRows.length === 0 && !isLoading">
            <td :colspan="columns.length" class="grid__empty">
              <slot name="empty"><span>{{ emptyMessage }}</span></slot>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- ── 무한 스크롤 sentinel (maxRows 미설정 시) ── -->
      <div v-if="!maxRows" ref="sentinelRef" class="grid__sentinel" aria-hidden="true">
        <div v-if="isLoading && rows.length > 0" class="grid__load-more-spinner" />
        <span v-else-if="!hasMoreData && rows.length > 0" class="grid__end-message">
          마지막 데이터입니다
        </span>
      </div>
    </div>

    <!-- ── 페이지네이션 UI (maxRows + hasPagination=true 시) ── -->
    <div v-if="maxRows && hasPagination" class="grid__pagination" role="navigation" aria-label="페이지 네비게이션">

      <!-- 전체 건수 정보 -->
      <span class="grid__pagination-info">
        총 <strong>{{ resolvedTotalCount.toLocaleString() }}</strong>건
        ({{ currentPage }} / {{ totalPages }} 페이지)
      </span>

      <!-- 페이지 버튼 그룹 -->
      <div class="grid__pagination-buttons">

        <!-- 처음 페이지 -->
        <button
          class="grid__page-btn grid__page-btn--nav"
          :disabled="currentPage <= 1"
          aria-label="첫 페이지"
          @click="goToPage(1)"
        >
          «
        </button>

        <!-- 이전 페이지 -->
        <button
          class="grid__page-btn grid__page-btn--nav"
          :disabled="currentPage <= 1"
          aria-label="이전 페이지"
          @click="prevPage"
        >
          ‹
        </button>

        <!-- 페이지 번호 목록 -->
        <button
          v-for="page in pageNumbers"
          :key="page"
          class="grid__page-btn"
          :class="{ 'grid__page-btn--active': page === currentPage }"
          :aria-current="page === currentPage ? 'page' : undefined"
          @click="handlePageClick(page)"
        >
          {{ page }}
        </button>

        <!-- 다음 페이지 -->
        <button
          class="grid__page-btn grid__page-btn--nav"
          :disabled="currentPage >= totalPages"
          aria-label="다음 페이지"
          @click="nextPage"
        >
          ›
        </button>

        <!-- 마지막 페이지 -->
        <button
          class="grid__page-btn grid__page-btn--nav"
          :disabled="currentPage >= totalPages"
          aria-label="마지막 페이지"
          @click="goToPage(totalPages)"
        >
          »
        </button>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
// 스타일 가이드: <script setup> 사용 (Vue 3 Composition API 권장 방식)
import { computed } from 'vue'
import { useColumnResize } from '~/composables/useColumnResize'
import { useInfiniteScroll } from '~/composables/useInfiniteScroll'
import { useGridPagination } from '~/composables/useGridPagination'
import type { GridColumn, GridSort, GridPageChange } from '~/types/grid'

// ─── Props ────────────────────────────────────────────────────────────────
// 스타일 가이드: Props는 타입과 validator를 상세하게 정의
const props = defineProps({
  columns: {
    type: Array as () => GridColumn[],
    default: () => []
  },
  rows: {
    type: Array as () => Record<string, any>[],
    default: () => []
  },
  rowKey: {
    type: String,
    default: 'id'
  },
  maxRows: {
    type: Number,
    default: undefined
  },
  // hasPagination: maxRows 설정 시에만 유효
  // true  → maxRows = 페이지당 행 수, 하단 페이지네이션 UI 표시
  // false → maxRows = 컨테이너 최대 높이 (내부 스크롤)
  hasPagination: {
    type: Boolean,
    default: false
  },
  // 서버사이드 페이지네이션 전용: 전체 데이터 건수
  // 미설정 시 rows.length를 전체 건수로 사용 (클라이언트사이드)
  totalCount: {
    type: Number,
    default: undefined
  },
  rowHeight: {
    type: Number,
    default: 48
  },
  isResizable: {
    type: Boolean,
    default: false
  },
  isLoading: {
    type: Boolean,
    default: false
  },
  hasMoreData: {
    type: Boolean,
    default: false
  },
  emptyMessage: {
    type: String,
    default: '데이터가 없습니다'
  },
  selectedRowKey: {
    type: [String, Number],
    default: undefined
  },
  currentSort: {
    type: Object as () => GridSort | null,
    default: null
  }
})

// ─── Emits ───────────────────────────────────────────────────────────────
// 스타일 가이드: Emit은 명시적으로 선언
const emit = defineEmits<{
  'row-click': [row: Record<string, any>]
  'sort-change': [sort: GridSort]
  'load-more': []
  'page-change': [payload: GridPageChange]
  'column-resize': [payload: { key: string; width: number }]
}>()

// ─── Composables ──────────────────────────────────────────────────────────
const { columnWidths, draggingKey, startResize } = useColumnResize(props.columns)

// 무한 스크롤: maxRows 미설정 시에만 동작
const { sentinelRef } = useInfiniteScroll(() => {
  if (!props.maxRows && props.hasMoreData && !props.isLoading) {
    emit('load-more')
  }
})

// 페이지네이션: maxRows + hasPagination=true 시에만 동작
const {
  currentPage,
  totalPages,
  pageNumbers,
  getPagedRows,
  goToPage,
  prevPage,
  nextPage
} = useGridPagination(
  // 전체 건수 getter: totalCount(서버사이드) 또는 rows.length(클라이언트사이드)
  () => props.totalCount ?? props.rows.length,
  props.maxRows ?? 10
)

// ─── Computed ─────────────────────────────────────────────────────────────
// 스타일 가이드: 복잡한 표현식은 computed로 분리

/** 실제로 테이블에 렌더링할 행 목록 */
const displayedRows = computed(() => {
  // 페이지네이션 모드 + 클라이언트사이드: 내부에서 슬라이싱
  if (props.maxRows && props.hasPagination && !props.totalCount) {
    return getPagedRows(props.rows)
  }
  // 그 외(무한스크롤, 내부스크롤, 서버사이드): rows를 그대로 사용
  return props.rows
})

/** 총 건수: 서버사이드(totalCount prop) 또는 클라이언트사이드(rows.length) */
const resolvedTotalCount = computed(() =>
  props.totalCount ?? props.rows.length
)

/** 그리드 wrapper 클래스 */
const gridWrapperClasses = computed(() => [
  'grid',
  {
    'grid--resizable': props.isResizable,
    'grid--paginated': props.maxRows && props.hasPagination,
    'grid--fixed-rows': props.maxRows && !props.hasPagination,
    'grid--infinite': !props.maxRows,
    'grid--loading': props.isLoading
  }
])

/** 테이블 클래스 */
const tableClasses = computed(() => [
  'grid__table',
  { 'grid__table--auto': !props.isResizable }
])

/**
 * 스크롤 컨테이너 높이 결정
 *  - maxRows + hasPagination=false: 고정 높이 + 내부 스크롤
 *  - maxRows + hasPagination=true : 높이 제한 없음 (페이지 단위로 교체)
 *  - maxRows 미설정              : 높이 제한 없음 (무한 스크롤)
 */
const scrollContainerStyle = computed(() => {
  if (props.maxRows && !props.hasPagination) {
    const height = props.rowHeight * props.maxRows + 52 // 52 = 헤더 높이
    return { maxHeight: `${height}px`, overflowY: 'auto' as const }
  }
  return {}
})

const hasFixedWidths = computed(() =>
  props.isResizable || props.columns.some(col => col.width)
)

// ─── 스타일/클래스 헬퍼 ───────────────────────────────────────────────────

const getColStyle = (col: GridColumn) => {
  if (props.isResizable) {
    const w = columnWidths[col.key]
    return w ? { width: `${w}px` } : {}
  }
  return col.width ? { width: `${col.width}px` } : {}
}

const getHeaderClasses = (col: GridColumn) => [
  'grid__th',
  `grid__th--${col.align ?? 'left'}`,
  { 'grid__th--sortable': col.sortable }
]

const getHeaderStyle = (col: GridColumn) => ({
  minWidth: `${col.minWidth ?? 60}px`
})

const getCellClasses = (col: GridColumn) => [
  'grid__td',
  `grid__td--${col.align ?? 'left'}`
]

const getRowClasses = (row: Record<string, any>) => [
  'grid__tr',
  {
    'grid__tr--selected':
      props.selectedRowKey !== undefined &&
      row[props.rowKey] === props.selectedRowKey
  }
]

const getAriaSort = (col: GridColumn): 'ascending' | 'descending' | 'none' | undefined => {
  if (!col.sortable) return undefined
  if (props.currentSort?.key !== col.key) return 'none'
  return props.currentSort.order === 'asc' ? 'ascending' : 'descending'
}

const isColumnResizable = (col: GridColumn): boolean => {
  if (col.resizable !== undefined) return col.resizable
  return props.isResizable
}

// ─── 이벤트 핸들러 ────────────────────────────────────────────────────────
// 스타일 가이드: 메서드명은 동사로 시작

const handleRowClick = (row: Record<string, any>) => {
  emit('row-click', row)
}

const handleHeaderClick = (col: GridColumn) => {
  if (!col.sortable) return
  const newOrder: 'asc' | 'desc' =
    props.currentSort?.key === col.key && props.currentSort.order === 'asc'
      ? 'desc'
      : 'asc'
  emit('sort-change', { key: col.key, order: newOrder })
}

/**
 * 페이지 클릭 처리
 * - 클라이언트사이드: 내부 상태만 변경 (displayedRows computed가 자동 갱신)
 * - 서버사이드: page-change 이벤트 발생 → 부모가 새 rows 로드
 */
const handlePageClick = (page: number) => {
  goToPage(page)
  // totalCount prop이 있으면 서버사이드 → 부모에게 알림
  if (props.totalCount !== undefined) {
    emit('page-change', { page, pageSize: props.maxRows ?? 10 })
  }
}
</script>

<style scoped>
/* 스타일 가이드: scoped 스타일로 컴포넌트 격리 */

/* ── Wrapper ── */
.grid {
  position: relative;
  width: 100%;
  background: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  overflow: hidden;
}

/* ── 스크롤 컨테이너 ── */
.grid__scroll-container {
  width: 100%;
  overflow-x: auto;
}

/* ── 테이블 ── */
.grid__table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 14px;
}

.grid__table--auto { table-layout: auto; }

/* ── 헤더 ── */
.grid__thead {
  position: sticky;
  top: 0;
  z-index: 10;
  background: #fafafa;
}

.grid__th {
  padding: 12px 16px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 2px solid #e8e8e8;
  white-space: nowrap;
  position: relative;
  user-select: none;
}

.grid__th--center { text-align: center; }
.grid__th--right  { text-align: right;  }

.grid__th--sortable { cursor: pointer; }
.grid__th--sortable:hover { background: #f0f0f0; }

.grid__header-label {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.grid__sort-icon {
  display: inline-flex;
  align-items: center;
  margin-left: 4px;
  color: #667eea;
  vertical-align: middle;
}

/* ── 리사이즈 핸들 ── */
.grid__resize-handle {
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 6px;
  cursor: col-resize;
  background: transparent;
  transition: background 0.15s;
}

.grid__resize-handle:hover,
.grid__resize-handle--active {
  background: rgba(102, 126, 234, 0.45);
}

/* ── 바디 ── */
.grid__tbody .grid__tr {
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.15s;
  cursor: pointer;
}

.grid__tbody .grid__tr:hover        { background: #f5f7ff; }
.grid__tbody .grid__tr--selected    { background: #eff2ff; }
.grid__tbody .grid__tr:focus        { outline: 2px solid #667eea; outline-offset: -2px; }

/* ── 셀 ── */
.grid__td {
  padding: 12px 16px;
  color: #555;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.grid__td--center { text-align: center; }
.grid__td--right  { text-align: right;  }

/* ── 빈 데이터 ── */
.grid__empty {
  padding: 48px 16px;
  text-align: center;
  color: #aaa;
  font-size: 14px;
}

/* ── 무한 스크롤 sentinel ── */
.grid__sentinel {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px;
  min-height: 1px;
}

.grid__end-message { font-size: 12px; color: #bbb; }

/* ── 로딩 스피너 ── */
.grid__load-more-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #e8e8e8;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: grid-spin 0.7s linear infinite;
}

@keyframes grid-spin { to { transform: rotate(360deg); } }

/* ── 스켈레톤 ── */
.grid__loading { padding: 8px; }

.grid__skeleton-row {
  display: flex;
  gap: 12px;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.grid__skeleton-cell {
  flex: 1;
  height: 16px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 4px;
  animation: grid-shimmer 1.4s infinite;
}

@keyframes grid-shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* ── 페이지네이션 ── */
.grid__pagination {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-top: 1px solid #e8e8e8;
  background: #fafafa;
  gap: 12px;
  flex-wrap: wrap;
}

.grid__pagination-info {
  font-size: 13px;
  color: #666;
  white-space: nowrap;
}

.grid__pagination-info strong {
  color: #333;
  font-weight: 600;
}

.grid__pagination-buttons {
  display: flex;
  gap: 4px;
  align-items: center;
}

/* 공통 페이지 버튼 */
.grid__page-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 32px;
  height: 32px;
  padding: 0 8px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background: #fff;
  color: #555;
  font-size: 13px;
  cursor: pointer;
  transition: background 0.15s, border-color 0.15s, color 0.15s;
  user-select: none;
}

.grid__page-btn:hover:not(:disabled) {
  background: #f0f3ff;
  border-color: #667eea;
  color: #667eea;
}

/* 현재 페이지 */
.grid__page-btn--active {
  background: #667eea;
  border-color: #667eea;
  color: #fff;
  font-weight: 600;
  cursor: default;
}

.grid__page-btn--active:hover {
  background: #667eea;
  color: #fff;
}

/* 이전/다음/처음/마지막 네비게이션 버튼 */
.grid__page-btn--nav {
  font-size: 16px;
  font-weight: bold;
  color: #888;
}

.grid__page-btn:disabled {
  opacity: 0.38;
  cursor: not-allowed;
  pointer-events: none;
}
</style>
```

---

## 8. 페이지네이션 동작 상세

### 8-1. 클라이언트사이드 페이지네이션

**사용 조건**: `totalCount` prop 미설정

- `:rows`에 **전체 데이터**를 한 번에 전달
- 컴포넌트 내부 `getPagedRows()`가 현재 페이지에 해당하는 슬라이스를 계산
- API 추가 호출 없음

```
rows = [row1, row2, ..., row150]  ← 전체 150건 전달
maxRows = 10, hasPagination = true

1페이지: rows.slice(0, 10)   → row1  ~ row10
2페이지: rows.slice(10, 20)  → row11 ~ row20
...
15페이지: rows.slice(140, 150) → row141 ~ row150
```

```vue
<BaseGrid
  :columns="columns"
  :rows="allRows"          ← 전체 데이터 전달
  :max-rows="10"
  :has-pagination="true"
  row-key="menuId"
/>
```

### 8-2. 서버사이드 페이지네이션

**사용 조건**: `totalCount` prop 설정

- `:rows`에 **현재 페이지 데이터만** 전달 (서버에서 페이지 단위로 받아옴)
- `:total-count`에 **전체 건수** 전달 (페이지 수 계산용)
- 페이지 클릭 시 `@page-change` 이벤트 발생 → 부모에서 새 데이터 로드

```
서버 API: GET /api/menus?page=1&size=10
  → { data: [row1~row10], totalCount: 150 }

1페이지 클릭 → emit('page-change', { page: 1, pageSize: 10 })
  → 부모: fetchMenus({ page: 1, size: 10 })
  → rows = [row1~row10], totalCount = 150

2페이지 클릭 → emit('page-change', { page: 2, pageSize: 10 })
  → 부모: fetchMenus({ page: 2, size: 10 })
  → rows = [row11~row20], totalCount = 150
```

```vue
<script setup lang="ts">
const rows = ref([])
const totalCount = ref(0)
const isLoading = ref(false)

const loadPage = async ({ page, pageSize }: { page: number; pageSize: number }) => {
  isLoading.value = true
  const result = await fetchMenus({ page, size: pageSize })
  rows.value = result.data
  totalCount.value = result.totalCount
  isLoading.value = false
}

onMounted(() => loadPage({ page: 1, pageSize: 10 }))
</script>

<template>
  <BaseGrid
    :columns="columns"
    :rows="rows"              ← 현재 페이지 데이터만
    :total-count="totalCount" ← 전체 건수 (서버사이드 신호)
    :max-rows="10"
    :has-pagination="true"
    :is-loading="isLoading"
    row-key="menuId"
    @page-change="loadPage"
  />
</template>
```

### 8-3. 페이지 번호 표시 로직

최대 5개 페이지 번호를 현재 페이지 중심으로 표시합니다.

```
전체 10페이지, 현재 1페이지  → [1] 2  3  4  5
전체 10페이지, 현재 3페이지  →  1  2 [3] 4  5
전체 10페이지, 현재 6페이지  →  4  5 [6] 7  8
전체 10페이지, 현재 9페이지  →  6  7  8 [9] 10
전체 10페이지, 현재 10페이지 →  6  7  8  9 [10]
```

---

## 9. 세 가지 모드 사용 예시 비교

```vue
<!-- ────────────────────────────────────────────
  모드 A: maxRows + hasPagination (페이지네이션)
  ──────────────────────────────────────────── -->

<!-- A-1. 클라이언트사이드 (전체 데이터 전달) -->
<BaseGrid
  :columns="columns"
  :rows="allRows"
  :max-rows="10"
  :has-pagination="true"
/>

<!-- A-2. 서버사이드 (페이지별 데이터 + 전체 건수) -->
<BaseGrid
  :columns="columns"
  :rows="pageRows"
  :total-count="totalCount"
  :max-rows="10"
  :has-pagination="true"
  :is-loading="isLoading"
  @page-change="loadPage"
/>

<!-- ────────────────────────────────────────────
  모드 B: maxRows (내부 스크롤, hasPagination 미설정)
  ──────────────────────────────────────────── -->
<BaseGrid
  :columns="columns"
  :rows="allRows"
  :max-rows="10"
/>

<!-- ────────────────────────────────────────────
  모드 C: maxRows 미설정 (무한 스크롤)
  ──────────────────────────────────────────── -->
<BaseGrid
  :columns="columns"
  :rows="rows"
  :has-more-data="hasMoreData"
  :is-loading="isLoading"
  @load-more="loadMore"
/>
```

---

## 10. 컬럼 정의 예시

```typescript
const columns: GridColumn[] = [
  { key: 'menuId',    label: '메뉴 ID',   width: 120, minWidth: 80,  sortable: true },
  { key: 'menuName',  label: '메뉴명',    minWidth: 150,             sortable: true },
  { key: 'menuGroup', label: '메뉴 그룹', width: 120, align: 'center', sortable: true },
  { key: 'sortOrder', label: '순서',      width: 80,  align: 'center', sortable: true },
  { key: 'action',    label: '작업',      width: 160, align: 'center', resizable: false }
]
```

---

## 11. 슬롯 활용 — 커스텀 셀 렌더링

```vue
<BaseGrid :columns="columns" :rows="rows" :max-rows="10" :has-pagination="true">

  <template #cell-menuGroup="{ value }">
    <span :class="`badge badge--${value?.toLowerCase()}`">{{ value ?? '-' }}</span>
  </template>

  <template #cell-action="{ row }">
    <BaseButton btn-type="save"   size="sm" @click.stop="editRow(row)" />
    <BaseButton btn-type="delete" size="sm" @click.stop="deleteRow(row)" />
  </template>

  <template #empty>
    <p>메뉴가 없습니다</p>
    <BaseButton btn-type="custom" custom-label="메뉴 추가" @click="openModal" />
  </template>

</BaseGrid>
```

---

## 12. 실제 MES 페이지 적용 예시

```vue
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useMenuAPI } from '~/composables/useMenuAPI'
import type { GridColumn, GridSort, GridPageChange } from '~/types/grid'

const { menus, loading, fetchMenus } = useMenuAPI()
const searchQuery = ref('')

const columns: GridColumn[] = [
  { key: 'menuId',    label: '메뉴 ID',   width: 120, sortable: true },
  { key: 'menuName',  label: '메뉴명',    minWidth: 150, sortable: true },
  { key: 'menuGroup', label: '메뉴 그룹', width: 120, align: 'center', sortable: true },
  { key: 'sortOrder', label: '순서',      width: 80,  align: 'center', sortable: true },
  { key: 'action',    label: '작업',      width: 160, align: 'center', resizable: false }
]

const currentSort = ref<GridSort | null>(null)

const filteredRows = computed(() => {
  let result = menus.value
  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase()
    result = result.filter(m =>
      m.menuName?.toLowerCase().includes(q) ||
      m.menuId?.toLowerCase().includes(q)
    )
  }
  if (currentSort.value) {
    const { key, order } = currentSort.value
    result = [...result].sort((a, b) => {
      const v1 = a[key] ?? ''
      const v2 = b[key] ?? ''
      return order === 'asc'
        ? String(v1).localeCompare(String(v2))
        : String(v2).localeCompare(String(v1))
    })
  }
  return result
})

onMounted(fetchMenus)
</script>

<template>
  <div class="menu-management">
    <PageHeader title="메뉴 관리" />

    <div class="toolbar">
      <input v-model="searchQuery" placeholder="메뉴명 또는 ID 검색..." />
      <BaseButton btn-type="search" @click="fetchMenus" />
      <BaseButton btn-type="reset"  @click="searchQuery = ''" />
    </div>

    <!--
      클라이언트사이드 페이지네이션 (전체 데이터 전달, 내부에서 페이지 처리)
    -->
    <BaseGrid
      :columns="columns"
      :rows="filteredRows"
      row-key="menuId"
      :max-rows="10"
      :has-pagination="true"
      :is-resizable="true"
      :is-loading="loading"
      :current-sort="currentSort"
      empty-message="메뉴가 없습니다"
      @sort-change="sort => currentSort = sort"
      @row-click="editMenu"
    >
      <template #cell-action="{ row }">
        <BaseButton btn-type="save"   size="sm" @click.stop="editMenu(row)" />
        <BaseButton btn-type="delete" size="sm" @click.stop="confirmDelete(row.menuId)" />
      </template>
    </BaseGrid>
  </div>
</template>
```

---

## 13. 스타일 가이드 적용 체크리스트

### A 우선순위 (필수)

- [x] 컴포넌트 이름이 PascalCase (`BaseGrid`)
- [x] 컴포넌트 이름이 두 단어 이상 (`Base` + `Grid`)
- [x] Props 타입과 기본값 상세 정의
- [x] `v-for`에 `:key` 필수 사용 (`row[rowKey]`, `col.key`, `page`)
- [x] `v-if`와 `v-for` 동시 사용 금지 (분리 처리)
- [x] Scoped 스타일 사용

### B 우선순위 (강력 권장)

- [x] 공통 컴포넌트에 `Base` 접두사 사용
- [x] Props를 camelCase로 정의 (`maxRows`, `hasPagination`, `totalCount`)
- [x] Boolean props에 `is` / `has` 접두사 사용 (`isResizable`, `isLoading`, `hasPagination`, `hasMoreData`)
- [x] `<script setup>` Composition API 사용
- [x] Emit 이벤트 명시적 선언 (`defineEmits`)
- [x] 복잡한 표현식을 computed로 분리 (`displayedRows`, `resolvedTotalCount`, `scrollContainerStyle` 등)
- [x] 메서드명 동사로 시작 (`handleRowClick`, `handleHeaderClick`, `handlePageClick`)
- [x] 재사용 로직을 Composable로 분리 (`useColumnResize`, `useInfiniteScroll`, `useGridPagination`)

### C 우선순위 (권장)

- [x] `aria-sort`로 정렬 상태 접근성 처리
- [x] `role="navigation"` + `aria-label`로 페이지네이션 접근성 처리
- [x] `aria-current="page"`로 현재 페이지 접근성 처리
- [x] `tabindex="0"` + `keydown.enter`로 키보드 행 선택 지원
- [x] 슬롯으로 커스텀 렌더링 확장성 확보
- [x] `onUnmounted`에서 IntersectionObserver 정리 (메모리 누수 방지)

---

## 14. 파일 구조

```
components/
└── base/
    ├── BaseButton.vue          ← 공통 버튼 (common-button-guide.md 참조)
    └── BaseGrid.vue            ← 공통 그리드 (이 문서)

composables/
├── useColumnResize.ts          ← 컬럼 크기 조절 로직
├── useInfiniteScroll.ts        ← 무한 스크롤 로직
├── useGridPagination.ts        ← 그리드 내장 페이지네이션 로직
├── useGridSort.ts              ← 기존 정렬 로직 (재사용)
└── usePagination.ts            ← 기존 페이지네이션 로직 (재사용)

types/
└── grid.ts                     ← GridColumn, GridSort, GridPageChange 타입 정의

docs/
├── common-button-guide.md
└── common-grid-guide.md        ← 이 문서
```

---

## 참고 자료

- [Vue.js 공식 스타일 가이드](https://vuejs.org/style-guide/)
- [Vue 3 Composition API](https://vuejs.org/guide/extras/composition-api-faq)
- [IntersectionObserver MDN](https://developer.mozilla.org/ko/docs/Web/API/Intersection_Observer_API)
- [Nuxt 3 컴포넌트 자동 임포트](https://nuxt.com/docs/guide/directory-structure/components)
