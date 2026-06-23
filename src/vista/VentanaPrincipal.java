package vista;

import Controlador.ControladorCliente;
import Controlador.ControladorEmpleado;
import Controlador.ControladorFacturas;
import Controlador.ControladorHabitaciones;
import Controlador.ControladorReserva;
import Controlador.ControladorTipoHabitacion;
import Controlador.ControladorUsuario;
import util.ToastNotifier;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import modelo.ConexionBD;
import modelo.Login;

public class VentanaPrincipal extends JFrame {

    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color GOLD_LIGHT = new Color(241, 196, 15);
    private static final Color DARK_SIDEBAR = new Color(17, 24, 39);
    private static final Color SIDEBAR_HOVER = new Color(31, 45, 78);
    private static final Color CONTENT_BG = new Color(245, 242, 235);
    private static final Color CARD_BORDER = new Color(226, 232, 240);
    private static final Color TEXT_MAIN = new Color(17, 24, 39);
    private static final Color TEXT_MUTED = new Color(107, 114, 128);

    private final Login usuario;
    private final JDesktopPane desktopPane;
    private final JPanel sidebar;
    private final Map<String, JInternalFrame> frames = new LinkedHashMap<>();
    private boolean sidebarCollapsed = false;
    private static final int SIDEBAR_EXPANDED = 240;
    private static final int SIDEBAR_COLLAPSED = 0;

    public VentanaPrincipal(Login usuario) {
        this.usuario = usuario;
        ConexionBD.getInstance();

        setTitle("Hotel Gales \u2022 Panel de Gesti\u00f3n");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setSize(1400, 850);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CONTENT_BG);

