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
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  background-color: #0f1117;
}
</style>
