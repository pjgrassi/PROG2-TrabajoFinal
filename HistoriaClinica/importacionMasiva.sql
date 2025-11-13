USE clinica53;

-- INSERTAR LAS 10 HISTORIAS CLÍNICAS

INSERT INTO HistoriaClinica (NroHistoria, GrupoSanguineo, Antecedentes, MedicacionActual, Observaciones)
VALUES
('HC001', 'O_POS', 'Hipertensión Arterial', 'Losartán 50mg', 'Control de presión arterial bimensual.'),
('HC002', 'A_POS', 'Asma bronquial', 'Salbutamol SOS', 'Evitar alérgenos conocidos.'),
('HC003', 'B_NEG', 'Diabetes Mellitus Tipo 2', 'Metformina 850mg c/12hs', 'Requiere control de glucemia y dieta estricta.'),
('HC004', 'AB_POS', 'Ninguno de relevancia', 'Ninguna', 'Paciente sano, chequeo anual.'),
('HC005', 'O_NEG', 'Alergia a la penicilina', 'Loratadina ocasional', 'Antecedente de anafilaxia. Marcar en rojo.'),
('HC006', 'A_NEG', 'Hipotiroidismo', 'Levotiroxina 75mcg', 'Control de TSH semestral.'),
('HC007', 'B_POS', 'Cirugía de apendicitis (2015)', 'Ninguna', 'Buena recuperación postquirúrgica.'),
('HC008', 'A_POS', 'Miopía', 'Ninguna', 'Usa lentes de contacto.'),
('HC009', 'O_POS', 'Gastritis crónica', 'Omeprazol 20mg ayunas', 'Evitar comidas irritantes y alcohol.'),
('HC010', 'AB_NEG', 'Ansiedad generalizada', 'Sertralina 50mg', 'En seguimiento por psiquiatría.');

-- INSERTAR LOS 10 PACIENTES

INSERT INTO pacientes (Nombre, Apellido, DNI, FechaNacimiento, ID_HistoriaClinica)
VALUES
('Juan', 'Pérez', '30123456', '1980-05-15', 1),
('María', 'Gómez', '32987654', '1985-11-20', 2),
('Carlos', 'Rodríguez', '28456789', '1975-02-10', 3),
('Ana', 'López', '35123123', '1990-07-30', 4),
('Luis', 'Martínez', '38765432', '1995-01-05', 5),
('Laura', 'Fernández', '40123456', '1998-03-12', 6),
('Miguel', 'Sánchez', '25123987', '1970-09-18', 7),
('Sofía', 'Díaz', '42789012', '2000-12-01', 8),
('Diego', 'Torres', '33456789', '1988-06-25', 9),
('Elena', 'Ruiz', '36987123', '1992-04-14', 10);

