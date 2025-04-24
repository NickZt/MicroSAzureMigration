# Project Dependencies

This project relies on the following key dependencies and technologies:

## Java & Frameworks
- **Java 17+** — Language runtime for all microservices
- **Spring Boot 3.4.0+** — Core framework for all services
- **Spring Cloud Netflix Eureka** — Service discovery
- **Spring Cloud Gateway** — API Gateway

## Database
- **PostgreSQL** — Used by both resource-service and song-service (separate databases)

## Build & Packaging
- **Maven** — Dependency management and build tool
- **Docker** — Containerization of all services

## Orchestration
- **Docker Compose** — Local development and integration
- **Kubernetes** — Production orchestration
- **Helm** — Kubernetes package management (charts in /helm)

## Other
- **Eclipse Temurin 17 Alpine** — Base image for Dockerfiles

---

# Maven Dependencies (Typical)
Each service's `pom.xml` includes (but is not limited to):
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-actuator
- spring-cloud-starter-netflix-eureka-client (for clients)
- spring-cloud-starter-netflix-eureka-server (for discovery)
- spring-cloud-starter-gateway (for gateway)
- postgresql (JDBC driver)

See the individual `pom.xml` files for complete and up-to-date dependency lists.
