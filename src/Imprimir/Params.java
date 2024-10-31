/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Imprimirl;

import java.io.InputStream;
import java.util.List;

/**
 *
 * @author eder
 */
public class Params<T> {
    private List<T> fields;
    private InputStream qrcode;

    public Params() {
    }
    

    public Params(List<T> fields, InputStream qrcode) {
        this.fields = fields;
        this.qrcode = qrcode;
    }

    public List<T> getFields() {
        return fields;
    }

    public void setFields(List<T> fields) {
        this.fields = fields;
    }

    public InputStream getQrcode() {
        return qrcode;
    }

    public void setQrcode(InputStream qrcode) {
        this.qrcode = qrcode;
    }


    
   
}
