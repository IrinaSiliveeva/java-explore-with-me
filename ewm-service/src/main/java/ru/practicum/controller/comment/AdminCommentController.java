package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UserCommentDto;
import ru.practicum.service.comment.AdminCommentService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    public final AdminCommentService service;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId) {
        log.info("Администратор удаление комментария по id {}", commentId);
        service.deleteCommentById(commentId);
    }

    @GetMapping("/{commentId}")
    public UserCommentDto getById(@PathVariable Long commentId) {
        log.info("Администратор получение комментраия по id {}", commentId);
        return service.getById(commentId);
    }

    @GetMapping("/event/{eventId}")
    public List<EventFullDto> getAllCommentsByEvent(@PathVariable Long eventId) {
        log.info("Получение администратором всех коментариев событию {}", eventId);
        return service.getAllCommentsByEvent(eventId);
    }
}
