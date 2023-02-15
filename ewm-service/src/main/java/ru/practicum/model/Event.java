package ru.practicum.model;

import lombok.*;
import ru.practicum.model.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    private String description;
    private String title;
    private LocalDateTime eventDate;
    private Double lat;
    private Double lon;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;
}
