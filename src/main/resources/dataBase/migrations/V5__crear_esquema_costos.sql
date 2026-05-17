-- Tabla para parametrizar la cobertura/acuerdo de una EPS sobre un Servicio
CREATE TABLE tarifa_eps (
    id SERIAL PRIMARY KEY,
    codigo_eps INT NOT NULL,
    codigo_servicio INT NOT NULL,
    porcentaje_cobertura DECIMAL(5,2) NOT NULL DEFAULT 100.00,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tarifa_eps FOREIGN KEY (codigo_eps) REFERENCES eps(codigo),
    CONSTRAINT fk_tarifa_servicio FOREIGN KEY (codigo_servicio) REFERENCES servicio(codigo),
    CONSTRAINT uq_eps_servicio UNIQUE (codigo_eps, codigo_servicio) -- Una EPS solo puede tener una tarifa activa por servicio
);

-- Tabla para agrupar los cobros a una EPS en un periodo (Liquidación/Corte)
CREATE TABLE liquidacion (
    id SERIAL PRIMARY KEY,
    codigo_eps INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    total_bruto DECIMAL(12,2) NOT NULL,
    total_cobertura_eps DECIMAL(12,2) NOT NULL,
    total_copago_paciente DECIMAL(12,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE', -- PENDIENTE, PAGADA, ANULADA
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_liquidacion_eps FOREIGN KEY (codigo_eps) REFERENCES eps(codigo)
);

-- le añadimos la relación a la liquidación desde facturas
-- para saber qué facturas entraron en qué liquidación de la EPS.
ALTER TABLE factura ADD COLUMN id_liquidacion INT NULL;
ALTER TABLE factura ADD CONSTRAINT fk_factura_liquidacion FOREIGN KEY (id_liquidacion) REFERENCES liquidacion(id);