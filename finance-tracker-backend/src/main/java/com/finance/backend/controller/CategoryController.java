package com.finance.backend.controller;

import com.finance.backend.model.Category;
import com.finance.backend.model.OnUpdate;
import com.finance.backend.service.ICategoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/financetracker/category/api/v1")
@Validated
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    @Qualifier("categoryServiceImpl")
    private ICategoryService categoryService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategories() {
        LOGGER.info("Request received to display all categories");
        try {
            List<Category> categories = categoryService.displayAllCategory();
            LOGGER.info("Request completed for display all categories");
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch(Exception e) {
            LOGGER.error("Error occurred while getting all categories", e);
            throw e;
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAllCategories(@NotNull(message = "id can't be null")
                                                  @Positive(message = "id must be positive")
                                                  @PathVariable("id") Integer id) {
        LOGGER.info("Request received to fetch category with id {}", id);
        try {
            Category category = categoryService.findCategoryById(id);
            LOGGER.info("Category fetched successfully with id {}", id);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while fetching category with id {}", id, e);
            throw e;
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@NotBlank(message = "Category name can not be blank")
                                             @RequestParam(name = "name") String name) {
        LOGGER.info("Request received to add new category {}", name);
        Category category = new Category(name);
        try {
            Category savedCategory = categoryService.addCategory(category);
            LOGGER.info("New category {} added successfully with id {}", savedCategory.getName(), savedCategory.getId());
            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while adding category", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@NotNull(message = "id can't be null")
                                                @Positive(message = "id must be positive")
                                                @PathVariable("id") Integer id) {
        LOGGER.info("Request received to delete category with id {}", id);
        try {
            categoryService.deleteCategory(id);
            LOGGER.info("Category deleted successfully with id {}", id);
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while deleting category with id {}", id, e);
            throw e;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateCategory(@Validated(OnUpdate.class) @RequestBody Category category) {
        LOGGER.info("Request received to edit category with id {}", category.getId());
        try {
            Category updatedCategory = categoryService.updateCategory(category);
            LOGGER.info("Category updated successfully with id {}", updatedCategory.getId());
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while updating category with id {}", category.getId(), e);
            throw e;
        }
    }
}
