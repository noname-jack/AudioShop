<template>
  <!-- Хлебные крошки -->
  <nav style="--bs-breadcrumb-divider: '›';" aria-label="breadcrumb" class="mb-4">
    <ol class="breadcrumb">
      <li class="breadcrumb-item">
        <router-link
            :to="{ name: 'Home' }"
            title="Главная"
        >
          Главная
        </router-link>

      </li>
      <li class="breadcrumb-item">
        <router-link
            :to="{ name: 'Products' }"
            title="Управление товарами"
        >
          Управление товарами
        </router-link>
      </li>
      <li class="breadcrumb-item active" aria-current="page">Редактировать товар</li>
    </ol>
  </nav>

  <ProductInfo :id="props.id" />

</template>
<script setup lang="ts">
import ProductInfo from "@/components/features/products/ProductInfo.vue";

interface Props {
  id: number
}

const props = defineProps<Props>()


</script>