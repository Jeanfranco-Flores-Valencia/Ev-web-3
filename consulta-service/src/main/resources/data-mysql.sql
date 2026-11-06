-- Datos de prueba del microservicio Consulta (bd_consulta)
-- id_mascota referencia a las mascotas 1 a 4 de bd_mascota.
INSERT IGNORE INTO consultas (id, id_mascota, fecha, motivo, diagnostico, tratamiento, veterinario, costo) VALUES
(1, 1, '2026-09-05', 'Vacunacion anual', 'Paciente sano, se aplica vacuna sextuple', 'Vacuna sextuple + desparasitacion', 'Dra. Ana Lopez', 80.00),
(2, 1, '2026-10-12', 'Cojera en pata trasera', 'Esguince leve', 'Antiinflamatorio por 5 dias y reposo', 'Dr. Jorge Paredes', 120.50),
(3, 2, '2026-10-20', 'Perdida de apetito', 'Gastritis leve', 'Dieta blanda y protector gastrico', 'Dra. Ana Lopez', 95.00),
(4, 3, '2026-11-02', 'Control de peso', 'Sobrepeso moderado', 'Dieta controlada y paseos diarios', 'Dr. Jorge Paredes', 60.00);
