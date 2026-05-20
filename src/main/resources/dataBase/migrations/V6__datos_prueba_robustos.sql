-- ===================================================================================
-- V6: CARGA MASIVA DE DATOS DE PRUEBA (ROBUSTOS Y REALISTAS PARA REPORTES BI)
-- ===================================================================================

-- 1. Nuevas Especialidades y Tipos
INSERT INTO especialidad (nombre) VALUES 
('neurologia'), ('dermatologia'), ('ortopedia'), ('pediatria');

INSERT INTO tipo_cita (nombre) VALUES 
('urgencia'), ('valoracion especialista');

INSERT INTO departamento (nombre) VALUES 
('cundinamarca'), ('valle del cauca'), ('atlantico');

INSERT INTO ciudad (codigo, nombre, id_departamento, estado) VALUES
('CIU003', 'bogota', 3, true),
('CIU004', 'cali', 4, true),
('CIU005', 'barranquilla', 5, true);

-- 2. Nuevas Aseguradoras (EPS)
INSERT INTO eps (codigo, nombre) VALUES
('EPS003', 'salud total'),
('EPS004', 'compensar'),
('EPS005', 'coomeva'),
('EPS006', 'nueva eps');

-- 3. Nuevos Hospitales y Sedes
INSERT INTO hospital (codigo, nombre, direccion, telefono, estado, codigo_ciudad) VALUES
('HOS003', 'fundacion santa fe', 'calle 119 #7-75', '6013333333', true, 'CIU003'),
('HOS004', 'clinica valle del lili', 'cra 98 #18-49', '6024444444', true, 'CIU004'),
('HOS005', 'clinica portoazul', 'cra 50 #90-85', '6055555555', true, 'CIU005');

INSERT INTO area_interna (codigo, nombre) VALUES
('AI003', 'cuidados intensivos (uci)'),
('AI004', 'quirofanos');

INSERT INTO hospital_area_interna (codigo, nombre, descripcion, codigo_hospital, codigo_area_interna) VALUES
('HAI003', 'uci santa fe', 'unidad de cuidado intensivo avanzado', 'HOS003', 'AI003'),
('HAI004', 'quirofanos valle del lili', 'salas de cirugia de alta complejidad', 'HOS004', 'AI004');

-- 4. Creación Masiva de Pacientes
INSERT INTO usuario (documento, nombre, apellido, correo, telefono, fecha_nacimiento, contrasena, estado, rol, codigo_eps, codigo_ciudad) VALUES
('PAC003', 'andres', 'vargas', 'andres@correo.com', '3100000001', '1985-08-20', '1234', true, 'PACIENTE', 'EPS003', 'CIU003'),
('PAC004', 'laura', 'gomez', 'laura@correo.com', '3100000002', '1992-11-15', '1234', true, 'PACIENTE', 'EPS004', 'CIU003'),
('PAC005', 'camilo', 'rojas', 'camilo@correo.com', '3100000003', '1978-02-10', '1234', true, 'PACIENTE', 'EPS005', 'CIU004'),
('PAC006', 'diana', 'castro', 'diana@correo.com', '3100000004', '2001-09-05', '1234', true, 'PACIENTE', 'EPS006', 'CIU005'),
('PAC007', 'sergio', 'ruiz', 'sergio@correo.com', '3100000005', '1995-04-22', '1234', true, 'PACIENTE', 'EPS001', 'CIU004');

-- 5. Creación Masiva de Médicos Especialistas
INSERT INTO medico (documento, nombre, apellido, id_especialidad, telefono, correo, estado, codigo_ciudad) VALUES
('MED003', 'jorge', 'martinez', 3, '3200000001', 'jorge.neuro@hospital.com', true, 'CIU003'),
('MED004', 'ana', 'torres', 4, '3200000002', 'ana.derma@hospital.com', true, 'CIU003'),
('MED005', 'luis', 'diaz', 5, '3200000003', 'luis.orto@hospital.com', true, 'CIU004'),
('MED006', 'claudia', 'pena', 6, '3200000004', 'claudia.pedia@hospital.com', true, 'CIU005');

-- 6. Catálogo de Servicios (Ajustado a la V3: id_tipo_servicio 1=Consulta, 2=Examen, 3=Procedimiento)
INSERT INTO servicio (codigo, nombre, descripcion, costo, estado, id_tipo_servicio) VALUES
('SRV003', 'resonancia magnetica', 'rm cerebral y espinal', 350000.00, true, 2),
('SRV004', 'consulta neurologica', 'valoracion por especialista en neurologia', 150000.00, true, 1),
('SRV005', 'radiografia de torax', 'rx ap y lateral', 60000.00, true, 2),
('SRV006', 'cirugia ortopedica menor', 'reduccion cerrada', 850000.00, true, 3),
('SRV007', 'consulta pediatrica', 'control de desarrollo y crecimiento', 110000.00, true, 1);

-- 7. Reglas de Negocio (Tarifas y Coberturas EPS de la V5)
INSERT INTO tarifa_eps (codigo_eps, codigo_servicio, porcentaje_cobertura) VALUES
('EPS003', 'SRV004', 100.00), -- Salud Total cubre 100% de la consulta neurológica
('EPS003', 'SRV003', 70.00),  -- Salud Total cubre 70% de RM (30% copago)
('EPS004', 'SRV006', 85.00),  -- Compensar cubre 85% de Cirugía Ortopédica
('EPS001', 'SRV005', 90.00),  -- Sura cubre 90% de Rayos X
('EPS006', 'SRV007', 100.00); -- Nueva EPS cubre 100% de pediatría

