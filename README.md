# WiP Code for article (TBD) : "Cloud Symphony: Orchestrating Microservices for Music Base in Azure"



## Prerequisite

2 microservices

### Service relationships

The services are designed to work together as follows:
Resource Service handles the storage and processing of MP3 files.
Song Service manages metadata for each song, ensuring that each metadata entry corresponds to a unique MP3 file in the Resource Service.
The song metadata and resource entities maintain a one-to-one relationship:
Each song metadata entry is uniquely associated with a resource, linked via the resource ID.
Deleting a resource triggers a cascading deletion of its associated metadata.
Each service uses an .env file to substitute variables.

Eureka Service Registry for Service Registration and Discovery
Spring Cloud Gateway as API Gateway to route requests automatically
Spring Boot 3.4.0 or higher
Java 17 or later (LTS versions)
Build Tool: Maven
Database: PostgreSQL
Dockerfile has two-stage builds and based on eclipse-temurin-17-alpine images from official docker repo


## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.
eclipse-temurin-17-alpine images from official docker repo

## License
For open source projects, say how it is licensed.

## Project status
WiP