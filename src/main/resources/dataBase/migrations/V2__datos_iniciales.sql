insert into tipo_documento (nombre) values ('cedula de ciudadania'), ('tarjeta de identidad');
insert into especialidad (nombre) values ('medicina general'), ('cardiologia');
insert into tipo_cita (nombre) values ('primera vez'), ('control');
insert into departamento (nombre) values ('caldas'), ('antioquia');

insert into ciudad (codigo, nombre, id_departamento, estado) values
    ('CIU001', 'manizales', 1, true),
    ('CIU002', 'medellin',  2, true);

insert into eps (codigo, nombre) values
    ('EPS001', 'sura'),
    ('EPS002', 'sanitas');

insert into hospital (codigo, nombre, direccion, telefono, estado, codigo_ciudad) values
    ('HOS001', 'hospital santa sofia',  'cra 23#45-10', '8761234', true, 'CIU001'),
    ('HOS002', 'clinica las americas',  'cll 50#2-80',  '3042222', true, 'CIU002');

insert into area_interna (codigo, nombre) values
    ('AI001', 'urgencias'),
    ('AI002', 'pediatria');

insert into hospital_area_interna (codigo, nombre, descripcion, codigo_hospital, codigo_area_interna) values
    ('HAI001', 'urgencias santa sofia',  'atencion de urgencias 24h', 'HOS001', 'AI001'),
    ('HAI002', 'pediatria las americas', 'atencion pediatrica',       'HOS002', 'AI002');

insert into usuario (documento, nombre, apellido, correo, telefono, fecha_nacimiento, contrasena, estado, rol, codigo_eps, codigo_ciudad) values
    ('PAC001', 'juan',  'garcia',  'juan@correo.com',  '3033333333', '1990-05-12', '1234', true, 'PACIENTE', 'EPS001', 'CIU001'),
    ('PAC002', 'maria', 'herrera', 'maria@correo.com', '3044444444', '1985-08-23', '1234', true, 'PACIENTE', 'EPS002', 'CIU002');

insert into medico (documento, nombre, apellido, id_especialidad, telefono, correo, estado, codigo_ciudad) values
    ('MED001', 'carlos',   'ramirez', 1, '3011111111', 'carlos.ramirez@hospital.com',  true, 'CIU001'),
    ('MED002', 'patricia', 'lopez',   2, '3022222222', 'patricia.lopez@hospital.com',  true, 'CIU002');

insert into historial_clinico (codigo, fecha, tipo, descripcion, documento_paciente, documento_medico) values
    ('HC001', '2024-01-15', 'consulta general', 'paciente con fiebre y malestar general', 'PAC001', 'MED001'),
    ('HC002', '2024-03-10', 'control cardiaco', 'revision rutinaria de presion arterial',  'PAC002', 'MED002');

insert into servicio (codigo, nombre, procedimiento, resultados, costo, codigo_historial) values
    ('SRV001', 'electrocardiograma', 'toma de ecg en reposo', 'ritmo sinusal normal',          85000.00, 'HC001'),
    ('SRV002', 'hemograma completo', 'extraccion de muestra', 'valores dentro del rango normal', 45000.00, 'HC002');

insert into cita (codigo, estado, fecha, costo, id_tipo, documento_paciente, documento_medico, codigo_hospital) values
    ('CIT001', 'PENDIENTE', '2024-06-01 08:00:00', 50000.00, 1, 'PAC001', 'MED001', 'HOS001'),
    ('CIT002', 'PENDIENTE', '2024-06-02 10:30:00', 70000.00, 2, 'PAC002', 'MED002', 'HOS002');

insert into notificacion_cita (codigo, estado, descripcion, correo_destino, codigo_cita) values
    ('NOT001', true, 'recordatorio de cita medica',   'juan@correo.com',  'CIT001'),
    ('NOT002', true, 'confirmacion de cita asignada', 'maria@correo.com', 'CIT002');

insert into asignacion_medico (codigo, fecha, estado, documento_medico, codigo_hai) values
    ('ASG001', '2024-01-01', true, 'MED001', 'HAI001'),
    ('ASG002', '2024-01-01', true, 'MED002', 'HAI002');

insert into horario_medico (codigo, dia_semana, hora_inicio, hora_fin, estado, codigo_asignacion) values
    ('HOR001', 'lunes',     '07:00', '13:00', true, 'ASG001'),
    ('HOR002', 'miercoles', '14:00', '20:00', true, 'ASG002');

insert into factura (codigo, fecha, estado, costo_total, descripcion, codigo_cita, codigo_eps, codigo_hospital) values
    ('FAC001', '2024-06-01', true, 50000.00, 'factura consulta medicina general', 'CIT001', 'EPS001', 'HOS001'),
    ('FAC002', '2024-06-02', true, 70000.00, 'factura control cardiologico',      'CIT002', 'EPS002', 'HOS002');