/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digielancer.component;

import digielancer.main.UserSession;
import digielancer.main.KoneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Dwi R.A. Kautsar
 */
public class tarifSett extends javax.swing.JPanel {

    /**
     * Creates new form tarifSett
     */
    class ServiceItem {
        int id;
        String name;
        public ServiceItem(int id, String name) { this.id = id; this.name = name; }
        @Override
        public String toString() { return name; }
    }

    public tarifSett() {
        initComponents();
        loadDynamicAddons();
    }

    private void loadDynamicAddons() {
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Tarif Add-Ons");
        title.setFont(new Font("Inter", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // Container for addons list
        JPanel addonsList = new JPanel();
        addonsList.setLayout(new BoxLayout(addonsList, BoxLayout.Y_AXIS));
        addonsList.setBackground(Color.WHITE);
        addonsList.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<ServiceItem> cbServices = new JComboBox<>();
        cbServices.setMaximumSize(new Dimension(200, 40));
        
        try {
            Connection conn = KoneksiDB.configDB();
            
            // Populate ComboBox
            String sqlMs = "SELECT id, service_name FROM MAIN_SERVICE WHERE user_id = ?";
            PreparedStatement pstMs = conn.prepareStatement(sqlMs);
            pstMs.setInt(1, UserSession.getId());
            ResultSet rsMs = pstMs.executeQuery();
            while (rsMs.next()) {
                cbServices.addItem(new ServiceItem(rsMs.getInt("id"), rsMs.getString("service_name")));
            }
            
            // Populate Addons List
            String sqlAddon = "SELECT a.id, m.service_name, a.addon_name, a.price FROM ADD_ON a JOIN MAIN_SERVICE m ON a.main_service_id = m.id WHERE m.user_id = ?";
            PreparedStatement pstAddon = conn.prepareStatement(sqlAddon);
            pstAddon.setInt(1, UserSession.getId());
            ResultSet rsAddon = pstAddon.executeQuery();
            
            while (rsAddon.next()) {
                int addonId = rsAddon.getInt("id");
                String msName = rsAddon.getString("service_name");
                String addonName = rsAddon.getString("addon_name");
                double price = rsAddon.getDouble("price");

                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBackground(new Color(226, 232, 240));
                itemPanel.setMaximumSize(new Dimension(630, 60));
                itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                
                JLabel lblName = new JLabel(msName + " - " + addonName);
                lblName.setFont(new Font("Inter", Font.BOLD, 14));
                
                JLabel lblPrice = new JLabel(String.format("Rp %,.0f", price));
                lblPrice.setFont(new Font("Inter", Font.BOLD, 14));
                lblPrice.setForeground(new Color(6, 141, 240));
                
                JButton btnDelete = new JButton("X");
                btnDelete.setBackground(new Color(255, 51, 51));
                btnDelete.setForeground(Color.WHITE);
                btnDelete.setFont(new Font("Inter", Font.BOLD, 14));
                btnDelete.setBorderPainted(false);
                btnDelete.setFocusPainted(false);
                btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnDelete.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus Add-on ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement del = conn.prepareStatement("DELETE FROM ADD_ON WHERE id=?");
                            del.setInt(1, addonId);
                            del.executeUpdate();
                            loadDynamicAddons(); // Reload UI
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                
                JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                rightBox.setOpaque(false);
                rightBox.add(lblPrice);
                rightBox.add(btnDelete);
                
                itemPanel.add(lblName, BorderLayout.WEST);
                itemPanel.add(rightBox, BorderLayout.EAST);
                
                addonsList.add(itemPanel);
                addonsList.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JScrollPane scrollPane = new JScrollPane(addonsList);
        scrollPane.setBorder(null);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(scrollPane);
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // Input Area
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField tfName = new JTextField();
        tfName.setMaximumSize(new Dimension(250, 40));
        tfName.setPreferredSize(new Dimension(250, 40));
        tfName.setText("Nama Add-on");
        tfName.setForeground(Color.GRAY);
        tfName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) { if(tfName.getText().equals("Nama Add-on")) { tfName.setText(""); tfName.setForeground(Color.BLACK); } }
            public void focusLost(java.awt.event.FocusEvent e) { if(tfName.getText().isEmpty()) { tfName.setText("Nama Add-on"); tfName.setForeground(Color.GRAY); } }
        });
        
        JTextField tfPrice = new JTextField();
        tfPrice.setMaximumSize(new Dimension(130, 40));
        tfPrice.setPreferredSize(new Dimension(130, 40));
        tfPrice.setText("Harga (Rp)");
        tfPrice.setForeground(Color.GRAY);
        tfPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) { if(tfPrice.getText().equals("Harga (Rp)")) { tfPrice.setText(""); tfPrice.setForeground(Color.BLACK); } }
            public void focusLost(java.awt.event.FocusEvent e) { if(tfPrice.getText().isEmpty()) { tfPrice.setText("Harga (Rp)"); tfPrice.setForeground(Color.GRAY); } }
        });
        
        JButton btnAdd = new JButton("+");
        btnAdd.setBackground(new Color(6, 141, 240));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Inter", Font.BOLD, 18));
        btnAdd.setBorderPainted(false);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdd.addActionListener(e -> {
            ServiceItem selectedMs = (ServiceItem) cbServices.getSelectedItem();
            String name = tfName.getText().trim();
            String priceStr = tfPrice.getText().trim().replaceAll("[^\\d]", "");
            
            if (selectedMs == null) {
                JOptionPane.showMessageDialog(this, "Pilih Layanan Utama terlebih dahulu!");
                return;
            }
            if (name.isEmpty() || name.equals("Nama Add-on") || priceStr.isEmpty() || priceStr.equals("Harga (Rp)")) {
                JOptionPane.showMessageDialog(this, "Nama Add-on dan Harga harus diisi!");
                return;
            }
            
            try {
                double price = Double.parseDouble(priceStr);
                Connection conn = KoneksiDB.configDB();
                String sql = "INSERT INTO ADD_ON (main_service_id, addon_name, price) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, selectedMs.id);
                pst.setString(2, name);
                pst.setDouble(3, price);
                pst.executeUpdate();
                
                loadDynamicAddons(); // Reload UI
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        inputPanel.add(cbServices);
        inputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        inputPanel.add(tfName);
        inputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        inputPanel.add(tfPrice);
        inputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        inputPanel.add(btnAdd);
        
        this.add(inputPanel);
        
        this.revalidate();
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cancelButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cancelButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        layananTextField = new javax.swing.JTextField();
        hargaTextField = new javax.swing.JTextField();
        tambahButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        jLabel1.setText("Tarif Add-Ons");

        jPanel1.setBackground(new java.awt.Color(226, 232, 240));
        jPanel1.setMaximumSize(new java.awt.Dimension(630, 66));
        jPanel1.setMinimumSize(new java.awt.Dimension(630, 66));
        jPanel1.setPreferredSize(new java.awt.Dimension(630, 66));

        jLabel2.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        jLabel2.setText("Web Statis");

        cancelButton.setBackground(new java.awt.Color(255, 51, 51));
        cancelButton.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        cancelButton.setText("X");
        cancelButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(this::cancelButtonActionPerformed);

        jLabel3.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(6, 141, 240));
        jLabel3.setText("Rp 500.000");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton)
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(226, 232, 240));
        jPanel2.setMaximumSize(new java.awt.Dimension(630, 66));
        jPanel2.setMinimumSize(new java.awt.Dimension(630, 66));

        jLabel4.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        jLabel4.setText("Dashboard Admin");

        cancelButton1.setBackground(new java.awt.Color(255, 51, 51));
        cancelButton1.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        cancelButton1.setText("X");
        cancelButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cancelButton1.setBorderPainted(false);
        cancelButton1.setFocusPainted(false);
        cancelButton1.addActionListener(this::cancelButton1ActionPerformed);

        jLabel5.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(6, 141, 240));
        jLabel5.setText("Rp 3.000.000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton1)
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(cancelButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(226, 232, 240));
        jPanel3.setMaximumSize(new java.awt.Dimension(630, 66));
        jPanel3.setMinimumSize(new java.awt.Dimension(630, 66));

        jLabel6.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        jLabel6.setText("Midtrans");

        cancelButton2.setBackground(new java.awt.Color(255, 51, 51));
        cancelButton2.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        cancelButton2.setText("X");
        cancelButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cancelButton2.setBorderPainted(false);
        cancelButton2.setFocusPainted(false);
        cancelButton2.addActionListener(this::cancelButton2ActionPerformed);

        jLabel7.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(6, 141, 240));
        jLabel7.setText("Rp 1.500.000");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton2)
                .addGap(12, 12, 12))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton2)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layananTextField.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        layananTextField.setForeground(new java.awt.Color(204, 204, 204));
        layananTextField.setText("Nama Layanan (misal: Mobile with DB)");
        layananTextField.addActionListener(this::layananTextFieldActionPerformed);

        hargaTextField.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        hargaTextField.setForeground(new java.awt.Color(204, 204, 204));
        hargaTextField.setText("Harga (Rp)");
        hargaTextField.addActionListener(this::hargaTextFieldActionPerformed);

        tambahButton.setBackground(new java.awt.Color(6, 141, 240));
        tambahButton.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        tambahButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahButton.setText("+");
        tambahButton.addActionListener(this::tambahButtonActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(layananTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tambahButton)))
                        .addGap(0, 38, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(layananTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tambahButton))
                .addContainerGap(43, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void cancelButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButton2ActionPerformed

    private void layananTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_layananTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_layananTextFieldActionPerformed

    private void hargaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaTextFieldActionPerformed

    private void tambahButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tambahButtonActionPerformed

    private void cancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton cancelButton1;
    private javax.swing.JButton cancelButton2;
    private javax.swing.JTextField hargaTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField layananTextField;
    private javax.swing.JButton tambahButton;
    // End of variables declaration//GEN-END:variables
}
