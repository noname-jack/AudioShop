package ru.nonamejack.audioshop.dto.request;


import java.util.List;


public record ProductFilterAdminRequest(
        Integer categoryId,
        List<Integer> brandIds,
        Boolean active,
        String name
) {}

