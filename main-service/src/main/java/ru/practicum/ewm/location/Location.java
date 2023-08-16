package ru.practicum.ewm.location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "locations", schema = "public")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "lat", nullable = false)
    @NotNull(message = "lat не может быть пустым")
    float lat;

    @Column(name = "lon", nullable = false)
    @NotNull(message = "lon не может быть пустым")
    float lon;
}
