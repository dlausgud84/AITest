<template>
  <div class="app-layout">
    <Sidebar />
    <div class="main-content">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: var(--page-bg);
  transition: background 0.25s ease;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  background: var(--page-bg);
  transition: background 0.25s ease;
}
</style>
