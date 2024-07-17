package com.codefirstgirls.green_energy_manager.model.repository;

import com.codefirstgirls.green_energy_manager.model.entity.Energy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnergyRepository extends JpaRepository<Energy, Integer> {
//    List<Energy> findByTransactionId(Integer id);
}

