CREATE DATABASE IF NOT EXISTS fitclub;
USE fitclub;

CREATE TABLE socio
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email     VARCHAR(150) NOT NULL UNIQUE,
    telefono  VARCHAR(20)
);

CREATE TABLE clase
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL, -- Ej: 'Spinning', 'Yoga'
    profesor   VARCHAR(100) NOT NULL,
    fecha_hora DATETIME     NOT NULL,
    aforo_max  INT          NOT NULL
);

CREATE TABLE reserva
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    socio_id      INT      NOT NULL,
    clase_id      INT      NOT NULL,
    fecha_reserva DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reserva_socio FOREIGN KEY (socio_id) REFERENCES socio (id) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_clase FOREIGN KEY (clase_id) REFERENCES clase (id) ON DELETE CASCADE,
    CONSTRAINT uq_reserva UNIQUE (socio_id, clase_id) -- Evita que un socio reserve 2 veces la misma clase
);
