package ru.practicum.ewm.stats;

import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats", schema = "public")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    @Size(max = 100)
    @Column
    String app;

    @NotNull
    @Size(max = 250)
    @Column
    String uri;

    @NotNull
    @Size(max = 15)
    @Column
    String ip;

    @Column
    LocalDateTime created;

}
