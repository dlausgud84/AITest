/**
 * 사용자 권한 API composable (NB_USER_ROLES)
 */

export interface UserRoleItem {
  userId:      string
  userName:    string   // NB_USERS 조인 결과 (사용자명)
  roleId:      string
  roleName:    string   // NB_ROLES 조인 결과 (권한명)
  description: string
}

interface ApiResponse<T> {
  success: boolean
  code:    string
  message: string
  data:    T
}

export const useUserRoleAPI = () => {
  const config  = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const userRoleList = ref<UserRoleItem[]>([])
  const isLoading    = ref(false)
  const errorMsg     = ref('')

  /**
   * 사용자 권한 목록 조회
   * - SITE_ID + USER_ID 기준 필터
   */
  const fetchUserRoleList = async (siteId?: string | null, userId?: string | null) => {
    isLoading.value = true
    errorMsg.value  = ''
    try {
      const query = new URLSearchParams()
      if (siteId) query.append('siteId', siteId)
      if (userId) query.append('userId', userId)

      const res = await $fetch<ApiResponse<UserRoleItem[]>>(
        `${baseUrl}/api/user-roles?${query.toString()}`
      )
      userRoleList.value = res.success ? (res.data ?? []) : []
      if (!res.success) errorMsg.value = res.message
    } catch {
      errorMsg.value = '사용자 권한 목록 조회 중 오류가 발생했습니다.'
      userRoleList.value = []
    } finally {
      isLoading.value = false
    }
  }

  return { userRoleList, isLoading, errorMsg, fetchUserRoleList }
}
