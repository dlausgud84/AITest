import { ref, computed, watch } from 'vue'

/**
 * 페이지네이션 логика를 제공하는 Composable
 * 대용량 데이터를 페이지 단위로 나누어 표시합니다.
 */
export const usePagination = (items: any[], initialPageSize: number = 10) => {
  const currentPage = ref(1)
  const pageSize = ref(initialPageSize)

  const totalPages = computed(() => {
    return Math.ceil(items.length / pageSize.value) || 1
  })

  const pagedItems = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    return items.slice(start, end)
  })

  const setPage = (page: number) => {
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page
    }
  }

  const nextPage = () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value++
    }
  }

  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value--
    }
  }

  // 아이템 수 변경 시 페이지 리셋
  watch(() => items.length, () => {
    currentPage.value = 1
  })

  return {
    currentPage,
    totalPages,
    pagedItems,
    pageSize,
    setPage,
    nextPage,
    prevPage
  }
}
