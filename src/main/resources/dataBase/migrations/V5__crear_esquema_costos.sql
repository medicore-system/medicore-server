-- Secuencia y Tabla para Tarifas EPS
CREATE SEQUENCE seq_tarifa_eps START 1;
-- Tabla para parametrizar la cobertura/acuerdo de una EPS sobre un Servicio
CREATE TABLE tarifa_eps (
    codigo VARCHAR(50) PRIMARY KEY DEFAULT 'TAEPS' || nextval('seq_tarifa_eps'),
    codigo_eps varchar(50) NOT NULL,
    codigo_servicio varchar(50) NOT NULL,
    porcentaje_cobertura DECIMAL(5,2) NOT NULL DEFAULT 100.00,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tarifa_eps FOREIGN KEY (codigo_eps) REFERENCES eps(codigo),
    CONSTRAINT fk_tarifa_servicio FOREIGN KEY (codigo_servicio) REFERENCES servicio(codigo),
    CONSTRAINT uq_eps_servicio UNIQUE (codigo_eps, codigo_servicio) -- Una EPS solo puede tener una tarifa activa por servicio
);
-- Secuencia y Tabla para Liquidaciones
CREATE SEQUENCE seq_liquidacion START 1;
-- Tabla para agrupar los cobros a una EPS en un periodo (Liquidación/Corte)
CREATE TABLE liquidacion (
    codigo VARCHAR(50) PRIMARY KEY DEFAULT 'LIQ' || nextval('seq_liquidacion'),
    codigo_eps varchar(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    total_bruto DECIMAL(12,2) NOT NULL,
    total_cobertura_eps DECIMAL(12,2) NOT NULL,
    total_copago_paciente DECIMAL(12,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE', -- PENDIENTE, PAGADA, ANULADA, etc....
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_liquidacion_eps FOREIGN KEY (codigo_eps) REFERENCES eps(codigo)
);

-- 3. Vincular Facturas a la Liquidación usando el código alfanumérico
ALTER TABLE factura ADD COLUMN codigo_liquidacion VARCHAR(50) NULL;
ALTER TABLE factura ADD CONSTRAINT fk_factura_liquidacion FOREIGN KEY (codigo_liquidacion) REFERENCES liquidacion(codigo);