CREATE DATABASE fitclub;
USE fitclub;

CREATE TABLE socio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    fecha_alta DATE NOT NULL,
    tipo_cuota ENUM('MENSUAL', 'TRIMESTRAL', 'ANUAL') NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE profesor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL
);

CREATE TABLE tipo_clase (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    duracion_min INT NOT NULL,
    aforo_max INT NOT NULL,

    CONSTRAINT chk_tipo_clase_duracion
        CHECK (duracion_min > 0),

    CONSTRAINT chk_tipo_clase_aforo
        CHECK (aforo_max > 0)
);

CREATE TABLE profesor_tipo_clase (
    profesor_id INT NOT NULL,
    tipo_clase_id INT NOT NULL,

    PRIMARY KEY (profesor_id, tipo_clase_id),

    CONSTRAINT fk_profesor_tipo_profesor
        FOREIGN KEY (profesor_id)
        REFERENCES profesor(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_profesor_tipo_clase
        FOREIGN KEY (tipo_clase_id)
        REFERENCES tipo_clase(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE sesion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_clase_id INT NOT NULL,
    profesor_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado ENUM('PROGRAMADA', 'REALIZADA', 'CANCELADA') NOT NULL DEFAULT 'PROGRAMADA',

    CONSTRAINT fk_sesion_tipo_clase
        FOREIGN KEY (tipo_clase_id)
        REFERENCES tipo_clase(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_sesion_profesor
        FOREIGN KEY (profesor_id)
        REFERENCES profesor(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sesion_id INT NOT NULL,
    socio_id INT NOT NULL,
    fecha_reserva DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    asistio BOOLEAN DEFAULT NULL,

    CONSTRAINT fk_reserva_sesion
        FOREIGN KEY (sesion_id)
        REFERENCES sesion(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_reserva_socio
        FOREIGN KEY (socio_id)
        REFERENCES socio(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uq_reserva_sesion_socio
        UNIQUE (sesion_id, socio_id)
);

CREATE TABLE cuota (
    id INT AUTO_INCREMENT PRIMARY KEY,
    socio_id INT NOT NULL,
    mes TINYINT NOT NULL,
    anio YEAR NOT NULL,
    importe DECIMAL(8,2) NOT NULL,
    pagada BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_pago DATE DEFAULT NULL,

    CONSTRAINT fk_cuota_socio
        FOREIGN KEY (socio_id)
        REFERENCES socio(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT chk_cuota_mes
        CHECK (mes BETWEEN 1 AND 12),

    CONSTRAINT chk_cuota_importe
        CHECK (importe >= 0),

    CONSTRAINT uq_cuota_socio_mes_anio
        UNIQUE (socio_id, mes, anio)
);
