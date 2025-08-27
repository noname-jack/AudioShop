import type {FilterType, ValueType} from "./enums.ts";

export interface AttributeDto {
    attributeId: number
    attributeName: string
    valueType: ValueType
    filterType: FilterType
}