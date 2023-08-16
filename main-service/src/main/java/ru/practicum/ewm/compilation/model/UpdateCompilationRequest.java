package ru.practicum.ewm.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    List<Long> events;

    Boolean pinned;

    @Size(max = 50)
    String title;
}
