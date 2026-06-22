package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class TableHeaderRenderer implements TableCellRenderer {

    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color HEADER_BG = new Color(17, 24, 39);

    private final Font headerFont = new Font("SansSerif", Font.BOLD, 12);

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel lbl = new JLabel(value != null ? value.toString() : "");
        lbl.setOpaque(true);
        lbl.setBackground(HEADER_BG);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(headerFont);
        lbl.setHorizontalAlignment(SwingConstants.LEADING);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, GOLD),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        return lbl;
    }

    public static void apply(JTable table) {
        TableHeaderRenderer renderer = new TableHeaderRenderer();
        JTableHeader th = table.getTableHeader();
        th.setDefaultRenderer(renderer);
        th.setPreferredSize(new Dimension(th.getWidth(), 38));
        th.setBackground(HEADER_BG);
    }
}
