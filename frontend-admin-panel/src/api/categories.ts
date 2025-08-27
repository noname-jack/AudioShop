import type {CategoryMini} from "@/types";
import {apiClient} from "@/api/client.ts";
import {ENDPOINTS} from "@/api/endpoints.ts";

export async function getAllMiniCategories(): Promise<CategoryMini[]> {
    const response = await apiClient.get(ENDPOINTS.CATEGORIES_MINI)
    return response.data.data
}