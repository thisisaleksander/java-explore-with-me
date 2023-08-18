package ru.practicum.ewm.exception.model;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(long id) {
        super(String.format("Category with id %d not found", id));
    }
}
