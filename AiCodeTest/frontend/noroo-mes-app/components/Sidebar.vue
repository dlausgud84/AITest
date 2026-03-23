<template>
  <aside :class="['sidebar', { collapsed: isCollapsed }]">

    <!-- NeoMES 로고 = 접기/펼치기 토글 버튼 -->
    <button
      class="sidebar-header"
      @click="toggle"
      :title="isCollapsed ? '메뉴 펼치기' : '메뉴 접기'"
    >
      <!-- 좌측: 아이콘 + 브랜드명 -->
      <span class="logo-group">
        <svg class="logo-icon" width="24" height="24" viewBox="0 0 28 28" fill="none">
          <rect x="2"  y="2"  width="10" height="10" rx="2" fill="#6c8fff"/>
          <rect x="16" y="2"  width="10" height="10" rx="2" fill="#6c8fff" opacity="0.55"/>
          <rect x="2"  y="16" width="10" height="10" rx="2" fill="#6c8fff" opacity="0.55"/>
          <rect x="16" y="16" width="10" height="10" rx="2" fill="#6c8fff"/>
        </svg>
        <span class="logo-text nav-label">NeoMES</span>
      </span>

      <!-- 우측: 더블 쉐브론 (접힌 상태에서 숨김 + 180° 회전) -->
      <span class="toggle-wrap">
        <svg
          class="toggle-icon"
          :class="{ 'is-collapsed': isCollapsed }"
          width="14" height="14" viewBox="0 0 16 16" fill="none"
        >
          <path d="M10 3L5 8L10 13" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M13 3L8 8L13 13" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </span>
    </button>

    <!-- 네비게이션 -->
    <nav class="sidebar-nav">

      <!-- 대시보드 -->
      <NuxtLink to="/" class="nav-item" active-class="active" exact-active-class="active" @click="handleLeafClick">
        <svg class="nav-icon" width="16" height="16" viewBox="0 0 20 20" fill="none">
          <rect x="2" y="2" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
          <rect x="12" y="2" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
          <rect x="2" y="12" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
          <rect x="12" y="12" width="6" height="6" rx="1.5" stroke="currentColor" stroke-width="1.6"/>
        </svg>
        <span class="nav-label">대시보드</span>
      </NuxtLink>

      <!-- 메뉴 관리 -->
      <NuxtLink to="/menu-management" class="nav-item" active-class="active" @click="handleLeafClick">
        <svg class="nav-icon" width="16" height="16" viewBox="0 0 20 20" fill="none">
          <path d="M3 5H17M3 10H17M3 15H11" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/>
        </svg>
        <span class="nav-label">메뉴 관리</span>
      </NuxtLink>

      <!-- ── 설정 그룹 ── -->
      <div class="nav-group">
        <!-- 설정 그룹 헤더: collapsed 상태면 사이드바 펼치기 -->
        <button class="nav-group-header" @click="handleGroupHeaderClick('settings')">
          <div class="nav-group-header-left">
            <svg class="nav-icon" width="16" height="16" viewBox="0 0 20 20" fill="none">
              <path d="M10 13A3 3 0 1 0 10 7 3 3 0 0 0 10 13ZM10 2V4M10 16V18M2 10H4M16 10H18" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/>
            </svg>
            <span class="nav-label">설정</span>
          </div>
          <svg class="chevron" :class="{ open: openGroups.settings }" width="12" height="12" viewBox="0 0 12 12" fill="none">
            <path d="M3 4.5L6 7.5L9 4.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>

        <!-- 설정 그룹 하위 메뉴 -->
        <div class="nav-group-body" :class="{ open: openGroups.settings }">

          <!-- 기본 설정 서브그룹 -->
          <div class="nav-subgroup">
            <button class="nav-subgroup-header" @click="handleSubgroupHeaderClick('basicSettings')">
              <div class="nav-subgroup-header-left">
                <svg class="nav-icon" width="14" height="14" viewBox="0 0 20 20" fill="none">
                  <circle cx="10" cy="10" r="7" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M10 7V10L12 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                </svg>
                <span>기본 설정</span>
              </div>
              <svg class="chevron" :class="{ open: openSubgroups.basicSettings }" width="10" height="10" viewBox="0 0 12 12" fill="none">
                <path d="M3 4.5L6 7.5L9 4.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
            <div class="nav-subgroup-body" :class="{ open: openSubgroups.basicSettings }">
              <NuxtLink to="/settings/basic/user"            class="nav-leaf" active-class="active" @click="handleLeafClick">사용자 설정</NuxtLink>
              <NuxtLink to="/settings/basic/permission"      class="nav-leaf" active-class="active" @click="handleLeafClick">권한 설정</NuxtLink>
              <NuxtLink to="/settings/basic/function"        class="nav-leaf" active-class="active" @click="handleLeafClick">기능 설정</NuxtLink>
              <NuxtLink to="/settings/basic/favorites"       class="nav-leaf" active-class="active" @click="handleLeafClick">즐겨찾기 설정</NuxtLink>
              <NuxtLink to="/settings/basic/password-policy" class="nav-leaf" active-class="active" @click="handleLeafClick">Password Security Policy</NuxtLink>
              <NuxtLink to="/settings/basic/code"            class="nav-leaf" active-class="active" @click="handleLeafClick">코드관리</NuxtLink>
              <NuxtLink to="/settings/basic/message"         class="nav-leaf" active-class="active" @click="handleLeafClick">메시지 설정</NuxtLink>
              <NuxtLink to="/settings/basic/service-member"  class="nav-leaf" active-class="active" @click="handleLeafClick">Service Member Setup</NuxtLink>
              <NuxtLink to="/settings/basic/board"           class="nav-leaf" active-class="active" @click="handleLeafClick">게시판 관리</NuxtLink>
              <NuxtLink to="/settings/basic/help"            class="nav-leaf" active-class="active" @click="handleLeafClick">Help Setup</NuxtLink>
              <NuxtLink to="/settings/basic/department"      class="nav-leaf" active-class="active" @click="handleLeafClick">부서 설정</NuxtLink>
              <NuxtLink to="/settings/basic/customer"        class="nav-leaf" active-class="active" @click="handleLeafClick">고객사 설정</NuxtLink>
              <NuxtLink to="/settings/basic/vendor"          class="nav-leaf" active-class="active" @click="handleLeafClick">Vendor Setup</NuxtLink>
              <NuxtLink to="/settings/basic/id-rule"         class="nav-leaf" active-class="active" @click="handleLeafClick">ID Generation Rule Setup</NuxtLink>
              <NuxtLink to="/settings/basic/query"           class="nav-leaf" active-class="active" @click="handleLeafClick">Query</NuxtLink>
              <NuxtLink to="/settings/basic/manager"         class="nav-leaf" active-class="active" @click="handleLeafClick">Manager</NuxtLink>
              <NuxtLink to="/settings/basic/ftp"             class="nav-leaf" active-class="active" @click="handleLeafClick">FTP 서버 설정</NuxtLink>
            </div>
          </div>

          <!-- 공정 관리 서브그룹 -->
          <div class="nav-subgroup">
            <button class="nav-subgroup-header" @click="handleSubgroupHeaderClick('processManagement')">
              <div class="nav-subgroup-header-left">
                <svg class="nav-icon" width="14" height="14" viewBox="0 0 20 20" fill="none">
                  <path d="M3 10H17M8 5L3 10L8 15" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <span>공정 관리</span>
              </div>
              <svg class="chevron" :class="{ open: openSubgroups.processManagement }" width="10" height="10" viewBox="0 0 12 12" fill="none">
                <path d="M3 4.5L6 7.5L9 4.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
            <div class="nav-subgroup-body" :class="{ open: openSubgroups.processManagement }">
              <span class="nav-empty">준비 중</span>
            </div>
          </div>

          <!-- 문서 제어 서브그룹 -->
          <div class="nav-subgroup">
            <button class="nav-subgroup-header" @click="handleSubgroupHeaderClick('documentControl')">
              <div class="nav-subgroup-header-left">
                <svg class="nav-icon" width="14" height="14" viewBox="0 0 20 20" fill="none">
                  <path d="M5 3H13L17 7V17H5V3Z" stroke="currentColor" stroke-width="1.5" stroke-linejoin="round"/>
                  <path d="M13 3V7H17" stroke="currentColor" stroke-width="1.5" stroke-linejoin="round"/>
                </svg>
                <span>문서 제어</span>
              </div>
              <svg class="chevron" :class="{ open: openSubgroups.documentControl }" width="10" height="10" viewBox="0 0 12 12" fill="none">
                <path d="M3 4.5L6 7.5L9 4.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
            <div class="nav-subgroup-body" :class="{ open: openSubgroups.documentControl }">
              <span class="nav-empty">준비 중</span>
            </div>
          </div>

          <!-- 설비 관리 서브그룹 -->
          <div class="nav-subgroup">
            <button class="nav-subgroup-header" @click="handleSubgroupHeaderClick('equipmentManagement')">
              <div class="nav-subgroup-header-left">
                <svg class="nav-icon" width="14" height="14" viewBox="0 0 20 20" fill="none">
                  <rect x="3" y="7" width="14" height="10" rx="1.5" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M7 7V5C7 3.9 7.9 3 9 3H11C12.1 3 13 3.9 13 5V7" stroke="currentColor" stroke-width="1.5"/>
                </svg>
                <span>설비 관리</span>
              </div>
              <svg class="chevron" :class="{ open: openSubgroups.equipmentManagement }" width="10" height="10" viewBox="0 0 12 12" fill="none">
                <path d="M3 4.5L6 7.5L9 4.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
            <div class="nav-subgroup-body" :class="{ open: openSubgroups.equipmentManagement }">
              <span class="nav-empty">준비 중</span>
            </div>
          </div>

          <!-- 데이터 관리 서브그룹 -->
          <div class="nav-subgroup">
            <button class="nav-subgroup-header" @click="handleSubgroupHeaderClick('dataManagement')">
              <div class="nav-subgroup-header-left">
                <svg class="nav-icon" width="14" height="14" viewBox="0 0 20 20" fill="none">
                  <ellipse cx="10" cy="6" rx="7" ry="3" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M3 6V10C3 11.66 6.13 13 10 13C13.87 13 17 11.66 17 10V6" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M3 10V14C3 15.66 6.13 17 10 17C13.87 17 17 15.66 17 14V10" stroke="currentColor" stroke-width="1.5"/>
                </svg>
                <span>데이터 관리</span>
              </div>
              <svg class="chevron" :class="{ open: openSubgroups.dataManagement }" width="10" height="10" viewBox="0 0 12 12" fill="none">
                <path d="M3 4.5L6 7.5L9 4.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
            <div class="nav-subgroup-body" :class="{ open: openSubgroups.dataManagement }">
              <span class="nav-empty">준비 중</span>
            </div>
          </div>

        </div>
      </div>
      <!-- ── /설정 그룹 ── -->

    </nav>

    <!-- 하단: 로그아웃 -->
    <div class="sidebar-footer">
      <button @click="handleLogout" class="btn-logout">
        <svg width="14" height="14" viewBox="0 0 16 16" fill="none">
          <path d="M6 14H3C2.45 14 2 13.55 2 13V3C2 2.45 2.45 2 3 2H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          <path d="M10 11L14 8L10 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M14 8H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        <span class="nav-label">로그아웃</span>
      </button>
    </div>

  </aside>
