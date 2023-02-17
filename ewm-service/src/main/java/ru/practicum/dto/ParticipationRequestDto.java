package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.enums.Status;

import java.time.LocalDateTime;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status;
}
