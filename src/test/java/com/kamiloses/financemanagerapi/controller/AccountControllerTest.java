package com.kamiloses.financemanagerapi.controller;

import com.kamiloses.financemanagerapi.dto.AccountRequestDTO;
import com.kamiloses.financemanagerapi.entity.Account;
import com.kamiloses.financemanagerapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndGetAccount() throws Exception {

        AccountRequestDTO request = new AccountRequestDTO();
        request.setName("Integration Account");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Account"));

        Account saved = accountRepository.findAll().get(0);

        mockMvc.perform(get("/accounts/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Account"));
    }

    @Test
    void shouldDeleteAccount() throws Exception {

        Account account = Account.builder()
                .name("To Delete")
                .balance(BigDecimal.ZERO)
                .build();

        Account saved = accountRepository.save(account);

        mockMvc.perform(delete("/accounts/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}