/**
 * 세션 타임아웃 플러그인 (클라이언트 전용)
 * useSessionTimeout composable에 세션 타임아웃 로직을 위임합니다.
 */
export default defineNuxtPlugin(() => {
  const { startWatching } = useSessionTimeout()

  if (process.client) {
    startWatching()
  }
})
