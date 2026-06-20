package vista;

import Controlador.ControladorCliente;
import modelo.Cliente;
import javax.swing.*;
import java.awt.*;

public class DialogCliente extends JDialog {
    private JTextField txtNombre, txtApellido, txtDocumento, txtCorreo, txtTelefono, txtDireccion, txtIdUsuario;
    private int idClienteToEdit = -1;
    private Runnable onSaved;

    public DialogCliente(Window owner, int idClienteToEdit, Runnable onSaved) {
        super(owner, idClienteToEdit == -1 ? "Crear Cliente" : "Editar Cliente", ModalityType.APPLICATION_MODAL);
        this.idClienteToEdit = idClienteToEdit;
        this.onSaved = onSaved;

        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panel.add(txtApellido);

        panel.add(new JLabel("Documento:"));
        txtDocumento = new JTextField();
        panel.add(txtDocumento);

        panel.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panel.add(txtCorreo);

        panel.add(new JLabel("Telefono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);

        panel.add(new JLabel("Direccion:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);

        panel.add(new JLabel("ID Usuario (Opcional):"));
        txtIdUsuario = new JTextField("0");
        panel.add(txtIdUsuario);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        add(btnPanel, BorderLayout.SOUTH);

        if (idClienteToEdit != -1) {
            cargarDatos();
        }
    }

    private void cargarDatos() {
        ControladorCliente ctrl = new ControladorCliente();
        Cliente c = ctrl.buscarClientePorId(idClienteToEdit);
        if (c != null && !c.getNombre().contains("no existe")) {
            txtNombre.setText(c.getNombre());
            txtApellido.setText(c.getApellido());
            txtDocumento.setText(String.valueOf(c.getDocumento()));
            txtCorreo.setText(c.getCorreo());
            txtTelefono.setText(String.valueOf(c.getTelefono()));
            txtDireccion.setText(c.getDireccion());
            txtIdUsuario.setText(String.valueOf(c.getIdUsuario()));
        }
    }

    private void guardar() {
        try {
            ControladorCliente ctrl = new ControladorCliente();
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            long documento = Long.parseLong(txtDocumento.getText());
            String correo = txtCorreo.getText();
            long telefono = Long.parseLong(txtTelefono.getText());
            String direccion = txtDireccion.getText();
            int idUsuario = Integer.parseInt(txtIdUsuario.getText());

            if (idClienteToEdit == -1) {
                ctrl.insertarCliente(nombre, apellido, documento, correo, telefono, direccion, idUsuario);
            } else {
                ctrl.modificarCliente(idClienteToEdit, nombre, apellido, documento, correo, telefono, direccion, idUsuario);
            }
            if (onSaved != null) onSaved.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor verifique que Documento, Telefono y ID Usuario sean numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
