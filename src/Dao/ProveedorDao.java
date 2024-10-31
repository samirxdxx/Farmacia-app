package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Proveedor;
import Modelo.TipoIdentidad;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class ProveedorDao implements Crud<Proveedor> {

    private static final String SQL_INSERT = "INSERT INTO Proveedor (idPersona, representanteLegal, Marca) VALUES (?, ?,?)";
    private static final String SQL_DELETE = "DELETE FROM Proveedor WHERE idProveedor=?";
    private static final String SQL_UPDATE = "UPDATE Proveedor SET representanteLegal=?, Marca=? WHERE idProveedor=?";
    private static final String SQL_READ = "SELECT a.idProveedor, a.representanteLegal, a.Marca, "
            + "b.idPersona, b.idTipoIdentidad, b.numeroIdentidad, b.Nombres, b.Direccion, b.Telefono,"
            + "b.Email FROM Proveedor a INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE a.idProveedor=?";
    private static final String SQL_READALL = "SELECT a.idProveedor, a.representanteLegal, a.Marca, "
            + "b.idPersona, b.idTipoIdentidad, b.numeroIdentidad, b.Nombres, b.Direccion, b.Telefono,"
            + "b.Email FROM Proveedor a INNER JOIN Persona b ON a.idPersona=b.idPersona";
    private static final String SQL_EXISTS_NROIDENTIDAD = "SELECT COUNT(a.idProveedor) FROM Proveedor a "
            + "INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE b.numeroIdentidad=?";
    private static final String SQL_READ_NROIDENTIDAD = "SELECT a.idProveedor, a.representanteLegal, a.Marca, "
            + "b.idPersona, b.idTipoIdentidad, b.numeroIdentidad, b.Nombres, b.Direccion, b.Telefono,"
            + "b.Email FROM Proveedor a INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE b.numeroIdentidad=?";

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

    public Proveedor traenroIdentidad(String nroIdentidad) throws Exception {
        Proveedor cli = new Proveedor();
        TipoIdentidad tipoIdentidad;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ_NROIDENTIDAD);
            ps.setString(1, nroIdentidad);

            rs = ps.executeQuery();

            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(5), "");
                cli = new Proveedor(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4), tipoIdentidad,
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10));
            }
        } catch (SQLException ex) {
            throw new Exception(ProveedorDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return cli;
    }

    @Override
    public List<Proveedor> Listar() throws Exception {
        List<Proveedor> proveedors = new ArrayList<>();
        TipoIdentidad tipoIdentidad;
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(5), "");

                proveedors.add(new Proveedor(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4), tipoIdentidad,
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10)));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + ProveedorDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return proveedors;
    }

    @Override
    public Proveedor Trae(Proveedor t) throws Exception {
        Proveedor cli = new Proveedor();
        TipoIdentidad tipoIdentidad;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdProveedor());

            rs = ps.executeQuery();

            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(5), "");
                cli = new Proveedor(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4), tipoIdentidad,
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10));
            }
        } catch (SQLException ex) {
            throw new Exception(ProveedorDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return cli;
    }

    @Override
    public boolean Crear(Proveedor t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getIdPersona());
            ps.setString(2, t.getRepresentanteLegal());
            ps.setString(3, t.getMarca());

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
            throw new Exception(ProveedorDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Proveedor t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);

            ps.setString(1, t.getRepresentanteLegal());
            ps.setString(2, t.getMarca());
            ps.setInt(3, t.getIdProveedor());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(ProveedorDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Proveedor t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdProveedor());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(ProveedorDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Proveedor t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
