package Controlador;

import Dao.ClienteDao;
import Dao.PersonaDao;
import Dao.TipoIdentidadDao;
import Interface.Control;
import Vista.frmCliente;
import Modelo.Cliente;
import Modelo.Persona;
import Modelo.TipoIdentidad;
import Utils.Validator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eder
 */
public class ClienteController implements Control, ActionListener, KeyListener, MouseListener {

    private Cliente cliente;
    private Persona persona;
    private ClienteDao cliDao;
    private PersonaDao perDao;
    private TipoIdentidadDao tipoDao;
    private frmCliente frmcliente;
    private int crud;
    private boolean success;
    List<TipoIdentidad> listaTipo;
    DefaultTableModel modelo = new DefaultTableModel();

    public ClienteController(frmCliente frmcliente) {
        this.cliente = new Cliente();
        this.persona = null;
        this.frmcliente = frmcliente;
        this.cliDao = new ClienteDao();
        this.perDao = new PersonaDao();
        this.tipoDao = new TipoIdentidadDao();

        this.frmcliente.btnGrabar.addActionListener(this);
        this.frmcliente.btnNuevo.addActionListener(this);
        this.frmcliente.btnEditar.addActionListener(this);
        this.frmcliente.btnEliminar.addActionListener(this);
        this.frmcliente.txtnumeroIdentidad.addKeyListener(this);

        llenarTipoEntidad(this.frmcliente.cbxTipoIdentidad);
        llenarTabla(frmcliente.jtCliente);

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

    public boolean esEmail(String email) {
        Validator emailvalidador = new Validator();
        return emailvalidador.validate(email);
    }

    @Override
    public void Iniciar() {
        //frmcliente.setLocationRelativeTo(null);
    }

    @Override
    public void Habilita(Boolean t) {
        frmcliente.txtNombres.setEnabled(t);
        frmcliente.cbxTipoIdentidad.setEnabled(!t);
        frmcliente.txtnumeroIdentidad.setEnabled(!t);
        frmcliente.txtDireccion.setEnabled(t);
        frmcliente.txtTelefono.setEnabled(t);
        frmcliente.txtEmail.setEnabled(t);
        frmcliente.txtnombreComerial.setEnabled(t);
        frmcliente.txtRL.setEnabled(t);
    }

    @Override
    public void llenarTabla(JTable jt) {

        modelo = (DefaultTableModel) jt.getModel();
        jt.setModel(modelo);
        try {

            List<Cliente> lista = cliDao.Listar();
            Object[] objeto = new Object[6];
            for (int i = 0; i < lista.size(); i++) {
                objeto[0] = lista.get(i).getIdCliente();
                objeto[1] = lista.get(i).getNumeroIdentidad();
                objeto[2] = lista.get(i).getNombres();
                objeto[3] = lista.get(i).getDireccion();
                objeto[4] = lista.get(i).getTelefono();
                objeto[5] = lista.get(i).getEmail();
                modelo.addRow(objeto);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        jt.setRowHeight(20);
        jt.setRowMargin(5);

    }

    @Override
    public void Limpiar() {
        frmcliente.txtCodigo.setText("Autogenerado");
        frmcliente.txtNombres.setText("");
        //frmcliente.cbxTipoIdentidad.setEnabled(t);
        frmcliente.txtnumeroIdentidad.setText("");
        frmcliente.txtDireccion.setText("");
        frmcliente.txtTelefono.setText("");
        frmcliente.txtEmail.setText("");
        frmcliente.txtnombreComerial.setText("");
        frmcliente.txtRL.setText("");

        cliente.setIdCliente(0);
        cliente.setIdPersona(0);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == frmcliente.btnNuevo) {
                crud = 0;
                Habilita(false);
                Limpiar();
            } else if (e.getSource() == frmcliente.btnEliminar) {
                int Fila = frmcliente.jtCliente.getSelectedRow();
                if (Fila >= 0) {
                    int Id = Integer.parseInt(frmcliente.jtCliente.getValueAt(Fila, 0).toString());
                    cliente.setIdCliente(Id);
                    int result = JOptionPane.showConfirmDialog(null, "esta seguero? eliminar registro", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        if (cliDao.Eliminar(cliente)) {
                            modelo.removeRow(Fila);
                            JOptionPane.showMessageDialog(null, "Cliente Eliminado");
                            crud = 0;
                            Habilita(false);
                            Limpiar();

                        } else {
                            JOptionPane.showMessageDialog(null, "No se eliminó");
                        }
                    }
                }

            } else if (e.getSource() == frmcliente.btnEditar) {
                crud = 1;
                Limpiar();
                int Fila = frmcliente.jtCliente.getSelectedRow();
                if (Fila >= 0) {

                    cliente.setIdCliente(Integer.parseInt(frmcliente.jtCliente.getValueAt(Fila, 0).toString()));
                    cliente = cliDao.Trae(cliente);
                    frmcliente.txtCodigo.setText(String.valueOf(cliente.getIdCliente()));
                    frmcliente.cbxTipoIdentidad.setSelectedItem(cliente.getTipoIdentidad());

                    frmcliente.txtnumeroIdentidad.setText(cliente.getNumeroIdentidad());
                    frmcliente.txtNombres.setText(cliente.getNombres());
                    frmcliente.txtDireccion.setText(cliente.getDireccion());
                    frmcliente.txtTelefono.setText(cliente.getTelefono());
                    frmcliente.txtEmail.setText(cliente.getEmail());
                    frmcliente.txtnombreComerial.setText(cliente.getNombreComercial());
                    frmcliente.txtRL.setText(cliente.getRepresentanteLegal());
                    Habilita(true);

                    frmcliente.txtEmail.setEnabled(false);
                    frmcliente.btnGrabar.setEnabled(true);
                }

            } else if (e.getSource() == frmcliente.btnGrabar) {
                success = false;

                if (frmcliente.cbxTipoIdentidad.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(null, "Tipo de identidad no Válido");
                    return;
                }

                if (frmcliente.txtnumeroIdentidad.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Numero no Válido");
                    return;
                }

                if (frmcliente.txtNombres.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Nombre no Válido");
                    return;
                }
                if (frmcliente.txtEmail.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Email no Válido");
                    return;
                }

                if (esEmail(frmcliente.txtEmail.getText())) {
                    JOptionPane.showMessageDialog(null, "Email no Válido");
                    return;
                }

                if (frmcliente.txtDireccion.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Direccion no Válido");
                    return;
                }

                cliente.setTipoIdentidad(frmcliente.cbxTipoIdentidad.getItemAt(frmcliente.cbxTipoIdentidad.getSelectedIndex()));
                cliente.setNumeroIdentidad(frmcliente.txtnumeroIdentidad.getText());
                cliente.setNombres(frmcliente.txtNombres.getText());
                cliente.setDireccion(frmcliente.txtDireccion.getText());
                cliente.setTelefono(frmcliente.txtTelefono.getText());
                cliente.setEmail(frmcliente.txtEmail.getText());

                cliente.setNombreComercial(frmcliente.txtnombreComerial.getText());
                cliente.setRepresentanteLegal(frmcliente.txtRL.getText());

                persona = new Persona(cliente);

                if (crud == 0) {
                    if (cliDao.existeNroIdentidad(cliente.getNumeroIdentidad())) {
                        JOptionPane.showMessageDialog(null, "El Cliente ya existe");
                        return;
                    }

                    if (persona.getIdPersona() == 0) {
                        persona.setIdPersona(perDao.creaId(persona));
                    }

                    if (persona.getIdPersona() > 0) {
                        cliente.setIdPersona(persona.getIdPersona());
                        cliente.setIdCliente(cliDao.creaId(cliente));
                    }

                    if (cliente.getIdCliente() > 0) {
                        Object[] fila = new Object[6];
                        fila[0] = cliente.getIdCliente();
                        fila[1] = persona.getNumeroIdentidad();
                        fila[2] = persona.getNombres();
                        fila[3] = cliente.getDireccion();
                        fila[4] = cliente.getTelefono();
                        fila[5] = cliente.getEmail();

                        modelo.addRow(fila);
                        success = true;
                    }

                } else if (crud == 1) {
                    if (persona.getIdPersona() > 0) {
                        if (perDao.Modificar(persona)) {
                            if (cliDao.Modificar(cliente)) {
                                success = true;
                                int Fila = frmcliente.jtCliente.getSelectedRow();
                                modelo.setValueAt(persona.getNombres(), Fila, 2);
                                modelo.setValueAt(persona.getDireccion(), Fila, 3);
                                modelo.setValueAt(persona.getTelefono(), Fila, 4);
                            }
                        }
                    }

                }
                if (success) {
                    crud = 1;
                    JOptionPane.showMessageDialog(null, "Registro Guardado");
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
        if (e.getSource() == frmcliente.txtnumeroIdentidad) {

            if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                frmcliente.txtnumeroIdentidad.setEditable(true);
            } else {
                frmcliente.txtnumeroIdentidad.setEditable(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String NroIdentidad = frmcliente.txtnumeroIdentidad.getText();
                //cliente = new Cliente();
                try {
                    cliente = new Cliente(perDao.traeNroIdentidad(NroIdentidad));
                    //persona = perDao.traeNroIdentidad(NroIdentidad);

                    if (cliente.getIdPersona() > 0) {
                        frmcliente.cbxTipoIdentidad.setSelectedItem(cliente.getTipoIdentidad());
                        frmcliente.txtnumeroIdentidad.setText(NroIdentidad);
                        frmcliente.txtNombres.setText(cliente.getNombres());
                        frmcliente.txtDireccion.setText(cliente.getDireccion());
                        frmcliente.txtTelefono.setText(cliente.getTelefono());
                        frmcliente.txtEmail.setText(cliente.getEmail());
                        Habilita(true);

                        frmcliente.cbxTipoIdentidad.setEnabled(false);
                        frmcliente.txtnumeroIdentidad.setEnabled(false);
                        frmcliente.txtNombres.setEnabled(false);
                        frmcliente.txtDireccion.setEnabled(false);
                        frmcliente.txtTelefono.setEnabled(false);
                        frmcliente.txtEmail.setEnabled(false);

                        frmcliente.btnGrabar.setEnabled(true);

                    } else {
                        Habilita(true);
                        frmcliente.btnGrabar.setEnabled(true);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e
    ) {

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
  System.out.printIn ("HOla a todos");
    @Override
    public void mouseExited(MouseEvent e
    ) {

    }

}
