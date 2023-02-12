package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ViewStatsEndpointDto {
    private String app;
    private String uri;
    private Long hits;
}
