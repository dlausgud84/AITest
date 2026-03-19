<template>
  <div class="login-container">

    <!-- 배경 장식 원 -->
    <div class="bg-circle bg-circle--1" />
    <div class="bg-circle bg-circle--2" />
    <div class="bg-circle bg-circle--3" />

    <div class="login-card">

      <!-- 로고 영역 -->
      <div class="login-header">
        <div class="logo-icon">
          <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
            <rect width="36" height="36" rx="10" fill="white" fill-opacity="0.2"/>
            <path d="M8 10h4v16H8zM14 18l6-8v16l-6-8zM24 10h4v16h-4z" fill="white"/>
          </svg>
        </div>
        <h1 class="logo-title">NeoMES</h1>
        <p class="logo-subtitle">Manufacturing Execution System</p>
      </div>

      <!-- 폼 영역 -->
      <div class="login-body">
        <form @submit.prevent="handleLogin" novalidate>

          <!-- SITE 선택 -->
          <div class="field">
            <label class="field-label" for="site">
              <svg class="field-icon" width="14" height="14" viewBox="0 0 16 16" fill="none">
                <path d="M8 1.5A4.5 4.5 0 0 1 12.5 6c0 3.5-4.5 8.5-4.5 8.5S3.5 9.5 3.5 6A4.5 4.5 0 0 1 8 1.5z" stroke="currentColor" stroke-width="1.4"/>
                <circle cx="8" cy="6" r="1.5" stroke="currentColor" stroke-width="1.4"/>
              </svg>
              SITE 선택
            </label>
            <div class="select-wrapper">
              <select v-model="siteId" id="site" required :disabled="sitesLoading" class="field-input field-select">
                <option value="" disabled>
                  {{ sitesLoading ? '불러오는 중...' : 'SITE를 선택하세요' }}
                </option>
                <option v-for="site in sites" :key="site.siteId" :value="site.siteId">
                  {{ site.siteName }}
                </option>
              </select>
              <span class="select-arrow" aria-hidden="true">
                <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
                  <path d="M2 4l4 4 4-4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span v-if="sitesLoading" class="field-spinner" aria-hidden="true" />
            </div>
          </div>

          <!-- 아이디 -->
          <div class="field">
            <label class="field-label" for="userId">
              <svg class="field-icon" width="14" height="14" viewBox="0 0 16 16" fill="none">
                <circle cx="8" cy="5.5" r="2.5" stroke="currentColor" stroke-width="1.4"/>
                <path d="M2.5 13.5c0-3.038 2.462-5.5 5.5-5.5s5.5 2.462 5.5 5.5" stroke="currentColor" stroke-width="1.4" stroke-linecap="round"/>
              </svg>
              아이디
            </label>
            <input
              v-model="userId"
              type="text"
              id="userId"
              class="field-input"
              placeholder="아이디를 입력하세요 (영문/숫자)"
              autocomplete="username"
              required
              @input="filterEnglishOnly"
            />
          </div>

          <!-- 비밀번호 -->
          <div class="field">
            <label class="field-label" for="password">
              <svg class="field-icon" width="14" height="14" viewBox="0 0 16 16" fill="none">
                <rect x="3" y="7" width="10" height="7" rx="1.5" stroke="currentColor" stroke-width="1.4"/>
                <path d="M5.5 7V5a2.5 2.5 0 0 1 5 0v2" stroke="currentColor" stroke-width="1.4" stroke-linecap="round"/>
              </svg>
              비밀번호
            </label>
            <div class="password-wrapper">
              <input
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                id="password"
                class="field-input"
                placeholder="비밀번호를 입력하세요"
                autocomplete="current-password"
                required
              />
              <button
                type="button"
                class="toggle-password"
                :aria-label="showPassword ? '비밀번호 숨기기' : '비밀번호 표시'"
                @click="showPassword = !showPassword"
                tabindex="-1"
              >
                <!-- 눈 열림 -->
                <svg v-if="showPassword" width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M1 8s2.5-5 7-5 7 5 7 5-2.5 5-7 5-7-5-7-5z" stroke="currentColor" stroke-width="1.4"/>
                  <circle cx="8" cy="8" r="2" stroke="currentColor" stroke-width="1.4"/>
                </svg>
                <!-- 눈 닫힘 -->
                <svg v-else width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M2 2l12 12M6.5 6.7A2 2 0 0 0 9.3 9.5M4.2 4.4C2.8 5.3 1.8 6.7 1 8c1.2 2.1 3.8 5 7 5a7 7 0 0 0 3.5-.9M7 3.1A7 7 0 0 1 15 8c-.4.8-1 1.7-1.7 2.4" stroke="currentColor" stroke-width="1.4" stroke-linecap="round"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- 오류 메시지 -->
          <div v-if="errorMessage" class="error-box" role="alert">
            <svg width="14" height="14" viewBox="0 0 16 16" fill="none">
              <circle cx="8" cy="8" r="6.5" stroke="currentColor" stroke-width="1.4"/>
              <path d="M8 5v3.5M8 11h.01" stroke="currentColor" stroke-width="1.4" stroke-linecap="round"/>
            </svg>
            {{ errorMessage }}
          </div>

          <!-- 로그인 버튼 -->
          <button type="submit" :disabled="loading" class="btn-login">
            <span v-if="loading" class="btn-spinner" aria-hidden="true" />
            <svg v-else width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M3 8h10M9 4l4 4-4 4" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            {{ loading ? '로그인 중...' : '로그인' }}
          </button>

        </form>
      </div>

      <!-- 하단 -->
      <div class="login-footer">
        <span>© 2025 NeoMES. All rights reserved.</span>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthAPI, type Site } from '~/composables/useAuthAPI'

