package Controlador;

import Dao.CompraDao;
import Dao.PersonaDao;
import Dao.TipoIdentidadDao;
import Interface.Control;
import Modelo.Proveedor;
import Vista.listaCompra;
import Modelo.Compra;
import Modelo.Persona;
import Modelo.TipoIdentidad;
import Utils.Colorear_Tablas;
import Utils.ImgTabla;
import Utils.Validator;
import Vista.modal.frmCompra;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eder
 */
public class listaCompraController implements Control, ActionListener, KeyListener, MouseListener {

    private Compra compra;
    private Persona persona;
    private CompraDao compraDao;
    private PersonaDao perDao;
    private TipoIdentidadDao tipoDao;
    private listaCompra listacompra;
    private int crud;
    private boolean success;
    List<TipoIdentidad> listaTipo;
    DefaultTableModel modelo = new DefaultTableModel();

    public listaCompraController(listaCompra listacompra) {
        this.compra = new Compra();
        this.persona = null;
        this.listacompra = listacompra;
        this.compraDao = new CompraDao();
        this.perDao = new PersonaDao();
        this.tipoDao = new TipoIdentidadDao();

        this.listacompra.btnNuevo.addActionListener(this);
        this.listacompra.btnBuscar.addActionListener(this);
        this.listacompra.tblDetalle.addMouseListener(this);

        this.listacompra.tblDetalle.setDefaultRenderer(Object.class, new ImgTabla());

        mostrar_Tabla();
        //llenarTipoEntidad(this.listacompra.cbxTipoIdentidad);
        llenarTabla(listacompra.tblDetalle);

    }

    private void mostrar_Tabla() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        modelo.addColumn("");
        modelo.addColumn("FECHA");
        modelo.addColumn("CLIENTE");
        modelo.addColumn("DOC PROVEEDOR");
        modelo.addColumn("DOC COMPRA");
        modelo.addColumn("TOTAL");
        modelo.addColumn("ESTADO");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        listacompra.tblDetalle.setRowHeight(21);
        listacompra.tblDetalle.setModel(modelo);
        listacompra.tblDetalle.setBackground(Color.WHITE);
        listacompra.tblDetalle.setAutoResizeMode(listacompra.tblDetalle.AUTO_RESIZE_OFF);

        listacompra.tblDetalle.getColumnModel().getColumn(0).setPreferredWidth(0);
        listacompra.tblDetalle.getColumnModel().getColumn(1).setPreferredWidth(100);
        listacompra.tblDetalle.getColumnModel().getColumn(2).setPreferredWidth(250);
        listacompra.tblDetalle.getColumnModel().getColumn(3).setPreferredWidth(100);
        listacompra.tblDetalle.getColumnModel().getColumn(4).setPreferredWidth(100);
        listacompra.tblDetalle.getColumnModel().getColumn(5).setPreferredWidth(80);
        listacompra.tblDetalle.getColumnModel().getColumn(6).setPreferredWidth(80);
        listacompra.tblDetalle.getColumnModel().getColumn(7).setPreferredWidth(28);
        listacompra.tblDetalle.getColumnModel().getColumn(8).setPreferredWidth(28);
        listacompra.tblDetalle.getColumnModel().getColumn(9).setPreferredWidth(28);
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

    @Override
    public void Iniciar() {
        //listacompra.setLocationRelativeTo(null);
    }

    @Override
    public void Habilita(Boolean t) {
    }

