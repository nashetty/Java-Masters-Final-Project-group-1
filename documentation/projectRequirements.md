# Project Requirements

## Green Energy Manager

### Table of Contents

* [Project Overview](#project-overview)
* [Project Requirements](#project-requirements-1)
    + [User Stories](#user-stories)
    + [Implementation](#implementation)
    + [Acceptance Criteria](#acceptance-criteria)
    + [Testing Considerations](#testing-considerations)
    + [Dependencies](#dependencies)
    + [Development Tools](#development-tools)
    + [Technologies](#technologies)
    + [Project-Related Tickets](#project-related-tickets)
    + [Task Delegation](#task-delegation)
    + [Notes/Questions/Stretch Goals](#notesquestionsstretch-goals)

## Project Overview

**What is the Project?**

The Green Energy Manager is an application designed to help users track their energy usage and production. The application has two primary functionalities:
1. Recording energy transactions.
2. Calculating the Net Energy Difference (NED) for a specified month.

This project may qualify as an MVP(Minimum Viable Product), as it provides the essential backend functionality for recording energy readings and calculating the net energy difference. 
This facilitates validating core features and gathering feedback, even without a user interface.

The backend is built using Spring Boot and connects to a MySQL database.

**Meaning of Net Energy Difference (NED)**

- **Increased NED**: Indicates that energy generation exceeds consumption, reflecting efficient performance of the user’s energy system and potential excess production that could be exported to the grid.

- **Decreased NED**: Indicates that energy consumption exceeds generation for the month. This might be due to increased energy use or insufficient production.

**What Could the Application Be Used For?**
- **Personal Energy Management**: Track energy consumption and generation.
- **Cost Savings**: Identify opportunities for energy cost savings.
- **Environmental Impact**: Contribute to sustainability goals by monitoring energy usage.
- **Policy Compliance**: Assist with energy regulation compliance and eligibility for incentives.

## Project Requirements

### User Stories

**User Story 1: Post New Energy Reading**
- **As a user**, I want to submit new energy readings, so I can record my energy usage and production.
- **Acceptance Criteria**:
    - The system accepts POST requests to `/api/energy/meterReading`.
    - Validates the `transactionType` as either "generated" or "used".
    - On successful submission, returns a confirmation message.
    - On failure, returns an appropriate error message.

**User Story 2: Retrieve Net Energy Difference**
- **As a user**, I want to calculate the net energy difference for a specific month, so I can see whether I am consuming or producing more energy.
- **Acceptance Criteria**:
    - The system accepts GET requests to `/api/energy/netDifference` with a month parameter.
    - Calculates the net energy difference and returns a message with the result.
    - Returns a meaningful error message if the month parameter is invalid.

### Implementation

- **POST `/api/energy/meterReading`**:
    - Parse and validate the incoming energy reading data.
    - Save the reading to the MySQL database.
    - Handle different transaction types and adjust `energyType` if necessary.
    - Return a success or error message based on the operation outcome.

- **GET `/api/energy/netDifference`**:
    - Validate the month parameter.
    - Query the database for total generated and used energy for the specified month.
    - Calculate the net energy difference.
    - Return the result in a user-friendly format.

### Acceptance Criteria

**POST `/api/energy/meterReading`**
- **Valid Input**: The system successfully saves the reading and returns a confirmation message.
- **Invalid Input**: The system returns a 400 Bad Request error with a message indicating the issue (e.g., invalid transaction type).

**GET `/api/energy/netDifference`**
- **Valid Month**: The system returns the net energy difference for the specified month in a readable format.
- **Invalid Month**: The system returns a 400 Bad Request error with a message indicating that the month is out of range.

### Testing Considerations

**Completed Tests**
1. **Unit Testing**:
    - Tested the EnergyController methods using JUnit and Mockito to ensure correct handling of valid and invalid inputs.
    - Mocked the EnergyRepository to verify the behavior of the service layer.

2. **Integration Testing**:
    - Used RestAssured to test API endpoints.
    - Verified that the endpoints correctly interacted with the MySQL database and returned expected results.

3. **Error Handling**:
    - Validated that the system handles and logs errors appropriately.
    - Checked that error responses are meaningful and user-friendly.

**Future Testing Recommendations**
1. **Boundary and Edge Case Testing**:
    - Test edge cases such as the first and last days of the month to ensure accurate data handling.

2. **Comprehensive Integration Testing**:
    - Conduct more extensive integration tests to cover all possible interaction scenarios between the API and the database.

3. **Performance and Load Testing**:
    - Assess the system's performance under high loads and with large datasets to ensure responsiveness and efficiency.

4. **Security and Vulnerability Testing**:
    - Execute security tests to identify and mitigate potential vulnerabilities, including authentication and authorisation mechanisms.

### Dependencies

- **Spring Boot**: For developing the RESTful API.
- **Lombok**: For reducing boilerplate code in Java.
- **Spring Data JPA**: For database interaction.
- **MySQL**: Database for storing energy transactions.
- **Swagger (OpenAPI)**: For API documentation and testing.
- **JUnit and Mockito**: For unit testing.
- **RestAssured**: For integration testing.

### Development Tools
- **Maven**: For building automation tool required for managing project builds, dependencies, and lifecycle.

### Technologies

**Backend**: Spring Boot
**Database**: MySQL
**Testing**: JUnit, Mockito, RestAssured
**Containerisation**: Docker


### Project-Related Tickets

- Design and document the MySQL database schema for energy transactions.
- Set up and document the database connection settings in application.yml
- Implement Energy entity and repository.
- Develop POST `/api/energy/meterReading` endpoint.
- Implement GET `/api/energy/netDifference` endpoint.
- Add unit tests for EnergyController using JUnit and Mockito.
- Add integration tests for API endpoints using RestAssured.
- Containerise the project using Docker. 
- Finalise project documentations.

### Task Delegation

**1. Backend Development**
- **API Development**:
    - Implemented necessary endpoints for energy data transactions.
    - Developed methods for calculating net energy differences and handling energy data.
- **Database Management**:
    - Designed and maintained the MySQL database schema.
    - Implemented the EnergyRepository for data persistence.

**2. Testing**
- **Unit Testing**:
    - Focused on individual components and methods, ensuring expected functionality.
- **Integration Testing**:
    - Verified the correct interaction between API endpoints and the MySQL database.
- **Error Handling and Edge Cases**:
    - Tested robustness in handling invalid inputs and edge cases.

**3. Project Management**
- **Task Coordination**:
    - Assigned tasks based on team member preferences and workload.
- **Timeline Management**:
    - Ensured project adherence to schedule and deadlines.
- **Code Reviews**:
    - Conducted regular code reviews and Zoom/Slack meetings to maintain code quality and consistency.

**4. Documentation**
- **Swagger (OpenAPI)**:
    - Provided clear and comprehensive API documentation.
- **projectRequirements.md**:
    - Documented overall project overview and structure, user stories, and acceptance criteria.
- **README.md**:
    - Developed and maintained a README file with project instructions and information.
- **manualTestingPlan.md**:
    - Outlined the procedures and test cases for verifying the functionality and quality of the application.
- **docker_instructions.md**:
    - Provided a step-by-step guide for running the project locally using Docker, including instructions for packaging the application, creating a Docker image, configuring docker-compose, and accessing the API.
- **CI_CD.md**:
    - Provided detailed instructions for deploying the Green Energy Manager application and pipeline stages.

### Notes/Questions/Stretch Goals

Given the small project size and tight deadline, the following considerations may be addressed in future iterations or as the project evolves:
1. **Date Handling**:
    - How should date ranges be managed for calculating monthly energy differences? Are specific time zones required?
2. **Transaction Types**:
    - Are additional transaction types or details needed in the future?
3. **Data Accuracy**:
    - How will the system ensure the accuracy and integrity of the submitted energy data?
4. **User Authentication**:
    - Will user authentication and authorisation be needed in the future? If so, what methods should be used?
5. **Additional Endpoints**:
    - Would an endpoint for the energy company to input and manage additional energy types be beneficial, enabling a comparison with user-reported energy data to provide a comprehensive analysis of generated versus used energy?
6. **Basic Front End**:
    - Should a simple front end be developed to allow users to upload energy readings, select a period with a dropdown menu, calculate the Net Energy Difference,and view the results?

