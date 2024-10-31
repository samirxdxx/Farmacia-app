/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author eder
 */
public class Comprobante{

    private int idComprobante;
    private String Descripcion;
    private String Serie;
    private int Correlativo;
    private Documento documento;

    public Comprobante() {

    }

    public Comprobante(int idComprobante, String Descripcion, String Serie, int Correlativo, Documento documento) {
        this.idComprobante = idComprobante;
        this.Descripcion = Descripcion;
        this.Serie = Serie;
        this.Correlativo = Correlativo;
        this.documento = documento;
    }

    public int getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String Serie) {
        this.Serie = Serie;
    }

    public int getCorrelativo() {
        return Correlativo;
    }

    public void setCorrelativo(int Correlativo) {
        this.Correlativo = Correlativo;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @Override
    public String toString() {
        return Descripcion + " " + Serie;
    }
    

    @Override
    public boolean equals(Object o) {
        if (o instanceof Comprobante) {
            Comprobante c = (Comprobante) o;
            return this.idComprobante == c.getIdComprobante();
        }
        return false;
    }

}
