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
    // [🔒보안] NUXT_PUBLIC_ 접두사 변수는 브라우저에 노출됨 — 비밀 정보 절대 포함 금지
    public: {
      apiBaseUrl: process.env.NUXT_PUBLIC_API_BASE_URL || 'http://localhost:8080'
    }
  },

  // 페이지별 렌더링 전략 설정 (성능 + 보안 최적화)
  routeRules: {
    '/login':       { prerender: true },     // 로그인 페이지: 정적 사전 렌더링
    '/settings/**': { ssr: false },          // 설정 페이지: 인증 후 CSR 처리
  },

  // 프로덕션 빌드에서 console.log, debugger 제거
  // Vite 기본 minifier는 esbuild → terserOptions 대신 esbuild.drop 사용
  vite: {
    esbuild: {
      drop: process.env.NODE_ENV === 'production' ? ['console', 'debugger'] : [],
    },
  },

  // Nitro 서버 설정 — 보안 헤더 및 성능 최적화
  nitro: {
    routeRules: {
      '/**': {
        headers: {
          /**
           * [🔒보안] Content Security Policy — XSS 공격으로 인한 스크립트 실행 차단
           * 운영 환경에서는 'unsafe-inline' 제거 후 nonce 방식으로 강화 권장
           */
          'Content-Security-Policy':
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self'; " +
            "connect-src 'self' http://localhost:8080",

          // MIME 타입 스니핑 방지
          'X-Content-Type-Options': 'nosniff',

          // Clickjacking 방지
          'X-Frame-Options': 'DENY',

          // 구형 브라우저 XSS 필터 활성화
          'X-XSS-Protection': '1; mode=block',

          // Referrer 정보 최소화
          'Referrer-Policy': 'strict-origin-when-cross-origin',
        }
      }
    }
  },

  app: {
    head: {
      title: 'AiCodeTest - MES System',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: 'AiCodeTest Manufacturing Execution System' }
      ],
      link: [
        // API 서버 사전 연결 — 첫 API 요청 지연 단축
        { rel: 'preconnect', href: 'http://localhost:8080' },
      ],
      // 테마 flash 방지: CSS가 적용되기 전 data-theme 속성을 즉시 설정
      // localStorage의 테마 설정 읽기 (인증 토큰이 아닌 UI 설정이므로 허용)
      script: [
        {
          innerHTML: `(function(){try{var t=localStorage.getItem('appTheme');document.documentElement.setAttribute('data-theme',t||'dark');}catch(e){}})();`,
          tagPosition: 'head',
        }
      ]
    }
  }
})
