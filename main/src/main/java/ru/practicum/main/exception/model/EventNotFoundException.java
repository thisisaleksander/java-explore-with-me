package ru.practicum.main.exception.model;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(Long eventId) {
        super(String.format("Event with id %d not found", eventId));
    }
}