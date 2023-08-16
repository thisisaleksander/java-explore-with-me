package ru.practicum.ewm.event;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFullDto;
import ru.practicum.ewm.event.model.EventRequestDto;
import ru.practicum.ewm.event.model.UpdateEventAdminRequest;

import java.util.List;

@Transactional(readOnly = true)
public interface EventService {
    List<EventFullDto> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    @Transactional
    EventFullDto patchEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

    List<EventFullDto> getEventsPrivate(Long userId, Integer from, Integer size);

    @Transactional
    EventFullDto saveEventPrivate(Long userId, EventRequestDto eventRequestDto);

    EventFullDto getEventPrivate(Long userId, Long eventId);

    @Transactional
    EventFullDto patchEventPrivate(UpdateEventAdminRequest updateEventAdminRequest, Long userId, Long eventId);

    List<EventFullDto> getEventsPublic(String text, List<Long> categories, String paid, String rangeStart, String rangeEnd, String onlyAvailable, String sort, Integer from, Integer size, String uri, String ip);

    EventFullDto getEventPublic(Long id, String uri, String ip);

    boolean findByCategory(Category category);

    List<Event> findByIds(List<Long> eventIds);

    Event findById(Long eventId);

    @Transactional
    void save(Event event);

    @Transactional
    void updateConfirmedRequest(int confirmedRequest, Long eventId);
}
