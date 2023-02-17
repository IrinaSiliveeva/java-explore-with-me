package ru.practicum.exception;

public class ConflictException extends IllegalArgumentException {
    public ConflictException(String massage) {
        super(massage);
    }
}
