package com.kamiloses.financemanagerapi.controller;

import com.kamiloses.financemanagerapi.dto.TransactionRequestDTO;
import com.kamiloses.financemanagerapi.entity.Account;
import com.kamiloses.financemanagerapi.entity.TransactionType;
import com.kamiloses.financemanagerapi.repository.AccountRepository;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateIncomeAndIncreaseBalance() throws Exception {

        Account account = accountRepository.save(
                Account.builder()
                        .name("Test Acc")
                        .balance(new BigDecimal("100.00"))
                        .build()
        );

        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("50.00"),
                TransactionType.INCOME,
                "Salary",
                "Test"
        );

        mockMvc.perform(post("/accounts/" + account.getId() + "/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(50.00));


        Account updated = accountRepository.findById(account.getId()).orElseThrow();

        assertEquals(new BigDecimal("150.00"), updated.getBalance());

    }

    @Test
    void shouldCreateAndDeleteTransactionProperly() throws Exception {

        Account account = accountRepository.save(
                Account.builder()
                        .name("Acc")
                        .balance(new BigDecimal("100.00"))
                        .build()
        );

        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("20.00"),
                TransactionType.EXPENSE,
                "Food",
                "Lunch"
        );

        String response = mockMvc.perform(post("/accounts/" + account.getId() + "/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(20.00))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long transactionId = objectMapper.readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(delete("/accounts/" + account.getId() + "/transactions/" + transactionId))
                .andExpect(status().isNoContent());

        assertTrue(transactionRepository.findById(transactionId).isEmpty());
    }
}