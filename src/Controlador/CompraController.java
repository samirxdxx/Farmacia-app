package Controlador;

import Dao.ProveedorDao;
import Dao.DetalleCompraDao;
import Dao.MedicamentoDao;
import Dao.CompraDao;
import Dao.PersonaDao;
import Dao.TipoIdentidadDao;
import Interface.Control;
import Modelo.Proveedor;
import Modelo.DetalleCompra;
import Modelo.Medicamento;
import Vista.modal.frmCompra;
import Modelo.Compra;
import Modelo.Persona;
import Modelo.Personal;
import Modelo.TipoIdentidad;
import Utils.ConsultaWebService;
import Utils.Fecha_Date;
import Utils.ImgTabla;
import Utils.Numero_a_Letra;
import Vista.busqueda.Compra_Producto;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eder
 */
public class CompraController implements Control, ActionListener, KeyListener, MouseListener {

    private Compra compra;
    private DetalleCompra detallecompra;
    private Proveedor proveedor;
    private Personal personal;
    private Persona persona;
    private CompraDao compraDao;
    private DetalleCompraDao detDao;
    private PersonaDao perDao;
    private ProveedorDao proveedorDao;
    private MedicamentoDao medDao;
    private TipoIdentidadDao tipoDao;
    private frmCompra frmcompra;
    private int crud;
    private boolean success;
    List<TipoIdentidad> listaTipo;
    DefaultTableModel modelo = new DefaultTableModel();
    ConsultaWebService webService = new ConsultaWebService();
    Fecha_Date fec = new Fecha_Date();

    public CompraController(frmCompra frmcompra) {
        this.compra = new Compra();
        this.detallecompra = null;
        this.personal = null;
        this.persona = null;
        this.proveedor = new Proveedor();
        this.frmcompra = frmcompra;
        this.compraDao = new CompraDao();
        this.detDao = new DetalleCompraDao();
        this.medDao = new MedicamentoDao();
        this.perDao = new PersonaDao();
        this.proveedorDao = new ProveedorDao();
        this.tipoDao = new TipoIdentidadDao();

        this.frmcompra.btnRegistrar.addActionListener(this);
        this.frmcompra.btnlistarProducto.addActionListener(this);
        this.frmcompra.txtDocumentoProveedor.addKeyListener(this);
        this.frmcompra.txtDocumentoProveedor.addActionListener(this);
        this.frmcompra.tblDetalle.setDefaultRenderer(Object.class, new ImgTabla());
        
        this.frmcompra.setTitle("REGISTRO DE COMPRAS");

        fec.capturaymuestrahoradelsistema(this.frmcompra.txtFecha);
        mostrar_Tabla();

    }

