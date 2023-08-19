package ru.practicum.ewm.comment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusUpdateComment {

    List<Long> commentIds;

    String status;

    String text;
}
