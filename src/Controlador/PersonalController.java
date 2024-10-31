package Controlador;

import Dao.PersonalDao;
import Dao.PersonaDao;
import Dao.TipoIdentidadDao;
import Interface.Control;
import Vista.frmPersonal;
import Modelo.Personal;
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
public class PersonalController implements Control, ActionListener, KeyListener, MouseListener {

    private Personal personal;
    private Persona persona;
    private PersonalDao personalDao;
    private PersonaDao perDao;
    private TipoIdentidadDao tipoDao;
    private frmPersonal frmpersonal;
    private int crud;
    private boolean success;
    List<TipoIdentidad> listaTipo;
    DefaultTableModel modelo = new DefaultTableModel();

    public PersonalController(frmPersonal frmpersonal) {
        this.personal = new Personal();
        this.persona = null;
        this.frmpersonal = frmpersonal;
        this.personalDao = new PersonalDao();
        this.perDao = new PersonaDao();
        this.tipoDao = new TipoIdentidadDao();

        this.frmpersonal.btnGrabar.addActionListener(this);
        this.frmpersonal.btnNuevo.addActionListener(this);
        this.frmpersonal.btnEditar.addActionListener(this);
        this.frmpersonal.btnEliminar.addActionListener(this);
        this.frmpersonal.btnDeshabilita.addActionListener(this);
        this.frmpersonal.txtnumeroIdentidad.addKeyListener(this);

        llenarTipoEntidad(this.frmpersonal.cbxTipoIdentidad);
        llenarTabla(frmpersonal.jtPersonal);

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
        
    }

    @Override
    public void Habilita(Boolean t) {
        frmpersonal.txtNombres.setEnabled(t);
        frmpersonal.cbxTipoIdentidad.setEnabled(!t);
        frmpersonal.txtnumeroIdentidad.setEnabled(!t);
        frmpersonal.txtDireccion.setEnabled(t);
        frmpersonal.txtTelefono.setEnabled(t);
        frmpersonal.txtEmail.setEnabled(t);
        frmpersonal.txtCargo.setEnabled(t);
        frmpersonal.txtSueldo.setEnabled(t);
    }

