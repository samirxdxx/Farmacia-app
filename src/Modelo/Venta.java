package Modelo;

import Imprimirl.Params;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author eder
 */
public class Venta extends Params<DetalleVenta>{
    private int idVenta;
    private Cliente cliente;
    private LocalDate fecha_Venta;
    private int numero_Venta;
    private Personal Vendedor;
    private double subTotal, Igv, Total;
    private int Estado;

    public Venta() {
    }

    public Venta(int idVenta, Cliente cliente, LocalDate fecha_Venta, int numero_Venta, Personal Vendedor, double subTotal, double Igv, double Total, int Estado, List<DetalleVenta> fields, InputStream qrcode) {
        super(fields, qrcode);
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.fecha_Venta = fecha_Venta;
        this.numero_Venta = numero_Venta;
        this.Vendedor = Vendedor;
        this.subTotal = subTotal;
        this.Igv = Igv;
        this.Total = Total;
        this.Estado = Estado;
    }



    public Venta(int idVenta, Cliente cliente, LocalDate fecha_Venta, int numero_Venta, Personal Vendedor, double subTotal, double Igv, double Total, int Estado) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.fecha_Venta = fecha_Venta;
        this.numero_Venta = numero_Venta;
        this.Vendedor = Vendedor;
        this.subTotal = subTotal;
        this.Igv = Igv;
        this.Total = Total;
        this.Estado = Estado;
    }



    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha_Venta() {
        return fecha_Venta;
    }

    public void setFecha_Venta(LocalDate fecha_Venta) {
        this.fecha_Venta = fecha_Venta;
    }

    public int getNumero_Venta() {
        return numero_Venta;
    }

    public void setNumero_Venta(int numero_Venta) {
        this.numero_Venta = numero_Venta;
    }

    public Personal getVendedor() {
        return Vendedor;
    }

    public void setVendedor(Personal Vendedor) {
        this.Vendedor = Vendedor;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getIgv() {
        return Igv;
    }

    public void setIgv(double Igv) {
        this.Igv = Igv;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    @Override
    public String toString() {
        return "Venta{" + "idVenta=" + idVenta + ", cliente=" + cliente + ", fecha_Venta=" + fecha_Venta + ", numero_Venta=" + numero_Venta + ", Vendedor=" + Vendedor + ", subTotal=" + subTotal + ", Igv=" + Igv + ", Total=" + Total + ", Estado=" + Estado + '}';
    }

    
    
    
}
