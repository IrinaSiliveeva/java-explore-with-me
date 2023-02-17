package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.storage.event.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public List<EventShortDto> getAllPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, String sort, int from, int size) {
        if (text.isBlank()) {
            throw new ConflictException("Текст запроса пуст");
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (sort != null) {
            Sort sorting = Sort.by(Sort.Direction.DESC, "id");
            switch (sort) {
                case "EVENT_DATE":
                    sorting = Sort.by(Sort.Direction.DESC, "eventDate");
                    break;
                case "VIEWS":
                    sorting = Sort.by(Sort.Direction.DESC, "views");
                    break;
            }
            pageRequest = PageRequest.of(from / size, size, sorting);
        }
        List<EventShortDto> eventList = eventRepository.findAllByAnnotationIgnoreCaseOrDescriptionIgnoreCaseAndCategoryIdInAndPaidAndEventDateBetween(text, text, categories, paid, rangeStart, rangeEnd, pageRequest)
                .stream().map(eventMapper::mapToShortEvent).collect(Collectors.toList());
        if (onlyAvailable) {
            eventList = eventList.stream().filter(x -> x.getConfirmedRequests() < x.getParticipantLimit()).collect(Collectors.toList());
        }
        return eventList;
    }

    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return eventMapper.mapToEventFull(event);
    }
}
