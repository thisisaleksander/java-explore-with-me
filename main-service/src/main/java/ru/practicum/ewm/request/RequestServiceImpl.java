package ru.practicum.ewm.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.model.*;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {

    RequestRepository requestRepository;

    UserService userService;

    EventService eventService;

    @Override
    public List<ParticipationRequestDto> getAllRequest(Long userId) {
        User requester = userService.findById(userId);

        return RequestMapper.toListParticipationRequestDtoFromListRequest(requestRepository.findByRequester(requester));
    }

    @Override
    public EventRequestStatusUpdateResult patchRequest(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId) {
        Event event = eventService.findById(eventId);
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ValidationException("Confirmed requests is over");
        }
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        String status = eventRequestStatusUpdateRequest.getStatus();
        requestRepository.confirmRequestStatus(status, requestIds);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<Request> confirmedRequest = requestRepository.findAllByStatusAndEventId("CONFIRMED", eventId);
        List<Request> rejectedRequest = requestRepository.findAllByStatusAndEventId("REJECTED", eventId);
        List<ParticipationRequestDto> confirmedParticipationRequest = new ArrayList<>();
        List<ParticipationRequestDto> rejectedParticipationRequest = new ArrayList<>();
        for (Request request:confirmedRequest
        ) {
            confirmedParticipationRequest.add(RequestMapper.toParticipationRequestDtoFromRequest(request));
        }
        for (Request request:rejectedRequest
        ) {
            rejectedParticipationRequest.add(RequestMapper.toParticipationRequestDtoFromRequest(request));
        }
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmedParticipationRequest);
        eventRequestStatusUpdateResult.setRejectedRequests(rejectedParticipationRequest);
        eventService.updateConfirmedRequest(confirmedParticipationRequest.size(), eventId);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        Event event = eventService.findById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ValidationException("User is not initiator");
        }
        return RequestMapper.toListParticipationRequestDtoFromListRequest(requestRepository.findByEventId(eventId));
    }

    @Override
    public ParticipationRequestDto saveRequest(Long userId, Long eventId) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(userService.findById(userId));
        Event event = eventService.findById(eventId);
        if (event.getInitiator().getId() == userId) {
            throw new ValidationException("Requester is initiator");
        }
        if (!event.getState().equals("PUBLISHED")) {
            throw new ValidationException("Event is not PUBLISHED");
        }
        List<Request> requestList = requestRepository.findByRequesterId(userId);
        if (!requestList.isEmpty()) {
            for (Request requestFromRepository:requestList
            ) {
                if (requestFromRepository.getEvent().equals(event)) {
                    throw new ValidationException("Request is exist");
                }
            }
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ValidationException("Confirmed requests is over");
        }
        request.setEvent(event);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus("CONFIRMED");
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.save(event);
        } else {
            request.setStatus("PENDING");
        }
        return RequestMapper.toParticipationRequestDtoFromRequest(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getRequester().getId() != userId) {
            throw new ValidationException("User is not requester");
        }
        request.setStatus("CANCELED");
        return RequestMapper.toParticipationRequestDtoFromRequest(requestRepository.save(request));
    }
}
