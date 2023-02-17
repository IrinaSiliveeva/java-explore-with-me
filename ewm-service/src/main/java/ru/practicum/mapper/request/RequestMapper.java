package ru.practicum.mapper.request;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.enums.Status;

import java.time.LocalDateTime;

@Component
public class RequestMapper {

    public Request mapToRequest(User user, Event event) {
        return Request.builder()
                .id(null)
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(event.getRequestModeration() ? Status.PENDING : Status.CONFIRMED)
                .build();
    }

    public ParticipationRequestDto mapToParticipationRequest(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
