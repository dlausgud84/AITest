/** 메뉴 도메인 타입 (백엔드 Menu.java 기준) */
export interface MenuItem {
  menuId: string
  menuName: string
  menuGroup?: string
  menuType?: number       // 0: 폴더, 1: 메뉴, 2: 분리선
  pageId?: string
  iconImage?: string
  menuUrl?: string
  sortOrder?: number
  creatorId?: string
  createDttm?: string
  modifierId?: string
  modifyDttm?: string
}

interface MenuResponse<T = MenuItem | MenuItem[]> {
  success: boolean
  code: string
  message: string
  data: T
}

/**
 * 메뉴 API 통신을 담당하는 Composable
 * 백엔드의 /api/menus 엔드포인트와 통신합니다.
 * SSR 환경 호환을 위해 Nuxt의 $fetch를 사용합니다.
 */
export const useMenuAPI = () => {
  const menus = ref<MenuItem[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const config = useRuntimeConfig()
  const apiBaseUrl = config.public.apiBaseUrl

  /**
   * 모든 메뉴 조회
   */
  const fetchMenus = async () => {
    loading.value = true
    error.value = null

    try {
      const result = await $fetch<MenuResponse<MenuItem[]>>(`${apiBaseUrl}/api/menus`)
      if (result.success) {
        menus.value = result.data || []
      } else {
        error.value = result.message || '메뉴 조회 실패'
      }
    } catch (err: any) {
      error.value = err?.data?.message ?? err?.message ?? '네트워크 오류'
      console.error('메뉴 조회 실패:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * 메뉴 그룹별 조회
   */
  const fetchMenusByGroup = async (menuGroup: string): Promise<MenuItem[]> => {
    loading.value = true
    error.value = null

    try {
      const result = await $fetch<MenuResponse<MenuItem[]>>(`${apiBaseUrl}/api/menus/group/${menuGroup}`)
      if (result.success) {
        return result.data || []
      } else {
        error.value = result.message
        return []
      }
    } catch (err: any) {
      error.value = err?.data?.message ?? err?.message ?? '네트워크 오류'
      console.error('메뉴 그룹 조회 실패:', err)
      return []
    } finally {
      loading.value = false
    }
  }

  /**
   * 특정 메뉴 조회
   */
  const fetchMenuById = async (menuId: string): Promise<MenuItem | null> => {
    try {
      const result = await $fetch<MenuResponse<MenuItem>>(`${apiBaseUrl}/api/menus/${menuId}`)
      if (result.success) {
        return result.data
      } else {
        error.value = result.message
        return null
      }
    } catch (err: any) {
      error.value = err?.data?.message ?? err?.message ?? '네트워크 오류'
      return null
    }
  }

  /**
   * 메뉴 생성
   */
  const createMenu = async (menuData: Omit<MenuItem, 'menuId' | 'creatorId' | 'createDttm' | 'modifierId' | 'modifyDttm'>): Promise<boolean> => {
    loading.value = true
    error.value = null

    try {
      const result = await $fetch<MenuResponse>(`${apiBaseUrl}/api/menus`, {
        method: 'POST',
        body: menuData
      })

      if (!result.success) {
        error.value = result.message
        throw new Error(result.message)
      }

      // 메뉴 목록 새로고침
      await fetchMenus()
      return true
    } catch (err: any) {
      error.value = err?.data?.message ?? err?.message ?? '메뉴 생성 실패'
      console.error('메뉴 생성 실패:', err)
      return false
    } finally {
      loading.value = false
    }
  }

  /**
   * 메뉴 수정
   */
  const updateMenu = async (menuId: string, menuData: Partial<Omit<MenuItem, 'menuId' | 'creatorId' | 'createDttm' | 'modifierId' | 'modifyDttm'>>): Promise<boolean> => {
    loading.value = true
    error.value = null

    try {
      const result = await $fetch<MenuResponse>(`${apiBaseUrl}/api/menus/${menuId}`, {
        method: 'PUT',
        body: menuData
      })

      if (!result.success) {
        error.value = result.message
        throw new Error(result.message)
      }

      // 메뉴 목록 새로고침
      await fetchMenus()
      return true
    } catch (err: any) {
      error.value = err?.data?.message ?? err?.message ?? '메뉴 수정 실패'
      console.error('메뉴 수정 실패:', err)
      return false
    } finally {
      loading.value = false
    }
  }

  /**
   * 메뉴 삭제
   */
  const deleteMenu = async (menuId: string) => {
    loading.value = true
    error.value = null

    try {
      const result = await $fetch<MenuResponse>(`${apiBaseUrl}/api/menus/${menuId}`, {
        method: 'DELETE'
      })

      if (!result.success) {
        error.value = result.message
        throw new Error(result.message)
      }

      // 메뉴 목록 새로고침
      await fetchMenus()
      return true
    } catch (err: any) {
      error.value = err?.data?.message ?? err?.message ?? '메뉴 삭제 실패'
      console.error('메뉴 삭제 실패:', err)
      return false
    } finally {
      loading.value = false
    }
  }

  return {
    menus,
    loading,
    error,
    fetchMenus,
    fetchMenusByGroup,
    fetchMenuById,
    createMenu,
    updateMenu,
    deleteMenu
  }
}
