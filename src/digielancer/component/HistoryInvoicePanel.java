package digielancer.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryInvoicePanel extends javax.swing.JPanel {

    // Model class for Invoices
    private static class Invoice {
        String clientName;
        String date;
        String status; // "Pending", "Dibayar", "Overdue"
        String amount;
        
        public Invoice(String clientName, String date, String status, String amount) {
            this.clientName = clientName;
            this.date = date;
            this.status = status;
            this.amount = amount;
        }
    }

    private List<Invoice> invoices;
    private JPanel cardsPanel;
    private Container lastGrandParent = null;
    private RoundedButton btnHistory;

    public HistoryInvoicePanel() {
        // Initialize default invoice data matching the design reference
        invoices = new ArrayList<>();
        invoices.add(new Invoice("Ansyah", "05 Jun 2026", "Pending", "Rp 8.000.000"));
        invoices.add(new Invoice("Creative Studio", "25 Mei 2026", "Dibayar", "Rp 5.000.000"));
        invoices.add(new Invoice("Ansyah Creative", "12 Mei 2026", "Overdue", "Rp 3.200.000"));
        
        initComponents();
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
        
        leftPanel.add(nameBadgeRow);
        leftPanel.add(dateLabel);
        
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
            JOptionPane.showMessageDialog(this, 
                "Pratinjau Invoice untuk " + inv.clientName + "\nTotal: " + inv.amount + "\nTanggal: " + inv.date + "\nStatus: " + inv.status, 
                "Pratinjau Invoice", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        DownloadButton downloadBtn = new DownloadButton(12, new Color(6, 141, 240), Color.WHITE);
        downloadBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Mengunduh invoice " + inv.clientName + "...", 
                "Unduh Berhasil", 
                JOptionPane.INFORMATION_MESSAGE);
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
            inv.status = "Pending";
            rebuildCards();
        });
        
        RoundedButton btnDibayar = new RoundedButton("Dibayar", 12, new Color(220, 252, 231), new Color(21, 128, 61));
        btnDibayar.addActionListener(e -> {
            inv.status = "Dibayar";
            rebuildCards();
        });
        
        RoundedButton btnOverdue = new RoundedButton("Overdue", 12, new Color(254, 226, 226), new Color(185, 28, 28));
        btnOverdue.addActionListener(e -> {
            inv.status = "Overdue";
            rebuildCards();
        });
        
        statusRow.add(btnPending);
        statusRow.add(btnDibayar);
        statusRow.add(btnOverdue);
        
        bottomSection.add(divider);
        bottomSection.add(statusRow);
        
        card.add(bottomSection, BorderLayout.SOUTH);
        
        return card;
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

