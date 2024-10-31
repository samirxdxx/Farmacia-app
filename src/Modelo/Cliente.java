package Modelo;

import Abstracto.PersonaAbs;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author eder
 */
public class Cliente extends PersonaAbs implements Comparable<Cliente> {

    private int idCliente;
    private String nombreComercial;
    private String representanteLegal;

    public Cliente() {
    }

    public Cliente(int idCliente, String nombreComercial, String representanteLegal, int idPersona, TipoIdentidad tipoIdentidad, String numeroIdentidad, String Nombres, String Direccion, String Telefono, String Email) {
        super(idPersona, tipoIdentidad, numeroIdentidad, Nombres, Direccion, Telefono, Email);
        this.idCliente = idCliente;
        this.nombreComercial = nombreComercial;
        this.representanteLegal = representanteLegal;
    }
    

//    public Cliente(Cliente t) {
//        super((PersonaAbs) t);
//    }
//    
    public Cliente(PersonaAbs t) {
        super((PersonaAbs) t);
    }
    

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    @Override
    public String toString() {
        return (super.toString() + " \t " + nombreComercial + " \t " + representanteLegal);
    }

    @Override
    public boolean equals(Object O) {
        if (this == O) {
            return true;
        }
        if (!(O instanceof Cliente)) {
            return false;
        }

        Cliente c = (Cliente) O;
        return Objects.equals(getNombres(), c.getNombres())
                || Objects.equals(getNumeroIdentidad(), c.getNumeroIdentidad());

    }

    @Override
    public int compareTo(Cliente o) {
        return COMPARATOR_CLIENTE.compare(this, o);

    }

    public static final Comparator<Cliente> COMPARATOR_CLIENTE
            = Comparator.comparing(Cliente::getNombres);
  

}
