package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Documento;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class DocumentoDao implements Crud<Documento> {

    private static final String SQL_INSERT = "INSERT INTO Documento (Codigo, Nombre) VALUES (?,?)";
    private static final String SQL_DELETE = "DELETE FROM Documento WHERE idDocumento=?";
    private static final String SQL_UPDATE = "UPDATE Documento SET Codigo=?, Nombre=? WHERE idDocumento=?";
    private static final String SQL_READ = "SELECT idDocumento, Codigo, Nombre FROM Documento WHERE idDocumento=?";
    private static final String SQL_READALL = "SELECT idDocumento, Codigo, Nombre FROM Documento";
    private static final String SQL_EXISTS_CODIGO = "SELECT COUNT(a.idDocumento) FROM Documento WHERE Codigo=?";


    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    public boolean existeCodigo(String nroIdentidad) throws Exception {
        int rtd = 0;

        try {
            ps = con.getCnn().prepareStatement(SQL_EXISTS_CODIGO);
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
    public List<Documento> Listar() throws Exception {
        List<Documento> clientes = new ArrayList<>();
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(new Documento(rs.getInt(1), rs.getString(2),
                        rs.getString(3)));

            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + DocumentoDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return clientes;
    }

    @Override
    public Documento Trae(Documento t) throws Exception {
        Documento cli = new Documento();

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdDocumento());

            rs = ps.executeQuery();

            while (rs.next()) {
                cli = new Documento(rs.getInt(1), rs.getString(2),
                        rs.getString(3));
            }
        } catch (SQLException ex) {
            throw new Exception(DocumentoDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return cli;
    }

    @Override
    public boolean Crear(Documento t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNombre());

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
            throw new Exception(DocumentoDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Documento t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNombre());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(DocumentoDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Documento t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdDocumento());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(DocumentoDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Documento t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
