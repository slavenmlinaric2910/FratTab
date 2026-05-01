# FratTab — Project Description

FratTab is a **monolithic Spring Boot web application** for managing **drink inventory**, **member consumption logging**, and **periodic billing** (e.g., quarterly) for a fraternity/club. It provides a **server-side rendered UI** (Thymeleaf) for day-to-day operations (tablet-friendly drink logging) and an **admin dashboard** for billing runs and status tracking.

## Key Features

### Member & Drink Flow
- **Drink catalog & inventory**
  - Maintain drinks (active/inactive)
  - Track on-hand quantities
  - Record deliveries/restocks (updates inventory and price calculation logic)
- **Consumption logging**
  - Member drink selection flow (multi-drink selection)
  - Quick single-item consumption logging
  - Per-log line items (quantities) and computed totals
  - Inventory decreases automatically when consumption is logged

### Billing & Administration
- **Billing runs**
  - Aggregates consumption totals per member for a billing window
  - Marks logs as billed and associates them with a billing run
  - Tracks payment state per member/run (paid/unpaid)
- **Admin dashboard**
  - Dashboard routes live under `/dashboard` (requires authentication)
  - Operational actions such as triggering billing and marking paid
- **Email delivery**
  - Sends HTML billing emails rendered via Thymeleaf templates
  - SMTP configuration via environment variables (MailerSend in current config)

### UX & Platform
- **Server-side rendered UI** with Thymeleaf templates and static assets (CSS/JS/images)
- **Internationalization (i18n)** via message bundles (`i18n/messages`)
- **Authentication** via Spring Security (form login)

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.x**
  - Spring MVC (web)
  - Spring Data JPA (Hibernate ORM)
  - Spring Security (form login)
  - Spring Mail (SMTP)
- **Lombok** for reduced boilerplate

### Database
- **PostgreSQL** (Docker image in local setup)
- JPA entities for:
  - Drinks, Members, Deliveries
  - Drink logs + line items
  - Billing runs (billed/paid workflow)

### Frontend (Server-side)
- **Thymeleaf** templates
- Static assets served from Spring Boot (`/static`)

### Infrastructure / Local Development
- **Docker Compose**
  - `postgres` for the application database
  - `pgadmin` for DB administration
  - `frattab` app container (built from the `frattab/` module)

## Repository Layout (high-level)
- `frattab/`: Spring Boot application (source, resources, Maven build)
- `docker-compose.yaml`: local environment (Postgres + pgAdmin + app)
- `PROJECT_STATE_01.md`: current implementation notes and code excerpts

## Local URLs (common)
- **App (Docker Compose)**: `http://localhost/` (host port 80 → app port 12000)
- **Admin dashboard**: `http://localhost/dashboard`
- **pgAdmin**: `http://localhost:5050`

