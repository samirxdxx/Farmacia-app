/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Imprimir;

import Imprimirl.Params;
import Modelo.DetalleVenta;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author eder
 */
public class Ventai extends Params{

    private String staff;
    private String customer;
    private String total;

    public Ventai() {
    }

    public Ventai(String staff, String customer, String total, List<DetalleVenta> fields, InputStream qrcode) {
        super(fields, qrcode);
        this.staff = staff;
        this.customer = customer;
        this.total = total;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    

}
