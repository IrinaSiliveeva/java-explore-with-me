package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.compilation.CompilationMapper;
import ru.practicum.storage.compilation.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    public CompilationDto getCompilationById(Long compId) {
        return compilationMapper.mapFromCompilation(compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена")));
    }

    public List<CompilationDto> getAllCompilation(Boolean pinned, PageRequest pageRequest) {
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageRequest).stream().map(compilationMapper::mapFromCompilation).collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(pageRequest).stream().map(compilationMapper::mapFromCompilation).collect(Collectors.toList());
        }
    }
}
