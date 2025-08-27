export interface ProductFilterAdminRequest {
    categoryId: number | null
    brandIds: number[]
    active: boolean | null
    name: string
}