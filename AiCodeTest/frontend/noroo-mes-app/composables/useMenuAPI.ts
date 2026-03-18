import { ref, computed } from 'vue'

interface MenuResponse {
  success: boolean
  code: string
  message: string
  data: any
}

/**
 * 메뉴 API 통신을 담당하는 Composable
 * 백엔드의 /api/menus 엔드포인트와 통신합니다.
 */
export const useMenuAPI = () => {
  const menus = ref<any[]>([])
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
      const response = await fetch(`${apiBaseUrl}/api/menus`)
      const result: MenuResponse = await response.json()

      if (result.success) {
        menus.value = result.data || []
      } else {
        error.value = result.message || '메뉴 조회 실패'
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : '네트워크 오류'
      console.error('메뉴 조회 실패:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * 메뉴 그룹별 조회
   */
  const fetchMenusByGroup = async (menuGroup: string) => {
    loading.value = true
    error.value = null

    try {
      const response = await fetch(`${apiBaseUrl}/api/menus/group/${menuGroup}`)
      const result: MenuResponse = await response.json()

      if (result.success) {
        return result.data || []
      } else {
        error.value = result.message
        return []
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : '네트워크 오류'
      console.error('메뉴 그룹 조회 실패:', err)
      return []
    } finally {
      loading.value = false
    }
  }

  /**
   * 특정 메뉴 조회
   */
  const fetchMenuById = async (menuId: string) => {
    try {
      const response = await fetch(`${apiBaseUrl}/api/menus/${menuId}`)
      const result: MenuResponse = await response.json()

      if (result.success) {
        return result.data
      } else {
        error.value = result.message
        return null
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : '네트워크 오류'
      return null
    }
  }

  /**
   * 메뉴 생성
   */
  const createMenu = async (menuData: any) => {
    loading.value = true
    error.value = null

    try {
      const response = await fetch(`${apiBaseUrl}/api/menus`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(menuData)
      })

      const result: MenuResponse = await response.json()

      if (!result.success) {
        error.value = result.message
        throw new Error(result.message)
      }

      // 메뉴 목록 새로고침
      await fetchMenus()
      return true
    } catch (err) {
      error.value = err instanceof Error ? err.message : '메뉴 생성 실패'
      console.error('메뉴 생성 실패:', err)
      return false
    } finally {
      loading.value = false
    }
  }

  /**
   * 메뉴 수정
   */
  const updateMenu = async (menuId: string, menuData: any) => {
    loading.value = true
    error.value = null

    try {
      const response = await fetch(`${apiBaseUrl}/api/menus/${menuId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(menuData)
      })

      const result: MenuResponse = await response.json()

      if (!result.success) {
        error.value = result.message
        throw new Error(result.message)
      }

      // 메뉴 목록 새로고침
      await fetchMenus()
      return true
    } catch (err) {
      error.value = err instanceof Error ? err.message : '메뉴 수정 실패'
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
      const response = await fetch(`${apiBaseUrl}/api/menus/${menuId}`, {
        method: 'DELETE'
      })

      const result: MenuResponse = await response.json()

      if (!result.success) {
        error.value = result.message
        throw new Error(result.message)
      }

      // 메뉴 목록 새로고침
      await fetchMenus()
      return true
    } catch (err) {
      error.value = err instanceof Error ? err.message : '메뉴 삭제 실패'
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
