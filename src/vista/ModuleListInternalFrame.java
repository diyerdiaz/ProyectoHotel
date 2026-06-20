package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Objects;
import java.util.function.Consumer;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ModuleListInternalFrame extends JInternalFrame {
    private final JTable table;
    private final DefaultTableModel model;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final Consumer<JTable> loader;
    private final JTextField searchField;

    public ModuleListInternalFrame(String title, Object[] columns, Consumer<JTable> loader) {
        super(title, true, true, true, true);
        this.loader = Objects.requireNonNull(loader, "loader");

        setSize(980, 560);
        setLocation(20, 20);

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        toolbar.add(new JLabel("Buscar"));

        searchField = new JTextField(24);
        toolbar.add(searchField);

        JButton refresh = new JButton("Actualizar");
        refresh.addActionListener(e -> reloadData());
        toolbar.add(refresh);

        JButton clear = new JButton("Limpiar");
        clear.addActionListener(e -> {
            searchField.setText("");
            applyFilter();
        });
        toolbar.add(clear);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        reloadData();
    }

    private void reloadData() {
        model.setRowCount(0);
        loader.accept(table);
        applyFilter();
    }

    private void applyFilter() {
        String text = searchField.getText();
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text.trim())));
        }
    }
}