</template>

<script setup lang="ts">
const router = useRouter()
const route  = useRoute()
const { isCollapsed, toggle, collapse, expand } = useSidebarState()

/* ── 1단계: 설정 그룹 열림 상태 ── */
const openGroups = reactive<Record<string, boolean>>({
  settings: false,
})

/* ── 2단계: 서브그룹 열림 상태 ── */
const openSubgroups = reactive<Record<string, boolean>>({
  basicSettings:      false,
  processManagement:  false,
  documentControl:    false,
  equipmentManagement:false,
  dataManagement:     false,
})

/* 현재 경로가 설정 하위이면 자동 펼침
   - 사이드바가 펼쳐진 상태(isCollapsed = false)일 때만 동작
   - 리프 메뉴 클릭 시: collapse()로 이미 isCollapsed = true이므로 자동으로 건너뜀 */
watch(() => route.path, (newPath) => {
  if (isCollapsed.value) return   // 접힌 상태면 메뉴 열지 않음
  if (newPath.startsWith('/settings')) {
    openGroups.settings = true
    if      (newPath.startsWith('/settings/basic'))     openSubgroups.basicSettings       = true
    else if (newPath.startsWith('/settings/process'))   openSubgroups.processManagement   = true
    else if (newPath.startsWith('/settings/document'))  openSubgroups.documentControl     = true
    else if (newPath.startsWith('/settings/equipment')) openSubgroups.equipmentManagement = true
    else if (newPath.startsWith('/settings/data'))      openSubgroups.dataManagement      = true
  }
}, { immediate: true })

