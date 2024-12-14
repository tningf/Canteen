package com.example.canteen.converter;

import com.example.canteen.entity.Category;
import com.example.canteen.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategoryConverter implements Converter<String, Category> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category convert(String source) {
        Category category = categoryRepository.findByName(source);
        if (category == null) {
            throw new IllegalArgumentException("Unknown category: " + source);
        }
        return category;
    }
}