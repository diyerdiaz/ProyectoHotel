package vista;

import Controlador.ControladorReserva;
import modelo.Reserva;
import util.ToastNotifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogReserva extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);

    private JTextField txtIdCliente, txtIdHabitacion, txtHabitacionDesc, txtPersonas, txtFechaEntrada, txtFechaSalida;
    private JComboBox<String> cbMedioPago;
    private int idReservaToEdit = -1;
    private Runnable onSaved;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DialogReserva(Window owner, int idReservaToEdit, Runnable onSaved) {
        super(owner, idReservaToEdit == -1 ? "Crear Reserva" : "Editar Reserva", ModalityType.APPLICATION_MODAL);
        this.idReservaToEdit = idReservaToEdit;
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

        JLabel headerTitle = new JLabel("  " + (idReservaToEdit == -1 ? "NUEVA RESERVA" : "EDITAR RESERVA"));
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

        txtIdCliente = createField(); txtIdHabitacion = createField(); txtHabitacionDesc = createField();
        txtPersonas = createField(); txtFechaEntrada = createField(); txtFechaSalida = createField();

        cbMedioPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA"});
        cbMedioPago.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbMedioPago.setBackground(FIELD_BG);

        addRow(panel, gbc, 0, "ID Cliente:", txtIdCliente);
        addRow(panel, gbc, 1, "ID Habitaci\u00f3n:", txtIdHabitacion);
        addRow(panel, gbc, 2, "Ref. Habitaci\u00f3n:", txtHabitacionDesc);
        addRow(panel, gbc, 3, "N\u00b0 Personas:", txtPersonas);
        addRow(panel, gbc, 4, "Entrada (YYYY-MM-DD):", txtFechaEntrada);
        addRow(panel, gbc, 5, "Salida (YYYY-MM-DD):", txtFechaSalida);
        addRowCombo(panel, gbc, 6, "Medio de Pago:", cbMedioPago);

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

        if (idReservaToEdit != -1) {
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
        ControladorReserva ctrl = new ControladorReserva();
        Reserva r = ctrl.buscarReservaPorId(idReservaToEdit);
        if (r != null && !r.getHabitacion().contains("no existe")) {
            txtIdCliente.setText(String.valueOf(r.getIdCliente()));
            txtIdHabitacion.setText(String.valueOf(r.getIdHabitacion()));
            txtHabitacionDesc.setText(r.getHabitacion());
            txtPersonas.setText(String.valueOf(r.getPersonas()));
            if(r.getFechaEntrada() != null) txtFechaEntrada.setText(sdf.format(r.getFechaEntrada()));
            if(r.getFechaSalida() != null) txtFechaSalida.setText(sdf.format(r.getFechaSalida()));
            cbMedioPago.setSelectedItem(r.getMedioPago());
        }
    }

    private void guardar() {
        try {
            ControladorReserva ctrl = new ControladorReserva();
            int idCliente = Integer.parseInt(txtIdCliente.getText());
            int idHabitacion = Integer.parseInt(txtIdHabitacion.getText());
            String habitacion = txtHabitacionDesc.getText();
            int personas = Integer.parseInt(txtPersonas.getText());
            Date fEntrada = sdf.parse(txtFechaEntrada.getText());
            Date fSalida = sdf.parse(txtFechaSalida.getText());
            String medioPago = cbMedioPago.getSelectedItem().toString();

            String mensaje = "";
            if (idReservaToEdit == -1) {
                mensaje = ctrl.insertarReserva(idCliente, idHabitacion, habitacion, personas, fEntrada, fSalida, medioPago);
            } else {
                mensaje = ctrl.modificarReserva(idReservaToEdit, idCliente, idHabitacion, habitacion, personas, fEntrada, fSalida, medioPago);
            }
            
            if (mensaje.contains("éxito") || mensaje.contains("exito")) {
                if (onSaved != null) onSaved.run();
                Window owner = getOwner();
                dispose();
                if (owner != null) {
                    String msg = idReservaToEdit == -1 ? "Reserva creada exitosamente." : "Reserva actualizada exitosamente.";
                    ToastNotifier.showSuccess(owner, msg);
                }
            } else {
                ToastNotifier.showWarning(this, mensaje);
            }
        } catch (NumberFormatException ex) {
            ToastNotifier.showError(this, "IDs y Personas deben ser valores num\u00e9ricos.");
        } catch (ParseException ex) {
            ToastNotifier.showError(this, "El formato de fecha debe ser YYYY-MM-DD.");
        }
    }
}
