/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import javax.swing.JTable;

/**
 *
 * @author eder
 */
public interface Control {
    void Iniciar();
    void Limpiar();
    void Habilita(Boolean t);
    void llenarTabla(JTable jt);
}
