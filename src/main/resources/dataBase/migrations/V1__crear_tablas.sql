create table tipo_documento (
    id     serial      primary key,
    nombre varchar(50) not null unique
);

create table especialidad (
    id     serial       primary key,
    nombre varchar(100) not null unique
);

create table tipo_cita (
    id     serial      primary key,
    nombre varchar(50) not null unique
);

create table departamento (
    id     serial       primary key,
    nombre varchar(100) not null unique
);

create table ciudad (
    codigo          varchar(50)  primary key,
    nombre          varchar(50)  not null,
    id_departamento int          not null,
    estado          boolean      not null default true,
    constraint fk_ciudad_departamento foreign key (id_departamento) references departamento(id)
);

create table eps (
    codigo varchar(50) primary key,
    nombre varchar(50) not null
);

create table hospital (
    codigo        varchar(50)  primary key,
    nombre        varchar(50)  not null,
    direccion     varchar(150) not null,
    telefono      varchar(20)  not null,
    estado        boolean      not null default true,
    codigo_ciudad varchar(50)  not null,
    constraint fk_hospital_ciudad foreign key (codigo_ciudad) references ciudad(codigo)
);

create table area_interna (
    codigo varchar(50) primary key,
    nombre varchar(50) not null
);

create table hospital_area_interna (
    codigo              varchar(50)  primary key,
    nombre              varchar(100),
    descripcion         varchar(250),
    codigo_hospital     varchar(50)  not null,
    codigo_area_interna varchar(50)  not null,
    constraint fk_hai_hospital     foreign key (codigo_hospital)     references hospital(codigo),
    constraint fk_hai_area_interna foreign key (codigo_area_interna) references area_interna(codigo)
);

create table usuario (
    documento        varchar(50)  primary key,
    nombre           varchar(50)  not null,
    apellido         varchar(50)  not null,
    correo           varchar(50)  not null,
    telefono         varchar(50)  not null,
    fecha_nacimiento date         ,
    contrasena       varchar(300)  ,
    estado           boolean      not null default true,
    rol              varchar(20)  not null default 'PACIENTE',
    codigo_eps       varchar(50)  not null,
    codigo_ciudad    varchar(50)  not null,
    constraint fk_usuario_eps    foreign key (codigo_eps)   references eps(codigo),
    constraint fk_usuario_ciudad foreign key (codigo_ciudad) references ciudad(codigo),
    constraint chk_usuario_rol   check (rol in ('PACIENTE', 'ADMIN'))
);

create table medico (
    documento       varchar(50) primary key,
    nombre          varchar(50) not null,
    apellido        varchar(50) not null,
    id_especialidad int         not null,
    telefono        varchar(20) not null,
    correo          varchar(50) not null,
    estado          boolean     not null default true,
    codigo_ciudad   varchar(50) not null,
    constraint fk_medico_especialidad foreign key (id_especialidad) references especialidad(id),
    constraint fk_medico_ciudad       foreign key (codigo_ciudad)   references ciudad(codigo)
);

create table historial_clinico (
    codigo             varchar(50)  primary key,
    fecha              date         not null,
    tipo               varchar(50)  not null,
    descripcion        varchar(200) not null,
    documento_paciente varchar(50)  not null,
    documento_medico   varchar(50)  not null,
    constraint fk_historial_paciente foreign key (documento_paciente) references usuario(documento),
    constraint fk_historial_medico   foreign key (documento_medico)   references medico(documento)
);

create table servicio (
    codigo           varchar(50)   primary key,
    nombre           varchar(50)   not null,
    procedimiento    varchar(250),
    resultados       varchar(250),
    costo            numeric(12,2) not null,
    codigo_historial varchar(50)   not null,
    constraint fk_servicio_historial foreign key (codigo_historial) references historial_clinico(codigo)
);

create table cita (
    codigo             varchar(50)   primary key,
    estado             varchar(20)   not null default 'PENDIENTE',
    fecha              timestamp     not null,
    costo              numeric(12,2) default 10000.00,
    id_tipo            int           not null,
    documento_paciente varchar(50)   not null,
    documento_medico   varchar(50)   not null,
    codigo_hospital    varchar(50)   not null,
    constraint fk_cita_tipo     foreign key (id_tipo)            references tipo_cita(id),
    constraint fk_cita_paciente foreign key (documento_paciente) references usuario(documento),
    constraint fk_cita_medico   foreign key (documento_medico)   references medico(documento),
    constraint fk_cita_hospital foreign key (codigo_hospital)    references hospital(codigo),
    constraint chk_cita_estado  check (estado in ('APROBADA', 'PENDIENTE', 'DENEGADA'))
);

create table notificacion_cita (
    codigo         varchar(50)  primary key,
    estado         boolean      not null default true,
    descripcion    varchar(50)  not null,
    correo_destino varchar(100) not null,
    codigo_cita    varchar(50)  not null,
    constraint fk_notificacion_cita foreign key (codigo_cita) references cita(codigo)
);

create table horario_medico (
    codigo serial primary key,
    horario varchar(50) not null unique,
    siguiente_horario varchar(50) not null
);

create table asignacion_medico (
    codigo serial primary key,
    fecha_inicio date not null,
    fecha_fin date not null,

    codigo_hospital varchar(50) not null,
    documento_medico varchar(50) not null,
    codigo_ciudad varchar(50) not null,
    codigo_horario integer not null,

    estado boolean not null default true,

    constraint fk_asignacion_hospital
        foreign key (codigo_hospital)
        references hospital(codigo),

    constraint fk_asignacion_medico
        foreign key (documento_medico)
        references medico(documento),

    constraint fk_asignacion_ciudad
        foreign key (codigo_ciudad)
        references ciudad(codigo),

    constraint fk_asignacion_horario
        foreign key (codigo_horario)
        references horario_medico(codigo)
);

create table factura (
    codigo          varchar(50)   primary key,
    fecha           date          not null,
    estado          boolean       not null default true,
    costo_total     numeric(12,2) not null,
    descripcion     varchar(250)  not null,
    codigo_cita     varchar(50)   not null,
    codigo_eps      varchar(50)   not null,
    codigo_hospital varchar(50)   not null,
    constraint fk_factura_cita     foreign key (codigo_cita)     references cita(codigo),
    constraint fk_factura_eps      foreign key (codigo_eps)      references eps(codigo),
    constraint fk_factura_hospital foreign key (codigo_hospital) references hospital(codigo)
);