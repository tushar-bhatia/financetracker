package com.finance.backend.service;

import com.finance.backend.dao.CategoryRepository;
import com.finance.backend.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

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
}
