package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Cliente;
import Modelo.Personal;
import Modelo.Venta;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class VentaDao implements Crud<Venta> {

    private static final String SQL_INSERT = "INSERT INTO Venta (idCliente, idVendedor,"
            + " fecha_Venta, numero_Venta, subTotal, Igv, Total, Estado) VALUES (?,?,?,?,?,?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM Venta WHERE idVenta=?";
    private static final String SQL_UPDATE = "UPDATE Venta SET idCliente=?, idVendedor=?, fecha_Venta=?,"
            + " numero_Venta=?, subTotal=?, Igv=?, Total=?, Estado=? WHERE idVenta=?";
    private static final String SQL_READ = "SELECT idVenta, idCliente, idVendedor,"
            + " fecha_Venta, numero_Venta, subTotal, Igv, Total, Estado WHERE a.idVenta=?";
    private static final String SQL_READALL = "SELECT a.idVenta, a.idCliente, a.idVendedor,"
            + " a.fecha_Venta, a.numero_venta, a.subTotal, a.Igv, a.Total, a.Estado, b.numeroIdentidad,"
            + " b.nombres  FROM venta a INNER JOIN view_Cliente b ON a.idCliente=b.idCliente";
    private static final String SQL_CHANGE_STATUS = "UPDATE Venta SET Estado=? WHERE idVenta=?";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    public boolean cambiarEstado(Venta t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_CHANGE_STATUS);
            ps.setInt(1, t.getEstado());
            ps.setInt(2, t.getIdVenta());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(VentaDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;
    }

    @Override
    public List<Venta> Listar() throws Exception {
        List<Venta> ventas = new ArrayList<>();
        Cliente cliente;
        Personal personal;
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                cliente = new Cliente(rs.getInt(2), "", "", 0, null,
                        rs.getString(10), rs.getString(11),
                        "", "", "");
                personal = new Personal(rs.getInt(3), "", 0, 0);
                String dateString = rs.getString(4);
                LocalDate date = LocalDate.parse(dateString);

                ventas.add(new Venta(rs.getInt(1), cliente, date, rs.getInt(5), personal, rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getInt(9)));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + VentaDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return ventas;
    }

    @Override
    public Venta Trae(Venta t) throws Exception {
        Venta venta = new Venta();
        Personal personal;
        Cliente cliente;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdVenta());

            rs = ps.executeQuery();

            while (rs.next()) {
                cliente = new Cliente(rs.getInt(2), "", "", 0, null,
                        rs.getString(10), rs.getString(11),
                        "", "", "");
                personal = new Personal(rs.getInt(3), "", 0, 0);
                String dateString = rs.getString(4);
                LocalDate date = LocalDate.parse(dateString);

                venta = new Venta(rs.getInt(1), cliente, date, rs.getInt(5), personal, rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getInt(9));
            }
        } catch (SQLException ex) {
            throw new Exception(VentaDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return venta;
    }

    @Override
    public boolean Crear(Venta t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getCliente().getIdCliente());
            ps.setInt(2, t.getVendedor().getIdPersonal());
            ps.setString(3, t.getFecha_Venta().toString());
            ps.setInt(4, t.getNumero_Venta());
            ps.setDouble(5, t.getSubTotal());
            ps.setDouble(6, t.getIgv());
            ps.setDouble(7, t.getTotal());
            ps.setInt(8, t.getEstado());

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
            throw new Exception(VentaDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Venta t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setInt(1, t.getCliente().getIdCliente());
            ps.setInt(2, t.getVendedor().getIdPersonal());
            ps.setString(3, t.getFecha_Venta().toString());
            ps.setInt(4, t.getNumero_Venta());
            ps.setDouble(5, t.getSubTotal());
            ps.setDouble(6, t.getIgv());
            ps.setDouble(7, t.getTotal());
            ps.setInt(8, t.getEstado());
            ps.setInt(9, t.getIdVenta());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(VentaDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Venta t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdVenta());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(VentaDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Venta t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
