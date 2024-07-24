# CI/CD Plan for Green Energy Manager API

## Overview

This CI/CD plan outlines the steps to automate the building, testing, and deployment of the Green Energy Manager 
API project using a popular CI/CD tool GitHub Actions.

### Table of Contents

- [Steps:](#steps)
- [Pipeline Configuration Example](#pipeline-configuration-example)
- [Steps Explanation](#steps-explanation)
- [Notes](#notes)

### Steps:

1. **Choose CI/CD Tool**:.
    - We will be using GitHub Actions. To do this, we need to create and configure a workflow file in our GitHub 
   repository in `.github/workflows/`. This configuration file, written in YAML, will define the steps to be executed for 
   Continuous Integration (CI) and Continuous Deployment (CD).
2. **Define CI Pipeline**:
    - **Trigger on Code Changes**: We will configure the pipeline to trigger on push and pull request events for the 
   `main` and development branches (`working-branch`).
    - **Stages**: 
        1. **Static Code Analysis**:
            - Run tools like Checkstyle, PMD, or SonarQube to analyze code quality, which serves as an additional test.
        2. **Build and Test Stage**:
            - Compile the project.
            - Resolve dependencies.
            - Run unit tests.
            - Run integration tests.
        3. **Package Stage**:
            - Package the application into a deployable format (i.e., JAR).

3. **Define CD Pipeline**:
    - **Trigger on Successful CI**: Configure the CD pipeline to trigger upon successful completion of the CI pipeline.
    - **Stages**:
        1. **Deploy to Staging**:
            - Deploy the application to a staging environment.
            - Run smoke tests to ensure the deployment was successful.
        2. **Manual Approval**:
            - Require manual approval before deploying to production.
        3. **Deploy to Production**:
            - Deploy the application to the production environment.
            - Run post-deployment tests to verify the application is working as expected.

### Pipeline Configuration Example:

#### GitHub Actions Workflow File (`.github/workflows/ci-cd.yml`):

```yaml
name: CI/CD Pipeline

on:
  push:
    branches:
      - main
      - working-branch
  pull_request:
    branches:
      - main
      - working-branch

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up JDK 22
      uses: actions/setup-java@v4
      with:
        java-version: '22'
        distribution: 'temurin'
        cache: maven
        
    - name: Static Code Analysis
      run: mvn checkstyle:check

    - name: Start MySQL
      run: sudo systemctl start mysql
    - name: Set up MySQL database
      run: mysql --user=root --password=root --host 127.0.0.1 < src/main/resources/database.sql
    - name: Add sample data to MySQL database
      run: mysql --user=root --password=root --host 127.0.0.1 < src/main/resources/sample-data.sql
    - name: Create MySQL admin user to access database
      env:
        ENERGY_MANAGER_USER: ${{ secrets.ENERGY_MANAGER_USER }}
        ENERGY_MANAGER_PASSWORD: ${{ secrets.ENERGY_MANAGER_PASSWORD }}
      run: mysql --user=root --password=root --host 127.0.0.1 <<< "CREATE USER '$ENERGY_MANAGER_USER'@'localhost' IDENTIFIED BY '$ENERGY_MANAGER_PASSWORD'; GRANT ALL PRIVILEGES ON energy_manager.* TO '$ENERGY_MANAGER_USER'@'localhost';"

    - name: Build and test with Maven
      env:
        ENERGY_MANAGER_USER: ${{ secrets.ENERGY_MANAGER_USER }}
        ENERGY_MANAGER_PASSWORD: ${{ secrets.ENERGY_MANAGER_PASSWORD }}
      run: mvn -B package

  deploy:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'

    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up JDK 22
      uses: actions/setup-java@v4
      with:
        java-version: '22'
        distribution: 'temurin'
        cache: maven

    - name: Build with Maven
      run: mvn clean install

    - name: Deploy to Staging
      run: |
        echo "Deploying to Staging Environment"
        # more deployment scripts or commands

    - name: Manual Approval
      uses: peter-evans/slash-command-dispatch@v4
      with:
        token: ${{ secrets.GITHUB_TOKEN }}
        command: '/approve-deploy'
        reaction-token: ${{ secrets.GITHUB_TOKEN }}
        allowed-users: 'your-github-username'

    - name: Deploy to Production
      if: success() && github.event.comment.body == '/approve-deploy'
      run: |
        echo "Deploying to Production Environment"
        # more deployment scripts or commands
```

### Steps Explanation:

1. **Trigger Events**: The pipeline triggers on push and pull request events to the main and development branches.
2. **Build Job**:
    - **Checkout code**: Check out the repository code.
    - **Set up JDK 22**: Set up Java Development Kit 22.
    - **Static Code Analysis**: Run Checkstyle for static code analysis.
    - **MySQL:** Start MySQL, Set up MySQL database, Add sample data to MySQL database and Create MySQL admin user to access database.
    - **Run unit tests and build with Maven**: Execute unit tests, compile the code and resolve dependencies.
    - **Package application**: Package the application into a JAR file.
3. **Deploy Job**:
    - **Depends on Build**: This job runs after the build job.
    - **Deploy to Staging**: Deploy the packaged application to the staging environment.
    - **Manual Approval**: Wait for manual approval to proceed.
    - **Deploy to Production**: Deploy the application to the production environment if approved.

### Notes:

- **Environment Variables**: We will use secrets and environment variables to securely manage credentials and sensitive data.
- **Notifications**: We will configure notifications (e.g., email, Slack) to alert on pipeline success or failure.
- **Rollback Strategy**: We will implement a rollback strategy in case of deployment failures.

By following this CI/CD plan, we will ensure that our code is continuously tested and deployed in a streamlined and 
automated manner, reducing the risk of errors and increasing the efficiency of our development process.