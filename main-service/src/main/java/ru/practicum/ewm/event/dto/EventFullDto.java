package ru.practicum.ewm.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.user.model.User;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
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
