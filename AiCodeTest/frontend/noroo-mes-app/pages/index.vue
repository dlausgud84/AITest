<template>
  <div class="page-layout">

    <!-- ══ TOP BAR ══ -->
    <header class="top-bar">
      <!-- 좌측: 로고 -->
      <div class="top-left">
        <div class="logo">
          <svg width="26" height="26" viewBox="0 0 28 28" fill="none">
            <rect x="2"  y="2"  width="10" height="10" rx="2" fill="#6c8fff"/>
            <rect x="16" y="2"  width="10" height="10" rx="2" fill="#6c8fff" opacity="0.55"/>
            <rect x="2"  y="16" width="10" height="10" rx="2" fill="#6c8fff" opacity="0.55"/>
            <rect x="16" y="16" width="10" height="10" rx="2" fill="#6c8fff"/>
          </svg>
          <span class="logo-text">NeoMES</span>
        </div>
        <div class="site-badge" v-if="siteName">
          <svg width="10" height="10" viewBox="0 0 12 12" fill="none">
            <path d="M6 1L7.5 4.5H11L8.25 6.75L9.25 10.5L6 8.25L2.75 10.5L3.75 6.75L1 4.5H4.5L6 1Z" fill="#6c8fff"/>
          </svg>
          {{ siteName }}
        </div>
      </div>

      <!-- 우측: 테마 토글 + 날짜/시간 + 사용자 + 로그아웃 -->
      <div class="top-right">
        <!-- 테마 토글 스위치 -->
        <button
          class="theme-switch"
          :class="{ 'is-light': theme === 'light' }"
          @click="toggleTheme"
          :title="theme === 'dark' ? '화이트 테마로 전환' : '블랙 테마로 전환'"
          :aria-label="theme === 'dark' ? '화이트 테마로 전환' : '블랙 테마로 전환'"
        >
          <span class="switch-track">
            <span class="switch-knob">
              <!-- 달 아이콘 (블랙 모드) -->
              <svg v-if="theme === 'dark'" width="10" height="10" viewBox="0 0 16 16" fill="none">
                <path d="M13.5 10.5a6 6 0 0 1-8-8 6 6 0 1 0 8 8z" fill="currentColor"/>
              </svg>
              <!-- 태양 아이콘 (화이트 모드) -->
              <svg v-else width="10" height="10" viewBox="0 0 16 16" fill="none">
                <circle cx="8" cy="8" r="3" fill="currentColor"/>
                <path d="M8 1.5v1M8 13.5v1M1.5 8h1M13.5 8h1M3.4 3.4l.7.7M11.9 11.9l.7.7M3.4 12.6l.7-.7M11.9 4.1l.7-.7" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/>
              </svg>
            </span>
          </span>
        </button>

        <ClientOnly>
          <!-- 날짜·시간 -->
          <div class="top-datetime">
            <span class="datetime-date">{{ currentDate }}</span>
            <span class="datetime-sep">|</span>
            <span class="datetime-time">{{ currentTime }}</span>
          </div>

          <!-- 사용자 정보 -->
          <div class="top-user">
            <div class="user-avatar">{{ userInitial }}</div>
            <div class="user-detail">
              <span class="user-name">{{ userName }}</span>
              <span class="user-id">{{ userId }}</span>
            </div>
          </div>
        </ClientOnly>

        <!-- 로그아웃 버튼 -->
        <button class="btn-logout" @click="handleLogout">
          <svg width="15" height="15" viewBox="0 0 16 16" fill="none">
            <path d="M6 14H3C2.45 14 2 13.55 2 13V3C2 2.45 2.45 2 3 2H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
            <path d="M10 11L14 8L10 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M14 8H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
          로그아웃
        </button>
      </div>
    </header>

    <!-- ══ SIDEBAR ══ -->
    <aside class="sidebar">
      <nav class="sidebar-nav">
        <div
          v-for="section in menuSections"
          :key="section.id"
          class="nav-section"
        >
          <!-- 섹션 헤더 -->
          <button
            class="nav-section-header"
            :class="{ open: section.open }"
            @click="section.open = !section.open"
          >
            <span class="nav-section-icon" :class="section.colorClass">
              <component :is="section.icon" />
            </span>
            <span class="nav-section-title">{{ section.title }}</span>
            <span class="nav-section-badge">{{ section.badge }}</span>
            <svg class="nav-chevron" :class="{ rotated: section.open }" width="12" height="12" viewBox="0 0 12 12" fill="none">
              <path d="M3 5L6 8L9 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>

          <!-- 섹션 하위 메뉴 : 일반(플랫) -->
          <div v-if="!section.subgroups" class="nav-items" :class="{ visible: section.open }">
            <button
              v-for="item in section.items"
              :key="item.id"
              class="nav-item"
              @click="navigate(item.path)"
            >
              <span class="nav-item-dot" :class="section.colorClass" />
              <span class="nav-item-label">{{ item.title }}</span>
            </button>
          </div>

          <!-- 섹션 하위 메뉴 : 서브그룹(3단계) -->
          <div v-else class="nav-items nav-items--tall" :class="{ visible: section.open }">
            <div
              v-for="sg in section.subgroups"
              :key="sg.id"
              class="nav-subgroup"
            >
              <button class="nav-subgroup-header" @click="sg.open = !sg.open">
                <span class="nav-subgroup-title">{{ sg.title }}</span>
                <svg class="nav-chevron nav-chevron--sm" :class="{ rotated: sg.open }" width="10" height="10" viewBox="0 0 12 12" fill="none">
                  <path d="M3 5L6 8L9 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
              <div class="nav-subitems" :class="{ visible: sg.open }">
                <button
                  v-for="item in sg.items"
                  :key="item.id"
                  class="nav-subitem"
                  @click="navigate(item.path)"
                >
                  <span class="nav-subitem-dot" />
                  {{ item.title }}
                </button>
                <span v-if="!sg.items || sg.items.length === 0" class="nav-empty-hint">준비 중</span>
              </div>
            </div>
          </div>
        </div>
      </nav>
    </aside>

    <!-- ══ CONTENT ══ -->
    <main class="content">
      <!-- 환영 배너 -->
      <div class="welcome-card">
        <div class="welcome-text">
          <ClientOnly>
            <h1>안녕하세요, <strong>{{ userName }}</strong>님</h1>
            <template #fallback><h1>안녕하세요!</h1></template>
          </ClientOnly>
          <p>NeoMES 제조실행시스템에 오신 것을 환영합니다.</p>
        </div>
        <div class="welcome-badge">MES</div>
      </div>

      <!-- 요약 카드 4개 -->
      <div class="summary-grid">
        <div class="summary-card" v-for="stat in summaryStats" :key="stat.id">
          <div class="summary-icon" :class="stat.colorClass">
            <component :is="stat.icon" />
          </div>
          <div class="summary-body">
            <span class="summary-label">{{ stat.label }}</span>
            <span class="summary-value" :class="stat.colorClass">{{ stat.value }}</span>
          </div>
        </div>
      </div>

      <!-- 빠른 메뉴 -->
      <div class="quick-section">
        <h2 class="quick-title">빠른 메뉴</h2>
        <div class="quick-grid">
          <button
            v-for="item in quickMenus"
            :key="item.id"
            class="quick-card"
            @click="navigate(item.path)"
          >
            <div class="quick-icon" :class="item.colorClass">
              <component :is="item.icon" />
            </div>
            <div class="quick-body">
              <span class="quick-name">{{ item.title }}</span>
              <span class="quick-desc">{{ item.description }}</span>
            </div>
            <svg class="quick-arrow" width="14" height="14" viewBox="0 0 16 16" fill="none">
              <path d="M6 4L10 8L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
      </div>
    </main>

    <!-- ══ FOOTER ══ -->
    <footer class="footer">
      <span class="footer-company">NOROO Information System Co., Ltd.</span>
      <span class="footer-sep">|</span>
      <span class="footer-copy">© {{ currentYear }} NeoMES. All rights reserved.</span>
      <span class="footer-sep">|</span>
      <span class="footer-version">v1.0.0</span>
    </footer>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive, onMounted, onUnmounted, defineComponent, h } from 'vue'
