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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    // Post request for users to send in their meter readings
    @PostMapping("/meterReading")
    public ResponseEntity<String> postNewEnergyReading(@RequestBody Energy energyInfo) {
        try {
            // Check if energyInfo and its fields are valid
            if (energyInfo == null) {
                return ResponseEntity.badRequest().body("Energy information is required.");
            }
            if (energyInfo.getTransactionType() == null || energyInfo.getTransactionType().isEmpty()) {
                return ResponseEntity.badRequest().body("Transaction type is required.");
            }
            if (energyInfo.getAmountKWh() == null) {
                return ResponseEntity.badRequest().body("Amount of energy in KW/h is required.");
            }

            if (energyInfo.getTransactionDate() == null) {
                return ResponseEntity.badRequest().body("Valid date in format YYYY-MM-DD is required.");
            }
//            assign energy variable outside of scope to be used in the switch
            Energy submitEnergy;
//            get the enum of transactionType and make to uppercase as uppercase is in the transactionType enum class
            switch (TransactionType.valueOf(energyInfo.getTransactionType().toUpperCase())) {
                case GENERATED:
                    log.info("Generated energy recorded.");
                    if (energyInfo.getEnergyType() == null || energyInfo.getEnergyType().isEmpty()) {
                        return ResponseEntity.badRequest().body("A energy type is required for generated energy.");
                    }
                    submitEnergy = energyRepository.save(energyInfo);
                    break;
                case USED:
                    log.info("Used energy recorded.");
                    // Set energy type to 'N/A' for used transactions
                    if (energyInfo.getEnergyType() == null || energyInfo.getEnergyType().isEmpty()) {
                        energyInfo.setEnergyType("N/A");
                    }
                    submitEnergy = energyRepository.save(energyInfo);
                    break;
                default:
                    log.error("Invalid transaction type: " + energyInfo.getTransactionType());
                    return ResponseEntity.badRequest().body("Invalid transaction type.");
            }
            return ResponseEntity.ok("New energy reading added: \n" + submitEnergy.toString());
        } catch (IllegalArgumentException e) {
            assert energyInfo != null;
            log.error("Invalid transaction type provided: " + energyInfo.getTransactionType(), e);
            return ResponseEntity.badRequest().body("Invalid transaction type.");
        } catch (Exception e) {
            log.error("Error adding new reading to your profile!");
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred. Please try again later.");
        }
    }

    // GET endpoint which calculates the net energy difference for a specified month.

    @GetMapping("/netDifference")
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

    // exception handler for when month is provided as String instead of int
    // since we only have 1 method in this controller that requires particular type in @RequestParam we are
    // handling this on controller level
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        log.error("Invalid parameter type used to calculate netEnergyDifference. Please provide a valid month value as an integer.");
        errorResponse.put("error", "Invalid parameter type. Please provide a valid month value as an integer.");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}

//  A Link to test @GetMapping endpoint:
//  http://localhost:8080/api/energy/netDifference?month=4
//  Append the required month number as a query parameter to the end of the URL.
// Swap '4' with the month number that the calculation of the net energy difference should be for.


