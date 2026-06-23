package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Dialog.ModalityType;

public class ToastNotifier {

    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
    private static final Color ERROR_RED = new Color(239, 68, 68);
    private static final Color INFO_BLUE = new Color(59, 130, 246);

    public static void showSuccess(Window parent, String message) {
        showToast(parent, message, "\u2713", SUCCESS_GREEN);
    }

    public static void showError(Window parent, String message) {
        showToast(parent, message, "\u2717", ERROR_RED);
    }

    public static void showInfo(Window parent, String message) {
        showToast(parent, message, "\u2139", INFO_BLUE);
    }

    public static void showWarning(Window parent, String message) {
        showToast(parent, message, "\u26A0", new Color(250, 204, 21));
    }

    private static void showToast(Window parent, String message, String icon, Color accent) {
        JDialog toast = new JDialog(parent);
        toast.setUndecorated(true);
        toast.setAlwaysOnTop(true);
        toast.setFocusableWindowState(false);

        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(55, 65, 81), 1),
                        new EmptyBorder(14, 16, 14, 20))));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        iconLabel.setForeground(accent);
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 4));
        panel.add(iconLabel, BorderLayout.WEST);

        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msgLabel.setForeground(Color.WHITE);
        panel.add(msgLabel, BorderLayout.CENTER);

        toast.add(panel);
        toast.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = parent != null ? parent.getX() + parent.getWidth() - toast.getWidth() - 20
                : screenSize.width - toast.getWidth() - 20;
        int y = parent != null ? parent.getY() + 12 : 12;
        toast.setLocation(x, y);

        toast.setVisible(true);

        new Timer(3000, e -> {
            toast.dispose();
        }).start();
    }

    public static boolean showConfirm(Window parent, String message) {
        JDialog dialog = new JDialog(parent, "Confirmar", ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(380, 180);
        dialog.setLocationRelativeTo(parent);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, GOLD),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(55, 65, 81), 1),
                        new EmptyBorder(24, 24, 20, 24))));

        JLabel msgLabel = new JLabel("<html><body style='width:300px;text-align:center;'>" + message + "</body></html>");
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(msgLabel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setOpaque(false);

        JButton btnYes = new JButton("S\u00ed");
        btnYes.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnYes.setBackground(GOLD);
        btnYes.setForeground(DARK_BG);
        btnYes.setFocusPainted(false);
        btnYes.setBorder(BorderFactory.createEmptyBorder(8, 28, 8, 28));
        btnYes.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnNo = new JButton("No");
        btnNo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnNo.setBackground(new Color(55, 65, 81));
        btnNo.setForeground(Color.WHITE);
        btnNo.setFocusPainted(false);
        btnNo.setBorder(BorderFactory.createEmptyBorder(8, 28, 8, 28));
        btnNo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final boolean[] result = {false};

        btnYes.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });
        btnNo.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnYes);
        btnPanel.add(btnNo);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
        return result[0];
    }
}
