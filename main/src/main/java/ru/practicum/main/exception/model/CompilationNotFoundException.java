package ru.practicum.main.exception.model;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(Long compilationId) {
        super(String.format("Compilation with id %d not found", compilationId));
    }
}
