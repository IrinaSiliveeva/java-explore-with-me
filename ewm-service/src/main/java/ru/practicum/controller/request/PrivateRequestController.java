package ru.practicum.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.request.PrivateRequestServer;

import java.util.List;

@RestController
@RequestMapping("users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final PrivateRequestServer server;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Создание пользователем {} запроса на событие {}", userId, eventId);
        return server.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getUsersRequests(@PathVariable Long userId) {
        log.info("Получение пользователем {} запросов", userId);
        return server.getUsersRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Отмена пользователем {} запроса {}", userId, requestId);
        return server.cancelRequest(userId, requestId);
    }
}
