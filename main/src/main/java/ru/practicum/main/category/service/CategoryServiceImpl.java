package ru.practicum.main.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.Category;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.exception.model.CategoryNotFoundException;
import ru.practicum.main.exception.model.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    EventService eventService;

    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ValidationException("Category already exist");
        }
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(CategoryMapper.mapToCategory(categoryDto)));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (eventService.findByCategory(category)) {
            throw new ValidationException("Category is related with Event");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto, Long categoryId) {
        categoryDto.setId(categoryId);
        Category categoryFromRepository = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (categoryDto.getName().equals(categoryFromRepository.getName())) {
            return categoryDto;
        }
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ValidationException("Category already exist");
        }
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(
                CategoryMapper.mapToCategory(categoryDto)));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return CategoryMapper.mapToMultipleCategoriesDto(categoryRepository.findAll(page).toList());
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        return CategoryMapper.mapToCategoryDto(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException(categoryId)));
    }
}
