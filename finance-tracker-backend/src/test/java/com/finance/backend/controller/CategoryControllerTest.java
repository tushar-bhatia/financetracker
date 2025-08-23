package com.finance.backend.controller;

import com.finance.backend.model.Category;
import com.finance.backend.service.ICategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "categoryServiceImpl")
    private ICategoryService categoryService;

    @Test
    void testGetAllCategories() throws Exception {
        Category cat1 = new Category(1, "Food");
        Category cat2 = new Category(2, "Transport");
        Mockito.when(categoryService.displayAllCategory()).thenReturn(Arrays.asList(cat1, cat2));

        mockMvc.perform(get("/financetracker/category/api/v1/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Food"));
    }

    @Test
    void testCreateCategory() throws Exception {
        Category savedCat = new Category(3, "Shopping");
        Mockito.when(categoryService.addCategory(Mockito.any(Category.class))).thenReturn(savedCat);

        String json = "{\"name\":\"Shopping\"}";

        mockMvc.perform(post("/financetracker/category/api/v1/add?name=Shopping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedCat.getId()))
                .andExpect(jsonPath("$.name").value(savedCat.getName()));
    }


    @Test
    void testUpdateCategory() throws Exception {
        Category updatedCat = new Category(1, "Updated");
        Mockito.when(categoryService.updateCategory(Mockito.any(Category.class))).thenReturn(updatedCat);

        String json = "{\"id\":1,\"name\":\"Updated\"}";

        mockMvc.perform(post("/financetracker/category/api/v1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCat.getId()))
                .andExpect(jsonPath("$.name").value(updatedCat.getName()));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory(1);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/financetracker/category/api/v1/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Category deleted successfully"));
    }
}