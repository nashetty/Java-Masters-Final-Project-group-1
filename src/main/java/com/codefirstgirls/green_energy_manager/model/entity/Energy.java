package com.codefirstgirls.green_energy_manager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="energy")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Energy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="transaction_type")
    private String transactionType;

    @Column(name="energy_type")
    private String energyType;

    @Column(name="amount_K_W")
//    changed from double to BigDecimal to match sql database
    private BigDecimal amountKW;

    @Column(name="transaction_date")
//    changed from double to BigDecimal to match sql database
    private Date transactionDate;

}