-- ============================================================
--  HospitalDB · schema.sql · MySQL 8+
--  Spring Boot cargará este archivo automáticamente al iniciar
-- ============================================================

CREATE DATABASE IF NOT EXISTS hospitaldb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE hospitaldb;

-- ── roles ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS roles (
    id_rol      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol  VARCHAR(50)  NOT NULL UNIQUE,
    descripcion TEXT,
    permisos    JSON
);

INSERT IGNORE INTO roles (nombre_rol, descripcion) VALUES
    ('ADMIN',          'Administrador del sistema'),
    ('MEDICO',         'Médico / Doctor'),
    ('PACIENTE',       'Paciente registrado'),
    ('RECEPCIONISTA',  'Personal de recepción');

-- ── usuarios ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario       INT AUTO_INCREMENT PRIMARY KEY,
    id_rol           INT          NOT NULL,
    nombre           VARCHAR(80)  NOT NULL,
    apellidos        VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    password_hash    VARCHAR(255) NOT NULL,
    telefono         VARCHAR(20),
    fecha_nacimiento DATE,
    genero           ENUM('M','F','OTRO','PREFIERO_NO_DECIR'),
    activo           BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol)
        REFERENCES roles (id_rol)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ── especialidades ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS especialidades (
    id_especialidad   INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(100) NOT NULL UNIQUE,
    descripcion       TEXT,
    duracion_cita_min SMALLINT     NOT NULL DEFAULT 30,
    activa            BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ── doctores ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS doctores (
    id_doctor          INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario         INT          NOT NULL UNIQUE,
    id_especialidad    INT          NOT NULL,
    cedula_profesional VARCHAR(30)  UNIQUE,
    consultorio        VARCHAR(20),
    costo_consulta     DECIMAL(8,2),
    bio                TEXT,
    foto_url           VARCHAR(255),
    activo             BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_doctor_usuario      FOREIGN KEY (id_usuario)
        REFERENCES usuarios (id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_doctor_especialidad FOREIGN KEY (id_especialidad)
        REFERENCES especialidades (id_especialidad)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ── pacientes ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS pacientes (
    id_paciente           INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario            INT          NOT NULL UNIQUE,
    num_expediente        VARCHAR(20)  UNIQUE,
    tipo_sangre           ENUM('A_POS','A_NEG','B_POS','B_NEG',
                               'AB_POS','AB_NEG','O_POS','O_NEG'),
    alergias              TEXT,
    enfermedades_cronicas TEXT,
    seguro_medico         VARCHAR(100),
    contacto_emergencia   VARCHAR(150),
    tel_emergencia        VARCHAR(20),
    direccion             TEXT,
    CONSTRAINT fk_paciente_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuarios (id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ── horarios_disponibles ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS horarios_disponibles (
    id_horario    INT AUTO_INCREMENT PRIMARY KEY,
    id_doctor     INT      NOT NULL,
    dia_semana    TINYINT  NOT NULL COMMENT '0=Dom 1=Lun 2=Mar 3=Mie 4=Jue 5=Vie 6=Sab',
    hora_inicio   TIME     NOT NULL,
    hora_fin      TIME     NOT NULL,
    intervalo_min SMALLINT NOT NULL DEFAULT 30,
    activo        BOOLEAN  NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_horario_doctor FOREIGN KEY (id_doctor)
        REFERENCES doctores (id_doctor)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT chk_horario_valido CHECK (hora_fin > hora_inicio),
    CONSTRAINT chk_dia_semana     CHECK (dia_semana BETWEEN 0 AND 6)
);

-- ── citas ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS citas (
    id_cita           INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente       INT      NOT NULL,
    id_doctor         INT      NOT NULL,
    id_especialidad   INT      NOT NULL,
    fecha_cita        DATETIME NOT NULL,
    hora_fin_estimada DATETIME,
    estado            ENUM('PENDIENTE','CONFIRMADA','CANCELADA',
                          'COMPLETADA','NO_ASISTIO')
                      NOT NULL DEFAULT 'PENDIENTE',
    tipo_consulta     ENUM('PRESENCIAL','TELEMEDICINA','URGENCIA')
                      NOT NULL DEFAULT 'PRESENCIAL',
    motivo_consulta   TEXT,
    notas_internas    TEXT,
    confirmada_en     TIMESTAMP NULL,
    creada_en         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cita_paciente     FOREIGN KEY (id_paciente)
        REFERENCES pacientes (id_paciente)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_cita_doctor       FOREIGN KEY (id_doctor)
        REFERENCES doctores (id_doctor)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_cita_especialidad FOREIGN KEY (id_especialidad)
        REFERENCES especialidades (id_especialidad)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ── bloqueos_calendario ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS bloqueos_calendario (
    id_bloqueo   INT AUTO_INCREMENT PRIMARY KEY,
    id_doctor    INT          NOT NULL,
    fecha_inicio DATETIME     NOT NULL,
    fecha_fin    DATETIME     NOT NULL,
    motivo       VARCHAR(200),
    tipo         ENUM('VACACIONES','CAPACITACION','URGENCIA','FESTIVO'),
    CONSTRAINT fk_bloqueo_doctor FOREIGN KEY (id_doctor)
        REFERENCES doctores (id_doctor)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT chk_bloqueo_fechas CHECK (fecha_fin > fecha_inicio)
);

-- ── historial_citas ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS historial_citas (
    id_historial      INT AUTO_INCREMENT PRIMARY KEY,
    id_cita           INT         NOT NULL,
    id_usuario_accion INT         NOT NULL,
    estado_anterior   VARCHAR(30),
    estado_nuevo      VARCHAR(30) NOT NULL,
    comentario        TEXT,
    cambio_en         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_hist_cita    FOREIGN KEY (id_cita)
        REFERENCES citas (id_cita)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_hist_usuario FOREIGN KEY (id_usuario_accion)
        REFERENCES usuarios (id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ── Índices de rendimiento ───────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_citas_doctor_fecha
    ON citas (id_doctor, fecha_cita);

CREATE INDEX IF NOT EXISTS idx_citas_paciente
    ON citas (id_paciente, fecha_cita);

CREATE INDEX IF NOT EXISTS idx_citas_estado
    ON citas (estado, fecha_cita);

CREATE INDEX IF NOT EXISTS idx_horarios_doctor
    ON horarios_disponibles (id_doctor, dia_semana);

CREATE INDEX IF NOT EXISTS idx_bloqueos_doctor
    ON bloqueos_calendario (id_doctor, fecha_inicio, fecha_fin);
