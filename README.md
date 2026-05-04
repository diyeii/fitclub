# Proyecto Final 1DAM — Grupo 4

# 💪 FitClub

> *Reservas de un gimnasio*

**Modalidad:** trabajo en grupo (≈5 alumnos/as)
**Duración estimada:** 3–4 semanas
**Entrega:** repositorio en GitHub + presentación oral

---

## 1. Qué hay que hacer

Vuestro grupo desarrollará una **aplicación de consola** en Java que se conecta a una base de datos MySQL para gestionar la información de un negocio. La app muestra un menú por consola, el usuario elige una opción escribiendo un número, y la aplicación lee/escribe en la base de datos.

No es una aplicación complicada: es básicamente un **CRUD** (altas, bajas, modificaciones y consultas) sobre 3 entidades, con alguna pequeña operación extra del negocio.

---

## 2. Requisitos técnicos (lo mínimo que tiene que tener)

| Requisito | Detalle |
|---|---|
| **Java** | Java 21 |
| **Maven multimódulo** | Proyecto padre con **2 módulos**: `core` y `app` (ver §3) |
| **MySQL + JDBC** | Conexión a MySQL con `Connection` y `PreparedStatement`. **Nada de `Statement` con concatenación de Strings** |
| **Interfaz** | Aplicación de consola: bucle + `Scanner` + menús con opciones numeradas. Sin GUI |
| **GitHub** | Un repositorio por grupo, con **commits de cada miembro del equipo** |
| **Docker** | Un `docker-compose.yml` que levante MySQL con un script de creación de tablas |
| **Documentación** | Un `README.md` con cómo arrancarlo y un diagrama del modelo de datos |

> Si veo `Statement` con `+` concatenando lo que escribe el usuario, baja la nota: hemos visto en clase que eso es inyección SQL.

---

## 3. Estructura del proyecto Maven

Solo hace falta partir el proyecto en **2 módulos**:

```
proyecto-padre/                  ← pom.xml padre (packaging=pom)
├── pom.xml
├── docker/
│   ├── docker-compose.yml        ← servicio MySQL 8
│   └── init.sql                  ← CREATE TABLE + INSERTs de datos de prueba
├── core/                         ← clases del modelo (POJOs) + DAOs (JDBC)
│   ├── pom.xml
│   └── src/main/java/com/<grupo>/core/...
└── app/                          ← clase Main + menús de consola
    ├── pom.xml
    └── src/main/java/com/<grupo>/app/Main.java
```

- **`core`**: las clases que representan el modelo (una por entidad) y las clases DAO con los métodos para hablar con la base de datos (`insertar`, `buscarPorId`, `listarTodos`, `actualizar`, `borrar`).
- **`app`**: la clase `Main` y todo lo relacionado con los menús de consola y la lectura por `Scanner`.
- `app` depende de `core`. `core` no depende de nadie.

Una sola clase `ConexionBD` en `core` que devuelva una `Connection` con los datos del MySQL del docker-compose es suficiente.

---

## 4. Restricciones de diseño (para no liarla)

- **Sin Hibernate, JPA ni Spring**. JDBC a mano, como en clase.
- **Sin Swing, JavaFX ni interfaces gráficas**. Todo por consola.
- **Sin frameworks web**.
- **Sin `enum`**. Si un campo solo admite unos valores concretos (por ejemplo `"PENDIENTE"` / `"ENTREGADO"` / `"CANCELADO"`), se guarda como **`String`** y los valores válidos se documentan en un comentario en la clase.
- **Sin herencia ni clases abstractas en el modelo**. Cada entidad es una clase normal con sus atributos, su constructor y sus getters/setters.

---

## 5. Cómo debe ser la interfaz de consola

Un bucle `while` que muestra un menú numerado, lee un número con `Scanner` y entra en un `switch`. Algo así:

```
========================================
  NOCHEDEPIZZA
========================================
  1) Gestionar clientes
  2) Gestionar productos
  3) Gestionar pedidos
  0) Salir
----------------------------------------
Elige una opción:
```

Cada opción del menú principal abre un **submenú** con su propio bucle (Listar / Crear / Modificar / Eliminar / Volver) hasta que el usuario elige "Volver".

Lo que sí se valora:

- Que **no se rompa la app** si el usuario escribe una letra cuando se espera un número.
- Que los listados se muestren con un formato legible (no un volcado feo).
- Pedir confirmación antes de borrar (`¿Seguro? (s/n)`).
- Cerrar la conexión a la base de datos al salir.

---

## 6. GitHub

- Un repositorio por grupo. Una persona lo crea y añade al resto como colaboradores.
- **Cada miembro del equipo debe tener commits propios**. Si todos los commits los hace una sola persona, se penaliza. Voy a mirar la pestaña de "Contributors".
- Mensajes de commit con sentido (`Añade DAO de clientes`, no `asdfasdf`).
- Buena práctica recomendada (no obligatoria): trabajar en ramas y fusionar a `main` con Pull Requests.

