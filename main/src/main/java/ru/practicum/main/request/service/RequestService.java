package ru.practicum.main.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

@Transactional(readOnly = true)
public interface RequestService {
    List<ParticipationRequestDto> getAllRequest(Long userId);

    @Transactional
    ParticipationRequestDto saveRequest(Long userId, Long eventId);

    @Transactional
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    @Transactional
    EventRequestStatusUpdateResult patchRequest(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long userId,
            Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);
}
