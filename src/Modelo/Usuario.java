package Modelo;

/**
 *
 * @author eder
 */
public class Usuario extends Personal {

    private int idUsuario;
    private String Nombre;
    private String Clave;
    private int Estado;

    public Usuario() {

    }

    public Usuario(int idUsuario, String Nombre, String Clave, int Estado, int idPersonal, String Cargo, double Sueldo, int EstadoP) {
        super(idPersonal, Cargo, Sueldo, EstadoP);
        this.idUsuario = idUsuario;
        this.Nombre = Nombre;
        this.Clave = Clave;
        this.Estado = Estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String Clave) {
        this.Clave = Clave;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    

}
