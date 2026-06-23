package vista;

import Controlador.ControladorUsuario;
import modelo.Login;
import util.ToastNotifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogUsuario extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);

    private JTextField txtNombre;
    private JPasswordField txtContrasena;
    private JComboBox<String> cmbRol;
    private int idToEdit = -1;
    private Runnable onSaved;

    public DialogUsuario(Window owner, int idToEdit, Runnable onSaved) {
        super(owner, idToEdit == -1 ? "Crear Usuario" : "Editar Usuario", ModalityType.APPLICATION_MODAL);
        this.idToEdit = idToEdit;
        this.onSaved = onSaved;

        setSize(480, 380);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel header = new JPanel();
        header.setBackground(DARK_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, GOLD));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 54));

        JLabel headerTitle = new JLabel("  " + (idToEdit == -1 ? "NUEVO USUARIO" : "EDITAR USUARIO"));
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

        txtNombre = new JTextField(18);
        txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtNombre.setBackground(FIELD_BG);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                new EmptyBorder(6, 10, 6, 10)));

        txtContrasena = new JPasswordField(18);
        txtContrasena.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtContrasena.setBackground(FIELD_BG);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                new EmptyBorder(6, 10, 6, 10)));

        cmbRol = new JComboBox<>(new String[]{"administrador", "recepcionista", "cliente"});
        cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbRol.setBackground(FIELD_BG);

        addRow(panel, gbc, 0, "Usuario:", txtNombre);
        addRow(panel, gbc, 1, "Contrase\u00f1a:", txtContrasena);
        addRowCombo(panel, gbc, 2, "Rol:", cmbRol);

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

        if (idToEdit != -1) {
            cargarDatos();
        }
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

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
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

    private void addRowCombo(JPanel panel, GridBagConstraints gbc, int row, String label, JComboBox<?> combo) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_BG);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(combo, gbc);
    }

    private void cargarDatos() {
        ControladorUsuario ctrl = new ControladorUsuario();
        Login u = ctrl.buscarUsuarioPorId(idToEdit);
        if (u != null && !u.getNombreUsuario().contains("no existe")) {
            txtNombre.setText(u.getNombreUsuario());
            txtNombre.setEnabled(false);
            cmbRol.setSelectedItem(u.getRolUsuario());
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String rol = (String) cmbRol.getSelectedItem();

        if (nombre.isEmpty() || (idToEdit == -1 && contrasena.isEmpty())) {
            ToastNotifier.showError(this, "Complete todos los campos.");
            return;
        }

        ControladorUsuario ctrl = new ControladorUsuario();
        if (idToEdit == -1) {
            ctrl.insertarUsuario(nombre, contrasena, rol);
        } else {
            ctrl.modificarUsuario(idToEdit, nombre, contrasena, rol);
        }
        if (onSaved != null) onSaved.run();
        Window owner = getOwner();
        dispose();
        if (owner != null) {
            String msg = idToEdit == -1 ? "Usuario creado exitosamente." : "Usuario actualizado exitosamente.";
            ToastNotifier.showSuccess(owner, msg);
        }
    }
}
