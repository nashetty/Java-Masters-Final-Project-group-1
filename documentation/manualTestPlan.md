# Manual Test Plan

This document outlines the steps for manually testing the EnergyController in the Green Energy Manager application. The focus is on ensuring that the controller correctly handles various scenarios for calculating net energy differences and posting energy readings.

### Table of Contents

- [Test Environment](#test-environment)
- [Test Tools](#test-tools)
- [Test Scenarios](#test-scenarios)
  - [1. Test Net Energy Difference Calculation for a Valid Month](#1-test-net-energy-difference-calculation-for-a-valid-month)
  - [2. Test Net Energy Difference Calculation for an Invalid Month](#2-test-net-energy-difference-calculation-for-an-invalid-month)
  - [3. Test Net Energy Difference Calculation for an Invalid Month (String)](#3-test-net-energy-difference-calculation-for-an-invalid-month-string)
  - [4. Test Net Energy Difference Calculation with No Data for Month](#4-test-net-energy-difference-calculation-with-no-data-for-month)
  - [5. Test Posting Energy Reading for Generated Energy](#5-test-posting-energy-reading-for-generated-energy)
  - [6. Test Posting Energy Reading for Used Energy](#6-test-posting-energy-reading-for-used-energy)
  - [7. Test Posting Energy Reading with Invalid Transaction Type](#7-test-posting-energy-reading-with-invalid-transaction-type)
  - [8. Test Posting Energy Reading with Database Error](#8-test-posting-energy-reading-with-database-error)
  - [9. Test Posting Energy Reading with Empty Body](#9-test-posting-energy-reading-with-empty-body)
  - [10. Test Posting Energy Reading with Missing Transaction Type](#10-test-posting-energy-reading-with-missing-transaction-type)
  - [11. Test Posting Energy Reading with Missing amountkWh](#11-test-posting-energy-reading-with-missing-amountkwh)
  - [12. Test Posting Energy Reading with Missing transactionDate](#12-test-posting-energy-reading-with-missing-transactiondate)
- [Further Testing Ideas](#further-testing-ideas)
  - [Test Access Control for Authenticated Users](#test-access-control-for-authenticated-users)
  - [Test Role-Based Access Control](#test-role-based-access-control)
  - [Test Posting Energy Reading with Negative Amount](#test-posting-energy-reading-with-negative-amount)
  - [Test Posting Energy Reading with Invalid Date Format](#test-posting-energy-reading-with-invalid-date-format)

## Test Environment

- Application server with EnergyController deployed.
- Access to the REST API endpoints.
- Database containing sample data for energy transactions.

## Test Tools

- API testing tool (e.g., Postman)
- Browser

## Test Scenarios

### 1. Test Net Energy Difference Calculation for a Valid Month

**Objective:** Verify that the `netEnergyDifferenceCalculation` method returns the correct net energy difference for a valid month.

**Steps:**

1. Use Postman to send a GET request to the endpoint: `/api/energy/netDifference?month=4`.
2. Verify the response status is HTTP 200 OK.
3. Verify the response body contains the message: `Net Energy Difference for APRIL is: 8 kWh`.

**Expected Result:**

- Status code: 
> 200
- Response body: 
> `{"netEnergyDifference": "Net Energy Difference for APRIL is: 8 kWh"}`

### 2. Test Net Energy Difference Calculation for an Invalid Month

**Objective:** Verify that the `netEnergyDifferenceCalculation` method returns a bad request error for an invalid month.

**Steps:**

1. Use Postman to send a GET request to the endpoint: `/api/energy/netDifference?month=13`.
2. Verify the response status is HTTP 400 Bad Request.
3. Verify the response body contains the error message: `Invalid month. Please provide a month value between 1 and 12.`

**Expected Result:**

- Status code: 
> 400
- Response body: 
>`{"error": "Invalid month. Please provide a month value between 1 and 12."}`

### 3. Test Net Energy Difference Calculation for an Invalid Month (String)

**Objective:** Verify that the `netEnergyDifferenceCalculation` method returns a bad request error for when the month is entered as string.

**Steps:**

1. Use Postman to send a GET request to the endpoint: `/api/energy/netDifference?month=June`.
2. Verify the response status is HTTP 400 Bad Request.
3. Verify the response body contains the error message: `Invalid parameter type. Please provide a valid month value as an integer.`

**Expected Result:**

- Status code:
> 400
- Response body:
>`{"error": "Invalid parameter type. Please provide a valid month value as an integer."}`

### 4. Test Net Energy Difference Calculation with No Data for Month

**Objective:** Verify that the `netEnergyDifferenceCalculation` method returns a zero net energy difference when there is no data for the specified month.

**Steps:**

1. Use Postman to send a GET request to the endpoint: `/api/energy/netDifference?month=8`.
2. Verify the response status is HTTP 200 OK.
3. Verify the response body contains the message: `Net Energy Difference for AUGUST is: 0 kWh`.

**Expected Result:**

- Status code: 
> 200
- Response body: 
> `{"netEnergyDifference": "Net Energy Difference for AUGUST is: 0 kWh"}`

### 5. Test Posting Energy Reading for Generated Energy

**Objective:** Verify that the `meterReading` endpoint correctly processes a new energy reading for generated energy.

**Steps:**

1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with the following JSON body:
    ```json
    {
      "transactionType": "GENERATED",
      "energyType": "solar",
      "amountKWh": 50.1,
      "transactionDate": "2024-07-22T00:00:00.000Z"
    }
    ```
2. Verify the response status is HTTP 200 OK.
3. Verify the response body contains the message: `New energy reading added: {meterReading}`.

**Expected Result:**

- Status code: 
> 200
- Response body: 
>`New energy reading added: {meterReading}`

### 6. Test Posting Energy Reading for Used Energy

**Objective:** Verify that the `meterReading` endpoint correctly processes a new energy reading for used energy.

**Steps:**

1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with the following JSON body:
    ```json
    {
      "transactionType": "USED",
      "energyType": "wind",
      "amountKWh": 48.1,
      "transactionDate": "2024-07-22T00:00:00.000Z"
    }
    ```
2. Verify the response status is HTTP 200 OK.
3. Verify the response body contains the message: `New energy reading added: {meterReading}`.

**Expected Result:**

- Status code: 
> 200
- Response body: 
> `New energy reading added: {meterReading}`

### 7. Test Posting Energy Reading with Invalid Transaction Type

**Objective:** Verify that the `meterReading` endpoint returns a bad request error when an invalid transaction type is provided.

**Steps:**

1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with the following JSON body:
    ```json
    {
      "transactionType": "NOTVALID",
      "energyType": "wind",
      "amountKWh": 48.1,
      "transactionDate": "2024-07-22T00:00:00.000Z"
    }
    ```
2. Verify the response status is HTTP 400 Bad Request.

**Expected Result:**

- Status code: 
> 400
- Response body:
> `Invalid transaction type.`

### 8. Test Posting Energy Reading with Database Error

**Objective:** Verify that the `meterReading` endpoint returns a bad request error when there is a database error.

**Steps:**

1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with the following JSON body:
    ```json
    {
      "transactionType": "GENERATED",
      "energyType": "wind",
      "amountKWh": 48.1,
      "transactionDate": "2024-07-22T00:00:00.000Z"
    }
    ```
2. Verify the response status is HTTP 500 Internal Server Error.

**Expected Result:**

- Status code: 
> 500
- Response body:
>`Internal server error occurred. Please try again later.`


### 9. Test Posting Energy Reading with Empty Body

**Objective:** Verify that the `meterReading` endpoint returns a bad request error when the request body is empty.

**Steps:**

1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with an empty JSON body: `{}`.
2. Verify the response status is HTTP 400 Bad Request.

**Expected Result:**

- Status code:
> 400


### 10. Test Posting Energy Reading with Missing Transaction Type

**Objective:** Verify that the `meterReading` endpoint returns a bad request error when the transaction type is missing.

**Steps:**

1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with the following JSON body:
    ```json
    {
      "energyType": "solar",
      "amountKWh": 50.1,
      "transactionDate": "2024-07-22T00:00:00.000Z"
    }
    ```
2. Verify the response status is HTTP 400 Bad Request.

**Expected Result:**

- Status code: 
> 400
- Response body:
> `Transaction type is required.`


### 11. Test Posting Energy Reading with Missing amountkWh

**Objective**: Verify that the meterReading endpoint returns a bad request error when the amountkWh  field is missing in the request body.

**Steps**:

1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with the following JSON body:
   ```json
    { "transactionType": "GENERATED",
      "energyType": "solar",
      "transactionDate": "2024-07-22T00:00:00.000Z"
    }
    ```
   
2. Verify that the response status is HTTP 400 Bad Request.

**Expected Result**:
- Status code: 
> 400
- Response body:
> `Amount of energy in KW/h is required.`



### 12. Test Posting Energy Reading with Missing transactionDate

**Objective**: Verify that the meterReading endpoint returns a bad request error when the transactionDate field is missing in the request body.

**Steps**:
1. Use Postman to send a POST request to the endpoint: `/api/energy/meterReading` with the following JSON body:

   ```json
    {
      "transactionType": "GENERATED",
      "energyType": "solar",
      "amountKWh": 50.1
      
    }
    ```
2. Verify that the response status is HTTP 400 Bad Request.

**Expected Result**:
- Status code:
> 400
- Response body:
> `Valid date in format YYYY-MM-DD is required.`

## Further Testing Ideas

### Test Access Control for Authenticated Users

**Objective:** Verify that only authenticated users can access the energy endpoints.

**Steps:**

1. Use Postman to send a GET and POST request to the endpoint without authentication.
2. Verify the response status is HTTP 401 Unauthorised.

**Expected Result:**

- Status code: 
> 401
- Response body: 
> `{"error": "Unauthorised access"}`

### Test Role-Based Access Control

**Objective:** Verify that users with appropriate roles can access and perform actions.

**Steps:**

1. Use Postman to send a GET/POST request with a user having insufficient privileges.
2. Verify the response status is HTTP 403 Forbidden.

**Expected Result:**

- Status code: 
> 403
- Response body: 
> `{"error": "Forbidden"}`


### Test Posting Energy Reading with Negative Amount

**Objective:** Verify that a negative amount of energy returns a proper error message.

**Steps:**

1. Use Postman to send a POST request with a negative energy amount.
2. Verify the response status is HTTP 400 Bad Request.

**Expected Result:**

- Status code: 
 > 400
- Response body: 
 > {"error": "Amount of energy cannot be negative"}`

### Test Posting Energy Reading with Invalid Date Format
**Objective**: Verify that the endpoint rejects invalid date formats.

**Steps**:

1. Use Postman to send a POST request with an incorrectly formatted transactionDate.
2. Verify that the system returns a 400 Bad Request response with an appropriate error message.

**Expected Result:**

- Status code:
> 400

- Response body:
> {"error": "Invalid date format"}
