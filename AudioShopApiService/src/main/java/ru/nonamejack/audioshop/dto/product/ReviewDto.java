package ru.nonamejack.audioshop.dto.product;

import java.util.Date;

public record ReviewDto(
        Integer reviewId,
        Double rating,
        String senderName,
        String advantages,
        String disadvantages,
        String comment,
        Date reviewDate
) {}