/* ── 그룹 헤더 클릭
     - 접힌 상태: 사이드바 펼치기 + 해당 그룹 열기
     - 펼친 상태: 그룹 토글
  ── */
const handleGroupHeaderClick = (key: string) => {
  if (isCollapsed.value) {
    expand()
    openGroups[key] = true
  } else {
    openGroups[key] = !openGroups[key]
  }
}

/* 서브그룹 헤더 클릭 (접힌 상태에서는 동작 안 함) */
const handleSubgroupHeaderClick = (key: string) => {
  if (!isCollapsed.value) {
    openSubgroups[key] = !openSubgroups[key]
  }
}

/* 리프(최종 목적지) 메뉴 클릭 → 사이드바 + 열린 메뉴 그룹 모두 접기
   collapse()가 먼저 실행되어 isCollapsed = true 가 된 뒤
   watch가 돌더라도 isCollapsed.value 체크로 자동 차단됨 */
const handleLeafClick = () => {
  collapse()
  openGroups.settings = false
  Object.keys(openSubgroups).forEach(key => { openSubgroups[key] = false })
}

/* ── 로그아웃 ── */
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
/* ════════════════════════════
   사이드바 기본 + 슬라이드 트랜지션
   ════════════════════════════ */
.sidebar {
  display: flex;
  flex-direction: column;
  width: 220px;
  height: 100vh;
  background: var(--sidebar-bg);
  border-right: 1px solid var(--sidebar-border);
  /* width 트랜지션 추가 — AppLayout 의 flex:1 main-content 가 자동으로 늘어남 */
  transition: width 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              background 0.25s ease,
              border-color 0.25s ease;
  flex-shrink: 0;
  overflow: hidden; /* 접힐 때 내부 콘텐츠 클리핑 */
}

