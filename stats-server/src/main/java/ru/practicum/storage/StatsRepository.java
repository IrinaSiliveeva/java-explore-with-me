package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.dto.ViewStatsDto (e.app,e.uri,count (e.ip)) from EndpointHit e " +
            "where e.uri in :uris and (timestamp > :start and timestamp < :end) group by e.app,e.uri order by count (e.ip) desc ")
    List<ViewStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.ViewStatsDto (e.app,e.uri,count (distinct e.ip)) from EndpointHit e " +
            "where e.uri in :uris and (timestamp > :start and timestamp < :end) group by e.app,e.uri order by count (distinct e.ip) desc")
    List<ViewStatsDto> getStatsUnique(List<String> uris, LocalDateTime start, LocalDateTime end);

}
