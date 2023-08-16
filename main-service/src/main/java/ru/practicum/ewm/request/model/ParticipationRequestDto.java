package ru.practicum.ewm.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    long id;

    String created;

    Long event;

    Long requester;

    String status;
}
