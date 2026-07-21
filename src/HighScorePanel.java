import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {

    private final gameMain mainFrame;
    private final DefaultTableModel model;

    public HighScorePanel(gameMain mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 50));

        // ★ این خیلی نازههه

        JLabel title = new JLabel("★ HIGH SCORES ★", SwingConstants.CENTER);
        title.setForeground(new Color(255, 215, 0)); // Gold Yellow
        title.setFont(new Font("Consolas", Font.BOLD, 36));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Rank", "Username", "Score", "Level", "Date"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // غيرقابل ادیت بشه
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Consolas", Font.PLAIN, 14));
        table.setBackground(new Color(45, 45, 70));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 90));
        table.setSelectionBackground(new Color(70, 70, 110));
        table.setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setOpaque(false);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Consolas", Font.BOLD, 15));
        header.setBackground(new Color(20, 20, 35));
        header.setForeground(new Color(0, 255, 255));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(100, 35));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(45, 45, 70));
        scroll.setBackground(new Color(30, 30, 50));
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 50, 10, 50),
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true)
        ));

        add(scroll, BorderLayout.CENTER);

        JButton back = new JButton("Back to Menu");
        back.setFont(new Font("Consolas", Font.BOLD, 14));
        back.setFocusPainted(false);
        back.setPreferredSize(new Dimension(160, 40));
        back.addActionListener(e -> mainFrame.changePanel("Menu"));

        JPanel south = new JPanel();
        south.setBackground(new Color(30, 30, 50));
        south.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }

    public void refresh() {
        model.setRowCount(0);
        List<String[]> rows = DatabaseManager.getTopScores(20);

        if (rows.isEmpty()) {
            model.addRow(new String[]{"—", "—", "—", "—", "No scores recorded yet!"});
        } else {
            int rank = 1;
            for (String[] row : rows) {
                String rankStr;
                switch (rank) {
                    case 1:  rankStr = " 1."; break;
                    case 2:  rankStr = " 2."; break;
                    case 3:  rankStr = " 3."; break;
                    default: rankStr = "#" + rank; break;
                }

                String username = row.length > 0 ? row[0] : "—";
                String score    = row.length > 1 ? row[1] : "0";
                String level    = row.length > 2 ? row[2] : "1";
                String date     = row.length > 3 ? row[3] : "—";

                model.addRow(new String[]{rankStr, username, score, level, date});
                rank++;
            }
        }
    }
}