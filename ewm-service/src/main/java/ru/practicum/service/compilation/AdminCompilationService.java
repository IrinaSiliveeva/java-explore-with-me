package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.compilation.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.storage.category.CategoryRepository;
import ru.practicum.storage.compilation.CompilationRepository;
import ru.practicum.storage.event.EventRepository;

@Service
@RequiredArgsConstructor
public class AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CompilationDto createNewCompilation(NewCompilationDto newCompilationDto) {
        if (compilationRepository.existsByTitle(newCompilationDto.getTitle())) {
            throw new ConflictException("Категория с таким названием уже существует");
        }
        Compilation compilation = compilationMapper.mapFromNewCompilation(newCompilationDto);
        return compilationMapper.mapFromCompilation(compilationRepository.save(compilation));
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(updateCompilationRequest.getEvents()));
        }
        return compilationMapper.mapFromCompilation(compilationRepository.save(compilation));
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        categoryRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        compilationRepository.deleteById(compId);
    }
}
