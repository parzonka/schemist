# schemist

Generic Spring Boot JDBC repository and MVC controller for Postgres JSONB based on JSONSchema. 

Since this is a quite a mouthful, we have chosen [a shorter name](https://en.wiktionary.org/wiki/schemist).

## Status

Toy project for toy projects.

## What it does

- Generic DDD aggregates configurable with Pojos generated from JSONSchema (`*.schema.yaml`)
- Generic Spring Data JDBC repository and converters storing Pojos as JSONB in Postgres
- Generic Spring MVC controller for CRUD operations

## Roadmap

- Generic Spring MVC controller to publish `*.schema.yaml` files as `/schema/**/.*.json`
- Schema instance validation at writable request and storage time
