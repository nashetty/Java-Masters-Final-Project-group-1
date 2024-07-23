# To run the project locally using Docker, please follow the steps below:

1. **In IntelliJ** - In the terminal execute `mvn clean` (alternatively, do this by opening the Maven sidebar and 
double-clicking **`green-energy-manager -> Lifecycle -> clean`**).  


2. **Create a package** - Run **`mvn package`** (alternatively, do this by opening the Maven sidebar and 
double-clicking **`green-energy-manager -> Lifecycle -> package`**). This will create a JAR file inside 
the **target** folder. The file should be named **green-energy-manager-0.0.1-SNAPSHOT.jar** (verify the name 
matches, otherwise the image build will fail).


3. **Build the Docker image**:

   ```bash 
   docker build -t green-energy-manager .
    ```

4. **Verify the image** - Go to Docker Desktop to verify the image has been built and copy the 256 SHA hash 
number assigned to it (e.g., ab98ed1e5def252ce92047df6f45b00ab3e362d8b7c930aecbbe411ee0498268). You will 
need to enter this number on line 15 of the **`docker-compose.yml`** file.


5. **Update the init script path** - You might also need to update line 11 in the **`docker-compose.yml`** file 
with the absolute path to the db init script where you store it locally. This includes the bit 
before **`:/docker-entrypoint-initdb.d`**.  


6. **Run docker-compose.yml**:
    ```bash
   docker-compose up -d
    ```

7. **Verify the container** - Ensure the container is running successfully in Docker Desktop. If the container 
has been created but is not fully running (i.e., if the green-energy-manager part is not running), you might 
need to stop it and run it again manually in Docker Desktop. If this does not help after a few tries, try deleting 
the container and repeating step 6. You might still need to manually restart the container in Docker Desktop. 
This usually fixes the issue.


8. **Access the API** - You can access the API endpoints by clicking on the link in the ‘Ports’ column 
(e.g., **8080:8080** which redirects to **http://localhost:8080/**). You can then access the endpoints as usual 
in the browser or in Postman.


9. **Clean up** - At the end, run to clean up the services:
   ```bash
   docker-compose down
   ```
