# Proyecto Final 1DAM — Grupo 4

# 💪 FitClub

> *Gestión de un gimnasio con clases dirigidas*

**Modalidad:** trabajo en grupo (≈5 alumnos/as)
**Duración estimada:** 3–4 semanas
**Entrega:** repositorio en GitHub + presentación oral del proyecto

---

## 1. Objetivo general

Vuestro grupo desarrollará una aplicación Java de escritorio que integre **todo lo aprendido durante el curso**: programación orientada a objetos, organización del código con **Maven multimódulo**, persistencia con **JDBC** sobre **MySQL**, interfaz gráfica con **Swing**, control de versiones con **Git/GitHub** y despliegue del entorno con **Docker**.

El producto final no es solo el código: es también la documentación, la organización del trabajo en equipo y el repositorio limpio.

---

## 2. Requisitos técnicos comunes (obligatorios)

| Requisito | Descripción mínima |
|---|---|
| **Java** | Java 21 |
| **Maven multimódulo** | Proyecto padre con al menos 4 módulos (ver §4) |
| **MySQL + JDBC** | Sin ORMs. Acceso a datos con `Connection`, `PreparedStatement`, `ResultSet` y patrón **DAO** |
| **Swing** | Interfaz gráfica con varias ventanas/pestañas. Validación de formularios |
| **GitHub** | Repositorio público o de la organización del centro. Uso real de **ramas y Pull Requests** |
| **Docker** | `docker-compose.yml` que levante MySQL (y opcionalmente phpMyAdmin) con los datos del proyecto |
| **Documentación** | `README.md` profesional + diagrama E/R + capturas de la app |

> **No vale** una app monolítica con un único `main()` y SQL embebido en los `JButton`. Si veo `Statement` con concatenación de strings en vez de `PreparedStatement`, baja la nota automáticamente (y abre la puerta a inyección SQL, que ya hemos visto en clase).

---

## 3. Tecnologías que **no** se permiten

Para asegurar que se aplica lo estudiado:

- **Sin Hibernate, JPA ni Spring Data**. El acceso a datos se hace a mano con JDBC.
- **Sin JavaFX**. La interfaz va en Swing.
- **Sin frameworks web** (esto va para los pícaros). Es una aplicación de escritorio.
- Se permiten librerías auxiliares pequeñas: por ejemplo `mysql-connector-j`, `slf4j` + `logback`, `junit-jupiter` para tests, `jcalendar` o similar para selectores de fecha si lo necesitáis.

---

## 4. Estructura recomendada del proyecto Maven

```
proyecto-padre/                 ← pom.xml de tipo "pom" (parent)
├── pom.xml
├── docker/
│   ├── docker-compose.yml       ← servicio "db" con MySQL 8
│   └── init.sql                 ← script de creación de tablas + datos de prueba
├── core/                        ← modelo de dominio + lógica de negocio
│   ├── pom.xml
│   └── src/main/java/com/<grupo>/core/...
├── persistence/                 ← DAOs e interfaces de persistencia (JDBC)
│   ├── pom.xml
│   └── src/main/java/com/<grupo>/persistence/...
├── ui/                          ← interfaz Swing (JFrames, paneles, modelos de tabla)
│   ├── pom.xml
│   └── src/main/java/com/<grupo>/ui/...
└── app/                         ← módulo ensamblador con la clase Main
    ├── pom.xml
    └── src/main/java/com/<grupo>/app/Main.java
```

**Dependencias entre módulos:**

- `persistence` depende de `core`
- `ui` depende de `core`
- `app` depende de `ui`, `persistence` y `core`
- `core` no depende de nadie (es el corazón del dominio)

El módulo `app` debe generar un **fat jar ejecutable** (`maven-shade-plugin` o `maven-assembly-plugin`) de manera que `java -jar app.jar` lance la aplicación.

---

## 5. Cómo organizar el trabajo en el equipo (5 personas)

Reparto orientativo. **Todos** tocan código de todos los módulos en algún momento, pero cada cual lidera un área:

| Rol | Responsabilidad principal |
|---|---|
| **Coordinador / DevOps** | Estructura Maven multimódulo, `docker-compose.yml`, organización del repo de GitHub, gestión de ramas y PRs, integración continua manual, README final |
| **Backend — Persistencia** | Diseño del modelo E/R, `init.sql`, conexión JDBC, DAOs, manejo de transacciones |
| **Backend — Dominio** | Clases del módulo `core`, servicios, validaciones, reglas de negocio, tests JUnit |
| **Frontend — Gestión** | Pantallas Swing de mantenimiento (CRUDs): altas, bajas, modificaciones, búsquedas |
| **Frontend — Operación** | Pantallas Swing del flujo principal del negocio (las "interesantes": reservas, pedidos, brackets, préstamos…) + integración final |

> Cada miembro del equipo debe tener **commits propios** en GitHub. Si todos los commits los hace una sola persona, **eso se penaliza**. La gráfica de contributors la voy a mirar.

---

## 6. Flujo de trabajo en GitHub (obligatorio)

1. Una persona crea el repositorio y añade al resto como colaboradores.
2. Rama `main` **protegida**: nadie hace push directo.
3. Cada funcionalidad se desarrolla en una rama: `feature/login`, `feature/dao-cliente`, `fix/calculo-total`…
4. Para integrar a `main` se abre un **Pull Request** que **otro compañero/a** debe revisar y aprobar.
5. Mensajes de commit con sentido (`feat: añadir DAO de pedidos`, no `asdfasdf`).
6. Issues de GitHub para repartir tareas.

---

## 7. Docker — Despliegue del entorno

