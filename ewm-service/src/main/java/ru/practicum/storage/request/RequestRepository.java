package ru.practicum.storage.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;
import ru.practicum.model.enums.Status;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Long countAllByEventIdAndStatus(Long eventId, Status status);

    boolean existsByRequesterId(Long requesterId);

    Long countAllByEventIdIs(Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);

    Request findByIdAndRequesterId(Long requestId, Long requesterId);

    List<Request> findAllByEventInitiatorIdAndEventId(Long requesterId, Long eventId);

    List<Request> findAllByEventIdAndIdIn(Long eventId, List<Long> requestIds);

    Integer countByEventIdAndStatus(Long eventId, Status status);
}
