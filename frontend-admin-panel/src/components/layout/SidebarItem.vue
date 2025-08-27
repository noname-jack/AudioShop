<script setup lang="ts">

interface Props {
  to: string
  text: string
  icon: string
}
const props = defineProps<Props>()
</script>

<template>
  <router-link :to="to" class="list-group-item list-group-item-action" active-class="active">
    <i :class="icon"></i>
    {{ text }}
  </router-link>
</template>

<style scoped>

</style>