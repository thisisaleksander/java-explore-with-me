package ru.practicum.main.event.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.main.category.Category;
import ru.practicum.main.event.Event;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.EventRequestDto;
import ru.practicum.main.location.Location;
import ru.practicum.main.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.utils.DateUtils.dateTimeFormatter;

@NoArgsConstructor
public class EventMapper {
    public static Event mapToEvent(EventRequestDto eventRequestDto,
                                   Location location,
                                   Category category,
                                   User initiator) {
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

    public static EventDto mapToEventDto(Event event, int confirmedRequests) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(event.getCategory());
        eventDto.setCreatedOn(event.getCreatedOn().format(dateTimeFormatter));
        eventDto.setDescription(event.getDescription());
        eventDto.setEventDate(event.getEventDate().format(dateTimeFormatter));
        eventDto.setInitiator(event.getInitiator());
        eventDto.setLocation(event.getLocation());
        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventDto.setPublishedOn(event.getPublishedOn().format(dateTimeFormatter));
        }
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setTitle(event.getTitle());
        eventDto.setViews(event.getViews());
        eventDto.setConfirmedRequests(confirmedRequests);
        return eventDto;
    }

    public static List<EventDto> mapToMultipleEventsDto(List<Event> listEvent) {
        List<EventDto> listEventDto = new ArrayList<>();
        for (Event event:listEvent) {
            listEventDto.add(mapToEventDto(event, event.getConfirmedRequests()));
        }
        return listEventDto;
    }
}
