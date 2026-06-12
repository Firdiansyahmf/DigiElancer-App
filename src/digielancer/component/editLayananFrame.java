/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package digielancer.component;

import digielancer.main.UserSession;
import digielancer.main.KoneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Dwi R.A. Kautsar
 */
public class editLayananFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(editLayananFrame.class.getName());

    /**
     * Creates new form editLayananFrame
     */
    public editLayananFrame() {
        initComponents();
        setupUI();
    }

    private List<String> existingServices = new ArrayList<>();

    private void setupUI() {
        // Fetch existing services
        try {
            Connection conn = KoneksiDB.configDB();
            String sql = "SELECT service_name FROM MAIN_SERVICE WHERE user_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, UserSession.getId());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                existingServices.add(rs.getString("service_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Styling toggle buttons
        styleToggleButton(jToggleButton1, "Pembuatan Web");
        styleToggleButton(jToggleButton2, "Aplikasi Mobile");
        styleToggleButton(jToggleButton3, "Desain Grafis");
        styleToggleButton(jToggleButton4, "Motion Graphic");
        styleToggleButton(jToggleButton5, "Backend Dev");
        styleToggleButton(jToggleButton6, "Database Design");
        styleToggleButton(jToggleButton7, "API Integration");

        // Styling placeholder for textfield
        jTextField1.setText("Tambah Keahlian Lain...");
        jTextField1.setForeground(java.awt.Color.GRAY);
        
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (jTextField1.getText().equals("Tambah Keahlian Lain...")) {
                    jTextField1.setText("");
                    jTextField1.setForeground(java.awt.Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (jTextField1.getText().isEmpty()) {
                    jTextField1.setText("Tambah Keahlian Lain...");
                    jTextField1.setForeground(java.awt.Color.GRAY);
                }
            }
        });

        hargaTextField.setText("Harga (Rp)");
        hargaTextField.setForeground(java.awt.Color.GRAY);
        
        hargaTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (hargaTextField.getText().equals("Harga (Rp)") || hargaTextField.getText().equals("Harga")) {
                    hargaTextField.setText("");
                    hargaTextField.setForeground(java.awt.Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (hargaTextField.getText().isEmpty()) {
                    hargaTextField.setText("Harga (Rp)");
                    hargaTextField.setForeground(java.awt.Color.GRAY);
                }
            }
        });

        simpanButton.addActionListener(e -> simpanLayanan());
    }

    private void styleToggleButton(javax.swing.JToggleButton btn, String text) {
        btn.setText(text);
        if (existingServices.contains(text)) {
            btn.setSelected(true);
            btn.setBackground(new java.awt.Color(14, 165, 233));
            btn.setForeground(java.awt.Color.WHITE);
        } else {
            btn.setBackground(java.awt.Color.WHITE);
            btn.setForeground(new java.awt.Color(100, 116, 139));
        }
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        btn.addItemListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(new java.awt.Color(14, 165, 233));
                btn.setForeground(java.awt.Color.WHITE);
            } else {
                btn.setBackground(java.awt.Color.WHITE);
                btn.setForeground(new java.awt.Color(100, 116, 139));
            }
        });
    }

    private void simpanLayanan() {
        List<String> selectedServices = new ArrayList<>();
        javax.swing.JToggleButton[] btns = {jToggleButton1, jToggleButton2, jToggleButton3, jToggleButton4, jToggleButton5, jToggleButton6, jToggleButton7};
        for (javax.swing.JToggleButton btn : btns) {
            if (btn.isSelected()) selectedServices.add(btn.getText());
        }
        
        // custom service from textfield
        String custom = jTextField1.getText().trim();
        if (!custom.isEmpty() && !custom.equals("Tambah Keahlian Lain...")) {
            selectedServices.add(custom);
        }
        
        if (selectedServices.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih minimal 1 keahlian!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double customPrice = 500000.0;
        String priceText = hargaTextField.getText().replaceAll("[^\\d]", "");
        if (!priceText.isEmpty()) {
            customPrice = Double.parseDouble(priceText);
        }

        try {
            Connection conn = KoneksiDB.configDB();
            conn.setAutoCommit(false);
            
            // For each selected, if not in existing -> insert
            for (String sel : selectedServices) {
                if (!existingServices.contains(sel)) {
                    String sqlIn = "INSERT INTO MAIN_SERVICE (user_id, service_name, base_price) VALUES (?, ?, ?)";
                    PreparedStatement pstIn = conn.prepareStatement(sqlIn);
                    pstIn.setInt(1, UserSession.getId());
                    pstIn.setString(2, sel);
                    pstIn.setDouble(3, customPrice);
                    pstIn.executeUpdate();
                }
            }
            
            conn.commit();
            JOptionPane.showMessageDialog(this, "Layanan berhasil disimpan!");
            this.dispose(); // Close modal
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan layanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jTextField1 = new javax.swing.JTextField();
        tambahButton = new javax.swing.JButton();
        simpanButton = new javax.swing.JButton();
        hargaTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        jLabel1.setText("Edit Jenis Layanan");

        jLabel2.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel2.setText("Pilih Keahlian (Minimal 1)");

        jToggleButton1.setBackground(new java.awt.Color(6, 141, 240));
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setText("Pembuatan Web");
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.addActionListener(this::jToggleButton1ActionPerformed);

        jToggleButton2.setBackground(new java.awt.Color(6, 141, 240));
        jToggleButton2.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton2.setText("Pembuatan Web");
        jToggleButton2.setBorderPainted(false);
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.addActionListener(this::jToggleButton2ActionPerformed);

        jToggleButton3.setBackground(new java.awt.Color(6, 141, 240));
        jToggleButton3.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton3.setText("Pembuatan Web");
        jToggleButton3.setBorderPainted(false);
        jToggleButton3.setFocusPainted(false);
        jToggleButton3.addActionListener(this::jToggleButton3ActionPerformed);

        jToggleButton4.setBackground(new java.awt.Color(6, 141, 240));
        jToggleButton4.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton4.setText("Pembuatan Web");
        jToggleButton4.setBorderPainted(false);
        jToggleButton4.setFocusPainted(false);
        jToggleButton4.addActionListener(this::jToggleButton4ActionPerformed);

        jToggleButton5.setBackground(new java.awt.Color(204, 204, 204));
        jToggleButton5.setText("Pembuatan Web");
        jToggleButton5.setBorderPainted(false);
        jToggleButton5.setFocusPainted(false);
        jToggleButton5.addActionListener(this::jToggleButton5ActionPerformed);

        jToggleButton6.setBackground(new java.awt.Color(204, 204, 204));
        jToggleButton6.setText("Pembuatan Web");
        jToggleButton6.setBorderPainted(false);
        jToggleButton6.setFocusPainted(false);
        jToggleButton6.addActionListener(this::jToggleButton6ActionPerformed);

        jToggleButton7.setBackground(new java.awt.Color(204, 204, 204));
        jToggleButton7.setText("Pembuatan Web");
        jToggleButton7.setBorderPainted(false);
        jToggleButton7.setFocusPainted(false);
        jToggleButton7.addActionListener(this::jToggleButton7ActionPerformed);

        jTextField1.setText("Tambah Keahlian Lain...");

        tambahButton.setBackground(new java.awt.Color(6, 141, 240));
        tambahButton.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        tambahButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahButton.setText("+");
        tambahButton.addActionListener(this::tambahButtonActionPerformed);

        simpanButton.setBackground(new java.awt.Color(6, 141, 240));
        simpanButton.setForeground(new java.awt.Color(255, 255, 255));
        simpanButton.setText("Simpan Layanan");
        simpanButton.setBorderPainted(false);
        simpanButton.setFocusPainted(false);

        hargaTextField.setText("Harga");
        hargaTextField.addActionListener(this::hargaTextFieldActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tambahButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton4))
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton7))
                    .addComponent(simpanButton))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton2)
                    .addComponent(jToggleButton3)
                    .addComponent(jToggleButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton5)
                    .addComponent(jToggleButton6)
                    .addComponent(jToggleButton7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tambahButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void tambahButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tambahButtonActionPerformed

    private void hargaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new editLayananFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField hargaTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JButton simpanButton;
    private javax.swing.JButton tambahButton;
    // End of variables declaration//GEN-END:variables
}
