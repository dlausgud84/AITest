<template>
  <div class="main-layout">
    <!-- 상단 헤더 -->
    <header class="top-header">
      <div class="header-left">
        <div class="logo">
          <svg width="28" height="28" viewBox="0 0 28 28" fill="none">
            <rect x="2" y="2" width="10" height="10" rx="2" fill="#6c8fff"/>
            <rect x="16" y="2" width="10" height="10" rx="2" fill="#6c8fff" opacity="0.6"/>
            <rect x="2" y="16" width="10" height="10" rx="2" fill="#6c8fff" opacity="0.6"/>
            <rect x="16" y="16" width="10" height="10" rx="2" fill="#6c8fff"/>
          </svg>
          <span class="logo-text">NeoMES</span>
        </div>
        <div class="site-badge" v-if="siteName">
          <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
            <path d="M6 1L7.5 4.5H11L8.25 6.75L9.25 10.5L6 8.25L2.75 10.5L3.75 6.75L1 4.5H4.5L6 1Z" fill="#6c8fff"/>
          </svg>
          {{ siteName }}
        </div>
      </div>
      <div class="header-right">
        <ClientOnly>
          <div class="user-info">
            <div class="user-avatar">{{ userInitial }}</div>
            <div class="user-detail">
              <span class="user-name">{{ userName }}</span>
              <span class="user-id">{{ userId }}</span>
            </div>
          </div>
        </ClientOnly>
        <button class="logout-btn" @click="handleLogout">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M6 14H3C2.45 14 2 13.55 2 13V3C2 2.45 2.45 2 3 2H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
            <path d="M10 11L14 8L10 5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M14 8H6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
          로그아웃
        </button>
      </div>
    </header>

    <!-- 메인 콘텐츠 -->
    <main class="main-content">
      <!-- 환영 배너 -->
      <div class="welcome-banner">
        <div class="banner-text">
          <ClientOnly>
            <h1>안녕하세요, <strong>{{ userName }}</strong>님</h1>
            <template #fallback><h1>안녕하세요!</h1></template>
          </ClientOnly>
          <p>NeoMES 제조실행시스템에 오신 것을 환영합니다.</p>
        </div>
        <ClientOnly>
          <div class="banner-stats">
            <div class="stat-item">
              <span class="stat-value">{{ currentDate }}</span>
              <span class="stat-label">오늘 날짜</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-value">{{ currentTime }}</span>
              <span class="stat-label">현재 시간</span>
            </div>
          </div>
        </ClientOnly>
      </div>

      <!-- 메뉴 섹션 -->
      <div class="menu-sections">
        <!-- 생산 실행 -->
        <div class="menu-section">
          <div class="section-header">
            <div class="section-icon production">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <path d="M2 16L6 8L10 12L14 6L18 10" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <h2 class="section-title">생산 실행</h2>
            <span class="section-badge">NGP</span>
          </div>
          <div class="menu-cards">
            <div class="menu-card" v-for="item in productionMenus" :key="item.id" @click="navigate(item.path)">
              <div class="card-icon" :class="item.iconClass">
                <component :is="item.icon" />
              </div>
              <div class="card-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
              </div>
              <div class="card-arrow">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M6 4L10 8L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- 생산 계획 -->
        <div class="menu-section">
          <div class="section-header">
            <div class="section-icon planning">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <rect x="3" y="3" width="14" height="14" rx="2" stroke="currentColor" stroke-width="1.8"/>
                <path d="M7 10H13M7 7H10M7 13H11" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
              </svg>
            </div>
            <h2 class="section-title">생산 계획</h2>
            <span class="section-badge">PM</span>
          </div>
          <div class="menu-cards">
            <div class="menu-card" v-for="item in planningMenus" :key="item.id" @click="navigate(item.path)">
              <div class="card-icon" :class="item.iconClass">
                <component :is="item.icon" />
              </div>
              <div class="card-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
              </div>
              <div class="card-arrow">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M6 4L10 8L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- 품질 관리 -->
        <div class="menu-section">
          <div class="section-header">
            <div class="section-icon quality">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <path d="M10 2L12.5 7.5L18 8.5L14 12.5L15 18L10 15.5L5 18L6 12.5L2 8.5L7.5 7.5L10 2Z" stroke="currentColor" stroke-width="1.8" stroke-linejoin="round"/>
              </svg>
            </div>
            <h2 class="section-title">품질 관리</h2>
            <span class="section-badge">QTM</span>
          </div>
          <div class="menu-cards">
            <div class="menu-card" v-for="item in qualityMenus" :key="item.id" @click="navigate(item.path)">
              <div class="card-icon" :class="item.iconClass">
                <component :is="item.icon" />
              </div>
              <div class="card-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
              </div>
              <div class="card-arrow">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M6 4L10 8L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- 설비 관리 -->
        <div class="menu-section">
          <div class="section-header">
            <div class="section-icon equipment">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <circle cx="10" cy="10" r="3" stroke="currentColor" stroke-width="1.8"/>
                <path d="M10 2V5M10 15V18M2 10H5M15 10H18M4.1 4.1L6.2 6.2M13.8 13.8L15.9 15.9M15.9 4.1L13.8 6.2M6.2 13.8L4.1 15.9" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
              </svg>
            </div>
            <h2 class="section-title">설비 관리</h2>
            <span class="section-badge">EQP</span>
          </div>
          <div class="menu-cards">
            <div class="menu-card" v-for="item in equipmentMenus" :key="item.id" @click="navigate(item.path)">
              <div class="card-icon" :class="item.iconClass">
                <component :is="item.icon" />
              </div>
              <div class="card-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
              </div>
              <div class="card-arrow">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M6 4L10 8L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- 공통/마스터 -->
        <div class="menu-section">
          <div class="section-header">
            <div class="section-icon master">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <path d="M3 5H17M3 10H17M3 15H11" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
              </svg>
            </div>
            <h2 class="section-title">마스터 데이터</h2>
            <span class="section-badge">CMN</span>
          </div>
          <div class="menu-cards">
            <div class="menu-card" v-for="item in masterMenus" :key="item.id" @click="navigate(item.path)">
              <div class="card-icon" :class="item.iconClass">
                <component :is="item.icon" />
              </div>
              <div class="card-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
              </div>
              <div class="card-arrow">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M6 4L10 8L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- 리포트/시스템 -->
        <div class="menu-section">
          <div class="section-header">
            <div class="section-icon report">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <path d="M4 2H16V18H4V2Z" stroke="currentColor" stroke-width="1.8" stroke-linejoin="round"/>
                <path d="M7 7H13M7 10H13M7 13H10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
              </svg>
            </div>
            <h2 class="section-title">리포트 / 시스템</h2>
            <span class="section-badge">RPT</span>
          </div>
          <div class="menu-cards">
            <div class="menu-card" v-for="item in reportMenus" :key="item.id" @click="navigate(item.path)">
              <div class="card-icon" :class="item.iconClass">
                <component :is="item.icon" />
              </div>
              <div class="card-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
              </div>
              <div class="card-arrow">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M6 4L10 8L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, defineComponent, h } from 'vue'

