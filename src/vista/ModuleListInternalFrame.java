package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Objects;
import java.util.function.Consumer;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Box;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ModuleListInternalFrame extends JInternalFrame {
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_BG = new Color(17, 24, 39);
    private static final Color TOOLBAR_BG = new Color(249, 250, 251);
    private static final Color HOVER_BG = new Color(241, 196, 15);

    private final JTable table;
    private final DefaultTableModel model;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final Consumer<JTable> loader;
    private final JTextField searchField;
    private final JButton createBtn;
    private final JButton editBtn;
    private final JButton deleteBtn;

    public ModuleListInternalFrame(String title, Object[] columns, Consumer<JTable> loader) {
        super(title, true, true, true, true);
        this.loader = Objects.requireNonNull(loader, "loader");

        setSize(1000, 600);
        setLocation(20, 20);
        setVisible(false);

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                }
                if (isRowSelected(row)) {
                    c.setBackground(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 30));
                }
                return c;
            }
        };
        table.setRowHeight(32);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setShowGrid(false);
        table.setSelectionBackground(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 40));
        table.setSelectionForeground(DARK_BG);
        table.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        TableHeaderRenderer.apply(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(TOOLBAR_BG);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JLabel searchLabel = new JLabel("Buscar");
        searchLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        searchLabel.setForeground(DARK_BG);
        toolbar.add(searchLabel);

        searchField = new JTextField(28);
        searchField.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        toolbar.add(searchField);

        toolbar.add(Box.createHorizontalStrut(8));

        createBtn = createActionButton("+ Crear");
        editBtn = createActionButton("Editar");
        deleteBtn = createActionButton("Eliminar");

        createBtn.setVisible(false);
        editBtn.setVisible(false);
        deleteBtn.setVisible(false);

        toolbar.add(createBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        toolbar.add(Box.createHorizontalGlue());

        JButton refresh = createSecondaryButton("Actualizar");
        refresh.addActionListener(e -> reloadData());
        toolbar.add(refresh);

        JButton clear = createSecondaryButton("Limpiar");
        clear.addActionListener(e -> {
            searchField.setText("");
            applyFilter();
        });
        toolbar.add(clear);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        reloadData();
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11));
        btn.setBackground(GOLD);
        btn.setForeground(DARK_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(HOVER_BG); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(GOLD); }
        });
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(107, 114, 128));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    private void reloadData() {
        model.setRowCount(0);
        loader.accept(table);
        applyFilter();
        if (table.getRowCount() > 0 && table.getSelectionModel().isSelectionEmpty()) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void applyFilter() {
        String text = searchField.getText();
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text.trim())));
        }
    }

    public void setCreateAction(ActionListener l) {
        createBtn.addActionListener(l);
        createBtn.setVisible(true);
    }

    public void setEditAction(ActionListener l) {
        editBtn.addActionListener(l);
        editBtn.setVisible(true);
    }

    public void setDeleteAction(ActionListener l) {
        deleteBtn.addActionListener(l);
        deleteBtn.setVisible(true);
    }

    public int getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) return -1;
        int modelRow = table.convertRowIndexToModel(row);
        Object value = model.getValueAt(modelRow, 0);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException ex) {
                return -1;
            }
        }
    }

    public void triggerReload() {
        reloadData();
    }
}