    @Override
    public void llenarTabla(JTable jt) {
        //modelo = (DefaultTableModel) jt.getModel();
        //jt.setModel(modelo);
        try {

            List<Compra> lista = compraDao.Listar();
            System.out.println("data:" + lista.size());

            Object[] objeto = new Object[10];
            for (int i = 0; i < lista.size(); i++) {

                objeto[0] = lista.get(i).getIdCompra();
                objeto[1] = lista.get(i).getFecha_Compra();
                objeto[2] = lista.get(i).getProveedor().getNombres();
                objeto[3] = lista.get(i).getProveedor().getNumeroIdentidad();
                objeto[4] = lista.get(i).getNumero_Compra();
                objeto[5] = lista.get(i).getTotal();
                objeto[6] = (lista.get(i).getEstado() == 0 ? "ANULADO" : "");

                //BOTON MODIFICAR emp.icono.general
                ImageIcon iconoModi = new ImageIcon(getClass().getResource("/image/EditarTabla_40px.png"));
                Icon btnModificar = new ImageIcon(iconoModi.getImage().getScaledInstance(14, 14, Image.SCALE_DEFAULT));
                JButton botonModificar = new JButton("", btnModificar);
                botonModificar.setName("btnModificar");
                botonModificar.setToolTipText("Modificar Registro");

                objeto[7] = botonModificar;
                //BOTON ANULAR
                if (lista.get(i).getEstado() == 0) {
                    objeto[8] = "";
                } else {
                    ImageIcon icono = new ImageIcon(getClass().getResource("/image/delete.png"));
                    Icon btnAnular = new ImageIcon(icono.getImage().getScaledInstance(14, 14, Image.SCALE_DEFAULT));
                    JButton botonAnular = new JButton("", btnAnular);
                    botonAnular.setName("btnAnular");
                    botonAnular.setToolTipText("Anular Compra");

                    objeto[8] = botonAnular;
                }
                //BOTON VER
                ImageIcon iconoVer = new ImageIcon(getClass().getResource("/image/VerTabla_40px.png"));
                Icon btnVer = new ImageIcon(iconoVer.getImage().getScaledInstance(14, 14, Image.SCALE_DEFAULT));
                JButton botonVer = new JButton("", btnVer);
                botonVer.setName("btnVer");
                botonVer.setToolTipText("Ver Registro");
                objeto[9] = botonVer;
                modelo.addRow(objeto);
            }
            listacompra.lblCantidadDatos.setText("Cantidad de Datos Cargados: " + lista.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    @Override
    public void Limpiar() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == listacompra.btnNuevo) {
//                try {
//                    listaCompra listacompra = new listaCompra();
//                    listaCompraController comprasCtrl = new listaCompraController(listacompra);
//                    jpload.jPanelLoader(panel_load, listacompra);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
                frmCompra frmcompra = new frmCompra(null, true);
                CompraController compraCtrl = new CompraController(frmcompra);
                compraCtrl.Iniciar();
                
                frmcompra.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

//        try {
//            if (e.getSource() == listacompra.btnNuevo) {
//                crud = 0;
//                Habilita(false);
//                Limpiar();
//            } else if (e.getSource() == listacompra.btnEliminar) {
//                int Fila = listacompra.jtCompra.getSelectedRow();
//                if (Fila >= 0) {
//                    int Id = Integer.parseInt(listacompra.jtCompra.getValueAt(Fila, 0).toString());
//                    compra.setIdCompra(Id);
//                    int result = JOptionPane.showConfirmDialog(null, "esta seguero? eliminar registro", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if (result == JOptionPane.YES_OPTION) {
//                        if (compraDao.Eliminar(compra)) {
//                            modelo.removeRow(Fila);
//                            JOptionPane.showMessageDialog(null, "Compra Eliminado");
//                            crud = 0;
//                            Habilita(false);
//                            Limpiar();
//
//                        } else {
//                            JOptionPane.showMessageDialog(null, "No se eliminó");
//                        }
//                    }
//                }
//
//            } else if (e.getSource() == listacompra.btnEditar) {
//                crud = 1;
//                Limpiar();
//                int Fila = listacompra.jtCompra.getSelectedRow();
//                if (Fila >= 0) {
//
//                    compra.setIdCompra(Integer.parseInt(listacompra.jtCompra.getValueAt(Fila, 0).toString()));
//                    compra = compraDao.Trae(compra);
//                    listacompra.txtCodigo.setText(String.valueOf(compra.getIdCompra()));
//                    listacompra.cbxTipoIdentidad.setSelectedItem(compra.getTipoIdentidad());
//
//                    listacompra.txtnumeroIdentidad.setText(compra.getNumeroIdentidad());
//                    listacompra.txtNombres.setText(compra.getNombres());
//                    listacompra.txtDireccion.setText(compra.getDireccion());
//                    listacompra.txtTelefono.setText(compra.getTelefono());
//                    listacompra.txtEmail.setText(compra.getEmail());
//                    listacompra.txtnombreComerial.setText(compra.getNombreComercial());
//                    listacompra.txtRL.setText(compra.getRepresentanteLegal());
//                    Habilita(true);
//
//                    listacompra.txtEmail.setEnabled(false);
//                    listacompra.btnGrabar.setEnabled(true);
//                }
//
//            } else if (e.getSource() == listacompra.btnGrabar) {
//                success = false;
//
//                if (listacompra.cbxTipoIdentidad.getSelectedIndex() == -1) {
//                    JOptionPane.showMessageDialog(null, "Tipo de identidad no Válido");
//                    return;
//                }
//
//                if (listacompra.txtnumeroIdentidad.getText().equals("")) {
//                    JOptionPane.showMessageDialog(null, "Numero no Válido");
//                    return;
//                }
//
//                if (listacompra.txtNombres.getText().equals("")) {
//                    JOptionPane.showMessageDialog(null, "Nombre no Válido");
//                    return;
//                }
//                if (listacompra.txtEmail.getText().equals("")) {
//                    JOptionPane.showMessageDialog(null, "Email no Válido");
//                    return;
//                }
//
//                if (esEmail(listacompra.txtEmail.getText())) {
//                    JOptionPane.showMessageDialog(null, "Email no Válido");
//                    return;
//                }
//
//                if (listacompra.txtDireccion.getText().equals("")) {
//                    JOptionPane.showMessageDialog(null, "Direccion no Válido");
//                    return;
//                }
//
//                compra.setTipoIdentidad(listacompra.cbxTipoIdentidad.getItemAt(listacompra.cbxTipoIdentidad.getSelectedIndex()));
//                compra.setNumeroIdentidad(listacompra.txtnumeroIdentidad.getText());
//                compra.setNombres(listacompra.txtNombres.getText());
//                compra.setDireccion(listacompra.txtDireccion.getText());
//                compra.setTelefono(listacompra.txtTelefono.getText());
//                compra.setEmail(listacompra.txtEmail.getText());
//
//                compra.setNombreComercial(listacompra.txtnombreComerial.getText());
//                compra.setRepresentanteLegal(listacompra.txtRL.getText());
//
//                persona = new Persona(compra);
//
//                if (crud == 0) {
//                    if (compraDao.existeNroIdentidad(compra.getNumeroIdentidad())) {
//                        JOptionPane.showMessageDialog(null, "El Compra ya existe");
//                        return;
//                    }
//
//                    if (persona.getIdPersona() == 0) {
//                        persona.setIdPersona(perDao.creaId(persona));
//                    }
//
//                    if (persona.getIdPersona() > 0) {
//                        compra.setIdPersona(persona.getIdPersona());
//                        compra.setIdCompra(compraDao.creaId(compra));
//                    }
//
//                    if (compra.getIdCompra() > 0) {
//                        Object[] fila = new Object[6];
//                        fila[0] = compra.getIdCompra();
//                        fila[1] = persona.getNumeroIdentidad();
//                        fila[2] = persona.getNombres();
//                        fila[3] = compra.getDireccion();
//                        fila[4] = compra.getTelefono();
//                        fila[5] = compra.getEmail();
//
//                        modelo.addRow(fila);
//                        success = true;
//                    }
//
//                } else if (crud == 1) {
//                    if (persona.getIdPersona() > 0) {
//                        if (perDao.Modificar(persona)) {
//                            if (compraDao.Modificar(compra)) {
//                                success = true;
//                                int Fila = listacompra.jtCompra.getSelectedRow();
//                                modelo.setValueAt(persona.getNombres(), Fila, 2);
//                                modelo.setValueAt(persona.getDireccion(), Fila, 3);
//                                modelo.setValueAt(persona.getTelefono(), Fila, 4);
//                            }
//                        }
//                    }
//
//                }
//                if (success) {
//                    crud = 1;
//                    JOptionPane.showMessageDialog(null, "Registro Guardado");
//                } else {
//                    JOptionPane.showMessageDialog(null, "No se guardadó", "Advertencia", JOptionPane.WARNING_MESSAGE);
//
//                }
//
//            }
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }

    @Override
    public void keyTyped(KeyEvent e
    ) {

    }

    @Override
    public void keyPressed(KeyEvent e
    ) {
//        if (e.getSource() == listacompra.txtnumeroIdentidad) {
//
//            if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
//                listacompra.txtnumeroIdentidad.setEditable(true);
//            } else {
//                listacompra.txtnumeroIdentidad.setEditable(false);
//            }
//            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                String NroIdentidad = listacompra.txtnumeroIdentidad.getText();
//                //compra = new Compra();
//                try {
//                    compra = new Compra(perDao.traeNroIdentidad(NroIdentidad));
//                    //persona = perDao.traeNroIdentidad(NroIdentidad);
//
//                    if (compra.getIdPersona() > 0) {
//                        listacompra.cbxTipoIdentidad.setSelectedItem(compra.getTipoIdentidad());
//                        listacompra.txtnumeroIdentidad.setText(NroIdentidad);
//                        listacompra.txtNombres.setText(compra.getNombres());
//                        listacompra.txtDireccion.setText(compra.getDireccion());
//                        listacompra.txtTelefono.setText(compra.getTelefono());
//                        listacompra.txtEmail.setText(compra.getEmail());
//                        Habilita(true);
//
//                        listacompra.cbxTipoIdentidad.setEnabled(false);
//                        listacompra.txtnumeroIdentidad.setEnabled(false);
//                        listacompra.txtNombres.setEnabled(false);
//                        listacompra.txtDireccion.setEnabled(false);
//                        listacompra.txtTelefono.setEnabled(false);
//                        listacompra.txtEmail.setEnabled(false);
//
//                        listacompra.btnGrabar.setEnabled(true);
//
//                    } else {
//                        Habilita(true);
//                        listacompra.btnGrabar.setEnabled(true);
//                    }
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(null, ex.getMessage(),
//                            "Error", JOptionPane.ERROR_MESSAGE);
//
//                }
//            }
//        }
    }

    @Override
    public void keyReleased(KeyEvent e
    ) {

    }

    @Override
    public void mouseClicked(MouseEvent e
    ) {
        if (e.getSource() == listacompra.tblDetalle) {
            int Fila = listacompra.tblDetalle.getSelectedRow();
            int id = Integer.parseInt(listacompra.tblDetalle.getValueAt(Fila, 0).toString());
            //lblIdCargos.setText(id);

            int colum = listacompra.tblDetalle.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / listacompra.tblDetalle.getRowHeight();

            if (row < listacompra.tblDetalle.getRowCount() && row >= 0 && colum < listacompra.tblDetalle.getColumnCount() && colum >= 0) {
                Object value = listacompra.tblDetalle.getValueAt(row, colum);
                if (value instanceof JButton) {
                    ((JButton) value).doClick();
                    JButton boton = (JButton) value;

                    if (boton.getName().equals("btnAnular")) {
                        var Filas = listacompra.tblDetalle.getSelectedRowCount();
                        if (Filas == 0) {//si no elije ninguna fila
                            JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        } else {//cuando si seleciona
                            String valor = String.valueOf(listacompra.tblDetalle.getValueAt(Fila, 4));
                            int opcion = JOptionPane.showConfirmDialog(null, "Realmente desea anular la Compra " + valor + "?", "Mensaje", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if (opcion == JOptionPane.OK_OPTION) {
                                compra.setIdCompra(id);
                                compra.setEstado(0);
                                System.out.println(compra.toString());
                                try {
                                    compraDao.cambiarEstado(compra);
                                    modelo.removeRow(Fila);
                                    JOptionPane.showMessageDialog(null, "Eliminación Exitosa", "Confirmación", JOptionPane.INFORMATION_MESSAGE);

                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                                            "Error", JOptionPane.ERROR_MESSAGE);

                                }

                            }
                        }
                    } else if (boton.getName().equals("btnModificar")) {
                        int filas = listacompra.tblDetalle.getSelectedRowCount();
                        if (filas == 0) {//si no elije ninguna fila
                            JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        } else {//cuando si seleciona
                            System.out.println("Modificando");

//                        int id_categoria = Integer.parseInt(lblIdCargos.getText());
//                        new Form_Categoria(this, true, id_categoria, 0, id_usuario, id_sede).setVisible(true);
//                        mostrar_Tabla();
//                        Limpiar_Tabla();
//                        lista_ObProveedorDato();
                        }
                    } else if (boton.getName().equals("btnVer")) {
                        int filas = listacompra.tblDetalle.getSelectedRowCount();
                        if (filas == 0) {//si no elije ninguna fila
                            JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        } else {//cuando si seleciona

//                        int id_categoria = Integer.parseInt(lblIdCargos.getText());
//                        new Form_Categoria(this, true, id_categoria, 1, id_usuario, id_sede).setVisible(true);
//                        mostrar_Tabla();
//                        Limpiar_Tabla();
//                        lista_ObProveedorDato();
                        }
                    }
                }
            }

        }

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
