<script setup lang="ts">
import { ref } from 'vue';
import type { ProductFilterAdminRequest } from '@/types';
import ProductFilter from '@/components/features/products/ProductFilter.vue';
import ProductList from '@/components/features/products/ProductList.vue';

const filter = ref<ProductFilterAdminRequest>({
  categoryId: null,
  brandIds: [],
  active: null,
  name: '',
});

function handleApplyFilters(newFilter: ProductFilterAdminRequest) {
  filter.value = { ...newFilter };
}
</script>

<template>
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
      <li class="breadcrumb-item active" aria-current="page">Управление товарами</li>
    </ol>
  </nav>

  <div class="row">
    <ProductList :filter="filter"/>
    <ProductFilter @apply-filters="handleApplyFilters" />
  </div>
</template>

<style scoped>

</style>