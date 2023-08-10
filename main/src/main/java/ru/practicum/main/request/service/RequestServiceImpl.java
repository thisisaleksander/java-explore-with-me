package ru.practicum.main.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.event.Event;
import ru.practicum.main.exception.model.RequestNotFoundException;
import ru.practicum.main.exception.model.ValidationException;
import ru.practicum.main.request.Request;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.user.service.UserService;
import ru.practicum.main.user.User;
import ru.practicum.main.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {
    RequestRepository requestRepository;
    UserService userService;
    EventService eventService;

    DateUtils utils = new DateUtils();

    @Override
    public List<ParticipationRequestDto> getAllRequest(Long userId) {
        User requester = userService.findById(userId);
        return RequestMapper.mapToMultipleParticipationRequestsDto(
                requestRepository.findByRequester(requester));
    }

    @Override
    public EventRequestStatusUpdateResult patchRequest(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long userId,
            Long eventId) {
        Event event = eventService.findById(eventId);
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ValidationException("Confirmed requests has already finished");
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
            confirmedParticipationRequest.add(RequestMapper.mapToParticipationRequestDto(request));
        }
        for (Request request:rejectedRequest
        ) {
            rejectedParticipationRequest.add(RequestMapper.mapToParticipationRequestDto(request));
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
            throw new ValidationException("User is not the initiator of this request");
        }
        return RequestMapper.mapToMultipleParticipationRequestsDto(requestRepository.findByEventId(eventId));
    }

    @Override
    public ParticipationRequestDto saveRequest(Long userId, Long eventId) {
        Request request = new Request();
        request.setCreated(utils.now());
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
                    throw new ValidationException("Request already exists");
                }
            }
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ValidationException("Confirmed requests has already finished");
        }
        request.setEvent(event);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus("CONFIRMED");
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.save(event);
        } else {
            request.setStatus("PENDING");
        }
        return RequestMapper.mapToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        if (request.getRequester().getId() != userId) {
            throw new ValidationException("User is not requester");
        }
        request.setStatus("CANCELED");
        return RequestMapper.mapToParticipationRequestDto(requestRepository.save(request));
    }
}