-- 8. Citas y Operación Diaria (Repartidas entre 2024, 2025 y 2026 para alimentar los gráficos)
INSERT INTO cita (codigo, estado, fecha, costo, id_especialidad, id_tipo, documento_paciente, documento_medico, codigo_hospital) VALUES
('CIT003', 'COMPLETADA', '2024-05-15 09:00:00', 150000.00, 3, 3, 'PAC003', 'MED003', 'HOS003'),
('CIT004', 'COMPLETADA', '2024-11-20 14:00:00', 350000.00, 3, 4, 'PAC004', 'MED003', 'HOS003'),
('CIT005', 'COMPLETADA', '2025-02-10 10:00:00', 850000.00, 5, 4, 'PAC005', 'MED005', 'HOS004'),
('CIT006', 'COMPLETADA', '2025-08-05 11:30:00', 110000.00, 6, 3, 'PAC006', 'MED006', 'HOS005'),
('CIT007', 'PENDIENTE',  '2026-06-15 08:00:00', 60000.00,  1, 4, 'PAC007', 'MED001', 'HOS001'),
('CIT008', 'COMPLETADA', '2026-04-12 16:00:00', 150000.00, 3, 3, 'PAC003', 'MED003', 'HOS003');

-- 9. Historial Clínico de las Citas Completadas
INSERT INTO historial_clinico (codigo, fecha, tipo, descripcion, documento_paciente, documento_medico) VALUES
('HC003', '2024-05-15', 'consulta neuro', 'migrañas constantes', 'PAC003', 'MED003'),
('HC004', '2024-11-20', 'examen', 'rm solicitada por sospecha', 'PAC004', 'MED003'),
('HC005', '2025-02-10', 'procedimiento', 'fractura de muñeca operada', 'PAC005', 'MED005'),
('HC006', '2025-08-05', 'control', 'niño sano, esquema de vacunas ok', 'PAC006', 'MED006'),
('HC008', '2026-04-12', 'seguimiento', 'dolor de cabeza disminuido', 'PAC003', 'MED003');

-- 10. Facturas Generadas
-- Unas quedarán huérfanas (sin liquidación) para que pruebes el UI, otras quedarán liquidadas.
INSERT INTO factura (codigo, fecha, estado, costo_total, descripcion, codigo_cita, codigo_eps, codigo_hospital, codigo_servicio, codigo_liquidacion) VALUES
-- Facturas del 2024 (Liquidadas)
('FAC003', '2024-05-15', true, 150000.00, 'factura por consulta neurologica', 'CIT003', 'EPS003', 'HOS003', 'SRV004', 'LIQ001'),
('FAC004', '2024-11-20', true, 350000.00, 'factura por resonancia magnetica', 'CIT004', 'EPS004', 'HOS003', 'SRV003', 'LIQ002'),
-- Facturas del 2025 (Liquidadas)
('FAC005', '2025-02-10', true, 850000.00, 'factura por cirugia menor', 'CIT005', 'EPS005', 'HOS004', 'SRV006', 'LIQ003'),
('FAC006', '2025-08-05', true, 110000.00, 'factura por consulta pediatrica', 'CIT006', 'EPS006', 'HOS005', 'SRV007', 'LIQ004'),
-- Facturas del 2026 (HUÉRFANAS - Para probar en la App de Escritorio)
('FAC008', '2026-04-12', true, 150000.00, 'factura de seguimiento neuro', 'CIT008', 'EPS003', 'HOS003', 'SRV004', NULL);

-- 11. Liquidaciones Históricas (Para poblar el Estado de Cartera y el Historial)
-- EPS003 - Salud Total
INSERT INTO liquidacion (codigo, codigo_eps, fecha_inicio, fecha_fin, total_bruto, total_cobertura_eps, total_copago_paciente, estado, fecha_generacion) VALUES
('LIQ001', 'EPS003', '2024-05-01', '2024-05-31', 150000.00, 150000.00, 0.00, 'PAGADA', '2024-06-01 08:00:00');

-- EPS004 - Compensar (Pendiente de pago, generará Estado de Cartera)
INSERT INTO liquidacion (codigo, codigo_eps, fecha_inicio, fecha_fin, total_bruto, total_cobertura_eps, total_copago_paciente, estado, fecha_generacion) VALUES
('LIQ002', 'EPS004', '2024-11-01', '2024-11-30', 350000.00, 245000.00, 105000.00, 'PENDIENTE', '2024-12-01 10:00:00');

-- EPS005 - Coomeva (Pagada)
INSERT INTO liquidacion (codigo, codigo_eps, fecha_inicio, fecha_fin, total_bruto, total_cobertura_eps, total_copago_paciente, estado, fecha_generacion) VALUES
('LIQ003', 'EPS005', '2025-02-01', '2025-02-28', 850000.00, 850000.00, 0.00, 'PAGADA', '2025-03-01 09:30:00');

-- EPS006 - Nueva EPS (Pendiente de pago, generará Estado de Cartera)
INSERT INTO liquidacion (codigo, codigo_eps, fecha_inicio, fecha_fin, total_bruto, total_cobertura_eps, total_copago_paciente, estado, fecha_generacion) VALUES
('LIQ004', 'EPS006', '2025-08-01', '2025-08-31', 110000.00, 110000.00, 0.00, 'PENDIENTE', '2025-09-01 14:15:00');