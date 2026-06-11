package digielancer.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HistoryInvoicePanel extends javax.swing.JPanel {

    public HistoryInvoicePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        // Outer container like dashboard card
        JPanel container = new JPanel();
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEtchedBorder());
        container.setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("Riwayat Invoice");
        title.setFont(new Font("Inter", Font.BOLD, 14));
        title.setForeground(new Color(15, 23, 42));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        container.add(title, BorderLayout.PAGE_START);

        // Static table/list mock
        String[] cols = {"No", "Nomor Nota", "Tanggal", "Project", "Total"};
        String[][] rows = {
                {"1", "INV-2026-001", "08 Juni 2026", "Nama Project A", "Rp 5.000.000"},
                {"2", "INV-2026-002", "10 Juni 2026", "Nama Project B", "Rp 7.500.000"},
                {"3", "INV-2026-003", "12 Juni 2026", "Nama Project C", "Rp 3.200.000"}
        };

        JTable table = new JTable(rows, cols);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setFont(new Font("Inter", Font.PLAIN, 12));
        table.setEnabled(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        container.add(scroll, BorderLayout.CENTER);

        // Bottom preview panel (static card)
        JPanel preview = new JPanel();
        preview.setBackground(new Color(248, 250, 252));
        preview.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));
        preview.setLayout(new BorderLayout());

        JPanel invoiceCard = new JPanel();
        invoiceCard.setBackground(Color.WHITE);
        invoiceCard.setBorder(BorderFactory.createEtchedBorder());
        invoiceCard.setLayout(new BorderLayout());

        JLabel invoiceHeader = new JLabel("Preview Nota (Static)");
        invoiceHeader.setFont(new Font("Inter", Font.BOLD, 12));
        invoiceHeader.setForeground(new Color(6, 141, 240));
        invoiceHeader.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
        invoiceCard.add(invoiceHeader, BorderLayout.PAGE_START);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new GridLayout(4, 2, 10, 8));
        details.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        details.add(new JLabel("Nomor"));
        details.add(new JLabel("INV-2026-001"));
        details.add(new JLabel("Tanggal"));
        details.add(new JLabel("08 Juni 2026"));
        details.add(new JLabel("Kepada"));
        details.add(new JLabel("Nama Klien"));
        details.add(new JLabel("Total"));
        details.add(new JLabel("Rp 5.000.000"));

        invoiceCard.add(details, BorderLayout.CENTER);
        preview.add(invoiceCard, BorderLayout.CENTER);

        container.add(preview, BorderLayout.PAGE_END);

        add(container, BorderLayout.CENTER);
    }
}

