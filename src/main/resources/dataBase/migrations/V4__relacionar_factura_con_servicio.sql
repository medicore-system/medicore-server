alter table factura
    add column codigo_servicio varchar(50);

update factura
set codigo_servicio = 'SRV001'
where codigo = 'FAC001'
  and exists (select 1 from servicio where codigo = 'SRV001');

update factura
set codigo_servicio = 'SRV002'
where codigo = 'FAC002'
  and exists (select 1 from servicio where codigo = 'SRV002');

alter table factura
    add constraint fk_factura_servicio
        foreign key (codigo_servicio)
            references servicio(codigo);