/* 접힌 너비 */
.sidebar.collapsed {
  width: 56px;
}

/* ════════════════════════════
   헤더 = NeoMES 로고 토글 버튼
   ════════════════════════════ */
.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 좌: logo-group / 우: toggle-wrap */
  width: 100%;
  padding: 16px 14px;
  border-bottom: 1px solid var(--sidebar-border);
  border-left: none;
  border-right: none;
  border-top: none;
  border-radius: 0;
  flex-shrink: 0;
  min-height: 57px;
  background: none;
  cursor: pointer;
  color: var(--nav-text-color);
  font-family: inherit;
  transition: background 0.2s, border-color 0.25s ease;
}

.sidebar-header:hover {
  background: var(--nav-hover-bg);
}

/* 접힌 상태: logo-group 만 남아 중앙 정렬 */
.sidebar.collapsed .sidebar-header {
  justify-content: center;
  padding: 16px 0;
}

/* ── 좌측: 아이콘 + 브랜드명 ── */
.logo-group {
  display: flex;
  align-items: center;
  gap: 9px;
}

/* 접힌 상태: gap 제거 (숨겨진 텍스트로 인한 여백 제거) */
.sidebar.collapsed .logo-group {
  gap: 0;
}

.logo-icon {
  flex-shrink: 0;
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

/* ── 우측: 더블 쉐브론 래퍼 ── */
.toggle-wrap {
  display: flex;
  align-items: center;
  overflow: hidden;
  max-width: 20px;    /* 아이콘 너비 확보 */
  opacity: 0.5;
  flex-shrink: 0;
  /* 펼칠 때: 너비 먼저, 이후 opacity */
  transition: max-width 0.25s ease, opacity 0.15s ease 0.1s;
}

.sidebar-header:hover .toggle-wrap {
  opacity: 1;
}

/* 접힐 때: opacity 먼저, 너비 뒤따라 */
.sidebar.collapsed .toggle-wrap {
  max-width: 0;
  opacity: 0;
  transition: opacity 0.1s ease, max-width 0.25s ease;
}

/* 더블 쉐브론 회전 */
.toggle-icon {
  flex-shrink: 0;
  transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 접힌 상태에서 180° 회전 → 방향 반전 */
.toggle-icon.is-collapsed {
  transform: rotate(180deg);
}

/* ════════════════════════════
   nav-label: 텍스트 슬라이드 아웃/인
   ════════════════════════════ */
.nav-label {
  overflow: hidden;
  white-space: nowrap;
  max-width: 200px;
  opacity: 1;
  /* 펼칠 때: 너비 먼저, 이후 opacity (살짝 딜레이) */
  transition: max-width 0.25s ease, opacity 0.15s ease 0.1s;
}

/* 접힐 때: opacity 먼저, 너비 뒤따라 */
.sidebar.collapsed .nav-label {
  max-width: 0;
  opacity: 0;
  transition: opacity 0.1s ease, max-width 0.25s ease;
}

/* ════════════════════════════
   접힌 상태 아이템 중앙 정렬
   ════════════════════════════ */

/* nav-item */
.sidebar.collapsed .nav-item {
  justify-content: center;
  padding: 10px 0;
  gap: 0;
}

/* 그룹 헤더 */
.sidebar.collapsed .nav-group-header {
  justify-content: center;
  padding: 10px 0;
}

.sidebar.collapsed .nav-group-header-left {
  gap: 0;
}

/* chevron: 접힌 상태에서 숨김 */
.sidebar.collapsed .chevron {
  display: none;
}

/* 그룹 바디: 접힌 상태에서 강제 닫힘 */
.sidebar.collapsed .nav-group-body {
  max-height: 0 !important;
}

/* 로그아웃 버튼 */
.sidebar.collapsed .btn-logout {
  justify-content: center;
  padding: 8px;
  gap: 0;
}

/* ════════════════════════════
   네비게이션 공통
   ════════════════════════════ */
.sidebar-nav {
  flex: 1;
  padding: 12px 0;
  overflow-y: auto;
  overflow-x: hidden;
}

/* ── 일반 NavLink 아이템 ── */
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
  transition: color 0.2s, background 0.2s, border-color 0.2s,
              padding 0.25s ease, gap 0.25s ease;
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

/* ── 1단계 그룹 (설정) ── */
.nav-group {
  border-left: 3px solid transparent;
}

.nav-group-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 10px 16px 10px 13px;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--nav-text-color);
  font-size: 13px;
  font-weight: 500;
  font-family: inherit;
  transition: color 0.2s, background 0.2s, padding 0.25s ease;
}

