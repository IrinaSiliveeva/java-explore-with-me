package ru.practicum.mapper.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.client.Client;
import ru.practicum.client.dto.ViewStatsEndpointDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.Location;
import ru.practicum.dto.NewEventDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.Event;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.Status;
import ru.practicum.storage.category.CategoryRepository;
import ru.practicum.storage.comment.CommentRepository;
import ru.practicum.storage.request.RequestRepository;
import ru.practicum.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.StatConstant.APP_NAME;
import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final Client client;

    public EventFullDto mapToEventFull(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapFromCategory(event.getCategory()))
                .confirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(), Status.CONFIRMED))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userMapper.mapToShortUser(event.getInitiator()))
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(getViewsFromStatsServer(event.getId()))
                .comments(commentRepository.findAllByEventId(event.getId()).stream().map(CommentMapper::mapFromComment).collect(Collectors.toSet()))
                .build();
    }

    public Event mapToEvent(Long userId, NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена")))
                .initiator(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден")))
                .description(newEventDto.getDescription())
                .title(newEventDto.getTitle())
                .eventDate(newEventDto.getEventDate())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .createdOn(LocalDateTime.now())
                .requestModeration(newEventDto.isRequestModeration())
                .state(State.PENDING)
                .build();
    }

    public EventShortDto mapToShortEvent(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .category(categoryMapper.mapFromCategory(event.getCategory()))
                .confirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(), Status.CONFIRMED))
                .eventDate(event.getEventDate())
                .initiator(userMapper.mapToShortUser(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .views(getViewsFromStatsServer(event.getId()))
                .comments(commentRepository.findAllByEventId(event.getId()).stream().map(CommentMapper::mapFromComment).collect(Collectors.toSet()))
                .build();
    }

    private Long getViewsFromStatsServer(Long eventId) {
        String end = LocalDateTime.now().plusYears(1000).format(DateTimeFormatter.ofPattern(TIME_PATTERN));
        String start = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(TIME_PATTERN));
        List<ViewStatsEndpointDto> result = client.getStats(start, end, new String[]{"/events/" + eventId}, false);
        if (result != null && result.size() > 0) {
            result = result.stream().filter(x -> APP_NAME.equals(x.getApp())).collect(Collectors.toList());
            return result.size() > 0 ? result.get(0).getHits() : 0L;
        } else {
            return 0L;
        }
    }
}
