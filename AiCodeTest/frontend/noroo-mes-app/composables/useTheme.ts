/**
 * 테마 관리 Composable
 * - 'dark' (블랙) / 'light' (화이트) 두 가지 테마 지원
 * - 선택한 테마를 localStorage에 저장하여 새로고침 후에도 유지
 * - document.documentElement(html 태그)에 data-theme 속성을 적용
 *   → CSS에서 [data-theme="dark"], [data-theme="light"] 선택자로 색상 변수 분기
 *
 * [변경 이유]
 * - 이전: 모듈 레벨 ref → Nuxt 3 SSR에서 요청 간 상태 공유(오염) 위험
 * - 현재: useState('appTheme') → Nuxt 3 SSR-safe, hydration 보장, 컴포넌트 간 공유
 */

export type Theme = 'dark' | 'light'

const STORAGE_KEY = 'appTheme'

export const useTheme = () => {
  // Nuxt 3 useState: 동일한 key('appTheme')로 모든 컴포넌트에서 같은 state 공유
  // SSR 시 서버-클라이언트 hydration이 올바르게 동작함
  const theme = useState<Theme>('appTheme', () => 'dark')

  /**
   * 테마 적용 (state 업데이트 + html 속성 + localStorage 저장)
   */
  const setTheme = (t: Theme) => {
    theme.value = t
    // CSR(브라우저)에서만 DOM 조작 가능
    if (typeof document !== 'undefined') {
      document.documentElement.setAttribute('data-theme', t)
    }
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(STORAGE_KEY, t)
    }
  }

  /**
   * 저장된 테마를 읽어 초기화 (onMounted에서 1회 호출)
   * SSR에서는 window/localStorage가 없으므로 typeof 체크 필수
   */
  const initTheme = () => {
    if (typeof localStorage === 'undefined') return
    const saved = localStorage.getItem(STORAGE_KEY) as Theme | null
    setTheme(saved ?? 'dark')
  }

  /**
   * 블랙 ↔ 화이트 토글
   */
  const toggleTheme = () => {
    setTheme(theme.value === 'dark' ? 'light' : 'dark')
  }

  return {
    theme,        // 현재 테마 ('dark' | 'light')
    setTheme,     // 특정 테마로 설정
    initTheme,    // 저장된 테마로 초기화 (onMounted에서 호출)
    toggleTheme,  // 토글
  }
}
