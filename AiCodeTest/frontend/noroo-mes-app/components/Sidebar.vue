<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <h2>AiCodeTest</h2>
    </div>
    <nav class="sidebar-nav">
      <NuxtLink to="/" class="nav-item" active-class="active">
        <span>📊 대시보드</span>
      </NuxtLink>
      <NuxtLink to="/menu-management" class="nav-item" active-class="active">
        <span>📋 메뉴 관리</span>
      </NuxtLink>
      <NuxtLink to="/settings" class="nav-item" active-class="active">
        <span>⚙️ 설정</span>
      </NuxtLink>
    </nav>
    <div class="sidebar-footer">
      <button @click="handleLogout" class="btn-logout">로그아웃</button>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const handleLogout = () => {
  // localStorage 전체 정리
  localStorage.removeItem('authToken')
  localStorage.removeItem('userId')
  localStorage.removeItem('userName')
  localStorage.removeItem('siteId')
  localStorage.removeItem('siteName')

  // 쿠키도 제거 (SSR 미들웨어가 쿠키로 인증 여부 판단)
  const authCookie = useCookie('authToken')
  authCookie.value = null

  router.push('/login')
}
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  flex-direction: column;
  height: 100vh;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.sidebar-header h2 {
  margin: 0;
  font-size: 20px;
}

.sidebar-nav {
  flex: 1;
  padding: 20px 0;
}

.nav-item {
  display: block;
  padding: 15px 20px;
  color: white;
  text-decoration: none;
  transition: background 0.3s;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.2);
  border-left-color: white;
}

.sidebar-footer {
  padding: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.btn-logout {
  width: 100%;
  padding: 10px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid white;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-logout:hover {
  background: rgba(255, 255, 255, 0.3);
}
</style>
