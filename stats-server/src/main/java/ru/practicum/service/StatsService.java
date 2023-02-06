package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsEndpointDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.storage.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper mapper;

    public void createEndpointHit(EndpointHitDto endpointHit) {
        statsRepository.save(mapper.toEndpoint(endpointHit));
    }

    public List<ViewStatsEndpointDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, Boolean unique) {
        if (unique) {
            return statsRepository.getStatsUnique(uris, start, end);
        } else {
            return statsRepository.getStats(uris, start, end);
        }
    }
}
