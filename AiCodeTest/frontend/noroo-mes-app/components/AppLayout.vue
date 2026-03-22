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
  min-width: 0;
  min-height: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--page-bg);
  transition: background 0.25s ease;
}
</style>
