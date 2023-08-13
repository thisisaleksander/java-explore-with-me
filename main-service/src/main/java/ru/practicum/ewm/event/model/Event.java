package ru.practicum.ewm.event.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.location.Location;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@ToString
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
