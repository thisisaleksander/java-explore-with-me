package ru.practicum.ewm.exception.model;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(long id) {
        super(String.format("Request with id %d not found", id));
    }
}
