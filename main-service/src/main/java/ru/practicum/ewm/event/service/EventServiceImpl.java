package ru.practicum.ewm.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.ewm.category.service.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.exception.model.*;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.service.LocationService;
import ru.practicum.ewm.user.service.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.utils.DateUtils.dateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    LocationService locationService;
    StatsClient client;

    @Override
    public List<EventFullDto> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        LocalDateTime start;
        if (users.isEmpty()) {
            users = userRepository.findAllId();
        }
        if (categories.isEmpty()) {

            categories = categoryRepository.findAllId();

        }
        if (states.isEmpty()) {

            states = List.of("PUBLISHED", "WAITING", "REJECTED", "CANCELED");

        }
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        } else {
            start = LocalDateTime.now();
        }
        LocalDateTime end;
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        } else {
            end = LocalDateTime.of(3000, 1, 1, 1, 1);
        }
        if (end.isBefore(LocalDateTime.now()) || end.isBefore(start)) {
            throw new ResponseException("Incorrectly made request");
        }
        List<User> usersList = userRepository.findByIds(users);
        List<Category> categoriesList = categoryRepository.findByIds(categories);
        return EventMapper.toListEventFullDtoFromListEvent(
                eventRepository.findByInitiatorAndStateAndCategoryAndEventDateBeforeEnd(
                        usersList, states, categoriesList, start, end, page).toList());
    }

    @Override
    public EventFullDto patchEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (!event.getState().equals("WAITING")) {
            throw new ValidationException("Event state is not WAITING");
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new ValidationException(String.format("Category with id %d not found", updateEventAdminRequest.getCategory())));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), dateTimeFormatter);
            if (eventDate.plusHours(2).isBefore(LocalDateTime.now())) {
                throw new ResponseException("Event date is incorrect");
            }
            event.setEventDate(eventDate);
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(locationService.save(updateEventAdminRequest.getLocation()));
        }
        if (updateEventAdminRequest.getParticipantLimit() != 0) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                event.setState("PUBLISHED");
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
                event.setState("REJECTED");
                event.setPublishedOn(null);
            }
            if (updateEventAdminRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState("CANCELED");
                event.setPublishedOn(null);
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        event.setId(eventId);

        return EventMapper.toFullDtoFromEntity(eventRepository.save(event), event.getConfirmedRequests());
    }

    @Override
    public List<EventFullDto> getEventsPrivate(Long userId, Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        User initiator = userRepository.findById(userId).orElseThrow();
        return EventMapper.toListEventFullDtoFromListEvent(eventRepository.findByInitiator(initiator, page).toList());
    }

    @Override
    public EventFullDto saveEventPrivate(Long userId, EventRequestDto eventRequestDto) {
        LocalDateTime eventDate = LocalDateTime.parse(eventRequestDto.getEventDate(), dateTimeFormatter);
        if (eventDate.plusHours(2).isBefore(LocalDateTime.now())) {
            throw new ResponseException("Event date is incorrect");
        }
        if (eventRequestDto.getPaid() == null) {
            eventRequestDto.setPaid(false);
        }
        if (eventRequestDto.getRequestModeration() == null) {
            eventRequestDto.setRequestModeration(true);
        }
        Location location = locationService.save(eventRequestDto.getLocation());
        Category category = categoryRepository.findById(eventRequestDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(eventRequestDto.getCategory()));
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = EventMapper.toEntityFromRequest(eventRequestDto, location, category, initiator);
        EventFullDto eventFullDto = EventMapper.toFullDtoFromEntity(eventRepository.save(event), event.getConfirmedRequests());
        if (eventFullDto.getState().equals("WAITING")) {
            eventFullDto.setState("PENDING");
        }
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventPrivate(Long userId, Long eventId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findByIdAndInitiator(eventId, initiator);
        return EventMapper.toFullDtoFromEntity(event, event.getConfirmedRequests());
    }

    @Override
    public EventFullDto patchEventPrivate(UpdateEventAdminRequest updateEventAdminRequest, Long userId, Long eventId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState().equals("PUBLISHED")) {
            throw new ValidationException(String.format("Event with ID =%d is PUBLISHED", userId));
        }
        if (!initiator.equals(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)))) {
            throw new ValidationException(String.format("User with ID =%d is not initiator", userId));
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new ValidationException(String.format("Category with id %d not found", updateEventAdminRequest.getCategory())));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), dateTimeFormatter);
            if (eventDate.plusHours(2).isBefore(LocalDateTime.now())) {
                throw new ResponseException("Event date is incorrect");
            }
            event.setEventDate(eventDate);
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(locationService.save(updateEventAdminRequest.getLocation()));
        }
        if (updateEventAdminRequest.getParticipantLimit() != 0) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState("PENDING");
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEventAdminRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState("CANCELED");
                event.setPublishedOn(null);
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        event.setId(eventId);
        EventFullDto eventFullDto = EventMapper.toFullDtoFromEntity(eventRepository.save(event), event.getConfirmedRequests());
        if (eventFullDto.getState().equals("WAITING")) {
            eventFullDto.setState("PENDING");
        }
        return eventFullDto;
    }


    @Override
    public List<EventFullDto> getEventsPublic(String text, List<Long> categories, String paid, String rangeStart,
                                              String rangeEnd, String onlyAvailable, String sort, Integer from,
                                              Integer size, String uri, String ip) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        LocalDateTime start;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        } else {
            start = LocalDateTime.now();
        }
        LocalDateTime end;
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        } else {
            end = LocalDateTime.of(3000, 1, 1, 1, 1);
        }
        if (end.isBefore(LocalDateTime.now()) || end.isBefore(start)) {
            throw new ResponseException("Incorrectly made request");
        }
        List<Boolean> listPaid = new ArrayList<>();
        if (paid == null) {
            listPaid.add(false);
            listPaid.add(true);
        } else {
            if (paid.equals("true")) {
                listPaid.add(true);
            }
            if (paid.equals("false")) {
                listPaid.add(false);
            }
        }
        if (categories.isEmpty()) {
            client.postHit(uri, ip);
            return EventMapper.toListEventFullDtoFromListEvent(
                    eventRepository.getEventsNoCategory(text, listPaid, start, end, page).toList());
        }
        client.postHit(uri, ip);
        return EventMapper.toListEventFullDtoFromListEvent(
                eventRepository.findByQuery(text, categories, listPaid, start, end, page).toList());
    }

    @Override
    public EventFullDto getEventPublic(Long id, String uri, String ip) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        if (event.getState().equals("WAITING")) {
            throw new EventNotFoundException(id);
        }
        client.postHit(uri, ip);
        event.setViews(client.getViews(uri));
        eventRepository.save(event);
        return EventMapper.toFullDtoFromEntity(event, event.getConfirmedRequests());
    }

    @Override
    public boolean findByCategory(Category category) {
        return eventRepository.findByCategory(category).isPresent();
    }

    @Override
    public List<Event> findByIds(List<Long> eventIds) {
        return eventRepository.findByIds(eventIds);
    }

    @Override
    public Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    public void updateConfirmedRequest(int confirmedRequest, Long eventId) {
        eventRepository.updateConfirmedRequest(confirmedRequest, eventId);
    }

}
