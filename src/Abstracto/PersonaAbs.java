package Abstracto;

import Modelo.TipoIdentidad;

/**
 *
 * @author eder
 */
public abstract class PersonaAbs {
    private int idPersona;
    private TipoIdentidad tipoIdentidad;
    private String numeroIdentidad;
    private String Nombres;
    private String Direccion;
    private String Telefono;
    private String Email;

    public PersonaAbs() {
        
    }

    public PersonaAbs(int idPersona, TipoIdentidad tipoIdentidad, String numeroIdentidad, String Nombres, String Direccion, String Telefono, String Email) {
        this.idPersona = idPersona;
        this.tipoIdentidad = tipoIdentidad;
        this.numeroIdentidad = numeroIdentidad;
        this.Nombres = Nombres;
        this.Direccion = Direccion;
        this.Telefono = Telefono;
        this.Email = Email;
    }



    public PersonaAbs(PersonaAbs p){
        this.idPersona = p.idPersona;
        this.tipoIdentidad = p.tipoIdentidad;
        this.numeroIdentidad = p.numeroIdentidad;
        this.Nombres = p.Nombres;
        this.Direccion = p.Direccion;
        this.Telefono = p.Telefono;
        this.Email = p.Email;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public TipoIdentidad getTipoIdentidad() {
        return tipoIdentidad;
    }

    public void setTipoIdentidad(TipoIdentidad tipoIdentidad) {
        this.tipoIdentidad = tipoIdentidad;
    }

    public String getNumeroIdentidad() {
        return numeroIdentidad;
    }

    public void setNumeroIdentidad(String numeroIdentidad) {
        this.numeroIdentidad = numeroIdentidad;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    @Override
    public String toString() {
        return "PersonaAbs{" + "idPersona=" + idPersona + ", tipoIdentidad=" + tipoIdentidad + ", numeroIdentidad=" + numeroIdentidad + ", Nombres=" + Nombres + ", Direccion=" + Direccion + ", Telefono=" + Telefono + ", Email=" + Email + '}';
    }
    
  
    
    
}
