/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JFrame;

/**
 *
 * @author eder
 */
public class VentanasActivas {

    private boolean rest;
    private JFrame fmain;

    public boolean existeVentana(String nomframe) {
        this.rest = false;

        Window[] windows = Window.getWindows();

        for (Window window : windows) {
            if (window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                if(frame.getName().contains(nomframe))this.rest=true;
            }
        }
        System.out.println("rest" + this.rest);
        return this.rest;
    }
    
    public JFrame getHome() {
        //this.fmain = null;
        
        Window[] windows = Window.getWindows();

        for (Window window : windows) {
            if (window instanceof JFrame) {
                JFrame frame  = (JFrame) window;
                System.out.println(frame.getName());
                if( frame.getName().contains("Home")) this.fmain=frame;
            }
        }
        return this.fmain;
    }
    
    

}