En la carpeta `docker/` debe haber un `docker-compose.yml` que levante al menos:

- **MySQL 8.x** con un volumen persistente, usuario y contraseña en variables de entorno, y el `init.sql` montado para que se cargue al arrancar.
- (Opcional pero recomendado) **phpMyAdmin** o **adminer** para depuración.

La aplicación Swing **se ejecuta en el equipo del usuario**, no dentro de un contenedor (los contenedores con GUI se complican y no entra en el alcance del curso). Solo dockerizamos la base de datos.

El `README.md` debe incluir las instrucciones exactas:

```bash
cd docker
docker compose up -d
cd ..
mvn clean package
java -jar app/target/app-<version>-shaded.jar
```

---

## 8. Entregables

1. **URL del repositorio de GitHub** (un único repo por grupo).
2. **Memoria técnica** (en el README o en un PDF dentro del repo) con:
   - Descripción del proyecto
   - Diagrama de clases (módulo `core`)
   - Diagrama Entidad/Relación de la BD
   - Capturas de cada pantalla
   - Instrucciones de instalación y ejecución
   - Reparto real de tareas y enlace a los PRs de cada miembro
3. **Presentación oral** de 10–15 minutos con demo en vivo. Todos los miembros intervienen.

---

## 9. Criterios de evaluación

| Bloque | Peso |
|---|---|
| Funcionalidad implementada (que el proyecto **funcione**) | 30% |
| Calidad del código y arquitectura multimódulo | 20% |
| Acceso a datos (JDBC, DAOs, integridad) | 15% |
| Interfaz gráfica (usabilidad, validaciones) | 15% |
| Uso real de Git/GitHub (commits repartidos, PRs, ramas) | 10% |
| Documentación + Docker reproducible | 10% |

---

## 10. Plus opcionales (subida de nota)

- **Login y roles**: pantalla de autenticación con varios tipos de usuario (admin/operador) y permisos distintos.
- **Hash de contraseñas** con `BCrypt` o similar (no guardarlas en claro).
- **Exportar a CSV o PDF** algún listado o informe.
- **Logging** con SLF4J + Logback en lugar de `System.out.println`.
- **Tests JUnit** del módulo `core` con cobertura razonable.
- **Internacionalización (i18n)** con archivos `messages_es.properties` / `messages_en.properties`.
- **GitHub Actions** que ejecute `mvn verify` en cada PR.

---

## 11. Calendario sugerido

| Semana | Hito |
|---|---|
| 1 | Reparto de roles, diseño del E/R, repo de GitHub creado, `docker-compose.yml` levantando MySQL, esqueleto Maven multimódulo compilando |
| 2 | DAOs y módulo `core` listos. Primeras pantallas Swing de mantenimiento. Tests del `core` |
| 3 | Flujo principal del negocio funcionando end-to-end |
| 4 | Plus, pulido, memoria técnica, capturas, ensayo de la presentación |

---


# Enunciado del Grupo 4: FitClub

### Contexto

FitClub es un gimnasio de barrio con sala de musculación y clases dirigidas (spinning, zumba, yoga, body-pump…). Quieren una aplicación para gestionar a los socios, programar las clases del mes, controlar las reservas con aforo limitado y llevar el control de las cuotas mensuales.

### Entidades principales (mínimo)

- **Socio**: `id`, `nombre`, `apellidos`, `email`, `teléfono`, `fecha_alta`, `tipo_cuota` (MENSUAL/TRIMESTRAL/ANUAL), `activo`
- **Profesor**: `id`, `nombre`, `apellidos`, `especialidades` (puede ser una relación N:M con tipos de clase)
- **TipoClase**: `id`, `nombre`, `descripción`, `duración_min`, `aforo_max`
- **Sesión**: `id`, `tipo_clase_id`, `profesor_id`, `fecha_hora`, `estado` (PROGRAMADA/REALIZADA/CANCELADA)
- **Reserva**: `id`, `sesión_id`, `socio_id`, `fecha_reserva`, `asistio`
- **Cuota**: `id`, `socio_id`, `mes`, `año`, `importe`, `pagada`, `fecha_pago`

### Casos de uso obligatorios

1. CRUD de **socios**, **profesores** y **tipos de clase**.
2. **Programar sesión**: seleccionar tipo de clase, profesor (que la imparta), fecha y hora. Validar que el profesor no tiene otra sesión a esa hora.
3. **Reservar plaza** en una sesión: validar que el socio está al día con las cuotas, que la sesión está PROGRAMADA y que **queda aforo**.
4. **Cancelar reserva**: con política (p. ej., hasta 2h antes del inicio).
5. **Marcar asistencia** al cierre de la sesión.
6. **Generar cuotas mensuales**: botón "Generar cuotas del mes" que crea automáticamente la cuota del mes para todos los socios activos.
7. **Listar morosos** (socios con cuotas no pagadas) y permitir marcar la cuota como pagada.
8. Informes: clases más demandadas, ocupación media de cada sesión, profesor con más asistentes.

### Pantallas Swing mínimas

- Calendario semanal/mensual con las sesiones programadas (puede ser un `JTable` con días en columnas).
- Mantenimiento de socios, profesores y tipos de clase.
- Pantalla "Reservar plaza" desde el calendario.
- Panel de cuotas y morosos.
- Informes.

### Dificultades a tener en cuenta

- La validación de aforo debe ser robusta: no se pueden hacer dos reservas simultáneas que dejen el aforo por encima del límite.
- La generación de cuotas mensuales debe ser idempotente: si la pulsas dos veces el mismo mes, no debe duplicar las cuotas.

---

¡Mucha suerte!
