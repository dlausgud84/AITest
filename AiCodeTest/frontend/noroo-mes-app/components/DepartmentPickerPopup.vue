<template>
  <!-- 오버레이 배경 -->
  <Teleport to="body">
    <div class="picker-overlay" @click.self="$emit('close')">
      <div class="picker-dialog" role="dialog" aria-modal="true" aria-label="부서 선택">

        <!-- 헤더 -->
        <div class="picker-header">
          <span class="picker-title">부서 선택</span>
          <button class="picker-close" @click="$emit('close')" title="닫기">
            <svg width="14" height="14" viewBox="0 0 16 16" fill="none">
              <path d="M4 4L12 12M12 4L4 12" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
            </svg>
          </button>
        </div>

        <!-- 사이트 ID 표시 (조회 기준) -->
        <div class="picker-context">
          <span class="context-label">사이트 ID</span>
          <span class="context-value">{{ siteId || '전체' }}</span>
        </div>

        <!-- 검색창 -->
        <div class="picker-search">
          <svg class="search-icon" width="13" height="13" viewBox="0 0 16 16" fill="none">
            <circle cx="6.5" cy="6.5" r="4" stroke="currentColor" stroke-width="1.5"/>
            <path d="M10 10L13.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
          <input
            v-model="keyword"
            class="search-input"
            placeholder="부서 ID 또는 부서명 검색..."
            autofocus
          />
        </div>

        <!-- 목록 -->
        <div class="picker-body">
          <div v-if="isLoading" class="picker-empty">목록을 불러오는 중...</div>
          <div v-else-if="filtered.length === 0" class="picker-empty">검색 결과가 없습니다.</div>
          <table v-else class="picker-table">
            <thead>
              <tr>
                <th>부서 ID</th>
                <th>부서명</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="item in filtered"
                :key="item.departmentId"
                class="picker-row"
                :class="{ 'picker-row--selected': selected === item.departmentId }"
                @click="select(item)"
                @dblclick="confirm(item)"
              >
                <td class="cell-id">{{ item.departmentId }}</td>
                <td>{{ item.departmentName }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 푸터 -->
        <div class="picker-footer">
          <button class="pk-btn pk-btn--ok" :disabled="!selected" @click="confirmSelected">확인</button>
          <button class="pk-btn pk-btn--cancel" @click="$emit('close')">취소</button>
        </div>

      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import type { DepartmentItem } from '~/composables/useDepartmentAPI'

const props = defineProps<{
  siteId?: string | null  // 조회 기준 사이트 ID (부서는 사이트 기준으로 필터됨)
}>()

const emit = defineEmits<{
  select: [departmentId: string, departmentName: string]
  close:  []
}>()

/* ── API ── */
const { departmentList, fetchDepartmentList } = useDepartmentAPI()
const isLoading = ref(true)

onMounted(async () => {
  await fetchDepartmentList(props.siteId)
  isLoading.value = false
})

/* ── 검색 필터 ── */
const keyword  = ref('')
const filtered = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return departmentList.value
  return departmentList.value.filter(
    item =>
      item.departmentId.toLowerCase().includes(kw) ||
      item.departmentName.toLowerCase().includes(kw)
  )
})

/* ── 선택 상태 ── */
const selected = ref('')

function select(item: DepartmentItem) {
  selected.value = item.departmentId
}

function confirm(item: DepartmentItem) {
  emit('select', item.departmentId, item.departmentName)
}

function confirmSelected() {
  const item = departmentList.value.find(i => i.departmentId === selected.value)
  if (item) emit('select', item.departmentId, item.departmentName)
}
</script>

<style scoped>
.picker-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.picker-dialog {
  background: var(--bg-secondary, #fff);
  border: 1px solid var(--border-color, #d1d5db);
  border-radius: 8px;
  width: 460px;
  max-height: 520px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.18);
  overflow: hidden;
}

.picker-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  background: var(--bg-primary, #f8f9fa);
  flex-shrink: 0;
}

.picker-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-primary, #111827);
}

.picker-close {
  padding: 4px;
  border: none;
  background: transparent;
  cursor: pointer;
  color: var(--text-muted, #6b7280);
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.picker-close:hover {
  background: var(--hover-bg, rgba(0, 0, 0, 0.06));
  color: var(--text-primary, #111827);
}

/* 사이트 ID 표시 컨텍스트 바 */
.picker-context {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 16px;
  background: var(--accent-light, rgba(59, 130, 246, 0.06));
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  flex-shrink: 0;
}

.context-label {
  font-size: 0.75rem;
  color: var(--text-muted, #6b7280);
  font-weight: 500;
}

.context-value {
  font-size: 0.78rem;
  color: var(--accent, #3b82f6);
  font-weight: 600;
}

.picker-search {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 12px;
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  flex-shrink: 0;
}

.search-icon {
  color: var(--text-muted, #9ca3af);
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: 0.82rem;
  color: var(--text-primary, #111827);
}

.picker-body {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}

.picker-empty {
  padding: 32px 0;
  text-align: center;
  color: var(--text-muted, #9ca3af);
  font-size: 0.82rem;
}

.picker-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.82rem;
}

.picker-table thead tr {
  position: sticky;
  top: 0;
  z-index: 1;
  background: var(--bg-primary, #f8f9fa);
}

.picker-table th {
  padding: 6px 12px;
  text-align: left;
  font-weight: 600;
  color: var(--text-secondary, #374151);
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  white-space: nowrap;
  font-size: 0.78rem;
}

.picker-row {
  cursor: pointer;
  transition: background 0.1s;
}

.picker-row:hover {
  background: var(--hover-bg, rgba(0, 0, 0, 0.04));
}

.picker-row--selected {
  background: var(--accent-light, rgba(59, 130, 246, 0.1));
}

.picker-row--selected td {
  color: var(--accent, #3b82f6);
  font-weight: 500;
}

.picker-table td {
  padding: 7px 12px;
  border-bottom: 1px solid var(--border-color-light, #f3f4f6);
  color: var(--text-primary, #111827);
}

.cell-id {
  font-weight: 600;
  color: var(--text-secondary, #374151);
  white-space: nowrap;
}

.picker-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding: 10px 14px;
  border-top: 1px solid var(--border-color, #e5e7eb);
  background: var(--bg-primary, #f8f9fa);
  flex-shrink: 0;
}

.pk-btn {
  padding: 5px 16px;
  font-size: 0.8rem;
  border-radius: 4px;
  border: 1px solid transparent;
  cursor: pointer;
  font-weight: 500;
  transition: opacity 0.15s;
}

.pk-btn:disabled {
  opacity: 0.45;
  cursor: not-allowed;
}

.pk-btn--ok {
  background: var(--accent, #3b82f6);
  color: #fff;
  border-color: var(--accent, #3b82f6);
}
.pk-btn--ok:hover:not(:disabled) {
  opacity: 0.85;
}

.pk-btn--cancel {
  background: var(--bg-secondary, #fff);
  color: var(--text-primary, #374151);
  border-color: var(--border-color, #d1d5db);
}
.pk-btn--cancel:hover {
  background: var(--hover-bg, rgba(0, 0, 0, 0.04));
}
</style>
