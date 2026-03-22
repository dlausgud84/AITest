/**
 * 사이트 목록 API composable (NB_SITES - 콤보박스용)
 */

export interface SiteItem {
  siteId: string
  siteName: string
}

interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

export const useSiteAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const siteList = ref<SiteItem[]>([])

  const fetchSiteList = async () => {
    try {
      const res = await $fetch<ApiResponse<SiteItem[]>>(`${baseUrl}/api/sites`)
      if (res.success) {
        siteList.value = res.data ?? []
      }
    } catch {
      // 조용히 실패 - 콤보박스가 빈 상태로 유지됨
    }
  }

  return { siteList, fetchSiteList }
}