.nav-group-header:hover {
  color: var(--nav-text-open);
  background: var(--nav-hover-bg);
}

.nav-group-header-left {
  display: flex;
  align-items: center;
  gap: 10px;
  transition: gap 0.25s ease;
}

/* ── chevron 화살표 ── */
.chevron {
  flex-shrink: 0;
  transition: transform 0.2s ease;
  color: var(--nav-text-color);
  opacity: 0.6;
}

.chevron.open {
  transform: rotate(180deg);
}

/* ── 1단계 그룹 바디 (접기/펼치기) ── */
.nav-group-body {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.25s ease;
}

.nav-group-body.open {
  max-height: 1200px;
}

/* ── 2단계 서브그룹 ── */
.nav-subgroup {
  margin: 0;
}

.nav-subgroup-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 8px 16px 8px 28px;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--nav-text-color);
  font-size: 12.5px;
  font-weight: 500;
  font-family: inherit;
  transition: color 0.2s, background 0.2s;
}

.nav-subgroup-header:hover {
  color: var(--nav-text-open);
  background: var(--nav-hover-bg);
}

.nav-subgroup-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* ── 2단계 서브그룹 바디 ── */
.nav-subgroup-body {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.25s ease;
}

.nav-subgroup-body.open {
  max-height: 800px;
}

/* ── 3단계 리프 메뉴 ── */
.nav-leaf {
  display: block;
  padding: 7px 16px 7px 44px;
  color: var(--nav-text-color);
  text-decoration: none;
  font-size: 12px;
  font-weight: 400;
  border-left: 3px solid transparent;
  transition: color 0.2s, background 0.2s, border-color 0.2s;
  opacity: 0.85;
}

.nav-leaf:hover {
  color: var(--nav-text-open);
  background: var(--nav-hover-bg);
  text-decoration: none;
  opacity: 1;
}

.nav-leaf.active {
  color: var(--accent);
  background: var(--nav-item-hover);
  border-left-color: var(--accent);
  opacity: 1;
}

/* ── 준비 중 표시 ── */
.nav-empty {
  display: block;
  padding: 6px 16px 6px 44px;
  font-size: 11px;
  color: var(--nav-text-color);
  opacity: 0.4;
  font-style: italic;
}

/* ════════════════════════════
   푸터 (로그아웃)
   ════════════════════════════ */
.sidebar-footer {
  padding: 14px 16px;
  border-top: 1px solid var(--sidebar-border);
  flex-shrink: 0;
  transition: padding 0.25s ease;
}

.sidebar.collapsed .sidebar-footer {
  padding: 14px 8px;
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
  transition: background 0.2s, border-color 0.2s, gap 0.25s ease, padding 0.25s ease;
}

.btn-logout:hover {
  background: rgba(255, 99, 99, 0.16);
  border-color: rgba(255, 99, 99, 0.38);
}
</style>
