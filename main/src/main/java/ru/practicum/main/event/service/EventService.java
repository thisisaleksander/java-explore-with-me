package ru.practicum.main.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.Category;
import ru.practicum.main.event.Event;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.EventRequestDto;
import ru.practicum.main.event.dto.UpdateEventAdminRequest;

import java.util.List;

@Transactional(readOnly = true)
public interface EventService {
    @Transactional
    EventDto patchEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

    @Transactional
    void save(Event event);

    @Transactional
    void updateConfirmedRequest(int confirmedRequest, Long eventId);

    @Transactional
    EventDto saveEventPrivate(Long userId, EventRequestDto eventRequestDto);

    @Transactional
    EventDto patchEventPrivate(UpdateEventAdminRequest updateEventAdminRequest,
                               Long userId,
                               Long eventId);

    List<EventDto> getEventsAdmin(List<Long> users,
                                  List<String> states,
                                  List<Long> categories,
                                  String rangeStart,
                                  String rangeEnd,
                                  Integer from,
                                  Integer size);

    List<EventDto> getEventsPrivate(Long userId, Integer from, Integer size);

    EventDto getEventPrivate(Long userId, Long eventId);

    List<EventDto> getEventsPublic(String text,
                                   List<Long> categories,
                                   String paid,
                                   String rangeStart,
                                   String rangeEnd,
                                   String onlyAvailable,
                                   String sort,
                                   Integer from,
                                   Integer size,
                                   String uri,
                                   String ip);

    EventDto getEventPublic(Long id, String uri, String ip);

    boolean findByCategory(Category category);

    List<Event> findByIds(List<Long> eventIds);

    Event findById(Long eventId);
}