import { useTheme } from '~/composables/useTheme'

const router = useRouter()
const { theme, toggleTheme } = useTheme()

/* ── 사용자 정보 ── */
const userName = ref('')
const userId   = ref('')
const siteName = ref('')

const userInitial = computed(() =>
  userName.value ? userName.value.charAt(0).toUpperCase() : 'U'
)

/* ── 날짜·시간 ── */
const currentDate = ref('')
const currentTime = ref('')
const currentYear = new Date().getFullYear()
let timer: ReturnType<typeof setInterval>

function updateTime() {
  const now = new Date()
  const y  = now.getFullYear()
  const mo = String(now.getMonth() + 1).padStart(2, '0')
  const d  = String(now.getDate()).padStart(2, '0')
  const dw = ['일', '월', '화', '수', '목', '금', '토'][now.getDay()]
  currentDate.value = `${y}.${mo}.${d} (${dw})`
  const hh = String(now.getHours()).padStart(2, '0')
  const mm = String(now.getMinutes()).padStart(2, '0')
  const ss = String(now.getSeconds()).padStart(2, '0')
  currentTime.value = `${hh}:${mm}:${ss}`
}

onMounted(() => {
  // 테마 복원은 app.vue에서 전역 처리 — 여기서는 사용자 정보만 로드
  userName.value = localStorage.getItem('userName') || '사용자'
  userId.value   = localStorage.getItem('userId')   || ''
  siteName.value = localStorage.getItem('siteName') || localStorage.getItem('siteId') || ''
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => clearInterval(timer))

/* ── 로그아웃 ── */
function handleLogout() {
  localStorage.removeItem('authToken')
  localStorage.removeItem('userId')
  localStorage.removeItem('userName')
  localStorage.removeItem('siteId')
  localStorage.removeItem('siteName')
  const authCookie = useCookie('authToken')
  authCookie.value = null
  router.push('/login')
}

/* ── 페이지 이동 ── */
function navigate(path: string) {
  router.push(path)
}

/* ── SVG 아이콘 ── */
const makeIcon = (pathD: string) =>
  defineComponent({ render: () =>
    h('svg', { width: 18, height: 18, viewBox: '0 0 20 20', fill: 'none' },
      [h('path', { d: pathD, stroke: 'currentColor', 'stroke-width': '1.8', 'stroke-linecap': 'round', 'stroke-linejoin': 'round' })]
    )
  })

const IconProduction = makeIcon('M2 16L6 8L10 12L14 6L18 10')
const IconPlanning   = makeIcon('M3 5H17M3 10H17M3 15H11')
const IconQuality    = makeIcon('M10 2L12.5 7.5L18 8.5L14 12.5L15 18L10 15.5L5 18L6 12.5L2 8.5L7.5 7.5L10 2')
const IconEquipment  = makeIcon('M10 3V6M10 14V17M3 10H6M14 10H17M5.1 5.1L7.2 7.2M12.8 12.8L14.9 14.9')
const IconMaster     = makeIcon('M3 5H17M3 10H17M3 15H11')
const IconReport     = makeIcon('M4 2H16V18H4V2ZM7 7H13M7 11H13M7 15H10')
const IconMenu       = makeIcon('M3 6H17M3 11H17M3 16H13')
const IconSettings   = makeIcon('M10 13A3 3 0 1 0 10 7 3 3 0 0 0 10 13ZM10 2V4M10 16V18M2 10H4M16 10H18')

/* ── 사이드바 메뉴 데이터 ── */
const menuSections = reactive([
  {
    id: 'production', title: '생산 실행', badge: 'NGP',
    colorClass: 'color-blue', open: true, icon: IconProduction,
    items: [
      { id: 'lot',      title: 'LOT 관리',  path: '/lot' },
      { id: 'process',  title: '공정 실적', path: '/production' },
      { id: 'tracking', title: 'LOT 추적',  path: '/lot-tracking' },
    ]
  },
  {
    id: 'planning', title: '생산 계획', badge: 'PM',
    colorClass: 'color-green', open: false, icon: IconPlanning,
    items: [
      { id: 'plan',      title: '생산 계획',  path: '/plan' },
      { id: 'workorder', title: '작업 지시',  path: '/workorder' },
      { id: 'order',     title: '주문 관리',  path: '/order' },
    ]
  },
  {
    id: 'quality', title: '품질 관리', badge: 'QTM',
    colorClass: 'color-yellow', open: false, icon: IconQuality,
    items: [
      { id: 'qcheck',  title: '품질 검사',    path: '/quality' },
      { id: 'defect',  title: '불량 관리',    path: '/defect' },
      { id: 'qtm',     title: 'QTM 모니터링', path: '/qtm' },
    ]
  },
  {
    id: 'equipment', title: '설비 관리', badge: 'EQP',
    colorClass: 'color-purple', open: false, icon: IconEquipment,
    items: [
      { id: 'eqp-status', title: '설비 현황',  path: '/equipment' },
      { id: 'eqp-event',  title: '설비 이벤트', path: '/eqp-event' },
      { id: 'eqp-maint',  title: '설비 보전',  path: '/eqp-maintenance' },
    ]
  },
  {
    id: 'master', title: '마스터 데이터', badge: 'CMN',
    colorClass: 'color-cyan', open: false, icon: IconMaster,
    items: [
      { id: 'material', title: '자재 마스터', path: '/master' },
      { id: 'shift',    title: '교대 관리',   path: '/shift' },
      { id: 'user',     title: '사용자 관리', path: '/user-management' },
    ]
  },
  {
    id: 'report', title: '리포트 / 시스템', badge: 'RPT',
    colorClass: 'color-red', open: false, icon: IconReport,
    items: [
      { id: 'rpt-prod',     title: '생산 실적 리포트', path: '/report-production' },
      { id: 'rpt-qual',     title: '품질 분석 리포트', path: '/report-quality' },
      { id: 'menu-mgt',     title: '메뉴 관리',        path: '/menu-management' },
      { id: 'sys-settings', title: '시스템 설정',       path: '/settings' },
    ]
  },
  {
    id: 'settings', title: '설정', badge: 'CFG',
    colorClass: 'color-gray', open: false, icon: IconSettings,
    subgroups: [
      {
        id: 'sg-basic', title: '기본 설정', open: false,
        items: [
          { id: 'sg-user',       title: '사용자 설정',              path: '/settings/basic/user' },
          { id: 'sg-perm',       title: '권한 설정',                path: '/settings/basic/permission' },
          { id: 'sg-func',       title: '기능 설정',                path: '/settings/basic/function' },
          { id: 'sg-fav',        title: '즐겨찾기 설정',            path: '/settings/basic/favorites' },
          { id: 'sg-pwd',        title: 'Password Security Policy', path: '/settings/basic/password-policy' },
          { id: 'sg-code',       title: '코드관리',                 path: '/settings/basic/code' },
          { id: 'sg-msg',        title: '메시지 설정',              path: '/settings/basic/message' },
          { id: 'sg-svc',        title: 'Service Member Setup',     path: '/settings/basic/service-member' },
          { id: 'sg-board',      title: '게시판 관리',              path: '/settings/basic/board' },
          { id: 'sg-help',       title: 'Help Setup',               path: '/settings/basic/help' },
          { id: 'sg-dept',       title: '부서 설정',                path: '/settings/basic/department' },
          { id: 'sg-cust',       title: '고객사 설정',              path: '/settings/basic/customer' },
          { id: 'sg-vendor',     title: 'Vendor Setup',             path: '/settings/basic/vendor' },
          { id: 'sg-idrule',     title: 'ID Generation Rule Setup', path: '/settings/basic/id-rule' },
          { id: 'sg-query',      title: 'Query',                    path: '/settings/basic/query' },
          { id: 'sg-manager',    title: 'Manager',                  path: '/settings/basic/manager' },
          { id: 'sg-ftp',        title: 'FTP 서버 설정',            path: '/settings/basic/ftp' },
        ]
      },
      { id: 'sg-process',   title: '공정 관리', open: false, items: [] },
      { id: 'sg-document',  title: '문서 제어', open: false, items: [] },
      { id: 'sg-equipment', title: '설비 관리', open: false, items: [] },
      { id: 'sg-data',      title: '데이터 관리', open: false, items: [] },
    ]
  },
])

/* ── 요약 통계 카드 ── */
const summaryStats = [
  { id: 1, label: '오늘 생산 LOT', value: '–',  colorClass: 'color-blue',   icon: IconProduction },
  { id: 2, label: '진행 중 공정',  value: '–',  colorClass: 'color-green',  icon: IconPlanning   },
  { id: 3, label: '품질 이슈',     value: '–',  colorClass: 'color-yellow', icon: IconQuality    },
  { id: 4, label: '설비 알람',     value: '–',  colorClass: 'color-red',    icon: IconEquipment  },
]

/* ── 빠른 메뉴 ── */
const quickMenus = [
  {
    id: 'menu-mgt', title: '메뉴 관리',
    description: '메뉴 CRUD 및 권한 설정',
    path: '/menu-management', colorClass: 'color-blue', icon: IconMenu
  },
]
</script>

<style scoped>
/* ════════════════════════════════════
   CSS Grid 4-Zone 레이아웃
════════════════════════════════════ */
.page-layout {
  display: grid;
  grid-template-rows: 56px 1fr 40px;
  grid-template-columns: 220px 1fr;
  grid-template-areas:
    "top     top"
    "sidebar content"
    "footer  footer";
  height: 100vh;
  overflow: hidden;
  background: var(--page-bg);
  color: var(--text-primary);
  font-family: 'Pretendard', 'Noto Sans KR', -apple-system, sans-serif;
  transition: background 0.25s ease, color 0.25s ease;
}

/* ══ TOP BAR ══ */
.top-bar {
  grid-area: top;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px 0 24px;
  background: var(--top-bg);
  border-bottom: 1px solid var(--top-border);
  z-index: 50;
  transition: background 0.25s ease, border-color 0.25s ease;
}

.top-left {
  display: flex;
  align-items: center;
  gap: 14px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 9px;
}

.logo-text {
  font-size: 17px;
  font-weight: 700;
  background: linear-gradient(135deg, #6c8fff, #a78bfa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 0.5px;
}

.site-badge {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 4px 11px;
  background: rgba(108, 143, 255, 0.1);
  border: 1px solid rgba(108, 143, 255, 0.22);
  border-radius: 20px;
  font-size: 11px;
  font-weight: 500;
  color: #8ba4ff;
  letter-spacing: 0.3px;
}

.top-right {
  display: flex;
  align-items: center;
  gap: 14px;
}

/* ── 테마 토글 스위치 ── */
.theme-switch {
  position: relative;
  width: 48px;
  height: 26px;
  padding: 0;
  border: none;
  background: none;
  cursor: pointer;
  flex-shrink: 0;
}

.switch-track {
  display: block;
  width: 100%;
  height: 100%;
  border-radius: 13px;
  background: rgba(30, 40, 90, 0.6);
  border: 1px solid var(--card-border);
  position: relative;
  transition: background 0.35s ease, border-color 0.35s ease;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.25);
}

.theme-switch.is-light .switch-track {
  background: rgba(255, 185, 30, 0.18);
  border-color: rgba(220, 155, 0, 0.38);
}

.switch-knob {
  position: absolute;
  top: 3px;
  left: 3px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #6c8fff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: left 0.35s cubic-bezier(0.34, 1.56, 0.64, 1), background 0.35s ease;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.35);
}

.theme-switch.is-light .switch-knob {
  left: calc(100% - 21px);
  background: #f5a623;
}

/* 날짜·시간 */
.top-datetime {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.datetime-date {
  color: var(--text-secondary);
}

.datetime-sep {
  color: var(--text-dim);
}

.datetime-time {
  font-size: 14px;
  font-weight: 700;
  color: var(--accent);
  font-variant-numeric: tabular-nums;
  letter-spacing: 1px;
}

/* 사용자 */
.top-user {
  display: flex;
  align-items: center;
  gap: 9px;
}

.user-avatar {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #6c8fff, #a78bfa);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  color: #fff;
  flex-shrink: 0;
}

.user-detail {
  display: flex;
  flex-direction: column;
  line-height: 1.3;
}

.user-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
}

.user-id {
  font-size: 11px;
  color: var(--text-muted);
}

/* 로그아웃 */
.btn-logout {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 6px 12px;
  background: rgba(255, 99, 99, 0.08);
  border: 1px solid rgba(255, 99, 99, 0.2);
  border-radius: 6px;
  color: #ff8080;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.2s, border-color 0.2s;
}

.btn-logout:hover {
  background: rgba(255, 99, 99, 0.18);
  border-color: rgba(255, 99, 99, 0.4);
}

/* ══ SIDEBAR ══ */
.sidebar {
  grid-area: sidebar;
  background: var(--sidebar-bg);
  border-right: 1px solid var(--sidebar-border);
  overflow-y: auto;
  overflow-x: hidden;
  transition: background 0.25s ease, border-color 0.25s ease;
}

.sidebar-nav {
  padding: 12px 0 20px;
}

/* 섹션 헤더 */
.nav-section-header {
  display: flex;
  align-items: center;
  gap: 9px;
  width: 100%;
  padding: 9px 16px;
  background: none;
  border: none;
  color: var(--nav-text-color);
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s, background 0.2s;
  text-align: left;
  letter-spacing: 0.3px;
}

.nav-section-header:hover,
.nav-section-header.open {
  color: var(--nav-text-open);
  background: var(--nav-hover-bg);
}

.nav-section-icon {
  width: 26px;
  height: 26px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.nav-section-title {
  flex: 1;
  min-width: 0;
}

.nav-section-badge {
  font-size: 9px;
  padding: 1px 5px;
  background: var(--nav-badge-bg);
  border: 1px solid var(--nav-badge-border);
  border-radius: 8px;
  color: var(--nav-badge-color);
  letter-spacing: 0.5px;
}

.nav-chevron {
  color: var(--nav-dot-color);
  flex-shrink: 0;
  transition: transform 0.25s ease;
}

.nav-chevron.rotated {
  transform: rotate(180deg);
}

/* 섹션 하위 아이템 */
.nav-items {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.28s ease;
}

.nav-items.visible {
  max-height: 300px;
}

/* 서브그룹이 있는 섹션은 max-height를 크게 확보 */
.nav-items--tall.visible {
  max-height: 1200px;
}

/* ── 서브그룹 헤더 ── */
.nav-subgroup-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 7px 16px 7px 26px;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--nav-item-color);
  font-size: 11.5px;
  font-weight: 500;
  font-family: inherit;
  transition: color 0.2s, background 0.2s;
  text-align: left;
}

