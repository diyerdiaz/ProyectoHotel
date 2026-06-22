package vista;

import Controlador.ControladorHabitaciones;
import modelo.Habitaciones;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogHabitacion extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);

    private JTextField txtNumero, txtPrecio;
    private JComboBox<String> cbTipo, cbEstado;
    private int idHabitacionToEdit = -1;
    private Runnable onSaved;

    public DialogHabitacion(Window owner, int idHabitacionToEdit, Runnable onSaved) {
        super(owner, idHabitacionToEdit == -1 ? "Crear Habitaci\u00f3n" : "Editar Habitaci\u00f3n", ModalityType.APPLICATION_MODAL);
        this.idHabitacionToEdit = idHabitacionToEdit;
        this.onSaved = onSaved;

        setSize(500, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel header = new JPanel();
        header.setBackground(DARK_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, GOLD));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 54));

        JLabel headerTitle = new JLabel("  " + (idHabitacionToEdit == -1 ? "NUEVA HABITACI\u00d3N" : "EDITAR HABITACI\u00d3N"));
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerTitle.setForeground(GOLD);
        header.add(headerTitle, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 24, 12, 24));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);

        txtNumero = createField();
        txtPrecio = createField();
        cbTipo = new JComboBox<>(new String[]{"Sencilla", "Doble", "Suite", "Matrimonial"});
        styleCombo(cbTipo);
        cbEstado = new JComboBox<>(new String[]{"DISPONIBLE", "OCUPADA", "MANTENIMIENTO"});
        styleCombo(cbEstado);

        addRow(panel, gbc, 0, "N\u00famero:", txtNumero);
        addRowCombo(panel, gbc, 1, "Tipo:", cbTipo);
        addRow(panel, gbc, 2, "Precio/noche:", txtPrecio);
        addRowCombo(panel, gbc, 3, "Estado:", cbEstado);

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

        if (idHabitacionToEdit != -1) {
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

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setBackground(FIELD_BG);
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

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_BG);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private void addRowCombo(JPanel panel, GridBagConstraints gbc, int row, String label, JComboBox<String> cb) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_BG);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cb, gbc);
    }

    private void cargarDatos() {
        ControladorHabitaciones ctrl = new ControladorHabitaciones();
        Habitaciones h = ctrl.buscarHabitacionPorId(idHabitacionToEdit);
        if (h != null && !h.getTipoHabitacion().contains("no existe")) {
            txtNumero.setText(String.valueOf(h.getNumeroHabitacion()));
            cbTipo.setSelectedItem(h.getTipoHabitacion());
            txtPrecio.setText(String.valueOf(h.getPrecioHabitacion()));
            cbEstado.setSelectedItem(h.getEstadoHbitacion());
        }
    }

    private void guardar() {
        try {
            ControladorHabitaciones ctrl = new ControladorHabitaciones();
            int numero = Integer.parseInt(txtNumero.getText());
            String tipo = cbTipo.getSelectedItem().toString();
            double precio = Double.parseDouble(txtPrecio.getText());
            String estado = cbEstado.getSelectedItem().toString();

            if (idHabitacionToEdit == -1) {
                ctrl.insertarHabitacion(numero, tipo, precio, estado);
            } else {
                ctrl.modificarHabitacion(idHabitacionToEdit, numero, tipo, precio, estado);
            }
            if (onSaved != null) onSaved.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número y Precio deben ser valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
