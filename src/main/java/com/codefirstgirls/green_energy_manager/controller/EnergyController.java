package com.codefirstgirls.green_energy_manager.controller;

import com.codefirstgirls.green_energy_manager.model.repository.EnergyRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // scanning by spring
@Slf4j // for logging
@RequestMapping("/api/energy") // feel free to change this or delete this
@Tag(name = "Green Energy Manager", description = "API for managing and tracking energy transactions (used and produced).") // feel free to change this
public class EnergyController {
    @Autowired
    private EnergyRepository energyRepository;
}
