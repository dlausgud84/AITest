<template>
  <div class="login-container">
    <div class="login-box">
      <h1>AiCodeTest MES System</h1>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">사용자명</label>
          <input 
            v-model="username" 
            type="text" 
            id="username" 
            placeholder="사용자명 입력"
            required
          />
        </div>
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input 
            v-model="password" 
            type="password" 
            id="password" 
            placeholder="비밀번호 입력"
            required
          />
        </div>
        <button type="submit" :disabled="loading" class="btn-login">
          {{ loading ? '로그인 중...' : '로그인' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    // 실제 API 호출 대신 여기서는 로컬 스토리지에 토큰 저장
    // TODO: 실제 백엔드 로그인 API 호출로 변경
    localStorage.setItem('authToken', 'dummy-token-for-testing')
    localStorage.setItem('username', username.value)
    
    await router.push('/')
  } catch (error) {
    errorMessage.value = '로그인에 실패했습니다.'
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

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 5px rgba(102, 126, 234, 0.1);
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
