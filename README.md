# Cloud Symphony: Orchestrating Microservices for Music Base in Azure

This repository contains the source code and deployment assets for the article "Cloud Symphony: Orchestrating Microservices for Music Base in Azure". It demonstrates a microservices architecture for managing music resources and metadata, orchestrated with Spring Cloud, Eureka, and PostgreSQL, and deployable on Azure Kubernetes Service (AKS) or any Kubernetes environment.

---

## Architecture Overview

- **resource-service** — Handles MP3 file storage and processing
- **song-service** — Manages metadata for each song, linked 1:1 with resources
- **discovery-service (Eureka)** — Service registry for dynamic discovery
- **gateway-service** — API gateway for routing and cross-cutting concerns
- **PostgreSQL** — Separate databases for resource and song services

All services are containerized with Docker and orchestrated via Kubernetes/Helm.

---

## Prerequisites

- Java 17 or later (LTS recommended)
- Maven
- Docker
- Kubernetes (with Helm)
- PostgreSQL

---

## Quick Start

### Local (Docker Compose)
1. Clone the repo
2. Copy `.env.example` to `.env` and fill in variables
3. Run: `docker-compose up --build`

### Kubernetes (Helm)
1. Build Docker images for each service and push to your registry (or use Docker Desktop for local)
2. Deploy PostgreSQL via Helm: `helm upgrade --install postgres ./helm/postgres`
3. Deploy discovery, gateway, resource-service, and song-service via their respective Helm charts in `./helm`

---

## Project Structure
- `resource-service/` — Resource microservice
- `song-service/` — Song metadata microservice
- `discovery/` — Eureka server
- `gateway/` — API Gateway
- `helm/` — Helm charts for Kubernetes deployment
- `doc/` — Project documentation

---

## Documentation
- See `doc/architecture.md` for a detailed overview
- See `doc/dependencies.md` for dependencies and technology stack

---

## Authors and Acknowledgments
- Built using Spring Boot, Spring Cloud, PostgreSQL, Docker, and Kubernetes
- Based on Eclipse Temurin 17 Alpine Docker images
- See article for full credits

## License
This project is licensed under the MIT License.

## Project Status
Work in Progress (WiP)