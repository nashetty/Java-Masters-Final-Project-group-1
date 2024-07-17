package com.codefirstgirls.green_energy_manager.controller;

import com.codefirstgirls.green_energy_manager.model.entity.Energy;
import com.codefirstgirls.green_energy_manager.model.entity.TransactionType;
import com.codefirstgirls.green_energy_manager.model.repository.EnergyRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // scanning by spring
@Slf4j // for logging
@RequestMapping("/api/energy") // feel free to change this or delete this
@Tag(name = "Green Energy Manager", description = "API for managing and tracking energy transactions (used and produced).") // feel free to change this
public class EnergyController {
    @Autowired
    private EnergyRepository energyRepository;

    @PostMapping("/postReading")
    public ResponseEntity<String> postNewEnergyReading(@RequestBody Energy energyInfo){
        try{
//            assign energy variable outside of scope to be used in the switch
            Energy submitEnergy;
//            get the enum of transactionType and make to uppercase as uppercase is in the transactionType enum class
           switch (TransactionType.valueOf(energyInfo.getTransactionType().toUpperCase())){
               case GENERATED:
                   log.info("Generated energy recorded.");
                   submitEnergy = energyRepository.save(energyInfo);
                   break;
               case USED:
                   log.info("Used energy recorded.");
                   // Set energy type to 'N/A' for used transactions
                   energyInfo.setEnergyType("N/A");
                   submitEnergy = energyRepository.save(energyInfo);
                   break;
               default:
                   return ResponseEntity.badRequest().body("Invalid transaction type.");
           }
            return ResponseEntity.ok("New energy reading added" + submitEnergy);

        }
        catch(Exception e){
            log.error("Error adding new reading to your profile!");
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Failed to add the new energy reading submission." + e.getMessage());
        }
    }

    //    @GetMapping("/types")
    //    public ResponseEntity<String> getTypes() {
    //        String getEnergyFirst = energyRepository.findAll().getFirst().getEnergyType();
    //        log.info("Retrieved energy type: " + getEnergyFirst);
    //        return  ResponseEntity.ok("Energy type returned: " + getEnergyFirst );
    //    }
}

