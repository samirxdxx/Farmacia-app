package Controlador;

import Dao.VentaDao;
import Dao.PersonaDao;
import Dao.TipoIdentidadDao;
import Interface.Control;
import Modelo.Cliente;
import Vista.listaVenta;
import Modelo.Venta;
import Modelo.Persona;
import Modelo.TipoIdentidad;
import Utils.Colorear_Tablas;
import Utils.ImgTabla;
import Utils.Validator;
import Vista.frmCliente;
import Vista.modal.frmVenta;
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
public class listaVentaController implements Control, ActionListener, KeyListener, MouseListener {

    private Venta venta;
    private Persona persona;
    private VentaDao ventaDao;
    private PersonaDao perDao;
    private TipoIdentidadDao tipoDao;
    private listaVenta listaventa;
    private int crud;
    private boolean success;
    List<TipoIdentidad> listaTipo;
    DefaultTableModel modelo = new DefaultTableModel();

    public listaVentaController(listaVenta listaventa) {
        this.venta = new Venta();
        this.persona = null;
        this.listaventa = listaventa;
        this.ventaDao = new VentaDao();
        this.perDao = new PersonaDao();
        this.tipoDao = new TipoIdentidadDao();

        this.listaventa.btnNuevo.addActionListener(this);
        this.listaventa.btnBuscar.addActionListener(this);
        this.listaventa.tblDetalle.addMouseListener(this);

        this.listaventa.tblDetalle.setDefaultRenderer(Object.class, new ImgTabla());

        mostrar_Tabla();
        //llenarTipoEntidad(this.listaventa.cbxTipoIdentidad);
        llenarTabla(listaventa.tblDetalle);

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
        modelo.addColumn("DOC CLIENTE");
        modelo.addColumn("DOC VENTA");
        modelo.addColumn("TOTAL");
        modelo.addColumn("ESTADO");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        listaventa.tblDetalle.setRowHeight(21);
        listaventa.tblDetalle.setModel(modelo);
        listaventa.tblDetalle.setBackground(Color.WHITE);
        listaventa.tblDetalle.setAutoResizeMode(listaventa.tblDetalle.AUTO_RESIZE_OFF);

        listaventa.tblDetalle.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaventa.tblDetalle.getColumnModel().getColumn(1).setPreferredWidth(100);
        listaventa.tblDetalle.getColumnModel().getColumn(2).setPreferredWidth(250);
        listaventa.tblDetalle.getColumnModel().getColumn(3).setPreferredWidth(100);
        listaventa.tblDetalle.getColumnModel().getColumn(4).setPreferredWidth(100);
        listaventa.tblDetalle.getColumnModel().getColumn(5).setPreferredWidth(80);
        listaventa.tblDetalle.getColumnModel().getColumn(6).setPreferredWidth(80);
        listaventa.tblDetalle.getColumnModel().getColumn(7).setPreferredWidth(28);
        listaventa.tblDetalle.getColumnModel().getColumn(8).setPreferredWidth(28);
        listaventa.tblDetalle.getColumnModel().getColumn(9).setPreferredWidth(28);
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
        //listaventa.setLocationRelativeTo(null);
    }

    @Override
    public void Habilita(Boolean t) {
    }

    @Override
    public void llenarTabla(JTable jt) {
        //modelo = (DefaultTableModel) jt.getModel();
        //jt.setModel(modelo);
        try {

            List<Venta> lista = ventaDao.Listar();
            System.out.println("data:" + lista.size());

            Object[] objeto = new Object[10];
            for (int i = 0; i < lista.size(); i++) {

                objeto[0] = lista.get(i).getIdVenta();
                objeto[1] = lista.get(i).getFecha_Venta();
                objeto[2] = lista.get(i).getCliente().getNombres();
                objeto[3] = lista.get(i).getCliente().getNumeroIdentidad();
                objeto[4] = lista.get(i).getNumero_Venta();
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
                    botonAnular.setToolTipText("Anular Venta");

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
            listaventa.lblCantidadDatos.setText("Cantidad de Datos Cargados: " + lista.size());
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
            if (e.getSource() == listaventa.btnNuevo) {

                frmVenta frmventa = new frmVenta(null, true);
                VentaController ventaCtrl = new VentaController(frmventa);
                ventaCtrl.Iniciar();

                frmventa.setVisible(true);
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

    }

    @Override
    public void mouseClicked(MouseEvent e
    ) {
        if (e.getSource() == listaventa.tblDetalle) {
            int Fila = listaventa.tblDetalle.getSelectedRow();
            int id = Integer.parseInt(listaventa.tblDetalle.getValueAt(Fila, 0).toString());
            //lblIdCargos.setText(id);

            int colum = listaventa.tblDetalle.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / listaventa.tblDetalle.getRowHeight();

            if (row < listaventa.tblDetalle.getRowCount() && row >= 0 && colum < listaventa.tblDetalle.getColumnCount() && colum >= 0) {
                Object value = listaventa.tblDetalle.getValueAt(row, colum);
                if (value instanceof JButton) {
                    ((JButton) value).doClick();
                    JButton boton = (JButton) value;

                    if (boton.getName().equals("btnAnular")) {
                        var Filas = listaventa.tblDetalle.getSelectedRowCount();
                        if (Filas == 0) {//si no elije ninguna fila
                            JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        } else {//cuando si seleciona
                            String valor = String.valueOf(listaventa.tblDetalle.getValueAt(Fila, 4));
                            int opcion = JOptionPane.showConfirmDialog(null, "Realmente desea anular la Venta " + valor + "?", "Mensaje", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if (opcion == JOptionPane.OK_OPTION) {
                                venta.setIdVenta(id);
                                venta.setEstado(0);
                                System.out.println(venta.toString());
                                try {
                                    ventaDao.cambiarEstado(venta);
                                    modelo.removeRow(Fila);
                                    JOptionPane.showMessageDialog(null, "Eliminación Exitosa", "Confirmación", JOptionPane.INFORMATION_MESSAGE);

                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                                            "Error", JOptionPane.ERROR_MESSAGE);

                                }

                            }
                        }
                    } else if (boton.getName().equals("btnModificar")) {
                        int filas = listaventa.tblDetalle.getSelectedRowCount();
                        if (filas == 0) {//si no elije ninguna fila
                            JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        } else {//cuando si seleciona
                            System.out.println("Modificando");

//                        int id_categoria = Integer.parseInt(lblIdCargos.getText());
//                        new Form_Categoria(this, true, id_categoria, 0, id_usuario, id_sede).setVisible(true);
//                        mostrar_Tabla();
//                        Limpiar_Tabla();
//                        lista_ObClienteDato();
                        }
                    } else if (boton.getName().equals("btnVer")) {
                        int filas = listaventa.tblDetalle.getSelectedRowCount();
                        if (filas == 0) {//si no elije ninguna fila
                            JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                        } else {//cuando si seleciona

//                        int id_categoria = Integer.parseInt(lblIdCargos.getText());
//                        new Form_Categoria(this, true, id_categoria, 1, id_usuario, id_sede).setVisible(true);
//                        mostrar_Tabla();
//                        Limpiar_Tabla();
//                        lista_ObClienteDato();
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
