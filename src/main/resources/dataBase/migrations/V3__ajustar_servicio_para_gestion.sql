create table tipo_servicio (
                               id      serial       primary key,
                               nombre  varchar(50)  not null unique,
                               prefijo varchar(10)  not null unique
);

insert into tipo_servicio (nombre, prefijo) values
                                                ('Consulta', 'MED'),
                                                ('Examen', 'EXA'),
                                                ('Procedimiento', 'PROC');

alter table servicio
    add column descripcion varchar(250);

alter table servicio
    add column estado boolean not null default true;

alter table servicio
    add column id_tipo_servicio int;

update servicio
set descripcion = coalesce(procedimiento, resultados, nombre)
where descripcion is null;

update servicio
set id_tipo_servicio = (
    select id
    from tipo_servicio
    where nombre = 'Procedimiento'
)
where id_tipo_servicio is null;

alter table servicio
    alter column descripcion set not null;

alter table servicio
    alter column id_tipo_servicio set not null;

alter table servicio
    add constraint fk_servicio_tipo_servicio
        foreign key (id_tipo_servicio)
            references tipo_servicio(id);

alter table servicio
    alter column codigo_historial drop not null;