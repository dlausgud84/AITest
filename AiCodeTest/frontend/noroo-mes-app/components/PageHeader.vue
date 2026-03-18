<template>
  <header class="page-header">
    <h1>{{ title }}</h1>
  </header>
</template>

<script setup lang="ts">
defineProps<{
  title: string
}>()
</script>

<style scoped>
.page-header {
  background: white;
  padding: 20px;
  border-bottom: 2px solid #eee;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
}
</style>
