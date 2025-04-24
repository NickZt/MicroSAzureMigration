# Architecture Overview

This project implements a microservices-based music platform, as described in the article "Cloud Symphony: Orchestrating Microservices for Music Base in Azure".

## Services
- **resource-service**: Handles MP3 file storage and processing.
- **song-service**: Manages song metadata and links to resources.
- **discovery-service (Eureka)**: Service registry for dynamic service discovery.
- **gateway-service (Spring Cloud Gateway)**: API gateway for routing and cross-cutting concerns.
- **PostgreSQL**: Separate databases for resource and song services.

## Service Interaction
- Each song metadata entry is linked to a resource by resource ID (1:1 relationship).
- Deleting a resource triggers cascading deletion of its metadata.
- All services register with Eureka for discovery.
- All external API traffic is routed through the gateway.

## Containerization & Orchestration
- Each service has its own Dockerfile (multi-stage, Eclipse Temurin 17 Alpine base).
- Docker Compose and Helm charts are provided for local and Kubernetes deployments.

## Configuration
- Environment variables are managed via .env files and Helm values.yaml.
- Database credentials and URLs are injected at runtime.

---

# Deployment

- **Local**: Use `docker-compose.yml` to run all services locally.
- **Kubernetes**: Use Helm charts in the `helm/` directory for production-grade deployment.

---

# Dependencies
- Java 17+
- Spring Boot 3.4.0+
- Spring Cloud (Eureka, Gateway)
- PostgreSQL
- Maven
- Docker
- Kubernetes (Helm, kubectl)

---

# Project Structure
- `resource-service/` — Source for resource microservice
- `song-service/` — Source for song metadata microservice
- `discovery/` — Eureka server
- `gateway/` — Spring Cloud Gateway
- `helm/` — Helm charts for all services
- `doc/` — Documentation (this folder)

---

# Authors & License
See main README.md for credits and licensing.