.nav-subgroup-header:hover {
  color: var(--nav-text-open);
  background: var(--nav-item-hover);
}

.nav-subgroup-title {
  flex: 1;
}

.nav-chevron--sm {
  flex-shrink: 0;
  color: var(--nav-dot-color);
  transition: transform 0.2s ease;
}

/* ── 서브그룹 아이템 영역 ── */
.nav-subitems {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.25s ease;
}

.nav-subitems.visible {
  max-height: 800px;
}

/* ── 서브그룹 리프 아이템 ── */
.nav-subitem {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding: 5px 16px 5px 38px;
  background: none;
  border: none;
  color: var(--nav-item-color);
  font-size: 11px;
  cursor: pointer;
  transition: color 0.2s, background 0.2s;
  text-align: left;
  font-family: inherit;
  opacity: 0.85;
}

.nav-subitem:hover {
  color: var(--nav-text-open);
  background: var(--nav-item-hover);
  opacity: 1;
}

.nav-subitem-dot {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: currentColor;
  flex-shrink: 0;
  opacity: 0.5;
}

/* 준비 중 힌트 */
.nav-empty-hint {
  display: block;
  padding: 5px 16px 5px 38px;
  font-size: 11px;
  color: var(--nav-item-color);
  opacity: 0.4;
  font-style: italic;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 7px 16px 7px 28px;
  background: none;
  border: none;
  color: var(--nav-item-color);
  font-size: 12px;
  cursor: pointer;
  transition: color 0.2s, background 0.2s;
  text-align: left;
}

