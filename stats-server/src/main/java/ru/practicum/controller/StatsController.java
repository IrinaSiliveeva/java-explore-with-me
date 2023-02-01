package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void createEndpointHit(@RequestBody EndpointHit endpointHit) {
        log.info("С ip {} получен запрос к {} по адресу {}", endpointHit.getIp(), endpointHit.getApp(), endpointHit.getUri());
        statsService.createEndpointHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam List<String> uris,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получение статистики с {} по {} по адресам {} c уникальным ip {}", start, end, uris, unique);
        return statsService.getStats(uris, start, end, unique);
    }
}
