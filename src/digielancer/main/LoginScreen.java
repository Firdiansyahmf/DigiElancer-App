package digielancer.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("Digi Elancer - Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new GridLayout(1, 2)); 

        // kirii
        JPanel leftPanel = new JPanel(new GridBagLayout()) { 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(11, 25, 44));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(17, 42, 70));
                g2d.fillOval(-100, -50, 400, 400); 
                g2d.fillOval(getWidth() - 250, getHeight() - 250, 400, 400); 
            }
        };
        JPanel innerLeft = new JPanel();
        innerLeft.setLayout(new BoxLayout(innerLeft, BoxLayout.Y_AXIS));
        innerLeft.setOpaque(false);

        JLabel logoLabel = new JLabel(" DE ");
        logoLabel.setFont(new Font("Inter", Font.BOLD, 50));
        logoLabel.setForeground(new Color(15, 118, 206));
        logoLabel.setOpaque(true);
        logoLabel.setBackground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Digi Elancer");
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("GoDigi BETA");
        subtitle.setFont(new Font("Inter", Font.PLAIN, 18));
        subtitle.setForeground(new Color(148, 163, 184));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><div style='text-align: center; width: 350px;'>Sistem manajemen siklus kerja freelancer dengan otomatisasi nota yang powerful dan mudah digunakan</div></html>");
        desc.setFont(new Font("Inter", Font.PLAIN, 13));
        desc.setForeground(new Color(203, 213, 225));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(400, 60));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        statsPanel.add(createStatItem("500+", "Freelancers"));
        statsPanel.add(createStatItem("10K+", "Projects"));
        statsPanel.add(createStatItem("98%", "Satisfaction"));

        innerLeft.add(logoLabel);
        innerLeft.add(Box.createRigidArea(new Dimension(0, 20)));
        innerLeft.add(title);
        innerLeft.add(Box.createRigidArea(new Dimension(0, 5)));
        innerLeft.add(subtitle);
        innerLeft.add(Box.createRigidArea(new Dimension(0, 30)));
        innerLeft.add(desc);
        innerLeft.add(Box.createRigidArea(new Dimension(0, 50)));
        innerLeft.add(statsPanel);

        leftPanel.add(innerLeft);

        // kanann
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setPreferredSize(new Dimension(350, 450));

        JLabel welcomeTitle = new JLabel("Selamat Datang Kembali!");
        welcomeTitle.setFont(new Font("Inter", Font.BOLD, 24));
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeSub = new JLabel("Masuk ke akun Anda untuk melanjutkan");
        welcomeSub.setFont(new Font("Inter", Font.PLAIN, 13));
        welcomeSub.setForeground(Color.GRAY);
        welcomeSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Inter", Font.BOLD, 12));
        emailLabel.setMaximumSize(new Dimension(350, 20));
        emailLabel.setHorizontalAlignment(SwingConstants.LEFT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        JTextField emailField = new JTextField("nama@email.com");
        styleTextField(emailField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Inter", Font.BOLD, 12));
        passLabel.setMaximumSize(new Dimension(350, 20));
        passLabel.setHorizontalAlignment(SwingConstants.LEFT);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPasswordField passField = new JPasswordField("password");
        styleTextField(passField);

        JButton loginBtn = new JButton("Masuk");
        loginBtn.setFont(new Font("Inter", Font.BOLD, 14));
        loginBtn.setBackground(new Color(0, 136, 255)); 
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(350, 40));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // loginn db
        loginBtn.addActionListener(e -> {
            String emailInput = emailField.getText();
            String passInput = new String(passField.getPassword());
            
            if(emailInput.equals("nama@email.com") || passInput.isEmpty() || passInput.equals("password")){
                JOptionPane.showMessageDialog(this, "Email dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String sql = "SELECT * FROM USER WHERE email = ? AND password_hash = ?";
                Connection conn = KoneksiDB.configDB();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, emailInput);
                pst.setString(2, passInput); 
                
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    UserSession.setSession(
                        rs.getInt("id"), 
                        rs.getString("business_name"), 
                        rs.getString("email")
                    );
                    
                    JOptionPane.showMessageDialog(this, "Berhasil Login! Selamat Datang " + rs.getString("business_name"));
                    this.dispose(); 
                    java.awt.EventQueue.invokeLater(() -> {
                        new Navbar().setVisible(true); 
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Email atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JLabel divider = new JLabel("atau");
        divider.setForeground(Color.LIGHT_GRAY);
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerBtn = new JButton("Daftar Akun Baru");
        registerBtn.setFont(new Font("Inter", Font.BOLD, 14));
        registerBtn.setBackground(new Color(243, 244, 246)); 
        registerBtn.setForeground(new Color(31, 41, 55));
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setMaximumSize(new Dimension(350, 40));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT); 
        registerBtn.addActionListener(e -> {
            this.dispose();
            java.awt.EventQueue.invokeLater(() -> {
                new OnboardingScreen().setVisible(true);
            });
        });
        
        formContainer.add(welcomeTitle);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(welcomeSub);
        formContainer.add(Box.createRigidArea(new Dimension(0, 35)));
        
        formContainer.add(emailLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(emailField);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        
        formContainer.add(passLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(passField);
        formContainer.add(Box.createRigidArea(new Dimension(0, 35)));
        
        formContainer.add(loginBtn);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        formContainer.add(divider);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        formContainer.add(registerBtn);

        rightPanel.add(formContainer);

        add(leftPanel);
        add(rightPanel);
    }

    private JPanel createStatItem(String number, String label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel numLabel = new JLabel(number);
        numLabel.setFont(new Font("Inter", Font.BOLD, 24));
        numLabel.setForeground(Color.WHITE);
        numLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel txtLabel = new JLabel(label);
        txtLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        txtLabel.setForeground(new Color(148, 163, 184));
        txtLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(numLabel);
        panel.add(txtLabel);
        return panel;
    }

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(350, 40));
        field.setPreferredSize(new Dimension(350, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setForeground(Color.GRAY);
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(field.getText().equals("nama@email.com") || field.getText().equals("password")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if(field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    if(field instanceof JPasswordField) field.setText("password");
                    else field.setText("nama@email.com");
                }
            }
        });
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}