    @Override
    public void llenarTabla(JTable jt) {

        modelo = (DefaultTableModel) jt.getModel();
        jt.setModel(modelo);
        try {

            List<Personal> lista = personalDao.Listar();
            Object[] objeto = new Object[7];
            for (int i = 0; i < lista.size(); i++) {
                objeto[0] = lista.get(i).getIdPersonal();
                objeto[1] = lista.get(i).getNumeroIdentidad();
                objeto[2] = lista.get(i).getNombres();
                objeto[3] = lista.get(i).getDireccion();
                objeto[4] = lista.get(i).getTelefono();
                objeto[5] = lista.get(i).getEmail();
                objeto[6] = (lista.get(i).getEstado()==0)?"DESHABILITADO":"ACTIVO";
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
        frmpersonal.txtCodigo.setText("Autogenerado");
        frmpersonal.txtNombres.setText("");
        //frmpersonal.cbxTipoIdentidad.setEnabled(t);
        frmpersonal.txtnumeroIdentidad.setText("");
        frmpersonal.txtDireccion.setText("");
        frmpersonal.txtTelefono.setText("");
        frmpersonal.txtEmail.setText("");
        frmpersonal.txtCargo.setText("");
        frmpersonal.txtSueldo.setText("");
        frmpersonal.txtEstado.setText("");

        personal.setIdPersonal(0);
        personal.setIdPersona(0);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == frmpersonal.btnNuevo) {
                crud = 0;
                Habilita(false);
                Limpiar();
            } else if (e.getSource() == frmpersonal.btnDeshabilita) {
                int Fila = frmpersonal.jtPersonal.getSelectedRow();
                if (Fila >= 0) {
                    int Id = Integer.parseInt(frmpersonal.jtPersonal.getValueAt(Fila, 0).toString());
                    personal.setIdPersonal(Id);
                    personal.setEstado(0);
                }
                if (personal.getIdPersonal() > 0) {
                    if (personalDao.cambiarEstado(personal)) {
                        success = true;
                        //int Fila = frmpersonal.jtPersonal.getSelectedRow();
                        modelo.setValueAt("DESHABILITADO", Fila, 7);
                    }
                }

            } else if (e.getSource() == frmpersonal.btnEliminar) {
                int Fila = frmpersonal.jtPersonal.getSelectedRow();
                if (Fila >= 0) {
                    int Id = Integer.parseInt(frmpersonal.jtPersonal.getValueAt(Fila, 0).toString());
                    personal.setIdPersonal(Id);
                    int result = JOptionPane.showConfirmDialog(null, "esta seguero? eliminar registro", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        if (personalDao.Eliminar(personal)) {
                            modelo.removeRow(Fila);
                            JOptionPane.showMessageDialog(null, "Personal Eliminado");
                            crud = 0;
                            Habilita(false);
                            Limpiar();

                        } else {
                            JOptionPane.showMessageDialog(null, "No se eliminó");
                        }
                    }
                }

            } else if (e.getSource() == frmpersonal.btnEditar) {
                crud = 1;
                Limpiar();
                int Fila = frmpersonal.jtPersonal.getSelectedRow();
                if (Fila >= 0) {

                    personal.setIdPersonal(Integer.parseInt(frmpersonal.jtPersonal.getValueAt(Fila, 0).toString()));
                    personal = personalDao.Trae(personal);
                    frmpersonal.txtCodigo.setText(String.valueOf(personal.getIdPersonal()));
                    frmpersonal.cbxTipoIdentidad.setSelectedItem(personal.getTipoIdentidad());

                    frmpersonal.txtnumeroIdentidad.setText(personal.getNumeroIdentidad());
                    frmpersonal.txtNombres.setText(personal.getNombres());
                    frmpersonal.txtDireccion.setText(personal.getDireccion());
                    frmpersonal.txtTelefono.setText(personal.getTelefono());
                    frmpersonal.txtEmail.setText(personal.getEmail());

                    frmpersonal.txtCargo.setText(personal.getCargo());
                    frmpersonal.txtSueldo.setText(String.valueOf(personal.getSueldo()));
                    frmpersonal.txtEstado.setText((personal.getEstado()==0)?"DESHABILITADO":"ACTIVO");

                    Habilita(true);

                    frmpersonal.txtEmail.setEnabled(false);
                    frmpersonal.btnGrabar.setEnabled(true);
                }

            } else if (e.getSource() == frmpersonal.btnGrabar) {
                success = false;

                if (frmpersonal.cbxTipoIdentidad.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(null, "Tipo de identidad no Válido");
                    return;
                }

                if (frmpersonal.txtnumeroIdentidad.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Numero no Válido");
                    return;
                }

                if (frmpersonal.txtNombres.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Nombre no Válido");
                    return;
                }
                if (frmpersonal.txtEmail.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Email no Válido");
                    return;
                }

                if (esEmail(frmpersonal.txtEmail.getText())) {
                    JOptionPane.showMessageDialog(null, "Email no Válido");
                    return;
                }

                if (frmpersonal.txtDireccion.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Direccion no Válido");
                    return;
                }

                personal.setTipoIdentidad(frmpersonal.cbxTipoIdentidad.getItemAt(frmpersonal.cbxTipoIdentidad.getSelectedIndex()));
                personal.setNumeroIdentidad(frmpersonal.txtnumeroIdentidad.getText());
                personal.setNombres(frmpersonal.txtNombres.getText());
                personal.setDireccion(frmpersonal.txtDireccion.getText());
                personal.setTelefono(frmpersonal.txtTelefono.getText());
                personal.setEmail(frmpersonal.txtEmail.getText());

                personal.setCargo(frmpersonal.txtCargo.getText());
                personal.setSueldo(Double.parseDouble(frmpersonal.txtSueldo.getText()));

                persona = new Persona(personal);

                if (crud == 0) {
                    if (personalDao.existeNroIdentidad(personal.getNumeroIdentidad())) {
                        JOptionPane.showMessageDialog(null, "El Personal ya existe");
                        return;
                    }

                    if (persona.getIdPersona() == 0) {
                        persona.setIdPersona(perDao.creaId(persona));
                    }

                    if (persona.getIdPersona() > 0) {
                        personal.setIdPersona(persona.getIdPersona());
                        personal.setEstado(1);
                        personal.setIdPersonal(personalDao.creaId(personal));
                    }

                    if (personal.getIdPersonal() > 0) {
                        Object[] fila = new Object[7];
                        fila[0] = personal.getIdPersonal();
                        fila[1] = persona.getNumeroIdentidad();
                        fila[2] = persona.getNombres();
                        fila[3] = personal.getDireccion();
                        fila[4] = personal.getTelefono();
                        fila[5] = personal.getEmail();
                        fila[6] = (personal.getEstado()==0)?"DESHABILITADO":"";

                        modelo.addRow(fila);
                        success = true;
                    }

                } else if (crud == 1) {
                    if (persona.getIdPersona() > 0) {
                        if (perDao.Modificar(persona)) {
                            if (personalDao.Modificar(personal)) {
                                success = true;
                                int Fila = frmpersonal.jtPersonal.getSelectedRow();
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
        if (e.getSource() == frmpersonal.txtnumeroIdentidad) {

            if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                frmpersonal.txtnumeroIdentidad.setEditable(true);
            } else {
                frmpersonal.txtnumeroIdentidad.setEditable(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String NroIdentidad = frmpersonal.txtnumeroIdentidad.getText();
                //personal = new Personal();
                try {
                    personal = new Personal(perDao.traeNroIdentidad(NroIdentidad));
                    //persona = perDao.traeNroIdentidad(NroIdentidad);

                    if (personal.getIdPersona() > 0) {
                        frmpersonal.cbxTipoIdentidad.setSelectedItem(personal.getTipoIdentidad());
                        frmpersonal.txtnumeroIdentidad.setText(NroIdentidad);
                        frmpersonal.txtNombres.setText(personal.getNombres());
                        frmpersonal.txtDireccion.setText(personal.getDireccion());
                        frmpersonal.txtTelefono.setText(personal.getTelefono());
                        frmpersonal.txtEmail.setText(personal.getEmail());
                        Habilita(true);

                        frmpersonal.cbxTipoIdentidad.setEnabled(false);
                        frmpersonal.txtnumeroIdentidad.setEnabled(false);
                        frmpersonal.txtNombres.setEnabled(false);
                        frmpersonal.txtDireccion.setEnabled(false);
                        frmpersonal.txtTelefono.setEnabled(false);
                        frmpersonal.txtEmail.setEnabled(false);

                        frmpersonal.btnGrabar.setEnabled(true);

                    } else {
                        Habilita(true);
                        frmpersonal.btnGrabar.setEnabled(true);
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

    @Override
    public void mouseExited(MouseEvent e
    ) {

    }

}
