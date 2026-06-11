package digielancer.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InvoicePanel extends javax.swing.JPanel {

    private JComboBox<String> cbProject;
    private JTextField tfNotaNumber;
    private AddOnRow rowMobileDB;
    private AddOnRow rowMobileAI;
    private AddOnRow rowAPIntegration;
    private ToggleSwitch tsShowLogo;

    private JPanel logoSection;
    private JPanel logoDivider;
    private RoundedPanel previewCard;
    private JLabel lblPreviewClientName;
    private JLabel lblPreviewProjectName;
    private JLabel lblPreviewInvoiceId;
    private JPanel itemsPreviewContainer;
    private JLabel lblPreviewTotal;

    private Container lastGrandParent = null;

    /**
     * Creates new form InvoicePanel
     */
    public InvoicePanel() {
        initComponents();
        setupModernUI();
    }

    /**
     * Rebuild the UI programmatically to match the visual standard of HistoryInvoicePanel
     * and the reference design in nota.png with a fully responsive layout.
     */
    private void setupModernUI() {
        removeAll();
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

        // 2. Columns Panel (Left: Form, Right: Button & Preview)
        JPanel columnsPanel = new JPanel(new GridBagLayout());
        columnsPanel.setOpaque(false);

        GridBagConstraints colGbc = new GridBagConstraints();
        colGbc.fill = GridBagConstraints.BOTH;
        colGbc.weighty = 1.0;

        // Left Column (Form)
        colGbc.gridx = 0;
        colGbc.gridy = 0;
        colGbc.weightx = 0.55;
        colGbc.insets = new Insets(0, 0, 0, 16);
        JPanel leftColumn = createLeftColumn();
        columnsPanel.add(leftColumn, colGbc);

        // Right Column (Button + Preview)
        colGbc.gridx = 1;
        colGbc.gridy = 0;
        colGbc.weightx = 0.45;
        colGbc.insets = new Insets(0, 16, 0, 0);
        JPanel rightColumn = createRightColumn();
        columnsPanel.add(rightColumn, colGbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(columnsPanel, gbc);

        // JScrollPane wrap
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(248, 250, 252));
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Populate initial calculations
        updateInvoicePreview();

        revalidate();
        repaint();
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

        // Navigation Tabs Container
        JPanel tabsContainer = new JPanel();
        tabsContainer.setOpaque(false);
        tabsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));

        // Active Tab Button: Buat Nota Baru
        RoundedButton btnNota = new RoundedButton("Buat Nota Baru", 16, new Color(6, 141, 240), Color.WHITE);

        // Inactive Tab Button: History Invoice
        RoundedButton btnHistory = new RoundedButton("History Invoice (3)", 16, new Color(241, 245, 249), new Color(74, 85, 101));
        btnHistory.addActionListener(e -> navigateToHistoryInvoicePanel());

        tabsContainer.add(btnNota);
        tabsContainer.add(btnHistory);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        headerPanel.add(tabsContainer, gbc);

        return headerPanel;
    }

    private JPanel createLeftColumn() {
        JPanel leftCol = new JPanel(new GridBagLayout());
        leftCol.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        // Card 1: Detail Nota
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);
        leftCol.add(createDetailNotaCard(), gbc);

        // Card 2: Pilih Add-ons
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0);
        leftCol.add(createAddOnsCard(), gbc);

        // Card 3: Opsi Tampilan
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        leftCol.add(createOpsiTampilanCard(), gbc);

        // Vertical Spacer to push items upward
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        leftCol.add(spacer, gbc);

        return leftCol;
    }

    private JPanel createDetailNotaCard() {
        RoundedPanel card = new RoundedPanel(16);
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Header Title
        JLabel cardTitle = new JLabel("Detail Nota");
        cardTitle.setFont(getModernFont(Font.BOLD, 14));
        cardTitle.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(cardTitle, gbc);

        // Label: Pilih Project
        JLabel lblProject = new JLabel("Pilih Project");
        lblProject.setFont(getModernFont(Font.PLAIN, 12));
        lblProject.setForeground(new Color(100, 116, 139));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        card.add(lblProject, gbc);

        // Project Dropdown
        cbProject = new JComboBox<>(new String[] { "Cahya Motion", "Creative Studio", "Ansyah Creative" });
        cbProject.setFont(getModernFont(Font.PLAIN, 13));
        cbProject.setPreferredSize(new Dimension(cbProject.getPreferredSize().width, 42));
        cbProject.setBackground(Color.WHITE);
        cbProject.addActionListener(e -> updateInvoicePreview());
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(cbProject, gbc);

        // Label: Nomor Nota
        JLabel lblNota = new JLabel("Nomor Nota");
        lblNota.setFont(getModernFont(Font.PLAIN, 12));
        lblNota.setForeground(new Color(100, 116, 139));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 6, 0);
        card.add(lblNota, gbc);

        // Nomor Nota Input Field
        tfNotaNumber = new JTextField("INV-2026-752");
        tfNotaNumber.setFont(getModernFont(Font.PLAIN, 13));
        tfNotaNumber.setPreferredSize(new Dimension(tfNotaNumber.getPreferredSize().width, 42));
        tfNotaNumber.setBackground(new Color(248, 250, 252));
        tfNotaNumber.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
        tfNotaNumber.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateInvoicePreview(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateInvoicePreview(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateInvoicePreview(); }
        });
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(tfNotaNumber, gbc);

        return card;
    }

    private JPanel createAddOnsCard() {
        RoundedPanel card = new RoundedPanel(16);
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Header Title
        JLabel cardTitle = new JLabel("Pilih Add-ons");
        cardTitle.setFont(getModernFont(Font.BOLD, 14));
        cardTitle.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(cardTitle, gbc);

        // Add-on 1: Mobile with DB
        rowMobileDB = new AddOnRow("Mobile with DB", "Rp 5.000.000", true, this::updateInvoicePreview);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(rowMobileDB, gbc);

        // Add-on 2: Mobile with AI
        rowMobileAI = new AddOnRow("Mobile with AI", "Rp 7.500.000", false, this::updateInvoicePreview);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(rowMobileAI, gbc);

        // Add-on 3: APIntegration
        rowAPIntegration = new AddOnRow("APIntegration", "Rp 3.000.000", true, this::updateInvoicePreview);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(rowAPIntegration, gbc);

        return card;
    }

    private JPanel createOpsiTampilanCard() {
        RoundedPanel card = new RoundedPanel(16);
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Header Title
        JLabel cardTitle = new JLabel("Opsi Tampilan");
        cardTitle.setFont(getModernFont(Font.BOLD, 14));
        cardTitle.setForeground(new Color(15, 23, 42));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(cardTitle, gbc);

        // Label: Tampilkan Logo
        JLabel lblShowLogo = new JLabel("Tampilkan Logo");
        lblShowLogo.setFont(getModernFont(Font.PLAIN, 12));
        lblShowLogo.setForeground(new Color(15, 23, 42));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(lblShowLogo, gbc);

        // Toggle Switch
        tsShowLogo = new ToggleSwitch();
        tsShowLogo.setSelected(true);
        tsShowLogo.addActionListener(e -> updateLogoVisibility());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        card.add(tsShowLogo, gbc);

        return card;
    }

    private JPanel createRightColumn() {
        JPanel rightCol = new JPanel(new GridBagLayout());
        rightCol.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        // Button: Generate & Export Nota (Teal color)
        RoundedButton btnGenerate = new RoundedButton("Generate & Export Nota", 8, new Color(16, 185, 129), Color.WHITE) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.setStroke(new BasicStroke(1.5f));
                
                int cx = 24;
                int cy = getHeight() / 2;
                
                // Vector download/export icon inside button
                g2.drawLine(cx, cy - 5, cx, cy + 3);
                g2.drawLine(cx - 3, cy, cx, cy + 3);
                g2.drawLine(cx + 3, cy, cx, cy + 3);
                g2.drawLine(cx - 5, cy + 5, cx + 5, cy + 5);
                
                g2.dispose();
            }
        };
        btnGenerate.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 16));
        btnGenerate.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Nota berhasil digenerate dan diexport!\n" +
                "File: " + tfNotaNumber.getText() + ".pdf", 
                "Export Berhasil", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);
        rightCol.add(btnGenerate, gbc);

        // Card 4: Invoice Preview Card
        previewCard = new RoundedPanel(16);
        previewCard.setLayout(new GridBagLayout());
        
        GridBagConstraints pGbc = new GridBagConstraints();
        pGbc.gridx = 0;
        pGbc.fill = GridBagConstraints.HORIZONTAL;
        pGbc.weightx = 1.0;

        // 1. Logo Section
        logoSection = new JPanel(new GridBagLayout());
        logoSection.setOpaque(false);
        
        // Logo icon "De"
        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(6, 141, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getModernFont(Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String text = "de";
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent() - 1;
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        logoIcon.setPreferredSize(new Dimension(32, 32));
        logoIcon.setOpaque(false);
        
        GridBagConstraints lGbc = new GridBagConstraints();
        lGbc.gridx = 0;
        lGbc.gridy = 0;
        lGbc.gridheight = 2;
        lGbc.insets = new Insets(0, 0, 0, 10);
        logoSection.add(logoIcon, lGbc);
        
        JLabel lblCompName = new JLabel("Digi Elancer");
        lblCompName.setFont(getModernFont(Font.BOLD, 12));
        lblCompName.setForeground(new Color(15, 23, 42));
        lGbc.gridx = 1;
        lGbc.gridy = 0;
        lGbc.gridheight = 1;
        lGbc.anchor = GridBagConstraints.WEST;
        lGbc.insets = new Insets(0, 0, 2, 0);
        logoSection.add(lblCompName, lGbc);
        
        JLabel lblCompSubtitle = new JLabel("Professional Services");
        lblCompSubtitle.setFont(getModernFont(Font.PLAIN, 10));
        lblCompSubtitle.setForeground(new Color(100, 116, 139));
        lGbc.gridx = 1;
        lGbc.gridy = 1;
        lGbc.anchor = GridBagConstraints.WEST;
        lGbc.insets = new Insets(0, 0, 0, 0);
        logoSection.add(lblCompSubtitle, lGbc);
        
        pGbc.gridy = 0;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(logoSection, pGbc);

        // Logo Divider
        logoDivider = createDivider();
        pGbc.gridy = 1;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(logoDivider, pGbc);

        // 2. Invoice Title Section
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        GridBagConstraints dGbc = new GridBagConstraints();
        dGbc.gridx = 0;
        dGbc.fill = GridBagConstraints.HORIZONTAL;
        dGbc.weightx = 1.0;
        dGbc.anchor = GridBagConstraints.WEST;

        JLabel lblInvTitle = new JLabel("NOTA / INVOICE");
        lblInvTitle.setFont(getModernFont(Font.BOLD, 14));
        lblInvTitle.setForeground(new Color(15, 23, 42));
        dGbc.gridy = 0;
        dGbc.insets = new Insets(0, 0, 4, 0);
        detailsPanel.add(lblInvTitle, dGbc);

        lblPreviewInvoiceId = new JLabel("INV-2026-752");
        lblPreviewInvoiceId.setFont(getModernFont(Font.PLAIN, 10));
        lblPreviewInvoiceId.setForeground(new Color(100, 116, 139));
        dGbc.gridy = 1;
        dGbc.insets = new Insets(0, 0, 2, 0);
        detailsPanel.add(lblPreviewInvoiceId, dGbc);

        JLabel lblInvDate = new JLabel("Tanggal: 08 Juni 2026");
        lblInvDate.setFont(getModernFont(Font.PLAIN, 10));
        lblInvDate.setForeground(new Color(100, 116, 139));
        dGbc.gridy = 2;
        dGbc.insets = new Insets(0, 0, 0, 0);
        detailsPanel.add(lblInvDate, dGbc);

        pGbc.gridy = 2;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(detailsPanel, pGbc);

        // Divider
        pGbc.gridy = 3;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(createDivider(), pGbc);

        // 3. Client Section ("Kepada")
        JPanel clientPanel = new JPanel(new GridBagLayout());
        clientPanel.setOpaque(false);
        GridBagConstraints cGbc = new GridBagConstraints();
        cGbc.gridx = 0;
        cGbc.fill = GridBagConstraints.HORIZONTAL;
        cGbc.weightx = 1.0;
        cGbc.anchor = GridBagConstraints.WEST;

        JLabel lblKepada = new JLabel("Kepada:");
        lblKepada.setFont(getModernFont(Font.PLAIN, 10));
        lblKepada.setForeground(new Color(100, 116, 139));
        cGbc.gridy = 0;
        cGbc.insets = new Insets(0, 0, 2, 0);
        clientPanel.add(lblKepada, cGbc);

        lblPreviewClientName = new JLabel("Nama Klien");
        lblPreviewClientName.setFont(getModernFont(Font.BOLD, 11));
        lblPreviewClientName.setForeground(new Color(15, 23, 42));
        cGbc.gridy = 1;
        cGbc.insets = new Insets(0, 0, 0, 0);
        clientPanel.add(lblPreviewClientName, cGbc);

        pGbc.gridy = 4;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(clientPanel, pGbc);

        // Divider
        pGbc.gridy = 5;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(createDivider(), pGbc);

        // 4. Project Section
        JPanel projectPanel = new JPanel(new GridBagLayout());
        projectPanel.setOpaque(false);
        GridBagConstraints prGbc = new GridBagConstraints();
        prGbc.gridx = 0;
        prGbc.fill = GridBagConstraints.HORIZONTAL;
        prGbc.weightx = 1.0;
        prGbc.anchor = GridBagConstraints.WEST;

        JLabel lblProjectText = new JLabel("Project:");
        lblProjectText.setFont(getModernFont(Font.PLAIN, 10));
        lblProjectText.setForeground(new Color(100, 116, 139));
        prGbc.gridy = 0;
        prGbc.insets = new Insets(0, 0, 2, 0);
        projectPanel.add(lblProjectText, prGbc);

        lblPreviewProjectName = new JLabel("Nama Project");
        lblPreviewProjectName.setFont(getModernFont(Font.BOLD, 11));
        lblPreviewProjectName.setForeground(new Color(15, 23, 42));
        prGbc.gridy = 1;
        prGbc.insets = new Insets(0, 0, 12, 0);
        projectPanel.add(lblPreviewProjectName, prGbc);

        pGbc.gridy = 6;
        pGbc.insets = new Insets(0, 0, 0, 0);
        previewCard.add(projectPanel, pGbc);

        // 5. Items Container (Dynamic Checked Add-ons)
        itemsPreviewContainer = new JPanel();
        itemsPreviewContainer.setOpaque(false);
        itemsPreviewContainer.setLayout(new BoxLayout(itemsPreviewContainer, BoxLayout.Y_AXIS));
        pGbc.gridy = 7;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(itemsPreviewContainer, pGbc);

        // Divider
        pGbc.gridy = 8;
        pGbc.insets = new Insets(0, 0, 12, 0);
        previewCard.add(createDivider(), pGbc);

        // 6. Total Section
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        
        JLabel lblTotalText = new JLabel("Total");
        lblTotalText.setFont(getModernFont(Font.BOLD, 12));
        lblTotalText.setForeground(new Color(15, 23, 42));
        
        lblPreviewTotal = new JLabel("Rp 0");
        lblPreviewTotal.setFont(getModernFont(Font.BOLD, 16));
        lblPreviewTotal.setForeground(new Color(6, 141, 240));
        
        totalPanel.add(lblTotalText, BorderLayout.WEST);
        totalPanel.add(lblPreviewTotal, BorderLayout.EAST);
        
        pGbc.gridy = 9;
        pGbc.insets = new Insets(0, 0, 0, 0);
        previewCard.add(totalPanel, pGbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        rightCol.add(previewCard, gbc);

        // Vertical Spacer to push items upward
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        rightCol.add(spacer, gbc);

        return rightCol;
    }

    private JPanel createDivider() {
        JPanel div = new JPanel();
        div.setPreferredSize(new Dimension(1, 1));
        div.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        div.setBackground(new Color(241, 245, 249));
        return div;
    }

    private void updateLogoVisibility() {
        if (logoSection != null) {
            logoSection.setVisible(tsShowLogo.isSelected());
            logoDivider.setVisible(tsShowLogo.isSelected());
            if (previewCard != null) {
                previewCard.revalidate();
                previewCard.repaint();
            }
        }
    }

    private void updateInvoicePreview() {
        // 1. Update Project Label & Client Name
        String selectedProject = cbProject != null ? (String) cbProject.getSelectedItem() : "Cahya Motion";
        if (lblPreviewProjectName != null) {
            lblPreviewProjectName.setText(selectedProject);
        }
        
        if (lblPreviewClientName != null) {
            if ("Cahya Motion".equals(selectedProject)) {
                lblPreviewClientName.setText("Cahya Client");
            } else if ("Creative Studio".equals(selectedProject)) {
                lblPreviewClientName.setText("Creative Studio Client");
            } else if ("Ansyah Creative".equals(selectedProject)) {
                lblPreviewClientName.setText("Ansyah Creative Client");
            } else {
                lblPreviewClientName.setText("Nama Klien");
            }
        }

        // 2. Update Invoice ID Label
        String invNumber = tfNotaNumber != null ? tfNotaNumber.getText().trim() : "INV-2026-752";
        if (lblPreviewInvoiceId != null) {
            if (invNumber.isEmpty()) {
                lblPreviewInvoiceId.setText("INV-xxxx");
            } else {
                lblPreviewInvoiceId.setText(invNumber);
            }
        }

        // 3. Update Dynamic Add-ons List & Calculate Total
        if (itemsPreviewContainer != null) {
            itemsPreviewContainer.removeAll();
            long totalAmount = 0;

            if (rowMobileDB != null && rowMobileDB.isSelected()) {
                itemsPreviewContainer.add(createPreviewItemRow("Mobile with DB", "Rp 5.000.000"));
                itemsPreviewContainer.add(Box.createRigidArea(new Dimension(0, 6)));
                totalAmount += 5000000;
            }

            if (rowMobileAI != null && rowMobileAI.isSelected()) {
                itemsPreviewContainer.add(createPreviewItemRow("Mobile with AI", "Rp 7.500.000"));
                itemsPreviewContainer.add(Box.createRigidArea(new Dimension(0, 6)));
                totalAmount += 7500000;
            }

            if (rowAPIntegration != null && rowAPIntegration.isSelected()) {
                itemsPreviewContainer.add(createPreviewItemRow("API Integration", "Rp 3.000.000"));
                itemsPreviewContainer.add(Box.createRigidArea(new Dimension(0, 6)));
                totalAmount += 3000000;
            }

            // Format Total Amount
            java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("in", "ID"));
            if (lblPreviewTotal != null) {
                lblPreviewTotal.setText("Rp " + nf.format(totalAmount));
            }

            itemsPreviewContainer.revalidate();
            itemsPreviewContainer.repaint();
        }

        if (previewCard != null) {
            previewCard.revalidate();
            previewCard.repaint();
        }
    }

    private JPanel createPreviewItemRow(String name, String price) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel lblName = new JLabel(name);
        lblName.setFont(getModernFont(Font.PLAIN, 10));
        lblName.setForeground(new Color(15, 23, 42));

        JLabel lblPrice = new JLabel(price);
        lblPrice.setFont(getModernFont(Font.PLAIN, 10));
        lblPrice.setForeground(new Color(15, 23, 42));

        row.add(lblName, BorderLayout.WEST);
        row.add(lblPrice, BorderLayout.EAST);
        return row;
    }

    private void navigateToHistoryInvoicePanel() {
        java.awt.Container parent = this.getParent();
        while (parent != null && !(parent instanceof MenuInvoice)) {
            parent = parent.getParent();
        }
        if (parent instanceof MenuInvoice) {
            MenuInvoice menuInvoice = (MenuInvoice) parent;
            menuInvoice.switchContent(new HistoryInvoicePanel());
        }
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

    // --- Custom GUI Helpers ---

    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor = Color.WHITE;
        
        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
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
            
            // Border
            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(0, 0, width - 2, height - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
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

    private static class ToggleSwitch extends JToggleButton {
        private boolean selected = true;
        private Color colorSelected = new Color(6, 141, 240);
        private Color colorUnselected = new Color(226, 232, 240);
        private Color colorButton = Color.WHITE;
        private javax.swing.Timer timer;
        private float location = 1.0f; // 0.0 to 1.0

        public ToggleSwitch() {
            setPreferredSize(new Dimension(44, 24));
            setMinimumSize(new Dimension(44, 24));
            setMaximumSize(new Dimension(44, 24));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            setBorder(null);
            setSelected(true);
            addActionListener(e -> {
                selected = isSelected();
                animate();
            });
        }

        private void animate() {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            timer = new javax.swing.Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selected) {
                        location += 0.1f;
                        if (location >= 1.0f) {
                            location = 1.0f;
                            timer.stop();
                        }
                    } else {
                        location -= 0.1f;
                        if (location <= 0.0f) {
                            location = 0.0f;
                            timer.stop();
                        }
                    }
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            
            // Track background
            g2.setColor(selected ? colorSelected : colorUnselected);
            g2.fillRoundRect(0, 0, w, h, h, h);
            
            // Knob
            int knobSize = h - 4;
            int minX = 2;
            int maxX = w - knobSize - 2;
            int knobX = (int) (minX + (maxX - minX) * location);
            
            g2.setColor(colorButton);
            g2.fillOval(knobX, 2, knobSize, knobSize);
            g2.dispose();
        }
    }

    private static class AddOnRow extends JPanel {
        private JCheckBox checkBox;
        private JLabel priceLabel;
        private boolean isChecked;
        private Color activeBorderColor = new Color(6, 141, 240);
        private Color inactiveBorderColor = new Color(226, 232, 240);

        public AddOnRow(String title, String price, boolean selected, Runnable onChange) {
            this.isChecked = selected;
            setLayout(new BorderLayout());
            setOpaque(false);
            
            checkBox = new JCheckBox(title, selected);
            checkBox.setFont(getModernFont(Font.BOLD, 12));
            checkBox.setForeground(new Color(15, 23, 42));
            checkBox.setOpaque(false);
            checkBox.setFocusPainted(false);
            checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            checkBox.addActionListener(e -> {
                isChecked = checkBox.isSelected();
                repaint();
                onChange.run();
            });

            priceLabel = new JLabel(price);
            priceLabel.setFont(getModernFont(Font.BOLD, 12));
            priceLabel.setForeground(new Color(6, 141, 240));

            setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

            add(checkBox, BorderLayout.WEST);
            add(priceLabel, BorderLayout.EAST);
        }

        public boolean isSelected() {
            return isChecked;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 12, 12);

            g2.setColor(isChecked ? activeBorderColor : inactiveBorderColor);
            g2.setStroke(new BasicStroke(isChecked ? 1.5f : 1.0f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
