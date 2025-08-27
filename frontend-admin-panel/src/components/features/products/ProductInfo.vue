<script setup lang="ts">
import {onMounted, ref} from "vue";
import type {BrandMini, CategoryMini, ProductDetailsDto} from "@/types";
import {getProductById} from "@/api/products.ts";
import {getAllMiniCategories} from "@/api/categories.ts";
import {getAllMiniBrands} from "@/api/brands.ts";
import Loader from "@/components/common/Loader.vue";
import ProductImages from "@/components/features/products/ProductImages.vue";

interface Props {
  id: number
}

const props = defineProps<Props>()

const categories = ref<CategoryMini[]>([])
const brands = ref<BrandMini[]>([])
const product = ref<ProductDetailsDto>()

const loading = ref(false)
const error = ref<string | null>(null)

const selectedCategoryId = ref<number>()

const selectedBrandId = ref<number>()
const loadProduct = async () => {
  try {
    product.value = await getProductById(props.id)
    selectedCategoryId.value = product.value?.categoryId
    selectedBrandId.value = product.value?.brandId
  } catch (err) {
    console.error('Ошибка загрузки товаров:', err)
    error.value = 'Не удалось загрузить товары'
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    categories.value = await getAllMiniCategories()
  } catch (err) {
    console.error('Ошибка загрузки доступных категорий:', err)
    error.value = 'Не удалось загрузить доступные категории'
    loading.value = false
  }
}

const loadPageInfo = async () => {
  loading.value = true
  await loadProduct()
  await loadCategories()
  await loadBrands()
  loading.value = false
}

onMounted(async () => {
      await loadPageInfo()
    }
)
const loadBrands = async () => {
  try {
    brands.value = await getAllMiniBrands()
  } catch (err) {
    console.error('Ошибка загрузки доступных брендов:', err)
    error.value = 'Не удалось загрузить доступные бренды'
  }
}

</script>

<template>
  <div class="card shadow-sm">
    <Loader :visible="loading"/>
    <div v-if="error" class="alert alert-danger m-3">
      <i class="bi bi-exclamation-triangle me-2"></i>
      {{ error }}
      <button class="btn btn-sm btn-outline-danger ms-2" @click="(loadPageInfo())">
        Повторить
      </button>
    </div>
    <template v-if="product">
      <div class="card-header">
        <h5 class="mb-0 text-accent"><i class="bi bi-pencil-square me-2"></i> Редактирование товара
          {{ product.brandName }} {{ product.name }}</h5>
      </div>
      <div class="card-body">
        <form id="edit-product-form">

          <div class="mb-3 row">
            <label for="manufacturer" class="col-sm-3 col-form-label">Категория</label>
            <div class="col-sm-9">
              <select
                  id="category"
                  class="form-select"
                  v-model="selectedCategoryId"
              >
                <option
                    v-for="c in categories"
                    :key="c.categoryId"
                    :value="c.categoryId"
                >
                  {{ c.name }}
                </option>
              </select>
            </div>
          </div>
          <!-- Производитель -->
          <div class="mb-3 row">
            <label for="manufacturer" class="col-sm-3 col-form-label">Производитель</label>
            <div class="col-sm-9">
              <select
                  id="manufacturer"
                  class="form-select"
                  v-model="selectedBrandId"
              >
                <option
                    v-for="b in brands"
                    :key="b.id"
                    :value="b.id"
                >
                  {{ b.name }}
                </option>
              </select>
            </div>
          </div>
          <!-- Название -->
          <div class="mb-3 row">
            <label for="product-name" class="col-sm-3 col-form-label">Название</label>
            <div class="col-sm-9">
              <input
                  type="text"
                  id="product-name"
                  class="form-control"
                  v-model="product.name"
                  placeholder="Введите название товара"
              />
            </div>
          </div>
          <!-- Описание -->
          <div class="mb-3 row">
            <label for="description" class="col-sm-3 col-form-label">Описание</label>
            <div class="col-sm-9">
               <textarea
                   id="description"
                   class="form-control"
                   style="height:200px;"
                   v-model="product.description"
                   placeholder="Ввведите описание товара"
               ></textarea>
            </div>
          </div>
          <!-- Цена -->
          <div class="mb-3 row">
            <label for="price" class="col-sm-3 col-form-label">Цена (₽)</label>
            <div class="col-sm-9">
              <input
                  type="number"
                  id="price"
                  class="form-control"
                  v-model.number="product.price"
                  step="0.01"
                  placeholder="0.00"
              />
            </div>
          </div>
          <!-- Количество -->
          <div class="mb-3 row">
            <label for="quantity" class="col-sm-3 col-form-label">Количество</label>
            <div class="col-sm-9">
              <input
                  type="number"
                  id="quantity"
                  class="form-control"
                  v-model.number="product.stock"
                  placeholder="0"
              />
            </div>
          </div>

          <!-- Основное изображение -->
          <div class="mb-3 row">
            <label class="col-sm-3 col-form-label">Основное изображение</label>
            <div class="col-sm-9">
              <div class="main-image-container">
                <img
                    v-if="product.mainImage"
                    :src="product.mainImage"
                    class="img-thumbnail"
                    style="max-width: 7rem;"
                    alt="Основное изображение"
                />
                <button type="button" class="main-image-edit"
                        onclick="document.getElementById('main-image-input').click()">
                  <i class="bi bi-pencil"></i>
                </button>
              </div>
              <input type="file" id="main-image-input" class="d-none" accept="image/*">
              <small class="text-muted d-block mt-2">JPEG, PNG. Макс. 2 МБ.</small>
            </div>
          </div>
          <div class="mb-4 row">
            <label class="col-sm-3 col-form-label">Дополнительные изображения</label>
            <ProductImages :images-map="product.imagesMap"/>
          </div>


          <!-- Характеристики товара -->
          <div class="mb-3 row">
            <label class="col-sm-3 col-form-label">Характеристики</label>
            <div class="col-sm-9">
              <div class="d-flex align-items-center">
                <span class="badge bg-secondary me-2">{{product.attributes.length}} характеристик</span>
                <button type="button" class="btn btn-outline-primary btn-sm" data-bs-toggle="modal"
                        data-bs-target="#characteristicsModal">
                  <i class="bi bi-pencil me-1"></i> Редактировать
                </button>
              </div>
            </div>
          </div>

          <!-- Отзывы -->
          <div class="mb-4 row">
            <label class="col-sm-3 col-form-label">Отзывы</label>
            <div class="col-sm-9">
              <div class="d-flex align-items-center">
                <span class="badge bg-secondary me-2">{{product.reviews.length}} отзывов</span>
                <button type="button" class="btn btn-outline-primary btn-sm" data-bs-toggle="modal"
                        data-bs-target="#reviewsModal">
                  <i class="bi bi-pencil me-1"></i> Редактировать
                </button>
              </div>
            </div>
          </div>

          <!-- Кнопки действий -->
          <div class="text-end">
            <button type="reset" class="btn btn-outline-secondary me-2">
              <i class="bi bi-x-circle"></i> Отмена
            </button>
            <button type="submit" class="btn btn-accent">
              <i class="bi bi-save-fill"></i> Сохранить
            </button>
          </div>
        </form>
      </div>
    </template>
  </div>


</template>

<style scoped>

</style>