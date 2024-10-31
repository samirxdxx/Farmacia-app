package Controlador;

import Dao.ClienteDao;
import Dao.ComprobanteDao;
import Dao.DetalleVentaDao;
import Dao.MedicamentoDao;
import Dao.VentaDao;
import Dao.PersonaDao;
import Dao.TipoIdentidadDao;
import Imprimir.ReportManager;
import Interface.Control;
import Modelo.Cliente;
import Modelo.Comprobante;
import Modelo.DetalleVenta;
import Modelo.Documento;
import Modelo.Medicamento;
import Vista.modal.frmVenta;
import Modelo.Venta;
import Modelo.Persona;
import Modelo.Personal;
import Modelo.TipoIdentidad;
import Utils.ConsultaWebService;
import Utils.Fecha_Date;
import Utils.ImgTabla;
import Utils.Numero_a_Letra;
import Utils.Qr;
import Vista.busqueda.Venta_Producto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author eder
 */
public class VentaController implements Control, ActionListener, KeyListener, MouseListener {

    private Venta venta;
    private DetalleVenta detalleventa;
    private Cliente cliente;
    private Personal personal;
    private Persona persona;
    private VentaDao ventaDao;
    private DetalleVentaDao detDao;
    private PersonaDao perDao;
    private ClienteDao clienteDao;
    private MedicamentoDao medDao;
    private TipoIdentidadDao tipoDao;
    private ComprobanteDao comprobanteDao;
    private frmVenta frmventa;
    private int crud;
    private boolean success;
    List<TipoIdentidad> listaTipo;

    DefaultTableModel modelo = new DefaultTableModel();
    ConsultaWebService webService = new ConsultaWebService();
    Fecha_Date fec = new Fecha_Date();

