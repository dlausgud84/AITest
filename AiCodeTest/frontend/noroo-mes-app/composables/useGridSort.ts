import { type Ref } from 'vue'

/**
 * 그리드 컬럼 정렬 기능을 제공하는 Composable
 * 문자열, 숫자, 날짜 자동 타입 인식 및 정렬
 */
export const useGridSort = (items: any[] | Ref<any[]>) => {
  const sortKey = ref<string | null>(null)
  const sortOrder = ref<'asc' | 'desc'>('asc')

  const sortedItems = computed(() => {
    const arr = unref(items)
    if (!sortKey.value) return arr

    const sorted = [...arr].sort((a, b) => {
      const valA = a[sortKey.value!]
      const valB = b[sortKey.value!]

      // null/undefined 값 처리
      if (valA == null && valB == null) return 0
      if (valA == null) return sortOrder.value === 'asc' ? 1 : -1
      if (valB == null) return sortOrder.value === 'asc' ? -1 : 1

      // 문자열 비교 (대소문자 구분 없음)
      if (typeof valA === 'string' && typeof valB === 'string') {
        const comparison = valA.toLowerCase().localeCompare(valB.toLowerCase())
        return sortOrder.value === 'asc' ? comparison : -comparison
      }

      // 숫자/날짜 비교
      if (valA > valB) return sortOrder.value === 'asc' ? 1 : -1
      if (valA < valB) return sortOrder.value === 'asc' ? -1 : 1
      return 0
    })

    return sorted
  })

  const toggleSort = (key: string) => {
    if (sortKey.value === key) {
      // 같은 컬럼을 다시 클릭하면 정렬 방향 변경
      sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
    } else {
      // 새로운 컬럼이면 오름차순으로 정렬
      sortKey.value = key
      sortOrder.value = 'asc'
    }
  }

  return {
    sortedItems,
    sortKey,
    sortOrder,
    toggleSort
  }
}
