package ru.practicum.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    private List<String> errors;
    private final String messages;
    private final String reason;
    private final String status;
    private final LocalDateTime timestamp;
}
