
import {apiClient} from "@/api/client.ts";
import {ENDPOINTS} from "@/api/endpoints.ts";
import type {BrandMini} from "@/types";

export async function getAllMiniBrands(): Promise<BrandMini[]> {
    const response = await apiClient.get(ENDPOINTS.BRANDS_MINI)
    return response.data.data
}