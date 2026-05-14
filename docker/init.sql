CREATE
DATABASE IF NOT EXISTS fitclub;
USE
fitclub;

-- ─────────────────────────────────────────────────────────────────────────────
-- TABLAS
-- ─────────────────────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS socio
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    nombre
    VARCHAR
(
    100
) NOT NULL,
    apellidos VARCHAR
(
    150
) NOT NULL,
    email VARCHAR
(
    150
) NOT NULL UNIQUE,
    telefono VARCHAR
(
    20
)
    );

CREATE TABLE IF NOT EXISTS clase
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    nombre
    VARCHAR
(
    100
) NOT NULL,
    profesor VARCHAR
(
    100
) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    aforo_max INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS reserva
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    socio_id
    INT
    NOT
    NULL,
    clase_id
    INT
    NOT
    NULL,
    fecha_reserva
    DATETIME
    NOT
    NULL
    DEFAULT
    CURRENT_TIMESTAMP,

    CONSTRAINT
    fk_reserva_socio
    FOREIGN
    KEY
(
    socio_id
) REFERENCES socio
(
    id
) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_clase FOREIGN KEY
(
    clase_id
) REFERENCES clase
(
    id
)
  ON DELETE CASCADE,
    CONSTRAINT uq_reserva UNIQUE
(
    socio_id,
    clase_id
)
    );

-- ─────────────────────────────────────────────────────────────────────────────
-- DATOS DE PRUEBA — SOCIOS (15 socios)
-- ─────────────────────────────────────────────────────────────────────────────

INSERT INTO socio (nombre, apellidos, email, telefono)
VALUES ('Laura', 'García Martínez', 'laura.garcia@email.com', '612 345 678'),
       ('Carlos', 'López Fernández', 'carlos.lopez@email.com', '623 456 789'),
       ('María', 'Sánchez Ruiz', 'maria.sanchez@email.com', '634 567 890'),
       ('Alejandro', 'Martínez Torres', 'alex.martinez@email.com', '645 678 901'),
       ('Sofía', 'Rodríguez Gómez', 'sofia.rodriguez@email.com', '656 789 012'),
       ('Daniel', 'González Jiménez', 'daniel.gonzalez@email.com', '667 890 123'),
       ('Lucía', 'Fernández Díaz', 'lucia.fernandez@email.com', '678 901 234'),
       ('Pablo', 'Díaz Moreno', 'pablo.diaz@email.com', '689 012 345'),
       ('Marta', 'Moreno Álvarez', 'marta.moreno@email.com', '690 123 456'),
       ('Jorge', 'Jiménez Romero', 'jorge.jimenez@email.com', '601 234 567'),
       ('Ana', 'Romero Navarro', 'ana.romero@email.com', '612 345 000'),
       ('Sergio', 'Navarro Iglesias', 'sergio.navarro@email.com', '623 456 111'),
       ('Elena', 'Iglesias Herrero', 'elena.iglesias@email.com', '634 567 222'),
       ('Marcos', 'Herrero Castillo', 'marcos.herrero@email.com', '645 678 333'),
       ('Beatriz', 'Castillo Vega', 'beatriz.castillo@email.com', '656 789 444');

-- ─────────────────────────────────────────────────────────────────────────────
-- DATOS DE PRUEBA — CLASES (12 clases: pasadas + futuras)
-- ─────────────────────────────────────────────────────────────────────────────

INSERT INTO clase (nombre, profesor, fecha_hora, aforo_max)
VALUES
-- Clases pasadas (para probar que no se pueden reservar)
('Spinning', 'Roberto Vega', '2025-04-10 09:00:00', 15),
('Yoga', 'Carmen Ibáñez', '2025-04-12 11:00:00', 10),
('Zumba', 'Patricia Blanco', '2025-04-15 18:30:00', 20),

-- Clases futuras
('Spinning', 'Roberto Vega', DATE_ADD(NOW(), INTERVAL 1 DAY), 15),
('Yoga', 'Carmen Ibáñez', DATE_ADD(NOW(), INTERVAL 2 DAY), 10),
('Zumba', 'Patricia Blanco', DATE_ADD(NOW(), INTERVAL 3 DAY), 20),
('Pilates', 'Mónica Serrano', DATE_ADD(NOW(), INTERVAL 4 DAY), 12),
('CrossFit', 'Adrián Molina', DATE_ADD(NOW(), INTERVAL 5 DAY), 8),
('Body Pump', 'Roberto Vega', DATE_ADD(NOW(), INTERVAL 6 DAY), 18),
('Aquagym', 'Carmen Ibáñez', DATE_ADD(NOW(), INTERVAL 7 DAY), 25),
('GAP', 'Mónica Serrano', DATE_ADD(NOW(), INTERVAL 8 DAY), 15),
('Meditación', 'Carmen Ibáñez', DATE_ADD(NOW(), INTERVAL 9 DAY), 10);

-- ─────────────────────────────────────────────────────────────────────────────
-- DATOS DE PRUEBA — RESERVAS (mezcla variada)
-- ─────────────────────────────────────────────────────────────────────────────

-- Reservas en clases futuras (IDs 4-12)
INSERT INTO reserva (socio_id, clase_id, fecha_reserva)
VALUES (1, 4, NOW()),
       (2, 4, NOW()),
       (3, 4, NOW()),
       (4, 5, NOW()),
       (5, 5, NOW()),
       (6, 6, NOW()),
       (7, 6, NOW()),
       (8, 6, NOW()),
       (9, 7, NOW()),
       (10, 7, NOW()),
       (1, 8, NOW()),
       (2, 9, NOW()),
       (3, 10, NOW()),
       (4, 11, NOW()),
       (5, 12, NOW()),
       (11, 4, NOW()),
       (12, 5, NOW()),
       (13, 6, NOW()),
       (14, 7, NOW()),
       (15, 8, NOW());
