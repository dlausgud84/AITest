export default defineNuxtPlugin(() => {
  // 세션 타임아웃 로직 (예: 30분)
  const SESSION_TIMEOUT = 30 * 60 * 1000 // 30분
  let timeoutId: NodeJS.Timeout | null = null

  const resetTimeout = () => {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }

    timeoutId = setTimeout(() => {
      // 토큰 삭제 및 로그인 페이지로 이동
      localStorage.removeItem('authToken')
      localStorage.removeItem('username')
      navigateTo('/login')
    }, SESSION_TIMEOUT)
  }

  // 사용자 활동 감지
  if (process.client) {
    resetTimeout()
    
    window.addEventListener('mousedown', resetTimeout)
    window.addEventListener('keydown', resetTimeout)
    window.addEventListener('wheel', resetTimeout)
  }
})
