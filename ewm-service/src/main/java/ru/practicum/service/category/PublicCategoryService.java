package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.storage.category.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest).stream().map(categoryMapper::mapFromCategory).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {
        return categoryMapper.mapFromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдна")));
    }
}
