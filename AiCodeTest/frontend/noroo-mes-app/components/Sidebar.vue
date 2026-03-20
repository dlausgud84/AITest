<template>
  <aside class="sidebar">
    <!-- 로고 -->
    <div class="sidebar-header">
      <div class="logo">
        <svg width="24" height="24" viewBox="0 0 28 28" fill="none">
          <rect x="2"  y="2"  width="10" height="10" rx="2" fill="#6c8fff"/>
          <rect x="16" y="2"  width="10" height="10" rx="2" fill="#6c8fff" opacity="0.55"/>
          <rect x="2"  y="16" width="10" height="10" rx="2" fill="#6c8fff" opacity="0.55"/>
          <rect x="16" y="16" width="10" height="10" rx="2" fill="#6c8fff"/>
        </svg>
        <span class="logo-text">NeoMES</span>
      </div>
    </div>

    <!-- 네비게이션 -->
    <nav class="sidebar-nav">
      <NuxtLink to="/" class="nav-item" active-class="active" exact-active-class="active">
        <svg class="nav-icon" width="16" height="16" viewBox="0 0 20 20" fill="none">
          <rect x="2" y="2" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
          <rect x="12" y="2" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
          <rect x="2" y="12" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
          <rect x="12" y="12" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
        </svg>
        <span>대시보드</span>
      </NuxtLink>
      <NuxtLink to="/menu-management" class="nav-item" active-class="active">
        <svg class="nav-icon" width="16" height="16" viewBox="0 0 20 20" fill="none">
          <path d="M3 5H17M3 10H17M3 15H11" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/>
        </svg>
        <span>메뉴 관리</span>
      </NuxtLink>
      <NuxtLink to="/settings" class="nav-item" active-class="active">
        <svg class="nav-icon" width="16" height="16" viewBox="0 0 20 20" fill="none">
          <path d="M10 13A3 3 0 1 0 10 7 3 3 0 0 0 10 13ZM10 2V4M10 16V18M2 10H4M16 10H18" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/>
        </svg>
        <span>설정</span>
      </NuxtLink>
    </nav>

    <!-- 하단: 로그아웃 -->
    <div class="sidebar-footer">
      <button @click="handleLogout" class="btn-logout">
        <svg width="14" height="14" viewBox="0 0 16 16" fill="none">
          <path d="M6 14H3C2.45 14 2 13.55 2 13V3C2 2.45 2.45 2 3 2H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          <path d="M10 11L14 8L10 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M14 8H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        로그아웃
      </button>
    </div>
  </aside>
</template>

<script setup lang="ts">
const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('authToken')
  localStorage.removeItem('userId')
  localStorage.removeItem('userName')
  localStorage.removeItem('siteId')
  localStorage.removeItem('siteName')

  const authCookie = useCookie('authToken')
  authCookie.value = null

  router.push('/login')
}
</script>

<style scoped>
/* ── 사이드바 래퍼 ── */
.sidebar {
  display: flex;
  flex-direction: column;
  width: 220px;
  height: 100vh;
  background: var(--sidebar-bg);
  border-right: 1px solid var(--sidebar-border);
  transition: background 0.25s ease, border-color 0.25s ease;
  flex-shrink: 0;
}

/* ── 로고 헤더 ── */
.sidebar-header {
  padding: 18px 16px 14px;
  border-bottom: 1px solid var(--sidebar-border);
  flex-shrink: 0;
}

.logo {
  display: flex;
  align-items: center;
  gap: 9px;
}

.logo-text {
  font-size: 16px;
  font-weight: 700;
  background: linear-gradient(135deg, #6c8fff, #a78bfa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 0.5px;
}

/* ── 네비게이션 ── */
.sidebar-nav {
  flex: 1;
  padding: 12px 0;
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  color: var(--nav-text-color);
  text-decoration: none;
  font-size: 13px;
  font-weight: 500;
  border-left: 3px solid transparent;
  transition: color 0.2s, background 0.2s, border-color 0.2s;
}

.nav-item:hover {
  color: var(--nav-text-open);
  background: var(--nav-hover-bg);
  text-decoration: none;
}

.nav-item.active {
  color: var(--accent);
  background: var(--nav-item-hover);
  border-left-color: var(--accent);
}

.nav-icon {
  flex-shrink: 0;
  color: inherit;
}

/* ── 로그아웃 ── */
.sidebar-footer {
  padding: 14px 16px;
  border-top: 1px solid var(--sidebar-border);
  flex-shrink: 0;
}

.btn-logout {
  display: flex;
  align-items: center;
  gap: 7px;
  width: 100%;
  padding: 8px 12px;
  background: rgba(255, 99, 99, 0.07);
  border: 1px solid rgba(255, 99, 99, 0.18);
  border-radius: 7px;
  color: #ff8080;
  font-size: 12px;
  font-family: inherit;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s, border-color 0.2s;
}

.btn-logout:hover {
  background: rgba(255, 99, 99, 0.16);
  border-color: rgba(255, 99, 99, 0.38);
}
</style>
