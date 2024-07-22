package com.codefirstgirls.green_energy_manager.controller;

import com.codefirstgirls.green_energy_manager.model.entity.Energy;
import com.codefirstgirls.green_energy_manager.model.entity.TransactionType;
import com.codefirstgirls.green_energy_manager.model.repository.EnergyRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



     // GET endpoint which calculates the net energy difference for a specified month.

    @GetMapping("/netMonthlyEnergyDifference")
    public ResponseEntity<Map<String, Object>> netEnergyDifferenceCalculation(@RequestParam int month) {
        if (month < 1 || month > 12) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid month. Please provide a month value between 1 and 12.");
            return ResponseEntity.badRequest().body(errorResponse);
        }


        try {
            List<Object[]> monthlyTotals = energyRepository.findTotalMonthlyEnergy(month);


            // initialise both variables.
            double totalGenerated = 0;
            double totalUsed = 0;

            // Iteration to help calculate generated and used energy
            for (Object[] total : monthlyTotals) {
                String transactionType = (String) total[0];
                double amount = ((Number) total[1]).doubleValue();

                if ("generated".equals(transactionType)) {
                    totalGenerated = amount;
                } else if ("used".equals(transactionType)) {
                    totalUsed = amount;
                }
            }
            // Calculate the net energy difference
            double netEnergyDifference = totalGenerated - totalUsed;
            // This method is  for rounding the net energy difference calculation to make it more readable.
            long roundedNetEnergyDifference = Math.round(netEnergyDifference);

            // A method to get the month's name based on the month number to improve readability for user.
            String monthName = Month.of(month).name();

            // This response message will be displayed to the user.
            String message = "Net Energy Difference for " + monthName + " is: " + roundedNetEnergyDifference + " kWh";

            // Structure of the message for displaying  the calculated value of the  Net Energy Difference and a descriptive message to the user.
            Map<String, Object> responseMessage = new HashMap<>();
            responseMessage.put("netEnergyDifference", message);

            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("Failed to calculate the net energy difference for the month of " + month, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

//  A Link to test @GetMapping endpoint:
//  http://localhost:8080/api/energy/netMonthlyEnergyDifference?month=4
//  Append the required month number as a query parameter to the end of the URL.
// Swap '4' with the month number that the calculation of the net energy difference should be for.


