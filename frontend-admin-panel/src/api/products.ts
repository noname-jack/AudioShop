import { apiClient } from './client'
import { ENDPOINTS } from './endpoints'
import type {AdminProductSummaryDto, ApiResponse, PageDto, ProductDetailsDto, ProductFilterAdminRequest} from '@/types'


export async function getProductPage(page: number = 0,
                                  size: number = 40,
                                  filter?: Partial<ProductFilterAdminRequest>): Promise<PageDto<AdminProductSummaryDto>> {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString()
    })
    if (filter?.categoryId) {
        params.append('categoryId', filter.categoryId.toString())
    }
    if (filter?.brandIds && filter.brandIds.length > 0) {
        filter.brandIds.forEach(id => params.append('brandIds', id.toString()))
    }
    if (filter?.name) {
        params.append('name', filter.name)
    }
    if (filter?.active !== undefined && filter.active !== null) {
        params.append('active', filter.active.toString())
    }
    const response = await apiClient.get(ENDPOINTS.PRODUCTS(params))
    return response.data.data

}

export async function getProductById(id: number): Promise<ProductDetailsDto> {
    const response = await apiClient.get(ENDPOINTS.PRODUCT_BY_ID(id))
    return response.data.data
}

export async function activeProduct(id: number, active: boolean) {
    const response = await apiClient.patch(ENDPOINTS.PRODUCT_ACTIVATE(id), active)
}