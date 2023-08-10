package ru.practicum.main;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.EventRequestDto;
import ru.practicum.main.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.exception.model.ResponseException;
import ru.practicum.main.request.service.RequestService;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.ws.rs.QueryParam;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PrivateController {
    EventService eventService;
    RequestService requestService;

    @GetMapping("/events")
    public List<EventDto> getEvents(@PathVariable Long userId,
                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET event by user id = {} request", userId);
        return eventService.getEventsPrivate(userId, from, size);
    }

    @PostMapping("/events")
    public ResponseEntity<EventDto> saveEvent(@PathVariable Long userId,
                                              @RequestBody @Valid EventRequestDto eventRequestDto) {
        log.info("POST event request");
        return new ResponseEntity<>(eventService.saveEventPrivate(userId, eventRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/events/{eventId}")
    public EventDto getEvent(@PathVariable Long userId,
                             @PathVariable Long eventId) {
        log.info("GET event by id = {} request", eventId);
        return eventService.getEventPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventDto patchEvent(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest,
                               @PathVariable Long userId,
                               @PathVariable Long eventId) {
        log.info("PATCH event by parameters request");
        return eventService.patchEventPrivate(updateEventAdminRequest, userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("GET requests by user id = {} and event id = {} request", userId, eventId);
        return requestService.getRequests(userId, eventId);
    }


    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequest(@RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                       @PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        log.info("GET request by user id = {} and event id = {} request", userId, eventId);
        return requestService.patchRequest(eventRequestStatusUpdateRequest, userId, eventId);
    }


    @GetMapping("/requests")
    public List<ParticipationRequestDto> getAllRequests(@PathVariable Long userId) {
        log.info("GET request request");
        return requestService.getAllRequest(userId);
    }

    @PostMapping("/requests")
    public ResponseEntity<ParticipationRequestDto> saveRequest(@PathVariable Long userId,
                               @QueryParam(value = "eventId") Long eventId) {
        log.info("GET requests request");
        if (eventId == null) {
            throw new ResponseException("eventId == null");
        }
        return new ResponseEntity<>(requestService.saveRequest(userId, eventId), HttpStatus.CREATED);

    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                   @PathVariable Long requestId) {
        log.info("PATCH request request");
        return requestService.cancelRequest(userId, requestId);
    }

}
