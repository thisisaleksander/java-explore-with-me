package ru.practicum.statistics.model;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "Statistics", schema = "public")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @Size(max = 100)
    @Column(name = "app")
    String app;

    @NotNull
    @Size(max = 250)
    @Column(name = "uri")
    String uri;

    @NotNull
    @Size(max = 15)
    @Column(name = "ip")
    String ip;

    @Column(name = "time_added")
    LocalDateTime created;
}