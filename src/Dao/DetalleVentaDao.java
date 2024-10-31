package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Cliente;
import Modelo.Personal;
import Modelo.DetalleVenta;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class DetalleVentaDao implements Crud<DetalleVenta> {

    private static final String SQL_INSERT = "INSERT INTO DetalleVenta (idVenta, idMedicamento,"
            + " Cantidad, Precio) VALUES (?,?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM DetalleVenta WHERE idDetalleVenta=?";
    private static final String SQL_UPDATE = "UPDATE DetalleVenta SET idVenta=?, idMedicamento=?,"
            + " Cantidad=?, Precio=? WHERE idDetalleVenta=?";
    private static final String SQL_READ = "SELECT idDetalleVenta, idVenta, idMedicamento,"
            + " Cantidad, Precio FROM DetalleVenta WHERE a.idDetalleVenta=?";
    private static final String SQL_READALL = "SELECT idDetalleVenta, idVenta, idMedicamento,"
            + " Cantidad, Precio FROM DetalleVenta";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    @Override
    public List<DetalleVenta> Listar() throws Exception {
        List<DetalleVenta> detalleVentas = new ArrayList<>();
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                detalleVentas.add(new DetalleVenta(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getDouble(5)));

            }

        } catch (SQLException ex) {
            throw new Exception("error : " + DetalleVentaDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return detalleVentas;
    }

    @Override
    public DetalleVenta Trae(DetalleVenta t) throws Exception {
        DetalleVenta detalleVenta = new DetalleVenta();
        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdDetalleVenta());

            rs = ps.executeQuery();

            while (rs.next()) {

                detalleVenta = new DetalleVenta(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getDouble(5));
            }
        } catch (SQLException ex) {
            throw new Exception(DetalleVentaDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return detalleVenta;
    }

    @Override
    public boolean Crear(DetalleVenta t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getIdVenta());
            ps.setInt(2, t.getIdMedicamento());
            ps.setDouble(3, t.getCantidad());
            ps.setDouble(4, t.getPrecio());

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
            throw new Exception(DetalleVentaDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(DetalleVenta t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setInt(1, t.getIdVenta());
            ps.setInt(2, t.getIdMedicamento());
            ps.setDouble(3, t.getCantidad());
            ps.setDouble(4, t.getPrecio());
            ps.setInt(5, t.getIdDetalleVenta());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(DetalleVentaDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(DetalleVenta t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdDetalleVenta());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(DetalleVentaDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(DetalleVenta t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
