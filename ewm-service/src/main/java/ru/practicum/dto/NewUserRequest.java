package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    private String name;
    private String email;
}
