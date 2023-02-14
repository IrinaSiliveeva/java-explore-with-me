package ru.practicum.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime timestamp;
}
