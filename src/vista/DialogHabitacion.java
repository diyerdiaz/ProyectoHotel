package vista;

import Controlador.ControladorHabitaciones;
import modelo.Habitaciones;
import javax.swing.*;
import java.awt.*;

public class DialogHabitacion extends JDialog {
    private JTextField txtNumero, txtPrecio;
    private JComboBox<String> cbTipo, cbEstado;
    private int idHabitacionToEdit = -1;
    private Runnable onSaved;

    public DialogHabitacion(Window owner, int idHabitacionToEdit, Runnable onSaved) {
        super(owner, idHabitacionToEdit == -1 ? "Crear Habitación" : "Editar Habitación", ModalityType.APPLICATION_MODAL);
        this.idHabitacionToEdit = idHabitacionToEdit;
        this.onSaved = onSaved;

        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Número de Habitación:"));
        txtNumero = new JTextField();
        panel.add(txtNumero);

        panel.add(new JLabel("Tipo de Habitación:"));
        cbTipo = new JComboBox<>(new String[]{"Sencilla", "Doble", "Suite", "Matrimonial"});
        panel.add(cbTipo);

        panel.add(new JLabel("Precio por Noche:"));
        txtPrecio = new JTextField();
        panel.add(txtPrecio);

        panel.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(new String[]{"DISPONIBLE", "OCUPADA", "MANTENIMIENTO"});
        panel.add(cbEstado);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        add(btnPanel, BorderLayout.SOUTH);

        if (idHabitacionToEdit != -1) {
            cargarDatos();
        }
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
