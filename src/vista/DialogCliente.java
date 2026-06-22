package vista;

import Controlador.ControladorCliente;
import modelo.Cliente;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogCliente extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);

    private JTextField txtNombre, txtApellido, txtDocumento, txtCorreo, txtTelefono, txtDireccion, txtIdUsuario;
    private int idClienteToEdit = -1;
    private Runnable onSaved;

    public DialogCliente(Window owner, int idClienteToEdit, Runnable onSaved) {
        super(owner, idClienteToEdit == -1 ? "Crear Cliente" : "Editar Cliente", ModalityType.APPLICATION_MODAL);
        this.idClienteToEdit = idClienteToEdit;
        this.onSaved = onSaved;

        setSize(540, 480);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel header = new JPanel();
        header.setBackground(DARK_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, GOLD));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 54));

        JLabel headerTitle = new JLabel("  " + (idClienteToEdit == -1 ? "NUEVO CLIENTE" : "EDITAR CLIENTE"));
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerTitle.setForeground(GOLD);
        header.add(headerTitle, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 24, 12, 24));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);

        txtNombre = createField(); txtApellido = createField(); txtDocumento = createField();
        txtCorreo = createField(); txtTelefono = createField(); txtDireccion = createField(); txtIdUsuario = createField();

        addRow(panel, gbc, 0, "Nombre:", txtNombre);
        addRow(panel, gbc, 1, "Apellido:", txtApellido);
        addRow(panel, gbc, 2, "Documento:", txtDocumento);
        addRow(panel, gbc, 3, "Correo:", txtCorreo);
        addRow(panel, gbc, 4, "Tel\u00e9fono:", txtTelefono);
        addRow(panel, gbc, 5, "Direcci\u00f3n:", txtDireccion);
        addRow(panel, gbc, 6, "ID Usuario:", txtIdUsuario);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 0, 16, 24));

        JButton btnCancelar = createButton("Cancelar", false);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = createButton("Guardar", true);
        btnGuardar.addActionListener(e -> guardar());

        btnPanel.add(btnCancelar);
        btnPanel.add(btnGuardar);
        add(btnPanel, BorderLayout.SOUTH);

        if (idClienteToEdit != -1) {
            cargarDatos();
        }
    }

    private JTextField createField() {
        JTextField f = new JTextField(18);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBackground(FIELD_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JButton createButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (primary) {
            btn.setBackground(GOLD);
            btn.setForeground(DARK_BG);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(107, 114, 128));
            btn.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        }
        return btn;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_BG);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
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
