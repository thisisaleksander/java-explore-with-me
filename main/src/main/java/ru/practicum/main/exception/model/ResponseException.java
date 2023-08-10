package ru.practicum.main.exception.model;

public class ResponseException extends RuntimeException {
    public ResponseException(String message) {
        super(message);
    }
}