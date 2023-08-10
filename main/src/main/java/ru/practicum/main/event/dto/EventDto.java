package ru.practicum.main.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.category.Category;
import ru.practicum.main.location.Location;
import ru.practicum.main.user.User;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDto {
    long id;
    String annotation;
    Category category;
    String createdOn;
    String description;
    String eventDate;
    User initiator;
    Location location;
    Boolean paid;
    int participantLimit;
    String publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    Long views;
    int confirmedRequests;
}
