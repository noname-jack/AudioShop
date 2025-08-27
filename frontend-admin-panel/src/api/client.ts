import axios, { type AxiosResponse, type AxiosError } from 'axios'
import {notify} from "@kyvg/vue3-notification";

// Создание базового HTTP клиента
export const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/admin/',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
})

// Interceptor для обработки запросов
apiClient.interceptors.request.use(
    (config) => {
        console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`)
        return config
    },
    (error) => {
        console.error('❌ Request Error:', error)
        return Promise.reject(error)
    }
)

// Interceptor для обработки ответов
apiClient.interceptors.response.use(
    (response: AxiosResponse) => {
        console.log(`✅ API Response: ${response.config.method?.toUpperCase()} ${response.config.url}`)
        return response
    },
    (error: AxiosError) => {
        if (error.response) {
            // Сервер вернул ошибку (например 404, 500 и т.д.)
            const data: any = error.response.data

            notify({
                title: `Ошибка ${error.response.status}`,
                text: data?.error || data?.message || 'Неизвестная ошибка',
                type: 'error',
                duration: 5000
            })
        } else {
            // Сетевая ошибка (таймаут, нет сети и т.д.)
            notify({
                title: 'Ошибка сети',
                text: error.message || 'Сетевая ошибка',
                type: 'error',
                duration: 5000
            })
        }
        return Promise.reject(error)
    }
)