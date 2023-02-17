package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.User;
import ru.practicum.storage.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto createNewUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Емайл уже занят");
        }
        User user = userMapper.mapToUser(userDto);
        return userMapper.mapFromUser(userRepository.save(user));
    }

    public List<UserDto> getAllUsers(List<Long> userIds, PageRequest pageRequest) {
        return userRepository.findByIdIsIn(userIds, pageRequest)
                .stream().map(userMapper::mapFromUser).collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.delete(user);
    }
}
