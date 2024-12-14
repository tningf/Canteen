package com.example.canteen.service;

import com.example.canteen.entity.Category;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppExeception(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public Category getCategoryByName(String name) {

        return Optional.ofNullable(categoryRepository.findByName(name))
                .orElseThrow(() -> new AppExeception(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public List<Category> getAllActiveCategories() {
        return categoryRepository.findAllByStatusTrue();
    }

    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(cat -> !categoryRepository.existsByName(cat.getName()))
                .map(cat -> {
                    cat.setStatus(true);
                    return cat;
                })
                .map(categoryRepository::save)
                .orElseThrow(() -> new AppExeception(ErrorCode.CATEGORY_ALREADY_EXISTS));
    }

    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                })
                .orElseThrow(() -> new AppExeception(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(category -> {
                    category.setStatus(false);
                    categoryRepository.save(category);
                }, () -> {
                    throw new AppExeception(ErrorCode.CATEGORY_NOT_FOUND);
                });
    }
}
