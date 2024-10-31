package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Cliente;
import Modelo.TipoIdentidad;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class ClienteDao implements Crud<Cliente> {

    private static final String SQL_INSERT = "INSERT INTO Cliente (idPersona, nombreComercial, representanteLegal) VALUES (?, ?,?)";
    private static final String SQL_DELETE = "DELETE FROM Cliente WHERE idCliente=?";
    private static final String SQL_UPDATE = "UPDATE Cliente SET nombreComercial=?, representanteLegal=? WHERE idCliente=?";
    private static final String SQL_READ = "SELECT a.idCliente, a.nombreComercial, a.representanteLegal,"
            + "b.idPersona, b.idTipoIdentidad, b.numeroIdentidad, b.Nombres, b.Direccion, b.Telefono,"
            + "b.Email FROM Cliente a INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE a.idCliente=?";
    private static final String SQL_READALL = "SELECT a.idCliente, a.nombreComercial, a.representanteLegal,"
            + "b.idPersona, b.idTipoIdentidad, b.numeroIdentidad, b.Nombres, b.Direccion, b.Telefono,"
            + "b.Email FROM Cliente a INNER JOIN Persona b ON a.idPersona=b.idPersona";
    private static final String SQL_EXISTS_NROIDENTIDAD = "SELECT COUNT(a.idCliente) FROM Cliente a "
            + "INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE b.numeroIdentidad=?";
    private static final String SQL_READ_NROIDENTIDAD = "SELECT a.idCliente, a.nombreComercial, a.representanteLegal,"
            + "b.idPersona, b.idTipoIdentidad, b.numeroIdentidad, b.Nombres, b.Direccion, b.Telefono,"
            + "b.Email FROM Cliente a INNER JOIN Persona b ON a.idPersona=b.idPersona WHERE b.numeroIdentidad=?";

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

    public Cliente traenroIdentidad(String nroIdentidad) throws Exception {
        Cliente cli = new Cliente();
        TipoIdentidad tipoIdentidad;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ_NROIDENTIDAD);
            ps.setString(1, nroIdentidad);

            rs = ps.executeQuery();

            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(5), "");
                cli = new Cliente(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4), tipoIdentidad,
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10));
            }
        } catch (SQLException ex) {
            throw new Exception(ClienteDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return cli;
    }

    @Override
    public List<Cliente> Listar() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        TipoIdentidad tipoIdentidad;
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(5), "");

                clientes.add(new Cliente(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4), tipoIdentidad,
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10)));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + ClienteDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return clientes;
    }

    @Override
    public Cliente Trae(Cliente t) throws Exception {
        Cliente cli = new Cliente();
        TipoIdentidad tipoIdentidad;

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdCliente());

            rs = ps.executeQuery();

            while (rs.next()) {
                tipoIdentidad = new TipoIdentidad(rs.getString(5), "");
                cli = new Cliente(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4), tipoIdentidad,
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10));
            }
        } catch (SQLException ex) {
            throw new Exception(ClienteDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return cli;
    }

    @Override
    public boolean Crear(Cliente t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getIdPersona());
            ps.setString(2, t.getNombreComercial());
            ps.setString(3, t.getRepresentanteLegal());

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
            throw new Exception(ClienteDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Cliente t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, t.getNombreComercial());
            ps.setString(2, t.getRepresentanteLegal());
            ps.setInt(3, t.getIdCliente());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(ClienteDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Cliente t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdCliente());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(ClienteDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Cliente t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
