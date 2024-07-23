package com.codefirstgirls.green_energy_manager.controller;

import com.codefirstgirls.green_energy_manager.model.entity.Energy;
import com.codefirstgirls.green_energy_manager.model.repository.EnergyRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class EnergyControllerGetEndpointTest {

    @Mock
    private EnergyRepository energyRepository;

    @InjectMocks
    private EnergyController energyController;

    @Test
    //Test that the netEnergyDifferenceCalculation method correctly calculates
    // and returns net energy difference when called with a valid month.
    public void netEnergyDifferenceCalculation_calledWithValidMonth_returnsCorrectDifference() {
        int month = 4 ; // Work out the Net Energy Difference for April.

        // Create mock data for Simulating energy generation and usage for April.
        List<Object[]> monthlyTotals = new ArrayList<>();
        monthlyTotals.add(new Object[]{"generated", 17.5});
        monthlyTotals.add(new Object[]{"used", 10.0});


        when(energyRepository.findTotalMonthlyEnergy(month)).thenReturn(monthlyTotals);

        // Call the netEnergyDifferenceCalculation method.
        ResponseEntity<Map<String, Object>> response = energyController.netEnergyDifferenceCalculation(month);

        // Assert that the response status is HTTP 200 OK.
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "The status code is HTTP 200 OK");

        // Assert that the response body is not null
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody, "The response body cannot be null");

        // Assert that the net energy difference response body contains the correct message,
        // including the month name and calculated rounded value.
        // The expected value of the net energy calculation should be 7.5 kWh.
        // However, to test how rounding is handled in the response, the expected value here is set to 8 kWh.
        assertEquals("Net Energy Difference for APRIL is: 8 kWh", responseBody.get("netEnergyDifference"));
        log.info("netEnergyDifferenceCalculation_calledWithValidMonth test passed.");
    }

    @Test
    // Test that the netEnergyDifferenceCalculation method can handle invalid month input.
    public void netEnergyDifferenceCalculation_calledWithInvalidMonth_returnsBadRequest() {
        int invalidMonth = 13; // Use a month value outside of range such as 0 or 13.


        ResponseEntity<Map<String, Object>> response = energyController.netEnergyDifferenceCalculation(invalidMonth);

        // Assert that the response status is HTTP 400 Bad Request.
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue(), "Expected HTTP 400 Bad Request");

        // Assert that the error message in the response body is appropriate for the invalid month input.
        assertNotNull(response.getBody());
        assertEquals("Invalid month. Please provide a month value between 1 and 12.", response.getBody().get("error"));

        log.info("netEnergyDifferenceCalculation_calledWithInvalidMonth_returnsBadRequest test passed.");
    }

    @Test
    // Test that the netEnergyDifferenceCalculation method returns the right response when
    // there is no energy  transaction data for the specified month.
    public void netEnergyDifferenceCalculation_calledWithEmptyData_returnsCorrectResponse() {
        int month = 8; // AUGUST

        // Simulate the repository call to return empty data for the specified month.
        when(energyRepository.findTotalMonthlyEnergy(month)).thenReturn(new ArrayList<>());


        ResponseEntity<Map<String, Object>> response = energyController.netEnergyDifferenceCalculation(month);

        // Assert that the response status is HTTP 200 OK
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "Expected HTTP status 200");

        // Assert that the response body returns a net energy difference of 0 kWh
        Map<String, Object> responseBody = response.getBody();
        assertEquals("Net Energy Difference for AUGUST is: 0 kWh", responseBody.get("netEnergyDifference"));

        log.info("netEnergyDifferenceCalculation_emptyData test passed.");
    }


    // Now use RestAssured tests to simulate the actual HTTP requests to the get endpoint.

    @Test
    public void getNetMonthlyEnergyDifference_calledWithValidMonth() {
        RestAssuredMockMvc.standaloneSetup(energyController);

        int month = 4;


        List<Object[]> monthlyTotals = new ArrayList<>();
        monthlyTotals.add(new Object[]{"generated", 17.5});
        monthlyTotals.add(new Object[]{"used", 10.0});

        when(energyRepository.findTotalMonthlyEnergy(month)).thenReturn(monthlyTotals);

        RestAssuredMockMvc
                .given()
                .when()
                .get("/api/energy/netDifference?month=" + month)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("netEnergyDifference", equalTo("Net Energy Difference for APRIL is: 8 kWh"));
    }

    @Test
    public void getNetMonthlyEnergyDifference_calledWithInvalidMonth() {
        RestAssuredMockMvc.standaloneSetup(energyController);

        int invalidMonth = 13;

        RestAssuredMockMvc
                .given()
                .when()
                .get("/api/energy/netDifference?month=" + invalidMonth)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("Invalid month. Please provide a month value between 1 and 12."));
    }

    @Test
    public void getNetMonthlyEnergyDifference_calledWithEmptyData() {
        RestAssuredMockMvc.standaloneSetup(energyController);

        int month = 8;


        when(energyRepository.findTotalMonthlyEnergy(month)).thenReturn(new ArrayList<>());

        RestAssuredMockMvc
                .given()
                .when()
                .get("/api/energy/netDifference?month=" + month)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("netEnergyDifference", equalTo("Net Energy Difference for AUGUST is: 0 kWh"));
    }





    @Test
    public void postReading_calledWithEnergyClass_forGenerated(){
        RestAssuredMockMvc.standaloneSetup(energyController);
        Energy meterReading = new Energy();
        meterReading.setTransactionType("GENERATED");
        meterReading.setEnergyType("solar");
        meterReading.setAmountKW(BigDecimal.valueOf(50.1));
        meterReading.setTransactionDate(new Date());

        // tried to use the save, by had to use any due to the dynamic nature of the transactionDate
        when(energyRepository.save(any(Energy.class))).thenReturn(meterReading);

        // Test the endpoint
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(meterReading)
                .when()
                .post("/api/energy/meterReading")
                .then()
                .statusCode(HttpStatus.OK.value())
//                for checking
//                    .log().all();
                .body(equalTo("New energy reading added" + meterReading));

    }

    @Test
    public void postReading_calledWithEnergyClass_forUsed(){
        RestAssuredMockMvc.standaloneSetup(energyController);
        Energy meterReading = new Energy();
        meterReading.setTransactionType("USED");
        meterReading.setEnergyType("wind");
        meterReading.setAmountKW(BigDecimal.valueOf(48.1));
        meterReading.setTransactionDate(new Date());

        // tried to use the save, by had to use any due to the dynamic nature of the transactionDate
        when(energyRepository.save(any(Energy.class))).thenReturn(meterReading);

        // Test the endpoint
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(meterReading)
                .when()
                .post("/api/energy/meterReading")
                .then()
                .statusCode(HttpStatus.OK.value())
//                for checking
//                    .log().all();
                .body(equalTo("New energy reading added" + meterReading));

    }

    @Test
    public void postReading_calledWithInvalidTransactionType_ThrowsError(){
        RestAssuredMockMvc.standaloneSetup(energyController);
        Energy meterReading = new Energy();
        meterReading.setTransactionType("NOTVALID");
        meterReading.setEnergyType("wind");
        meterReading.setAmountKW(BigDecimal.valueOf(48.1));
        meterReading.setTransactionDate(new Date());


        // Test the endpoint
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(meterReading)
                .when()
                .post("/api/energy/meterReading")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postReading_calledWithEnergyClass_ReturnBadRequest() throws Exception {
        RestAssuredMockMvc.standaloneSetup(energyController);
        Energy meterReading = new Energy();
        meterReading.setTransactionType("GENERATED");
        meterReading.setEnergyType("wind");
        meterReading.setAmountKW(BigDecimal.valueOf(48.1));
        meterReading.setTransactionDate(new Date());


        when(energyRepository.save(any(Energy.class))).thenThrow(new RuntimeException("Database error"));

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(meterReading) // Use the correct variable name
                .when()
                .post("/api/energy/meterReading")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void postReading_calledWithEmptyBody_ReturnBadRequest() {
        RestAssuredMockMvc.standaloneSetup(energyController);
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/api/energy/meterReading")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    //    because transaction type is the only not null
    public void postReading_calledWithMissingTransactionType_ReturnBadRequest() {
        RestAssuredMockMvc.standaloneSetup(energyController);
        Energy meterReading = new Energy();
        meterReading.setEnergyType("solar");
        meterReading.setAmountKW(BigDecimal.valueOf(50.1));
        meterReading.setTransactionDate(new Date());

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(meterReading)
                .when()
                .post("/api/energy/meterReading")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}
