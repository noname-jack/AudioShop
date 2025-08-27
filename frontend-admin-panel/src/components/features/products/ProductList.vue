<script setup lang="ts">

import type {AdminProductSummaryDto, PageDto, PageInfo, ProductFilterAdminRequest} from "@/types";
import {computed, onMounted, ref, watch} from "vue";
import {activeProduct, getProductPage} from "@/api/products.ts";
import Loader from "@/components/common/Loader.vue";
import {formatPrice} from "@/util/utilityFunc.ts";



interface Props {
  filter: ProductFilterAdminRequest
}

const props = defineProps<Props>()


const products = ref<AdminProductSummaryDto[]>([])
const pageInfo = ref<PageInfo>()
const pageDto = ref<PageDto<AdminProductSummaryDto>>()


const loading = ref(false)
const error = ref<string | null>(null)
const currentPage = ref(0)
const totalElements = ref(0)
const totalPages = ref(0)
const pageSize = ref(40)

const hasProducts = computed(() => products.value.length > 0)
const hasPrevPage = computed(() => currentPage.value > 0)
const hasNextPage = computed(() => currentPage.value < totalPages.value - 1)

const visiblePages = computed(() => {
  const current = currentPage.value
  const total = totalPages.value
  if (!total) return []

  const pages: number[] = []

  const addRange = (start: number, end: number) => {
    for (let i = start; i <= end; i++)
      pages.push(i)
  }

  if (total <= 7) {
    addRange(0, total - 1)
    return pages
  }

  const start = Math.max(0, current - 2)
  const end = Math.min(total - 1, current + 2)

  // Всегда показываем первую страницу
  pages.push(0)

  // Добавляем "..." если есть пропуск
  if (start > 1) pages.push(-1)

  // Добавляем центральный диапазон
  addRange(Math.max(1, start), Math.min(total - 2, end))

  // Добавляем "..." если есть пропуск перед последней
  if (end < total - 2) pages.push(-1)

  // Последняя страница
  pages.push(total - 1)
  console.log(pages)
  return pages
})


const loadProducts = async (page: number = 0) => {
  try {
    loading.value = true
    error.value = null
    pageDto.value = await getProductPage(page,pageSize.value, props.filter)
    products.value = pageDto.value.content
    pageInfo.value = pageDto.value.page
    currentPage.value =pageInfo.value.number
    totalElements.value = pageInfo.value.totalElements
    totalPages.value = pageInfo.value.totalPages
    pageSize.value = pageInfo.value.size

  } catch (err) {
    console.error('Ошибка загрузки товаров:', err)
    error.value = 'Не удалось загрузить товары'
  } finally {
    loading.value = false
  }
}

const activeSelectProduct = async (id: number, active: boolean) => {
  try {
    loading.value = true
    await activeProduct(id, active)
    await loadProducts(currentPage.value)
  } catch (err) {
    console.error('Ошибка активации товара:', err)
    error.value = 'Не удалось изменить статус товара'
  } finally {
    loading.value = false
  }
}

const goToPage = (page: number) => {
  if (page >= 0 && page < totalPages.value) {
    loadProducts(page)
  }
}

const nextPage = () => {
  if (hasNextPage.value) {
    goToPage(currentPage.value + 1)
  }
}

const prevPage = () => {
  if (hasPrevPage.value) {
    goToPage(currentPage.value - 1)
  }
}





// Обработка изображений
const getImageUrl = (imagePath: string) => {
  if (imagePath){
    return imagePath
  }
  return '/src/assets/images/no-image.png'
}

onMounted( () => {
   loadProducts()
})


watch(() => props.filter, () => {
  currentPage.value = 0
  loadProducts()
}, { deep: true })

</script>

