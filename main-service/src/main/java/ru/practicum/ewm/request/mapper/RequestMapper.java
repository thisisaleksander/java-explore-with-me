package ru.practicum.ewm.request.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.utils.DateUtils.dateTimeFormatter;

@NoArgsConstructor
public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDtoFromRequest(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setCreated(request.getCreated().format(dateTimeFormatter));
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }

    public static List<ParticipationRequestDto> toListParticipationRequestDtoFromListRequest(List<Request> listRequester) {
        List<ParticipationRequestDto> listParticipationRequestDto = new ArrayList<>();
        for (Request request:listRequester
             ) {
            listParticipationRequestDto.add(toParticipationRequestDtoFromRequest(request));
        }
        return listParticipationRequestDto;
    }
}