---

## 7. Docker

En la carpeta `docker/` debe haber un `docker-compose.yml` que levante:

- **MySQL 8** con usuario, contraseña y base de datos en variables de entorno.
- El fichero `init.sql` montado para que MySQL lo ejecute al arrancar (creación de tablas + datos de prueba).

La aplicación Java se ejecuta en el equipo del usuario (no dentro de un contenedor). Solo dockerizamos la base de datos.

El `README.md` debe explicar cómo arrancarlo, por ejemplo:

```bash
cd docker
docker compose up -d
cd ..
mvn clean package
java -jar app/target/app.jar
```

---

## 8. Reparto del trabajo (grupos de 5)

Una sugerencia razonable es que **cada miembro se encargue de una entidad de principio a fin** (su clase del modelo, su DAO y los menús asociados), y los dos miembros restantes hagan:

- La **infraestructura común**: estructura Maven, `ConexionBD`, `docker-compose.yml`, `init.sql`, README, organización de GitHub.
- El **menú principal** y la integración de los menús de los compañeros.

Lo importante es que **todos toquéis código** y haya commits de todos en GitHub. No vale el clásico "yo es que no sé hacer eso".

---

## 9. Entregables

1. **Enlace al repositorio de GitHub**.
2. **README** con:
   - Descripción del proyecto.
   - Diagrama del modelo de datos (puede ser una imagen, una tabla o un dibujo).
   - Instrucciones para arrancarlo.
   - Quién ha hecho qué.
3. **Presentación oral** de 10 minutos con demo en vivo.

---

## 10. Cómo se evalúa

| Bloque | Peso |
|---|---|
| Que el proyecto **funcione** (CRUDs operativos, sin crashes) | 35% |
| Acceso a datos correcto (JDBC, `PreparedStatement`, DAOs) | 20% |
| Estructura del proyecto Maven multimódulo | 15% |
| Interfaz de consola (claridad, validación de entrada) | 10% |
| Uso de Git/GitHub (commits repartidos) | 10% |
| Documentación + Docker funcionando | 10% |

---

## 11. Plus opcionales (subida de nota)

- **Login** sencillo: usuario y contraseña en una tabla `usuarios`, comparar al arrancar.
- **Exportar a CSV** alguno de los listados.
- **Datos de prueba abundantes** en el `init.sql` (mínimo 10 filas por tabla).
- **Trabajar con ramas y Pull Requests** en GitHub (en vez de hacer push directo a `main`).
- **Filtros y ordenación** en los listados (por ejemplo, ordenar por fecha o filtrar por nombre).

---

## 12. Calendario sugerido

| Semana | Hito |
|---|---|
| 1 | Repartir entidades. Repo de GitHub creado. `docker-compose.yml` levantando MySQL. Esqueleto Maven con los 2 módulos compilando. `init.sql` con las tablas. |
| 2 | Cada cual termina su clase del modelo + DAO. Al final de la semana se puede insertar y listar desde Java. |
| 3 | Menús de consola completos. La app funciona de punta a punta. |
| 4 | Pulir, plus opcionales, README, ensayo de la presentación. |

---


# Enunciado del Grupo 4: FitClub

### Contexto

FitClub es un gimnasio de barrio con clases dirigidas (spinning, yoga, zumba…). Los socios pueden reservar plaza en las clases que les interesan, pero el aforo es limitado. Queréis hacer una aplicación para gestionar socios, clases y reservas.

### Entidades (3)

- **Socio**: `id`, `nombre`, `apellidos`, `email`, `teléfono`
- **Clase**: `id`, `nombre`, `profesor`, `fecha_hora`, `aforo_max`
- **Reserva**: `id`, `socio_id`, `clase_id`, `fecha_reserva`

### Funcionalidades

1. **CRUD de socios**: alta, listar, modificar, borrar.
2. **CRUD de clases**: alta, listar, modificar, borrar.
3. **Reservar plaza**: pedir un socio y una clase. Validar que **queden plazas libres** (que el número de reservas existentes para esa clase sea menor que `aforo_max`) y que ese socio no la haya reservado ya.
4. **Cancelar reserva**: pedir un socio, mostrar sus reservas y permitir cancelar una.
5. **Ver clases con plazas libres**: listar las clases futuras mostrando para cada una "X plazas libres de Y" (Y = aforo_max).
6. **Ver reservas de un socio**: pedir un socio y mostrar todas sus reservas.

### Reglas sencillas

- No se puede reservar una clase que ya ha pasado (fecha anterior a hoy).
- No se puede borrar un socio que tenga reservas activas.

### Menú principal

```
1) Socios
2) Clases
3) Reservas
0) Salir
```

---

¡Mucha suerte!
