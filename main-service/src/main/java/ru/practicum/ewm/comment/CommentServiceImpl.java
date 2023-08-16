package ru.practicum.ewm.comment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentMapper;
import ru.practicum.ewm.comment.model.ParticipationCommentDto;
import ru.practicum.ewm.comment.model.StatusUpdateComment;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;

    UserService userService;

    EventService eventService;

    @Override
    public List<ParticipationCommentDto> getAllCommentsForEvent(Long eventId, Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return CommentMapper.toListParticipationCommentDtoFromListComment(commentRepository.findByEventIdAndStatus(eventId, "PUBLISHED", page).toList());
    }

    @Override
    public List<ParticipationCommentDto> getAllCommentsPrivate(Long userId, Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return CommentMapper.toListParticipationCommentDtoFromListComment(commentRepository.findByCommentatorId(userId, page).toList());
    }

    @Override
    public ParticipationCommentDto saveComment(ParticipationCommentDto participationCommentDto, Long userId, Long eventId) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setCommentator(userService.findById(userId));
        Event event = eventService.findById(eventId);
        if (!event.getState().equals("PUBLISHED")) {
            throw new ValidationException("Event is not PUBLISHED");
        }
        comment.setEvent(event);
        comment.setStatus("PENDING");
        comment.setText(participationCommentDto.getText());
        return CommentMapper.toParticipationCommentDtoFromComment(commentRepository.save(comment));
    }

    @Override
    public ParticipationCommentDto patchCommentPrivate(ParticipationCommentDto participationCommentDto, Long userId, Long commentId) {
        User commentator = userService.findById(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!commentator.equals(comment.getCommentator())) {
            throw new ValidationException("User is not commentator");
        }
        comment.setText(participationCommentDto.getText());
        comment.setStatus("PENDING");
        comment.setUpdated(LocalDateTime.now());
        ParticipationCommentDto commentDto = CommentMapper.toParticipationCommentDtoFromComment(commentRepository.save(comment));
        commentDto.setStatus("WAITING");
        return commentDto;
    }

    @Override
    public ParticipationCommentDto getCommentPrivate(Long userId, Long commentId) {
        User commentator = userService.findById(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!commentator.equals(comment.getCommentator())) {
            throw new ValidationException("User is not commentator");
        }
        return CommentMapper.toParticipationCommentDtoFromComment(comment);
    }

    @Override
    public void deleteCommentPrivate(Long userId, Long commentId) {
        User commentator = userService.findById(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!commentator.equals(comment.getCommentator())) {
            throw new ValidationException("User is not commentator");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<ParticipationCommentDto> getCommentsAdmin(Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return CommentMapper.toListParticipationCommentDtoFromListComment(commentRepository.findByStatus("PENDING", page).toList());
    }

    @Override
    public ParticipationCommentDto patchCommentsAdmin(StatusUpdateComment statusUpdateComment, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (statusUpdateComment.getStatus().equals("PUBLISH_COMMENT")) {
            comment.setStatus("PUBLISHED");
        }
        if (statusUpdateComment.getStatus().equals("REJECT_COMMENT")) {
            comment.setStatus("REJECTED");
        }
        return CommentMapper.toParticipationCommentDtoFromComment(commentRepository.save(comment));
    }
}
