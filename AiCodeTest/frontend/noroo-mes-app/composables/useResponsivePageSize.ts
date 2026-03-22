
interface BreakPoint {
  maxWidth: number
  pageSize: number
}

/**
 * 화면 크기에 따라 자동으로 페이지 크기를 조정하는 Composable
 */
export const useResponsivePageSize = (breakPoints: BreakPoint[]) => {
  const pageSize = ref(10)
  let resizeTimeout: NodeJS.Timeout | null = null

  const calculatePageSize = () => {
    const width = window.innerWidth

    // 주어진 브레이크포인트에서 첫 번째 매칭 항목 찾기
    for (const bp of breakPoints) {
      if (width <= bp.maxWidth) {
        pageSize.value = bp.pageSize
        return
      }
    }

    // 아무것도 매칭되지 않으면 마지막 항목 사용
    if (breakPoints.length > 0) {
      pageSize.value = breakPoints[breakPoints.length - 1].pageSize
    }
  }

  const handleResize = () => {
    // 디바운스: 리사이즈 이벤트가 끝난 후 200ms 후에 실행
    if (resizeTimeout) {
      clearTimeout(resizeTimeout)
    }

    resizeTimeout = setTimeout(() => {
      calculatePageSize()
    }, 200)
  }

  onMounted(() => {
    calculatePageSize()
    window.addEventListener('resize', handleResize)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', handleResize)
    if (resizeTimeout) {
      clearTimeout(resizeTimeout)
    }
  })

  return {
    pageSize
  }
}
