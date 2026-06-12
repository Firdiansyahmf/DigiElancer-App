/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digielancer.component;

import digielancer.main.UserSession;
import digielancer.model.ProjectModel;
import digielancer.model.TaskDAO;
import digielancer.model.TaskModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ASUS
 */
public class ProjectManagement extends javax.swing.JPanel {
    
    private int currentProjectId;
    private digielancer.main.Navbar mainApp;
    private javax.swing.JLabel labelProgress;
    private java.util.List<digielancer.model.BoardModel> allBoards;
    private java.util.List<digielancer.model.TaskModel> allTasks;

    /**
     * Creates new form ProjectList
     */
    public ProjectManagement(digielancer.main.Navbar mainApp, int projectId) {
        this.mainApp = mainApp;
        initComponents();

        // Safely recreate boardContent to override getPreferredSize without breaking NetBeans form
        boardContent = new javax.swing.JPanel() {
            @Override
            public java.awt.Dimension getPreferredSize() {
                java.awt.Dimension d = super.getPreferredSize();
                java.awt.Container parent = getParent();
                if (parent instanceof javax.swing.JViewport) {
                    return new java.awt.Dimension(d.width, Math.max(d.height, parent.getHeight()));
                }
                return d;
            }
        };
        boardContent.setBackground(new java.awt.Color(248, 250, 252));
        boardContent.setLayout(new javax.swing.BoxLayout(boardContent, javax.swing.BoxLayout.X_AXIS));
        boardScrollPane.setViewportView(boardContent);
        
        // Enable horizontal scrolling and set smooth wheel scrolling unit increments
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane4.getHorizontalScrollBar().setUnitIncrement(16);
        
        boardScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        boardScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        // Initialize labelProgress and layout it programmatically inside jPanel4
        labelProgress = new javax.swing.JLabel();
        labelProgress.setFont(new java.awt.Font("Inter", 0, 14));
        labelProgress.setForeground(new java.awt.Color(100, 110, 120));
        labelProgress.setText("Progres: 0%");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(projectLabel)
                    .addComponent(serviceLabel)
                    .addComponent(labelProgress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 356, Short.MAX_VALUE)
                .addComponent(buttonAddBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(projectLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serviceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelProgress))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonAddBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        
        this.currentProjectId = projectId;
        
        // Add action listener to buttonAddBoard programmatically
        buttonAddBoard.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddBoardActionPerformed(evt);
            }
        });

        // Populate priority filter dropdown
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Semua Priority");
        jComboBox1.addItem("Low");
        jComboBox1.addItem("Medium");
        jComboBox1.addItem("High");

        // Add action listener to jComboBox1 to trigger filter
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterAndDisplayTasks();
            }
        });
        
        // Setup searchTask placeholder focus behavior
        searchTask.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchTask.getText().equals("Cari Task...")) {
                    searchTask.setText("");
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchTask.getText().trim().isEmpty()) {
                    searchTask.setText("Cari Task...");
                }
            }
        });

        // Add document listener to searchTask to filter instantly on typing!
        searchTask.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterAndDisplayTasks();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterAndDisplayTasks();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterAndDisplayTasks();
            }
        });
        
        // Load the board immediately when this screen is opened
        loadBoard(currentProjectId);
        
        // Load the horizontal projects list immediately
        loadHorizontalProjects();
    }

    public int getCurrentProjectId() {
        return currentProjectId;
    }

    private void buttonAddBoardActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        java.awt.Frame parentFrame = (java.awt.Frame) window;
        
        boolean isModal = true;
        addBoard dialog = new addBoard(parentFrame, isModal, currentProjectId, this);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private JPanel createEmptyState() {
        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setOpaque(false); 
        
        JLabel lblEmpty = new JLabel("Belum ada task");
        lblEmpty.setForeground(new Color(170, 180, 190)); 
        lblEmpty.setFont(new Font("Inter", Font.PLAIN, 14)); 
        
        emptyPanel.add(lblEmpty);
        return emptyPanel;
    }
    
    public void loadBoard(int projectId) {
        // 0. Update labels to match selected project
        if (projectId > 0) {
            digielancer.model.ProjectModel project = digielancer.model.ProjectDAO.getProjectById(projectId);
            if (project != null) {
                projectLabel.setText("Project: " + project.getClientName());
                digielancer.model.ServiceModel service = digielancer.model.ServiceDAO.getServiceById(project.getMainServiceId());
                if (service != null) {
                    serviceLabel.setText(service.getServiceName());
                } else {
                    serviceLabel.setText("-");
                }
            } else {
                projectLabel.setText("Pilih Project");
                serviceLabel.setText("Pilih project dari daftar di atas");
            }
        } else {
            projectLabel.setText("Pilih Project");
            serviceLabel.setText("Pilih project dari daftar di atas");
        }

        // 1. Fetch from DB
        this.allBoards = TaskDAO.getBoardsForProject(projectId);
        this.allTasks = TaskDAO.getTasksForProject(projectId);

        // 2. Calculate and update project progress & completion status
        updateProgressLabel(projectId);

        // 3. Filter and render the columns/cards based on search queries
        filterAndDisplayTasks();
    }

    private void updateProgressLabel(int projectId) {
        if (allTasks == null || allBoards == null) return;
        
        int totalTasks = allTasks.size();
        int doneTasks = 0;
        
        java.util.Set<Integer> doneBoardIds = new java.util.HashSet<>();
        for (digielancer.model.BoardModel b : allBoards) {
            if (b.getBoardName().equalsIgnoreCase("Done") || b.isCompletionBoard()) {
                doneBoardIds.add(b.getId());
            }
        }
        
        for (TaskModel task : allTasks) {
            if (doneBoardIds.contains(task.getBoardId())) {
                doneTasks++;
            }
        }
        
        int percent = 0;
        if (totalTasks > 0) {
            percent = (doneTasks * 100) / totalTasks;
        }
        
        if (projectId > 0) {
            if (totalTasks == 0) {
                labelProgress.setText("Progres: 0%");
            } else {
                if (percent == 100) {
                    labelProgress.setText("Progres: Selesai (100%)");
                    digielancer.model.ProjectModel project = digielancer.model.ProjectDAO.getProjectById(projectId);
                    if (project != null && project.isActive()) {
                        boolean updated = digielancer.model.ProjectDAO.setProjectActive(projectId, false);
                        if (updated) {
                            loadHorizontalProjects();
                        }
                    }
                } else {
                    labelProgress.setText("Progres: " + percent + "%");
                    digielancer.model.ProjectModel project = digielancer.model.ProjectDAO.getProjectById(projectId);
                    if (project != null && !project.isActive()) {
                        boolean updated = digielancer.model.ProjectDAO.setProjectActive(projectId, true);
                        if (updated) {
                            loadHorizontalProjects();
                        }
                    }
                }
            }
            labelProgress.setVisible(true);
        } else {
            labelProgress.setVisible(false);
        }
    }

    private void filterAndDisplayTasks() {
        if (allBoards == null) return;

        // 1. Wipe the container clean
        boardContent.removeAll();
        
        // 2. Read query filters
        String query = searchTask.getText().trim().toLowerCase();
        if (query.equals("cari task...")) {
            query = "";
        }
        
        String priorityFilter = (String) jComboBox1.getSelectedItem();
        if (priorityFilter == null) {
            priorityFilter = "Semua Priority";
        }
        
        // 3. Create board columns
        for (digielancer.model.BoardModel board : allBoards) {
            taskBoard column = new taskBoard(board, this);
            column.clearBoard();
            
            boolean hasTasks = false;
            if (allTasks != null) {
                for (TaskModel task : allTasks) {
                    if (task.getBoardId() == board.getId()) {
                        // Check query criteria
                        boolean matchesQuery = query.isEmpty() 
                            || task.getTitle().toLowerCase().contains(query) 
                            || (task.getDescription() != null && task.getDescription().toLowerCase().contains(query));
                        
                        // Check priority criteria
                        boolean matchesPriority = priorityFilter.equals("Semua Priority") 
                            || task.getPriority().equalsIgnoreCase(priorityFilter);
                        
                        if (matchesQuery && matchesPriority) {
                            taskCard newCard = new taskCard(task, this);
                            column.addTaskCard(newCard);
                            hasTasks = true;
                        }
                    }
                }
            }
            
            if (!hasTasks) {
                column.addEmptyState(createEmptyState());
            }
            
            boardContent.add(column);
            boardContent.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(18, 0)));
        }
        
        boardContent.revalidate();
        boardContent.repaint();
    }
    
    public void loadHorizontalProjects() {
        // 1. Wipe the container clean
        horizontalProjectContainer.removeAll();
        
        // 2. Get the currently logged-in User ID
        // (If nobody is logged in yet, default to 1 so you can keep testing)
        int currentUserId = digielancer.main.UserSession.getId();
        if (currentUserId <= 0) {
            currentUserId = 1; 
        }
        
        // 3. Fetch ONLY this user's projects
        java.util.List<ProjectModel> projects = digielancer.model.ProjectDAO.getProjectsForUser(currentUserId);
        
        // 4. Loop and add to the container
        for (ProjectModel project : projects) {
            ProjectPanel newCard = new ProjectPanel(project, mainApp, currentProjectId, this);
            horizontalProjectContainer.add(newCard);
            horizontalProjectContainer.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(15, 0)));
        }
        
        // 5. Force UI to refresh
        horizontalProjectContainer.revalidate();
        horizontalProjectContainer.repaint();
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
        jPanel4 = new javax.swing.JPanel();
        projectLabel = new javax.swing.JLabel();
        serviceLabel = new javax.swing.JLabel();
        buttonAddBoard = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        searchTask = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        boardScrollPane = new javax.swing.JScrollPane();
        boardContent = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        horizontalProjectContainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(248, 250, 252));

        jLabel1.setBackground(new java.awt.Color(15, 23, 42));
        jLabel1.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel1.setText("Manajemen Project");

        jLabel2.setBackground(new java.awt.Color(74, 85, 101));
        jLabel2.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(74, 85, 101));
        jLabel2.setText("Kelola project dan tracking pengerjaan dengan drag & drop");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        projectLabel.setFont(new java.awt.Font("Inter", 1, 20)); // NOI18N
        projectLabel.setText("Project: CMotion");

        serviceLabel.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        serviceLabel.setText("Web Development");

        buttonAddBoard.setBackground(new java.awt.Color(6, 141, 240));
        buttonAddBoard.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        buttonAddBoard.setForeground(new java.awt.Color(255, 255, 255));
        buttonAddBoard.setText("+ Tambah Board");

        jButton3.setBackground(new java.awt.Color(16, 185, 129));
        jButton3.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Generate Nota");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(serviceLabel)
                    .addComponent(projectLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 356, Short.MAX_VALUE)
                .addComponent(buttonAddBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(projectLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serviceLabel))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonAddBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(6, 141, 240));
        jButton1.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("+ Tambah Project");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        searchTask.setBackground(new java.awt.Color(248, 250, 252));
        searchTask.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        searchTask.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        searchTask.setToolTipText("");
        searchTask.addActionListener(this::searchTaskActionPerformed);

        jComboBox1.setFont(new java.awt.Font("Inter", 0, 15)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Priority" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        boardScrollPane.setBackground(new java.awt.Color(255, 153, 153));
        boardScrollPane.setBorder(null);
        boardScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        boardContent.setBackground(new java.awt.Color(255, 255, 255));
        boardContent.setLayout(new javax.swing.BoxLayout(boardContent, javax.swing.BoxLayout.X_AXIS));
        boardScrollPane.setViewportView(boardContent);

        jScrollPane4.setBorder(null);
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        horizontalProjectContainer.setBackground(new java.awt.Color(248, 250, 252));
        horizontalProjectContainer.setLayout(new javax.swing.BoxLayout(horizontalProjectContainer, javax.swing.BoxLayout.X_AXIS));
        jScrollPane4.setViewportView(horizontalProjectContainer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(searchTask)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(boardScrollPane)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1)
                    .addComponent(searchTask, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(boardScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (mainApp != null) {
            mainApp.navigateToInvoices();
        } else {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof digielancer.main.Navbar) {
                ((digielancer.main.Navbar) window).navigateToInvoices();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void searchTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTaskActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        java.awt.Frame parentFrame = (java.awt.Frame) window;
        
        boolean isModal = true;
        int userId = UserSession.getId(); 
            
        addProject dialog = new addProject(parentFrame, isModal, userId, 0, this);
        
        dialog.setLocationRelativeTo(parentFrame);
        
        dialog.setVisible(true);
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel boardContent;
    private javax.swing.JScrollPane boardScrollPane;
    private javax.swing.JButton buttonAddBoard;
    private javax.swing.JPanel horizontalProjectContainer;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel projectLabel;
    private javax.swing.JTextField searchTask;
    private javax.swing.JLabel serviceLabel;
    // End of variables declaration//GEN-END:variables
}
