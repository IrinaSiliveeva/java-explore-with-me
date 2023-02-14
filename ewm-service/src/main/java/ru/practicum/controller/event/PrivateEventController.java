package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.event.PrivateEventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final PrivateEventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createByUser(@PathVariable Long userId,
                                     @RequestBody @Validated NewEventDto newEventDto) {
        log.info("Создание пользователем {} события {}", userId, newEventDto);
        return service.createByUser(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение пользователем {} события {}", userId, eventId);
        return service.getEventById(userId, eventId);
    }

    @GetMapping
    public List<EventFullDto> getAllUserEvents(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение пользователем {} всех своих событий с параметрами: from {}, size {}", userId, from, size);
        return service.getAllUserEvents(userId, PageRequest.of(from / size, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                          @RequestBody @Validated UpdateEventUserRequest updateEventUserRequest) {
        log.info("Изменение пользователем {} события {} на {}", userId, eventId, updateEventUserRequest);
        return service.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUsersRequestsByEventId(@PathVariable Long userId,
                                                                   @PathVariable Long eventId) {
        log.info("Получение пользователем {} запросов на событие {}", userId, eventId);
        return service.getUsersRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Изменение пользователем {} запросов {} на событие {}", userId, eventRequestStatusUpdateRequest, eventId);
        return service.updateEventRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
