package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UserCommentDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.storage.comment.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventMapper eventMapper;

    @Transactional
    public void deleteCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий не напйден"));
        commentRepository.delete(comment);
    }

    public UserCommentDto getById(Long commentId) {
        return commentMapper.mapFromCommentToUserComment(commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден")));
    }

    public List<EventFullDto> getAllCommentsByEvent(Long eventId) {
        List<Event> eventList = commentRepository.findAllByEventId(eventId).stream()
                .map(Comment::getEvent).collect(Collectors.toList());
        return eventList.stream().map(eventMapper::mapToEventFull).collect(Collectors.toList());
    }
}
