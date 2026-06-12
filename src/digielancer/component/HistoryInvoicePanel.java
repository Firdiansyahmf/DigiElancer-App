package digielancer.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryInvoicePanel extends javax.swing.JPanel {

    // Model class for Invoices
    private static class Invoice {
        int id;
        String invoiceNumber;
        String clientName;
        String projectName;
        String date;
        String status; // "Pending", "Dibayar", "Overdue"
        String amount;
        double rawAmount;
        List<String> addOnsList;
        
        public Invoice(int id, String invoiceNumber, String clientName, String projectName, String date, String status, String amount, double rawAmount, List<String> addOnsList) {
            this.id = id;
            this.invoiceNumber = invoiceNumber;
            this.clientName = clientName;
            this.projectName = projectName;
            this.date = date;
            this.status = status;
            this.amount = amount;
            this.rawAmount = rawAmount;
            this.addOnsList = addOnsList;
        }
    }

    private List<Invoice> invoices;
    private JPanel cardsPanel;
    private Container lastGrandParent = null;
    private RoundedButton btnHistory;

    public HistoryInvoicePanel() {
        invoices = new ArrayList<>();
        loadInvoicesFromDB();
        initComponents();
    }

    private void loadInvoicesFromDB() {
        invoices.clear();
        int currentUserId = digielancer.main.UserSession.getId();
        try (Connection conn = digielancer.main.KoneksiDB.configDB()) {
            String sql = "SELECT i.id, i.invoice_number, i.total_amount, i.status, i.generated_date, p.client_name, ms.service_name " +
                         "FROM invoice i " +
                         "JOIN project p ON i.project_id = p.id " +
                         "LEFT JOIN main_service ms ON p.main_service_id = ms.id " +
                         "WHERE p.user_id = ? " +
                         "ORDER BY i.generated_date DESC";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, currentUserId);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        int invId = rs.getInt("id");
                        String invoiceNumber = rs.getString("invoice_number");
                        String clientName = rs.getString("client_name");
                        String projectName = rs.getString("service_name");
                        double totalAmount = rs.getDouble("total_amount");
                        String dbStatus = rs.getString("status");
                        Timestamp genDate = rs.getTimestamp("generated_date");
                        
                        // Map status: 'Pending' -> 'Pending', 'Paid' -> 'Dibayar', 'Overdue' -> 'Overdue'
                        String displayStatus = "Pending";
                        if ("Paid".equalsIgnoreCase(dbStatus)) {
                            displayStatus = "Dibayar";
                        } else if ("Overdue".equalsIgnoreCase(dbStatus)) {
                            displayStatus = "Overdue";
                        }

                        // Format date
                        String dateFormatted = "Baru Saja";
                        if (genDate != null) {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy", new java.util.Locale("id", "ID"));
                            dateFormatted = sdf.format(new java.util.Date(genDate.getTime()));
                        }

                        // Format amount
                        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("in", "ID"));
                        String amountFormatted = "Rp " + nf.format(totalAmount);

                        // Load items for this invoice
                        List<String> items = new ArrayList<>();
                        String sqlItems = "SELECT item_description, snapshot_price FROM invoice_item WHERE invoice_id = ?";
                        try (PreparedStatement pstItems = conn.prepareStatement(sqlItems)) {
                            pstItems.setInt(1, invId);
                            try (ResultSet rsItems = pstItems.executeQuery()) {
                                while (rsItems.next()) {
                                    String desc = rsItems.getString("item_description");
                                    double price = rsItems.getObject("snapshot_price") != null ? rsItems.getDouble("snapshot_price") : 0.0;
                                    items.add(desc + " (Rp " + nf.format(price) + ")");
                                }
                            }
                        }

                        invoices.add(new Invoice(
                            invId,
                            invoiceNumber,
                            clientName,
                            projectName != null ? projectName : "N/A",
                            dateFormatted,
                            displayStatus,
                            amountFormatted,
                            totalAmount,
                            items
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat invoice dari DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateInvoiceStatusInDB(int invId, String displayStatus) {
        String dbStatus = "Pending";
        if ("Dibayar".equalsIgnoreCase(displayStatus)) {
            dbStatus = "Paid";
        } else if ("Overdue".equalsIgnoreCase(displayStatus)) {
            dbStatus = "Overdue";
        }

        try (Connection conn = digielancer.main.KoneksiDB.configDB()) {
            String sql = "UPDATE invoice SET status = ? WHERE id = ?";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, dbStatus);
                pst.setInt(2, invId);
                pst.executeUpdate();
            }
            
            // Reload and rebuild UI
            loadInvoicesFromDB();
            rebuildCards();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        
        // Scrollable content container
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // 1. Header Panel
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 24, 0);
        contentPanel.add(createHeaderPanel(), gbc);
        
        // 2. Cards Panel
        cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(cardsPanel, gbc);
        
        // 3. Vertical Spacer
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        contentPanel.add(spacer, gbc);
        
        // ScrollPane customization
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(248, 250, 252));
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
        
        rebuildCards();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        
        // Title
        JLabel titleLabel = new JLabel("Generator & History Nota");
        titleLabel.setFont(getModernFont(Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        headerPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Buat nota baru dan lihat riwayat invoice");
        subtitleLabel.setFont(getModernFont(Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 24, 0);
        headerPanel.add(subtitleLabel, gbc);
        
        // Tabs
        JPanel tabsContainer = new JPanel();
        tabsContainer.setOpaque(false);
        tabsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        
        RoundedButton btnNota = new RoundedButton("Buat Nota Baru", 16, new Color(241, 245, 249), new Color(74, 85, 101));
        btnNota.addActionListener(e -> navigateToInvoicePanel());
        
        String activeTabText = "History Invoice (" + invoices.size() + ")";
        btnHistory = new RoundedButton(activeTabText, 16, new Color(6, 141, 240), Color.WHITE);
        
        tabsContainer.add(btnNota);
        tabsContainer.add(btnHistory);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        headerPanel.add(tabsContainer, gbc);
        
        return headerPanel;
    }

    private void rebuildCards() {
        cardsPanel.removeAll();
        
        for (Invoice inv : invoices) {
            cardsPanel.add(createCard(inv));
            cardsPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        }
        
        // Update tab count
        if (btnHistory != null) {
            btnHistory.setText("History Invoice (" + invoices.size() + ")");
        }
        
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createCard(Invoice inv) {
        RoundedPanel card = new RoundedPanel(16);
        card.setLayout(new BorderLayout(0, 12));
        
        // --- 1. Top Section ---
        JPanel topSection = new JPanel(new GridBagLayout());
        topSection.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Left Column: Name, Badge, Date
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        JPanel nameBadgeRow = new JPanel();
        nameBadgeRow.setOpaque(false);
        nameBadgeRow.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        nameBadgeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(inv.clientName);
        nameLabel.setFont(getModernFont(Font.BOLD, 18));
        nameLabel.setForeground(new Color(15, 23, 42));
        nameBadgeRow.add(nameLabel);
        
        nameBadgeRow.add(createStatusBadge(inv.status));
        
        JLabel dateLabel = new JLabel("• " + inv.date);
        dateLabel.setFont(getModernFont(Font.PLAIN, 13));
        dateLabel.setForeground(new Color(100, 116, 139));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 0, 0));
        
        JLabel invNumberLabel = new JLabel("Nota: " + inv.invoiceNumber + " • " + inv.projectName);
        invNumberLabel.setFont(getModernFont(Font.PLAIN, 12));
        invNumberLabel.setForeground(new Color(100, 116, 139));
        invNumberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        invNumberLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 0, 0));

        StringBuilder addonsSb = new StringBuilder();
        if (inv.addOnsList != null && !inv.addOnsList.isEmpty()) {
            for (String item : inv.addOnsList) {
                if (addonsSb.length() > 0) addonsSb.append(", ");
                addonsSb.append(item);
            }
        }
        JLabel addonsLabel = new JLabel("Layanan: " + (addonsSb.length() > 0 ? addonsSb.toString() : "Hanya Layanan Utama"));
        addonsLabel.setFont(getModernFont(Font.PLAIN, 12));
        addonsLabel.setForeground(new Color(100, 116, 139));
        addonsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addonsLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 0, 0));

        leftPanel.add(nameBadgeRow);
        leftPanel.add(dateLabel);
        leftPanel.add(invNumberLabel);
        leftPanel.add(addonsLabel);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        topSection.add(leftPanel, gbc);
        
        // Right Column: Amount, Action Buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        JLabel amountLabel = new JLabel(inv.amount);
        amountLabel.setFont(getModernFont(Font.BOLD, 22));
        amountLabel.setForeground(new Color(6, 141, 240));
        amountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JPanel actionsRow = new JPanel();
        actionsRow.setOpaque(false);
        actionsRow.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionsRow.setAlignmentX(Component.RIGHT_ALIGNMENT);
        actionsRow.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        
        PreviewButton previewBtn = new PreviewButton(12, new Color(241, 245, 249), new Color(74, 85, 101));
        previewBtn.addActionListener(e -> {
            // Switch to InvoiceReceiptPanel
            List<String> itemDescs = new ArrayList<>();
            List<Double> itemPrices = new ArrayList<>();
            for (String item : inv.addOnsList) {
                int idx = item.lastIndexOf("(Rp");
                String desc = item;
                double price = 0;
                if (idx != -1) {
                    desc = item.substring(0, idx).trim();
                    String prStr = item.substring(idx + 3, item.length() - 1).replaceAll("[^\\d]", "");
                    try { price = Double.parseDouble(prStr); } catch (Exception ignored) {}
                }
                itemDescs.add(desc);
                itemPrices.add(price);
            }
            
            java.awt.Container parentContainer = this.getParent();
            while (parentContainer != null && !(parentContainer instanceof MenuInvoice)) {
                parentContainer = parentContainer.getParent();
            }
            if (parentContainer instanceof MenuInvoice) {
                MenuInvoice menuInvoice = (MenuInvoice) parentContainer;
                menuInvoice.switchContent(new InvoiceReceiptPanel(
                    inv.invoiceNumber,
                    inv.clientName,
                    inv.projectName,
                    inv.rawAmount,
                    itemDescs,
                    itemPrices,
                    inv.date
                ));
            }
        });
        
        DownloadButton downloadBtn = new DownloadButton(12, new Color(6, 141, 240), Color.WHITE);
        downloadBtn.addActionListener(e -> {
            exportInvoiceToFile(inv.invoiceNumber, inv.clientName, inv.projectName, inv.rawAmount, inv.addOnsList, this);
        });
        
        actionsRow.add(previewBtn);
        actionsRow.add(downloadBtn);
        
        rightPanel.add(amountLabel);
        rightPanel.add(actionsRow);
        
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        topSection.add(rightPanel, gbc);
        
        card.add(topSection, BorderLayout.CENTER);
        
        // --- 2. Bottom Section ---
        JPanel bottomSection = new JPanel();
        bottomSection.setOpaque(false);
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        
        JPanel divider = new JPanel();
        divider.setPreferredSize(new Dimension(1, 1));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setBackground(new Color(241, 245, 249));
        
        JPanel statusRow = new JPanel();
        statusRow.setOpaque(false);
        statusRow.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        
        JLabel updateLabel = new JLabel("Update Status:");
        updateLabel.setFont(getModernFont(Font.BOLD, 12));
        updateLabel.setForeground(new Color(74, 85, 101));
        statusRow.add(updateLabel);
        
        RoundedButton btnPending = new RoundedButton("Pending", 12, new Color(254, 243, 199), new Color(180, 83, 9));
        btnPending.addActionListener(e -> {
            updateInvoiceStatusInDB(inv.id, "Pending");
        });
        
        RoundedButton btnDibayar = new RoundedButton("Dibayar", 12, new Color(220, 252, 231), new Color(21, 128, 61));
        btnDibayar.addActionListener(e -> {
            updateInvoiceStatusInDB(inv.id, "Dibayar");
        });
        
        RoundedButton btnOverdue = new RoundedButton("Overdue", 12, new Color(254, 226, 226), new Color(185, 28, 28));
        btnOverdue.addActionListener(e -> {
            updateInvoiceStatusInDB(inv.id, "Overdue");
        });
        
        statusRow.add(btnPending);
        statusRow.add(btnDibayar);
        statusRow.add(btnOverdue);
        
        bottomSection.add(divider);
        bottomSection.add(statusRow);
        
        card.add(bottomSection, BorderLayout.SOUTH);
        
        return card;
    }

    private void exportInvoiceToFile(String invoiceNumber, String clientName, String projectName, double totalPrice, List<String> items, Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(invoiceNumber + ".txt"));
        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(fileToSave))) {
                writer.println("=========================================");
                writer.println("            DIGI ELANCER INVOICE         ");
                writer.println("=========================================");
                writer.println("Nomor Nota     : " + invoiceNumber);
                writer.println("Tanggal        : " + new java.util.Date());
                writer.println("Klien          : " + clientName);
                writer.println("Project        : " + projectName);
                writer.println("-----------------------------------------");
                writer.println("Rincian Layanan:");
                for (String item : items) {
                    writer.println(" - " + item);
                }
                writer.println("-----------------------------------------");
                java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("in", "ID"));
                writer.println("TOTAL          : Rp " + nf.format(totalPrice));
                writer.println("=========================================");
                writer.println("       Terima kasih atas kerja samanya!  ");
                writer.flush();
                JOptionPane.showMessageDialog(parent, "Invoice berhasil diunduh ke:\n" + fileToSave.getAbsolutePath(), "Unduh Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Gagal mengunduh invoice: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Component createStatusBadge(String status) {
        if ("Pending".equalsIgnoreCase(status)) {
            return new BadgeLabel("🕒 Pending", new Color(254, 243, 199), new Color(180, 83, 9));
        } else if ("Dibayar".equalsIgnoreCase(status)) {
            return new BadgeLabel("✓ Dibayar", new Color(220, 252, 231), new Color(21, 128, 61));
        } else {
            return new BadgeLabel("Overdue", new Color(254, 226, 226), new Color(185, 28, 28));
        }
    }

    private void navigateToInvoicePanel() {
        java.awt.Container parent = this.getParent();
        while (parent != null && !(parent instanceof MenuInvoice)) {
            parent = parent.getParent();
        }
        if (parent instanceof MenuInvoice) {
            MenuInvoice menuInvoice = (MenuInvoice) parent;
            JButton btn = findButtonWithText(menuInvoice, "Buat Nota Baru");
            if (btn != null) {
                btn.doClick();
            } else {
                menuInvoice.switchContent(new InvoicePanel());
            }
        }
    }

    private JButton findButtonWithText(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && text.equalsIgnoreCase(((JButton) comp).getText())) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton found = findButtonWithText((Container) comp, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        java.awt.Container parent = this.getParent();
        if (parent != null) {
            java.awt.Container grandParent = parent.getParent();
            if (grandParent instanceof MenuInvoice) {
                lastGrandParent = grandParent;
                for (Component comp : grandParent.getComponents()) {
                    if (comp != parent) {
                        comp.setVisible(false);
                    }
                }
            }
        }
    }

    @Override
    public void removeNotify() {
        if (lastGrandParent != null) {
            for (Component comp : lastGrandParent.getComponents()) {
                comp.setVisible(true);
            }
        }
        super.removeNotify();
    }

    public static Font getModernFont(int style, int size) {
        Font font = new Font("Inter", style, size);
        if (font.getFamily().equals("Dialog") && !font.getName().equals("Inter")) {
            font = new Font("Segoe UI", style, size);
        }
        return font;
    }

    // --- Custom UI Component Helpers ---

    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor = Color.WHITE;
        
        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(20, 24, 16, 24));
        }
        
        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Short.MAX_VALUE, getPreferredSize().height);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Outer shadow
            g2.setColor(new Color(0, 0, 0, 6));
            g2.fillRoundRect(1, 2, width - 2, height - 4, cornerRadius, cornerRadius);
            
            // Inner shadow offset
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(2, 4, width - 4, height - 6, cornerRadius, cornerRadius);
            
            // Actual card fill
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, width - 2, height - 4, cornerRadius, cornerRadius);
            
            // Subtly colored border
            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(0, 0, width - 2, height - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }

    private static class BadgeLabel extends JLabel {
        private Color bg;
        
        public BadgeLabel(String text, Color bg, Color fg) {
            super(text);
            this.bg = bg;
            setForeground(fg);
            setFont(getModernFont(Font.BOLD, 12));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedButton extends JButton {
        protected int radius;
        protected Color bg;
        protected Color fg;
        protected boolean hovered = false;
        
        public RoundedButton(String text, int radius, Color bg, Color fg) {
            super(text);
            this.radius = radius;
            this.bg = bg;
            this.fg = fg;
            setForeground(fg);
            setFont(getModernFont(Font.BOLD, 12));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    hovered = true;
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    hovered = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (bg.getAlpha() == 0) {
                if (hovered) {
                    g2.setColor(new Color(0, 0, 0, 15));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                }
            } else {
                if (hovered) {
                    g2.setColor(darkenColor(bg));
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
        
        protected Color darkenColor(Color color) {
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            float brightness = Math.max(0.0f, hsb[2] - 0.08f);
            return Color.getHSBColor(hsb[0], hsb[1], brightness);
        }
    }

    private static class PreviewButton extends RoundedButton {
        public PreviewButton(int radius, Color bg, Color fg) {
            super("Preview", radius, bg, fg);
            setBorder(BorderFactory.createEmptyBorder(6, 32, 6, 14));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(1.5f));
            
            int cx = 18;
            int cy = getHeight() / 2;
            
            // Vector eye icon outline
            g2.drawArc(cx - 8, cy - 6, 16, 12, 0, 180);
            g2.drawArc(cx - 8, cy - 6, 16, 12, 180, 180);
            
            // Pupil fill
            g2.fillOval(cx - 2, cy - 2, 4, 4);
            
            g2.dispose();
        }
    }

    private static class DownloadButton extends RoundedButton {
        public DownloadButton(int radius, Color bg, Color fg) {
            super("", radius, bg, fg);
            setPreferredSize(new Dimension(36, 32));
            setMaximumSize(new Dimension(36, 32));
            setMinimumSize(new Dimension(36, 32));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(2.0f));
            
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            
            // Vector download arrow
            g2.drawLine(cx, cy - 6, cx, cy + 4);
            g2.drawLine(cx, cy + 4, cx - 4, cy);
            g2.drawLine(cx, cy + 4, cx + 4, cy);
            
            // Vector bottom tray bar
            g2.drawLine(cx - 6, cy + 7, cx + 6, cy + 7);
            
            g2.dispose();
        }
    }
}
