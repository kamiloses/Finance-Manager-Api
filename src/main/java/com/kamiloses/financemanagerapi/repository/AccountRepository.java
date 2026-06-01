package com.kamiloses.financemanagerapi.repository;

import com.kamiloses.financemanagerapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByName(String name);

}