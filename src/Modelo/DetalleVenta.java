/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author eder
 */
public class DetalleVenta {

    private int idDetalleVenta;
    private int idVenta;
    private int idMedicamento;
    private double Cantidad;
    private double Precio;

    public DetalleVenta(int idDetalleVenta, int idVenta, int idMedicamento, double Cantidad, double Precio) {
        this.idDetalleVenta = idDetalleVenta;
        this.idVenta = idVenta;
        this.idMedicamento = idMedicamento;
        this.Cantidad = Cantidad;
        this.Precio = Precio;

    }

    public DetalleVenta() {
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" + "idDetalleVenta=" + idDetalleVenta + ", idVenta=" + idVenta + ", idMedicamento=" + idMedicamento + ", Cantidad=" + Cantidad + ", Precio=" + Precio + '}';
    }

}
