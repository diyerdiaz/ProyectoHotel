package vista;

import Controlador.ControladorCliente;
import Controlador.ControladorHabitaciones;
import Controlador.ControladorReserva;
import modelo.Cliente;
import modelo.Habitaciones;
import modelo.Reserva;
import util.ToastNotifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class DialogReserva extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color FIELD_BG = new Color(249, 250, 251);

    private JComboBox<String> cmbCliente, cmbHabitacion, cmbPersonas;
    private JSpinner spFechaEntrada, spFechaSalida;
    private JComboBox<String> cbMedioPago;
    private int idReservaToEdit = -1;
    private Runnable onSaved;

    private Map<String, Integer> clientesMap = new LinkedHashMap<>();
    private Map<String, Integer> habitacionesMap = new LinkedHashMap<>();

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

        cargarClientes();
        cmbCliente = new JComboBox<>(clientesMap.keySet().toArray(new String[0]));
        cmbCliente.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbCliente.setBackground(FIELD_BG);
        cmbCliente.setForeground(DARK_BG);

        cargarHabitaciones();
        cmbHabitacion = new JComboBox<>(habitacionesMap.keySet().toArray(new String[0]));
        cmbHabitacion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbHabitacion.setBackground(FIELD_BG);
        cmbHabitacion.setForeground(DARK_BG);

        cmbPersonas = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});
        cmbPersonas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbPersonas.setBackground(FIELD_BG);
        cmbPersonas.setForeground(DARK_BG);

        spFechaEntrada = createDateSpinner();
        spFechaSalida = createDateSpinner();

        cbMedioPago = new JComboBox<>(new String[]{"EFECTIVO", "TRANSFERENCIA"});
        cbMedioPago.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbMedioPago.setBackground(FIELD_BG);
        cbMedioPago.setForeground(DARK_BG);

        addRowCombo(panel, gbc, 0, "Cliente:", cmbCliente);
        addRowCombo(panel, gbc, 1, "Habitaci\u00f3n:", cmbHabitacion);
        addRowCombo(panel, gbc, 2, "Personas:", cmbPersonas);
        addRow(panel, gbc, 3, "Entrada:", spFechaEntrada);
        addRow(panel, gbc, 4, "Salida:", spFechaSalida);
        addRowCombo(panel, gbc, 5, "Medio de Pago:", cbMedioPago);

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

    private void cargarClientes() {
        clientesMap.clear();
        ControladorCliente ctrl = new ControladorCliente();
        Iterator<Cliente> it = ctrl.listarClientes();
        while (it.hasNext()) {
            Cliente c = it.next();
            if (c.getNombre() != null && !c.getNombre().equals("No hay nada registrado")) {
                String display = c.getIdCliente() + " - " + c.getNombre() + " " + c.getApellido();
                clientesMap.put(display, c.getIdCliente());
            }
        }
    }

    private void cargarHabitaciones() {
        habitacionesMap.clear();
        ControladorHabitaciones ctrl = new ControladorHabitaciones();
        Iterator<Habitaciones> it = ctrl.listarHabitaciones();
        while (it.hasNext()) {
            Habitaciones h = it.next();
            if (h.getTipoHabitacion() != null && !h.getTipoHabitacion().contains("no existe")) {
                String display = "N\u00ba" + h.getNumeroHabitacion() + " - " + h.getTipoHabitacion() + " ($" + (int)h.getPrecioHabitacion() + ")";
                habitacionesMap.put(display, h.getIdHabitacion());
            }
        }
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
            for (String key : clientesMap.keySet()) {
                if (clientesMap.get(key).equals(r.getIdCliente())) {
                    cmbCliente.setSelectedItem(key);
                    break;
                }
            }
            for (String key : habitacionesMap.keySet()) {
                if (habitacionesMap.get(key).equals(r.getIdHabitacion())) {
                    cmbHabitacion.setSelectedItem(key);
                    break;
                }
            }
            cmbPersonas.setSelectedItem(String.valueOf(r.getPersonas()));
            if (r.getFechaEntrada() != null) spFechaEntrada.setValue(r.getFechaEntrada());
            if (r.getFechaSalida() != null) spFechaSalida.setValue(r.getFechaSalida());
            cbMedioPago.setSelectedItem(r.getMedioPago());
        }
    }

    private void guardar() {
        try {
            String clienteSel = (String) cmbCliente.getSelectedItem();
            String habSel = (String) cmbHabitacion.getSelectedItem();
            if (clienteSel == null || habSel == null) {
                ToastNotifier.showError(this, "Seleccione un cliente y una habitaci\u00f3n.");
                return;
            }

            int idCliente = clientesMap.get(clienteSel);
            int idHabitacion = habitacionesMap.get(habSel);
            String habitacion = habSel;
            int personas = Integer.parseInt((String) cmbPersonas.getSelectedItem());
            Date fEntrada = (Date) spFechaEntrada.getValue();
            Date fSalida = (Date) spFechaSalida.getValue();
            String medioPago = cbMedioPago.getSelectedItem().toString();

            ControladorReserva ctrl = new ControladorReserva();
            String mensaje;
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
        } catch (Exception ex) {
            ToastNotifier.showError(this, "Error: " + ex.getMessage());
        }
    }
}
