package com.finance.backend.service;

import com.finance.backend.model.Category;
import java.util.List;

public interface ICategoryService {
    List<Category> displayAllCategory();
    Category addCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(int id);
}
