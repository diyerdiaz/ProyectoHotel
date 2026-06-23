package vista;

import Controlador.ControladorEmpleado;
import Controlador.ControladorUsuario;
import modelo.Login;
import modelo.empleado;
import util.Encriptador;
import util.ToastNotifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class DialogEmpleado extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);

    private JTextField txtNombre, txtApellido, txtDocumento, txtCargo, txtSalario, txtFecha, txtTelefono, txtCorreo, txtDireccion, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;
    private int idToEdit = -1;
    private Runnable onSaved;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DialogEmpleado(Window owner, int idToEdit, Runnable onSaved) {
        super(owner, idToEdit == -1 ? "Crear Empleado" : "Editar Empleado", ModalityType.APPLICATION_MODAL);
        this.idToEdit = idToEdit;
        this.onSaved = onSaved;

        setSize(540, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel header = new JPanel();
        header.setBackground(DARK_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, GOLD));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 54));

        JLabel headerTitle = new JLabel("  " + (idToEdit == -1 ? "NUEVO EMPLEADO" : "EDITAR EMPLEADO"));
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerTitle.setForeground(GOLD);
        header.add(headerTitle, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 24, 12, 24));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);

        txtNombre = createField(); txtApellido = createField(); txtDocumento = createField();
        txtCargo = createField(); txtSalario = createField(); txtFecha = createField();
        txtTelefono = createField(); txtCorreo = createField(); txtDireccion = createField();
        txtUsername = createField();

        txtPassword = new JPasswordField(18);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setBackground(FIELD_BG);
        txtPassword.setForeground(DARK_BG);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                new EmptyBorder(6, 10, 6, 10)));

        cmbRol = new JComboBox<>(new String[]{"administrador", "recepcionista", "servicio_limpieza"});
        cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbRol.setBackground(FIELD_BG);
        cmbRol.setForeground(DARK_BG);

        addRow(panel, gbc, 0, "Nombre:", txtNombre);
        addRow(panel, gbc, 1, "Apellido:", txtApellido);
        addRow(panel, gbc, 2, "Documento:", txtDocumento);
        addRow(panel, gbc, 3, "Cargo:", txtCargo);
        addRow(panel, gbc, 4, "Salario:", txtSalario);
        addRow(panel, gbc, 5, "Fecha (YYYY-MM-DD):", txtFecha);
        addRow(panel, gbc, 6, "Tel\u00e9fono:", txtTelefono);
        addRow(panel, gbc, 7, "Correo:", txtCorreo);
        addRow(panel, gbc, 8, "Direcci\u00f3n:", txtDireccion);
        addRow(panel, gbc, 9, "Usuario:", txtUsername);
        addRow(panel, gbc, 10, "Contrase\u00f1a:", txtPassword);
        addRowCombo(panel, gbc, 11, "Rol Usuario:", cmbRol);

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
        ControladorEmpleado ctrl = new ControladorEmpleado();
        empleado e = ctrl.buscarEmpleadoPorId(idToEdit);
        if (e != null && !e.getNombre().contains("no existe")) {
            txtNombre.setText(e.getNombre());
            txtApellido.setText(e.getApellido());
            txtDocumento.setText(String.valueOf(e.getDocumento()));
            txtCargo.setText(e.getCargo());
            txtSalario.setText(String.valueOf(e.getSalario()));
            if (e.getFechaContratacion() != null) txtFecha.setText(sdf.format(e.getFechaContratacion()));
            txtTelefono.setText(e.getTelefono());
            txtCorreo.setText(e.getCorreo());
            txtDireccion.setText(e.getDireccion());

            int userId = e.getIdUsuario();
            if (userId > 0) {
                ControladorUsuario userCtrl = new ControladorUsuario();
                Login user = userCtrl.buscarUsuarioPorId(userId);
                if (user != null && !user.getNombreUsuario().contains("no existe")) {
                    txtUsername.setText(user.getNombreUsuario());
                    txtUsername.setEnabled(false);
                    cmbRol.setSelectedItem(user.getRolUsuario());
                }
            }
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String documentoStr = txtDocumento.getText().trim();
        String cargo = txtCargo.getText().trim();
        String salarioStr = txtSalario.getText().trim();
        String fechaStr = txtFecha.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || apellido.isEmpty() || documentoStr.isEmpty() || cargo.isEmpty() || salarioStr.isEmpty() || username.isEmpty()) {
            ToastNotifier.showError(this, "Complete los campos obligatorios.");
            return;
        }

        try {
            int documento = Integer.parseInt(documentoStr);
            double salario = Double.parseDouble(salarioStr);

            if (idToEdit == -1) {
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
                nuevoUser.setRolUsuario((String) cmbRol.getSelectedItem());
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

                ControladorEmpleado ctrl = new ControladorEmpleado();
                Date fecha = fechaStr.isEmpty() ? new Date() : sdf.parse(fechaStr);
                ctrl.insertarEmpleado(nombre, apellido, documento, cargo, salario, fecha, telefono, correo, direccion, idUsuario);
            } else {
                ControladorEmpleado ctrl = new ControladorEmpleado();
                empleado emp = ctrl.buscarEmpleadoPorId(idToEdit);
                int userId = emp.getIdUsuario();

                if (!password.isEmpty()) {
                    Login existingUser = new ControladorUsuario().buscarUsuarioPorId(userId);
                    if (existingUser != null && !existingUser.getNombreUsuario().contains("no existe")) {
                        new ControladorUsuario().modificarUsuario(userId, existingUser.getNombreUsuario(), Encriptador.hashSHA256(password), (String) cmbRol.getSelectedItem());
                    }
                }

                Date fecha = fechaStr.isEmpty() ? new Date() : sdf.parse(fechaStr);
                ctrl.modificarEmpleado(idToEdit, nombre, apellido, documento, cargo, salario, fecha, telefono, correo, direccion, userId);
            }

            if (onSaved != null) onSaved.run();
            Window owner = getOwner();
            dispose();
            if (owner != null) {
                String msg = idToEdit == -1 ? "Empleado creado exitosamente." : "Empleado actualizado exitosamente.";
                ToastNotifier.showSuccess(owner, msg);
            }
        } catch (NumberFormatException ex) {
            ToastNotifier.showError(this, "Documento y Salario deben ser num\u00e9ricos.");
        } catch (Exception ex) {
            ToastNotifier.showError(this, "Error: " + ex.getMessage());
        }
    }
}