const router = useRouter()
const { fetchSites, login } = useAuthAPI()

const sites = ref<Site[]>([])
const sitesLoading = ref(false)
const siteId = ref('')
const userId = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')

onMounted(async () => {
  sitesLoading.value = true
  try {
    sites.value = await fetchSites()
  } catch {
    errorMessage.value = 'SITE 목록을 불러오지 못했습니다.'
  } finally {
    sitesLoading.value = false
  }
})

// 영문자, 숫자, 특수문자(@._-)만 허용 (한글 등 비영문 자동 제거)
function filterEnglishOnly(e: Event) {
  const input = e.target as HTMLInputElement
  const filtered = input.value.replace(/[^a-zA-Z0-9@._\-]/g, '')
  if (input.value !== filtered) {
    input.value = filtered
    userId.value = filtered
  }
}

const handleLogin = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const result = await login({
      userId: userId.value,
      password: password.value
    })

    // localStorage 저장 (siteId는 result가 아닌 폼 선택값 사용 — 백엔드가 null 반환할 수 있음)
    localStorage.setItem('authToken', result.token)
    localStorage.setItem('userId', result.userId)
    localStorage.setItem('userName', result.userName ?? result.userId)
    localStorage.setItem('siteId', siteId.value)

    // 선택한 SITE 이름 저장 (메인 페이지 헤더 표시용)
    const selectedSite = sites.value.find(s => s.siteId === siteId.value)
    if (selectedSite) {
      localStorage.setItem('siteName', selectedSite.siteName)
    }

    // 쿠키에도 저장 (SSR 미들웨어가 인증 토큰을 읽을 수 있도록)
    const authCookie = useCookie('authToken', { maxAge: 60 * 60 * 8 })
    authCookie.value = result.token

    await router.push('/')
  } catch (err: any) {
    const msg = err?.data?.message ?? err?.message
    errorMessage.value = msg || '아이디 또는 비밀번호가 일치하지 않습니다.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* ── 배경 ── */
.login-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(145deg, #0f0c29 0%, #302b63 50%, #24243e 100%);
  overflow: hidden;
  padding: 24px;
  box-sizing: border-box;
}

/* 배경 장식 원 */
.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.12;
  pointer-events: none;
}
.bg-circle--1 {
  width: 480px; height: 480px;
  background: radial-gradient(circle, #667eea, transparent);
  top: -160px; left: -160px;
}
.bg-circle--2 {
  width: 360px; height: 360px;
  background: radial-gradient(circle, #764ba2, transparent);
  bottom: -100px; right: -80px;
}
.bg-circle--3 {
  width: 220px; height: 220px;
  background: radial-gradient(circle, #43e97b, transparent);
  top: 40%; left: 60%;
  opacity: 0.07;
}

/* ── 카드 ── */
.login-card {
  position: relative;
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 20px;
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.5), inset 0 1px 0 rgba(255,255,255,0.1);
  overflow: hidden;
}

/* ── 헤더 ── */
.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 40px 40px 28px;
  background: linear-gradient(180deg, rgba(102,126,234,0.25) 0%, transparent 100%);
  border-bottom: 1px solid rgba(255,255,255,0.08);
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px; height: 60px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.45);
  margin-bottom: 4px;
}

