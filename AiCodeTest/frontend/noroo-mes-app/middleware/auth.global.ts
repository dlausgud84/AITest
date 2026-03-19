export default defineNuxtRouteMiddleware((to, from) => {
  // 로그인 페이지는 스킵
  if (to.path === '/login') {
    return
  }

  // 인증 토큰 확인 (SSR 환경에서는 localStorage 사용 불가 → import.meta.client로 구분)
  const token = useCookie('authToken').value || (import.meta.client ? localStorage.getItem('authToken') : null)
  
  if (!token) {
    return navigateTo('/login')
  }
})
