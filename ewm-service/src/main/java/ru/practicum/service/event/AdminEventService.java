package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.StateActionAdmin;
import ru.practicum.storage.category.CategoryRepository;
import ru.practicum.storage.event.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    public List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest) {
        return eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(users, states.stream().map(State::valueOf).collect(Collectors.toList()),
                categories, rangeStart, rangeEnd, pageRequest).stream().map(eventMapper::mapToEventFull).collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (updateEventAdminRequest.getStateAction() == StateActionAdmin.PUBLISH_EVENT) {
            if (event.getState() != State.PENDING) {
                throw new ConflictException("Событие не может быть опубликовано");
            }
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
        }
        if (updateEventAdminRequest.getStateAction() == StateActionAdmin.REJECT_EVENT) {
            if (event.getState() == State.PUBLISHED && event.getPublishedOn().isBefore(LocalDateTime.now())) {
                throw new ConflictException("Событие не может быть отменино");
            }
            event.setState(State.CANCELED);
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Дата события должна быть минимум за два часа");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        return eventMapper.mapToEventFull(eventRepository.save(event));
    }
}
