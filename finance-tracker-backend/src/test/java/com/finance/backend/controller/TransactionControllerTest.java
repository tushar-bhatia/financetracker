package com.finance.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.backend.model.Category;
import com.finance.backend.model.Transaction;
import com.finance.backend.model.TransactionRequest;
import com.finance.backend.service.ICategoryService;
import com.finance.backend.service.ITransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "transactionServiceImpl")
    private ITransactionService transactionService;

    @MockBean(name = "categoryServiceImpl")
    private ICategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTransactions() throws Exception {
        Transaction t1 = new Transaction(1, LocalDate.now(), 100.0, new Category("Groceries"), "EXPENSE", "Groceries");
        Transaction t2 = new Transaction(2, LocalDate.now(), 5000.0, new Category("Bills"), "EXPENSE", "Room Rent");
        Mockito.when(transactionService.displayTransactions(null)).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/financetracker/transaction/api/v1/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description").value("Groceries"));
    }

    @Test
    void testGetTransactionById() throws Exception {
        Transaction t1 = new Transaction(1, LocalDate.now(), 100.0, new Category("Groceries"), "EXPENSE", "Groceries");
        Mockito.when(transactionService.findTransactionById(1)).thenReturn(t1);

        mockMvc.perform(get("/financetracker/transaction/api/v1/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Groceries"));
    }

    @Test
    void testCreateTransaction() throws Exception {
        Category category = new Category(1, "Groceries");
        Transaction t1 = new Transaction(1, LocalDate.now(), 100.0, category , "EXPENSE", "Groceries");
        TransactionRequest request = new TransactionRequest(t1.getId(), t1.getCategory().getId(), t1.getTransactionType(), null, null, LocalDate.now(), t1.getAmount(), t1.getDescription());
        Mockito.when(transactionService.addTransaction(Mockito.any(TransactionRequest.class))).thenReturn(t1);
        Mockito.when(categoryService.findCategoryById(Mockito.anyInt())).thenReturn(category);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/financetracker/transaction/api/v1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(t1.getId()))
                .andExpect(jsonPath("$.description").value(t1.getDescription()));
    }

    @Test
    void testUpdateTransaction() throws Exception {
        Category category = new Category(1, "Groceries");
        Transaction t1 = new Transaction(1, LocalDate.now(), 100.0, category, "EXPENSE", "Groceries");
        TransactionRequest request = new TransactionRequest(t1.getId(), t1.getCategory().getId(), t1.getTransactionType(), null, null, LocalDate.now(), t1.getAmount(), t1.getDescription());
        Mockito.when(transactionService.updateTransaction(Mockito.any(TransactionRequest.class))).thenReturn(t1);
        Mockito.when(categoryService.findCategoryById(Mockito.anyInt())).thenReturn(category);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/financetracker/transaction/api/v1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(t1.getId()))
                .andExpect(jsonPath("$.description").value(t1.getDescription()));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        Mockito.doNothing().when(transactionService).deleteTransaction(1);

        mockMvc.perform(delete("/financetracker/transaction/api/v1/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction deleted successfully"));
    }
}