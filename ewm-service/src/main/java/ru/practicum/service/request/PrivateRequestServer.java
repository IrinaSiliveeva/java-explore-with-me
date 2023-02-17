package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.Status;
import ru.practicum.storage.event.EventRepository;
import ru.practicum.storage.request.RequestRepository;
import ru.practicum.storage.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateRequestServer {
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Событие не опубликовано");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Пользователь является инициатором события");
        }
        if (requestRepository.existsByRequesterId(userId)) {
            throw new ConflictException("Пользователь уже отправил запрос");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requestRepository.countAllByEventIdIs(eventId)) {
            throw new ConflictException("Достигнут максимальный лимит на событие");
        }
        Request request = requestMapper.mapToRequest(user, event);
        return requestMapper.mapToParticipationRequest(requestRepository.save(request));
    }

    public List<ParticipationRequestDto> getUsersRequests(Long userId) {
        return requestRepository.findAllByRequesterId(userId).stream().map(requestMapper::mapToParticipationRequest).collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId);
        request.setStatus(Status.CANCELED);
        return requestMapper.mapToParticipationRequest(requestRepository.save(request));
    }
}
