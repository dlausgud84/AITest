// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: false },
  ssr: true,

  // 전역 CSS 등록 — 테마 CSS 변수(--page-bg, --card-bg 등)가 여기서 로드됨
  css: [
    '~/assets/css/main.css',    // 테마 CSS 변수 + 전역 리셋
    '~/assets/css/common.css',  // 그리드/테이블 공통 스타일
  ],

  modules: [
    // 필요한 모듈 추가
  ],

  runtimeConfig: {
    public: {
      apiBaseUrl: process.env.NUXT_PUBLIC_API_BASE_URL || 'http://localhost:8080'
    }
  },

  app: {
    head: {
      title: 'AiCodeTest - MES System',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: 'AiCodeTest Manufacturing Execution System' }
      ]
    }
  }
})
