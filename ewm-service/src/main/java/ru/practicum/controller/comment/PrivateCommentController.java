package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.dto.UserCommentDto;
import ru.practicum.service.comment.PrivateCommentService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final PrivateCommentService service;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCommentDto createComment(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @RequestBody @Validated NewCommentDto newCommentDto) {
        log.info("Создание коментрания {} пользователем {} для события {}", newCommentDto, userId, eventId);
        return service.createComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}/events/{eventId}")
    public UserCommentDto updateComment(@PathVariable Long commentId,
                                         @PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @RequestBody @Validated NewCommentDto newCommentDto) {
        log.info("Изменение коментрания {} на {} пользователем {} для события {}", commentId, newCommentDto, userId, eventId);
        return service.updateComment(commentId, userId, eventId, newCommentDto);
    }

    @DeleteMapping("/{commentId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId,
                               @PathVariable Long userId,
                               @PathVariable Long eventId) {
        log.info("Удаления комментария {} пользователем {} для события {}", commentId, userId, eventId);
        service.deleteComment(commentId, userId, eventId);
    }

    @GetMapping("/events/{eventId}")
    public List<UserCommentDto> getUsersCommentsByEvent(@PathVariable Long userId,
                                                        @PathVariable Long eventId) {
        log.info("Получение пользователем {} своих комментариев для события {}", userId, eventId);
        return service.getUsersCommentsByEvent(userId, eventId);
    }

    @GetMapping
    public List<UserCommentDto> getUsersComments(@PathVariable Long userId) {
        log.info("Получение всех коментариев пользователем {}", userId);
        return service.getUsersComments(userId);
    }
}
