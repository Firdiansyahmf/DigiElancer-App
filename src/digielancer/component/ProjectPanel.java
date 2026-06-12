/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digielancer.component;

import digielancer.main.Navbar;
import digielancer.model.ProjectModel;

/**
 *
 * @author ASUS
 */
public class ProjectPanel extends javax.swing.JPanel {

    private int projectId;
    private ProjectManagement parentPanel;
    /**
     * Creates new form ProjectPanel
     */
    public ProjectPanel(ProjectModel project, final Navbar mainApp, int selectedProjectId, ProjectManagement parentPanel) {
        initComponents();
        
        this.projectId = project.getId();
        this.parentPanel = parentPanel;
        
        // 1. Set your visual labels (ensure you have these named in your designer)
        labelKlien.setText(project.getClientName());
        labelDeadline.setText("Deadline: " + project.getDeadline());
        
        // Check active status
        if (project.isActive()) {
            activeLabel.setText("Aktif");
            activeLabel.setForeground(new java.awt.Color(30, 64, 175)); // Blue
            activeContainer.setBackground(new java.awt.Color(219, 234, 254)); // Light Blue
        } else {
            activeLabel.setText("Selesai");
            activeLabel.setForeground(new java.awt.Color(22,101,60)); // Green
            activeContainer.setBackground(new java.awt.Color(220,252,231)); // Light Green
        }
        
        // Set border highlight based on selection
        if (project.getId() == selectedProjectId) {
            this.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(6, 141, 240), 2, true)); // #068DF0
        } else {
            this.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 232, 240), 1, true)); // Subtle border
        }
        
        // 2. Lock the size so they don't stretch weirdly in the horizontal list
        this.setMinimumSize(new java.awt.Dimension(250, 105));
        this.setMaximumSize(new java.awt.Dimension(250, 105));
        this.setPreferredSize(new java.awt.Dimension(250, 105));
        
        // 3. Make the edit and delete icons clickable
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(ProjectPanel.this);
                java.awt.Frame parentFrame = (java.awt.Frame) window;
                
                int currentUserId = digielancer.main.UserSession.getId();
                addProject dialog = new addProject(parentFrame, true, currentUserId, projectId, parentPanel);
                dialog.setLocationRelativeTo(parentFrame);
                dialog.setVisible(true);
            }
        });

        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    ProjectPanel.this,
                    "Apakah Anda yakin ingin menghapus project ini beserta seluruh board dan task di dalamnya?",
                    "Konfirmasi Hapus Project",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    boolean success = digielancer.model.ProjectDAO.deleteProject(projectId);
                    if (success) {
                        javax.swing.JOptionPane.showMessageDialog(ProjectPanel.this, "Project berhasil dihapus!");
                        mainApp.switchContent(new ProjectManagement(mainApp, -1)); 
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(ProjectPanel.this, "Gagal menghapus project.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // 4. Make the entire card clickable!
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // When clicked, tell the Main Dashboard to load THIS project's board!
                mainApp.switchContent(new ProjectManagement(mainApp, projectId));
            }
            
            // Optional UX Polish: Change cursor to hand when hovering
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelKlien = new javax.swing.JLabel();
        labelService = new javax.swing.JLabel();
        activeContainer = new javax.swing.JPanel();
        activeLabel = new javax.swing.JLabel();
        labelDeadline = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelKlien.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        labelKlien.setText("CMotion");

        labelService.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        labelService.setForeground(new java.awt.Color(74, 85, 101));
        labelService.setText("Web Development");

        activeContainer.setBackground(new java.awt.Color(220, 252, 231));

        activeLabel.setBackground(new java.awt.Color(255, 255, 255));
        activeLabel.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        activeLabel.setForeground(new java.awt.Color(22, 101, 60));
        activeLabel.setText("Aktif");

        javax.swing.GroupLayout activeContainerLayout = new javax.swing.GroupLayout(activeContainer);
        activeContainer.setLayout(activeContainerLayout);
        activeContainerLayout.setHorizontalGroup(
            activeContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, activeContainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(activeLabel)
                .addContainerGap())
        );
        activeContainerLayout.setVerticalGroup(
            activeContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, activeContainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(activeLabel)
                .addContainerGap())
        );

        labelDeadline.setBackground(new java.awt.Color(106, 114, 130));
        labelDeadline.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        labelDeadline.setForeground(new java.awt.Color(106, 114, 130));
        labelDeadline.setText("2026-06-08");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/editIcon.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/closeIcon.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelService)
                    .addComponent(labelKlien)
                    .addComponent(labelDeadline))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(activeContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(40, 40, 40)
                        .addComponent(activeContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelKlien)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelService)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelDeadline)))
                .addGap(14, 14, 14))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activeContainer;
    private javax.swing.JLabel activeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel labelDeadline;
    private javax.swing.JLabel labelKlien;
    private javax.swing.JLabel labelService;
    // End of variables declaration//GEN-END:variables
}
