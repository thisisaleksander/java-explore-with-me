package ru.practicum.main.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.category.Category;
import ru.practicum.main.location.Location;
import ru.practicum.main.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "events", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String annotation;

    @ManyToOne
    Category category;

    @Column
    LocalDateTime createdOn;

    @Column
    String description;

    @Column
    LocalDateTime eventDate;

    @ManyToOne
    User initiator;

    @ManyToOne
    Location location;

    @Column
    Boolean paid;

    @Column
    int participantLimit;

    @Column
    LocalDateTime publishedOn;

    @Column
    Boolean requestModeration;

    @Column
    String state;

    @Column
    String title;

    @Column
    Long views;

    @Column
    int confirmedRequests;
}
