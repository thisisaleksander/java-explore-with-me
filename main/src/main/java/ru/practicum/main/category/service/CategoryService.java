package ru.practicum.main.category.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.dto.CategoryDto;

import java.util.List;

@Transactional(readOnly = true)
public interface CategoryService {
    @Transactional
    CategoryDto saveCategory(CategoryDto categoryDto);

    @Transactional
    void deleteCategory(Long catId);

    @Transactional
    CategoryDto patchCategory(CategoryDto categoryDto, Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
