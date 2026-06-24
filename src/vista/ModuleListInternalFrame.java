package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
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
    private static final Color SELECTED_BG = new Color(253, 230, 138);

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
                    c.setForeground(DARK_BG);
                }
                if (isRowSelected(row)) {
                    c.setBackground(SELECTED_BG);
                    c.setForeground(DARK_BG);
                }
                return c;
            }
        };
        table.setRowHeight(32);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setShowGrid(false);
        table.setSelectionBackground(SELECTED_BG);
        table.setSelectionForeground(DARK_BG);
        table.setForeground(DARK_BG);
        table.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        TableHeaderRenderer.apply(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerRenderer.setForeground(DARK_BG);
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(TOOLBAR_BG);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel searchLabel = new JLabel("Buscar");
        searchLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        searchLabel.setForeground(DARK_BG);
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 6);
        gbc.weightx = 0;
        toolbar.add(searchLabel, gbc);

        searchField = new JTextField(16);
        searchField.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));
        searchField.setForeground(DARK_BG);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        toolbar.add(searchField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        toolbar.add(new JPanel() {{ setBackground(TOOLBAR_BG); }}, gbc);

        createBtn = createActionButton("+ Crear");
        editBtn = createActionButton("Editar");
        deleteBtn = createActionButton("Eliminar");

        createBtn.setVisible(false);
        editBtn.setVisible(false);
        deleteBtn.setVisible(false);

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 0, 0, 4);
        toolbar.add(createBtn, gbc);
        gbc.gridx = 4;
        gbc.insets = new Insets(0, 0, 0, 4);
        toolbar.add(editBtn, gbc);
        gbc.gridx = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        toolbar.add(deleteBtn, gbc);

        gbc.gridx = 6;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        toolbar.add(new JPanel() {{ setBackground(TOOLBAR_BG); }}, gbc);

        JButton refresh = createSecondaryButton("Actualizar");
        refresh.addActionListener(e -> reloadData());
        gbc.gridx = 7;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 0, 0, 6);
        toolbar.add(refresh, gbc);

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
        btn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        btn.setBackground(GOLD);
        btn.setForeground(DARK_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 165, 45)),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(HOVER_BG);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 185, 40)),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(GOLD);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 165, 45)),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)));
            }
        });
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(75, 85, 99));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(243, 244, 246));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 185, 195)),
                        BorderFactory.createEmptyBorder(8, 18, 8, 18)));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219)),
                        BorderFactory.createEmptyBorder(8, 18, 8, 18)));
            }
        });
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
