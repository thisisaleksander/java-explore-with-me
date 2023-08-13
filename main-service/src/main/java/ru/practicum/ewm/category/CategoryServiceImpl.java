package ru.practicum.ewm.category;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.CategoryDto;
import ru.practicum.ewm.category.model.CategoryMapper;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;

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
            throw new ValidationException("Category exist");
        }
        return CategoryMapper.toDtoFromEntity(categoryRepository.save(CategoryMapper.toEntityFromDto(categoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category is not found"));
        if (eventService.findByCategory(category)) {
            throw new ValidationException("Category is related with Event");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto, Long catId) {
        categoryDto.setId(catId);
        Category categoryFromRepository = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Cat is not found"));
        if (categoryDto.getName().equals(categoryFromRepository.getName())) {
            return categoryDto;
        }
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ValidationException("Category exist");
        }
        return CategoryMapper.toDtoFromEntity(categoryRepository.save(CategoryMapper.toEntityFromDto(categoryDto)));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return CategoryMapper.toListDtoFromListEntity(categoryRepository.findAll(page).toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return CategoryMapper.toDtoFromEntity(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category is not found")));
    }
}
