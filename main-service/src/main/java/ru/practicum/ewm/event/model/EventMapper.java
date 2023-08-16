package ru.practicum.ewm.event.model;

import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.location.Location;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class EventMapper {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEntityFromRequest(EventRequestDto eventRequestDto, Location location, Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(eventRequestDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventRequestDto.getDescription());
        event.setEventDate(LocalDateTime.parse(eventRequestDto.getEventDate(), dateTimeFormatter));
        event.setLocation(location);
        event.setPaid(eventRequestDto.getPaid());
        event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        event.setCreatedOn(LocalDateTime.now());
        event.setRequestModeration(eventRequestDto.getRequestModeration());
        event.setState("WAITING");
        event.setTitle(eventRequestDto.getTitle());
        event.setInitiator(initiator);
        return event;
    }

    public static EventFullDto toFullDtoFromEntity(Event event, int confirmedRequests) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setCreatedOn(event.getCreatedOn().format(dateTimeFormatter));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate().format(dateTimeFormatter));
        eventFullDto.setInitiator(event.getInitiator());
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(dateTimeFormatter));
        }
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        eventFullDto.setConfirmedRequests(confirmedRequests);
        return eventFullDto;
    }

    public static List<EventFullDto> toListEventFullDtoFromListEvent(List<Event> listEvent) {
        List<EventFullDto> listEventFullDto = new ArrayList<>();
        for (Event event:listEvent
        ) {
            listEventFullDto.add(toFullDtoFromEntity(event, event.getConfirmedRequests()));
        }
        return listEventFullDto;
    }
}
