package ru.nonamejack.audioshop.util;

import java.math.BigDecimal;

public interface PriceRange {
    BigDecimal getMinValue();
    BigDecimal getMaxValue();
}
