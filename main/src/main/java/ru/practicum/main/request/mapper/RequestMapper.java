package ru.practicum.main.request.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.main.request.Request;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.utils.DateUtils.dateTimeFormatter;

@NoArgsConstructor
public class RequestMapper {
    public static ParticipationRequestDto mapToParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setCreated(request.getCreated().format(dateTimeFormatter));
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }

    public static List<ParticipationRequestDto> mapToMultipleParticipationRequestsDto(
            List<Request> listRequester) {
        List<ParticipationRequestDto> listParticipationRequestDto = new ArrayList<>();
        for (Request request:listRequester
             ) {
            listParticipationRequestDto.add(mapToParticipationRequestDto(request));
        }
        return listParticipationRequestDto;
    }
}
