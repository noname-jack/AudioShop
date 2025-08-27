package ru.nonamejack.audioshop.dto.category;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import java.util.List;

@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "categoryId"
)
public class CategoryTreeDto {
    private Integer categoryId;
    private String name;
    private String description;
    private String imgPath;
    private List<CategoryTreeDto> children;
}