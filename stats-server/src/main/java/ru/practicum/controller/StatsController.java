package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsEndpointDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndpointHit(@RequestBody @Validated EndpointHitDto endpointHitDto) {
        log.info("С ip {} получен запрос к {} по адресу {}", endpointHitDto.getIp(), endpointHitDto.getApp(), endpointHitDto.getUri());
        statsService.createEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsEndpointDto> getStats(@RequestParam List<String> uris,
                                               @RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime start,
                                               @RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime end,
                                               @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получение статистики с {} по {} по адресам {} c уникальным ip {}", start, end, uris, unique);
        return statsService.getStats(uris, start, end, unique);
    }
}
