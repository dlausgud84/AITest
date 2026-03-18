<template>
  <div class="dashboard">
    <PageHeader title="대시보드" />
    <div class="content">
      <div class="welcome-section">
        <h2>환영합니다! {{ username }}</h2>
        <p>AiCodeTest Manufacturing Execution System에 로그인하셨습니다.</p>
      </div>

      <div class="menu-grid">
        <div class="menu-card">
          <h3>📋 메뉴 관리</h3>
          <p>시스템 메뉴를 관리합니다</p>
          <NuxtLink to="/menu-management" class="btn">이동</NuxtLink>
        </div>
        <div class="menu-card">
          <h3>⚙️ 설정</h3>
          <p>시스템 설정을 변경합니다</p>
          <NuxtLink to="/settings" class="btn">이동</NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const username = ref('사용자')

onMounted(() => {
  username.value = localStorage.getItem('username') || '사용자'
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.welcome-section {
  background: white;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.welcome-section h2 {
  color: #333;
  margin-bottom: 10px;
}

.welcome-section p {
  color: #666;
  font-size: 14px;
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.menu-card {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.menu-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.menu-card h3 {
  color: #333;
  margin-bottom: 10px;
}

.menu-card p {
  color: #666;
  font-size: 14px;
  margin-bottom: 15px;
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  text-decoration: none;
  border-radius: 5px;
  transition: opacity 0.3s;
}

.btn:hover {
  opacity: 0.9;
}
</style>
