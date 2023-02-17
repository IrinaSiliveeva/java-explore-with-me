package ru.practicum.mapper.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.CommentEventDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.dto.UserCommentDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.Comment;
import ru.practicum.storage.event.EventRepository;
import ru.practicum.storage.user.UserRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    public Comment mapFromDto(NewCommentDto newCommentDto, Long eventId, Long authorId) {
        return Comment.builder()
                .author(userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("Пользователь не найден")))
                .event(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено")))
                .created(LocalDateTime.now())
                .text(newCommentDto.getText())
                .build();
    }

    public static CommentEventDto mapFromComment(Comment comment) {
        return CommentEventDto.builder()
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public UserCommentDto mapFromCommentToUserComment(Comment comment) {
        return UserCommentDto.builder()
                .id(comment.getId())
                .author(userMapper.mapToShortUser(comment.getAuthor()))
                .event(eventMapper.mapToShortEvent(comment.getEvent()))
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}
