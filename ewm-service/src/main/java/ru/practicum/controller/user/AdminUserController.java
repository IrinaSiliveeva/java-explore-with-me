package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.service.user.AdminUserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final AdminUserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createNewUser(@RequestBody @Validated UserDto userDto) {
        log.info("Создание пользователя {}", userDto);
        return service.createNewUser(userDto);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Получение пользователей {} с параметрами: from {}, size {}", ids, from, size);
        return service.getAllUsers(ids, PageRequest.of(from / size, size));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя {}", userId);
        service.deleteUser(userId);
    }
}
