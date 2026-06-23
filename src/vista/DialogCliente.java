package vista;

import Controlador.ControladorCliente;
import Controlador.ControladorUsuario;
import modelo.Cliente;
import modelo.Login;
import util.Encriptador;
import util.ToastNotifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Iterator;

public class DialogCliente extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);

    private JTextField txtNombre, txtApellido, txtDocumento, txtCorreo, txtTelefono, txtDireccion, txtUsername;
    private JPasswordField txtPassword;
    private int idClienteToEdit = -1;
    private Runnable onSaved;

    public DialogCliente(Window owner, int idClienteToEdit, Runnable onSaved) {
        super(owner, idClienteToEdit == -1 ? "Crear Cliente" : "Editar Cliente", ModalityType.APPLICATION_MODAL);
        this.idClienteToEdit = idClienteToEdit;
        this.onSaved = onSaved;

        setSize(540, 560);
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
        txtCorreo = createField(); txtTelefono = createField(); txtDireccion = createField();
        txtUsername = createField();

        txtPassword = new JPasswordField(18);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setBackground(FIELD_BG);
        txtPassword.setForeground(DARK_BG);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                new EmptyBorder(6, 10, 6, 10)));

        addRow(panel, gbc, 0, "Nombre:", txtNombre);
        addRow(panel, gbc, 1, "Apellido:", txtApellido);
        addRow(panel, gbc, 2, "Documento:", txtDocumento);
        addRow(panel, gbc, 3, "Correo:", txtCorreo);
        addRow(panel, gbc, 4, "Tel\u00e9fono:", txtTelefono);
        addRow(panel, gbc, 5, "Direcci\u00f3n:", txtDireccion);
        addRow(panel, gbc, 6, "Usuario:", txtUsername);
        addRow(panel, gbc, 7, "Contrase\u00f1a:", txtPassword);

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
        f.setForeground(DARK_BG);
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

            int userId = c.getIdUsuario();
            if (userId > 0) {
                ControladorUsuario userCtrl = new ControladorUsuario();
                Login user = userCtrl.buscarUsuarioPorId(userId);
                if (user != null && !user.getNombreUsuario().contains("no existe")) {
                    txtUsername.setText(user.getNombreUsuario());
                    txtUsername.setEnabled(false);
                }
            }
        }
    }

    private void guardar() {
        ControladorCliente ctrlCli = new ControladorCliente();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String documentoStr = txtDocumento.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefonoStr = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || apellido.isEmpty() || documentoStr.isEmpty() || correo.isEmpty() || telefonoStr.isEmpty() || username.isEmpty()) {
            ToastNotifier.showError(this, "Complete todos los campos obligatorios.");
            return;
        }

        try {
            long documento = Long.parseLong(documentoStr);
            long telefono = Long.parseLong(telefonoStr);

            if (idClienteToEdit == -1) {
                if (password.isEmpty()) {
                    ToastNotifier.showError(this, "Ingrese una contrase\u00f1a.");
                    return;
                }

                Iterator<Login> existentes = new Login().buscar(username);
                while (existentes.hasNext()) {
                    Login u = existentes.next();
                    if (username.equals(u.getNombreUsuario())) {
                        ToastNotifier.showError(this, "El nombre de usuario ya existe.");
                        return;
                    }
                }

                Login nuevoUser = new Login();
                nuevoUser.setNombreUsuario(username);
                nuevoUser.setContrasenaUsuario(Encriptador.hashSHA256(password));
                nuevoUser.setRolUsuario("cliente");
                nuevoUser.insertar();

                Iterator<Login> usuariosInsertados = new Login().buscar(username);
                int idUsuario = 0;
                while (usuariosInsertados.hasNext()) {
                    Login u = usuariosInsertados.next();
                    if (username.equals(u.getNombreUsuario())) {
                        idUsuario = u.getIdUsuario();
                        break;
                    }
                }

                ctrlCli.insertarCliente(nombre, apellido, documento, correo, telefono, direccion, idUsuario);
            } else {
                ControladorUsuario userCtrl = new ControladorUsuario();
                Cliente c = ctrlCli.buscarClientePorId(idClienteToEdit);
                int userId = c.getIdUsuario();

                if (!password.isEmpty()) {
                    Login existingUser = userCtrl.buscarUsuarioPorId(userId);
                    if (existingUser != null && !existingUser.getNombreUsuario().contains("no existe")) {
                        userCtrl.modificarUsuario(userId, existingUser.getNombreUsuario(), Encriptador.hashSHA256(password), existingUser.getRolUsuario());
                    }
                }

                ctrlCli.modificarCliente(idClienteToEdit, nombre, apellido, documento, correo, telefono, direccion, userId);
            }

            if (onSaved != null) onSaved.run();
            Window owner = getOwner();
            dispose();
            if (owner != null) {
                String msg = idClienteToEdit == -1 ? "Cliente creado exitosamente." : "Cliente actualizado exitosamente.";
                ToastNotifier.showSuccess(owner, msg);
            }
        } catch (NumberFormatException ex) {
            ToastNotifier.showError(this, "Documento y Tel\u00e9fono deben ser num\u00e9ricos.");
        }
    }
}
