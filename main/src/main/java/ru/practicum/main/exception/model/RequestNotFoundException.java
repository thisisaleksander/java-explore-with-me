package ru.practicum.main.exception.model;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(Long requestId) {
        super(String.format("Request with id %d not found", requestId));
    }
}

