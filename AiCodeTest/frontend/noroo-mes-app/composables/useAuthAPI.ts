export interface Site {
  siteId: string
  siteName: string
}

export interface LoginRequest {
  userId: string
  password: string
}

export interface LoginResponse {
  userId: string
  userName: string
  siteId: string
  token: string
}

// 백엔드 ApiResponse<T> 공통 응답 구조
interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

export const useAuthAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const fetchSites = async (): Promise<Site[]> => {
    const response = await $fetch<ApiResponse<Site[]>>(`${baseUrl}/api/auth/sites`)
    if (!response.success) {
      throw new Error(response.message ?? '공장 목록 조회 실패')
    }
    return response.data ?? []
  }

  const login = async (request: LoginRequest): Promise<LoginResponse> => {
    const response = await $fetch<ApiResponse<LoginResponse>>(`${baseUrl}/api/auth/login`, {
      method: 'POST',
      body: request
    })
    if (!response.success) {
      throw new Error(response.message ?? '로그인 실패')
    }
    return response.data
  }

  return { fetchSites, login }
}
