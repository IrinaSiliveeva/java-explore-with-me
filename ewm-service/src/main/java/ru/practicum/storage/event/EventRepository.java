package ru.practicum.storage.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Event;
import ru.practicum.model.enums.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    Set<Event> findAllByIdIn(List<Long> eventIds);

    List<Event> findAllByAnnotationIgnoreCaseOrDescriptionIgnoreCaseAndCategoryIdInAndPaidAndEventDateBetween(String annotation, String description, Collection<Long> categoryId, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(Collection<Long> initiatorId, Collection<State> state, Collection<Long> categoryId, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);
}
