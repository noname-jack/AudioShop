import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const HomePage = () => import('@/views/HomePage.vue')
const ProductsPage = () => import('@/views/ProductsPage.vue')
const ProductPage = () => import('@/views/ProductPage.vue')

const CategoriesPage = () => import('@/views/CategoriesPage.vue')
// Определение маршрутов
const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'Home',
        component: HomePage,
        meta: {
            title: 'Главная страница'
        }
    },
    {
        path: '/products',
        name: 'Products',
        component: ProductsPage,
        meta: {
            title: 'Каталог товаров'
        }
    },
    {
        path: '/products/:id(\\d+)',
        name: 'Product',
        component: ProductPage,
        meta: {
            title: 'Товар'
        },
        props: (route) => ({
            id: Number(route.params.id)
        })
    },

    {
        path: '/categories',
        name: 'Categories',
        component: CategoriesPage,
        meta: {
            title: 'Категории'
        }
    },

    // Обработка несуществующих маршрутов
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('@/views/NotFoundPage.vue'),
        meta: {
            title: 'Страница не найдена'
        }
    }
]

// Создание router instance
const router = createRouter({
    history: createWebHistory(),
    routes,
    // Прокрутка к началу страницы при переходе
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return { top: 0 }
        }
    }
})


router.beforeEach((to, from, next) => {
    next()
})

export default router