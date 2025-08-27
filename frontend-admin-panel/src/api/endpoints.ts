export const ENDPOINTS = {
    // Products endpoints
    PRODUCTS: (filter: URLSearchParams) => `/products?${filter}`,
    PRODUCT_BY_ID: (id: number) => `/products/${id}`,
    PRODUCT_ACTIVATE: (id: number) => `/products/${id}/active`,
    PRODUCTS_SEARCH: '/products/search',
    CATEGORIES_MINI: '/categories/mini',
    BRANDS_MINI: '/brands/mini',

} as const