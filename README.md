## Demo APIs - to demonstrate Unit Tests, Integration Tests, and Web Integration Tests.

Idea is to cover all the tests at lower level of Test Pyramid which will help for Continuous Integration.

### Unit tests
* Unit tests for Service uses h2 in memory database 
* Unit tests for Controller uses Mockito

### Integration tests
* Integrations tests are written with MockMvc and TestRestTemplate

### To run the unit tests, integration tests
```mvn clean install```

### Below both commands would run the application with in memory h2 database
```mvn spring-boot:run```

```mvn spring-boot:run -Dspring-boot.run.profiles=dev```

### To run the application with mysql database
```mvn spring-boot:run -Dspring-boot.run.profiles=test```

Note: Need to setup mysql server locally with details as mentioned in 'application-test.properties'.
This can be used for E2E tests written with Rest-assured in repository https://github.com/dhaneshkhot/books-api-rest-assured.

API Endpoint when ran locally:  
http://localhost:8080/api/books/


