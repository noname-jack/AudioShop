<template>
  <div id="content">
    <!-- Хлебные крошки -->
    <nav style="--bs-breadcrumb-divider: '›';" aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <router-link
              :to="{ name: 'Home' }"
              title="Главная"
          >
            Главная
          </router-link>
        </li>
        <li class="breadcrumb-item active" aria-current="page">Управление категориями</li>
      </ol>
    </nav>
  </div>


</template>

<script setup lang="ts">

</script>

<style scoped>
</style>