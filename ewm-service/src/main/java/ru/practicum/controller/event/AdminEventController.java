package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.service.event.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> getAllEventsByAdmin(@UniqueElements @RequestParam(defaultValue = "") List<Long> users,
                                                  @UniqueElements @RequestParam(defaultValue = "") List<String> states,
                                                  @UniqueElements @RequestParam(defaultValue = "") List<Long> categories,
                                                  @RequestParam(defaultValue = "") @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeStart,
                                                  @RequestParam(defaultValue = "") @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение администратором всех событий с параметрами: users {}, states {}, categories {}, rangeStart {}, rangeEnd {}, from {}, size {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return service.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId, @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Изменение события {} администратором на {}", eventId, updateEventAdminRequest);
        return service.updateEventByAdmin(eventId, updateEventAdminRequest);
    }
}
