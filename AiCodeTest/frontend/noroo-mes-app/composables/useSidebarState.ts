/**
 * 사이드바 접기/펼치기 전역 상태 관리
 * Nuxt useState 사용으로 SSR 안전 (hydration mismatch 방지)
 */
export const useSidebarState = () => {
  const isCollapsed = useState<boolean>('sidebar-collapsed', () => false)

  const toggle   = () => { isCollapsed.value = !isCollapsed.value }
  const collapse = () => { isCollapsed.value = true }
  const expand   = () => { isCollapsed.value = false }

  return { isCollapsed, toggle, collapse, expand }
}
