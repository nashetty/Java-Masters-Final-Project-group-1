package com.codefirstgirls.green_energy_manager.model.repository;

import com.codefirstgirls.green_energy_manager.model.entity.Energy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnergyRepository extends JpaRepository<Energy, Integer> {

     // query method to get the total energy transactions per month, grouped by transaction type.

    @Query("SELECT e.transactionType, SUM(e.amountKWh), MONTH(e.transactionDate) FROM Energy e WHERE MONTH(e.transactionDate) = :month GROUP BY e.transactionType, MONTH(e.transactionDate)")
    List<Object[]> findTotalMonthlyEnergy(@Param("month") int month);
}




