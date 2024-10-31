package Dao;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Interface.Crud;
import Modelo.Medicamento;
import Modelo.TipoIdentidad;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author eder
 */
public class MedicamentoDao implements Crud<Medicamento> {

    private static final String SQL_INSERT = "INSERT INTO Medicamento (Nombre, Descripcion,"
            + " Laboratorio, Precio, fechaCaducidad, Stock, Estado) VALUES (?,?,?,?,?,?,?)";

    private static final String SQL_DELETE = "DELETE FROM Medicamento WHERE idMedicamento=?";

    private static final String SQL_UPDATE = "UPDATE Medicamento SET Nombre=?, Descripcion=?,"
            + " Laboratorio=?, Precio=?, fechaCaducidad=?, Stock=?, Estado=? WHERE idMedicamento=?";

    private static final String SQL_READ = "SELECT idMedicamento, Nombre, Descripcion,"
            + " Laboratorio, Precio, fechaCaducidad, Stock, Estado FROM Medicamento WHERE idMedicamento=?";

    private static final String SQL_READALL = "SELECT idMedicamento, Nombre, Descripcion,"
            + " Laboratorio, Precio, fechaCaducidad, Stock, Estado FROM Medicamento";

    private static final String SQL_READ_ALL_LIKE = "SELECT idMedicamento, Nombre, Descripcion,"
            + " Laboratorio, Precio, fechaCaducidad, Stock, Estado FROM Medicamento WHERE Nombre LIKE '%?%' OR Descripcion LIKE '%?%'";

    private static final String SQL_DISCOUNT_STOCK = "UPDATE Medicamento SET Stock=Stock-? WHERE idMedicamento=?";
    private static final String SQL_INCREMENT_STOCK = "UPDATE Medicamento SET Stock=Stock+? WHERE idMedicamento=?";

    private static final Conexion con = Conexion.getConexion();

    PreparedStatement ps;
    ResultSet rs;
    private int idGenerado;

    public boolean actualizaStock(double cntDescont, int idMedicamento) throws Exception {
        try {
            if(cntDescont>0){
                ps = con.getCnn().prepareStatement(SQL_DISCOUNT_STOCK);
            }else{
                cntDescont = cntDescont*-1;
                ps = con.getCnn().prepareStatement(SQL_INCREMENT_STOCK);
            }
            System.out.println(cntDescont);
            ps.setDouble(1, cntDescont);
            ps.setInt(2, idMedicamento);

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(MedicamentoDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    public List<Medicamento> listarLike(String like) throws Exception {
        List<Medicamento> medicamentos = new ArrayList<>();
        try {

            ps = con.getCnn().prepareStatement(SQL_READ_ALL_LIKE);
            rs = ps.executeQuery();
            while (rs.next()) {
                String dateString = rs.getString(6);
                LocalDate date = LocalDate.parse(dateString);
                medicamentos.add(new Medicamento(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getDouble(5), date, rs.getDouble(7), rs.getInt(8)));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + MedicamentoDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return medicamentos;
    }

    @Override
    public List<Medicamento> Listar() throws Exception {
        List<Medicamento> medicamentos = new ArrayList<>();
        try {

            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                String dateString = rs.getString(6);
                LocalDate date = LocalDate.parse(dateString);

                medicamentos.add(new Medicamento(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDouble(5), date, rs.getDouble(7), rs.getInt(8)));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
            throw new Exception("error : " + MedicamentoDao.class.getName() + " " + Level.SEVERE + " " + ex.toString());
        } finally {
            con.cerrarConexion();
        }

        return medicamentos;
    }

    @Override
    public Medicamento Trae(Medicamento t) throws Exception {
        Medicamento medicamento = new Medicamento();

        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, t.getIdMedicamento());

            rs = ps.executeQuery();

            while (rs.next()) {
                String dateString = rs.getString(6);
                LocalDate date = LocalDate.parse(dateString);
                medicamento = new Medicamento(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getDouble(5), date, rs.getDouble(7), rs.getInt(8));
            }
        } catch (SQLException ex) {
            throw new Exception(MedicamentoDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return medicamento;
    }

    @Override
    public boolean Crear(Medicamento t) throws Exception {
        //int idMedicamento, String Nombre, String Descripcion, String Laboratorio, double Precio,
        //LocalDate fechaCaducidad, double Stock, int Estado)
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getLaboratorio());
            ps.setDouble(4, t.getPrecio());
            ps.setString(5, t.getFechaCaducidad().toString());
            ps.setDouble(6, t.getStock());
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
            throw new Exception(MedicamentoDao.class.getName() + " " + Level.SEVERE + " " + ex.getMessage());
        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public boolean Modificar(Medicamento t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getLaboratorio());
            ps.setDouble(4, t.getPrecio());
            ps.setString(5, t.getFechaCaducidad().toString());
            ps.setDouble(6, t.getStock());
            ps.setInt(7, t.getEstado());
            ps.setInt(8, t.getIdMedicamento());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(MedicamentoDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());
        } finally {
            con.cerrarConexion();

        }

        return false;

    }

    @Override
    public boolean Eliminar(Medicamento t) throws Exception {
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, t.getIdMedicamento());

            if (ps.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new Exception(MedicamentoDao.class.getName() + " " + Level.SEVERE + " " + e.getMessage());

        } finally {
            con.cerrarConexion();
        }

        return false;

    }

    @Override
    public int creaId(Medicamento t) throws Exception {
        try {
            this.Crear(t);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return this.idGenerado;
    }

}
