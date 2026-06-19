package vista;

import Controlador.ControladorCliente;
import Controlador.ControladorEmpleado;
import Controlador.ControladorFacturas;
import Controlador.ControladorHabitaciones;
import Controlador.ControladorReserva;
import Controlador.ControladorTipoHabitacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import modelo.ConexionBD;
import modelo.Login;

public class VentanaPrincipal extends JFrame {
    private final Login usuario;
    private final JDesktopPane desktopPane;
    private final JPanel sidebar;
    private final Map<String, JInternalFrame> frames = new LinkedHashMap<>();

    public VentanaPrincipal(Login usuario) {
        this.usuario = usuario;
        ConexionBD.getInstance();

        setTitle("Hotel Gales | Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 760));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 242, 235));

        sidebar = buildSidebar();
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(232, 226, 214));

        root.add(sidebar, BorderLayout.WEST);
        root.add(desktopPane, BorderLayout.CENTER);
        setContentPane(root);

        showHome();
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(280, 760));
        panel.setBackground(new Color(23, 33, 43));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 18, 24, 18));

        JLabel brand = new JLabel("HOTEL GALES");
        brand.setAlignmentX(LEFT_ALIGNMENT);
        brand.setForeground(new Color(212, 175, 55));
        brand.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Panel de gestion");
        subtitle.setAlignmentX(LEFT_ALIGNMENT);
        subtitle.setForeground(new Color(215, 220, 230));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel userCard = new JPanel(new GridLayout(2, 1));
        userCard.setBackground(new Color(33, 44, 57));
        userCard.setMaximumSize(new Dimension(9999, 90));
        userCard.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel userName = new JLabel("Usuario: " + usuario.getNombreUsuario());
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("SansSerif", Font.BOLD, 15));
        JLabel userRole = new JLabel("Rol: " + usuario.getRolUsuario());
        userRole.setForeground(new Color(212, 175, 55));
        userRole.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userCard.add(userName);
        userCard.add(userRole);

        panel.add(brand);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(18));
        panel.add(userCard);
        panel.add(Box.createVerticalStrut(18));

        panel.add(sideButton("Inicio", e -> showHome()));
        panel.add(sideButton("Clientes", e -> openModule("clientes", new ModuleListInternalFrame(
                "Clientes",
                new Object[]{"ID", "Nombre", "Apellido", "Documento", "Correo", "Telefono", "Direccion", "ID Usuario"},
                tabla -> new ControladorCliente().cargarTablaClientes(tabla)))));
        panel.add(sideButton("Habitaciones", e -> openModule("habitaciones", new ModuleListInternalFrame(
                "Habitaciones",
                new Object[]{"ID", "Numero", "Tipo", "Precio", "Estado"},
                tabla -> new ControladorHabitaciones().cargarTablaHabitaciones(tabla)))));
        panel.add(sideButton("Reservas", e -> openModule("reservas", new ModuleListInternalFrame(
                "Reservas",
                new Object[]{"ID", "Habitacion", "Personas", "Entrada", "Salida", "Pago"},
                tabla -> new ControladorReserva().cargarTablaReservas(tabla)))));
        panel.add(sideButton("Facturas", e -> openModule("facturas", new ModuleListInternalFrame(
                "Facturas",
                new Object[]{"ID", "Reserva", "Fecha", "Total", "Estado", "Metodo"},
                tabla -> new ControladorFacturas().cargarTablaFacturas(tabla)))));

        boolean admin = !"cliente".equalsIgnoreCase(usuario.getRolUsuario());
        JButton empleados = sideButton("Empleados", e -> openModule("empleados", new ModuleListInternalFrame(
                "Empleados",
                new Object[]{"ID", "Nombre", "Apellido", "Documento", "Cargo", "Salario", "Fecha", "Telefono", "Correo", "Direccion", "ID Usuario"},
                tabla -> new ControladorEmpleado().cargarTablaEmpleados(tabla))));
        JButton tipos = sideButton("Tipos de Habitacion", e -> openModule("tipos", new ModuleListInternalFrame(
                "Tipos de Habitacion",
                new Object[]{"ID", "Nombre", "Descripcion"},
                tabla -> new ControladorTipoHabitacion().cargarTablaTiposHabitacion(tabla))));
        empleados.setEnabled(admin);
        tipos.setEnabled(admin);
        panel.add(empleados);
        panel.add(tipos);
        panel.add(Box.createVerticalStrut(8));
        panel.add(sideButton("Cerrar sesion", e -> closeSession()));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton sideButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(9999, 44));
        button.setBackground(new Color(38, 52, 68));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.addActionListener(listener);
        return button;
    }

    private void showHome() {
        desktopPane.removeAll();

        JPanel home = new JPanel(new BorderLayout(18, 18));
        home.setBackground(new Color(245, 242, 235));
        home.setBorder(BorderFactory.createEmptyBorder(36, 36, 36, 36));

        JPanel hero = new JPanel(new BorderLayout(16, 16));
        hero.setBackground(Color.WHITE);
        hero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 175, 55), 2),
                BorderFactory.createEmptyBorder(28, 28, 28, 28)));

        JLabel title = new JLabel("Bienvenido a Hotel Gales");
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(new Color(23, 33, 43));

        JLabel text = new JLabel("<html><body style='width: 620px'>"
                + "Este panel concentra los modulos principales del hotel. "
                + "Desde aqui puedes administrar clientes, reservas, habitaciones, "
                + "facturacion y los elementos operativos del sistema."
                + "</body></html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 18));
        text.setForeground(new Color(74, 85, 104));

        hero.add(title, BorderLayout.NORTH);
        hero.add(text, BorderLayout.CENTER);
        home.add(hero, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 16, 16));
        cards.setOpaque(false);
        cards.add(card("Clientes", "Gestion de perfiles y datos de huespedes"));
        cards.add(card("Habitaciones", "Estado, tarifas y disponibilidad"));
        cards.add(card("Reservas", "Control de entrada y salida"));
        cards.add(card("Facturas", "Cobros y comprobantes"));
        cards.add(card("Empleados", "Personal y roles internos"));
        cards.add(card("Tipologias", "Clasificacion de habitaciones"));
        home.add(cards, BorderLayout.CENTER);

        JInternalFrame homeFrame = new JInternalFrame("Inicio", false, false, false, false);
        homeFrame.setSize(980, 560);
        homeFrame.setLocation(20, 20);
        homeFrame.setLayout(new BorderLayout());
        homeFrame.add(home, BorderLayout.CENTER);
        homeFrame.setVisible(true);

        desktopPane.add(homeFrame);
        desktopPane.revalidate();
        desktopPane.repaint();
    }

    private JPanel card(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 18));
        t.setForeground(new Color(23, 33, 43));
        JLabel d = new JLabel("<html><body style='width: 180px'>" + description + "</body></html>");
        d.setForeground(new Color(71, 85, 105));
        d.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(t, BorderLayout.NORTH);
        panel.add(d, BorderLayout.CENTER);
        return panel;
    }

    private void openModule(String key, JInternalFrame frame) {
        desktopPane.removeAll();
        desktopPane.add(frame);
        frame.setVisible(true);
        frames.put(key, frame);
        desktopPane.revalidate();
        desktopPane.repaint();
    }

    private void closeSession() {
        ConexionBD.desconectar();
        dispose();
        SwingUtilities.invokeLater(() -> {
            MDILogin login = new MDILogin();
            login.setVisible(true);
            new Controlador.ControladorLogin(login);
        });
    }
}
