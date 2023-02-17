package ru.practicum.mapper.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.Compilation;
import ru.practicum.storage.event.EventRepository;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public Compilation mapFromNewCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(eventRepository.findAllByIdIn(newCompilationDto.getEvents()))
                .build();
    }

    public CompilationDto mapFromCompilation(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream().map(eventMapper::mapToShortEvent).collect(Collectors.toSet()))
                .build();
    }
}
