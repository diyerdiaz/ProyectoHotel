package vista;

import Controlador.ControladorCliente;
import Controlador.ControladorEmpleado;
import Controlador.ControladorFacturas;
import Controlador.ControladorHabitaciones;
import Controlador.ControladorReserva;
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

    // ─── Paleta base ────────────────────────────────────────────────────────────
    private static final Color GOLD         = new Color(212, 175, 55);
    private static final Color GOLD_LIGHT   = new Color(241, 196, 15);
    private static final Color DARK_SIDEBAR = new Color(17, 24, 39);
    private static final Color SIDEBAR_HOVER= new Color(31, 45, 78);
    private static final Color CONTENT_BG   = new Color(245, 242, 235);
    private static final Color CARD_BORDER  = new Color(226, 232, 240);
    private static final Color TEXT_MAIN    = new Color(17, 24, 39);
    private static final Color TEXT_MUTED   = new Color(107, 114, 128);

    // ─── Paleta Super-Admin (púrpura) ────────────────────────────────────────────
    private static final Color PURPLE      = new Color(147, 51, 234);
    private static final Color PURPLE_LIGHT= new Color(243, 232, 255);
    private static final Color PURPLE_DARK = new Color(126, 34, 206);

    // ─── Estado ─────────────────────────────────────────────────────────────────
    private final Login  usuario;
    private final JDesktopPane desktopPane;
    private final JPanel sidebar;
    private final Map<String, JInternalFrame> frames = new LinkedHashMap<>();
    private boolean sidebarCollapsed = false;
    /** true cuando esta ventana es una simulación lanzada por el Super Admin */
    private final boolean isPreviewMode;

    private static final int SIDEBAR_EXPANDED  = 240;
    private static final int SIDEBAR_COLLAPSED = 0;

    // ======================== CONSTRUCTORES ========================

    /** Constructor normal — modo real */
    public VentanaPrincipal(Login usuario) {
        this(usuario, false);
    }

    /** Constructor primario — soporta modo vista previa */
    public VentanaPrincipal(Login usuario, boolean previewMode) {
        this.usuario       = usuario;
        this.isPreviewMode = previewMode;
        ConexionBD.getInstance();

        String suffix = previewMode
                ? "  \u2756  VISTA PREVIA  [\u00a0" + getRoleLabel() + "\u00a0]"
                : "";
        setTitle("Hotel Gales \u2022 Panel de Gesti\u00f3n" + suffix);
        setDefaultCloseOperation(previewMode ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
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
            case "administrador":     return "Administrador";
            case "recepcionista":     return "Recepcionista";
            case "cliente":           return "Cliente";
            case "servicio_limpieza": return "Limpieza";
            default:                  return usuario.getRolUsuario();
        }
    }

    // ======================== TOPBAR ========================

    private JPanel buildTopbar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        // ── Banner de MODO VISTA PREVIA ──────────────────────────────────────────
        if (isPreviewMode) {
            JPanel banner = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
            banner.setBackground(new Color(234, 88, 12));   // naranja
            JLabel lbl = new JLabel(
                    "\u26A0\uFE0F   MODO VISTA PREVIA  \u2014  ROL: "
                    + getRoleLabel().toUpperCase()
                    + "   \u2502   Esta ventana es solo de visualizaci\u00f3n."
                    + " Ci\u00e9rrala para volver al panel del administrador.");
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            lbl.setForeground(Color.WHITE);
            banner.add(lbl);
            wrapper.add(banner, BorderLayout.NORTH);
        }

        // ── Barra principal ──────────────────────────────────────────────────────
        JPanel bar = new JPanel(new BorderLayout());
        bar.setPreferredSize(new Dimension(0, 54));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(0, 16, 0, 16)));

        // ── Izquierda ────────────────────────────────────────────────────────────
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
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { menuBtn.setForeground(GOLD); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { menuBtn.setForeground(TEXT_MAIN); }
        });
        left.add(menuBtn);

        JLabel pageTitle = new JLabel("Hotel Gales");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        pageTitle.setForeground(TEXT_MAIN);
        left.add(pageTitle);

        bar.add(left, BorderLayout.WEST);

        // ── Derecha ──────────────────────────────────────────────────────────────
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        // ── 🎨 Botón color de fondo (siempre visible) ────────────────────────────
        JButton colorBtn = makeIconBtn("\uD83C\uDFA8", new Color(59, 130, 246));
        colorBtn.setToolTipText("Cambiar color del fondo de trabajo");
        colorBtn.addActionListener(e -> {
            Color elegido = JColorChooser.showDialog(
                    this, "Elige el color del \u00e1rea de trabajo", desktopPane.getBackground());
            if (elegido != null) desktopPane.setBackground(elegido);
        });
        right.add(colorBtn);

        // ── 👁 Botón Vista de Rol — EXCLUSIVO del admin, oculto en modo preview ──
        if (isAdmin() && !isPreviewMode) {
            JButton previewBtn = new JButton("\uD83D\uDC41\uFE0F  Vista de Rol");
            previewBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
            previewBtn.setForeground(PURPLE);
            previewBtn.setBackground(Color.WHITE);
            previewBtn.setFocusPainted(false);
            previewBtn.setOpaque(true);
            previewBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PURPLE, 1),
                    BorderFactory.createEmptyBorder(6, 14, 6, 14)));
            previewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            previewBtn.setToolTipText(
                    "Ver la interfaz tal como la ve cada rol \u2014 exclusivo del administrador");
            previewBtn.addActionListener(e -> openRolePreview());
            previewBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    previewBtn.setBackground(PURPLE_LIGHT);
                    previewBtn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(PURPLE_DARK, 2),
                            BorderFactory.createEmptyBorder(5, 13, 5, 13)));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    previewBtn.setBackground(Color.WHITE);
                    previewBtn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(PURPLE, 1),
                            BorderFactory.createEmptyBorder(6, 14, 6, 14)));
                }
            });
            right.add(previewBtn);
        }

        // ── Badge de usuario ─────────────────────────────────────────────────────
        JPanel badge = new JPanel(new GridLayout(2, 1, 0, 0));
        badge.setOpaque(false);
        badge.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel userName = new JLabel(usuario.getNombreUsuario());
        userName.setFont(new Font("SansSerif", Font.BOLD, 13));
        userName.setForeground(TEXT_MAIN);

        JLabel userRole = new JLabel(getRoleLabel());
        userRole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        // En preview el rol aparece en naranja para recordar que es simulación
        userRole.setForeground(isPreviewMode ? new Color(234, 88, 12) : GOLD);

        badge.add(userName);
        badge.add(userRole);
        right.add(badge);

        // ── Botón Salir / Cerrar Preview ─────────────────────────────────────────
        JButton logoutBtn = new JButton(isPreviewMode ? "Cerrar Preview" : "Salir");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setForeground(GOLD);
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setToolTipText(isPreviewMode ? "Cerrar vista previa" : "Cerrar sesi\u00f3n");
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
        wrapper.add(bar, BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * Crea un botón icono compacto con efecto hover de color acento.
     */
    private JButton makeIconBtn(String emoji, Color accent) {
        JButton btn = new JButton(emoji);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btn.setForeground(accent);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        Color bg20 = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 20);
        Color bd80 = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 100);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setOpaque(true);
                btn.setBackground(bg20);
                btn.setBorderPainted(true);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bd80, 1),
                        BorderFactory.createEmptyBorder(7, 7, 7, 7)));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setOpaque(false);
                btn.setBackground(Color.WHITE);
                btn.setBorderPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            }
        });
        return btn;
    }

    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        int w = sidebarCollapsed ? 0 : SIDEBAR_EXPANDED;
        sidebar.setPreferredSize(new Dimension(w, 0));
        sidebar.revalidate();
        sidebar.repaint();
    }

    // ======================== SUPER ADMIN — VISTA DE ROL ========================

    /**
     * Abre el diálogo de selección de rol y lanza la ventana de preview.
     * Solo accesible para el administrador.
     */
    private void openRolePreview() {
        String[] labels     = {
            "\uD83C\uDFAB  Recepcionista",
            "\uD83D\uDC64  Cliente",
            "\uD83E\uDDF9  Limpieza (servicio_limpieza)"
        };
        String[] roleValues = {"recepcionista", "cliente", "servicio_limpieza"};

        // ── Construir panel del diálogo ──────────────────────────────────────────
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBorder(new EmptyBorder(12, 12, 8, 12));

        JLabel header = new JLabel("Selecciona el rol a previsualizar:");
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(header, BorderLayout.NORTH);

        JPanel rolePanel = new JPanel(new GridLayout(labels.length, 1, 4, 8));
        rolePanel.setOpaque(false);
        ButtonGroup group  = new ButtonGroup();
        JRadioButton[] radios = new JRadioButton[labels.length];

        for (int i = 0; i < labels.length; i++) {
            radios[i] = new JRadioButton(labels[i]);
            radios[i].setFont(new Font("SansSerif", Font.PLAIN, 13));
            radios[i].setFocusPainted(false);
            group.add(radios[i]);
            rolePanel.add(radios[i]);
        }
        radios[0].setSelected(true);
        panel.add(rolePanel, BorderLayout.CENTER);

        JLabel note = new JLabel(
                "<html><font color='#888888'><i>"
                + "La ventana de preview se abre de forma independiente.<br>"
                + "Ci\u00e9rrala para volver al panel del administrador."
                + "</i></font></html>");
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        panel.add(note, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                this, panel,
                "\uD83D\uDC41\uFE0F  Vista Previa de Rol  \u2014  Super Admin",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < radios.length; i++) {
                if (radios[i].isSelected()) {
                    final String rol = roleValues[i];
                    SwingUtilities.invokeLater(() -> {
                        VentanaPrincipal preview = new VentanaPrincipal(buildPreviewLogin(rol), true);
                        preview.setVisible(true);
                    });
                    break;
                }
            }
        }
    }

    /**
     * Crea un objeto Login temporal sin ID de BD, usado solo para la vista previa.
     */
    private Login buildPreviewLogin(String rol) {
        Login preview = new Login();
        preview.setIdUsuario(-1);
        preview.setNombreUsuario("Vista Previa");
        preview.setContrasenaUsuario("");
        preview.setRolUsuario(rol);
        return preview;
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

        // Indicador extra en el sidebar cuando es preview
        if (isPreviewMode) {
            JLabel previewTag = new JLabel("[VISTA PREVIA]");
            previewTag.setAlignmentX(Component.CENTER_ALIGNMENT);
            previewTag.setForeground(new Color(234, 88, 12));
            previewTag.setFont(new Font("SansSerif", Font.BOLD, 10));
            content.add(previewTag);
        }

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
            content.add(sideButton("\uD83D\uDC65  Usuarios",   e -> openUsuarios(), false));
            content.add(sideButton("\uD83D\uDC64  Clientes",   e -> openClientes(), false));
            content.add(sideButton("\uD83D\uDC68\u200D\uD83D\uDCBB  Empleados", e -> openEmpleados(), false));
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
            content.add(sideButton("\uD83D\uDCCA  Estad\u00edsticas", e -> showHome(), false));
            content.add(sideButton("\uD83E\uDDFE  Facturaci\u00f3n",  e -> openFacturas(), false));
        }

        if (isCliente()) {
            content.add(Box.createVerticalStrut(8));
            content.add(sideSection("MI ESTANCIA"));
            content.add(sideButton("\uD83D\uDCC5  Mis Reservas", e -> openReservas(), false));
            content.add(sideButton("\uD83E\uDDFE  Mis Facturas",  e -> openFacturas(), false));
        }

        content.add(Box.createVerticalGlue());

        // ── Footer del sidebar ───────────────────────────────────────────────────
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

        JButton footerLogout = new JButton(isPreviewMode ? "Cerrar Preview" : "Cerrar sesi\u00f3n");
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

        PanelEstadisticas panel = new PanelEstadisticas(usuario);
        JScrollPane scroll = new JScrollPane(panel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CONTENT_BG);

        JInternalFrame homeFrame = new JInternalFrame("Inicio", false, false, false, false);
        homeFrame.setLayout(new BorderLayout());
        homeFrame.add(scroll, BorderLayout.CENTER);
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

    private void openClientes()      { openModule("clientes",     buildClientesModule()); }
    private void openHabitaciones()  { openModule("habitaciones", buildHabitacionesModule()); }
    private void openReservas()      { openModule("reservas",     buildReservasModule()); }
    private void openFacturas()      { openModule("facturas",     buildFacturasModule()); }
    private void openEmpleados()     { openModule("empleados",    buildEmpleadosModule()); }
    private void openUsuarios()      { openModule("usuarios",     buildUsuariosModule()); }

    private ModuleListInternalFrame buildClientesModule() {
        ModuleListInternalFrame f = new ModuleListInternalFrame(
                "Clientes",
                new Object[]{"ID","Nombre","Apellido","Documento","Correo","Telefono","Direcci\u00f3n","Usuario"},
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
                new Object[]{"ID","Reserva","Fecha","Total","Estado","M\u00e9todo","Acciones"},
                tabla -> new ControladorFacturas().cargarTablaFacturasConAcciones(tabla));

        f.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = f.getTable().rowAtPoint(e.getPoint());
                int col = f.getTable().columnAtPoint(e.getPoint());
                if (row >= 0 && col == 6) {
                    int idFactura = f.getSelectedIdFromRow(row);
                    if (idFactura != -1) {
                        new DialogFactura(VentanaPrincipal.this, idFactura).setVisible(true);
                    }
                }
            }
        });

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
        if (isPreviewMode) {
            // En modo preview solo cierra esta ventana, NO destruye la sesión real
            dispose();
            return;
        }
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
