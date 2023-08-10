package ru.practicum.main.category.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.main.category.Category;
import ru.practicum.main.category.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CategoryMapper {
    public static Category mapToCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setId(categoryDto.getId());
        return category;
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setId(category.getId());
        return categoryDto;
    }

    public static List<CategoryDto> mapToMultipleCategoriesDto(List<Category> listCategory) {
        List<CategoryDto> listDto = new ArrayList<>();
        for (Category category : listCategory) {
            listDto.add(mapToCategoryDto(category));
        }
        return listDto;
    }
}
