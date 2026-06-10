package digielancer.main;
import javax.swing.*;
import java.awt.*;


public class SplashScreen extends JFrame {

    private JProgressBar progressBar;
    private JLabel loadingText;
    
    public SplashScreen() {
        setUndecorated(true);
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(248, 250, 252));
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));

        // logoo
        // ImageIcon icon = new ImageIcon(getClass().getResource("/digielancer/assets/logo.png"));
        // JLabel logoLabel = new JLabel(icon);
        JLabel logoLabel = new JLabel("DE");
        logoLabel.setFont(new Font("Inter", Font.BOLD, 48));
        logoLabel.setForeground(new Color(15, 118, 206));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // titlee
        JLabel titleLabel = new JLabel("Digi Elancer");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // subtitlee
        JLabel subtitleLabel = new JLabel("GoDigi BETA");
        subtitleLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(15, 118, 206));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // bar
        progressBar = new JProgressBar();
        progressBar.setMaximumSize(new Dimension(300, 4));
        progressBar.setPreferredSize(new Dimension(300, 4));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(new Color(226, 232, 240));
        progressBar.setForeground(new Color(15, 118, 206));

        // load textt
        loadingText = new JLabel("Sistem Manajemen Freelancer");
        loadingText.setFont(new Font("Inter", Font.PLAIN, 12));
        loadingText.setForeground(new Color(74, 85, 101));
        loadingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(progressBar);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(loadingText);

        // footerr
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel footerText = new JLabel("Powered by GoDigi BETA © 2026");
        footerText.setFont(new Font("Inter", Font.PLAIN, 10));
        footerText.setForeground(new Color(148, 163, 184));
        footerPanel.add(footerText);

        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    public void startLoading() {
        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(30);
                    progressBar.setValue(i);
                    
                    if (i == 30) loadingText.setText("Loading Modules...");
                    if (i == 60) loadingText.setText("Connecting to Database...");
                    if (i == 90) loadingText.setText("Starting Digi Elancer...");
                }
                this.dispose();
                
                java.awt.EventQueue.invokeLater(() -> {
                    new Navbar().setVisible(true);
                });
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
      public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
            splash.startLoading();
        });
    }                 
}
