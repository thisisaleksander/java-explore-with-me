package ru.practicum.ewm.exception;

public class ResponseException extends RuntimeException {
    public ResponseException(String message) {
        super(message);
    }
}