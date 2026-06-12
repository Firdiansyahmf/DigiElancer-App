package digielancer.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PriceDialog extends JDialog {
    private Double priceResult = null;

    public PriceDialog(JFrame parent, String skillName) {
        super(parent, true);
        setUndecorated(true);
        setSize(940, 240);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(226, 232, 240));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel titleLabel = new JLabel("Tarif Dasar Keahlian Anda");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(new Color(15, 23, 42));
        
        JButton btnClose = new JButton("X");
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setFont(new Font("Inter", Font.PLAIN, 18));
        btnClose.setForeground(new Color(148, 163, 184));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnClose, BorderLayout.EAST);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField tfPrice = new JTextField();
        styleTextField(tfPrice);
        addPlaceholder(tfPrice, "Tarif dasar keahlian ini...");
        tfPrice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        tfPrice.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
        
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSave = styleButton("Simpan Tarif", new Color(14, 165, 233), Color.WHITE);
        btnSave.setPreferredSize(new Dimension(140, 45));
        btnSave.setMaximumSize(new Dimension(140, 45));

        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnContainer.setOpaque(false);
        btnContainer.add(btnSave);
        btnContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSave.addActionListener(e -> {
            String input = tfPrice.getText().replaceAll("[^\\d]", "");
            if(!input.isEmpty()) {
                try {
                    priceResult = Double.parseDouble(input);
                    dispose();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Harga tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Harga harus diisi dengan angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(tfPrice);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(btnContainer);
        add(mainPanel);
    }

    public Double getPrice() {
        return priceResult;
    }

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true), 
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
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

    private JButton styleButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Inter", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
}