    public int ConsultaReSun() {

        int valorRpta = 0;

        int longiDoc = 11;

        int longitud = frmcompra.txtDocumentoProveedor.getText().length();
        if (longitud > longiDoc) {
            JOptionPane.showMessageDialog(null, "El número de Ruc ingresado no es válido. Vuelva a intentarlo.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if (longitud == 11) {

            String rpt = "";
            try {
                rpt = webService.consultarSunatruc(frmcompra.txtDocumentoProveedor.getText(), frmcompra.txtNombreProveedor, frmcompra.txtDireccionProveedor);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (!rpt.equalsIgnoreCase("SI")) {
                JOptionPane.showMessageDialog(null, "Intente nuevamente",
                        "Mensaje", JOptionPane.INFORMATION_MESSAGE);

            } else {
                valorRpta = 1;
            }
        } else if (longitud == 8) {

            String rpt = "";
            try {
                rpt = webService.consultarReniecDni(frmcompra.txtDocumentoProveedor.getText(), frmcompra.txtNombreProveedor);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (!rpt.equalsIgnoreCase("SI")) {
                JOptionPane.showMessageDialog(null, "Intente nuevamente",
                        "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                valorRpta = 1;
            }
        }

        return valorRpta;
    }

    public void muestraProveedor(Proveedor proveedor) {
        //controladorProveedor.buscar_por_id(id_proveedorMostrar);
        String num_documentoProveedor = proveedor.getNumeroIdentidad();
        String nom_Proveedor = proveedor.getNombres();
        String correo_Proveedor = proveedor.getEmail();
        String direccion_Proveedor = proveedor.getDireccion();

        frmcompra.lblIdProveedor.setText("" + proveedor.getIdProveedor());
        frmcompra.txtDocumentoProveedor.setText(num_documentoProveedor);
        frmcompra.txtNombreProveedor.setText(nom_Proveedor);
        frmcompra.txtCorreoProveedor.setText(correo_Proveedor);
        frmcompra.txtDireccionProveedor.setText(direccion_Proveedor);
    }

    public void llenarTipoEntidad(JComboBox lista) {
        lista.removeAll();

        try {
            listaTipo = tipoDao.Listar();
            for (int i = 0; i < listaTipo.size(); i++) {
                lista.addItem(new TipoIdentidad(listaTipo.get(i).getIdTipoIdentidad(), listaTipo.get(i).getNombre()));

            }
        } catch (Exception e) {
        }

    }

    private void sumarDetalle() {
        int numFilas = modelo.getRowCount();
        int numColumnas = modelo.getColumnCount();
        double suma = 0;

        for (int fila = 0; fila < numFilas; fila++) {
            Object valorr = modelo.getValueAt(fila, 6);

            // Convertir el valor a double (suponiendo que sea un valor numérico)
            double valorNumerico = Double.parseDouble(valorr.toString());

            suma += valorNumerico;
        }

        double valtotal = suma * 0.82;
        double totalIgv = suma * 0.18;

        frmcompra.txtSubTotal.setText(String.format("%.2f", valtotal));
        frmcompra.txtTotaIgv.setText(String.format("%.2f", totalIgv));
        frmcompra.txtImporteTotal.setText(String.format("%.2f", suma));
        Numero_a_Letra numeroletra = new Numero_a_Letra();
        frmcompra.txtLeyenda.setText("SON: " + numeroletra.Convertir(String.valueOf(suma), true) + " SOLES");

    }

    private void agregarDetalle(int idprodBuscado) {
        MedicamentoDao mediDao = new MedicamentoDao();
        Medicamento medi = new Medicamento();
        medi.setIdMedicamento(idprodBuscado);

        try {
            //LISTA TODAS EMPRESAS
            medi = mediDao.Trae(medi);
            double cnt = Double.parseDouble(frmcompra.lblcntProducto.getText());
            Object datos[] = new Object[10];
            double valuni = medi.getPrecio() * 0.82;
            double igv = medi.getPrecio() * 0.18;

            datos[0] = (medi.getIdMedicamento()) + "";
            datos[1] = (medi.getNombre()) + "";
            datos[2] = (cnt) + "";
            datos[3] = (String.format("%.2f", valuni)) + "";
            datos[4] = (String.format("%.2f", igv)) + "";
            datos[5] = (medi.getPrecio()) + "";
            datos[6] = (medi.getPrecio() * cnt) + "";
            ImageIcon icono = new ImageIcon(getClass().getResource("/image/delete.png"));
            Icon btnEliminar = new ImageIcon(icono.getImage().getScaledInstance(14, 14, Image.SCALE_DEFAULT));
            JButton botonEliminar = new JButton("", btnEliminar);
            botonEliminar.setName("btnEliminar");
            botonEliminar.setToolTipText("Eliminar item");

            datos[7] = botonEliminar;
            modelo.addRow(datos);

            //lblCantidadDatos.setText("Cantidad de Datos Cargados: " + num);
        } catch (Exception ex) {
            Logger.getLogger(Compra_Producto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void mostrar_Tabla() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        modelo.addColumn("#");
        modelo.addColumn("ARTICULO");
        modelo.addColumn("CNT");
        modelo.addColumn("VLR. UNI");
        modelo.addColumn("IGV");
        modelo.addColumn("PRECIO");
        modelo.addColumn("IMPORTE");
        modelo.addColumn("");
        frmcompra.tblDetalle.setRowHeight(23);
        frmcompra.tblDetalle.setModel(modelo);
        frmcompra.tblDetalle.setBackground(Color.WHITE);
        frmcompra.tblDetalle.setAutoResizeMode(frmcompra.tblDetalle.AUTO_RESIZE_OFF);

        frmcompra.tblDetalle.getColumnModel().getColumn(0).setPreferredWidth(30);
        frmcompra.tblDetalle.getColumnModel().getColumn(1).setPreferredWidth(260);
        frmcompra.tblDetalle.getColumnModel().getColumn(2).setPreferredWidth(90);
        frmcompra.tblDetalle.getColumnModel().getColumn(3).setPreferredWidth(90);
        frmcompra.tblDetalle.getColumnModel().getColumn(4).setPreferredWidth(90);
        frmcompra.tblDetalle.getColumnModel().getColumn(5).setPreferredWidth(90);
        frmcompra.tblDetalle.getColumnModel().getColumn(6).setPreferredWidth(90);
        frmcompra.tblDetalle.getColumnModel().getColumn(7).setPreferredWidth(50);
    }

    private void limpiarTabla() {
        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }

    @Override
    public void Iniciar() {
        frmcompra.setLocationRelativeTo(null);
    }

    @Override
    public void Habilita(Boolean t) {
    }

    @Override
    public void llenarTabla(JTable jt) {
    }

    @Override
    public void Limpiar() {
        proveedor = new Proveedor();
        compra = new Compra();
        detallecompra = new DetalleCompra();
        muestraProveedor(proveedor);

        limpiarTabla();
        sumarDetalle();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == frmcompra.txtDocumentoProveedor) {

                int ctnTexto = frmcompra.txtDocumentoProveedor.getText().length();
                switch (ctnTexto) {
                    case 8 -> {
                        if (proveedor.getIdProveedor() < 1) {
                            int pregunta = JOptionPane.showConfirmDialog(null, "¿Desea realizar la Búsqueda del Proveedor Ingresado?\n"
                                    + "El número de Documento es: " + frmcompra.txtDocumentoProveedor.getText(), "Mensaje", JOptionPane.OK_CANCEL_OPTION);
                            if (pregunta == JOptionPane.OK_OPTION) {
                                if (ConsultaReSun() == 1) {
                                    TipoIdentidad tipoid = new TipoIdentidad("1", "DNI");
                                    persona = new Persona(0, tipoid, frmcompra.txtDocumentoProveedor.getText(), frmcompra.txtNombreProveedor.getText().toUpperCase(),
                                            frmcompra.txtDireccionProveedor.getText(), "", "");

                                    if (!perDao.existeNroIdentidad(frmcompra.txtDocumentoProveedor.getText())) {
                                        persona.setIdPersona(perDao.creaId(persona));
                                    } else {

                                        persona = perDao.traeNroIdentidad(frmcompra.txtDocumentoProveedor.getText());
                                    }
                                    proveedor = new Proveedor(persona);

                                    proveedor.setIdProveedor(proveedorDao.creaId(proveedor));
                                    muestraProveedor(proveedor);

                                    JOptionPane.showMessageDialog(null, "Registro Exitoso", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

                                } else {
                                    JOptionPane.showMessageDialog(null, "Operación Cancelada", "Mensaje", JOptionPane.WARNING_MESSAGE);

                                }

                            }
                        }
                    }
                    case 11 -> {
                        if (proveedor.getIdProveedor() < 1) {
                            int pregunta = JOptionPane.showConfirmDialog(null, "¿Desea realizar la Búsqueda del Proveedor Ingresado?\n"
                                    + "El número de Documento es: " + frmcompra.txtDocumentoProveedor.getText(), "Mensaje", JOptionPane.OK_CANCEL_OPTION);
                            if (pregunta == JOptionPane.OK_OPTION) {
                                if (ConsultaReSun() == 1) {
                                    TipoIdentidad tipoid = new TipoIdentidad("6", "RUC");
                                    persona = new Persona(0, tipoid, frmcompra.txtDocumentoProveedor.getText(), frmcompra.txtNombreProveedor.getText().toUpperCase(),
                                            frmcompra.txtDireccionProveedor.getText(), "", "");

                                    if (!perDao.existeNroIdentidad(frmcompra.txtDocumentoProveedor.getText())) {
                                        persona.setIdPersona(perDao.creaId(persona));
                                    } else {

                                        persona = perDao.traeNroIdentidad(frmcompra.txtDocumentoProveedor.getText());
                                    }

                                    proveedor = new Proveedor(persona);
                                    proveedor.setIdProveedor(proveedorDao.creaId(proveedor));
                                    muestraProveedor(proveedor);

                                    JOptionPane.showMessageDialog(null, "Registro Exitoso", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } else {

                                JOptionPane.showMessageDialog(null, "Operación Cancelada", "Mensaje", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                    default -> {
                        frmcompra.txtNombreProveedor.setText("");
                        frmcompra.txtCorreoProveedor.setText("");
                        frmcompra.txtDireccionProveedor.setText("");
                        frmcompra.lblIdProveedor.setText("");
                        JOptionPane.showMessageDialog(null, "El Documento ingresado no Cumple el valor necesario", "Mensaje", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else if (e.getSource() == frmcompra.btnlistarProducto) {
                new Compra_Producto(null, true).setVisible(true);
                if (Compra_Producto.valor == 1) {
                    agregarDetalle(Integer.parseInt(frmcompra.lblIdProducto.getText()));
                    sumarDetalle();
                }

            } else if (e.getSource() == frmcompra.btnRegistrar) {
                success = false;
                int numFilas = modelo.getRowCount();
                if (numFilas < 1) {
                    JOptionPane.showMessageDialog(null, "No hay detalle");
                    return;
                }

                if (proveedor.getIdProveedor() == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un proveedor");
                    return;
                }
                if (frmcompra.txtFecha.getDate().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, "Fecha no válida");
                    return;
                }
                if (frmcompra.txtDocumentoComprobante.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Nro de compra no válida");
                    return;
                }
                compra = new Compra();
                personal = new Personal();
                personal.setIdPersonal(1);
                compra.setProveedor(proveedor);
                compra.setFecha_Compra(LocalDate.now());
                compra.setNumero_Compra(frmcompra.txtDocumentoComprobante.getText());
                compra.setSubTotal(Double.parseDouble(frmcompra.txtSubTotal.getText()));
                compra.setIgv(Double.parseDouble(frmcompra.txtTotaIgv.getText()));
                compra.setTotal(Double.parseDouble(frmcompra.txtImporteTotal.getText()));
                compra.setEstado(1);

                compra.setIdCompra(compraDao.creaId(compra));

                if (compra.getIdCompra() > 0) {
                    for (int fila = 0; fila < numFilas; fila++) {
                        detallecompra = new DetalleCompra(0, compra.getIdCompra(),
                                Integer.parseInt(modelo.getValueAt(fila, 0).toString()),
                                Double.parseDouble(modelo.getValueAt(fila, 2).toString()),
                                Double.parseDouble(modelo.getValueAt(fila, 5).toString()));

                        if (detDao.creaId(detallecompra) > 0) {
                            medDao.actualizaStock(detallecompra.getCantidad()*-1, detallecompra.getIdMedicamento());
                        }

                    }
                    success = true;
                }

                if (success) {
                    crud = 1;
                    JOptionPane.showMessageDialog(null, "Registro Guardado");
                    Limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "No se guardadó", "Advertencia", JOptionPane.WARNING_MESSAGE);

                }

            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void keyTyped(KeyEvent e
    ) {

    }

    @Override
    public void keyPressed(KeyEvent e
    ) {

    }

    @Override
    public void keyReleased(KeyEvent e
    ) {

        try {

            if (e.getSource() == frmcompra.txtDocumentoProveedor) {

                int ctnTexto = frmcompra.txtDocumentoProveedor.getText().length();
                if (ctnTexto == 8) {
                    if (proveedorDao.existeNroIdentidad(frmcompra.txtDocumentoProveedor.getText())) {
                        proveedor = proveedorDao.traenroIdentidad(frmcompra.txtDocumentoProveedor.getText());
                        muestraProveedor(proveedor);
                    } else {
                        frmcompra.txtNombreProveedor.setText("");
                        frmcompra.txtCorreoProveedor.setText("");
                        frmcompra.txtDireccionProveedor.setText("");
                        frmcompra.lblIdProveedor.setText("");
                    }

                } else if (ctnTexto == 11) {
                    if (proveedorDao.existeNroIdentidad(frmcompra.txtDocumentoProveedor.getText())) {
                        proveedor = proveedorDao.traenroIdentidad(frmcompra.txtDocumentoProveedor.getText());
                        muestraProveedor(proveedor);

                    } else {
                        frmcompra.txtNombreProveedor.setText("");
                        frmcompra.txtCorreoProveedor.setText("");
                        frmcompra.txtDireccionProveedor.setText("");
                        frmcompra.lblIdProveedor.setText("");
                    }

                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e
    ) {

    }

    @Override
    public void mousePressed(MouseEvent e
    ) {

    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {

    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {

    }

    @Override
    public void mouseExited(MouseEvent e
    ) {

    }

}