    public VentaController(frmVenta frmventa) {
        this.venta = new Venta();
        this.detalleventa = null;
        this.personal = null;
        this.persona = null;
        this.cliente = new Cliente();
        this.frmventa = frmventa;
        this.ventaDao = new VentaDao();
        this.detDao = new DetalleVentaDao();
        this.medDao = new MedicamentoDao();
        this.perDao = new PersonaDao();
        this.clienteDao = new ClienteDao();
        this.tipoDao = new TipoIdentidadDao();
        this.comprobanteDao = new ComprobanteDao();

        this.frmventa.btnRegistrar.addActionListener(this);
        this.frmventa.btnlistarProducto.addActionListener(this);
        this.frmventa.txtDocumentoCliente.addKeyListener(this);
        this.frmventa.txtDocumentoCliente.addActionListener(this);
        this.frmventa.cboTipoComprobante.addActionListener(this);
        
        this.frmventa.tblDetalle.setDefaultRenderer(Object.class, new ImgTabla());

        this.llenarComprobante(this.frmventa.cboTipoComprobante);

        this.frmventa.setTitle("REGISTRO DE VENTAS");

        fec.capturaymuestrahoradelsistema(this.frmventa.txtFecha);
        mostrar_Tabla();

        try {
            ReportManager.getInstance().compileReport("/Imprimir/report_pay.jrxml");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    public int ConsultaReSun() {

        int valorRpta = 0;

        int longiDoc = 11;

        int longitud = frmventa.txtDocumentoCliente.getText().length();
        if (longitud > longiDoc) {
            JOptionPane.showMessageDialog(null, "El número de Ruc ingresado no es válido. Vuelva a intentarlo.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if (longitud == 11) {

            String rpt = "";
            try {
                rpt = webService.consultarSunatruc(frmventa.txtDocumentoCliente.getText(), frmventa.txtNombreCliente, frmventa.txtDireccionCliente);
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
                rpt = webService.consultarReniecDni(frmventa.txtDocumentoCliente.getText(), frmventa.txtNombreCliente);
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

    public void muestraCliente(Cliente cliente) {
        //controladorCliente.buscar_por_id(id_clienteMostrar);
        String num_documentoCliente = cliente.getNumeroIdentidad();
        String nom_Cliente = cliente.getNombres();
        String correo_Cliente = cliente.getEmail();
        String direccion_Cliente = cliente.getDireccion();

        frmventa.lblIdCliente.setText("" + cliente.getIdCliente());
        frmventa.txtDocumentoCliente.setText(num_documentoCliente);
        frmventa.txtNombreCliente.setText(nom_Cliente);
        frmventa.txtCorreoCliente.setText(correo_Cliente);
        frmventa.txtDireccionCliente.setText(direccion_Cliente);
    }

    public void llenarComprobante(JComboBox lista) {
        lista.removeAll();
        //ComprobanteDao comprobanteDao = new ComprobanteDao();
        List<Comprobante> listaComprobante;

        try {
            listaComprobante = comprobanteDao.Listar();
            for (int i = 0; i < listaComprobante.size(); i++) {
                lista.addItem(new Comprobante(listaComprobante.get(i).getIdComprobante(),
                        listaComprobante.get(i).getDescripcion(), listaComprobante.get(i).getSerie(),
                        listaComprobante.get(i).getCorrelativo(),
                        new Documento(listaComprobante.get(i).getDocumento().getIdDocumento(),
                                listaComprobante.get(i).getDocumento().getCodigo(), "")));
            }
        } catch (Exception e) {
        }

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

        frmventa.txtSubTotal.setText(String.format("%.2f", valtotal));
        frmventa.txtTotaIgv.setText(String.format("%.2f", totalIgv));
        frmventa.txtImporteTotal.setText(String.format("%.2f", suma));
        Numero_a_Letra numeroletra = new Numero_a_Letra();
        frmventa.txtLeyenda.setText("SON: " + numeroletra.Convertir(String.valueOf(suma), true) + " SOLES");

    }

    private void agregarDetalle(int idprodBuscado) {
        MedicamentoDao mediDao = new MedicamentoDao();
        Medicamento medi = new Medicamento();
        medi.setIdMedicamento(idprodBuscado);

        try {
            //LISTA TODAS EMPRESAS
            medi = mediDao.Trae(medi);
            double cnt = Double.parseDouble(frmventa.lblcntProducto.getText());
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
            Logger.getLogger(Venta_Producto.class.getName()).log(Level.SEVERE, null, ex);
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
        frmventa.tblDetalle.setRowHeight(23);
        frmventa.tblDetalle.setModel(modelo);
        frmventa.tblDetalle.setBackground(Color.WHITE);
        frmventa.tblDetalle.setAutoResizeMode(frmventa.tblDetalle.AUTO_RESIZE_OFF);

        frmventa.tblDetalle.getColumnModel().getColumn(0).setPreferredWidth(30);
        frmventa.tblDetalle.getColumnModel().getColumn(1).setPreferredWidth(260);
        frmventa.tblDetalle.getColumnModel().getColumn(2).setPreferredWidth(90);
        frmventa.tblDetalle.getColumnModel().getColumn(3).setPreferredWidth(90);
        frmventa.tblDetalle.getColumnModel().getColumn(4).setPreferredWidth(90);
        frmventa.tblDetalle.getColumnModel().getColumn(5).setPreferredWidth(90);
        frmventa.tblDetalle.getColumnModel().getColumn(6).setPreferredWidth(90);
        frmventa.tblDetalle.getColumnModel().getColumn(7).setPreferredWidth(50);
    }

    private void limpiarTabla() {
        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }

    @Override
    public void Iniciar() {
        frmventa.setLocationRelativeTo(null);
    }

    @Override
    public void Habilita(Boolean t) {
    }

    @Override
    public void llenarTabla(JTable jt) {
    }

    @Override
    public void Limpiar() {
        cliente = new Cliente();
        venta = new Venta();
        detalleventa = new DetalleVenta();
        muestraCliente(cliente);

        limpiarTabla();
        sumarDetalle();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == frmventa.cboTipoComprobante) {
                var idComprobante = frmventa.cboTipoComprobante.getItemAt(
                        frmventa.cboTipoComprobante.getSelectedIndex()).getIdComprobante();
                
                frmventa.txtDocumentoComprobante.setText(String.valueOf(comprobanteDao.obtenerCorrelativo(idComprobante)));

            } else if (e.getSource() == frmventa.txtDocumentoCliente) {

                int ctnTexto = frmventa.txtDocumentoCliente.getText().length();
                switch (ctnTexto) {
                    case 8:
                        if (cliente.getIdCliente() < 1) {
                            int pregunta = JOptionPane.showConfirmDialog(null, "¿Desea realizar la Búsqueda del Cliente Ingresado?\n"
                                    + "El número de Documento es: " + frmventa.txtDocumentoCliente.getText(), "Mensaje", JOptionPane.OK_CANCEL_OPTION);
                            if (pregunta == JOptionPane.OK_OPTION) {
                                if (ConsultaReSun() == 1) {
                                    TipoIdentidad tipoid = new TipoIdentidad("1", "DNI");
                                    persona = new Persona(0, tipoid, frmventa.txtDocumentoCliente.getText(), frmventa.txtNombreCliente.getText().toUpperCase(),
                                            frmventa.txtDireccionCliente.getText(), "", "");

                                    if (!perDao.existeNroIdentidad(frmventa.txtDocumentoCliente.getText())) {
                                        persona.setIdPersona(perDao.creaId(persona));
                                    } else {

                                        persona = perDao.traeNroIdentidad(frmventa.txtDocumentoCliente.getText());
                                    }
                                    cliente = new Cliente(persona);

                                    cliente.setIdCliente(clienteDao.creaId(cliente));
                                    muestraCliente(cliente);

                                    JOptionPane.showMessageDialog(null, "Registro Exitoso", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

                                } else {
                                    JOptionPane.showMessageDialog(null, "Operación Cancelada", "Mensaje", JOptionPane.WARNING_MESSAGE);

                                }

                            }
                        }
                        break;
                    case 11:
                        if (cliente.getIdCliente() < 1) {
                            int pregunta = JOptionPane.showConfirmDialog(null, "¿Desea realizar la Búsqueda del Cliente Ingresado?\n"
                                    + "El número de Documento es: " + frmventa.txtDocumentoCliente.getText(), "Mensaje", JOptionPane.OK_CANCEL_OPTION);
                            if (pregunta == JOptionPane.OK_OPTION) {
                                if (ConsultaReSun() == 1) {
                                    TipoIdentidad tipoid = new TipoIdentidad("6", "RUC");
                                    persona = new Persona(0, tipoid, frmventa.txtDocumentoCliente.getText(), frmventa.txtNombreCliente.getText().toUpperCase(),
                                            frmventa.txtDireccionCliente.getText(), "", "");

                                    if (!perDao.existeNroIdentidad(frmventa.txtDocumentoCliente.getText())) {
                                        persona.setIdPersona(perDao.creaId(persona));
                                    } else {

                                        persona = perDao.traeNroIdentidad(frmventa.txtDocumentoCliente.getText());
                                    }

                                    cliente = new Cliente(persona);
                                    cliente.setIdCliente(clienteDao.creaId(cliente));
                                    muestraCliente(cliente);

                                    JOptionPane.showMessageDialog(null, "Registro Exitoso", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } else {

                                JOptionPane.showMessageDialog(null, "Operación Cancelada", "Mensaje", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        break;
                    default:
                        frmventa.txtNombreCliente.setText("");
                        frmventa.txtCorreoCliente.setText("");
                        frmventa.txtDireccionCliente.setText("");
                        frmventa.lblIdCliente.setText("");
                        JOptionPane.showMessageDialog(null, "El Documento ingresado no Cumple el valor necesario", "Mensaje", JOptionPane.ERROR_MESSAGE);
                        break;

                }

            } else if (e.getSource() == frmventa.btnlistarProducto) {
                new Venta_Producto(null, true).setVisible(true);
                if (Venta_Producto.valor == 1) {
                    agregarDetalle(Integer.parseInt(frmventa.lblIdProducto.getText()));
                    sumarDetalle();
                }

            } else if (e.getSource() == frmventa.btnRegistrar) {
                List<DetalleVenta> fields = new ArrayList<>();
                Qr qr = new Qr();
                success = false;
                int numFilas = modelo.getRowCount();
                if (numFilas < 1) {
                    JOptionPane.showMessageDialog(null, "No hay detalle");
                    return;
                }

                if (cliente.getIdCliente() == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un cliente");
                    return;
                }
                if (frmventa.txtFecha.getDate().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, "Fecha no válida");
                    return;
                }
                venta = new Venta();
                personal = new Personal();
                personal.setIdPersonal(1);
                venta.setCliente(cliente);
                venta.setVendedor(personal);
                venta.setFecha_Venta(LocalDate.now());
                venta.setSubTotal(Double.parseDouble(frmventa.txtSubTotal.getText()));
                venta.setIgv(Double.parseDouble(frmventa.txtTotaIgv.getText()));
                venta.setTotal(Double.parseDouble(frmventa.txtImporteTotal.getText()));
                venta.setEstado(1);

                venta.setIdVenta(ventaDao.creaId(venta));

                if (venta.getIdVenta() > 0) {
                    for (int fila = 0; fila < numFilas; fila++) {
                        detalleventa = new DetalleVenta(0, venta.getIdVenta(),
                                Integer.parseInt(modelo.getValueAt(fila, 0).toString()),
                                Double.parseDouble(modelo.getValueAt(fila, 2).toString()),
                                Double.parseDouble(modelo.getValueAt(fila, 5).toString()));
                        fields.add(detalleventa);
                        System.out.println(detalleventa.toString());
                        if (detDao.creaId(detalleventa) > 0) {
                            medDao.actualizaStock(detalleventa.getCantidad(), detalleventa.getIdMedicamento());

                        }

                    }
                    success = true;
                }

                if (success) {
                    crud = 1;
                    venta.setFields(fields);
                    venta.setQrcode(qr.Generar(String.valueOf(venta.getNumero_Venta())
                            + venta.getIgv() + venta.getTotal() + venta.getFecha_Venta().toString()
                            + venta.getCliente().getNumeroIdentidad()));
                    Map parameter = new HashMap();
                    try {

                        parameter.put("staff", "Eder aira");
                        parameter.put("customer", venta.getCliente().getNombres());
                        parameter.put("total", venta.getTotal());
                        parameter.put("qrcode", venta.getQrcode());

                        ReportManager.getInstance().printReport(venta, parameter);
                    } catch (JRException ej) {
                        JOptionPane.showMessageDialog(null, ej.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } finally {
                        Limpiar();
                    }

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

            if (e.getSource() == frmventa.txtDocumentoCliente) {

                int ctnTexto = frmventa.txtDocumentoCliente.getText().length();
                if (ctnTexto == 8) {
                    if (clienteDao.existeNroIdentidad(frmventa.txtDocumentoCliente.getText())) {
                        cliente = clienteDao.traenroIdentidad(frmventa.txtDocumentoCliente.getText());
                        muestraCliente(cliente);
                    } else {
                        frmventa.txtNombreCliente.setText("");
                        frmventa.txtCorreoCliente.setText("");
                        frmventa.txtDireccionCliente.setText("");
                        frmventa.lblIdCliente.setText("");
                    }

                } else if (ctnTexto == 11) {
                    if (clienteDao.existeNroIdentidad(frmventa.txtDocumentoCliente.getText())) {
                        cliente = clienteDao.traenroIdentidad(frmventa.txtDocumentoCliente.getText());
                        muestraCliente(cliente);

                    } else {
                        frmventa.txtNombreCliente.setText("");
                        frmventa.txtCorreoCliente.setText("");
                        frmventa.txtDireccionCliente.setText("");
                        frmventa.lblIdCliente.setText("");
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
