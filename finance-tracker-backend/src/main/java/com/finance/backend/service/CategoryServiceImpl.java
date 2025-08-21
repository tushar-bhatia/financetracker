package com.finance.backend.service;

import com.finance.backend.dao.CategoryRepository;
import com.finance.backend.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    @Qualifier("categoryRepository")
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> displayAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        if(categoryRepository.findByName( category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getId()))
            throw new IllegalArgumentException("Category with id " + category.getId() + " does not exist");
        if(categoryRepository.findByName(category.getName()).isPresent())
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category with id " + id + " does not exist");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findCategoryById(int id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category with id " + id + " does not exist");
        }
        return category.get();
    }
}
