/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import java.util.List;

/**
 *
 * @author eder
 * @param <T>
 */
public interface Crud<T> {

    boolean Crear(T t) throws Exception;

    boolean Modificar(T t) throws Exception;

    boolean Eliminar(T t) throws Exception;

    int creaId(T t) throws Exception;

    List<T> Listar() throws Exception;

    T Trae(T t) throws Exception;
}
