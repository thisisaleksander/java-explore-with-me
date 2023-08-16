package ru.practicum.ewm.request;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.ParticipationRequestDto;

import java.util.List;

@Transactional(readOnly = true)
public interface RequestService {
    List<ParticipationRequestDto> getAllRequest(Long userId);

    @Transactional
    EventRequestStatusUpdateResult patchRequest(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    @Transactional
    ParticipationRequestDto saveRequest(Long userId, Long eventId);

    @Transactional
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
