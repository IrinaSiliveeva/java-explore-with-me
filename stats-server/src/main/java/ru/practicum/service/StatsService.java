package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.storage.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    public final StatsRepository statsRepository;

    public void createEndpointHit(EndpointHit endpointHit) {
        statsRepository.save(endpointHit);
    }

    public List<ViewStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, Boolean unique) {
        if (unique) {
            return statsRepository.getStatsUnique(uris, start, end);
        } else {
            return statsRepository.getStats(uris, start, end);
        }
    }
}
