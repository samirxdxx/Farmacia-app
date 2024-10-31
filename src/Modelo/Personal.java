/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Abstracto.PersonaAbs;

/**
 *
 * @author eder
 */
public class Personal extends PersonaAbs {

    private int idPersonal;
    private String Cargo;
    private double Sueldo;
    private int Estado;

    public Personal() {
    }

    public Personal(int idPersonal, String Cargo, double Sueldo, int Estado) {
        this.idPersonal = idPersonal;
        this.Cargo = Cargo;
        this.Sueldo = Sueldo;
        this.Estado = Estado;
    }

    public Personal(int idPersonal, String Cargo, double Sueldo, int Estado, int idPersona, TipoIdentidad tipoIdentidad, String numeroIdentidad, String Nombres, String Direccion, String Telefono, String Email) {
        super(idPersona, tipoIdentidad, numeroIdentidad, Nombres, Direccion, Telefono, Email);
        this.idPersonal = idPersonal;
        this.Cargo = Cargo;
        this.Sueldo = Sueldo;
        this.Estado = Estado;
    }

    public Personal(PersonaAbs t) {
        super((PersonaAbs) t);
    }

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public String getCargo() {
        return Cargo;
    }

    public void setCargo(String Cargo) {
        this.Cargo = Cargo;
    }

    public double getSueldo() {
        return Sueldo;
    }

    public void setSueldo(double Sueldo) {
        this.Sueldo = Sueldo;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

   
}
