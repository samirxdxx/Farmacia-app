package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Cliente;
import Modelo.Personal;
import Modelo.DetalleCompra;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class DetalleCompraDao implements Crud<DetalleCompra> {

    private static final String SQL_INSERT = "INSERT INTO DetalleCompra (idCompra, idMedicamento,"
            + " Cantidad, Precio) VALUES (?,?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM DetalleCompra WHERE idDetalleCompra=?";
    private static final String SQL_UPDATE = "UPDATE DetalleCompra SET idCompra=?, idMedicamento=?,"
            + " Cantidad=?, Precio=? WHERE idDetalleCompra=?";
    private static final String SQL_READ = "SELECT idDetalleCompra, idCompra, idMedicamento,"
            + " Cantidad, Precio FROM DetalleCompra WHERE a.idDetalleCompra=?";
    private static final String SQL_READALL = "SELECT idDetalleCompra, idCompra, idMedicamento,"
            + " Cantidad, Precio FROM DetalleCompra";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    @Override
    public List<DetalleCompra> Listar() throws Exception {
        List<DetalleCompra> detalleCompras = new ArrayList<>();
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                detalleCompras.add(new DetalleCompra(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getDouble(5)));

            }

        } catch (SQLException ex) {
            throw new Exception("error : " + DetalleCompraDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return detalleCompras;
    }

    @Override
    public DetalleCompra Trae(DetalleCompra t) throws Exception {
        DetalleCompra detalleCompra = new DetalleCompra();
        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdDetalleCompra());

            rs = ps.executeQuery();

            while (rs.next()) {

                detalleCompra = new DetalleCompra(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getDouble(5));
            }
        } catch (SQLException ex) {
            throw new Exception(DetalleCompraDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return detalleCompra;
    }

    @Override
    public boolean Crear(DetalleCompra t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getIdCompra());
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
            throw new Exception(DetalleCompraDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(DetalleCompra t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setInt(1, t.getIdCompra());
            ps.setInt(2, t.getIdMedicamento());
            ps.setDouble(3, t.getCantidad());
            ps.setDouble(4, t.getPrecio());
            ps.setInt(5, t.getIdDetalleCompra());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(DetalleCompraDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(DetalleCompra t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdDetalleCompra());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(DetalleCompraDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(DetalleCompra t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