        sidebar = buildSidebar();
        JPanel topbar = buildTopbar();
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(232, 226, 214));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topbar, BorderLayout.NORTH);
        centerPanel.add(desktopPane, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);
        root.add(centerPanel, BorderLayout.CENTER);
        setContentPane(root);
        root.revalidate();

        SwingUtilities.invokeLater(this::showHome);
    }

    // ======================== ROLE HELPERS ========================

    private boolean isAdmin() {
        return "administrador".equalsIgnoreCase(usuario.getRolUsuario());
    }

    private boolean isRecepcionista() {
        return "recepcionista".equalsIgnoreCase(usuario.getRolUsuario());
    }

    private boolean isCliente() {
        return "cliente".equalsIgnoreCase(usuario.getRolUsuario());
    }

    private boolean isStaff() {
        return !isCliente();
    }

    private String getRoleLabel() {
        switch (usuario.getRolUsuario().toLowerCase()) {
            case "administrador":   return "Administrador";
            case "recepcionista":   return "Recepcionista";
            case "cliente":         return "Cliente";
            case "servicio_limpieza": return "Limpieza";
            default:                return usuario.getRolUsuario();
        }
    }

    // ======================== TOPBAR ========================

    private JPanel buildTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setPreferredSize(new Dimension(0, 54));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(0, 16, 0, 16)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        left.setOpaque(false);

        JButton menuBtn = new JButton("\u2630");
        menuBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        menuBtn.setForeground(TEXT_MAIN);
        menuBtn.setBackground(Color.WHITE);
        menuBtn.setFocusPainted(false);
        menuBtn.setBorderPainted(false);
        menuBtn.setContentAreaFilled(false);
        menuBtn.setOpaque(false);
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuBtn.setToolTipText("Men\u00fa lateral");
        menuBtn.addActionListener(e -> toggleSidebar());
        menuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                menuBtn.setForeground(GOLD);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                menuBtn.setForeground(TEXT_MAIN);
            }
        });
        left.add(menuBtn);

        JLabel pageTitle = new JLabel("Hotel Gales");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        pageTitle.setForeground(TEXT_MAIN);
        left.add(pageTitle);

        bar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JPanel badge = new JPanel(new GridLayout(2, 1, 0, 0));
        badge.setOpaque(false);
        badge.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel userName = new JLabel(usuario.getNombreUsuario());
        userName.setFont(new Font("SansSerif", Font.BOLD, 13));
        userName.setForeground(TEXT_MAIN);

        JLabel userRole = new JLabel(getRoleLabel());
        userRole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        userRole.setForeground(GOLD);
        userRole.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        badge.add(userName);
        badge.add(userRole);
        right.add(badge);

        JButton logoutBtn = new JButton("Salir");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setForeground(GOLD);
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setToolTipText("Cerrar sesi\u00f3n");
        logoutBtn.addActionListener(e -> closeSession());
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logoutBtn.setBackground(new Color(253, 242, 208));
                logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 165, 45), 2),
                        BorderFactory.createEmptyBorder(5, 13, 5, 13)));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                logoutBtn.setBackground(Color.WHITE);
                logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GOLD, 1),
                        BorderFactory.createEmptyBorder(6, 14, 6, 14)));
            }
        });
        right.add(logoutBtn);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        int w = sidebarCollapsed ? 0 : SIDEBAR_EXPANDED;
        sidebar.setPreferredSize(new Dimension(w, 0));
        sidebar.revalidate();
        sidebar.repaint();
    }

    // ======================== SIDEBAR ========================

    private JPanel buildSidebar() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setPreferredSize(new Dimension(SIDEBAR_EXPANDED, 0));
        outer.setBackground(DARK_SIDEBAR);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(DARK_SIDEBAR);
        content.setBorder(new EmptyBorder(0, 0, 0, 0));

        content.add(Box.createVerticalStrut(24));

        JLabel brand = new JLabel("HOTEL GALES");
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        brand.setForeground(GOLD);
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setBorder(new EmptyBorder(0, 16, 0, 16));
        content.add(brand);

        JLabel roleLabel = new JLabel(getRoleLabel().toUpperCase());
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roleLabel.setForeground(new Color(215, 220, 230));
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        content.add(roleLabel);
        content.add(Box.createVerticalStrut(20));

        content.add(sideSection("INICIO"));
        content.add(sideButton("\uD83C\uDFE0  Inicio", e -> showHome(), false));

        if (isAdmin()) {
            content.add(Box.createVerticalStrut(8));
            content.add(sideSection("ADMINISTRACI\u00d3N"));
            content.add(sideButton("\uD83D\uDC65  Usuarios", e -> openUsuarios(), false));
            content.add(sideButton("\uD83D\uDC64  Clientes", e -> openClientes(), false));
            content.add(sideButton("\uD83D\uDC68\u200D\uD83D\uDCBB  Empleados", e -> openEmpleados(), false));
            content.add(sideButton("\uD83C\uDFF7  Tipos Hab.", e -> openTipos(), false));
        }

        if (isStaff()) {
            content.add(Box.createVerticalStrut(8));
            content.add(sideSection("INSTALACIONES"));
            content.add(sideButton("\uD83D\uDEAA  Habitaciones", e -> openHabitaciones(), false));

            content.add(Box.createVerticalStrut(8));
            content.add(sideSection("GESTI\u00d3N"));
            if (!isAdmin()) {
                content.add(sideButton("\uD83D\uDC64  Clientes", e -> openClientes(), false));
            }
            content.add(sideButton("\uD83D\uDCC5  Reservas", e -> openReservas(), false));

            content.add(Box.createVerticalStrut(8));
            content.add(sideSection("FINANZAS"));
            content.add(sideButton("\uD83E\uDDFE  Facturaci\u00f3n", e -> openFacturas(), false));
        }

        if (isCliente()) {
            content.add(Box.createVerticalStrut(8));
            content.add(sideSection("MI ESTANCIA"));
            content.add(sideButton("\uD83D\uDCC5  Mis Reservas", e -> openReservas(), false));
            content.add(sideButton("\uD83E\uDDFE  Mis Facturas", e -> openFacturas(), false));
        }

        content.add(Box.createVerticalGlue());

        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(DARK_SIDEBAR);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(255, 255, 255, 25)),
                new EmptyBorder(10, 12, 10, 12)));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userIcon = new JLabel("\uD83D\uDC64  " + usuario.getNombreUsuario());
        userIcon.setForeground(new Color(215, 220, 230));
        userIcon.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.add(userIcon);

        footer.add(Box.createVerticalStrut(4));

        JButton footerLogout = new JButton("Cerrar sesi\u00f3n");
        footerLogout.setFont(new Font("SansSerif", Font.BOLD, 11));
        footerLogout.setForeground(GOLD);
        footerLogout.setBackground(new Color(17, 24, 39));
        footerLogout.setFocusPainted(false);
        footerLogout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 120), 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        footerLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footerLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerLogout.setOpaque(true);
        footerLogout.addActionListener(e -> closeSession());
        footerLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                footerLogout.setBackground(new Color(31, 45, 78));
                footerLogout.setForeground(new Color(248, 113, 113));
                footerLogout.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(248, 113, 113), 1),
                        BorderFactory.createEmptyBorder(5, 13, 5, 13)));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                footerLogout.setBackground(DARK_SIDEBAR);
                footerLogout.setForeground(GOLD);
                footerLogout.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 120), 1),
                        BorderFactory.createEmptyBorder(6, 14, 6, 14)));
            }
        });
        footer.add(footerLogout);

        content.add(footer);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        outer.add(scroll, BorderLayout.CENTER);

        return outer;
    }

    private JLabel sideSection(String title) {
        JLabel label = new JLabel(title);
        label.setForeground(new Color(255, 255, 255, 100));
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setBorder(new EmptyBorder(8, 0, 4, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        return label;
    }

    private JButton sideButton(String text, ActionListener listener, boolean active) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(0, 42));
        btn.setMinimumSize(new Dimension(0, 42));
        btn.setBackground(active ? new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 30) : DARK_SIDEBAR);
        btn.setForeground(active ? GOLD : Color.WHITE);
        btn.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        btn.addActionListener(listener);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!active) btn.setBackground(SIDEBAR_HOVER);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!active) btn.setBackground(DARK_SIDEBAR);
            }
        });
        return btn;
    }

    // ======================== ICON LOADER ========================

    private ImageIcon loadIcon(String name, int w, int h) {
        java.net.URL url = getClass().getResource("/Imagenes/" + name);
        if (url == null) return null;
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // ======================== DASHBOARD / HOME ========================

    private void showHome() {
        closeActiveFrames();

        JPanel home = new JPanel(new BorderLayout(0, 20));
        home.setBackground(CONTENT_BG);
        home.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel hero = new JPanel(new BorderLayout(12, 8));
        hero.setBackground(Color.WHITE);
        hero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                new EmptyBorder(28, 28, 28, 28)));

        JLabel title = new JLabel("Bienvenido, " + usuario.getNombreUsuario());
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT_MAIN);
        hero.add(title, BorderLayout.NORTH);

        String heroMsg = isStaff()
                ? "Panel de gesti\u00f3n del Hotel Gales. Administra clientes, reservas, habitaciones y facturaci\u00f3n desde un solo lugar."
                : "Bienvenido a Hotel Gales. Aqu\u00ed puedes consultar tus reservas y facturas.";
        JLabel text = new JLabel("<html><body style='width:500px;'>" + heroMsg + "</body></html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));
        text.setForeground(TEXT_MUTED);
        hero.add(text, BorderLayout.CENTER);

        JLabel roleBadge = new JLabel(getRoleLabel().toUpperCase());
        roleBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
        roleBadge.setForeground(GOLD);
        roleBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                new EmptyBorder(4, 12, 4, 12)));
        hero.add(roleBadge, BorderLayout.EAST);

        home.add(hero, BorderLayout.NORTH);

        JPanel cards;
        if (isStaff()) {
            cards = new JPanel(new GridLayout(2, 3, 16, 16));
            cards.setOpaque(false);
            cards.add(new CardPanel("Clientes",    count("cliente", "") + " registrados"));
            cards.add(new CardPanel("Habitaciones", count("habitaciones", "WHERE estadohabitacion='DISPONIBLE'") + " disponibles"));
            cards.add(new CardPanel("Reservas",    count("reserva", "") + " activas"));
            cards.add(new CardPanel("Facturas",    count("facturas", "WHERE estadofactura='PENDIENTE'") + " pendientes"));
            cards.add(new CardPanel("Empleados",   count("empleado", "") + " activos"));
            cards.add(new CardPanel("Tipolog\u00edas", count("tipohabitacion", "") + " configuradas"));
        } else {
            cards = new JPanel(new GridLayout(1, 3, 16, 16));
            cards.setOpaque(false);
            cards.add(new CardPanel("Habitaciones", count("habitaciones", "WHERE estadohabitacion='DISPONIBLE'") + " disponibles"));
            cards.add(new CardPanel("Mis Reservas", count("reserva", "") + " activas"));
            cards.add(new CardPanel("Mis Facturas", count("facturas", "") + " registradas"));
        }
        home.add(cards, BorderLayout.CENTER);

        JInternalFrame homeFrame = new JInternalFrame("Inicio", false, false, false, false);
        homeFrame.setLayout(new BorderLayout());
        homeFrame.add(home, BorderLayout.CENTER);
        homeFrame.setVisible(true);

        desktopPane.add(homeFrame);
        SwingUtilities.invokeLater(() -> {
            homeFrame.setSize(desktopPane.getWidth() - 40, desktopPane.getHeight() - 40);
            homeFrame.setLocation(20, 20);
            homeFrame.revalidate();
        });
        desktopPane.revalidate();
        desktopPane.repaint();
    }

    // ======================== MODULE OPENERS ========================

    private void openClientes()        { openModule("clientes",   buildClientesModule()); }
    private void openHabitaciones()   { openModule("habitaciones", buildHabitacionesModule()); }
    private void openReservas()       { openModule("reservas", buildReservasModule()); }
    private void openFacturas()       { openModule("facturas", buildFacturasModule()); }
    private void openEmpleados()      { openModule("empleados", buildEmpleadosModule()); }
    private void openTipos()          { openModule("tipos",      buildTiposModule()); }
    private void openUsuarios()       { openModule("usuarios",  buildUsuariosModule()); }

    private ModuleListInternalFrame buildClientesModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Clientes",
                new Object[]{"ID","Nombre","Apellido","Documento","Correo","Telefono","Direccion","Usuario"},
                tabla -> new ControladorCliente().cargarTablaClientes(tabla));
        f.setCreateAction(e -> new DialogCliente(this, -1, f::triggerReload).setVisible(true));
        f.setEditAction(e   -> { int id = f.getSelectedId(); if (id!=-1) new DialogCliente(this, id, f::triggerReload).setVisible(true); else ToastNotifier.showError(this,"Seleccione un cliente."); });
        f.setDeleteAction(e -> { int id = f.getSelectedId(); if (id!=-1 && ToastNotifier.showConfirm(this,"\u00bfEliminar cliente?")) { new ControladorCliente().eliminarCliente(id); f.triggerReload(); ToastNotifier.showSuccess(this, "Cliente eliminado."); } });
        return f;
    }

    private ModuleListInternalFrame buildHabitacionesModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Habitaciones",
                new Object[]{"ID","N\u00famero","Tipo","Precio","Estado"},
                tabla -> new ControladorHabitaciones().cargarTablaHabitaciones(tabla));
        f.setCreateAction(e -> new DialogHabitacion(this, -1, f::triggerReload).setVisible(true));
        f.setEditAction(e   -> { int id = f.getSelectedId(); if (id!=-1) new DialogHabitacion(this, id, f::triggerReload).setVisible(true); else ToastNotifier.showError(this,"Seleccione una habitaci\u00f3n."); });
        f.setDeleteAction(e -> { int id = f.getSelectedId(); if (id!=-1 && ToastNotifier.showConfirm(this,"\u00bfEliminar habitaci\u00f3n?")) { new ControladorHabitaciones().eliminarHabitacion(id); f.triggerReload(); ToastNotifier.showSuccess(this, "Habitaci\u00f3n eliminada."); } });
        return f;
    }

    private ModuleListInternalFrame buildReservasModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Reservas",
                new Object[]{"ID","Habitaci\u00f3n","Personas","Entrada","Salida","Pago","ID Cliente"},
                tabla -> new ControladorReserva().cargarTablaReservas(tabla));
        f.setCreateAction(e -> new DialogReserva(this, -1, f::triggerReload).setVisible(true));
        if (isAdmin()) {
            f.setEditAction(e   -> { int id = f.getSelectedId(); if (id!=-1) new DialogReserva(this, id, f::triggerReload).setVisible(true); else ToastNotifier.showError(this,"Seleccione una reserva."); });
            f.setDeleteAction(e -> { int id = f.getSelectedId(); if (id!=-1 && ToastNotifier.showConfirm(this,"\u00bfEliminar reserva?")) { new ControladorReserva().eliminarReserva(id); f.triggerReload(); ToastNotifier.showSuccess(this, "Reserva eliminada."); } });
        }
        return f;
    }

    private ModuleListInternalFrame buildFacturasModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Facturas",
                new Object[]{"ID","Reserva","Fecha","Total","Estado","M\u00e9todo"},
                tabla -> new ControladorFacturas().cargarTablaFacturas(tabla));
        if (isAdmin()) {
            f.setEditAction(e   -> { int id = f.getSelectedId(); if (id!=-1) {/*TODO: dialogo edici\u00f3n*/} else ToastNotifier.showError(this,"Seleccione una factura."); });
        }
        return f;
    }

    private ModuleListInternalFrame buildEmpleadosModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Empleados",
                new Object[]{"ID","Nombre","Apellido","Documento","Cargo","Salario","Fecha","Tel\u00e9fono","Correo","Direcci\u00f3n","Usuario"},
                tabla -> new ControladorEmpleado().cargarTablaEmpleados(tabla));
        if (isAdmin()) {
            f.setCreateAction(e -> new DialogEmpleado(this, -1, f::triggerReload).setVisible(true));
            f.setEditAction(e   -> { int id = f.getSelectedId(); if (id!=-1) new DialogEmpleado(this, id, f::triggerReload).setVisible(true); else ToastNotifier.showError(this,"Seleccione un empleado."); });
            f.setDeleteAction(e -> { int id = f.getSelectedId(); if (id!=-1 && ToastNotifier.showConfirm(this,"\u00bfEliminar empleado?")) { new ControladorEmpleado().eliminarEmpleado(id); f.triggerReload(); ToastNotifier.showSuccess(this, "Empleado eliminado."); } });
        }
        return f;
    }

    private ModuleListInternalFrame buildTiposModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Tipos de Habitaci\u00f3n",
                new Object[]{"ID","Nombre","Descripci\u00f3n"},
                tabla -> new ControladorTipoHabitacion().cargarTablaTiposHabitacion(tabla));
        if (isAdmin()) {
            f.setCreateAction(e -> new DialogTipoHabitacion(this, -1, f::triggerReload).setVisible(true));
            f.setEditAction(e   -> { int id = f.getSelectedId(); if (id!=-1) new DialogTipoHabitacion(this, id, f::triggerReload).setVisible(true); else ToastNotifier.showError(this,"Seleccione un tipo."); });
            f.setDeleteAction(e -> { int id = f.getSelectedId(); if (id!=-1 && ToastNotifier.showConfirm(this,"\u00bfEliminar tipo?")) { new ControladorTipoHabitacion().eliminarTipoHabitacion(id); f.triggerReload(); ToastNotifier.showSuccess(this, "Tipo eliminado."); } });
        }
        return f;
    }

    private ModuleListInternalFrame buildUsuariosModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Usuarios",
                new Object[]{"ID","Usuario","Rol"},
                tabla -> new ControladorUsuario().cargarTablaUsuarios(tabla));
        f.setCreateAction(e -> new DialogUsuario(this, -1, f::triggerReload).setVisible(true));
        f.setEditAction(e   -> { int id = f.getSelectedId(); if (id!=-1) new DialogUsuario(this, id, f::triggerReload).setVisible(true); else ToastNotifier.showError(this,"Seleccione un usuario."); });
        f.setDeleteAction(e -> { int id = f.getSelectedId(); if (id!=-1 && ToastNotifier.showConfirm(this,"\u00bfEliminar usuario?")) { new ControladorUsuario().eliminarUsuario(id); f.triggerReload(); ToastNotifier.showSuccess(this, "Usuario eliminado."); } });
        return f;
    }

    // ======================== MODULE MANAGEMENT ========================

    private void openModule(String key, JInternalFrame frame) {
        JInternalFrame existing = frames.get(key);
        if (existing != null && existing.isDisplayable()) {
            try { existing.setIcon(false); existing.setSelected(true); existing.toFront(); }
            catch (Exception ignored) { existing.setVisible(true); }
            return;
        }
        closeActiveFrames();
        desktopPane.add(frame);
        frame.setVisible(true);
        frames.put(key, frame);
        SwingUtilities.invokeLater(() -> {
            frame.setSize(desktopPane.getWidth() - 40, desktopPane.getHeight() - 40);
            frame.setLocation(20, 20);
            frame.revalidate();
        });
        desktopPane.revalidate();
        desktopPane.repaint();
    }

    private void closeActiveFrames() {
        for (JInternalFrame f : frames.values()) { f.dispose(); }
        frames.clear();
        desktopPane.removeAll();
    }

    // ======================== SESSION ========================

    private void closeSession() {
        ConexionBD.desconectar();
        dispose();
        SwingUtilities.invokeLater(() -> {
            MDILogin login = new MDILogin();
            login.setVisible(true);
            new Controlador.ControladorLogin(login);
        });
    }

    // ======================== DB HELPERS ========================

    private int count(String table, String where) {
        try {
            String sql = "SELECT COUNT(*) FROM " + table + (where.isEmpty() ? "" : " " + where);
            java.sql.PreparedStatement st = modelo.ConexionBD.conexion.prepareStatement(sql);
            java.sql.ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.err.println("Error contando " + table + ": " + e.getMessage());
        }
        return 0;
    }
}
