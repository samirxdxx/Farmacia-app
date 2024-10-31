package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Proveedor;
import Modelo.Personal;
import Modelo.Compra;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class CompraDao implements Crud<Compra> {

    private static final String SQL_INSERT = "INSERT INTO Compra (idProveedor,"
            + " fecha_Compra, numero_Compra, subTotal, Igv, Total, Estado) VALUES (?,?,?,?,?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM Compra WHERE idCompra=?";
    private static final String SQL_UPDATE = "UPDATE Compra SET idProveedor=?, fecha_Compra=?,"
            + " numero_Compra=?, subTotal=?, Igv=?, Total=?, Estado=? WHERE idCompra=?";
    private static final String SQL_READ = "SELECT idCompra, idProveedor,"
            + " fecha_Compra, numero_Compra, subTotal, Igv, Total, Estado WHERE a.idCompra=?";
    private static final String SQL_READALL = "SELECT a.idCompra, a.idProveedor,"
            + " a.fecha_Compra, a.numero_compra, a.subTotal, a.Igv, a.Total, a.Estado, b.numeroIdentidad,"
            + " b.nombres  FROM compra a INNER JOIN view_Proveedor b ON a.idProveedor=b.idProveedor";
    private static final String SQL_CHANGE_STATUS = "UPDATE Compra SET Estado=? WHERE idCompra=?";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    public boolean cambiarEstado(Compra t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_CHANGE_STATUS);
            ps.setInt(1, t.getEstado());
            ps.setInt(2, t.getIdCompra());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(CompraDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;
    }

    @Override
    public List<Compra> Listar() throws Exception {
        List<Compra> compras = new ArrayList<>();
        Proveedor proveedor;
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                proveedor = new Proveedor(rs.getInt(2), "", "", 0, null,
                        rs.getString(9), rs.getString(10),
                        "", "", "");
                String dateString = rs.getString(3);
                LocalDate date = LocalDate.parse(dateString);

                compras.add(new Compra(rs.getInt(1), proveedor, date, rs.getString(4), rs.getDouble(5),
                        rs.getDouble(6), rs.getDouble(7), rs.getInt(8)));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + CompraDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return compras;
    }

    @Override
    public Compra Trae(Compra t) throws Exception {
        Compra compra = new Compra();
        Personal personal;
        Proveedor proveedor;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdCompra());

            rs = ps.executeQuery();

            while (rs.next()) {
                proveedor = new Proveedor(rs.getInt(2), "", "", 0, null,
                        rs.getString(10), rs.getString(11),
                        "", "", "");
                String dateString = rs.getString(3);
                LocalDate date = LocalDate.parse(dateString);

                compra = new Compra(rs.getInt(1), proveedor, date, rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getInt(8));
            }
        } catch (SQLException ex) {
            throw new Exception(CompraDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return compra;
    }

    @Override
    public boolean Crear(Compra t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getProveedor().getIdProveedor());
            ps.setString(2, t.getFecha_Compra().toString());
            ps.setString(3, t.getNumero_Compra());
            ps.setDouble(4, t.getSubTotal());
            ps.setDouble(5, t.getIgv());
            ps.setDouble(6, t.getTotal());
            ps.setInt(7, t.getEstado());

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
            throw new Exception(CompraDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Compra t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setInt(1, t.getProveedor().getIdProveedor());
            ps.setString(2, t.getFecha_Compra().toString());
            ps.setString(3, t.getNumero_Compra());
            ps.setDouble(4, t.getSubTotal());
            ps.setDouble(5, t.getIgv());
            ps.setDouble(6, t.getTotal());
            ps.setInt(7, t.getEstado());
            ps.setInt(8, t.getIdCompra());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(CompraDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Compra t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdCompra());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(CompraDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Compra t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
