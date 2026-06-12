package digielancer.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.text.NumberFormat;

public class InvoiceReceiptPanel extends javax.swing.JPanel {

    private String invoiceNumber;
    private String clientName;
    private String projectName;
    private double totalPrice;
    private List<String> itemDescriptions;
    private List<Double> itemPrices;
    private String generatedDate;

    // Component references
    private RoundedPanel invoiceCard;
    private java.awt.Container lastGrandParent = null;

    public InvoiceReceiptPanel(String invoiceNumber, String clientName, String projectName, double totalPrice, 
                               List<String> itemDescriptions, List<Double> itemPrices, String generatedDate) {
        this.invoiceNumber = invoiceNumber;
        this.clientName = clientName;
        this.projectName = projectName;
        this.totalPrice = totalPrice;
        this.itemDescriptions = itemDescriptions;
        this.itemPrices = itemPrices;
        this.generatedDate = generatedDate;
        
        setupUI();
    }

    private String getInvoiceStatus() {
        String status = "PENDING";
        try (Connection conn = digielancer.main.KoneksiDB.configDB()) {
            String sql = "SELECT status FROM invoice WHERE invoice_number = ?";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, invoiceNumber);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String dbStatus = rs.getString("status");
                        if (dbStatus != null && !dbStatus.trim().isEmpty()) {
                            status = dbStatus.trim().toUpperCase();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat status: " + e.getMessage());
        }
        return status;
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        // 1. TOP HEADER BAR (Fixed at the top)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));

        // Left of Top Bar: Kembali Button
        RoundedButton btnKembali = new RoundedButton("← Kembali", 8, new Color(241, 245, 249), new Color(15, 23, 42));
        btnKembali.setFont(getModernFont(Font.BOLD, 13));
        btnKembali.addActionListener(e -> navigateToInvoicePanel());
        topBar.add(btnKembali, BorderLayout.WEST);

        // Right of Top Bar: Print & Download Buttons
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionButtonsPanel.setOpaque(false);

        RoundedButton btnPrint = new RoundedButton("Print", 8, new Color(168, 85, 247), Color.WHITE) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.setStroke(new BasicStroke(1.5f));
                
                int cx = 20;
                int cy = getHeight() / 2;
                
                // Printer icon
                g2.drawRoundRect(cx - 6, cy - 2, 12, 8, 2, 2);
                g2.drawLine(cx - 4, cy - 2, cx - 4, cy - 5);
                g2.drawLine(cx + 4, cy - 2, cx + 4, cy - 5);
                g2.drawLine(cx - 4, cy - 5, cx + 4, cy - 5);
                g2.drawLine(cx - 3, cy + 2, cx - 3, cy + 5);
                g2.drawLine(cx + 3, cy + 2, cx + 3, cy + 5);
                g2.drawLine(cx - 3, cy + 5, cx + 3, cy + 5);
                
                g2.dispose();
            }
        };
        btnPrint.setBorder(BorderFactory.createEmptyBorder(10, 36, 10, 20));
        btnPrint.setFont(getModernFont(Font.BOLD, 13));
        btnPrint.addActionListener(e -> printInvoice());

        RoundedButton btnDownload = new RoundedButton("Download", 8, new Color(16, 185, 129), Color.WHITE) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.setStroke(new BasicStroke(1.5f));
                
                int cx = 20;
                int cy = getHeight() / 2;
                
                // Download arrow icon
                g2.drawLine(cx, cy - 5, cx, cy + 3);
                g2.drawLine(cx - 3, cy, cx, cy + 3);
                g2.drawLine(cx + 3, cy, cx, cy + 3);
                g2.drawLine(cx - 5, cy + 5, cx + 5, cy + 5);
                
                g2.dispose();
            }
        };
        btnDownload.setBorder(BorderFactory.createEmptyBorder(10, 36, 10, 20));
        btnDownload.setFont(getModernFont(Font.BOLD, 13));
        btnDownload.addActionListener(e -> exportInvoiceAsImage());

        actionButtonsPanel.add(btnPrint);
        actionButtonsPanel.add(btnDownload);
        topBar.add(actionButtonsPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // 2. CENTER SCROLLABLE PREVIEW (The Invoice Card)
        JPanel scrollContent = new JPanel(new GridBagLayout());
        scrollContent.setOpaque(false);
        scrollContent.setBorder(new EmptyBorder(32, 24, 32, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);

        // CREATE INVOICE CARD
        invoiceCard = new RoundedPanel(16);
        invoiceCard.setBackground(Color.WHITE);
        invoiceCard.setPreferredSize(new Dimension(720, 840));
        invoiceCard.setMinimumSize(new Dimension(720, 750));
        invoiceCard.setLayout(new GridBagLayout());
        
        // Populate invoiceCard elements
        buildInvoiceCardContent();

        scrollContent.add(invoiceCard, gbc);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(248, 250, 252));
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void buildInvoiceCardContent() {
        invoiceCard.removeAll();
        invoiceCard.setBorder(BorderFactory.createEmptyBorder(40, 48, 40, 48));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        int gridy = 0;
        
        // 1. Logo & Status (Row 0)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Logo Section
        JPanel logoSection = new JPanel(new GridBagLayout());
        logoSection.setOpaque(false);
        GridBagConstraints lGbc = new GridBagConstraints();
        
        JPanel logoIcon = new JPanel() {
            private Image imgLogo;

            {
                try {
                    java.net.URL imgURL = getClass().getResource("/digielancer/assets/logo.png");
                    if (imgURL != null) {
                        imgLogo = new ImageIcon(imgURL).getImage();
                    } else {
                        System.err.println("File logo tidak ditemukan di path yang ditentukan!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imgLogo != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(imgLogo, 0, 0, getWidth(), getHeight(), this);
                    g2.dispose();
                }
            }
        };

        logoIcon.setPreferredSize(new Dimension(40, 40)); 
        logoIcon.setOpaque(false);
        
        lGbc.gridx = 0;
        lGbc.gridy = 0;
        lGbc.gridheight = 2;
        lGbc.insets = new Insets(0, 0, 0, 10);
        logoSection.add(logoIcon, lGbc);
        
        JLabel lblCompName = new JLabel("Digi Elancer");
        lblCompName.setFont(getModernFont(Font.BOLD, 20));
        lblCompName.setForeground(new Color(15, 23, 42));
        lGbc.gridx = 1;
        lGbc.gridy = 0;
        lGbc.gridheight = 1;
        lGbc.anchor = GridBagConstraints.WEST;
        lGbc.insets = new Insets(0, 0, 0, 0);
        logoSection.add(lblCompName, lGbc);
        
        JLabel lblCompSub = new JLabel("Professional Freelance Services");
        lblCompSub.setFont(getModernFont(Font.PLAIN, 11));
        lblCompSub.setForeground(new Color(100, 116, 139));
        lGbc.gridx = 1;
        lGbc.gridy = 1;
        lGbc.anchor = GridBagConstraints.WEST;
        logoSection.add(lblCompSub, lGbc);
        
        headerPanel.add(logoSection, BorderLayout.WEST);
        
        // Status Container (Pill)
        String status = getInvoiceStatus();
        Color badgeBg = new Color(239, 246, 255);
        Color badgeBorder = new Color(191, 219, 254);
        Color badgeText = new Color(37, 99, 235);
        
        if (status.equals("PAID") || status.equals("SUCCESS") || status.equals("SELESAI")) {
            badgeBg = new Color(236, 253, 245);
            badgeBorder = new Color(167, 243, 208);
            badgeText = new Color(5, 150, 105);
        } else if (status.equals("CANCELLED") || status.equals("BATAL") || status.equals("FAILED")) {
            badgeBg = new Color(254, 242, 242);
            badgeBorder = new Color(254, 202, 202);
            badgeText = new Color(220, 38, 38);
        }

        JPanel statusContainer = new RoundedPanel(12, badgeBg);
        statusContainer.setLayout(new GridBagLayout());
        statusContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(badgeBorder, 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        
        GridBagConstraints sGbc = new GridBagConstraints();
        sGbc.gridx = 0;
        sGbc.gridy = 0;
        sGbc.anchor = GridBagConstraints.CENTER;
        
        JLabel lblStatusTitle = new JLabel("Status");
        lblStatusTitle.setFont(getModernFont(Font.PLAIN, 10));
        lblStatusTitle.setForeground(new Color(100, 116, 139));
        statusContainer.add(lblStatusTitle, sGbc);
        
        JLabel lblStatusVal = new JLabel(status);
        lblStatusVal.setFont(getModernFont(Font.BOLD, 15));
        lblStatusVal.setForeground(badgeText);
        sGbc.gridy = 1;
        sGbc.insets = new Insets(1, 0, 0, 0);
        statusContainer.add(lblStatusVal, sGbc);
        
        headerPanel.add(statusContainer, BorderLayout.EAST);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        invoiceCard.add(headerPanel, gbc);
        
        // 2. Line Separator (Row 1)
        JPanel linePanel = new JPanel();
        linePanel.setPreferredSize(new Dimension(1, 2));
        linePanel.setBackground(new Color(14, 165, 233));
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        invoiceCard.add(linePanel, gbc);
        
        // 3. Invoice Info Block (Row 2)
        JPanel infoBlock = new JPanel(new GridBagLayout());
        infoBlock.setOpaque(false);
        GridBagConstraints ibGbc = new GridBagConstraints();
        ibGbc.fill = GridBagConstraints.BOTH;
        ibGbc.weighty = 1.0;
        
        // Left Column of infoBlock
        JPanel infoLeft = new JPanel(new GridBagLayout());
        infoLeft.setOpaque(false);
        GridBagConstraints ilGbc = new GridBagConstraints();
        ilGbc.gridx = 0;
        ilGbc.anchor = GridBagConstraints.WEST;
        ilGbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblInvoiceTitle = new JLabel("INVOICE");
        lblInvoiceTitle.setFont(getModernFont(Font.BOLD, 26));
        lblInvoiceTitle.setForeground(new Color(15, 23, 42));
        ilGbc.gridy = 0;
        ilGbc.insets = new Insets(0, 0, 12, 0);
        infoLeft.add(lblInvoiceTitle, ilGbc);
        
        JPanel metaTable = new JPanel(new GridBagLayout());
        metaTable.setOpaque(false);
        GridBagConstraints mtGbc = new GridBagConstraints();
        mtGbc.anchor = GridBagConstraints.WEST;
        mtGbc.insets = new Insets(0, 0, 6, 12);
        
        mtGbc.gridx = 0; mtGbc.gridy = 0;
        JLabel lblNomorText = new JLabel("Nomor:");
        lblNomorText.setFont(getModernFont(Font.PLAIN, 12));
        lblNomorText.setForeground(new Color(100, 116, 139));
        metaTable.add(lblNomorText, mtGbc);
        
        mtGbc.gridx = 1;
        JLabel lblNomorVal = new JLabel(invoiceNumber);
        lblNomorVal.setFont(getModernFont(Font.BOLD, 12));
        lblNomorVal.setForeground(new Color(15, 23, 42));
        metaTable.add(lblNomorVal, mtGbc);
        
        mtGbc.gridx = 0; mtGbc.gridy = 1;
        JLabel lblTanggalText = new JLabel("Tanggal:");
        lblTanggalText.setFont(getModernFont(Font.PLAIN, 12));
        lblTanggalText.setForeground(new Color(100, 116, 139));
        metaTable.add(lblTanggalText, mtGbc);
        
        mtGbc.gridx = 1;
        JLabel lblTanggalVal = new JLabel(generatedDate);
        lblTanggalVal.setFont(getModernFont(Font.BOLD, 12));
        lblTanggalVal.setForeground(new Color(15, 23, 42));
        metaTable.add(lblTanggalVal, mtGbc);
        
        mtGbc.gridx = 0; mtGbc.gridy = 2;
        JLabel lblTempoText = new JLabel("Jatuh Tempo:");
        lblTempoText.setFont(getModernFont(Font.PLAIN, 12));
        lblTempoText.setForeground(new Color(100, 116, 139));
        metaTable.add(lblTempoText, mtGbc);
        
        mtGbc.gridx = 1;
        JLabel lblTempoVal = new JLabel(generatedDate);
        lblTempoVal.setFont(getModernFont(Font.BOLD, 12));
        lblTempoVal.setForeground(new Color(15, 23, 42));
        metaTable.add(lblTempoVal, mtGbc);
        
        ilGbc.gridy = 1;
        ilGbc.insets = new Insets(0, 0, 0, 0);
        infoLeft.add(metaTable, ilGbc);
        
        ibGbc.gridx = 0;
        ibGbc.gridy = 0;
        ibGbc.weightx = 0.55;
        infoBlock.add(infoLeft, ibGbc);
        
        // Right Column of infoBlock: KEPADA
        JPanel infoRight = new JPanel(new GridBagLayout());
        infoRight.setOpaque(false);
        GridBagConstraints irGbc = new GridBagConstraints();
        irGbc.gridx = 0;
        irGbc.anchor = GridBagConstraints.WEST;
        irGbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblKepada = new JLabel("KEPADA:");
        lblKepada.setFont(getModernFont(Font.BOLD, 10));
        lblKepada.setForeground(new Color(100, 116, 139));
        irGbc.gridy = 0;
        irGbc.insets = new Insets(0, 0, 4, 0);
        infoRight.add(lblKepada, irGbc);
        
        JPanel clientBox = new RoundedPanel(12, new Color(248, 250, 252));
        clientBox.setLayout(new GridBagLayout());
        clientBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        GridBagConstraints cbGbc = new GridBagConstraints();
        cbGbc.gridx = 0;
        cbGbc.anchor = GridBagConstraints.WEST;
        
        JLabel lblClientNameVal = new JLabel(clientName);
        lblClientNameVal.setFont(getModernFont(Font.BOLD, 13));
        lblClientNameVal.setForeground(new Color(15, 23, 42));
        cbGbc.gridy = 0;
        clientBox.add(lblClientNameVal, cbGbc);
        
        JLabel lblClientLoc = new JLabel("Indonesia");
        lblClientLoc.setFont(getModernFont(Font.PLAIN, 11));
        lblClientLoc.setForeground(new Color(100, 116, 139));
        cbGbc.gridy = 1;
        cbGbc.insets = new Insets(2, 0, 0, 0);
        clientBox.add(lblClientLoc, cbGbc);
        
        irGbc.gridy = 1;
        irGbc.insets = new Insets(0, 0, 0, 0);
        irGbc.fill = GridBagConstraints.BOTH;
        irGbc.weightx = 1.0;
        infoRight.add(clientBox, irGbc);
        
        ibGbc.gridx = 1;
        ibGbc.gridy = 0;
        ibGbc.weightx = 0.45;
        ibGbc.insets = new Insets(0, 20, 0, 0);
        infoBlock.add(infoRight, ibGbc);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        invoiceCard.add(infoBlock, gbc);
        
        // 4. Project Block (Row 3)
        JPanel projectBox = new RoundedPanel(12, new Color(248, 250, 252));
        projectBox.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
        projectBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        JLabel lblProjectLabel = new JLabel("PROJECT:");
        lblProjectLabel.setFont(getModernFont(Font.BOLD, 10));
        lblProjectLabel.setForeground(new Color(100, 116, 139));
        
        JLabel lblProjectVal = new JLabel(projectName);
        lblProjectVal.setFont(getModernFont(Font.BOLD, 12));
        lblProjectVal.setForeground(new Color(15, 23, 42));
        
        projectBox.add(lblProjectLabel);
        projectBox.add(lblProjectVal);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        invoiceCard.add(projectBox, gbc);
        
        // 5. Table Header (Row 4)
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);
        tableHeader.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JLabel lblDescHeader = new JLabel("DESKRIPSI LAYANAN");
        lblDescHeader.setFont(getModernFont(Font.BOLD, 10));
        lblDescHeader.setForeground(new Color(100, 116, 139));
        
        JLabel lblPriceHeader = new JLabel("HARGA");
        lblPriceHeader.setFont(getModernFont(Font.BOLD, 10));
        lblPriceHeader.setForeground(new Color(100, 116, 139));
        
        tableHeader.add(lblDescHeader, BorderLayout.WEST);
        tableHeader.add(lblPriceHeader, BorderLayout.EAST);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        invoiceCard.add(tableHeader, gbc);
        
        // 6. Table Items (Row 5)
        JPanel itemsContainer = new JPanel();
        itemsContainer.setOpaque(false);
        itemsContainer.setLayout(new BoxLayout(itemsContainer, BoxLayout.Y_AXIS));
        
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("in", "ID"));
        for (int i = 0; i < itemDescriptions.size(); i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
            
            JLabel descLabel = new JLabel(itemDescriptions.get(i));
            descLabel.setFont(getModernFont(Font.PLAIN, 12));
            descLabel.setForeground(new Color(15, 23, 42));
            
            JLabel priceLabel = new JLabel("Rp " + nf.format(itemPrices.get(i)));
            priceLabel.setFont(getModernFont(Font.BOLD, 12));
            priceLabel.setForeground(new Color(15, 23, 42));
            
            row.add(descLabel, BorderLayout.WEST);
            row.add(priceLabel, BorderLayout.EAST);
            
            itemsContainer.add(row);
            
            JPanel rowDivider = new JPanel();
            rowDivider.setPreferredSize(new Dimension(1, 1));
            rowDivider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            rowDivider.setBackground(new Color(241, 245, 249));
            itemsContainer.add(rowDivider);
        }
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        invoiceCard.add(itemsContainer, gbc);
        
        // 7. Total Badge (Row 6)
        JPanel totalBadgeWrapper = new JPanel(new BorderLayout());
        totalBadgeWrapper.setOpaque(false);
        
        JPanel totalBadge = new RoundedPanel(12, new Color(6, 141, 240));
        totalBadge.setLayout(new FlowLayout(FlowLayout.CENTER, 14, 0));
        totalBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 0, 0),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        
        JLabel lblTotalLabel = new JLabel("TOTAL:");
        lblTotalLabel.setFont(getModernFont(Font.BOLD, 12));
        lblTotalLabel.setForeground(Color.WHITE);
        
        JLabel lblTotalBadgeVal = new JLabel("Rp " + nf.format(totalPrice));
        lblTotalBadgeVal.setFont(getModernFont(Font.BOLD, 18));
        lblTotalBadgeVal.setForeground(Color.WHITE);
        
        totalBadge.add(lblTotalLabel);
        totalBadge.add(lblTotalBadgeVal);
        
        totalBadgeWrapper.add(totalBadge, BorderLayout.EAST);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 32, 0);
        invoiceCard.add(totalBadgeWrapper, gbc);
        
        // 8. Bottom Divider (Row 7)
        JPanel bottomDivider = new JPanel();
        bottomDivider.setPreferredSize(new Dimension(1, 1));
        bottomDivider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        bottomDivider.setBackground(new Color(226, 232, 240));
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        invoiceCard.add(bottomDivider, gbc);
        
        // 9. Footer (Row 8)
        JPanel footerPanel = new JPanel(new GridBagLayout());
        footerPanel.setOpaque(false);
        GridBagConstraints fGbc = new GridBagConstraints();
        fGbc.gridx = 0;
        fGbc.anchor = GridBagConstraints.CENTER;
        
        JLabel lblFooter1 = new JLabel("Terima kasih atas kepercayaan Anda!");
        lblFooter1.setFont(getModernFont(Font.PLAIN, 11));
        lblFooter1.setForeground(new Color(100, 116, 139));
        fGbc.gridy = 0;
        footerPanel.add(lblFooter1, fGbc);
        
        JLabel lblFooter2 = new JLabel("Invoice ini dibuat secara otomatis oleh Digi Elancer");
        lblFooter2.setFont(getModernFont(Font.PLAIN, 9));
        lblFooter2.setForeground(new Color(148, 163, 184));
        fGbc.gridy = 1;
        fGbc.insets = new Insets(2, 0, 0, 0);
        footerPanel.add(lblFooter2, fGbc);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        invoiceCard.add(footerPanel, gbc);
        
        invoiceCard.revalidate();
        invoiceCard.repaint();
    }

    private void printInvoice() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Invoice " + invoiceNumber);
        
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                
                // Scale to fit page width
                double scaleX = pf.getImageableWidth() / invoiceCard.getWidth();
                double scaleY = pf.getImageableHeight() / invoiceCard.getHeight();
                double scale = Math.min(scaleX, scaleY);
                if (scale < 1.0) {
                    g2.scale(scale, scale);
                }
                
                invoiceCard.printAll(g2);
                return Printable.PAGE_EXISTS;
            }
        });
        
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak invoice: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportInvoiceAsImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(invoiceNumber + ".png"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                int w = invoiceCard.getWidth();
                int h = invoiceCard.getHeight();
                
                if (w == 0 || h == 0) {
                    w = 720;
                    h = 840;
                }
                
                BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Paint background white
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, w, h);
                
                invoiceCard.paint(g2);
                g2.dispose();
                
                ImageIO.write(img, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Invoice berhasil diunduh ke:\n" + fileToSave.getAbsolutePath(), "Unduh Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mengunduh invoice: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void navigateToInvoicePanel() {
        java.awt.Container parent = this.getParent();
        while (parent != null && !(parent instanceof MenuInvoice)) {
            parent = parent.getParent();
        }
        if (parent instanceof MenuInvoice) {
            MenuInvoice menuInvoice = (MenuInvoice) parent;
            menuInvoice.switchContent(new InvoicePanel());
        }
    }

    public static Font getModernFont(int style, int size) {
        Font font = new Font("Inter", style, size);
        if (font.getFamily().equals("Dialog") && !font.getName().equals("Inter")) {
            font = new Font("Segoe UI", style, size);
        }
        return font;
    }

    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor = Color.WHITE;
        
        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        }
        
        public RoundedPanel(int radius, Color bg) {
            this.cornerRadius = radius;
            this.backgroundColor = bg;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Outer shadow
            g2.setColor(new Color(0, 0, 0, 5));
            g2.fillRoundRect(1, 2, width - 2, height - 4, cornerRadius, cornerRadius);
            
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
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
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
}
