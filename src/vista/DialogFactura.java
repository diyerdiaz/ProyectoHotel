package vista;

import Controlador.ControladorFacturas;
import Controlador.ControladorReserva;
import Controlador.ControladorHabitaciones;
import Controlador.ControladorCliente;
import modelo.facturas;
import modelo.Reserva;
import modelo.Habitaciones;
import modelo.Cliente;
import util.ToastNotifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;

public class DialogFactura extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color LIGHT_BG = new Color(249, 250, 251);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    public DialogFactura(Window owner, int idFactura) {
        super(owner, "Detalles de Factura #" + idFactura, ModalityType.APPLICATION_MODAL);
        
        setSize(520, 580);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel header = new JPanel();
        header.setBackground(DARK_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, GOLD));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 54));

        JLabel headerTitle = new JLabel("  DETALLES DE FACTURA #" + idFactura);
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerTitle.setForeground(GOLD);
        header.add(headerTitle, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 24, 12, 24));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        ControladorFacturas cf = new ControladorFacturas();
        facturas f = cf.buscarFacturaPorId(idFactura);

        if (f != null && !"FACTURA no existe".equals(f.getEstadoFactura())) {
            // Datos de la factura
            addDetailRow(panel, gbc, 0, "ID Factura:", String.valueOf(f.getIdFactura()));
            addDetailRow(panel, gbc, 1, "ID Reserva:", String.valueOf(f.getIdReserva()));
            addDetailRow(panel, gbc, 2, "Fecha:", f.getFechaFactura() != null ? f.getFechaFactura().toString() : "N/A");
            addDetailRow(panel, gbc, 3, "Total:", formatCurrency(f.getTotalFactura()));
            addDetailRow(panel, gbc, 4, "Estado:", f.getEstadoFactura());
            addDetailRow(panel, gbc, 5, "Método de Pago:", f.getMetodoPago());

            // Separador
            gbc.gridy = 6;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(12, 8, 12, 8);
            JSeparator sep = new JSeparator();
            sep.setForeground(BORDER_COLOR);
            panel.add(sep, gbc);
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.gridwidth = 1;

            // Datos de la reserva asociada
            addDetailRow(panel, gbc, 7, "RESERVA ASOCIADA:", "");
            
            ControladorReserva cr = new ControladorReserva();
            Reserva r = cr.buscarReservaPorId(f.getIdReserva());
            if (r != null && !r.getHabitacion().contains("no existe")) {
                addDetailRow(panel, gbc, 8, "Habitación:", r.getHabitacion());
                addDetailRow(panel, gbc, 9, "Personas:", String.valueOf(r.getPersonas()));
                addDetailRow(panel, gbc, 10, "Entrada:", r.getFechaEntrada() != null ? r.getFechaEntrada().toString() : "N/A");
                addDetailRow(panel, gbc, 11, "Salida:", r.getFechaSalida() != null ? r.getFechaSalida().toString() : "N/A");
                addDetailRow(panel, gbc, 12, "Medio de Pago Reserva:", r.getMedioPago());

                // Datos de la habitación
                ControladorHabitaciones ch = new ControladorHabitaciones();
                Habitaciones h = ch.buscarHabitacionPorId(r.getIdHabitacion());
                if (h != null && !h.getTipoHabitacion().contains("no existe")) {
                    addDetailRow(panel, gbc, 13, "Tipo Habitación:", h.getTipoHabitacion());
                    addDetailRow(panel, gbc, 14, "Precio/Noche:", formatCurrency(h.getPrecioHabitacion()));
                }

                // Datos del cliente
                ControladorCliente cc = new ControladorCliente();
                Cliente c = cc.buscarClientePorId(r.getIdCliente());
                if (c != null && c.getNombre() != null && !c.getNombre().equals("No hay nada registrado")) {
                    addDetailRow(panel, gbc, 15, "CLIENTE:", "");
                    addDetailRow(panel, gbc, 16, "Nombre:", c.getNombre() + " " + c.getApellido());
                    addDetailRow(panel, gbc, 17, "Documento:", String.valueOf(c.getDocumento()));
                    addDetailRow(panel, gbc, 18, "Correo:", c.getCorreo());
                    addDetailRow(panel, gbc, 19, "Teléfono:", String.valueOf(c.getTelefono()));
                    addDetailRow(panel, gbc, 20, "Dirección:", c.getDireccion());
                }
            }
        } else {
            JLabel notFound = new JLabel("Factura no encontrada", SwingConstants.CENTER);
            notFound.setFont(new Font("SansSerif", Font.PLAIN, 14));
            notFound.setForeground(Color.RED);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(notFound, gbc);
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Botón cerrar
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 0, 16, 24));

        JButton btnCerrar = createButton("Cerrar", false);
        btnCerrar.addActionListener(e -> dispose());
        btnPanel.add(btnCerrar);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_BG);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JLabel val = new JLabel(value != null ? value : "N/A");
        val.setFont(new Font("SansSerif", Font.PLAIN, 12));
        val.setForeground(new Color(75, 85, 99));
        panel.add(val, gbc);
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(amount);
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
            btn.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        }
        return btn;
    }
}
