package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.Client;
import ru.practicum.client.dto.EndpointHitDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.service.event.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.StatConstant.APP_NAME;
import static ru.practicum.util.StatConstant.TIME_PATTERN;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final PublicEventService service;
    private final Client client;

    @GetMapping
    public List<EventShortDto> getAllPublicEvents(@RequestParam String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @FutureOrPresent @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Positive @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest httpServletRequest) {
        log.info("Публичное получение всех событий с параметрами: text {}, categories {}, paid {}, rangeStart {}, rangeEnd {}, onlyAvailable {}, sort {}, from {}, size {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        client.addToStat(new EndpointHitDto(APP_NAME, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
        return service.getAllPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Публичное получение события {}", eventId);
        client.addToStat(new EndpointHitDto(APP_NAME, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
        return service.getEventById(eventId);
    }
}
