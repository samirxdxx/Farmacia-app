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
public class Persona extends PersonaAbs {

    public Persona() {
    }

    public Persona(int idPersona, TipoIdentidad tipoIdentidad, String numeroIdentidad, String Nombres, String Direccion, String Telefono, String Email) {
        super(idPersona, tipoIdentidad, numeroIdentidad, Nombres, Direccion, Telefono, Email);
    }

    public Persona(Cliente t) {
        super((PersonaAbs) t);
    }

    public Persona(Personal t) {
        super((PersonaAbs) t);
    }
}
