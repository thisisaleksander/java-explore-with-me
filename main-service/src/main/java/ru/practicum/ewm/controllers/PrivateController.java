package ru.practicum.ewm.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.comment.dto.ParticipationCommentDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.model.UpdateEventAdminRequest;
import ru.practicum.ewm.exception.model.ResponseException;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    CommentService commentService;

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

    @GetMapping("/comments")
    public List<ParticipationCommentDto> getAllCommentsPrivate(@PathVariable Long userId,
                                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get comments userId={}", userId);
        return commentService.getAllCommentsPrivate(userId, from, size);
    }

    @PostMapping("/comments")
    public ResponseEntity<ParticipationCommentDto> saveComment(@RequestBody @Valid ParticipationCommentDto participationCommentDto,
                                                               @PathVariable Long userId,
                                                               @QueryParam(value = "eventId") @NotNull Long eventId) {
        log.info("Post Comment for userId={}, eventId={}", userId, eventId);
        return new ResponseEntity<ParticipationCommentDto>(commentService.saveComment(participationCommentDto, userId, eventId), HttpStatus.CREATED);

    }

    @GetMapping("/comments/{commentId}")
    public ParticipationCommentDto getCommentPrivate(@PathVariable Long userId,
                                                     @PathVariable Long commentId) {
        return commentService.getCommentPrivate(userId, commentId);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ParticipationCommentDto> patchCommentPrivate(@RequestBody @Valid ParticipationCommentDto participationCommentDto,
                                                                       @PathVariable Long userId,
                                                                       @PathVariable Long commentId) {
        return new ResponseEntity<ParticipationCommentDto>(commentService.patchCommentPrivate(participationCommentDto, userId, commentId), HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ParticipationCommentDto> deleteCommentPrivate(@PathVariable Long userId,
                                                                        @PathVariable Long commentId) {
        log.info("Delete Comment for userId={}, commentId={}", userId, commentId);
        commentService.deleteCommentPrivate(userId, commentId);
        return new  ResponseEntity<ParticipationCommentDto>(HttpStatus.NO_CONTENT);
    }
}
