package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDateTime;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EndpointHitDto {
    @NotNull
    private String app;
    @NotNull
    private String uri;
    @NotNull
    private String ip;
    @NotNull
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime timestamp;
}
