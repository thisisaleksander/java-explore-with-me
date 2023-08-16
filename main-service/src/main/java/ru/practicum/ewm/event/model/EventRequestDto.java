package ru.practicum.ewm.event.model;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.location.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    String description;

    String eventDate;

    Location location;


    Boolean paid;

    int participantLimit;

    String publishedOn;

    Boolean requestModeration;

    String state;

    @Size(min = 3, max = 120)
    String title;

}

