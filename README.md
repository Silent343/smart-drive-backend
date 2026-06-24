# SmartDrive Finance — Backend (Spring Boot 3 + Java 21)

Backend RESTful para el frontend **SmartDrive Finance** (Angular). Está construido con
**Domain-Driven Design (DDD)**, **Clean Architecture** y **SOLID**, organizado por
**bounded contexts**, siguiendo el estilo del proyecto de referencia *learning-center*
(UPC/ACME). Funciona con el frontend **sin modificarlo**: corre en el puerto `3000`, expone
las rutas en la raíz (sin prefijo `/api/v1`) y respeta exactamente los nombres de campos que
esperan los assemblers del cliente.

> Sin Lombok. Los agregados son entidades JPA con getters explícitos; los recursos REST son
> `record`s de Java.

---

## Stack

- Spring Boot **3.3.5**, Java **21**
- Spring Web, Spring Data JPA, Spring Validation, Spring Security
- **H2** en memoria por defecto (cero configuración) / **MySQL** opcional por perfil
- **JWT** (jjwt) para los tokens Bearer
- **TOTP 2FA** (`dev.samstevens.totp`) compatible con Google Authenticator
- **springdoc-openapi** (Swagger UI)

---

## Cómo correrlo

### Opción A — H2 en memoria (recomendada para probar)

```bash
mvn spring-boot:run
```

Arranca en `http://localhost:3000`. La base se crea en memoria y se **siembra sola** con los
datos demo. Luego levanta el frontend Angular (`ng serve`, puerto 4200) y ya conversan.

- Swagger UI: `http://localhost:3000/swagger-ui.html`
- Consola H2: `http://localhost:3000/h2-console`
  (JDBC URL: `jdbc:h2:mem:smartdrive`, user `sa`, sin password)

### Opción B — MySQL

Crea la base y ajusta credenciales en `src/main/resources/application-mysql.properties`, luego:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### Build del jar

```bash
mvn clean package
java -jar target/platform-1.0.0.jar
```

---

## Usuario demo (sembrado al iniciar)

| Campo     | Valor                  |
|-----------|------------------------|
| Email     | `fernand04@gmail.com`  |
| Password  | `Sukerj$23`            |
| TOTP      | Habilitado             |

El secreto TOTP demo es `GVLHIWCSIZWEGOT2IAUFISBJMV4GSRS3`. Puedes cargarlo manualmente en tu
app de autenticación (Google Authenticator, Authy, etc.) para generar el código de 6 dígitos,
o registrar un usuario nuevo y pasar por el flujo de setup que devuelve el QR.

---

## Arquitectura

Tres bounded contexts + capa `shared`, cada uno con las capas DDD `domain` / `application` /
`infrastructure` / `interfaces`:

```
pe.edu.upc.smartdrive.platform
├── shared          → bases auditables, naming snake_case, OpenAPI, manejo de errores, seeding
├── iam             → autenticación, JWT y TOTP 2FA (agregado User)
├── arm             → clientes y vehículos (Client, Vehicle, VehicleSpecification, VehicleCommercial)
└── sdp             → simulación de crédito vehicular (CreditConfig, Loan + motor de amortización)
```

Por contexto:

```
domain/
  model/{aggregates, commands, queries, valueobjects, services}
  services/        → interfaces de command/query services
  repositories/    → puertos de persistencia (Spring Data JpaRepository)
application/
  internal/{commandservices, queryservices, outboundservices}
infrastructure/    → JWT, hashing bcrypt, TOTP, seguridad, etc.
interfaces/
  rest/{resources, transform} + Controllers
```

### Decisiones clave de integración

- **Puerto 3000, rutas en la raíz** para calzar con `environment.ts` del frontend.
- **CORS** habilitado para `http://localhost:4200` (y `:3000`).
- **IDs mixtos**, igual que el frontend: `String` (7 chars) en ARM; `Long` en `CreditConfig` y
  `Loan`. En los `POST` el id que mande el cliente se ignora y lo genera el servidor.
- **Convención JSON**: ARM en `camelCase`; `CreditConfig`, `Loan`, `ScheduleRow` en
  `snake_case` vía `@JsonProperty`. Se respeta el typo `instalments_qty` (una sola `l`).
- **Motor financiero en el backend**: el cronograma (`schedule`), el reporte (`report`) y la
  simulación (`/loans/simulate`) se **regeneran** con el método francés vencido ordinario
  (interés, seguro, portes, comisión, periodos de gracia parcial/total, TCEA, VAN y TIR por
  Newton-Raphson). El `POST /loans` persiste el préstamo confirmado tal cual lo envía el
  cliente.
- **Colecciones** (`GET` de listado) devuelven arreglos JSON planos. El `report` devuelve un
  arreglo de un solo elemento porque el frontend lee `reports[0]`.

---

## Mapa de endpoints

### IAM — autenticación (públicos)

| Método | Ruta                   | Cuerpo / Notas                                                        |
|--------|------------------------|----------------------------------------------------------------------|
| POST   | `/authentication/sign-in` | `{email,password}` → datos+token, o `{requiresTotp:true,userId}`  |
| POST   | `/authentication/sign-up` | `{email,password,fullName,dni,ruc,phone,businessName}` → `{id,email}` |
| POST   | `/totp-setup`          | `{userId}` → `{qrCode,secret}`                                        |
| POST   | `/verify-totp-setup`   | `{userId,token}` → `{success:true}`                                   |
| POST   | `/verify-totp`         | `{userId,token}` → datos del usuario + token                         |

### ARM — clientes y vehículos (requieren Bearer token)

CRUD completo en `/clients`, `/vehicles`, `/vehicle-specifications`, `/vehicle-commercials`
(`GET` lista, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`).

### SDP — configuración y préstamos (requieren Bearer token)

| Método | Ruta                          | Notas                                              |
|--------|-------------------------------|----------------------------------------------------|
| GET/POST/PUT | `/credit-configs[/{id}]`| Parámetros financieros del crédito                 |
| POST   | `/loans`                      | Persiste el préstamo confirmado                    |
| POST   | `/loans/simulate`             | Calcula indicadores y cronograma, sin persistir    |
| GET    | `/loans/{id}`                 | Préstamo persistido                                |
| GET    | `/loans/{id}/schedule`        | Cronograma de amortización (regenerado)            |
| GET    | `/loans/{id}/report`          | `[ { loan, config, schedule } ]` (un elemento)     |

---

## Notas

- El token JWT se firma de verdad; el frontend solo lo adjunta como `Authorization: Bearer`.
- El seeding es **idempotente** (revisa `count()==0` por tabla), así que reinicios y el perfil
  MySQL no duplican datos.
- Si cambias de máquina o red y algún dominio queda bloqueado al hacer `mvn`, revisa la
  configuración de red/proxy de tu entorno para permitir el acceso a Maven Central.
