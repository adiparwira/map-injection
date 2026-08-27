# Map Injection — Spring Bean Map Autowiring Demo

A minimal Spring Boot example that shows how Spring can inject a
`Map<String, T>` of every bean implementing an interface, keyed by bean name.
It uses a payment-processing scenario to turn a request parameter (`creditcard`,
`debitcard`, `paypal`) into the matching service implementation — no `if`/`switch`
chain and no manual registry.

## The pattern

When Spring sees a constructor parameter of type `Map<String, SomeInterface>`, it
populates the map with **all beans of that interface**, using each bean's name as
the key.

Each implementation names itself via the `@Service` value:

```java
@Service("creditcard")
public class CreditCardPaymentService implements PaymentService { ... }
```

The factory just receives the assembled map and does a lookup:

```java
@Service
public class PaymentServiceFactory {
    private final Map<String, PaymentService> paymentServiceMap;

    public PaymentServiceFactory(Map<String, PaymentService> paymentServiceMap) {
        this.paymentServiceMap = paymentServiceMap;
    }

    public PaymentService getPaymentService(String type) {
        return paymentServiceMap.get(type.toLowerCase());
    }
}
```

Adding a new payment method is a single new class annotated with
`@Service("<key>")` — the factory, controller, and map wiring need no changes.

## Components

| File | Role |
|------|------|
| `MapInjectionApplication.java` | Spring Boot entry point (`@SpringBootApplication`). |
| `PaymentService.java` | Interface with one method, `pay()`. |
| `CreditCardPaymentService.java` | `PaymentService` bean registered as `creditcard`. |
| `DebitCardPaymentService.java` | `PaymentService` bean registered as `debitcard`. |
| `PayPalPaymentService.java` | `PaymentService` bean registered as `paypal`. |
| `PaymentServiceFactory.java` | Receives the injected `Map<String, PaymentService>` and resolves a service by key. |
| `PaymentController.java` | REST controller exposing `POST /pay`. |

## API

### `POST /pay`

| Param | In | Values |
|-------|----|--------|
| `type` | query | `creditcard`, `debitcard`, `paypal` (case-insensitive) |

Responses:

- `200 OK` — `Payment processed with <type>` (the chosen service prints a line to stdout)
- `400 Bad Request` — `Invalid payment type` when `type` matches no bean

Example:

```bash
curl -X POST "http://localhost:8080/pay?type=paypal"
# -> Payment processed with paypal
# console: Processing PayPal payment...

curl -X POST "http://localhost:8080/pay?type=bitcoin"
# -> Invalid payment type   (HTTP 400)
```

## Project layout

```
pom.xml
src/main/java/com/eraclouds/map_injection/
    MapInjectionApplication.java
    PaymentService.java
    PaymentServiceFactory.java
    PaymentController.java
    CreditCardPaymentService.java
    DebitCardPaymentService.java
    PayPalPaymentService.java
```

## Build

Maven, Spring Boot **4.0.0**, Java 17. Dependencies: `spring-boot-starter-web`
plus `spring-boot-starter-test`.

## Running

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.
