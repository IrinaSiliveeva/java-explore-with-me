package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
