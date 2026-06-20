package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/** Renderiza los encabezados de JTable con fondo azul‑oscuro y texto blanco. */
public class TableHeaderRenderer implements TableCellRenderer {

    private final Font headerFont = new Font("SansSerif", Font.BOLD, 13);
    private final Color background = new Color(23, 33, 43);
    private final Color foreground = Color.WHITE;

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel lbl = new JLabel(value != null ? value.toString() : "");
        lbl.setOpaque(true);
        lbl.setBackground(background);
        lbl.setForeground(foreground);
        lbl.setFont(headerFont);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        return lbl;
    }

    /** Conveniencia: aplicar a una tabla existente */
    public static void apply(JTable table) {
        TableHeaderRenderer renderer = new TableHeaderRenderer();
        JTableHeader th = table.getTableHeader();
        th.setDefaultRenderer(renderer);
        th.setPreferredSize(new Dimension(th.getWidth(), 30));
    }
}
