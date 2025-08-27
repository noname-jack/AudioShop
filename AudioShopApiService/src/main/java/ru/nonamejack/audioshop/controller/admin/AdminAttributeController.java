package ru.nonamejack.audioshop.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.request.AttributeDtoRequest;
import ru.nonamejack.audioshop.model.enums.ValueType;
import ru.nonamejack.audioshop.service.AttributeService;

import java.util.List;

@RestController
@RequestMapping("/admin/attributes")
@Validated
public class AdminAttributeController {

    private final AttributeService attributeService;

    public AdminAttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @GetMapping
    public ApiResponse<List<AttributeDto>> getAllAttributes(@RequestParam(required = false)   ValueType valueType,
                                                            @RequestParam(required = false)  String name) {
        return ApiResponse.success(attributeService.getAllAttributes(valueType, name));
    }

    @PostMapping
    public ApiResponse<AttributeDto> createAttribute(@RequestBody @Valid AttributeDtoRequest attributeDto) {
        return ApiResponse.success(attributeService.createAttribute(attributeDto), HttpStatus.CREATED);
    }

    @PutMapping("/{attributeId}")
    public ApiResponse<Boolean> updateAttribute(@PathVariable @Positive Integer attributeId, @RequestBody @Valid AttributeDtoRequest attributeDto) {
        attributeService.updateAttribute(attributeId, attributeDto);
        return ApiResponse.success(true);
    }

    @DeleteMapping("/{attributeId}")
    public ApiResponse<Boolean> deleteAttribute(@PathVariable @Positive Integer attributeId) {
        attributeService.deleteAttribute(attributeId);
        return ApiResponse.success(true);
    }
}
