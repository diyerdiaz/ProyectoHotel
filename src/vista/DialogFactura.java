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
import java.util.Locale;

public class DialogFactura extends JDialog {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color TEXT_MUTED = new Color(107, 114, 128);
    private static final Color WHITE = Color.WHITE;

    private final int idFactura;
    private JLabel lblEstado;

    public DialogFactura(Window owner, int idFactura) {
        super(owner, "Factura #" + idFactura, ModalityType.APPLICATION_MODAL);
        this.idFactura = idFactura;
        setSize(460, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        buildHeader();
        buildContent();
        buildFooter();
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, GOLD));
        header.setPreferredSize(new Dimension(0, 60));

        JPanel left = new JPanel(new GridLayout(2, 1, 2, 2));
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("FACTURA #" + idFactura);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(GOLD);
        left.add(title);

        ControladorFacturas cf = new ControladorFacturas();
        facturas f = cf.buscarFacturaPorId(idFactura);
        String estado = (f != null && !"FACTURA no existe".equals(f.getEstadoFactura())) ? f.getEstadoFactura() : "DESCONOCIDO";
        lblEstado = new JLabel(estado);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblEstado.setForeground("PAGADA".equals(estado) ? new Color(34, 197, 94) : GOLD);
        left.add(lblEstado);

        header.add(left, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 24, 12, 24));
        panel.setBackground(WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        ControladorFacturas cf = new ControladorFacturas();
        facturas f = cf.buscarFacturaPorId(idFactura);

        if (f == null || "FACTURA no existe".equals(f.getEstadoFactura())) {
            JLabel nf = new JLabel("Factura no encontrada", SwingConstants.CENTER);
            nf.setFont(new Font("SansSerif", Font.PLAIN, 14));
            nf.setForeground(Color.RED);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panel.add(nf, gbc);
            add(panel, BorderLayout.CENTER);
            return;
        }

        int r = 0;
        addSection(panel, gbc, r++, "INFORMACI\u00d3N DE FACTURA");
        addRow(panel, gbc, r++, "Fecha:", formatDate(f.getFechaFactura()));
        addRow(panel, gbc, r++, "Total:", formatCurrency(f.getTotalFactura()));
        addRow(panel, gbc, r++, "Estado:", f.getEstadoFactura());
        addRow(panel, gbc, r++, "M\u00e9todo Pago:", f.getMetodoPago());

        ControladorReserva cr = new ControladorReserva();
        Reserva res = cr.buscarReservaPorId(f.getIdReserva());

        if (res != null && !res.getHabitacion().contains("no existe")) {
            r = addSeparator(panel, gbc, r);
            addSection(panel, gbc, r++, "HU\u00c9SPED");
            ControladorCliente cc = new ControladorCliente();
            Cliente c = cc.buscarClientePorId(res.getIdCliente());
            addRow(panel, gbc, r++, "Nombre:", c != null ? c.getNombre() + " " + c.getApellido() : "N/A");

            r = addSeparator(panel, gbc, r);
            addSection(panel, gbc, r++, "ESTAD\u00cdA");
            String habStr = res.getHabitacion();
            addRow(panel, gbc, r++, "Habitaci\u00f3n:", habStr);

            ControladorHabitaciones ch = new ControladorHabitaciones();
            Habitaciones h = ch.buscarHabitacionPorId(res.getIdHabitacion());
            if (h != null && !h.getTipoHabitacion().contains("no existe")) {
                addRow(panel, gbc, r++, "Tipo:", h.getTipoHabitacion());
            }

            addRow(panel, gbc, r++, "Entrada:", formatDate(res.getFechaEntrada()));
            addRow(panel, gbc, r++, "Salida:", formatDate(res.getFechaSalida()));
            addRow(panel, gbc, r++, "Personas:", String.valueOf(res.getPersonas()));

            long noches = 1;
            if (res.getFechaEntrada() != null && res.getFechaSalida() != null) {
                long diff = res.getFechaSalida().getTime() - res.getFechaEntrada().getTime();
                noches = Math.max(1, diff / (1000 * 60 * 60 * 24));
            }
            if (h != null) {
                addRow(panel, gbc, r++, "Precio/Noche:", formatCurrency(h.getPrecioHabitacion()));
                addRow(panel, gbc, r++, "Noches:", String.valueOf(noches));
                addRow(panel, gbc, r++, "Subtotal:", formatCurrency(noches * h.getPrecioHabitacion()));
            }
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void buildFooter() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnPanel.setBackground(WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 0, 16, 24));

        ControladorFacturas cf = new ControladorFacturas();
        facturas f = cf.buscarFacturaPorId(idFactura);
        if (f != null && "PENDIENTE".equals(f.getEstadoFactura())) {
            JButton btnPagar = new JButton("Marcar como Pagada");
            btnPagar.setFont(new Font("SansSerif", Font.BOLD, 12));
            btnPagar.setBackground(new Color(34, 197, 94));
            btnPagar.setForeground(WHITE);
            btnPagar.setFocusPainted(false);
            btnPagar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnPagar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(22, 163, 74), 1),
                    new EmptyBorder(8, 18, 8, 18)));
            btnPagar.addActionListener(e -> marcarPagada());
            btnPanel.add(btnPagar);
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCerrar.setBackground(WHITE);
        btnCerrar.setForeground(TEXT_MUTED);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        btnCerrar.addActionListener(e -> dispose());
        btnPanel.add(btnCerrar);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void marcarPagada() {
        new ControladorFacturas().cambiarEstadoFactura(idFactura, "PAGADA");
        ToastNotifier.showSuccess(this, "Factura marcada como Pagada.");
        dispose();
    }

    private int addSeparator(JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 8, 14, 8);
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        panel.add(sep, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 8, 6, 8);
        return row + 1;
    }

    private void addSection(JPanel panel, GridBagConstraints gbc, int row, String text) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(GOLD);
        panel.add(lbl, gbc);
        gbc.gridwidth = 1; gbc.weightx = 0.0;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridy = row; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JLabel val = new JLabel(value != null ? value : "N/A");
        val.setFont(new Font("SansSerif", Font.BOLD, 12));
        val.setForeground(DARK_BG);
        panel.add(val, gbc);
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(amount);
    }

    private String formatDate(java.util.Date d) {
        if (d == null) return "N/A";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }
}
