<script setup lang="ts">
import SidebarItem from "@/components/layout/SidebarItem.vue";
const props = defineProps<{ collapsed: boolean }>()
</script>

<template>
  <div id="sidebar" class="bg-light"  :class="{ collapsed: props.collapsed }">
    <div class="list-group list-group-flush">

      <SidebarItem to="/products" text="Товары" icon="bi bi-box-seam me-2"/>
      <SidebarItem to="/categories" text="Категории" icon="bi bi-tags-fill me-2"/>
      <SidebarItem to="/attributes" text="Характеристики" icon="bi bi-sliders me-2" />

    </div>
  </div>
</template>

<style scoped>

</style>