# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

OpenReports API is a Scala 3.3.7 REST API built with Scalatra 3.1.2, running on Jetty 12 (Jakarta EE / ee10). It generates reports from Excel templates (xls/xlsx) and outputs them as xls/xlsx/pdf. Uses PostgreSQL, Redis caching, AWS S3 storage, and Google OAuth.

## Build & Development Commands

Requires [Task](https://taskfile.dev/) (`brew install go-task`) and SBT 1.10.11.

```bash
# Docker-based development
task build          # Build Docker images (no cache)
task up             # Start all services (API, PostgreSQL, Redis)
task up-d           # Start in background
task down           # Stop and remove containers

# Local compilation & testing
sbt compile         # Compile
sbt test            # Run all tests
sbt "testOnly jp.ijufumi.openreports.utils.HashSpec"              # Run single test suite
sbt "testOnly jp.ijufumi.openreports.utils.HashSpec -- -z hmac"   # Run single test case by name
sbt ~reStart        # Hot-reload dev server

# Database migrations (Flyway)
task migrateUp      # Run migrations (starts Docker first)
task migrateClean   # Drop all tables
sbt flywayMigrate   # Run locally (needs DB_* env vars)

# Code formatting
sbt scalafmtAll     # Format all code (scalafmt 3.3.0, maxColumn=100, trailing commas always)

# Coverage
sbt coverage test   # Run with coverage (minimum 60% statement coverage)
```

## Architecture

Clean Architecture with four layers. Dependency direction: presentation -> usecase -> domain <- infrastructure.

### Layers

- **`domain/`** - Pure business logic. Entities (`models/entity/`), value objects (`models/value/`), repository traits in `repository/`, and outbound ports in `port/` (e.g. `AppConfigPort`, `CachePort`, `ConnectionPoolPort`, `GoogleAuthPort`). No external dependencies.
- **`usecase/`** - Application logic. Use case interfaces in `port/input/`, implementations in `interactor/`. Input parameter DTOs in `port/input/param/`.
- **`presentation/`** - HTTP layer. Scalatra servlets in `controller/`, split into `public_/` (no auth) and `private_/` (JWT auth required). Request/response DTOs and converters.
- **`infrastructure/`** - External concerns. Repository implementations in `persistence/repository/`, Slick table mappings in `persistence/entity/`, domain<->entity converters in `persistence/converter/`, plus `cache/` (Redis), `storage/` (S3/local), `external/` (Google OAuth), and `config/`.

### Key Patterns

- **DI**: Google Guice. Modules in `configs/injectors/`. `Injector` singleton wires everything. Servlets are created via `Injector.createAndInject[T]` in `ScalatraBootstrap`.
- **ORM**: Slick 3.6.1 with `Database` injected into repositories. Uses `Await.result` for async operations.
- **Auth**: JWT tokens via Auth0 library. `PrivateAPIServletBase` validates `Authorization` header and `X-Workspace-Id` header before each request.
- **IDs**: ULID generation via `IDs.ulid()`.
- **Converters**: Domain <-> infrastructure entity converters in `infrastructure/persistence/converter/`. Domain <-> presentation DTO converters in `presentation/converter/`.

### Route Mounting (ScalatraBootstrap)

Public: `/health`, `/login`, `/roles`, `/driver-types`
Private: `/members`, `/reports`, `/report-groups`, `/templates`, `/data-sources`, `/workspaces`, `/workspace-members`

### Entry Point

`JettyLauncher` (in `src/main/scala/JettyLauncher.scala`, default package) starts Jetty on port 8080 (configurable via `PORT` env var). It wires `ScalatraListener`, the `GuiceFilter`, and CORS settings against an `ee10` `WebAppContext`.

## Testing

- **Framework**: ScalaTest (FlatSpec style) + ScalaMock + Mockito
- **Test DB**: H2 in-memory database (mirrors PostgreSQL schema via `H2DatabaseHelper`)
- **Repository tests**: Use `BeforeAndAfterAll` for schema setup, `BeforeAndAfterEach` for table truncation
- **Servlet tests**: Extend `ScalatraFunSuite`, mock use cases
- **Test files**: Mirror `src/main/` structure under `src/test/`

## Configuration

Environment variables loaded via dotenv-java (`.env` file or system env). Key vars: `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_PORT`, `REDIS_HOST`, `REDIS_PORT`, `HASH_KEY`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `TEMPLATE_ROOT_PATH`, `OUTPUT_FILE_PATH`. See `Config.scala` for full list.

## Conventions

- Package root: `jp.ijufumi.openreports`
- Interactors (use case implementations): `*Interactor.scala`
- Repository implementations: `*RepositoryImpl.scala`
- Scalafmt: scala3 dialect (scalafmt 3.3.0, maxColumn=100, trailing commas always), import sorting via scalastyle
- Migrations: `src/main/resources/db/migration/V*.sql` (Flyway)
