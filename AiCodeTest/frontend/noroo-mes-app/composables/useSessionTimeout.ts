/**
 * 세션 타임아웃 관리 Composable
 * plugins/sessionTimeout.client.ts에서 사용합니다.
 */
export const useSessionTimeout = () => {
  const SESSION_TIMEOUT = 30 * 60 * 1000 // 30분
  let timeoutId: ReturnType<typeof setTimeout> | null = null

  /**
   * 타이머 초기화 (사용자 활동 감지 시 호출)
   */
  const resetTimeout = () => {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }

    timeoutId = setTimeout(() => {
      expireSession()
    }, SESSION_TIMEOUT)
  }

  /**
   * 세션 만료 처리
   */
  const expireSession = () => {
    localStorage.removeItem('authToken')
    localStorage.removeItem('username')
    navigateTo('/login')
  }

  /**
   * 사용자 활동 이벤트 리스너 등록
   */
  const startWatching = () => {
    resetTimeout()
    window.addEventListener('mousedown', resetTimeout)
    window.addEventListener('keydown', resetTimeout)
    window.addEventListener('wheel', resetTimeout)
  }

  /**
   * 이벤트 리스너 해제
   */
  const stopWatching = () => {
    if (timeoutId) {
      clearTimeout(timeoutId)
      timeoutId = null
    }
    window.removeEventListener('mousedown', resetTimeout)
    window.removeEventListener('keydown', resetTimeout)
    window.removeEventListener('wheel', resetTimeout)
  }

  return {
    startWatching,
    stopWatching,
    resetTimeout,
  }
}
