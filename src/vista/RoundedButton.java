package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/** Botón con esquinas redondeadas y efecto “lift” al pasar el mouse. */
public class RoundedButton extends JButton {

    private Color hoverBackground = new Color(56, 76, 105);
    private Color pressedBackground = new Color(35, 54, 78);
    private Color defaultBackground = getBackground();

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorderPainted(false);
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setMargin(new Insets(6, 12, 6, 12));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                defaultBackground = getBackground();
                setBackground(hoverBackground);
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                setBackground(defaultBackground);
                repaint();
            }
            @Override public void mousePressed(MouseEvent e) {
                setBackground(pressedBackground);
            }
            @Override public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackground);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        super.paintComponent(g2);
        g2.dispose();
    }
}
