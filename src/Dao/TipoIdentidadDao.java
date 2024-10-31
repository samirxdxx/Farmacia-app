package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.TipoIdentidad;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class TipoIdentidadDao implements Crud<TipoIdentidad> {

    private static final String SQL_INSERT = "INSERT INTO TipoIdentidad (idTipoIdentidad, Nombre) VALUES (?,?)";
    private static final String SQL_DELETE = "DELETE FROM TipoIdentidad WHERE idTipoIdentidad=?";
    private static final String SQL_UPDATE = "UPDATE TipoIdentidad SET Nombre=? WHERE idTipoIdentidad=?";
    private static final String SQL_READ = "SELECT * FROM TipoIdentidad WHERE idTipoIdentidad=?";
    private static final String SQL_READALL = "SELECT idTipoIdentidad, Nombre FROM TipoIdentidad";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    @Override
    public List<TipoIdentidad> Listar() throws Exception {
        List<TipoIdentidad> tipos = new ArrayList<>();
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipos.add(new TipoIdentidad(rs.getString(1), rs.getString(2)));

            }

        } catch (SQLException ex) {
            throw new Exception("error : " + TipoIdentidadDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return tipos;
    }

    @Override
    public TipoIdentidad Trae(TipoIdentidad t) throws Exception {
        TipoIdentidad tipo = new TipoIdentidad();

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setString(1, t.getIdTipoIdentidad());

            rs = ps.executeQuery();

            while (rs.next()) {
                tipo = new TipoIdentidad(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException ex) {
            throw new Exception(TipoIdentidadDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return tipo;
    }

    @Override
    public boolean Crear(TipoIdentidad t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, t.getIdTipoIdentidad());
            ps.setString(2, t.getNombre());

            if (ps.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            throw new Exception(TipoIdentidadDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(TipoIdentidad t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getIdTipoIdentidad());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(TipoIdentidadDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(TipoIdentidad t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setString(1, t.getIdTipoIdentidad());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(TipoIdentidadDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(TipoIdentidad t) throws Exception{
        return 0;
    }

}
