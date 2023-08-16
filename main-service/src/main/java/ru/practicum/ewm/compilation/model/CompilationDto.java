package ru.practicum.ewm.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    long id;

    List<Event> events;

    Boolean pinned;

    String title;
}
