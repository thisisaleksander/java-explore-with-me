package ru.practicum.main.exception.model;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long categoryId) {
        super(String.format("Category %d not found", categoryId));
    }
}