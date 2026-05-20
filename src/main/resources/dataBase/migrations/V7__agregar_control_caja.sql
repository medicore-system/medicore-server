-- Agregar campos de control de caja para los pacientes (B2C)
ALTER TABLE factura ADD COLUMN paciente_pago BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE factura ADD COLUMN valor_copago_pagado DECIMAL(12,2) DEFAULT 0.00;