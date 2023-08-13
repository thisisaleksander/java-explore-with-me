package ru.practicum.ewm.contrillers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.model.EventFullDto;
import ru.practicum.ewm.event.model.EventRequestDto;
import ru.practicum.ewm.event.model.UpdateEventAdminRequest;
import ru.practicum.ewm.exception.ResponseException;
import ru.practicum.ewm.request.RequestService;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateController {

    EventService eventService;

    RequestService requestService;

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@PathVariable Long userId,
                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get events from={}, size={}", from, size);
        return eventService.getEventsPrivate(userId, from, size);
    }



    @PostMapping("/events")
    public ResponseEntity<EventFullDto> saveEvent(@PathVariable Long userId,
                           @RequestBody @Valid EventRequestDto eventRequestDto) {
        System.out.println(eventRequestDto);
        return new ResponseEntity<EventFullDto>(eventService.saveEventPrivate(userId, eventRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Get events userId={}, eventId={}", userId, eventId);
        return eventService.getEventPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest,
                            @PathVariable Long userId,
                            @PathVariable Long eventId) {
        return eventService.patchEventPrivate(updateEventAdminRequest, userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("Get events userId={}, eventId = {}", userId, eventId);
        return requestService.getRequests(userId, eventId);
    }


    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequest(@RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                       @PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        return requestService.patchRequest(eventRequestStatusUpdateRequest, userId, eventId);
    }


    @GetMapping("/requests")
    public List<ParticipationRequestDto> getAllRequests(@PathVariable Long userId) {
        log.info("Get requests userId={}", userId);
        return requestService.getAllRequest(userId);
    }

    @PostMapping("/requests")
    public ResponseEntity<ParticipationRequestDto> saveRequest(@PathVariable Long userId,
                               @QueryParam(value = "eventId") Long eventId) {
        log.info("Post requests userId={}, eventId={}", userId, eventId);
        if (eventId == null) {
            throw new ResponseException("eventId == null");
        }
        return new ResponseEntity<ParticipationRequestDto>(requestService.saveRequest(userId,eventId), HttpStatus.CREATED);

    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                   @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

}
