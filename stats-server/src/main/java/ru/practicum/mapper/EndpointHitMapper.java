package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@Component
public class EndpointHitMapper {
    public EndpointHit toEndpoint(EndpointHitDto endpointHitDto) {
        return new EndpointHit(null, endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());
    }
}