<template>
  <div class="col-lg-9">
    <div class="card shadow-sm mb-4">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0 text-accent">
          <i class="bi bi-box-seam me-2"></i>
          Список товаров
          <span v-if="totalElements" class="badge bg-secondary ms-2">
            {{ totalElements }}
          </span>
        </h5>
        <button class="btn btn-accent">
          <i class="bi bi-plus-lg"></i> Добавить товар
        </button>
      </div>
      <div class="card-body p-0 " id="table-list">
        <Loader :visible="loading"/>
        <div v-if="error" class="alert alert-danger m-3">
          <i class="bi bi-exclamation-triangle me-2"></i>
          {{ error }}
          <button class="btn btn-sm btn-outline-danger ms-2" @click="loadProducts(currentPage)">
            Повторить
          </button>
        </div>
        <div v-else-if="!hasProducts" class="text-center py-5">
          <i class="bi bi-box text-muted" style="font-size: 3rem;"></i>
          <h4 class="text-muted mt-3">Товары не найдены</h4>
          <p class="text-muted">Попробуйте изменить параметры фильтрации</p>
        </div>
        <div v-else class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
            <tr>
              <th style="width: 80px;">Изображение</th>
              <th>Название товара</th>
              <th style="width: 120px;">Цена, ₽</th>
              <th style="width: 100px;">Остаток</th>
              <th style="width: 120px;">Статус</th>
              <th style="width: 160px;">Действия</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="product in products" :key="product.productId">
              <td>
                <img
                    :src="getImageUrl(product.mainImage)"
                    class="rounded-1"
                    style="width: 60px; height: 60px; object-fit: cover;"
                    :alt="product.name"
                >
              </td>
              <td>{{product.brandName}} {{product.name}}</td>
              <td>
                {{ formatPrice(product.price) }}
              </td>
              <td>
                  <span
                      :class="{
                      'text-danger': product.stock < 5,
                      'text-warning': product.stock >= 5 && product.stock < 10,
                      'text-success': product.stock >= 10
                    }"
                  >
                    {{ product.stock }}
                  </span>
              </td>
              <td>
                  <span
                      :class="product.active ? 'badge bg-success' : 'badge bg-secondary'"
                  >
                    {{ product.active ? 'Активен' : 'Не активен' }}
                  </span>
              </td>
              <td>
                <button
                    class="btn btn-sm"
                    :class="product.active ? 'btn-danger' : 'btn-success'"
                    :title="product.active ? 'Деактивировать товар' : 'Сделать активным'"
                    @click="activeSelectProduct(product.productId, !product.active)"
                >
                  <i :class="product.active ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'"></i>
                </button>

                <router-link
                    :to="{ name: 'Product', params: { id: product.productId } }"
                    class="btn btn-sm btn-outline-primary"
                    title="Редактировать"
                >
                  <i class="bi bi-pencil-fill"></i>
                </router-link>

                <button class="btn btn-sm btn-outline-danger" data-bs-toggle="tooltip" title="Удалить">
                  <i class="bi bi-trash-fill"></i>
                </button>
              </td>
            </tr>

            </tbody>
          </table>
        </div>
      </div>
    </div>
    <nav v-if="totalPages > 1" aria-label="Навигация по страницам">
      <ul class="pagination justify-content-center">

        <!-- Предыдущая -->
        <li class="page-item" :class="{ disabled: !hasPrevPage }">
          <button class="page-link" @click="prevPage" :disabled="!hasPrevPage">
            <i class="bi bi-chevron-left me-1"></i>Назад
          </button>
        </li>

        <!-- Страницы -->
        <li
            v-for="page in visiblePages"
            :key="page"
            class="page-item"
            :class="{
            active: page === currentPage,
            disabled: page === -1
          }"
        >
          <button
              v-if="page === -1"
              class="page-link"
              disabled
          >
            ...
          </button>
          <button
              v-else
              class="page-link"
              @click="goToPage(page)"
          >
            {{ page + 1 }}
          </button>
        </li>

        <!-- Следующая -->
        <li class="page-item" :class="{ disabled: !hasNextPage }">
          <button class="page-link" @click="nextPage" :disabled="!hasNextPage">
            Далее<i class="bi bi-chevron-right ms-1"></i>
          </button>
        </li>
      </ul>

    </nav>
  </div>

</template>

<style scoped>

</style>