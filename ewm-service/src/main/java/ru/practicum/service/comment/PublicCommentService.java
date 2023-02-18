package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventShortDto;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.storage.comment.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCommentService {
    private final CommentRepository commentRepository;
    public final EventMapper eventMapper;

    public List<EventShortDto> getAllEventsWithComment(PageRequest pageRequest) {
        List<Event> eventList = commentRepository.findAll(pageRequest).stream()
                .map(Comment::getEvent).collect(Collectors.toList());
        return eventList.stream().map(eventMapper::mapToShortEvent).collect(Collectors.toList());
    }
}
