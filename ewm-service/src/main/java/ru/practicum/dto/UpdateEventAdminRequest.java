package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.enums.StateActionAdmin;

import java.time.LocalDateTime;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    private String annotation;
    private Long category;
    private String description;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private StateActionAdmin stateAction;
}
