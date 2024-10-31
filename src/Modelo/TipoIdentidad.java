package Modelo;

/**
 *
 * @author eder
 */
public class TipoIdentidad {

    private String idTipoIdentidad;
    private String Nombre;

    public TipoIdentidad() {
    }

    public TipoIdentidad(String idTipoIdentidad, String Nombre) {
        this.idTipoIdentidad = idTipoIdentidad;
        this.Nombre = Nombre;
    }

    public String getIdTipoIdentidad() {
        return idTipoIdentidad;
    }

    public void setIdTipoIdentidad(String idTipoIdentidad) {
        this.idTipoIdentidad = idTipoIdentidad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    @Override
    public String toString() {
        return idTipoIdentidad + " " + Nombre;
    }

    @Override
    public int hashCode() {
        return super.hashCode(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TipoIdentidad) {
            TipoIdentidad c = (TipoIdentidad) o;
            return this.idTipoIdentidad.equals(c.getIdTipoIdentidad());
        }
        return false;
    }

}
