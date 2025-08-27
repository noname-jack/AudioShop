<script setup lang="ts">
import {ref, watch, computed} from 'vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation } from 'swiper/modules'

interface Props {
  imagesMap: Record<number, string>
}

const props = defineProps<Props>()


// Реактивные данные
const localImagesMap = ref<Record<number, string>>({ ...props.imagesMap })
const fileInput = ref<HTMLInputElement>()
const swiperRef = ref()

// Конфигурация Swiper
const swiperOptions = {
  modules: [Navigation],
  slidesPerView: 'auto' as const,
  spaceBetween: 2,
  navigation: {
    nextEl: '.next-custom',
    prevEl: '.prev-custom'
  }
}


watch(() => props.imagesMap, (newImages) => {
  localImagesMap.value = { ...newImages }
}, { deep: true })


const removeImage = (imageId: number) => {

}


const addNewImage = () => {
  fileInput.value?.click()
}


const imageEntries = computed(() => {
  return Object.entries(localImagesMap.value).map(([id, url]) => ({
    id: Number(id),
    url
  }))
})
</script>

<template>

    <div class="col-sm-9 right-dop-img-cont">
      <div class="container-dop-img" v-if="imageEntries.length > 0">
        <Swiper
            ref="swiperRef"
            v-bind="swiperOptions"
            class="swiper"
        >
          <SwiperSlide
              v-for="image in imageEntries"
              :key="image.id"
              class="swiper-slide item-dop-img"
          >
            <img
                class="img-thumbnail"
                :src="image.url"
                :alt="`Изображение ${image.id}`"
            >
            <button
                type="button"
                class="btn btn-danger btn-sm image-remove-btn"
                @click="removeImage(image.id)"
            >
              <i class="bi bi-trash"></i>
            </button>
          </SwiperSlide>

          <!-- Кнопки навигации -->
          <template #container-end>
            <button class="round-button prev-custom">
              <img src="@/assets/images/svg/angle-left-white.svg" alt="Назад">
            </button>
            <button class="round-button next-custom">
              <img src="@/assets/images/svg/angle-right-white.svg" alt="Вперед">
            </button>
          </template>
        </Swiper>
      </div>

      <!-- Кнопка добавления изображения -->
      <button
          type="button"
          class="btn btn-outline-secondary add-image-btn d-flex align-items-center justify-content-center"
          @click="addNewImage"
      >
        <i class="bi bi-plus-circle fs-1 text-muted"></i>
      </button>

      <!-- input для файлов -->
      <input
          ref="fileInput"
          type="file"
          class="d-none"
          accept="image/*"
          multiple
      >
      </div>
</template>

<style scoped>



.item-dop-img {
  flex: 0 0 auto; /* Элементы не сжимаются и сохраняют фиксированную ширину */
  position: relative;
  width: 7rem;
  height: 7rem;
}

.item-dop-img img {
  width: 7rem;
}


</style>