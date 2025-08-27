package ru.nonamejack.audioshop.mapper.factory;

import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.mapper.strategy.AttributeFilterStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttributeFilterFactory {
    private final List<AttributeFilterStrategy> strategies;

    public AttributeFilterFactory(List<AttributeFilterStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * Если ни одна стратегия не подошла, возвращаем базовый DTO без «значений»
     */
    public AttributeCategoryDto build(AttributeCategoryDto base, Integer categoryId) {
        return strategies.stream()
                .filter(s -> s.supports(base))
                .findFirst()
                .map(s -> s.build(base, categoryId))
                .orElse(base);
    }
    public boolean isSupported(AttributeCategoryDto base) {
        return strategies.stream()
                .anyMatch(s -> s.supports(base));
    }
}
