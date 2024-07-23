package com.codefirstgirls.green_energy_manager.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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
//    not null to handle if the user inputs null or empty in the body for the post request
    @NotNull(message = "Transaction type is required.")
    private String transactionType;

    @NotNull(message = "Energy type is required.")
    @Column(name="energy_type")
    private String energyType;

    @NotNull(message = "Amount in KW is required.")
    @Column(name="amount_K_W")
//    validation to ensure that input is only bigdecimal and not int
    @Digits(integer = 7, fraction = 2, message = "Amount must be a decimal with up to 7 digits and 2 decimal places")
//    changed from double to BigDecimal to match sql database
    private BigDecimal amountKW;


    @NotNull(message = "Transaction date is required.")
    @Column(name="transaction_date")

//    changed from double to BigDecimal to match sql database
    private Date transactionDate;

}