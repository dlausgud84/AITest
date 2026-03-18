<template>
  <div id="app">
    <AppLayout v-if="isAuthenticated">
      <slot />
    </AppLayout>
    <NuxtPage v-else />
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isAuthenticated = ref(false)

// 인증 상태 확인
watch(() => router.currentRoute.value.path, () => {
  // 로그인 페이지가 아닌 경우 인증 체크
  if (router.currentRoute.value.path !== '/login') {
    const token = localStorage.getItem('authToken')
    isAuthenticated.value = !!token
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  background-color: #f5f5f5;
}
</style>
