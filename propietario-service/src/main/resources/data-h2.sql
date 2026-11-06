-- Datos de prueba del microservicio Propietario (H2 en memoria, perfil test)
INSERT INTO propietarios (id, dni, nombres, apellidos, telefono, correo, direccion, fecha_registro) VALUES
(1, '45871236', 'Carlos Alberto', 'Ramirez Soto', '987654321', 'carlos.ramirez@correo.com', 'Av. Arequipa 1250, Lima', '2026-01-10 09:00:00'),
(2, '70123456', 'Maria Fernanda', 'Quispe Huaman', '956321478', 'maria.quispe@correo.com', 'Jr. Los Pinos 340, Santa Anita', '2026-02-15 10:30:00'),
(3, '41239875', 'Luis Enrique', 'Torres Medina', '912345678', 'luis.torres@correo.com', 'Calle Las Flores 89, Surco', '2026-03-20 16:45:00');

-- Como los IDs se insertan manualmente, se reinicia el contador para los nuevos registros
ALTER TABLE propietarios ALTER COLUMN id RESTART WITH 4;
