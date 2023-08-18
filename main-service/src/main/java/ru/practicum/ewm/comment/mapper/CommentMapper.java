package ru.practicum.ewm.comment.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.dto.ParticipationCommentDto;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.utils.DateUtils.dateTimeFormatter;

@NoArgsConstructor
public class CommentMapper {
    public static ParticipationCommentDto toParticipationCommentDtoFromComment(Comment comment) {
        ParticipationCommentDto participationCommentDto = new ParticipationCommentDto();
        participationCommentDto.setId(comment.getId());
        participationCommentDto.setCreated(comment.getCreated().format(dateTimeFormatter));
        if (comment.getUpdated() != null) {
            participationCommentDto.setUpdated(comment.getUpdated().format(dateTimeFormatter));
        }
        participationCommentDto.setEvent(comment.getEvent().getId());
        participationCommentDto.setCommentator(comment.getCommentator().getId());
        participationCommentDto.setStatus(comment.getStatus());
        participationCommentDto.setText(comment.getText());
        return participationCommentDto;
    }

    public static List<ParticipationCommentDto> toListParticipationCommentDtoFromListComment(List<Comment> listComments) {
        List<ParticipationCommentDto> listParticipationCommentDto = new ArrayList<>();
        for (Comment comment : listComments) {
            listParticipationCommentDto.add(toParticipationCommentDtoFromComment(comment));
        }
        return listParticipationCommentDto;
    }
}
