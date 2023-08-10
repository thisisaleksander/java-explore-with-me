package ru.practicum.main.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.location.Location;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    String description;

    String eventDate;

    Location location;

    int participantLimit;

    Boolean paid;

    Boolean requestModeration;

    String stateAction;

    @Size(min = 3, max = 120)
    String title;
}
