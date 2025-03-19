### **Project Overview**

The project calculates delivery fees based on weather conditions. It includes features such as:

- Managing base fees and extra fees.
- Fetching and storing weather data periodically.
- Calculating delivery fees based on city, vehicle type, and weather conditions.

---

### **How to Run**
- Clone the repository.
- Open the project in an IDE (e.g., IntelliJ IDEA).
- Set environment variables in for database connection in `application.yml`.
  - DATABASE_URL
  - DATABASE_USER
  - DATABASE_PASSWORD
- Run the `FujitsuApplication` class.
- To apply default base and extra fees values, send a POST request to `http://localhost:8080/api/setup/reset-fees`.
- Swagger UI is available at `http://localhost:8080/swagger-ui.html`.
- To run tests, right-click on the `test` directory and select `Run 'All Tests'`.

---

### **Implementation Details**
- Base and extra fees are stored in the database and can be updated via REST API.
  - This implementation allows adding other cities, vehicles, and weather conditions in the future.
- Weather data is fetched by scheduled tasks and stored in the database.
- Delivery fees are calculated based on the city, vehicle type, and weather conditions.
- Public methods are documented in the code with Javadoc comments.


