package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CardPanel extends JPanel {

    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color CARD_BORDER = new Color(226, 232, 240);

    public CardPanel(String title, String value) {
        setLayout(new BorderLayout(10, 6));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel iconLabel = new JLabel("\u2B50");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);
        top.add(iconLabel, BorderLayout.WEST);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(new Color(74, 85, 104));
        top.add(lblTitle, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblValue.setForeground(new Color(17, 24, 39));
        add(lblValue, BorderLayout.CENTER);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GOLD, 1),
                        new EmptyBorder(20, 20, 20, 20)));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CARD_BORDER, 1),
                        new EmptyBorder(20, 20, 20, 20)));
            }
        });
    }
}
