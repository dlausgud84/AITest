export interface Site {
  siteId: string
  siteName: string
}

export interface LoginRequest {
  siteId: string
  userId: string
  password: string
}

export interface LoginResponse {
  userId: string
  userName: string
  siteId: string
  token: string
}

export const useAuthAPI = () => {
  const config = useRuntimeConfig()
  const baseUrl = config.public.apiBaseUrl

  const fetchSites = async (): Promise<Site[]> => {
    const response = await $fetch<{ data: Site[] }>(`${baseUrl}/api/auth/sites`)
    return response.data ?? []
  }

  const login = async (request: LoginRequest): Promise<LoginResponse> => {
    const response = await $fetch<{ data: LoginResponse }>(`${baseUrl}/api/auth/login`, {
      method: 'POST',
      body: request
    })
    return response.data
  }

  return { fetchSites, login }
}
