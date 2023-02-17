package ru.practicum.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommentDto {
    private Long id;
    private UserShortDto author;
    private EventShortDto event;
    private String text;
    private LocalDateTime created;
}
