package ru.practicum.ewm.comment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationCommentDto {
    long id;

    String created;

    String updated;

    Long event;

    Long commentator;

    String status;

    @NotBlank
    @Size(max = 2000)
    String text;
}

