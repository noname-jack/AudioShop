export interface ReviewDto {
    reviewId: number
    rating: number
    senderName: string
    advantages?: string
    disadvantages?: string
    comment?: string
    reviewDate: string
}