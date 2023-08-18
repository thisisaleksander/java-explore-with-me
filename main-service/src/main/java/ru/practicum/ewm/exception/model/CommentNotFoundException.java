package ru.practicum.ewm.exception.model;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(long id) {
        super(String.format("Comment with id %d not found", id));
    }
}
