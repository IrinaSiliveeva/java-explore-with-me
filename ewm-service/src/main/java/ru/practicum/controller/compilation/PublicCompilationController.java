package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.compilation.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final PublicCompilationService service;

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Получение подборки {}", compId);
        return service.getCompilationById(compId);
    }

    @GetMapping
    public List<CompilationDto> getAllCompilation(@RequestParam(required = false) Boolean pinned,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение подборок по параментрам: pinned {}, from {}, size {}", pinned, from, size);
        return service.getAllCompilation(pinned, PageRequest.of(from / size, size));
    }
}
