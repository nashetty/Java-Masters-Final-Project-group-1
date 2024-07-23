package com.codefirstgirls.green_energy_manager.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotEmpty(message = "Transaction type is required.")
//    changed from notNull to notEmpty as notEmpty covers an empty string and null
    private String transactionType;

    @NotNull(message = "Energy type is required.")
    @Column(name="energy_type")
    private String energyType;

    @NotNull(message = "Amount in KW is required.")
    @Column(name="amount_k_W_h")
//    validation to ensure that the lowest amount the user can input is 0.00
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount KW must be a non-negative number.")
//    validation to ensure that input is only bigDecimal and not int
    @Digits(integer = 7, fraction = 2, message = "Amount must be a decimal with up to 7 digits and 2 decimal places")
//    changed from double to BigDecimal to match sql database
    private BigDecimal amountKWh;


    @NotNull(message = "Transaction date is required.")
    @Column(name="transaction_date")
//    ensure the correct format of the date to be input by the user
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Transaction date must be in the format YYYY-MM-DD.")
    private Date transactionDate;
}