.logo-title {
  margin: 0;
  font-size: 28px;
  font-weight: 800;
  color: #fff;
  letter-spacing: 1px;
}

.logo-subtitle {
  margin: 0;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 1.5px;
  text-transform: uppercase;
}

/* ── 폼 바디 ── */
.login-body {
  padding: 32px 40px 24px;
}

/* ── 필드 ── */
.field {
  margin-bottom: 20px;
}

.field-label {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  font-size: 12px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.65);
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.field-icon {
  color: #667eea;
  flex-shrink: 0;
}

.field-input {
  width: 100%;
  height: 46px;
  padding: 0 14px;
  background: rgba(255, 255, 255, 0.07);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 10px;
  color: #fff;
  font-size: 14px;
  font-family: inherit;
  transition: border-color 0.2s, background 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
  outline: none;
}

.field-input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.field-input:focus {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.1);
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2);
}

/* 셀렉트 */
.select-wrapper {
  position: relative;
}

.field-select {
  appearance: none;
  -webkit-appearance: none;
  padding-right: 40px;
  cursor: pointer;
}

.field-select option {
  background: #302b63;
  color: #fff;
}

.field-select:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.select-arrow {
  position: absolute;
  right: 13px;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, 0.4);
  pointer-events: none;
  display: flex;
  align-items: center;
}

.field-spinner {
  position: absolute;
  right: 13px;
  top: 50%;
  transform: translateY(-50%);
  width: 14px; height: 14px;
  border: 2px solid rgba(255,255,255,0.2);
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

/* 비밀번호 */
.password-wrapper {
  position: relative;
}

.password-wrapper .field-input {
  padding-right: 46px;
}

.toggle-password {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  transition: color 0.2s;
}

.toggle-password:hover {
  color: #667eea;
}

/* ── 오류 박스 ── */
.error-box {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 14px;
  margin-bottom: 16px;
  background: rgba(229, 62, 62, 0.15);
  border: 1px solid rgba(229, 62, 62, 0.35);
  border-radius: 8px;
  color: #fc8181;
  font-size: 13px;
}

/* ── 로그인 버튼 ── */
.btn-login {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  height: 48px;
  margin-top: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 700;
  font-family: inherit;
  cursor: pointer;
  letter-spacing: 0.5px;
  transition: opacity 0.2s, transform 0.15s, box-shadow 0.2s;
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-login:hover:not(:disabled) {
  opacity: 0.92;
  transform: translateY(-1px);
  box-shadow: 0 10px 28px rgba(102, 126, 234, 0.5);
}

.btn-login:active:not(:disabled) {
  transform: translateY(0);
}

.btn-login:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

/* 버튼 스피너 */
.btn-spinner {
  display: inline-block;
  width: 16px; height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.35);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
  flex-shrink: 0;
}

/* ── 푸터 ── */
.login-footer {
  padding: 14px 40px 20px;
  text-align: center;
  font-size: 11px;
  color: rgba(255, 255, 255, 0.25);
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

/* ── 애니메이션 ── */
@keyframes spin {
  to { transform: rotate(360deg); }
}

</style>
