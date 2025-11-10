package com.benchmark.jaxrs.service;

import com.benchmark.jaxrs.dto.CategoryDTO;
import com.benchmark.jaxrs.entity.Category;
import com.benchmark.jaxrs.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@ApplicationScoped
@Transactional
public class CategoryService {

    @Inject
    private CategoryRepository categoryRepository;

    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::toDTO);
    }

    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id).map(this::toDTO);
    }

    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = toEntity(categoryDTO);
        Category saved = categoryRepository.save(category);
        return toDTO(saved);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getCode(), category.getName());
    }

    private Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setCode(dto.getCode());
        category.setName(dto.getName());
        return category;
    }
}
