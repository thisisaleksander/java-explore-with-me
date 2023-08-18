package ru.practicum.ewm.exception.model;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(long id) {
        super(String.format("Compilation with id %d not found", id));
    }
}
