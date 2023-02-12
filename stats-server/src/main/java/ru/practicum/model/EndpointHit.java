package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.util.StatConstant.TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "stats")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app", length = 500)
    private String app;
    @Column(name = "uri", length = 500)
    private String uri;
    @Column(name = "ip", length = 500)
    private String ip;
    @Column(name = "timestamp")
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime timestamp;
}
