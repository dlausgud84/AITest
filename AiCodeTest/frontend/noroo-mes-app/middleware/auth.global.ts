export default defineNuxtRouteMiddleware((to) => {
  // 로그인 페이지는 인증 검사 스킵
  if (to.path === '/login') {
    return
  }

  /**
   * [🔒보안] 인증 토큰은 반드시 httpOnly 쿠키(useCookie)에서만 확인
   * - localStorage 폴백 제거: XSS 공격으로 토큰 탈취 가능하므로 절대 금지
   * - useCookie('authToken')은 SSR/CSR 모두 안전하게 동작
   */
  const token = useCookie('authToken').value

  if (!token) {
    return navigateTo('/login')
  }
})
