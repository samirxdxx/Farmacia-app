/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author eder
 */
public class DetalleCompra {

    private int idDetalleCompra;
    private int idCompra;
    private int idMedicamento;
    private double Cantidad;
    private double Precio;

    public DetalleCompra(int idDetalleCompra, int idCompra, int idMedicamento, double Cantidad, double Precio) {
        this.idDetalleCompra = idDetalleCompra;
        this.idCompra = idCompra;
        this.idMedicamento = idMedicamento;
        this.Cantidad = Cantidad;
        this.Precio = Precio;

    }

    public DetalleCompra() {
    }

    public int getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(int idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
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

}
