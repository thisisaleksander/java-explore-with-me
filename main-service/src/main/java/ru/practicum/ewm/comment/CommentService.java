package ru.practicum.ewm.comment;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.model.ParticipationCommentDto;
import ru.practicum.ewm.comment.model.StatusUpdateComment;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentService {
    List<ParticipationCommentDto> getAllCommentsForEvent(Long eventId, Integer from, Integer size);

    List<ParticipationCommentDto> getAllCommentsPrivate(Long userId, Integer from, Integer size);

    @Transactional
    ParticipationCommentDto saveComment(ParticipationCommentDto participationCommentDto, Long userId, Long eventId);

    @Transactional
    ParticipationCommentDto patchCommentPrivate(ParticipationCommentDto participationCommentDto, Long userId, Long commentId);

    ParticipationCommentDto getCommentPrivate(Long userId, Long commentId);

    @Transactional
    void deleteCommentPrivate(Long userId, Long commentId);

    List<ParticipationCommentDto> getCommentsAdmin(Integer from, Integer size);

    @Transactional
    ParticipationCommentDto patchCommentsAdmin(StatusUpdateComment statusUpdateComment, Long commentId);
}
