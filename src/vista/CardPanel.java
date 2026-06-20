package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Tarjeta de estadísticas con sombra ligera y tipografía premium. */
public class CardPanel extends JPanel {

    public CardPanel(String title, String value) {
        setLayout(new BorderLayout(8, 8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(18, 18, 18, 18)));

        // Título
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(new Color(23, 33, 43));

        // Valor
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.PLAIN, 24));
        lblValue.setForeground(new Color(38, 52, 68));

        add(lblTitle, BorderLayout.NORTH);
        add(lblValue, BorderLayout.CENTER);
    }
}
