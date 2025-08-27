export interface PageDto<T> {
    content: T[]
    page: PageInfo
}

export interface PageInfo {
    size: number
    number: number
    totalElements: number
    totalPages: number
}