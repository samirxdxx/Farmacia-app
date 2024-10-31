package Modelo;

import Abstracto.PersonaAbs;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author eder
 */
public class Proveedor extends PersonaAbs implements Comparable<Proveedor> {

    private int idProveedor;
    private String representanteLegal;
    private String Marca;

    public Proveedor() {
    }

    public Proveedor(int idProveedor, String representanteLegal, String Marca, int idPersona, TipoIdentidad tipoIdentidad, String numeroIdentidad, String Nombres, String Direccion, String Telefono, String Email) {
        super(idPersona, tipoIdentidad, numeroIdentidad, Nombres, Direccion, Telefono, Email);
        this.idProveedor = idProveedor;
        this.representanteLegal = representanteLegal;
        this.Marca = Marca;
    }
    

//    public Proveedor(Proveedor t) {
//        super((PersonaAbs) t);
//    }
//    
    public Proveedor(PersonaAbs t) {
        super((PersonaAbs) t);
    }
    

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }


    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }
    
    

    @Override
    public String toString() {
        return (super.toString() + " \t " + Marca + " \t " + representanteLegal);
    }

    @Override
    public boolean equals(Object O) {
        if (this == O) {
            return true;
        }
        if (!(O instanceof Proveedor)) {
            return false;
        }

        Proveedor c = (Proveedor) O;
        return Objects.equals(getNombres(), c.getNombres())
                || Objects.equals(getNumeroIdentidad(), c.getNumeroIdentidad());

    }

    @Override
    public int compareTo(Proveedor o) {
        return COMPARATOR_CLIENTE.compare(this, o);

    }

    public static final Comparator<Proveedor> COMPARATOR_CLIENTE
            = Comparator.comparing(Proveedor::getNombres);
  

}
