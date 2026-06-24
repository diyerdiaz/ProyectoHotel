package vista;

import Controlador.ControladorEmpleado;
import Controlador.ControladorUsuario;
import modelo.Login;
import modelo.empleado;
import util.Encriptador;
import util.ToastNotifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;

public class DialogEmpleado extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);
    private static final Color ERROR_BORDER = new Color(220, 38, 38);
    private static final Color NORMAL_BORDER = new Color(209, 213, 219);

    private JTextField txtNombre, txtApellido, txtDocumento, txtTelefono, txtCorreo, txtDireccion, txtUsername;
    private JTextField txtSalario;
    private JPasswordField txtPassword;
    private JSpinner spFecha;
    private JComboBox<String> cmbRol;
    private int idToEdit = -1;
    private Runnable onSaved;

    public DialogEmpleado(Window owner, int idToEdit, Runnable onSaved) {
        super(owner, idToEdit == -1 ? "Crear Empleado" : "Editar Empleado", ModalityType.APPLICATION_MODAL);
        this.idToEdit = idToEdit;
        this.onSaved = onSaved;

        setSize(560, 640);
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

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(18, 24, 8, 24));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 6, 3, 6);

        txtNombre = createField();
        txtApellido = createField();
        txtDocumento = createDigitField();
        txtSalario = createSalaryField();
        spFecha = createDateSpinner();
        txtTelefono = createField();
        txtCorreo = createField();
        txtDireccion = createField();
        txtUsername = createField();

        txtPassword = new JPasswordField(18);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setBackground(FIELD_BG);
        txtPassword.setForeground(DARK_BG);
        txtPassword.setBorder(createNormalBorder());

        String[] roles = {"administrador", "recepcionista", "servicio_limpieza"};
        cmbRol = new JComboBox<>(roles);
        cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbRol.setBackground(FIELD_BG);
        cmbRol.setForeground(DARK_BG);

        int r = 0;
        addRow(formPanel, gbc, r++, "Nombre:", txtNombre);
        addRow(formPanel, gbc, r++, "Apellido:", txtApellido);
        addRow(formPanel, gbc, r++, "Documento:", txtDocumento);
        addRowCombo(formPanel, gbc, r++, "Cargo / Rol:", cmbRol);
        addRow(formPanel, gbc, r++, "Salario:", txtSalario);
        addRow(formPanel, gbc, r++, "Fecha Ingreso:", spFecha);
        addRow(formPanel, gbc, r++, "Tel\u00e9fono:", txtTelefono);
        addRow(formPanel, gbc, r++, "Correo:", txtCorreo);
        addRow(formPanel, gbc, r++, "Direcci\u00f3n:", txtDireccion);
        addRow(formPanel, gbc, r++, "Usuario:", txtUsername);
        addRow(formPanel, gbc, r++, "Contrase\u00f1a:", txtPassword);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 0, 14, 24));

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
        styleField(f);
        return f;
    }

    private JTextField createDigitField() {
        JTextField f = new JTextField(18);
        styleField(f);
        ((PlainDocument) f.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) throws BadLocationException {
                if (str != null && str.matches("\\d*")) super.insertString(fb, off, str, attr);
            }
            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) throws BadLocationException {
                if (str != null && str.matches("\\d*")) super.replace(fb, off, len, str, attr);
            }
        });
        return f;
    }

    private JTextField createSalaryField() {
        JTextField f = new JTextField(18);
        styleField(f);
        ((PlainDocument) f.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) throws BadLocationException {
                if (str != null && str.matches("\\d*")) super.insertString(fb, off, str, attr);
            }
            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) throws BadLocationException {
                if (str != null && str.matches("\\d*")) super.replace(fb, off, len, str, attr);
            }
        });
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String raw = f.getText().replaceAll("[^\\d]", "");
                f.setText(raw);
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                formatSalary(f);
            }
        });
        return f;
    }

    private JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        JFormattedTextField tf = editor.getTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBackground(FIELD_BG);
        tf.setForeground(DARK_BG);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        spinner.setValue(new Date());
        return spinner;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBackground(FIELD_BG);
        f.setForeground(DARK_BG);
        f.setBorder(createNormalBorder());
    }

    private javax.swing.border.Border createNormalBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NORMAL_BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }

    private void markError(JComponent field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ERROR_BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
    }

    private void clearError(JComponent field) {
        field.setBorder(createNormalBorder());
    }

    private void formatSalary(JTextField field) {
        String raw = field.getText().replaceAll("[^\\d]", "");
        if (!raw.isEmpty()) {
            try {
                long val = Long.parseLong(raw);
                if (val > 0) {
                    DecimalFormat df = new DecimalFormat("#,###");
                    field.setText("$ " + df.format(val));
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    private double parseSalary(String text) {
        String raw = text.replaceAll("[^\\d]", "");
        if (raw.isEmpty()) return 0;
        return Double.parseDouble(raw);
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
            String cargo = e.getCargo();
            for (int i = 0; i < cmbRol.getItemCount(); i++) {
                if (cmbRol.getItemAt(i).equalsIgnoreCase(cargo)) {
                    cmbRol.setSelectedIndex(i);
                    break;
                }
            }
            txtSalario.setText(String.valueOf((long) e.getSalario()));
            SwingUtilities.invokeLater(() -> formatSalary(txtSalario));
            if (e.getFechaContratacion() != null) spFecha.setValue(e.getFechaContratacion());
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
        clearError(txtNombre); clearError(txtSalario); clearError(cmbRol);
        clearError(txtUsername); clearError(txtPassword);

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String documentoStr = txtDocumento.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        String cargo = (String) cmbRol.getSelectedItem();

        double salario = parseSalary(txtSalario.getText());

        boolean valid = true;
        if (nombre.isEmpty()) { markError(txtNombre); valid = false; }
        if (salario <= 0) { markError(txtSalario); valid = false; }
        if (username.isEmpty()) { markError(txtUsername); valid = false; }

        if (idToEdit == -1 && password.isEmpty()) {
            markError(txtPassword);
            valid = false;
        }

        if (!valid) {
            ToastNotifier.showError(this, "Corrija los campos marcados en rojo.");
            return;
        }

        try {
            int documento = documentoStr.isEmpty() ? 0 : Integer.parseInt(documentoStr);

            if (idToEdit == -1) {
                Iterator<Login> existentes = new Login().buscar(username);
                while (existentes.hasNext()) {
                    Login u = existentes.next();
                    if (username.equals(u.getNombreUsuario())) {
                        markError(txtUsername);
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
                Date fecha = (Date) spFecha.getValue();
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

                Date fecha = (Date) spFecha.getValue();
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
            ToastNotifier.showError(this, "Documento debe ser num\u00e9rico.");
        } catch (Exception ex) {
            ToastNotifier.showError(this, "Error: " + ex.getMessage());
        }
    }
}
