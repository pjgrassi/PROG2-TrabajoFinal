
-- Eliminamos la base de datos en caso de que exista, y luego creamos una base de datos nueva.

DROP DATABASE IF EXISTS clinica53;
CREATE DATABASE clinica53;

-- Indicamos que si existe la tabla HistoriaClinica, se elimine, y se cree una tabla nueva.

USE clinica53;
DROP TABLE IF EXISTS HistoriaClinica;
CREATE TABLE HistoriaClinica (
    ID_HistoriaClinica BIGINT AUTO_INCREMENT PRIMARY KEY,
    Eliminado BOOLEAN DEFAULT FALSE, -- Baja logica 
    NroHistoria VARCHAR(20) UNIQUE,
    GrupoSanguineo ENUM('A_POS', 'A_NEG', 'B_POS', 'B_NEG', 'AB_POS', 'AB_NEG', 'O_POS', 'O_NEG'),
    Antecedentes TEXT,
    MedicacionActual TEXT,
    Observaciones TEXT
);

-- Indicamos que si existe la tabla pacientes, se elimine, y se cree una tabla nueva.

USE clinica53;
DROP TABLE IF EXISTS pacientes;
CREATE TABLE pacientes (
    ID_Paciente BIGINT AUTO_INCREMENT PRIMARY KEY,
    Eliminado BOOLEAN DEFAULT FALSE, -- Baja logica 
    Nombre VARCHAR(80) NOT NULL,
    Apellido VARCHAR(80) NOT NULL,
    DNI VARCHAR(15) NOT NULL UNIQUE,
    FechaNacimiento DATE,
    ID_HistoriaClinica BIGINT UNIQUE, -- La referencia unica garantiza la relación uno a uno
     FOREIGN KEY (ID_HistoriaClinica) REFERENCES HistoriaClinica(ID_HistoriaClinica)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);