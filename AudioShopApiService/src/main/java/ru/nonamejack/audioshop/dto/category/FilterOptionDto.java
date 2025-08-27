package ru.nonamejack.audioshop.dto.category;

import ru.nonamejack.audioshop.dto.BigDecimalRangeDto;
import ru.nonamejack.audioshop.dto.DoubleRangeDto;
import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.brand.BrandDto;
import java.util.List;


public record FilterOptionDto(
        Integer categoryId,
        List<BrandDto> brandDtoList,
        BigDecimalRangeDto priceRange,
        List<AttributeCategoryDto> attributes
) {}
