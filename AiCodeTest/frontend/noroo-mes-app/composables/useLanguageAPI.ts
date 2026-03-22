/**
 * 언어 목록 API composable (NB_LANGUAGES - 팝업용)
 * IS_ENABLE_FLAG = 'Y' 인 항목만 반환
 */

export interface LanguageItem {
  languageId: string
  languageName: string
}

interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

export const useLanguageAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const languageList = ref<LanguageItem[]>([])

  const fetchLanguageList = async () => {
    try {
      const res = await $fetch<ApiResponse<LanguageItem[]>>(`${baseUrl}/api/languages`)
      if (res.success) {
        languageList.value = res.data ?? []
      }
    } catch {
      // 조용히 실패 - 팝업 목록이 빈 상태로 유지됨
    }
  }

  return { languageList, fetchLanguageList }
}
