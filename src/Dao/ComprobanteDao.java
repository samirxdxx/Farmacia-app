package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Comprobante;
import Modelo.Documento;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class ComprobanteDao implements Crud<Comprobante> {

    private static final String SQL_INSERT = "INSERT INTO Comprobante (idDocumento, Descripcion, Serie,"
            + " Correlativo) VALUES (?,?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM Comprobante WHERE idComprobante=?";
    private static final String SQL_UPDATE = "UPDATE Comprobante SET idDocumento=?, Descripcion=?, Serie=?,"
            + " Correlativo=? WHERE idComprobante=?";
    private static final String SQL_READ = "SELECT a.idComprobante, a.idDocumento, a.Descripcion, a.Serie,"
            + " a.Correlativo, b.codigo FROM Comprobante a INNER JOIN Documento b ON a.idDocumento=b.idDocumento"
            + " WHERE a.idComprobante=?";
    private static final String SQL_READALL = "SELECT a.idComprobante, a.idDocumento, a.Descripcion, a.Serie,"
            + " a.Correlativo, b.codigo FROM Comprobante a INNER JOIN Documento b ON a.idDocumento=b.idDocumento";
    private static final String SQL_EXISTS_CODIGO = "SELECT COUNT(a.idComprobante) FROM Comprobante WHERE Codigo=?";
    private static final String SP_OBTENER_CORRELATIVO = "{call spObtenerCorrelativo(?)}";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    public int obtenerCorrelativo(int idComprobante) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SP_OBTENER_CORRELATIVO);
            ps.setInt(1, idComprobante);

            if (ps.execute()) {
                rs = ps.getResultSet();
                if (rs.next()) {
                    return rs.getInt("NuevoCorrelativo");
                }

            }

        } catch (SQLException e) {
            throw new Exception(ComprobanteDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }
        return 0;

    }

    @Override
    public List<Comprobante> Listar() throws Exception {
        List<Comprobante> comprobantes = new ArrayList<>();
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {

                comprobantes.add(new Comprobante(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getInt(5),
                        new Documento(rs.getInt(2), rs.getString(6), "")));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + ComprobanteDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return comprobantes;
    }

    @Override
    public Comprobante Trae(Comprobante t) throws Exception {
        Comprobante comprobante = new Comprobante();

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdComprobante());

            rs = ps.executeQuery();

            while (rs.next()) {
                comprobante = new Comprobante(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getInt(5),
                        new Documento(rs.getInt(2), rs.getString(6), ""));
            }
        } catch (SQLException ex) {
            throw new Exception(ComprobanteDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return comprobante;
    }

    @Override
    public boolean Crear(Comprobante t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getDocumento().getIdDocumento());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getSerie());
            ps.setInt(4, t.getCorrelativo());

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
            throw new Exception(ComprobanteDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Comprobante t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setInt(1, t.getDocumento().getIdDocumento());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getSerie());
            ps.setInt(4, t.getCorrelativo());
            ps.setInt(5, t.getIdComprobante());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(ComprobanteDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Comprobante t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdComprobante());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(ComprobanteDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Comprobante t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
