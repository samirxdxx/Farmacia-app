package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Persona;
import Modelo.TipoIdentidad;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class PersonaDao implements Crud<Persona> {

    private static final String SQL_INSERT = "INSERT INTO Persona (idTipoIdentidad, numeroIdentidad,"
            + " Nombres, Direccion, Telefono, Email) VALUES (?,?,?,?,?,?)";

    private static final String SQL_DELETE = "DELETE FROM Persona WHERE idPersona=?";

    private static final String SQL_UPDATE = "UPDATE Persona SET idTipoIdentidad=?, numeroIdentidad=?,"
            + " Nombres=?, Direccion=?, Telefono=?, Email=? WHERE idPersona=?";

    private static final String SQL_READ = "SELECT idPersona, idTipoIdentidad, numeroIdentidad, Nombres,"
            + " Direccion, Telefono, Email FROM Persona WHERE idPersona=?";

    private static final String SQL_READALL = "SELECT idPersona, idTipoIdentidad, numeroIdentidad, Nombres,"
            + " Direccion, Telefono, Email FROM Persona";

    private static final String SQL_READ_NROIDENTIDAD = "SELECT idPersona, idTipoIdentidad, numeroIdentidad, Nombres,"
            + " Direccion, Telefono, Email FROM Persona WHERE numeroIdentidad=?";
    
    private static final String SQL_EXISTS_NROIDENTIDAD = "SELECT COUNT(idPersona) FROM Persona"
            + " WHERE numeroIdentidad=?";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    public boolean existeNroIdentidad(String nroIdentidad) throws Exception {
        int rtd = 0;

        try {
            ps = con.getCnn().prepareStatement(SQL_EXISTS_NROIDENTIDAD);
            ps.setString(1, nroIdentidad);
            rs = ps.executeQuery();
            if (rs.next()) {
                rtd = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new Exception("Erro al obtener " + ex.getMessage());
        }
        return rtd > 0;
    }

    public Persona traeNroIdentidad(String nroIdentidad) throws Exception {

        Persona persona = new Persona();
        TipoIdentidad tipoIdentidad;
        try {
            ps = con.getCnn().prepareStatement(SQL_READ_NROIDENTIDAD);
            ps.setString(1, nroIdentidad);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(2), "");

                persona = new Persona(rs.getInt(1), tipoIdentidad, rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));

            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener " + e.getMessage());
        }
        return persona;
    }

    @Override
    public List<Persona> Listar() throws Exception {
        List<Persona> personas = new ArrayList<>();
        TipoIdentidad tipoIdentidad;
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(2), "");

                personas.add(new Persona(rs.getInt(1), tipoIdentidad,
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7)));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + PersonaDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return personas;
    }

    @Override
    public Persona Trae(Persona t) throws Exception {
        Persona persona = new Persona();
        TipoIdentidad tipoIdentidad;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdPersona());

            rs = ps.executeQuery();

            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(2), "");
                persona = new Persona(rs.getInt(1), tipoIdentidad, rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
            }
        } catch (SQLException ex) {
            throw new Exception(PersonaDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return persona;
    }

    @Override
    public boolean Crear(Persona t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getTipoIdentidad().getIdTipoIdentidad());
            ps.setString(2, t.getNumeroIdentidad());
            ps.setString(3, t.getNombres());
            ps.setString(4, t.getDireccion());
            ps.setString(5, t.getTelefono());
            ps.setString(6, t.getEmail());

            if (ps.executeUpdate() > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.idGenerado = (int) generatedKeys.getLong(1);
                    }

                } catch (Exception e) {
                    throw new Exception("Erro al obtener indice " + e.getMessage());
                }

                return true;
            }
        } catch (SQLException ex) {
            throw new Exception(PersonaDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Persona t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, t.getTipoIdentidad().getIdTipoIdentidad());
            ps.setString(2, t.getNumeroIdentidad());
            ps.setString(3, t.getNombres());
            ps.setString(4, t.getDireccion());
            ps.setString(5, t.getTelefono());
            ps.setString(6, t.getEmail());
            ps.setInt(7, t.getIdPersona());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(PersonaDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Persona t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdPersona());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(PersonaDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Persona t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
