<template>
  <div id="app">
    <!-- 자체 레이아웃을 가진 페이지(메인, 로그인)는 AppLayout 없이 렌더링 -->
    <AppLayout v-if="showAppLayout">
      <NuxtPage />
    </AppLayout>
    <NuxtPage v-else />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'

const route = useRoute()
const isAuthenticated = ref(false)

// 자체 레이아웃을 포함한 페이지 — AppLayout 미사용
const standalonePages = ['/', '/login']

const showAppLayout = computed(() =>
  isAuthenticated.value && !standalonePages.includes(route.path)
)

function checkAuth() {
  // localStorage는 클라이언트에서만 접근 가능
  if (!import.meta.client) return
  isAuthenticated.value = route.path !== '/login'
    ? !!localStorage.getItem('authToken')
    : false
}

onMounted(() => {
  checkAuth()
})

watch(() => route.path, checkAuth)
</script>

<style>
/* 전역 스타일은 assets/css/main.css에서 관리 (테마 CSS 변수 포함) */
</style>
