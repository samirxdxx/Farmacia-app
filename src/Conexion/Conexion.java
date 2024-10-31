package Conexion;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

   public static Conexion instance;
    private Connection cnn;

    private Conexion() {
        /*Conexion Keren*/
 /*Connection conectar = null;
        String usuario = "sa";
        String contraseña = "keren";
        String bd = "dbCallCenter";
        String ip = "localhost";
        String puerto = "1433";*/

        String url = "jdbc:sqlserver://localhost:1433;"
                + "database=dbFarma;"
                + "user=sa;"
                + "password=++**Gania2023;"
                + "integratedSecurity=false;"
                + "encrypt=false;"
                + "trustServerCertificate=false";

        try {
            cnn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Conexion getConexion() {
        if (instance == null) {
            instance = new Conexion();
        }

        return instance;

    }

    public Connection getCnn() {
        return cnn;
    }

    public void cerrarConexion() {
        instance = null;

    }
}
