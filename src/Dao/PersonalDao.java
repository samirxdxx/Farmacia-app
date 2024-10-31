package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Personal;
import Modelo.TipoIdentidad;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class PersonalDao implements Crud<Personal> {

    private static final String SQL_INSERT = "INSERT INTO Personal (idPersona, Cargo, Sueldo, Estado) VALUES (?,?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM Personal WHERE idPersonal=?";
    private static final String SQL_UPDATE = "UPDATE Personal SET Cargo=?, Sueldo=?, Estado=? WHERE idPersonal=?";
    private static final String SQL_CHANGE_STATUS = "UPDATE Personal SET Estado=? WHERE idPersonal=?";
    private static final String SQL_READ = "SELECT a.idPersonal, a.Cargo, a.Sueldo, a.Estado, b.idPersona,"
            + " b.idTipoIdentidad, b. numeroIdentidad, b.Nombres, b.Direccion, b.Telefono, b.Email FROM Personal"
            + " a INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE a.idPersonal=?";
    private static final String SQL_READALL = "SELECT a.idPersonal, a.Cargo, a.Sueldo, a.Estado, b.idPersona,"
            + " b.idTipoIdentidad, b. numeroIdentidad, b.Nombres, b.Direccion, b.Telefono, b.Email FROM Personal"
            + " a INNER JOIN Persona b ON a.idPersona=b.idPersona";
    private static final String SQL_EXISTS_NROIDENTIDAD = "SELECT COUNT(a.idPersonal) FROM Personal a "
            + "INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE b.numeroIdentidad=?";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    public boolean cambiarEstado(Personal t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_CHANGE_STATUS);
            ps.setInt(1, t.getEstado());
            ps.setInt(2, t.getIdPersonal());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(PersonalDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

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

    @Override
    public List<Personal> Listar() throws Exception {
        List<Personal> personals = new ArrayList<>();
        TipoIdentidad tipoIdentidad;
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(6), "");

                personals.add(new Personal(rs.getInt(1), rs.getString(2),
                        rs.getDouble(3), rs.getInt(4), rs.getInt(5), tipoIdentidad,
                        rs.getString(7), rs.getString(8), rs.getString(9),
                        rs.getString(10), rs.getString(11)));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + PersonalDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return personals;
    }

    @Override
    public Personal Trae(Personal t) throws Exception {
        Personal per = new Personal();
        TipoIdentidad tipoIdentidad;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdPersonal());

            rs = ps.executeQuery();

            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(6), "");
                per = new Personal(rs.getInt(1), rs.getString(2),
                        rs.getDouble(3), rs.getInt(4), rs.getInt(5), tipoIdentidad,
                        rs.getString(7), rs.getString(8), rs.getString(9),
                        rs.getString(10), rs.getString(11));
            }
        } catch (SQLException ex) {
            throw new Exception(PersonalDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return per;
    }

    @Override
    public boolean Crear(Personal t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getIdPersona());
            ps.setString(2, t.getCargo());
            ps.setDouble(3, t.getSueldo());
            ps.setInt(4, t.getEstado());

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
            throw new Exception(PersonalDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Personal t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, t.getCargo());
            ps.setDouble(2, t.getSueldo());
            ps.setInt(3, t.getEstado());
            ps.setInt(4, t.getIdPersonal());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(PersonalDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Personal t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdPersonal());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(PersonalDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Personal t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
