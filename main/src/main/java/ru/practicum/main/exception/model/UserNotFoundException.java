package ru.practicum.main.exception.model;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id %d not found", userId));
    }
}
