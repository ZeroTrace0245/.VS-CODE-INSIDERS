# Smart Facility Booking & Maintenance

Spring Boot full-stack starter for booking shared spaces and tracking maintenance tickets with sessions, roles, pagination, file uploads, scheduling, caching, validation, and OpenAPI docs.

## Stack
- Spring Boot (Web, Security, Data JPA, Validation, Thymeleaf)
- H2 for dev, PostgreSQL for prod
- Spring Cache, Scheduling, Mail-ready, WebSocket-ready
- OpenAPI via springdoc (Swagger UI at `/swagger-ui/index.html`)

## Features
- Session-based auth with roles: ADMIN, MANAGER, MEMBER; login page `/auth/login`, register page `/auth/register`
- Dark-themed UI pages: dashboard, spaces CRUD, bookings (filters + pagination + approve/deny), tickets (filters + status updates) with attachment upload
- CRUD for spaces (`/api/spaces`), caching active list
- Bookings (`/api/bookings`) with pagination, sorting, filters (space/status/date range)
- Maintenance tickets (`/api/tickets`) with pagination, filters (status/priority/space)
- File uploads for ticket attachments (`POST /api/tickets/{id}/attachments`) with size/type guard
- CSRF enabled, HttpOnly cookies; SameSite/secure tightened in prod profile
- Global validation/error handler returning JSON; Bean Validation on inputs
- Seed data (non-prod): 3 users, 3 spaces

## Quick start (dev)
1. Requirements: Java 17, Maven.
2. From `smart-facility/` run: `mvn spring-boot:run`
3. H2 console: `/h2-console` (JDBC `jdbc:h2:mem:smartfacility`, user `sa`, pass `password`).
4. Default users (password `ChangeMe123!`):
   - admin@example.com (ADMIN)
   - manager@example.com (MANAGER)
   - member@example.com (MEMBER)
5. UI entry points (after login): `/dashboard`, `/spaces`, `/bookings`, `/tickets`
6. API docs: `/swagger-ui/index.html`

## Profiles & DB
- Default: in-memory H2 with dev-friendly settings.
- Prod: set `spring.profiles.active=prod` and configure `spring.datasource.*` for PostgreSQL in `application.yml`.

## Notes
- CSRF tokens are emitted via cookie for SPA/REST; form posts use hidden field.
- File uploads stored under `uploads/` (configurable via `app.upload-dir`).
- Scheduling hook: daily reminder job (extend to send mail/SMS).
- Tests: basic context load; extend with service/controller tests.
