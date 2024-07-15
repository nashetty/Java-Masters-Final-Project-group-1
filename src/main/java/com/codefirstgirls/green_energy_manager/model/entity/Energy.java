package com.codefirstgirls.green_energy_manager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name="energyType")
    private String energyType;

    @Column(name="transactionType")
    private String transactionType;

    @Column(name="amount_K_W")
    private double amount_K_W;
}
