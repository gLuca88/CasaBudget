package View;

import Entity.Entrata;
import Entity.Uscita;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MovimentiTableForm extends JFrame {
    private static final long serialVersionUID = 1L;

    public MovimentiTableForm(List<Entrata> entrate, List<Uscita> uscite) {
        setTitle("Movimenti");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Tipo", "Importo", "Data", "Descrizione"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // tutte le celle non modificabili
            }
        };

        for (Entrata e : entrate) {
            model.addRow(new Object[]{
                e.getEntrataId(), "Entrata", e.getImporto() + " €", e.getData(), e.getDescrizione()
            });
        }

        for (Uscita u : uscite) {
            model.addRow(new Object[]{
                u.getUscitaId(), "Uscita", u.getImporto() + " €", u.getData(), u.getDescrizione()
            });
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(Color.LIGHT_GRAY);

        // Header
        JTableHeaderRenderer headerRenderer = new JTableHeaderRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Righe alternate (zebra striping)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private final Color evenColor = new Color(240, 248, 255);

            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? evenColor : Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Custom header renderer
    static class JTableHeaderRenderer extends DefaultTableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public JTableHeaderRenderer() {
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setBackground(new Color(0, 102, 204));
            setForeground(Color.WHITE);
            setHorizontalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(new Color(0, 102, 204));
            c.setForeground(Color.WHITE);
            return c;
        }
    }
}