-- Datos de prueba del microservicio Mascota (bd_mascota)
-- id_propietario referencia a los propietarios 1, 2 y 3 de bd_propietario.
INSERT IGNORE INTO mascotas (id, nombre, especie, raza, sexo, fecha_nacimiento, peso, id_propietario) VALUES
(1, 'Firulais', 'Perro', 'Labrador', 'MACHO', '2021-05-14', 28.50, 1),
(2, 'Michi', 'Gato', 'Siames', 'HEMBRA', '2022-08-02', 4.20, 1),
(3, 'Rocky', 'Perro', 'Pastor Aleman', 'MACHO', '2019-11-20', 34.00, 2),
(4, 'Pelusa', 'Conejo', 'Mini Lop', 'HEMBRA', '2024-02-10', 1.80, 3);
