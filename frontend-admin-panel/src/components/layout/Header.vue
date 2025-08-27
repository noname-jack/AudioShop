<script setup lang="ts">
import { defineEmits } from 'vue'
const emit = defineEmits<{
  (e: 'toggle-sidebar'): void
}>()
</script>

<template>
  <nav id="header" class="navbar navbar-dark">
    <div class="container-fluid">
      <button class="btn btn-outline-light me-3" @click="emit('toggle-sidebar')" >
        <i class="bi bi-list fs-4"></i>
      </button>
      <div>
        <img class="logo-svg" src="../../assets/images/svg/logo.svg" alt="">
        <a class="navbar-brand fs-4" href="#">Администрирование системы</a>
      </div>
    </div>
  </nav>
</template>

<style scoped>

</style>