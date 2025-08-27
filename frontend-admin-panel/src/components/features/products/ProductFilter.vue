<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'

import type {BrandMini, CategoryMini, ProductFilterAdminRequest} from '@/types'
import {getAllMiniCategories} from "@/api/categories.ts";
import {getAllMiniBrands} from "@/api/brands.ts";

const categories = ref<CategoryMini[]>([])
const brands = ref<BrandMini[]>([])

const filter = reactive<ProductFilterAdminRequest>({
  categoryId: null,
  brandIds: [],
  active: null,
  name: ''
})
const loading = ref(false)
const error = ref<string | null>(null)



onMounted(async () => {
  try {
    loading.value = true
    error.value = null
    categories.value = await getAllMiniCategories()
    brands.value = await getAllMiniBrands()
    console.log('Загружено категорий:', categories.value.length)
    console.log('Загружено брендов:', brands.value.length)
  }
  catch (err) {
    console.error('Ошибка загрузки фильтров:', err)
    error.value = 'Не удалось загрузить фильтры'
  }
  finally {
    loading.value = false
  }
})


const emit = defineEmits<{
  (e: 'apply-filters', filter: ProductFilterAdminRequest): void;
}>();


function applyFilters() {
  console.log('Применяем фильтры:', { ...filter });
  emit('apply-filters', { ...filter });
}

function resetFilters() {
  filter.categoryId = null
  filter.brandIds = []
  filter.active = null
  filter.name = ''
  emit('apply-filters', { ...filter });
}
</script>

<template>
  <div class="col-lg-3">
    <div class="card shadow-sm mb-4">
      <div class="card-header text-accent">
        <i class="bi bi-funnel-fill me-2"></i> Фильтры
      </div>
      <div class="card-body">

        <div v-if="error" class="alert alert-danger alert-sm mb-3">
          <i class="bi bi-exclamation-triangle me-2"></i>
          {{ error }}
        </div>

        <form id="filter-form" @submit.prevent="applyFilters">
          <!-- Категория -->
          <div class="mb-3">
            <label for="filter-category" class="form-label">Категория</label>
            <select id="filter-category" class="form-select" v-model="filter.categoryId"  :disabled="loading">
              <option :value=null>Все категории</option>
              <option v-for="c in categories" :key="c.categoryId" :value="c.categoryId">{{ c.name }}</option>
            </select>
          </div>

          <!-- Бренды -->
          <div class="mb-3">
            <label class="form-label">Бренды</label>
            <div class="brands-scroll-container border rounded p-2">
              <div v-for="b in brands" :key="b.id" class="form-check">
                <input
                    class="form-check-input"
                    type="checkbox"
                    :id="'brand-' + b.id"
                    :value="b.id"
                    v-model="filter.brandIds"
                    :disabled="loading"
                />
                <label class="form-check-label" :for="'brand-' + b.id">{{ b.name }}</label>
              </div>
            </div>
          </div>


          <!-- Название -->
          <div class="mb-3">
            <label for="filter-name" class="form-label">Название товара</label>
            <input type="text" id="filter-name" class="form-control" placeholder="Введите название" v-model="filter.name">
          </div>

          <!-- Статус -->
          <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="filter-active" v-model="filter.active">
            <label class="form-check-label" for="filter-active">Только активные товары</label>
          </div>


          <!-- Кнопки -->
          <div class="d-grid gap-2">
            <button type="submit" class="btn btn-accent">Применить</button>
            <button type="button" class="btn btn-outline-secondary" @click="resetFilters">Сбросить</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
