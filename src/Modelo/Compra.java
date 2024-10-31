package Modelo;

import java.time.LocalDate;

/**
 *
 * @author eder
 */
public class Compra {
    private int idCompra;
    private Proveedor proveedor;
    private LocalDate fecha_Compra;
    private String numero_Compra;
    private double subTotal, Igv, Total;
    private int Estado;

    public Compra() {
    }

    public Compra(int idCompra, Proveedor proveedor, LocalDate fecha_Compra, String numero_Compra, double subTotal, double Igv, double Total, int Estado) {
        this.idCompra = idCompra;
        this.proveedor = proveedor;
        this.fecha_Compra = fecha_Compra;
        this.numero_Compra = numero_Compra;
        this.subTotal = subTotal;
        this.Igv = Igv;
        this.Total = Total;
        this.Estado = Estado;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getFecha_Compra() {
        return fecha_Compra;
    }

    public void setFecha_Compra(LocalDate fecha_Compra) {
        this.fecha_Compra = fecha_Compra;
    }

    public String getNumero_Compra() {
        return numero_Compra;
    }

    public void setNumero_Compra(String numero_Compra) {
        this.numero_Compra = numero_Compra;
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
        return "Compra{" + "idCompra=" + idCompra + ", proveedor=" + proveedor + ", fecha_Compra=" + fecha_Compra + ", numero_Compra=" + numero_Compra + ", subTotal=" + subTotal + ", Igv=" + Igv + ", Total=" + Total + ", Estado=" + Estado + '}';
    }
    
    
    
}
