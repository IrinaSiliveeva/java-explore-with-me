package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.StateActionUser;
import ru.practicum.model.enums.Status;
import ru.practicum.storage.category.CategoryRepository;
import ru.practicum.storage.event.EventRepository;
import ru.practicum.storage.request.RequestRepository;
import ru.practicum.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public EventFullDto createByUser(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Дата начала события должна быть минимум за два часа");
        }
        Event event = eventMapper.mapToEvent(userId, newEventDto);
        event.setPublishedOn(LocalDateTime.now());
        return eventMapper.mapToEventFull(eventRepository.save(event));
    }

    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return eventMapper.mapToEventFull(event);
    }

    public List<EventFullDto> getAllUserEvents(Long userId, PageRequest pageRequest) {
        return eventRepository.findAllByInitiatorId(userId, pageRequest).stream().map(eventMapper::mapToEventFull).collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие не может быть отредактировано, так как оно уже опубликовано");
        }
        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Время начало события должно быть минимум за два часа");
            }
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (updateEventUserRequest.getStateAction() == StateActionUser.SEND_TO_REVIEW) {
            event.setState(State.PENDING);
        }
        if (updateEventUserRequest.getStateAction() == StateActionUser.CANCEL_REVIEW) {
            event.setState(State.CANCELED);
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLat(updateEventUserRequest.getLocation().getLat());
            event.setLon(updateEventUserRequest.getLocation().getLon());
        }
        return eventMapper.mapToEventFull(eventRepository.save(event));
    }

    public List<ParticipationRequestDto> getUsersRequestsByEventId(Long userId, Long eventId) {
        return requestRepository.findAllByEventInitiatorIdAndEventId(userId, eventId).stream()
                .map(requestMapper::mapToParticipationRequest).collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        switch (Status.valueOf(eventRequestStatusUpdateRequest.getStatus())) {
            case CONFIRMED:
                for (Request request : requestRepository.findAllByEventIdAndIdIn(eventId, eventRequestStatusUpdateRequest.getRequestIds())) {
                    try {
                        if (!request.getStatus().equals(Status.PENDING)) {
                            throw new ConflictException("Статус не в ожидании");
                        }
                    } catch (ConflictException e) {
                        continue;
                    }
                    if (event.getParticipantLimit() - requestRepository.countByEventIdAndStatus(eventId, Status.CONFIRMED) <= 0) {
                        throw new ConflictException("Достигнут лимит на это событие");
                    } else {
                        request.setStatus(Status.CONFIRMED);
                        request = requestRepository.save(request);
                        result.getConfirmedRequests().add(requestMapper.mapToParticipationRequest(request));
                    }
                }
                return result;
            case REJECTED:
                for (Request request : requestRepository.findAllByEventIdAndIdIn(eventId, eventRequestStatusUpdateRequest.getRequestIds())) {
                    if (!request.getStatus().equals(Status.PENDING)) {
                        throw new ConflictException("Статус не в ожидании");
                    }
                    request.setStatus(Status.REJECTED);
                    request = requestRepository.save(request);
                    result.getRejectedRequests().add(requestMapper.mapToParticipationRequest(request));
                }
                return result;
        }
        return result;
    }
}