const router = useRouter()

// 사용자 정보 (초기값 빈값 → onMounted에서 localStorage 읽음)
const userName = ref('')
const userId = ref('')
const siteName = ref('')

// 현재 시간
const currentDate = ref('')
const currentTime = ref('')
let timer: ReturnType<typeof setInterval>

const userInitial = computed(() => {
  return userName.value ? userName.value.charAt(0) : 'U'
})

function updateTime() {
  const now = new Date()
  const y = now.getFullYear()
  const m = String(now.getMonth() + 1).padStart(2, '0')
  const d = String(now.getDate()).padStart(2, '0')
  currentDate.value = `${y}.${m}.${d}`
  const hh = String(now.getHours()).padStart(2, '0')
  const mm = String(now.getMinutes()).padStart(2, '0')
  const ss = String(now.getSeconds()).padStart(2, '0')
  currentTime.value = `${hh}:${mm}:${ss}`
}

onMounted(() => {
  userName.value = localStorage.getItem('userName') || localStorage.getItem('username') || '사용자'
  userId.value = localStorage.getItem('userId') || ''
  siteName.value = localStorage.getItem('siteName') || localStorage.getItem('siteId') || ''
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})

// 로그아웃
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

// 페이지 이동
function navigate(path: string) {
  router.push(path)
}

