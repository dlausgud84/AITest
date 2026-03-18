<template>
  <div class="pagination">
    <button 
      @click="previousPage" 
      :disabled="currentPage <= 1"
      class="btn-pagination"
    >
      이전
    </button>
    <span class="page-info">
      {{ currentPage }} / {{ totalPages }}
    </span>
    <button 
      @click="nextPage" 
      :disabled="currentPage >= totalPages"
      class="btn-pagination"
    >
      다음
    </button>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  total: {
    type: Number,
    default: 0
  },
  pageSize: {
    type: Number,
    default: 10
  },
  currentPage: {
    type: Number,
    default: 1
  }
})

const emit = defineEmits(['change-page'])

const totalPages = computed(() => Math.ceil(props.total / props.pageSize) || 1)

const previousPage = () => {
  if (props.currentPage > 1) {
    emit('change-page', props.currentPage - 1)
  }
}

const nextPage = () => {
  if (props.currentPage < totalPages.value) {
    emit('change-page', props.currentPage + 1)
  }
}
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.btn-pagination {
  padding: 8px 16px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-pagination:hover:not(:disabled) {
  background: #5568d3;
}

.btn-pagination:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.page-info {
  color: #666;
  font-size: 14px;
}
</style>
