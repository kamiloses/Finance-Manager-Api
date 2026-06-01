package com.kamiloses.financemanagerapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "budget_limits")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private BigDecimal limitAmount;


    //Zakładam że limit na kategorie jest miesięczny
}