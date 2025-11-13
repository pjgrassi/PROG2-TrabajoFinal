USE clinica53;
select * from pacientes;

USE clinica53;
select * from historiaclinica;

USE clinica53;
SELECT 
    p.ID_Paciente,
    p.Nombre,
    p.Apellido,
    p.DNI,
    p.FechaNacimiento,
    h.NroHistoria
FROM 
    pacientes p
JOIN 
    HistoriaClinica h ON p.ID_HistoriaClinica = h.ID_HistoriaClinica
WHERE 
    p.Eliminado = FALSE;
