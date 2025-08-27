import type { AttributeDto } from './AttributeDto'
import type { ReviewDto } from './ReviewDto'
export interface ProductDetailsDto {
    productId: number
    brandName: string
    brandId: number
    categoryName: string
    categoryId: number
    name: string
    description?: string
    price: number
    stock: number
    mainImage?: string
    imagesMap: Record<number, string>
    attributes: AttributeDto[]
    reviews: ReviewDto[]
    active: boolean
}