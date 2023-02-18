package ru.practicum.storage.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(Long userId);

    List<Comment> findAllByEventId(Long eventId);

    Optional<Comment> findByIdAndAuthorIdAndEventId(Long commentId, Long userId, Long eventId);

    List<Comment> findAllByAuthorIdAndEventId(Long userId, Long eventId);

}
