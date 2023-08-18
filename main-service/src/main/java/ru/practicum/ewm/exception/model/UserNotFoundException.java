package ru.practicum.ewm.exception.model;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(long id) {
        super(String.format("User with id %d not found", id));
    }
}
