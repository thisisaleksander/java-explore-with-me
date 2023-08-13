package ru.practicum.ewm.category.model;

import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CategoryMapper {
    public static Category toEntityFromDto(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setId(categoryDto.getId());
        return category;
    }

    public static CategoryDto toDtoFromEntity(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setId(category.getId());
        return categoryDto;
    }

    public static List<CategoryDto> toListDtoFromListEntity(List<Category> listEntity) {
        List<CategoryDto> listDto = new ArrayList<>();
        for (Category category:listEntity
        ) {
            listDto.add(toDtoFromEntity(category));
        }
        return listDto;
    }
}
