# Desplegar SmartDrive con PostgreSQL en Supabase

## Arquitectura

Supabase aloja la base PostgreSQL. El backend Spring Boot sigue ejecutándose como
una API independiente, por ejemplo en Render, Railway, Fly.io o cualquier servicio
que ejecute el `Dockerfile` del proyecto.

```text
Angular -> API Spring Boot -> PostgreSQL de Supabase
```

Supabase no ejecuta directamente este backend Java como una aplicación Spring.

## 1. Crear el proyecto de Supabase

1. Crea un proyecto en [supabase.com/dashboard](https://supabase.com/dashboard).
2. Guarda la contraseña de la base de datos. No la coloques en Git ni en el frontend.
3. En el proyecto abre **Connect** y copia la conexión **Session pooler** si el
   hosting del backend solo tiene IPv4. Usa el puerto `5432`.
4. Usa la conexión directa si el hosting tiene IPv6 y necesitas ejecutar migraciones
   o comandos administrativos. Supabase documenta las diferencias entre conexión
   directa, session pooler y transaction pooler en:
   [Connect to your database](https://supabase.com/docs/guides/database/connecting-to-postgres).

## 2. Convertir la conexión a JDBC

Spring necesita una URL JDBC, no la URL `postgres://` usada normalmente por `psql`.

Session pooler:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-REGION.pooler.supabase.com:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.PROJECT_REF
SPRING_DATASOURCE_PASSWORD=TU_PASSWORD_DE_SUPABASE
```

Conexión directa:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://db.PROJECT_REF.supabase.co:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=TU_PASSWORD_DE_SUPABASE
```

Reemplaza `REGION` y `PROJECT_REF` con los valores que aparecen en **Connect**.
No inventes el host: copia el que muestra tu proyecto.

## 3. Variables del backend

Configúralas como variables privadas en el proveedor donde despliegues Spring:

```text
SPRING_PROFILES_ACTIVE=supabase
PORT=8080
SPRING_DATASOURCE_URL=jdbc:postgresql://.../postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.PROJECT_REF
SPRING_DATASOURCE_PASSWORD=TU_PASSWORD_DE_SUPABASE
JPA_DDL_AUTO=update
JWT_SECRET=un_secreto_largo_y_aleatorio
APP_CRYPTO_AES_KEY=clave_base64_de_32_bytes
CORS_ORIGIN=https://TU-FRONTEND.example
GEMINI_API_KEY=
```

Genera los secretos desde una terminal local:

```bash
openssl rand -base64 32
```

Usa una salida para `JWT_SECRET` y otra para `APP_CRYPTO_AES_KEY`. La segunda
debe ser una clave Base64 que decodifique a 16, 24 o 32 bytes. Si ya tienes datos
cifrados, conserva la misma `APP_CRYPTO_AES_KEY`; cambiarla impide descifrar los
DNI existentes.

## 4. Primer despliegue

El perfil `supabase` usa `JPA_DDL_AUTO=update` para crear las tablas JPA en un
proyecto Supabase vacío. El `DemoDataSeeder` no se ejecuta con este perfil, por lo
que no se insertan usuarios ni créditos de demostración en producción.

Después de confirmar que la API inició correctamente, cambia:

```text
JPA_DDL_AUTO=validate
```

Así Hibernate deja de alterar el esquema automáticamente. Las futuras modificaciones
de tablas deben hacerse mediante migraciones SQL versionadas antes de desplegar una
nueva versión.

## 5. Desplegar el backend

El `Dockerfile` ya compila el proyecto con Java 21 y expone el puerto `8080`.
En Render/Railway:

1. Conecta el repositorio.
2. Selecciona despliegue mediante Docker.
3. Agrega las variables anteriores como secretos.
4. Espera a que el servicio termine el build.
5. Verifica `https://TU-API/swagger-ui.html` o una ruta pública de autenticación.

La URL pública de esa API debe quedar en el `platformProviderApiBaseUrl` del entorno
de producción del frontend Angular. Después recompila y vuelve a desplegar el frontend.

## 6. Comprobaciones en Supabase

Desde el SQL Editor puedes comprobar que la API está conectada:

```sql
select current_database(), current_user, version();
```

Luego verifica en **Table Editor** que existan tablas como `users`, `companies`,
`clients`, `vehicles`, `credit_configs`, `loans` y `loan_vehicle`.

Nunca uses la `service_role` key en Angular. Este backend se conecta directamente a
PostgreSQL con la contraseña de la base; las claves públicas de Supabase no sustituyen
esa conexión JDBC.
