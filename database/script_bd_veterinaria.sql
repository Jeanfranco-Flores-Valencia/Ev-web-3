-- =====================================================================
--  SISTEMA DE ATENCION VETERINARIA - MICROSERVICIOS
--  Autor: Jeanfranco Anderson Flores Valencia
--  Script de bases de datos (MySQL 8 / MariaDB 10.x)
--
--  Cada microservicio tiene SU PROPIA base de datos. No hay claves foraneas
--  entre bases: las referencias (id_propietario, id_mascota) son solo IDs y
--  se validan por REST entre los microservicios.
--
--  NOTA: en el perfil dev NO es necesario ejecutar este script; Hibernate
--  crea las tablas (ddl-auto: update) y carga los datos de prueba (data-mysql.sql).
--  Este script es necesario para el perfil prod (ddl-auto: validate).
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1) bd_propietario  ->  propietario-service (puerto 8081)
-- ---------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_propietario CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE bd_propietario;

CREATE TABLE IF NOT EXISTS propietarios (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  dni             VARCHAR(8)   NOT NULL,
  nombres         VARCHAR(80)  NOT NULL,
  apellidos       VARCHAR(80)  NOT NULL,
  telefono        VARCHAR(15)  NOT NULL,
  correo          VARCHAR(100) NULL,
  direccion       VARCHAR(150) NULL,
  fecha_registro  DATETIME(6)  NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_propietarios_dni UNIQUE (dni)
) ENGINE=InnoDB;

INSERT IGNORE INTO propietarios (id, dni, nombres, apellidos, telefono, correo, direccion, fecha_registro) VALUES
(1, '45871236', 'Carlos Alberto', 'Ramirez Soto', '987654321', 'carlos.ramirez@correo.com', 'Av. Arequipa 1250, Lima', '2026-01-10 09:00:00'),
(2, '70123456', 'Maria Fernanda', 'Quispe Huaman', '956321478', 'maria.quispe@correo.com', 'Jr. Los Pinos 340, Santa Anita', '2026-02-15 10:30:00'),
(3, '41239875', 'Luis Enrique', 'Torres Medina', '912345678', 'luis.torres@correo.com', 'Calle Las Flores 89, Surco', '2026-03-20 16:45:00');

-- ---------------------------------------------------------------------
-- 2) bd_mascota  ->  mascota-service (puerto 8082)
-- ---------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_mascota CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE bd_mascota;

CREATE TABLE IF NOT EXISTS mascotas (
  id                BIGINT        NOT NULL AUTO_INCREMENT,
  nombre            VARCHAR(60)   NOT NULL,
  especie           VARCHAR(40)   NOT NULL,
  raza              VARCHAR(60)   NULL,
  sexo              ENUM('HEMBRA','MACHO') NULL,
  fecha_nacimiento  DATE          NOT NULL,
  peso              DECIMAL(6,2)  NULL,
  id_propietario    BIGINT        NOT NULL,   -- referencia logica a bd_propietario.propietarios.id
  PRIMARY KEY (id),
  INDEX idx_mascotas_propietario (id_propietario)
) ENGINE=InnoDB;

-- id_propietario referencia a los propietarios 1, 2 y 3 de bd_propietario.
INSERT IGNORE INTO mascotas (id, nombre, especie, raza, sexo, fecha_nacimiento, peso, id_propietario) VALUES
(1, 'Firulais', 'Perro', 'Labrador', 'MACHO', '2021-05-14', 28.50, 1),
(2, 'Michi', 'Gato', 'Siames', 'HEMBRA', '2022-08-02', 4.20, 1),
(3, 'Rocky', 'Perro', 'Pastor Aleman', 'MACHO', '2019-11-20', 34.00, 2),
(4, 'Pelusa', 'Conejo', 'Mini Lop', 'HEMBRA', '2024-02-10', 1.80, 3);

-- ---------------------------------------------------------------------
-- 3) bd_consulta  ->  consulta-service (puerto 8083)
-- ---------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_consulta CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE bd_consulta;

CREATE TABLE IF NOT EXISTS consultas (
  id           BIGINT         NOT NULL AUTO_INCREMENT,
  id_mascota   BIGINT         NOT NULL,   -- referencia logica a bd_mascota.mascotas.id
  fecha        DATE           NOT NULL,
  motivo       VARCHAR(200)   NOT NULL,
  diagnostico  VARCHAR(500)   NOT NULL,
  tratamiento  VARCHAR(500)   NULL,
  veterinario  VARCHAR(100)   NULL,
  costo        DECIMAL(10,2)  NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_consultas_mascota (id_mascota),
  CONSTRAINT ck_consultas_costo CHECK (costo >= 0)
) ENGINE=InnoDB;

-- id_mascota referencia a las mascotas 1 a 4 de bd_mascota.
INSERT IGNORE INTO consultas (id, id_mascota, fecha, motivo, diagnostico, tratamiento, veterinario, costo) VALUES
(1, 1, '2026-09-05', 'Vacunacion anual', 'Paciente sano, se aplica vacuna sextuple', 'Vacuna sextuple + desparasitacion', 'Dra. Ana Lopez', 80.00),
(2, 1, '2026-10-12', 'Cojera en pata trasera', 'Esguince leve', 'Antiinflamatorio por 5 dias y reposo', 'Dr. Jorge Paredes', 120.50),
(3, 2, '2026-10-20', 'Perdida de apetito', 'Gastritis leve', 'Dieta blanda y protector gastrico', 'Dra. Ana Lopez', 95.00),
(4, 3, '2026-11-02', 'Control de peso', 'Sobrepeso moderado', 'Dieta controlada y paseos diarios', 'Dr. Jorge Paredes', 60.00);
