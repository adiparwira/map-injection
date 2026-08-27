# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

No Maven wrapper is checked in; use a local `mvn` (Maven 3.9+, JDK 17).

- Build: `mvn package`
- Run the app: `mvn spring-boot:run` (serves on `http://localhost:8080`)
- Run all tests: `mvn test`
- Run one test class: `mvn test -Dtest=ClassName`
- Run one test method: `mvn test -Dtest=ClassName#methodName`

There is no `src/test` directory yet — `spring-boot-starter-test` is already on the classpath for when tests are added.

## Architecture

This is a teaching example (Spring Boot 4.0.0) for one specific Spring feature: **injecting a `Map<String, T>` of every bean implementing an interface, keyed by bean name.** All code lives in the single package `com.eraclouds.map_injection`.

The wiring to understand spans four files:

- `PaymentService` — the strategy interface (`pay()`).
- `CreditCardPaymentService` / `DebitCardPaymentService` / `PayPalPaymentService` — implementations. Each declares its own lookup key through the `@Service` value, e.g. `@Service("creditcard")`. **The bean name is the map key** — changing that annotation value changes the API contract.
- `PaymentServiceFactory` — constructor takes `Map<String, PaymentService>`; Spring populates it with all `PaymentService` beans. `getPaymentService(type)` is just `map.get(type.toLowerCase())` and returns `null` for an unknown key.
- `PaymentController` — `POST /pay?type=<key>`; translates a `null` from the factory into `400 Invalid payment type`, otherwise calls `pay()` and returns `200`.

Consequence for changes: **adding a payment method is a single new `@Service("<key>")` class** — no edits to the factory, controller, or any registration list. Keys are matched case-insensitively (controller passes the raw value; factory lowercases).
