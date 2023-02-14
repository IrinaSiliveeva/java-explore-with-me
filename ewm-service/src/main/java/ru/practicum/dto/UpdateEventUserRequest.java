package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.enums.StateActionUser;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    @Size(min = 3, max = 120)
    private String title;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long eventId;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateActionUser stateAction;
}