.nav-item:hover {
  color: var(--nav-text-open);
  background: var(--nav-item-hover);
}

.nav-item-dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  flex-shrink: 0;
  opacity: 0.5;
  transition: opacity 0.2s;
}

.nav-item:hover .nav-item-dot {
  opacity: 1;
}

.nav-item-label {
  flex: 1;
}

/* 섹션 색상 */
.color-blue   { background: rgba(108, 143, 255, 0.14); color: #6c8fff; }
.color-green  { background: rgba(52,  211, 153, 0.14); color: #34d399; }
.color-yellow { background: rgba(251, 191,  36, 0.14); color: #fbbf24; }
.color-purple { background: rgba(167, 139, 250, 0.14); color: #a78bfa; }
.color-cyan   { background: rgba(56,  189, 248, 0.14); color: #38bdf8; }
.color-red    { background: rgba(251, 113, 133, 0.14); color: #fb7185; }
.color-gray   { background: rgba(140, 140, 140, 0.14); color: #8c8c8c; }

.nav-item-dot.color-blue   { background: #6c8fff; }
.nav-item-dot.color-green  { background: #34d399; }
.nav-item-dot.color-yellow { background: #fbbf24; }
.nav-item-dot.color-purple { background: #a78bfa; }
.nav-item-dot.color-cyan   { background: #38bdf8; }
.nav-item-dot.color-red    { background: #fb7185; }

/* ══ CONTENT ══ */
.content {
  grid-area: content;
  overflow-y: auto;
  padding: 24px 28px 28px;
  background: var(--page-bg);
  transition: background 0.25s ease;
}

/* 환영 카드 */
.welcome-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, rgba(108, 143, 255, 0.1) 0%, rgba(167, 139, 250, 0.07) 100%);
  border: 1px solid rgba(108, 143, 255, 0.18);
  border-radius: 12px;
  padding: 20px 28px;
  margin-bottom: 22px;
}

.welcome-text h1 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 5px;
}

.welcome-text h1 strong {
  color: #8ba4ff;
}

.welcome-text p {
  font-size: 12px;
  color: var(--text-muted);
  margin: 0;
}

.welcome-badge {
  width: 52px;
  height: 52px;
  background: linear-gradient(135deg, #6c8fff, #a78bfa);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 800;
  color: #fff;
  letter-spacing: 1px;
  box-shadow: 0 6px 20px rgba(108, 143, 255, 0.35);
  flex-shrink: 0;
}

/* 요약 카드 그리드 */
.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
  margin-bottom: 26px;
}

.summary-card {
  display: flex;
  align-items: center;
  gap: 14px;
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 10px;
  padding: 16px 18px;
  transition: background 0.25s ease, border-color 0.25s ease;
}

.summary-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.summary-body {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.summary-label {
  font-size: 11px;
  color: var(--text-muted);
}

.summary-value {
  font-size: 22px;
  font-weight: 700;
  font-variant-numeric: tabular-nums;
}

/* 빠른 메뉴 */
.quick-section {
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  border-radius: 12px;
  padding: 20px 22px;
  transition: background 0.25s ease, border-color 0.25s ease;
}

.quick-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
  margin: 0 0 14px;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 10px;
}

.quick-card {
  display: flex;
  align-items: center;
  gap: 13px;
  padding: 14px 16px;
  background: var(--quick-card-bg, rgba(255, 255, 255, 0.025));
  border: 1px solid var(--card-border);
  border-radius: 9px;
  cursor: pointer;
  transition: border-color 0.2s, background 0.2s, transform 0.15s;
  text-align: left;
  width: 100%;
}

.quick-card:hover {
  border-color: var(--card-hover-border);
  background: var(--card-hover-bg);
  transform: translateY(-1px);
}

.quick-icon {
  width: 38px;
  height: 38px;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.quick-body {
  flex: 1;
  min-width: 0;
}

.quick-name {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.quick-desc {
  display: block;
  font-size: 11px;
  color: var(--text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.quick-arrow {
  color: var(--text-dim);
  flex-shrink: 0;
  transition: color 0.2s, transform 0.2s;
}

.quick-card:hover .quick-arrow {
  color: var(--accent);
  transform: translateX(2px);
}

/* ══ FOOTER ══ */
.footer {
  grid-area: footer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: var(--footer-bg);
  border-top: 1px solid var(--footer-border);
  font-size: 11px;
  color: var(--text-dim);
  padding: 0 24px;
  transition: background 0.25s ease, border-color 0.25s ease;
}

.footer-company { color: var(--text-muted); }
.footer-sep     { color: var(--text-dim); }
.footer-copy    { color: var(--text-dim); }
.footer-version { color: var(--text-dim); }

/* ══ 반응형 ══ */
@media (max-width: 900px) {
  .page-layout {
    grid-template-columns: 180px 1fr;
  }
  .summary-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .page-layout {
    grid-template-rows: 56px 1fr 40px;
    grid-template-columns: 1fr;
    grid-template-areas:
      "top"
      "content"
      "footer";
  }
  .sidebar      { display: none; }
  .top-datetime,
  .user-detail  { display: none; }
  .summary-grid { grid-template-columns: repeat(2, 1fr); }
  .content      { padding: 16px; }
  .theme-switch { display: none; }
}
</style>
