/**
 * 권한 API composable (NB_ROLES)
 */

export interface RoleItem {
  siteId:   string
  roleId:   string
  roleName: string
}

interface ApiResponse<T> {
  success: boolean
  code:    string
  message: string
  data:    T
}

export const useRoleAPI = () => {
  const config  = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const roleList  = ref<RoleItem[]>([])
  const isLoading = ref(false)
  const errorMsg  = ref('')

  /**
   * 사이트 권한 목록 조회
   * - SITE_ID 기준 필터, SYSTEM_ROLE_FLAG != 'Y'
   */
  const fetchRoleList = async (siteId?: string | null) => {
    isLoading.value = true
    errorMsg.value  = ''
    try {
      const query = new URLSearchParams()
      if (siteId) query.append('siteId', siteId)

      const res = await $fetch<ApiResponse<RoleItem[]>>(
        `${baseUrl}/api/roles?${query.toString()}`
      )
      roleList.value = res.success ? (res.data ?? []) : []
      if (!res.success) errorMsg.value = res.message
    } catch {
      errorMsg.value = '권한 목록 조회 중 오류가 발생했습니다.'
      roleList.value = []
    } finally {
      isLoading.value = false
    }
  }

  return { roleList, isLoading, errorMsg, fetchRoleList }
}
