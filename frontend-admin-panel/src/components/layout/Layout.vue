<script setup lang="ts">
import Sidebar from './Sidebar.vue'
import Header from "@/components/layout/Header.vue";
import { ref } from 'vue'


const isCollapsed = ref(false)
function toggleSidebar() {
  isCollapsed.value = !isCollapsed.value
}
</script>

<template>
  <Header @toggle-sidebar="toggleSidebar"></Header>
  <div class="d-flex">
    <Sidebar :collapsed="isCollapsed"/>
    <div id="content">
      <slot />
    </div>
  </div>
</template>

