-- ===================================================================================
-- V6: CARGA MASIVA DE DATOS DE PRUEBA (CORREGIDO PARA INTEGRIDAD Y CHECK CONSTRAINTS)
-- ===================================================================================

-- 1. Nuevas Especialidades y Tipos
INSERT INTO especialidad (nombre) VALUES ('neurologia'), ('dermatologia'), ('ortopedia'), ('pediatria');
INSERT INTO tipo_cita (nombre) VALUES ('urgencia'), ('valoracion especialista');

-- 2. Departamentos y Ciudades
INSERT INTO departamento (nombre) VALUES ('cundinamarca'), ('valle del cauca'), ('atlantico');
INSERT INTO ciudad (codigo, nombre, id_departamento, estado) VALUES
('CIU003', 'bogota', 3, true),
('CIU004', 'cali', 4, true),
('CIU005', 'barranquilla', 5, true);

-- 3. Nuevas Aseguradoras (EPS)
INSERT INTO eps (codigo, nombre) VALUES
('EPS003', 'salud total'),
('EPS004', 'compensar'),
('EPS005', 'coomeva'),
('EPS006', 'nueva eps');

-- 4. Nuevos Hospitales y Sedes
INSERT INTO hospital (codigo, nombre, direccion, telefono, estado, codigo_ciudad) VALUES
('HOS003', 'fundacion santa fe', 'calle 119 #7-75', '6013333333', true, 'CIU003'),
('HOS004', 'clinica valle del lili', 'cra 98 #18-49', '6024444444', true, 'CIU004'),
('HOS005', 'clinica portoazul', 'cra 50 #90-85', '6055555555', true, 'CIU005');

-- 5. Creación Masiva de Pacientes
INSERT INTO usuario (documento, nombre, apellido, correo, telefono, fecha_nacimiento, contrasena, estado, rol, codigo_eps, codigo_ciudad) VALUES
('PAC003', 'andres', 'vargas', 'andres@correo.com', '3100000001', '1985-08-20', '1234', true, 'PACIENTE', 'EPS003', 'CIU003'),
('PAC004', 'laura', 'gomez', 'laura@correo.com', '3100000002', '1992-11-15', '1234', true, 'PACIENTE', 'EPS004', 'CIU003'),
('PAC005', 'camilo', 'rojas', 'camilo@correo.com', '3100000003', '1978-02-10', '1234', true, 'PACIENTE', 'EPS005', 'CIU004'),
('PAC006', 'diana', 'castro', 'diana@correo.com', '3100000004', '2001-09-05', '1234', true, 'PACIENTE', 'EPS006', 'CIU005');

-- 6. Creación Masiva de Médicos
INSERT INTO medico (documento, nombre, apellido, id_especialidad, telefono, correo, estado, codigo_ciudad) VALUES
('MED003', 'jorge', 'martinez', 3, '3200000001', 'jorge.neuro@hospital.com', true, 'CIU003'),
('MED004', 'ana', 'torres', 4, '3200000002', 'ana.derma@hospital.com', true, 'CIU003'),
('MED005', 'luis', 'diaz', 5, '3200000003', 'luis.orto@hospital.com', true, 'CIU004');

-- 7. Catálogo de Servicios
INSERT INTO servicio (codigo, nombre, descripcion, costo, estado, id_tipo_servicio) VALUES
('SRV003', 'resonancia magnetica', 'rm cerebral y espinal', 350000.00, true, 2),
('SRV004', 'consulta neurologica', 'valoracion especialista', 150000.00, true, 1),
('SRV005', 'radiografia de torax', 'rx ap y lateral', 60000.00, true, 2),
('SRV006', 'cirugia ortopedica menor', 'reduccion cerrada', 850000.00, true, 3);

-- 8. Reglas de Negocio (Tarifas)
INSERT INTO tarifa_eps (codigo_eps, codigo_servicio, porcentaje_cobertura) VALUES
('EPS003', 'SRV004', 100.00),
('EPS003', 'SRV003', 70.00),
('EPS004', 'SRV006', 85.00),
('EPS001', 'SRV005', 90.00);

-- 9. Liquidaciones (Deben ir ANTES de las facturas porque las facturas referencian el código de liquidación)
INSERT INTO liquidacion (codigo, codigo_eps, fecha_inicio, fecha_fin, total_bruto, total_cobertura_eps, total_copago_paciente, estado, fecha_generacion) VALUES
('LIQ001', 'EPS003', '2024-05-01', '2024-05-31', 150000.00, 150000.00, 0.00, 'PAGADA', '2024-06-01 08:00:00'),
('LIQ002', 'EPS004', '2024-11-01', '2024-11-30', 350000.00, 245000.00, 105000.00, 'PENDIENTE', '2024-12-01 10:00:00');

-- 10. Citas (Estado 'APROBADA' en lugar de 'COMPLETADA' para cumplir con el check constraint)
INSERT INTO cita (codigo, estado, fecha, costo, id_especialidad, id_tipo, documento_paciente, documento_medico, codigo_hospital) VALUES
('CIT003', 'APROBADA', '2024-05-15 09:00:00', 150000.00, 3, 1, 'PAC003', 'MED003', 'HOS003'),
('CIT004', 'APROBADA', '2024-11-20 14:00:00', 350000.00, 3, 2, 'PAC004', 'MED003', 'HOS003'),
('CIT008', 'APROBADA', '2026-04-12 16:00:00', 150000.00, 3, 1, 'PAC003', 'MED003', 'HOS003');

-- 11. Facturas
INSERT INTO factura (codigo, fecha, estado, costo_total, descripcion, codigo_cita, codigo_eps, codigo_hospital, codigo_servicio, codigo_liquidacion) VALUES
('FAC003', '2024-05-15', true, 150000.00, 'consulta neurologica', 'CIT003', 'EPS003', 'HOS003', 'SRV004', 'LIQ001'),
('FAC004', '2024-11-20', true, 350000.00, 'resonancia magnetica', 'CIT004', 'EPS004', 'HOS003', 'SRV003', 'LIQ002'),
('FAC008', '2026-04-12', true, 150000.00, 'consulta seguimiento', 'CIT008', 'EPS003', 'HOS003', 'SRV004', NULL);