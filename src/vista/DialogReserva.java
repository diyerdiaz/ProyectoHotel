package vista;

import Controlador.ControladorReserva;
import modelo.Reserva;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogReserva extends JDialog {
    private JTextField txtIdCliente, txtIdHabitacion, txtHabitacionDesc, txtPersonas, txtFechaEntrada, txtFechaSalida;
    private JComboBox<String> cbMedioPago;
    private int idReservaToEdit = -1;
    private Runnable onSaved;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DialogReserva(Window owner, int idReservaToEdit, Runnable onSaved) {
        super(owner, idReservaToEdit == -1 ? "Crear Reserva" : "Editar Reserva", ModalityType.APPLICATION_MODAL);
        this.idReservaToEdit = idReservaToEdit;
        this.onSaved = onSaved;

        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("ID Cliente:"));
        txtIdCliente = new JTextField();
        panel.add(txtIdCliente);

        panel.add(new JLabel("ID Habitación:"));
        txtIdHabitacion = new JTextField();
        panel.add(txtIdHabitacion);

        panel.add(new JLabel("Nombre/Ref Habitación:"));
        txtHabitacionDesc = new JTextField();
        panel.add(txtHabitacionDesc);

        panel.add(new JLabel("N° Personas:"));
        txtPersonas = new JTextField();
        panel.add(txtPersonas);

        panel.add(new JLabel("Fecha Entrada (YYYY-MM-DD):"));
        txtFechaEntrada = new JTextField();
        panel.add(txtFechaEntrada);

        panel.add(new JLabel("Fecha Salida (YYYY-MM-DD):"));
        txtFechaSalida = new JTextField();
        panel.add(txtFechaSalida);

        panel.add(new JLabel("Medio de Pago:"));
        cbMedioPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA"});
        panel.add(cbMedioPago);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        add(btnPanel, BorderLayout.SOUTH);

        if (idReservaToEdit != -1) {
            cargarDatos();
        }
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
            
            if (mensaje.contains("éxito")) {
                if (onSaved != null) onSaved.run();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs y Personas deben ser valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "El formato de fecha debe ser YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
