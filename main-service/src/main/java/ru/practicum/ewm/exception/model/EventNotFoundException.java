package ru.practicum.ewm.exception.model;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(long id) {
        super(String.format("Event with id %d not found", id));
    }
}
