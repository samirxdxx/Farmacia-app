package Modelo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author eder
 */
public class Medicamento implements Comparable<Medicamento> {

    private int idMedicamento;
    private String Nombre;
    private String Descripcion;
    private String Laboratorio;
    private double Precio;
    private LocalDate fechaCaducidad;
    private double Stock;
    private int Estado;

    public Medicamento() {
    }

    public Medicamento(int idMedicamento, String Nombre, String Descripcion, String Laboratorio, double Precio, LocalDate fechaCaducidad, double Stock, int Estado) {
        this.idMedicamento = idMedicamento;
        this.Nombre = Nombre;
        this.Descripcion = Descripcion;
        this.Laboratorio = Laboratorio;
        this.Precio = Precio;
        this.fechaCaducidad = fechaCaducidad;
        this.Stock = Stock;
        this.Estado = Estado;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getLaboratorio() {
        return Laboratorio;
    }

    public void setLaboratorio(String Laboratorio) {
        this.Laboratorio = Laboratorio;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public double getStock() {
        return Stock;
    }

    public void setStock(double Stock) {
        this.Stock = Stock;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    @Override
    public String toString() {
        return Nombre + " \t " + Descripcion + " \t " + Laboratorio + " \t " + Precio + " \t " + fechaCaducidad;
    }

    @Override
    public boolean equals(Object O) {
        if (this == O) {
            return true;
        }
        if (!(O instanceof Medicamento)) {
            return false;
        }

        Medicamento c = (Medicamento) O;
        return Objects.equals(getNombre(), c.getNombre());

    }

    @Override
    public int compareTo(Medicamento o) {
        return COMPARATOR_MEDICA.compare(this, o);
    }

    public static final Comparator<Medicamento> COMPARATOR_MEDICA
            = Comparator.comparing(Medicamento::getNombre);

}