// SVG 아이콘 컴포넌트
const LotIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('rect', { x: 2, y: 4, width: 18, height: 14, rx: 2, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M7 11H15M7 8H11M7 14H13', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

const ProcessIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('circle', { cx: 5, cy: 11, r: 3, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('circle', { cx: 17, cy: 11, r: 3, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M8 11H14', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

const TrackingIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('path', { d: 'M3 18L7 10L11 14L15 7L19 11', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linecap': 'round', 'stroke-linejoin': 'round' }),
    h('circle', { cx: 19, cy: 11, r: 2, fill: 'currentColor' })
  ])
})

const OrderIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('path', { d: 'M4 3H18L20 7H2L4 3Z', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linejoin': 'round' }),
    h('rect', { x: 2, y: 7, width: 18, height: 12, rx: 1, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M9 7V19', stroke: 'currentColor', 'stroke-width': 1.5 })
  ])
})

const WorkOrderIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('rect', { x: 3, y: 2, width: 16, height: 18, rx: 2, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M7 7H15M7 11H13M7 15H10', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' }),
    h('path', { d: 'M13 14L15 16L19 12', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round', 'stroke-linejoin': 'round' })
  ])
})

const PlanIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('rect', { x: 3, y: 4, width: 16, height: 15, rx: 2, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M7 2V6M15 2V6M3 9H19', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' }),
    h('path', { d: 'M7 13H10M7 16H13', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

const QualityCheckIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('path', { d: 'M11 2L13.5 7.5L19 8.5L15 12.5L16 19L11 16.5L6 19L7 12.5L3 8.5L8.5 7.5L11 2Z', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linejoin': 'round' })
  ])
})

const InspectionIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('circle', { cx: 10, cy: 10, r: 6, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M14.5 14.5L19 19', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linecap': 'round' }),
    h('path', { d: 'M8 10L10 12L13 8', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round', 'stroke-linejoin': 'round' })
  ])
})

const MonitorIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('rect', { x: 2, y: 3, width: 18, height: 13, rx: 2, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M8 19H14M11 16V19', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' }),
    h('path', { d: 'M6 12L8 9L10 11L13 7L16 10', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round', 'stroke-linejoin': 'round' })
  ])
})

const EqpStatusIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('circle', { cx: 11, cy: 11, r: 4, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M11 3V6M11 16V19M3 11H6M16 11H19M5.1 5.1L7.2 7.2M14.8 14.8L16.9 16.9M16.9 5.1L14.8 7.2M7.2 14.8L5.1 16.9', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

const EqpEventIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('path', { d: 'M11 4V11L15 13', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linecap': 'round', 'stroke-linejoin': 'round' }),
    h('circle', { cx: 11, cy: 11, r: 8, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('circle', { cx: 17, cy: 5, r: 3, fill: '#ff6b6b' })
  ])
})

const EqpMaintIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('path', { d: 'M14.5 3.5C15.3 4.3 15.3 5.7 14.5 6.5L6.5 14.5C5.7 15.3 4.3 15.3 3.5 14.5C2.7 13.7 2.7 12.3 3.5 11.5L11.5 3.5C12.3 2.7 13.7 2.7 14.5 3.5Z', stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M16 6L19 3M14 16C14 18.2 15.8 20 18 20C18 17.8 16.2 16 14 16Z', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

const MaterialIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('path', { d: 'M11 2L20 7V15L11 20L2 15V7L11 2Z', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linejoin': 'round' }),
    h('path', { d: 'M11 2V20M2 7L11 12L20 7', stroke: 'currentColor', 'stroke-width': 1.5 })
  ])
})

const ShiftIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('circle', { cx: 11, cy: 11, r: 8, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M11 6V11L14 14', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linecap': 'round', 'stroke-linejoin': 'round' }),
    h('path', { d: 'M5 2L2 5M17 2L20 5', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

const UserMgmtIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('circle', { cx: 9, cy: 7, r: 4, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M2 19C2 15.7 5.1 13 9 13', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linecap': 'round' }),
    h('path', { d: 'M16 14L18 16L21 12', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round', 'stroke-linejoin': 'round' }),
    h('circle', { cx: 18, cy: 16, r: 4, stroke: 'currentColor', 'stroke-width': 1.5 })
  ])
})

const ReportIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('rect', { x: 4, y: 2, width: 14, height: 18, rx: 2, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('path', { d: 'M8 7H14M8 11H14M8 15H11', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

const DashboardIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('rect', { x: 2, y: 2, width: 8, height: 8, rx: 1, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('rect', { x: 12, y: 2, width: 8, height: 8, rx: 1, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('rect', { x: 2, y: 12, width: 8, height: 8, rx: 1, stroke: 'currentColor', 'stroke-width': 1.8 }),
    h('rect', { x: 12, y: 12, width: 8, height: 8, rx: 1, stroke: 'currentColor', 'stroke-width': 1.8 })
  ])
})

const MenuMgmtIcon = defineComponent({
  render: () => h('svg', { width: 22, height: 22, viewBox: '0 0 22 22', fill: 'none' }, [
    h('path', { d: 'M3 6H19M3 11H19M3 16H13', stroke: 'currentColor', 'stroke-width': 1.8, 'stroke-linecap': 'round' }),
    h('circle', { cx: 17, cy: 16, r: 3, stroke: 'currentColor', 'stroke-width': 1.5 }),
    h('path', { d: 'M19.5 18.5L21 20', stroke: 'currentColor', 'stroke-width': 1.5, 'stroke-linecap': 'round' })
  ])
})

// 메뉴 데이터
const productionMenus = [
  {
    id: 'lot',
    title: 'LOT 관리',
    description: 'LOT 생성, 진행, 완료 현황 관리',
    path: '/lot',
    iconClass: 'icon-blue',
    icon: LotIcon
  },
  {
    id: 'process',
    title: '공정 실적',
    description: '공정별 생산 실적 등록 및 조회',
    path: '/production',
    iconClass: 'icon-cyan',
    icon: ProcessIcon
  },
  {
    id: 'tracking',
    title: 'LOT 추적',
    description: 'LOT 이력 및 이동 경로 추적',
    path: '/lot-tracking',
    iconClass: 'icon-purple',
    icon: TrackingIcon
  }
]

const planningMenus = [
  {
    id: 'plan',
    title: '생산 계획',
    description: '월/일별 생산 계획 수립 및 관리',
    path: '/plan',
    iconClass: 'icon-green',
    icon: PlanIcon
  },
  {
    id: 'workorder',
    title: '작업 지시',
    description: '작업지시 발행 및 현황 관리',
    path: '/workorder',
    iconClass: 'icon-orange',
    icon: WorkOrderIcon
  },
  {
    id: 'order',
    title: '주문 관리',
    description: '고객 주문 등록 및 진행 현황',
    path: '/order',
    iconClass: 'icon-yellow',
    icon: OrderIcon
  }
]

const qualityMenus = [
  {
    id: 'quality-check',
    title: '품질 검사',
    description: '수입/공정/출하 검사 결과 관리',
    path: '/quality',
    iconClass: 'icon-red',
    icon: QualityCheckIcon
  },
  {
    id: 'inspection',
    title: '불량 관리',
    description: '불량 유형별 현황 및 원인 분석',
    path: '/defect',
    iconClass: 'icon-pink',
    icon: InspectionIcon
  },
  {
    id: 'qtm-monitor',
    title: 'QTM 모니터링',
    description: '실시간 품질 지표 모니터링',
    path: '/qtm',
    iconClass: 'icon-indigo',
    icon: MonitorIcon
  }
]

const equipmentMenus = [
  {
    id: 'eqp-status',
    title: '설비 현황',
    description: '설비 가동/비가동 상태 조회',
    path: '/equipment',
    iconClass: 'icon-teal',
    icon: EqpStatusIcon
  },
  {
    id: 'eqp-event',
    title: '설비 이벤트',
    description: '설비 알람 및 이벤트 이력 조회',
    path: '/eqp-event',
    iconClass: 'icon-amber',
    icon: EqpEventIcon
  },
  {
    id: 'eqp-maint',
    title: '설비 보전',
    description: '예방/사후 보전 계획 및 이력',
    path: '/eqp-maintenance',
    iconClass: 'icon-lime',
    icon: EqpMaintIcon
  }
]

const masterMenus = [
  {
    id: 'material',
    title: '자재 마스터',
    description: '원자재/반제품/완제품 마스터 관리',
    path: '/master',
    iconClass: 'icon-sky',
    icon: MaterialIcon
  },
  {
    id: 'shift',
    title: '교대 관리',
    description: '교대 조편성 및 근무 일정 관리',
    path: '/shift',
    iconClass: 'icon-violet',
    icon: ShiftIcon
  },
  {
    id: 'user-mgmt',
    title: '사용자 관리',
    description: '사용자 등록 및 권한 관리',
    path: '/user-management',
    iconClass: 'icon-rose',
    icon: UserMgmtIcon
  }
]

const reportMenus = [
  {
    id: 'production-report',
    title: '생산 실적 리포트',
    description: '일/주/월별 생산 실적 통계',
    path: '/report-production',
    iconClass: 'icon-blue',
    icon: ReportIcon
  },
  {
    id: 'quality-report',
    title: '품질 분석 리포트',
    description: '불량률, 수율, SPC 분석 리포트',
    path: '/report-quality',
    iconClass: 'icon-green',
    icon: DashboardIcon
  },
  {
    id: 'menu-management',
    title: '메뉴 관리',
    description: '시스템 메뉴 구성 및 권한 설정',
    path: '/menu-management',
    iconClass: 'icon-gray',
    icon: MenuMgmtIcon
  }
]
</script>

<style scoped>
/* ── 전체 레이아웃 ── */
.main-layout {
  min-height: 100vh;
  background: #0f1117;
  color: #e0e4ef;
  font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
}

/* ── 상단 헤더 ── */
.top-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
  height: 60px;
  background: rgba(18, 20, 30, 0.95);
  border-bottom: 1px solid rgba(108, 143, 255, 0.15);
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(10px);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-text {
  font-size: 18px;
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
  padding: 4px 10px;
  background: rgba(108, 143, 255, 0.12);
  border: 1px solid rgba(108, 143, 255, 0.25);
  border-radius: 20px;
  font-size: 12px;
  color: #8ba4ff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 34px;
  height: 34px;
  background: linear-gradient(135deg, #6c8fff, #a78bfa);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  color: white;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 13px;
  font-weight: 600;
  color: #e0e4ef;
  line-height: 1.3;
}

.user-id {
  font-size: 11px;
  color: #6b7280;
  line-height: 1.3;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 7px 14px;
  background: rgba(255, 99, 99, 0.1);
  border: 1px solid rgba(255, 99, 99, 0.2);
  border-radius: 6px;
  color: #ff8080;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: rgba(255, 99, 99, 0.2);
  border-color: rgba(255, 99, 99, 0.4);
}

/* ── 메인 콘텐츠 ── */
.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 28px 28px 48px;
}

/* ── 환영 배너 ── */
.welcome-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, rgba(108, 143, 255, 0.12) 0%, rgba(167, 139, 250, 0.08) 100%);
  border: 1px solid rgba(108, 143, 255, 0.2);
  border-radius: 14px;
  padding: 24px 32px;
  margin-bottom: 32px;
}

.banner-text h1 {
  font-size: 22px;
  font-weight: 600;
  color: #e0e4ef;
  margin: 0 0 6px;
}

.banner-text h1 strong {
  color: #8ba4ff;
}

.banner-text p {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
}

.banner-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
}

.stat-value {
  font-size: 18px;
  font-weight: 700;
  color: #8ba4ff;
  letter-spacing: 1px;
  font-variant-numeric: tabular-nums;
}

.stat-label {
  font-size: 11px;
  color: #4b5563;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: rgba(108, 143, 255, 0.2);
}

/* ── 메뉴 섹션 ── */
.menu-sections {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.menu-section {
  background: rgba(18, 20, 30, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 14px;
  padding: 20px 24px 24px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.section-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.section-icon.production { background: rgba(108, 143, 255, 0.15); color: #6c8fff; }
.section-icon.planning   { background: rgba(52, 211, 153, 0.15); color: #34d399; }
.section-icon.quality    { background: rgba(251, 191, 36, 0.15); color: #fbbf24; }
.section-icon.equipment  { background: rgba(167, 139, 250, 0.15); color: #a78bfa; }
.section-icon.master     { background: rgba(56, 189, 248, 0.15); color: #38bdf8; }
.section-icon.report     { background: rgba(251, 113, 133, 0.15); color: #fb7185; }

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #c8d0e7;
  margin: 0;
}

.section-badge {
  margin-left: 4px;
  padding: 2px 8px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  font-size: 10px;
  color: #4b5563;
  letter-spacing: 0.5px;
}

/* ── 메뉴 카드 ── */
.menu-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 12px;
}

.menu-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 18px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
}

.menu-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(108, 143, 255, 0.05), transparent);
  opacity: 0;
  transition: opacity 0.2s;
}

.menu-card:hover {
  border-color: rgba(108, 143, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
}

.menu-card:hover::before {
  opacity: 1;
}

.card-icon {
  width: 42px;
  height: 42px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-blue   { background: rgba(108, 143, 255, 0.15); color: #6c8fff; }
.icon-cyan   { background: rgba(34, 211, 238, 0.15);  color: #22d3ee; }
.icon-purple { background: rgba(167, 139, 250, 0.15); color: #a78bfa; }
.icon-green  { background: rgba(52, 211, 153, 0.15);  color: #34d399; }
.icon-orange { background: rgba(251, 146, 60, 0.15);  color: #fb923c; }
.icon-yellow { background: rgba(251, 191, 36, 0.15);  color: #fbbf24; }
.icon-red    { background: rgba(248, 113, 113, 0.15); color: #f87171; }
.icon-pink   { background: rgba(244, 114, 182, 0.15); color: #f472b6; }
.icon-indigo { background: rgba(99, 102, 241, 0.15);  color: #818cf8; }
.icon-teal   { background: rgba(45, 212, 191, 0.15);  color: #2dd4bf; }
.icon-amber  { background: rgba(245, 158, 11, 0.15);  color: #f59e0b; }
.icon-lime   { background: rgba(163, 230, 53, 0.15);  color: #a3e635; }
.icon-sky    { background: rgba(56, 189, 248, 0.15);  color: #38bdf8; }
.icon-violet { background: rgba(139, 92, 246, 0.15);  color: #8b5cf6; }
.icon-rose   { background: rgba(251, 113, 133, 0.15); color: #fb7185; }
.icon-gray   { background: rgba(107, 114, 128, 0.15); color: #9ca3af; }

.card-content {
  flex: 1;
  min-width: 0;
}

.card-content h3 {
  font-size: 13px;
  font-weight: 600;
  color: #c8d0e7;
  margin: 0 0 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-content p {
  font-size: 11px;
  color: #4b5563;
  margin: 0;
  line-height: 1.4;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-arrow {
  color: #374151;
  flex-shrink: 0;
  transition: color 0.2s, transform 0.2s;
}

.menu-card:hover .card-arrow {
  color: #6c8fff;
  transform: translateX(3px);
}

/* ── 반응형 ── */
@media (max-width: 768px) {
  .top-header {
    padding: 0 16px;
  }

  .user-detail {
    display: none;
  }

  .main-content {
    padding: 16px 16px 32px;
  }

  .welcome-banner {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
    padding: 20px;
  }

  .menu-cards {
    grid-template-columns: 1fr;
  }

  .banner-stats {
    align-self: stretch;
    justify-content: center;
  }
}
</style>
