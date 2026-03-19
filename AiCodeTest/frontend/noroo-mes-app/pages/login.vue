<template>
  <div class="login-container">
    <div class="login-box">
      <h1>AiCodeTest MES System</h1>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="site">공장 선택</label>
          <select v-model="siteId" id="site" required :disabled="sitesLoading">
            <option value="" disabled>{{ sitesLoading ? '불러오는 중...' : '공장을 선택하세요' }}</option>
            <option v-for="site in sites" :key="site.siteId" :value="site.siteId">
              {{ site.siteName }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="userId">아이디</label>
          <input
            v-model="userId"
            type="text"
            id="userId"
            placeholder="아이디 입력"
            required
          />
        </div>
        <div class="form-group">
          <label for="password">비밀번호</label>
          <div class="password-wrapper">
            <input
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              id="password"
              placeholder="비밀번호 입력"
              required
            />
            <button type="button" class="toggle-password" @click="showPassword = !showPassword" tabindex="-1">
              {{ showPassword ? '숨김' : '표시' }}
            </button>
          </div>
        </div>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <button type="submit" :disabled="loading" class="btn-login">
          {{ loading ? '로그인 중...' : '로그인' }}
        </button>
      </form>
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
    errorMessage.value = '공장 목록을 불러오지 못했습니다.'
  } finally {
    sitesLoading.value = false
  }
})

const handleLogin = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const result = await login({
      siteId: siteId.value,
      userId: userId.value,
      password: password.value
    })

    localStorage.setItem('authToken', result.token)
    localStorage.setItem('userId', result.userId)
    localStorage.setItem('userName', result.userName ?? result.userId)
    localStorage.setItem('siteId', result.siteId)

    await router.push('/')
  } catch (err: any) {
    const msg = err?.data?.message
    errorMessage.value = msg || '아이디 또는 비밀번호가 일치하지 않습니다.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 400px;
}

.login-box h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 500;
}

.form-group select,
.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
  transition: border-color 0.3s;
  box-sizing: border-box;
  background: white;
}

.form-group select:focus,
.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 5px rgba(102, 126, 234, 0.1);
}

.form-group select:disabled {
  background: #f5f5f5;
  color: #aaa;
  cursor: not-allowed;
}

.password-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.password-wrapper input {
  padding-right: 60px;
}

.toggle-password {
  position: absolute;
  right: 8px;
  background: none;
  border: none;
  color: #667eea;
  font-size: 12px;
  cursor: pointer;
  padding: 4px 6px;
}

.toggle-password:hover {
  text-decoration: underline;
}

.error-message {
  color: #e53e3e;
  font-size: 13px;
  margin-bottom: 12px;
  text-align: center;
}

.btn-login {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: opacity 0.3s;
}

.btn-login:hover:not(:disabled) {
  opacity: 0.9;
}

.btn-login:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
