<template>
  <Layout>
    <router-view />
  </Layout>
  <notifications />
</template>

<script setup lang="ts">

import './assets/styles/main.scss'
import Layout from "@/components/layout/Layout.vue";
import {Notifications} from "@kyvg/vue3-notification";

</script>

<style scoped>
</style>
