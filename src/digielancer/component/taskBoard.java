/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digielancer.component;

/**
 *
 * @author ASUS
 */
public class taskBoard extends javax.swing.JPanel {

    private int boardId;
    private ProjectManagement parentBoard;

    /**
     * Creates new form taskBoard
     */
    public taskBoard() {
        initComponents();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        todoContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public taskBoard(final digielancer.model.BoardModel board, final ProjectManagement parentBoard) {
        initComponents();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        
        this.boardId = board.getId();
        this.parentBoard = parentBoard;
        
        // Add task click listener on addIcon (jLabel3)
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(taskBoard.this);
                java.awt.Frame parentFrame = (java.awt.Frame) window;
                int projectId = parentBoard.getCurrentProjectId();
                
                addTask dialog = new addTask(parentFrame, true, projectId, boardId, parentBoard);
                dialog.setLocationRelativeTo(parentFrame);
                dialog.setVisible(true);
            }
        });

        // Add edit click listener on editIcon (jLabel1)
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(taskBoard.this);
                java.awt.Frame parentFrame = (java.awt.Frame) window;
                int projectId = parentBoard.getCurrentProjectId();
                
                addBoard dialog = new addBoard(parentFrame, true, projectId, boardId, parentBoard);
                dialog.setLocationRelativeTo(parentFrame);
                dialog.setVisible(true);
            }
        });

        // Add delete click listener on closeIcon (jLabel2)
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    taskBoard.this,
                    "Apakah Anda yakin ingin menghapus board \"" + board.getBoardName() + "\" beserta seluruh task di dalamnya?",
                    "Konfirmasi Hapus Board",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    boolean success = digielancer.model.TaskDAO.deleteBoard(boardId);
                    if (success) {
                        javax.swing.JOptionPane.showMessageDialog(taskBoard.this, "Board berhasil dihapus!");
                        parentBoard.loadBoard(parentBoard.getCurrentProjectId());
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(taskBoard.this, "Gagal menghapus board.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        labelTitle.setText(board.getBoardName());
        labelDesc.setText(board.getDescription());
        
        // Done styling: light green background #ECFDF5 and emerald green border #10B981, hide close icon
        if (board.getBoardName().equalsIgnoreCase("Done") || board.isCompletionBoard()) {
            this.setBackground(new java.awt.Color(236, 253, 245)); // ECFDF5
            todoContentPanel.setBackground(new java.awt.Color(236, 253, 245)); // ECFDF5
            this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(16, 185, 129), 1)); // 10B981
            jLabel1.setVisible(false); // Hide the edit icon
            jLabel2.setVisible(false); // Hide the remove icon
        } else {
            this.setBackground(new java.awt.Color(255, 255, 255));
            todoContentPanel.setBackground(new java.awt.Color(248, 250, 252));
            this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240), 1));
            jLabel1.setVisible(true);
            jLabel2.setVisible(true);
        }
        
        todoContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Lock dimensions
        this.setMinimumSize(new java.awt.Dimension(280, 300));
        this.setMaximumSize(new java.awt.Dimension(280, Integer.MAX_VALUE));
        this.setPreferredSize(new java.awt.Dimension(280, 300));
    }

    public javax.swing.JPanel getContentPanel() {
        return todoContentPanel;
    }

    public int getBoardId() {
        return boardId;
    }

    public void clearBoard() {
        todoContentPanel.removeAll();
        this.setMinimumSize(new java.awt.Dimension(280, 300));
        this.setMaximumSize(new java.awt.Dimension(280, Integer.MAX_VALUE));
        this.setPreferredSize(new java.awt.Dimension(280, 300));
        todoContentPanel.revalidate();
        todoContentPanel.repaint();
        this.revalidate();
        this.repaint();
    }

    public void addTaskCard(taskCard card) {
        todoContentPanel.add(card);
        todoContentPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 10)));
        
        int cardWidth = card.getPreferredSize().width;
        int currentWidth = this.getPreferredSize().width;
        int newWidth = Math.max(currentWidth, cardWidth + 66);
        
        this.setMinimumSize(new java.awt.Dimension(newWidth, 300));
        this.setMaximumSize(new java.awt.Dimension(newWidth, Integer.MAX_VALUE));
        this.setPreferredSize(new java.awt.Dimension(newWidth, 300));
        
        todoContentPanel.revalidate();
        todoContentPanel.repaint();
        this.revalidate();
        this.repaint();
    }

    public void addEmptyState(java.awt.Component emptyState) {
        todoContentPanel.add(emptyState);
        todoContentPanel.revalidate();
        todoContentPanel.repaint();
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

        labelTitle = new javax.swing.JLabel();
        labelDesc = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        todoContentPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        labelTitle.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        labelTitle.setText("To-Do");

        labelDesc.setFont(new java.awt.Font("Inter", 0, 10)); // NOI18N
        labelDesc.setText("Tugas yang belum dikerjakan");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBar(null);

        todoContentPanel.setBackground(new java.awt.Color(248, 250, 252));
        todoContentPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        todoContentPanel.setLayout(new javax.swing.BoxLayout(todoContentPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(todoContentPanel);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/editIcon.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/closeIcon.png"))); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/addIcon.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelDesc)
                        .addGap(0, 124, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addComponent(labelTitle))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelDesc)
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JPanel todoContentPanel;
    // End of variables declaration//GEN-END:variables
}
