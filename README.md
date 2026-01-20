![Microservices architecture](flow.png)

This project consists of multiple Spring Boot microservices communicating via Kafka and SOAP. Stateful gateway has a REST API and is documented in OpenAPI.\
Saga pattern is implemented in order to handle distributed transactions across multiple services.\
Frontend application is implemented in Angular.\
Whole application is dockerized and can be built and run using docker compose.
