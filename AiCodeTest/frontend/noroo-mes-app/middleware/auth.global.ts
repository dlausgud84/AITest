export default defineNuxtRouteMiddleware((to, from) => {
  // 로그인 페이지는 스킵
  if (to.path === '/login') {
    return
  }

  // 인증 토큰 확인
  const token = useCookie('authToken').value || localStorage.getItem('authToken')
  
  if (!token) {
    return navigateTo('/login')
  }
})
