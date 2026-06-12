/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package digielancer.component;

/**
 *
 * @author ASUS
 */
public class addTask extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(addTask.class.getName());
    private int projectId;
    private int boardId;
    private int taskIdToEdit = 0;
    private ProjectManagement parentBoard;

    /**
     * Creates new form addTask
     */
    public addTask() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        ((javax.swing.text.AbstractDocument) judulField.getDocument()).setDocumentFilter(new LengthLimitFilter(30));
        ((javax.swing.text.AbstractDocument) deskripsiField.getDocument()).setDocumentFilter(new LengthLimitFilter(80));
        configureDatePicker(null);
    }

    public addTask(java.awt.Frame parent, boolean modal, int projectId, int boardId, ProjectManagement parentBoard) {
        initComponents();
        this.projectId = projectId;
        this.boardId = boardId;
        this.parentBoard = parentBoard;
        
        setAlwaysOnTop(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        ((javax.swing.text.AbstractDocument) judulField.getDocument()).setDocumentFilter(new LengthLimitFilter(30));
        ((javax.swing.text.AbstractDocument) deskripsiField.getDocument()).setDocumentFilter(new LengthLimitFilter(80));
        
        // Populate priorities
        taskDeadline.removeAllItems();
        taskDeadline.addItem("Low");
        taskDeadline.addItem("Medium");
        taskDeadline.addItem("High");
        
        // Populate boards
        loadBoardDropdown(projectId, boardId);
        configureDatePicker(null);
    }

    public addTask(java.awt.Frame parent, boolean modal, int projectId, int boardId, int taskIdToEdit, ProjectManagement parentBoard) {
        initComponents();
        this.projectId = projectId;
        this.boardId = boardId;
        this.taskIdToEdit = taskIdToEdit;
        this.parentBoard = parentBoard;
        
        setAlwaysOnTop(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        ((javax.swing.text.AbstractDocument) judulField.getDocument()).setDocumentFilter(new LengthLimitFilter(30));
        ((javax.swing.text.AbstractDocument) deskripsiField.getDocument()).setDocumentFilter(new LengthLimitFilter(80));
        
        // Populate priorities
        taskDeadline.removeAllItems();
        taskDeadline.addItem("Low");
        taskDeadline.addItem("Medium");
        taskDeadline.addItem("High");
        
        // Populate boards
        loadBoardDropdown(projectId, boardId);
        configureDatePicker(null);
        
        if (taskIdToEdit > 0) {
            jLabel1.setText("Edit Task");
            jButton1.setText("Perbarui Task");
            
            digielancer.model.TaskModel task = digielancer.model.TaskDAO.getTaskById(taskIdToEdit);
            if (task != null) {
                judulField.setText(task.getTitle());
                deskripsiField.setText(task.getDescription());
                if (task.getDeadline() != null && !task.getDeadline().isEmpty() && !task.getDeadline().equals("null")) {
                    try {
                        java.time.LocalDate deadlineDate = java.time.LocalDate.parse(task.getDeadline());
                        configureDatePicker(deadlineDate);
                        boardSelect.setDate(deadlineDate);
                    } catch (Exception ex) {
                        logger.log(java.util.logging.Level.WARNING, "Error parsing task deadline date", ex);
                    }
                }
                
                // Select priority
                taskDeadline.setSelectedItem(task.getPriority());
                
                // Select board dropdown item
                for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                    Object item = jComboBox1.getItemAt(i);
                    if (item instanceof digielancer.model.BoardModel) {
                        digielancer.model.BoardModel b = (digielancer.model.BoardModel) item;
                        if (b.getId() == task.getBoardId()) {
                            jComboBox1.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void configureDatePicker(java.time.LocalDate existingDate) {
        com.github.lgooddatepicker.components.DatePickerSettings settings = boardSelect.getSettings();
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate minDate = today;
        if (existingDate != null && existingDate.isBefore(today)) {
            minDate = existingDate;
        }
        settings.setDateRangeLimits(minDate, null);
    }


    private void loadBoardDropdown(int projectId, int boardId) {
        jComboBox1.removeAllItems();
        java.util.List<digielancer.model.BoardModel> boards = digielancer.model.TaskDAO.getBoardsForProject(projectId);
        
        digielancer.model.BoardModel selectedBoard = null;
        for (digielancer.model.BoardModel board : boards) {
            ((javax.swing.JComboBox) jComboBox1).addItem(board);
            if (board.getId() == boardId) {
                selectedBoard = board;
            }
        }
        
        if (selectedBoard != null) {
            jComboBox1.setSelectedItem(selectedBoard);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        judulField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        deskripsiField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        boardSelect = new com.github.lgooddatepicker.components.DatePicker();
        taskDeadline = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Task Baru");
        jLabel1.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N

        judulField.setBackground(new java.awt.Color(248, 250, 252));
        judulField.addActionListener(this::judulFieldActionPerformed);

        jButton1.setText("Simpan Board");
        jButton1.setBackground(new java.awt.Color(6, 141, 240));
        jButton1.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        deskripsiField.setBackground(new java.awt.Color(248, 250, 252));

        jLabel2.setText("Judul Task");
        jLabel2.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N

        jLabel3.setText("Deskripsi Task");
        jLabel3.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBackground(new java.awt.Color(248, 250, 252));

        jLabel4.setText("Board");
        jLabel4.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N

        jLabel5.setText("Task Deadline");
        jLabel5.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N

        boardSelect.setBackground(new java.awt.Color(248, 250, 252));

        taskDeadline.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        taskDeadline.setBackground(new java.awt.Color(248, 250, 252));

        jLabel6.setText("Prioritas");
        jLabel6.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(taskDeadline, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(241, 241, 241))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(judulField, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3)
                                    .addComponent(deskripsiField)
                                    .addComponent(boardSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))))
                        .addGap(17, 17, 17))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(judulField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deskripsiField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boardSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(3, 3, 3)
                .addComponent(taskDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void judulFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_judulFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_judulFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String title = judulField.getText().trim();
        String desc = deskripsiField.getText().trim();
        java.time.LocalDate deadlineDate = boardSelect.getDate(); // boardSelect is the DatePicker component
        Object selectedBoardItem = jComboBox1.getSelectedItem(); // jComboBox1 is the Board dropdown
        String priority = (String) taskDeadline.getSelectedItem(); // taskDeadline is the Priority dropdown

        if (title.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Judul task tidak boleh kosong!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (deadlineDate == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Harap pilih deadline task!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!(selectedBoardItem instanceof digielancer.model.BoardModel)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Harap pilih board yang valid!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        digielancer.model.BoardModel board = (digielancer.model.BoardModel) selectedBoardItem;
        String deadlineStr = deadlineDate.toString(); // ISO-8601 (yyyy-MM-dd)

        boolean success;
        if (taskIdToEdit > 0) {
            success = digielancer.model.TaskDAO.editTask(taskIdToEdit, board.getId(), title, desc, deadlineStr, priority);
        } else {
            success = digielancer.model.TaskDAO.addTask(board.getId(), title, desc, deadlineStr, priority);
        }
        if (success) {
            javax.swing.JOptionPane.showMessageDialog(this, taskIdToEdit > 0 ? "Task berhasil diperbarui!" : "Task baru berhasil ditambahkan!");
            if (parentBoard != null) {
                parentBoard.loadBoard(projectId); // Reload parent columns
            }
            this.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal menyimpan task ke database.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new addTask().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.github.lgooddatepicker.components.DatePicker boardSelect;
    private javax.swing.JTextField deskripsiField;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField judulField;
    private javax.swing.JComboBox<String> taskDeadline;
    // End of variables declaration//GEN-END:variables

    private static class LengthLimitFilter extends javax.swing.text.DocumentFilter {
        private final int limit;
        public LengthLimitFilter(int limit) {
            this.limit = limit;
        }
        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
            if (string == null) return;
            if ((fb.getDocument().getLength() + string.length()) <= limit) {
                super.insertString(fb, offset, string, attr);
            } else {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
            if (text == null) return;
            if ((fb.getDocument().getLength() + text.length() - length) <= limit) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
