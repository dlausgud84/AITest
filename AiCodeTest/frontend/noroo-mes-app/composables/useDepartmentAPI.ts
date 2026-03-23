/**
 * 부서 목록 API composable (NB_DEPARTMENTS - 팝업용)
 * SITE_ID 기준 필터, DELETE_FLAG = 'N'
 */

export interface DepartmentItem {
  departmentId: string
  departmentName: string
}

interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

export const useDepartmentAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const departmentList = ref<DepartmentItem[]>([])

  const fetchDepartmentList = async (siteId?: string | null) => {
    try {
      const query = siteId ? `?siteId=${encodeURIComponent(siteId)}` : ''
      const res = await $fetch<ApiResponse<DepartmentItem[]>>(`${baseUrl}/api/departments${query}`)
      if (res.success) {
        departmentList.value = res.data ?? []
      }
    } catch {
      // 조용히 실패 - 팝업 목록이 빈 상태로 유지됨
    }
  }

  return { departmentList, fetchDepartmentList }
}
