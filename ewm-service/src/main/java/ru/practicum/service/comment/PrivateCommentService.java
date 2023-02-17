package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.dto.UserCommentDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.enums.State;
import ru.practicum.storage.comment.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateCommentService {
    private final CommentRepository repository;
    private final CommentMapper commentMapper;

    public UserCommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        Comment comment = commentMapper.mapFromDto(newCommentDto, eventId, userId);
        if (comment.getEvent().getState().equals(State.CANCELED) || comment.getEvent().getState().equals(State.PENDING)) {
            throw new ConflictException("Нельзя оставить комментарий не опубликованному событию");
        }
        return commentMapper.mapFromCommentToUserComment(repository.save(comment));
    }

    public UserCommentDto updateComment(Long commentId, Long userId, Long eventId, NewCommentDto newCommentDto) {
        Comment comment = getCommentByUserAndEvent(commentId, userId, eventId);
        comment.setText(newCommentDto.getText());
        return commentMapper.mapFromCommentToUserComment(repository.save(comment));
    }

    public void deleteComment(Long commentId, Long userId, Long eventId) {
        Comment comment = getCommentByUserAndEvent(commentId, userId, eventId);
        repository.delete(comment);
    }

    public List<UserCommentDto> getUsersCommentsByEvent(Long userId, Long eventId) {
        return (repository.findAllByAuthorIdAndEventId(userId, eventId).stream().map(commentMapper::mapFromCommentToUserComment).collect(Collectors.toList()));
    }

    public List<UserCommentDto> getUsersComments(Long userId) {
        return repository.findAllByAuthorId(userId).stream().map(commentMapper::mapFromCommentToUserComment).collect(Collectors.toList());
    }

    private Comment getCommentByUserAndEvent(Long commentId, Long userId, Long eventId) {
        return repository.findByIdAndAuthorIdAndEventId(commentId, userId, eventId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
    }
}
