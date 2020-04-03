# schemist

Generic Spring Boot JDBC repository and MVC controller for Postgres JSONB based on JSONSchema. 

## Status

Toy project for toy projects.

## What it does

- Generic DDD aggregates configurable with Pojos generated from JSONSchema (as `*.yaml`)
- Generic Spring Data JDBC repository and converters storing Pojos as JSONB in Postgres
- Generic Spring MVC controller for CRUD operations
- Generic Spring MVC controller to publish schemata files as `/schema/**/*.yaml`
- Schema instance validation at POST/PUT/PATCH request time and before persisting to Postgres

## Roadmap

- hooks for services
- schema-based commands and command handlers
- optimistic locking
- auditing

## How To

### Remove Stacktraces from ResponseStatusException

Add `server.error.include-stacktrace=never` or `server.error.include-stacktrace=on_trace_param` 
to application.properties ([credits](https://stackoverflow.com/a/56546039/691083))
