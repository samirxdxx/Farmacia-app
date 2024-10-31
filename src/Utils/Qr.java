/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import com.google.zxing.BarcodeFormat;

import com.google.zxing.Writer;
import com.google.zxing.WriterException;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author eder
 */
public class Qr {

    public InputStream Generar(String texto) {

        int qrTamAncho = 400;
        int qrTamAlto = 400;
        String formato = "png";
        ByteArrayOutputStream outputStream = null;
        BitMatrix matriz = null;

        try {

            Writer writer = new QRCodeWriter();
            try {
                matriz = writer.encode(texto, BarcodeFormat.QR_CODE, qrTamAncho, qrTamAlto);
            } catch (WriterException e) {
                e.printStackTrace(System.err);
            }
            BufferedImage imagen = new BufferedImage(qrTamAncho,
                    qrTamAlto, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < qrTamAlto; y++) {
                for (int x = 0; x < qrTamAncho; x++) {
                    int valor = (matriz.get(x, y) ? 0 : 1) & 0xff;
                    imagen.setRGB(x, y, (valor == 0 ? 0 : 0xFFFFFF));
                }
            }
            /*FileOutputStream qrCode = new FileOutputStream(ruta);
            ImageIO.write(imagen, formato, qrCode);*/

            outputStream = new ByteArrayOutputStream();
            ImageIO.write(imagen, formato, outputStream);

            //qrCode.close();
        } catch (IOException ex) {
            Logger.getLogger(Qr.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}
