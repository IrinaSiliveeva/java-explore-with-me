package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.storage.category.CategoryRepository;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto createNewCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с таким названием существует");
        }
        Category category = categoryMapper.mapToCategory(newCategoryDto);
        return categoryMapper.mapFromCategory(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с таким названием существует");
        }
        category.setName(newCategoryDto.getName());
        return categoryMapper.mapFromCategory(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        categoryRepository.deleteById(catId);
    }
}
