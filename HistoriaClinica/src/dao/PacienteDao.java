package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import config.DatabaseConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import modelo.GrupoSanguineo;
import modelo.HistoriaClinica;
import modelo.Paciente;

public class PacienteDao implements GenericDao<Paciente>{

    @Override
    public void insertar(Paciente entidad) throws Exception {
        String sql = "INSERT INTO pacientes (nombre, apellido, dni, FechaNacimiento, ID_HistoriaClinica, eliminado) VALUES (?, ?, ?, ?, ?, false)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entidad.getNombre());
            stmt.setString(2, entidad.getApellido());
            stmt.setString(3, entidad.getDni());
            stmt.setDate(4, Date.valueOf(entidad.getFechaNacimiento()));
            stmt.setInt(5, entidad.getHistoriaClinica().getId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) entidad.setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error en transacción al insertar paciente: " + e.getMessage(), e);
        }

    }

    @Override
    public void actualizar(Paciente entidad) throws Exception {
        String sql = "UPDATE pacientes SET nombre=?, apellido=?, dni=?, FechaNacimiento=? WHERE ID_Paciente=? AND eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entidad.getNombre());
            stmt.setString(2, entidad.getApellido());
            stmt.setString(3, entidad.getDni());
            stmt.setDate(4, Date.valueOf(entidad.getFechaNacimiento()));
            stmt.setInt(5, entidad.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
        }

    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "UPDATE pacientes SET eliminado=true WHERE ID_Paciente=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
        }
    }

    @Override
    public void recuperar(int id) throws Exception {
        String sql = "UPDATE pacientes SET eliminado=false WHERE ID_Paciente=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al recuperar paciente: " + e.getMessage());
        }
    }

    @Override
    public Paciente getById(int id) throws Exception {
        String sql = "SELECT * FROM pacientes WHERE ID_Paciente = ? AND eliminado = false";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Paciente(
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("dni"),
                    rs.getDate("FechaNacimiento").toLocalDate(),
                    null, // HistoriaClinica se puede cargar aparte si lo necesitás
                    rs.getInt("ID_Paciente"),
                    rs.getBoolean("eliminado")
                );
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener paciente por ID: " + e.getMessage(), e);
        }
    }

@Override
public Paciente getByIdEliminado(int id) throws Exception {
    String sql = "SELECT * FROM pacientes WHERE ID_Paciente = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Paciente p = new Paciente();
            p.setId(rs.getInt("ID_Paciente"));
            p.setNombre(rs.getString("nombre"));
            p.setApellido(rs.getString("apellido"));
            p.setDni(rs.getString("dni"));
            p.setFechaNacimiento(rs.getDate("FechaNacimiento").toLocalDate());
            p.setHistoriaClinica(null);
            p.setEliminado(rs.getBoolean("eliminado"));
            return p;
        }

        return null;

    } catch (SQLException e) {
        throw new Exception("Error al obtener paciente eliminado por ID: " + e.getMessage(), e);
    }
}


    
    public Paciente getByDni(String dni) throws Exception {
        String sql = """
            SELECT p.ID_Paciente, p.Nombre, p.Apellido, p.DNI, p.FechaNacimiento, p.Eliminado,
                h.ID_HistoriaClinica, h.NroHistoria, h.GrupoSanguineo, h.Antecedentes, h.MedicacionActual, h.Observaciones, h.Eliminado AS HC_Eliminado
            FROM pacientes p
            JOIN HistoriaClinica h ON p.ID_HistoriaClinica = h.ID_HistoriaClinica
            WHERE p.DNI = ? AND p.Eliminado = false
            """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HistoriaClinica historia = new HistoriaClinica(
                    rs.getString("NroHistoria"),
                    GrupoSanguineo.fromCodigo(rs.getInt("GrupoSanguineo")),
                    rs.getString("Antecedentes"),
                    rs.getString("MedicacionActual"),
                    rs.getString("Observaciones"),
                    rs.getInt("ID_HistoriaClinica"),
                    rs.getBoolean("HC_Eliminado")
                );

                return new Paciente(
                    rs.getString("Nombre"),
                    rs.getString("Apellido"),
                    rs.getString("DNI"),
                    rs.getDate("FechaNacimiento").toLocalDate(),
                    historia,
                    rs.getInt("ID_Paciente"),
                    rs.getBoolean("Eliminado")
                );
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener paciente por DNI: " + e.getMessage(), e);
        }
    }



    @Override
    public List<Paciente> getAll() throws Exception {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE eliminado=false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while(rs.next()){
               Paciente paciente = new Paciente();
               paciente.setId(rs.getInt("ID_Paciente"));
               paciente.setNombre(rs.getString("nombre"));
               paciente.setApellido(rs.getString("apellido"));
               paciente.setDni(rs.getString("dni"));
               paciente.setFechaNacimiento(rs.getDate("FechaNacimiento").toLocalDate());

               
               lista.add(paciente);
           }
        } catch (SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        }
        return lista;

    }
    
}
