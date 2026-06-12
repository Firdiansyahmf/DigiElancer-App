package digielancer.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// dialogg
import digielancer.component.PriceDialog;

public class OnboardingScreen extends JFrame {

    private CardLayout cardLayout;
    private JPanel rightCardPanel;
    private JLabel lblStep1, lblStep2, lblStep3;

    private String regBusinessName, regEmail, regPassword;
    private List<ServiceData> selectedServices = new ArrayList<>();
    
    // forr step 1
    private JTextField tfNamaBisnis, tfEmail;
    private JPasswordField pfPassword, pfConfirm;

    // forr step 2
    private JPanel skillsGrid;
    private JTextField tfCustomSkill;

    // forr step 3
    private JComboBox<ServiceData> cbServices;
    private JPanel addOnsListPanel;
    private JTextField tfAddonName, tfAddonPrice;

    public OnboardingScreen() {
        setTitle("Digi Elancer - Mulai Perjalanan Freelance Anda");
        setSize(1280, 832);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // favicon
        try {
            java.net.URL iconURL = getClass().getResource("/digielancer/assets/favicon-64.png");
            if(iconURL != null) {
                ImageIcon appIcon = new ImageIcon(iconURL);
                setIconImage(appIcon.getImage());
            }
        } catch(Exception ignored){}
        
        setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        getContentPane().setBackground(new Color(248, 250, 252));

        JPanel mainWrapper = new JPanel();
        mainWrapper.setLayout(new BoxLayout(mainWrapper, BoxLayout.Y_AXIS));
        mainWrapper.setOpaque(false);
        mainWrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel mainTitle = new JLabel("Mulai Perjalanan Freelance Anda");
        mainTitle.setFont(new Font("Inter", Font.BOLD, 28));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("Kelola project, buat invoice, dan kembangkan bisnis freelance Anda");
        subTitle.setFont(new Font("Inter", Font.PLAIN, 14));
        subTitle.setForeground(new Color(100, 116, 139));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // strepperr
        JPanel stepperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        stepperPanel.setOpaque(false);
        
        lblStep1 = createStepLabel("1. Akun & Profil", true);
        JLabel line1 = new JLabel(" ──── ");
        line1.setForeground(Color.LIGHT_GRAY);
        lblStep2 = createStepLabel("2. Keahlian", false);
        JLabel line2 = new JLabel(" ──── ");
        line2.setForeground(Color.LIGHT_GRAY);
        lblStep3 = createStepLabel("3. Tarif & Add-ons", false);

        stepperPanel.add(lblStep1);
        stepperPanel.add(line1);
        stepperPanel.add(lblStep2);
        stepperPanel.add(line2);
        stepperPanel.add(lblStep3);

        headerPanel.add(mainTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(subTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        headerPanel.add(stepperPanel);

        // mainn split
        JPanel splitPanel = new JPanel();
        splitPanel.setLayout(new BoxLayout(splitPanel, BoxLayout.X_AXIS));
        splitPanel.setOpaque(false);

        // kirii 
        JPanel leftBanner = createLeftBanner();
        leftBanner.setPreferredSize(new Dimension(340, 560));
        leftBanner.setMaximumSize(new Dimension(340, 560)); 
        leftBanner.setMinimumSize(new Dimension(340, 560));

        // kanann
        cardLayout = new CardLayout();
        rightCardPanel = new JPanel(cardLayout);
        rightCardPanel.setOpaque(false);
        rightCardPanel.setPreferredSize(new Dimension(540, 560));
        rightCardPanel.setMaximumSize(new Dimension(540, 560)); 
        rightCardPanel.setMinimumSize(new Dimension(540, 560));

        rightCardPanel.add(createStep1Panel(), "STEP1");
        rightCardPanel.add(createStep2Panel(), "STEP2");
        rightCardPanel.add(createStep3Panel(), "STEP3");

        splitPanel.add(leftBanner);
        splitPanel.add(Box.createRigidArea(new Dimension(30, 0))); 
        splitPanel.add(rightCardPanel);

        mainWrapper.add(headerPanel);
        mainWrapper.add(splitPanel);
        add(mainWrapper);
    }

    // stepp 1
    private JPanel createStep1Panel() {
        JPanel panel = createBaseCardPanel("Buat Akun Baru", "Lengkapi data diri Anda untuk memulai");

        tfNamaBisnis = new JTextField();
        tfEmail = new JTextField();
        pfPassword = new JPasswordField();
        pfConfirm = new JPasswordField();

        panel.add(createInputGroup("Nama Bisnis", tfNamaBisnis, "Masukkan nama lengkap / perusahaan"));
        panel.add(createInputGroup("Email", tfEmail, "email@example.com"));
        panel.add(createInputGroup("Password", pfPassword, "Minimal 8 karakter"));
        panel.add(createInputGroup("Konfirmasi Password", pfConfirm, "Ketik ulang password"));

        panel.add(Box.createVerticalGlue());

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton btnLogin = styleButton("Sudah Punya Akun", new Color(243, 244, 246), new Color(31, 41, 55));
        btnLogin.addActionListener(e -> {
            this.dispose();
            java.awt.EventQueue.invokeLater(() -> new LoginScreen().setVisible(true));
        });

        JButton btnLanjut = styleButton("Lanjut ->", new Color(14, 165, 233), Color.WHITE);
        btnLanjut.addActionListener(e -> validateAndGoToStep2());

        btnPanel.add(btnLogin);
        btnPanel.add(btnLanjut);
        
        panel.add(btnPanel);
        return panel;
    }

    private void validateAndGoToStep2() {
        regBusinessName = tfNamaBisnis.getText().trim();
        regEmail = tfEmail.getText().trim();
        regPassword = new String(pfPassword.getPassword());
        String confirm = new String(pfConfirm.getPassword());

        if(regBusinessName.isEmpty() || regEmail.isEmpty() || regPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!regEmail.contains("@")) {
            JOptionPane.showMessageDialog(this, "Format email tidak valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!regPassword.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Password dan Konfirmasi tidak cocok!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        updateStepper(2);
        cardLayout.show(rightCardPanel, "STEP2");
    }

    // stepp 2
    private JPanel createStep2Panel() {
        JPanel panel = createBaseCardPanel("Keahlian Anda", "Pilih jenis pekerjaan freelance yang Anda tawarkan");

        JLabel lblReq = new JLabel("Pilih Keahlian (Minimal 1)");
        lblReq.setFont(new Font("Inter", Font.BOLD, 12));
        lblReq.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblReq);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        skillsGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        skillsGrid.setOpaque(false);
        skillsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] presetSkills = {"Pembuatan Web", "Aplikasi Mobile", "Desain Grafis", "Motion Graphic", "Backend Dev", "Database Design", "API Integration", "AI/ML"};
        for (String skill : presetSkills) {
            skillsGrid.add(createSkillToggleButton(skill));
        }

        JPanel customPanel = new JPanel();
        customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.X_AXIS));
        customPanel.setOpaque(false);
        customPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        customPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        tfCustomSkill = new JTextField();
        styleTextField(tfCustomSkill);
        String pCustom = "Tambah keahlian lain...";
        addPlaceholder(tfCustomSkill, pCustom);
        
        JButton btnAddCustom = styleButton("+", new Color(14, 165, 233), Color.WHITE);
        btnAddCustom.setMaximumSize(new Dimension(50, 45));
        
        btnAddCustom.addActionListener(e -> {
            String newSkill = tfCustomSkill.getText().trim();
            if(!newSkill.isEmpty() && !newSkill.equals(pCustom)) {
                JToggleButton newBtn = createSkillToggleButton(newSkill);
                skillsGrid.add(newBtn);
                skillsGrid.revalidate();
                skillsGrid.repaint();
                
                tfCustomSkill.setText(pCustom);
                tfCustomSkill.setForeground(Color.GRAY);
                newBtn.doClick(); 
            }
        });
        
        customPanel.add(tfCustomSkill);
        customPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        customPanel.add(btnAddCustom);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton btnKembali = styleButton("<- Kembali", new Color(243, 244, 246), new Color(31, 41, 55));
        btnKembali.addActionListener(e -> {
            updateStepper(1);
            cardLayout.show(rightCardPanel, "STEP1");
        });

        JButton btnLanjut = styleButton("Lanjut ->", new Color(14, 165, 233), Color.WHITE);
        btnLanjut.addActionListener(e -> {
            if(selectedServices.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih minimal 1 keahlian terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            prepareStep3();
            updateStepper(3);
            cardLayout.show(rightCardPanel, "STEP3");
        });

        btnPanel.add(btnKembali);
        btnPanel.add(btnLanjut);

        panel.add(skillsGrid);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(customPanel);
        panel.add(Box.createVerticalGlue()); 
        panel.add(btnPanel);

        return panel;
    }

    private JToggleButton createSkillToggleButton(String skillName) {
        JToggleButton btn = new JToggleButton(skillName);
        btn.setFont(new Font("Inter", Font.BOLD, 12));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(100, 116, 139));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(8, 15, 8, 15)
        ));

        btn.addActionListener(e -> {
            if (btn.isSelected()) {
                PriceDialog dialog = new PriceDialog(this, skillName);
                dialog.setVisible(true);  
                Double priceResult = dialog.getPrice();
                if (priceResult != null) {
                    selectedServices.add(new ServiceData(skillName, priceResult));
                    btn.setBackground(new Color(14, 165, 233));
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setSelected(false); 
                }
            } else {
                selectedServices.removeIf(s -> s.name.equals(skillName));
                btn.setBackground(Color.WHITE);
                btn.setForeground(new Color(100, 116, 139));
            }
        });
        return btn;
    }

    // stepp 3
    private JPanel createStep3Panel() {
        JPanel panel = createBaseCardPanel("Tarif & Add-ons", "Tentukan harga untuk layanan tambahan yang Anda tawarkan");

        cbServices = new JComboBox<>();
        cbServices.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cbServices.setBackground(new Color(14, 165, 233));
        cbServices.setForeground(Color.WHITE);
        cbServices.setFont(new Font("Inter", Font.BOLD, 14));
        cbServices.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbServices.addActionListener(e -> renderAddOnsList());

        addOnsListPanel = new JPanel();
        addOnsListPanel.setLayout(new BoxLayout(addOnsListPanel, BoxLayout.Y_AXIS));
        addOnsListPanel.setOpaque(false);
        addOnsListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(addOnsListPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel inputAddonPanel = new JPanel();
        inputAddonPanel.setLayout(new BoxLayout(inputAddonPanel, BoxLayout.X_AXIS));
        inputAddonPanel.setOpaque(false);
        inputAddonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        inputAddonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String pName = "Nama layanan (misal: Mobile with DB)";
        String pPrice = "Harga (Rp)";

        tfAddonName = new JTextField();
        styleTextField(tfAddonName);
        addPlaceholder(tfAddonName, pName);
        
        tfAddonPrice = new JTextField();
        styleTextField(tfAddonPrice);
        addPlaceholder(tfAddonPrice, pPrice);
        tfAddonPrice.setPreferredSize(new Dimension(120, 45));
        tfAddonPrice.setMaximumSize(new Dimension(120, 45)); 
        
        JButton btnAddon = styleButton("+", new Color(14, 165, 233), Color.WHITE);
        btnAddon.setMaximumSize(new Dimension(50, 45));
        
        btnAddon.addActionListener(e -> {
            ServiceData activeService = (ServiceData) cbServices.getSelectedItem();
            String aName = tfAddonName.getText().trim();
            String aPriceStr = tfAddonPrice.getText().trim();
            
            if(activeService != null && !aName.isEmpty() && !aName.equals(pName) && !aPriceStr.isEmpty() && !aPriceStr.equals(pPrice)) {
                try {
                    String cleanPrice = aPriceStr.replaceAll("[^\\d]", "");
                    if(cleanPrice.isEmpty()) throw new NumberFormatException();

                    double p = Double.parseDouble(cleanPrice);
                    activeService.addOns.add(new AddOnData(aName, p));
                    
                    tfAddonName.setText(pName);
                    tfAddonName.setForeground(Color.GRAY);
                    tfAddonPrice.setText(pPrice);
                    tfAddonPrice.setForeground(Color.GRAY);
                    
                    renderAddOnsList(); 
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Harga harus berupa angka yang valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Silakan isi nama layanan dan harganya!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        inputAddonPanel.add(tfAddonName);
        inputAddonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        inputAddonPanel.add(tfAddonPrice);
        inputAddonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        inputAddonPanel.add(btnAddon);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton btnKembali = styleButton("<- Kembali", new Color(243, 244, 246), new Color(31, 41, 55));
        btnKembali.addActionListener(e -> {
            updateStepper(2);
            cardLayout.show(rightCardPanel, "STEP2");
        });

        JButton btnSelesai = styleButton("Selesai & Mulai ->", new Color(16, 185, 129), Color.WHITE);
        btnSelesai.addActionListener(e -> executeDatabaseRegistration());

        btnPanel.add(btnKembali);
        btnPanel.add(btnSelesai);

        panel.add(cbServices);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(inputAddonPanel);
        panel.add(Box.createVerticalGlue());
        panel.add(btnPanel);

        return panel;
    }

    private void prepareStep3() {
        cbServices.removeAllItems();
        for (ServiceData s : selectedServices) {
            cbServices.addItem(s);
        }
        renderAddOnsList();
    }

    private void renderAddOnsList() {
        addOnsListPanel.removeAll();
        ServiceData activeService = (ServiceData) cbServices.getSelectedItem();
        
        if (activeService != null) {
            for (int i = 0; i < activeService.addOns.size(); i++) {
                AddOnData addon = activeService.addOns.get(i);
                final int index = i;
                
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                    new EmptyBorder(10, 5, 10, 5)
                ));
                itemPanel.setOpaque(false);
                
                JLabel nameLbl = new JLabel(addon.name);
                nameLbl.setFont(new Font("Inter", Font.BOLD, 13));
                
                JLabel priceLbl = new JLabel(String.format("Rp %,.0f", addon.price));
                priceLbl.setFont(new Font("Inter", Font.BOLD, 13));
                priceLbl.setForeground(new Color(14, 165, 233));
                
                JButton btnDelete = styleButton("X", new Color(239, 68, 68), Color.WHITE);
                btnDelete.setPreferredSize(new Dimension(40, 30));
                btnDelete.addActionListener(e -> {
                    activeService.addOns.remove(index);
                    renderAddOnsList();
                });
                
                JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                rightBox.setOpaque(false);
                rightBox.add(priceLbl);
                rightBox.add(btnDelete);
                
                itemPanel.add(nameLbl, BorderLayout.CENTER);
                itemPanel.add(rightBox, BorderLayout.EAST);
                addOnsListPanel.add(itemPanel);
            }
        }
        addOnsListPanel.revalidate();
        addOnsListPanel.repaint();
    }

    private void executeDatabaseRegistration() {
        Connection conn = null;
        try {
            conn = KoneksiDB.configDB();
            conn.setAutoCommit(false); 

            String sqlUser = "INSERT INTO USER (business_name, email, password_hash) VALUES (?, ?, ?)";
            PreparedStatement pstUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            pstUser.setString(1, regBusinessName);
            pstUser.setString(2, regEmail);
            pstUser.setString(3, regPassword);
            pstUser.executeUpdate();

            ResultSet rsUser = pstUser.getGeneratedKeys();
            int userId = 0;
            if(rsUser.next()) {
                userId = rsUser.getInt(1);
                UserSession.setSession(userId, regBusinessName, regEmail);
            }

            String sqlService = "INSERT INTO MAIN_SERVICE (user_id, service_name, base_price) VALUES (?, ?, ?)";
            String sqlAddon = "INSERT INTO ADD_ON (main_service_id, addon_name, price) VALUES (?, ?, ?)";
            
            PreparedStatement pstService = conn.prepareStatement(sqlService, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pstAddon = conn.prepareStatement(sqlAddon);

            String sqlProject = "INSERT INTO project (user_id, main_service_id, client_name, client_contact, deadline) VALUES (?, ?, ?, ?, ?)";
            String sqlBoard = "INSERT INTO board (project_id, board_name, description, is_completion_board, position_index) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstProject = conn.prepareStatement(sqlProject, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pstBoard = conn.prepareStatement(sqlBoard);

            for (ServiceData s : selectedServices) {
                pstService.setInt(1, userId);
                pstService.setString(2, s.name);
                pstService.setDouble(3, s.basePrice);
                pstService.executeUpdate();

                ResultSet rsService = pstService.getGeneratedKeys();
                if(rsService.next()) {
                    int serviceId = rsService.getInt(1);
                    for (AddOnData a : s.addOns) {
                        pstAddon.setInt(1, serviceId);
                        pstAddon.setString(2, a.name);
                        pstAddon.setDouble(3, a.price);
                        pstAddon.addBatch(); 
                    }
                    pstAddon.executeBatch();

                    // Automatically create a default/sample project for this service
                    pstProject.setInt(1, userId);
                    pstProject.setInt(2, serviceId);
                    pstProject.setString(3, regBusinessName ); 
                    pstProject.setString(4, regEmail);
                    
                    // Set deadline to 30 days from now
                    java.sql.Date deadlineDate = new java.sql.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
                    pstProject.setDate(5, deadlineDate);
                    pstProject.executeUpdate();
                    
                    ResultSet rsProject = pstProject.getGeneratedKeys();
                    if (rsProject.next()) {
                        int projectId = rsProject.getInt(1);
                        
                        // Insert standard Kanban boards for this project
                        pstBoard.setInt(1, projectId);
                        pstBoard.setString(2, "To-Do");
                        pstBoard.setString(3, "Tasks that need to be started");
                        pstBoard.setInt(4, 0); // is_completion_board = false
                        pstBoard.setInt(5, 1); // position_index = 1
                        pstBoard.addBatch();
                        
                        pstBoard.setInt(1, projectId);
                        pstBoard.setString(2, "In Progress");
                        pstBoard.setString(3, "Tasks currently being worked on");
                        pstBoard.setInt(4, 0); // is_completion_board = false
                        pstBoard.setInt(5, 2); // position_index = 2
                        pstBoard.addBatch();
                        
                        pstBoard.setInt(1, projectId);
                        pstBoard.setString(2, "Done");
                        pstBoard.setString(3, "Completed tasks ready for review");
                        pstBoard.setInt(4, 1); 
                        pstBoard.setInt(5, 3); 
                        pstBoard.addBatch();
                        
                        pstBoard.executeBatch();
                    }
                }
            }

            conn.commit(); 
            JOptionPane.showMessageDialog(this, "Registrasi Sukses! Selamat datang di Digi Elancer.");
            
            this.dispose();
            java.awt.EventQueue.invokeLater(() -> new Navbar().setVisible(true)); 

        } catch (SQLException ex) {
            try { if(conn != null) conn.rollback(); } catch(Exception ignored){}
            JOptionPane.showMessageDialog(this, "Gagal Registrasi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if(conn != null) conn.setAutoCommit(true); } catch(Exception ignored){}
        }
    }

    // utilityy
    private void updateStepper(int step) {
        lblStep1.setForeground(step >= 1 ? new Color(14, 165, 233) : Color.GRAY);
        lblStep2.setForeground(step >= 2 ? new Color(14, 165, 233) : Color.GRAY);
        lblStep3.setForeground(step >= 3 ? new Color(14, 165, 233) : Color.GRAY);
    }
    private JLabel createStepLabel(String text, boolean active) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Inter", Font.BOLD, 14));
        lbl.setForeground(active ? new Color(14, 165, 233) : Color.GRAY);
        return lbl;
    }
    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    //left bnnerr
    private JPanel createLeftBanner() {
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(14, 165, 233));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false); 
        left.setBorder(new EmptyBorder(40, 25, 40, 25));
        
        JLabel logo = new JLabel("DE Digi Elancer");
        logo.setFont(new Font("Inter", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // rokett
        try {
            java.net.URL imgURL = getClass().getResource("/digielancer/assets/roket.png");
            if(imgURL != null) {
                JLabel imgPlaceholder = new JLabel(new ImageIcon(imgURL));
                imgPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
                left.add(logo);
                left.add(Box.createRigidArea(new Dimension(0, 50)));
                left.add(imgPlaceholder);
            } else {
                left.add(logo);
            }
        } catch(Exception e) {
            left.add(logo);
        }
        
        left.add(Box.createVerticalGlue());
        
        left.add(createInfoCard("🛡️", "Keamanan Terjamin", "Data Anda aman bersama kami"));
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(createInfoCard("⚡", "Otomasi Invoice", "Generate nota dalam hitungan detik"));
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(createInfoCard("📈", "Grow Your Business", "Kelola project dengan mudah"));
        return left;
    }
    private JPanel createInfoCard(String iconStr, String titleStr, String descStr) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = new JLabel(iconStr);
        icon.setFont(new Font("Inter", Font.PLAIN, 24));
        icon.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel title = new JLabel(titleStr);
        title.setFont(new Font("Inter", Font.BOLD, 13));
        title.setForeground(Color.WHITE);
        
        JLabel desc = new JLabel(descStr);
        desc.setFont(new Font("Inter", Font.PLAIN, 11));
        desc.setForeground(new Color(241, 245, 249));

        textPanel.add(title);
        textPanel.add(desc);

        card.add(icon);
        card.add(Box.createRigidArea(new Dimension(15, 0)));
        card.add(textPanel);
        return card;
    }
    private JPanel createBaseCardPanel(String titleStr, String subStr) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.setColor(new Color(226, 232, 240));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); 
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel(titleStr);
        title.setFont(new Font("Inter", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(subStr);
        sub.setFont(new Font("Inter", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 116, 139));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(sub);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        return panel;
    }
    private JPanel createInputGroup(String labelText, JTextField field, String placeholder) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Inter", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        styleTextField(field);
        if(placeholder != null) addPlaceholder(field, placeholder);
        
        group.add(lbl);
        group.add(Box.createRigidArea(new Dimension(0, 8)));
        group.add(field);
        group.add(Box.createRigidArea(new Dimension(0, 20)));
        return group;
    }
    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(350, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true), 
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private JButton styleButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Inter", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    class ServiceData {
        String name;
        double basePrice;
        List<AddOnData> addOns = new ArrayList<>();
        public ServiceData(String n, double bp) { this.name = n; this.basePrice = bp; }
        @Override
        public String toString() { return name; } 
    }

    class AddOnData {
        String name;
        double price;
        public AddOnData(String n, double p) { this.name = n; this.price = p; }
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new OnboardingScreen().setVisible(true));
    }
}