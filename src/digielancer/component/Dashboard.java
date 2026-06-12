/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digielancer.component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import digielancer.model.TaskModel;
import digielancer.model.TaskDAO;
import digielancer.model.ProjectDAO;
import digielancer.main.UserSession;

/**
 *
 * @author ASUS
 */
public class Dashboard extends javax.swing.JPanel {

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        
        // Re-create deadlineReminderContent dynamically to override the default panel.
        // This custom panel overrides getPreferredSize to ensure it stretches horizontally
        // to at least the width of its parent (the scrollpane's viewport), making child elements fill the width.
        deadlineReminderContent = new javax.swing.JPanel() {
            @Override
            public java.awt.Dimension getPreferredSize() {
                java.awt.Dimension pref = super.getPreferredSize();
                java.awt.Container parent = getParent();
                if (parent != null && parent.getWidth() > pref.width) {
                    pref.width = parent.getWidth();
                }
                return pref;
            }
        };
        deadlineReminderContent.setLayout(new javax.swing.BoxLayout(deadlineReminderContent, javax.swing.BoxLayout.Y_AXIS));
        deadlineReminderContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jScrollPane1.setViewportView(deadlineReminderContent);

        // Re-create newestProjectContent dynamically
        newestProjectContent = new javax.swing.JPanel() {
            @Override
            public java.awt.Dimension getPreferredSize() {
                java.awt.Dimension pref = super.getPreferredSize();
                java.awt.Container parent = getParent();
                if (parent != null && parent.getWidth() > pref.width) {
                    pref.width = parent.getWidth();
                }
                return pref;
            }
        };
        newestProjectContent.setLayout(new javax.swing.BoxLayout(newestProjectContent, javax.swing.BoxLayout.Y_AXIS));
        newestProjectContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jScrollPane2.setViewportView(newestProjectContent);

        // Re-create newestInvoiceContent dynamically
        newestInvoiceContent = new javax.swing.JPanel() {
            @Override
            public java.awt.Dimension getPreferredSize() {
                java.awt.Dimension pref = super.getPreferredSize();
                java.awt.Container parent = getParent();
                if (parent != null && parent.getWidth() > pref.width) {
                    pref.width = parent.getWidth();
                }
                return pref;
            }
        };
        newestInvoiceContent.setLayout(new javax.swing.BoxLayout(newestInvoiceContent, javax.swing.BoxLayout.Y_AXIS));
        newestInvoiceContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jScrollPane3.setViewportView(newestInvoiceContent);
        
        loadUpcomingTaskReminders();
        loadNewestProjects();
        loadNewestInvoices();
        loadDashboardStats();
    }

    public void loadUpcomingTaskReminders() {
        deadlineReminderContent.removeAll();
        
        int userId = UserSession.getId();
        List<TaskModel> tasks = TaskDAO.getUpcomingTasksForUser(userId);
        
        LocalDate today = LocalDate.now();
        boolean hasReminders = false;
        
        if (tasks != null) {
            for (TaskModel task : tasks) {
                if (task.getDeadline() != null && !task.getDeadline().isEmpty() && !task.getDeadline().equals("null")) {
                    try {
                        LocalDate deadlineDate = LocalDate.parse(task.getDeadline());
                        long daysLeft = ChronoUnit.DAYS.between(today, deadlineDate);
                        
                        // Show warning for task if it is due in 5 days or fewer
                        if (daysLeft <= 5 && daysLeft >= 0) {
                            if (hasReminders) {
                                deadlineReminderContent.add(javax.swing.Box.createVerticalStrut(10));
                            }
                            deadlineReminder reminderPanel = new deadlineReminder(task, daysLeft);
                            // Set maximum size to stretch completely to the side
                            reminderPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, reminderPanel.getPreferredSize().height));
                            deadlineReminderContent.add(reminderPanel);
                            hasReminders = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        if (!hasReminders) {
            javax.swing.JLabel noReminderLabel = new javax.swing.JLabel("Tidak ada deadline dalam waktu dekat.");
            noReminderLabel.setFont(new java.awt.Font("Inter", 0, 16));
            noReminderLabel.setForeground(new java.awt.Color(106, 114, 130));
            noReminderLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
            deadlineReminderContent.add(noReminderLabel);
        }
        
        deadlineReminderContent.revalidate();
        deadlineReminderContent.repaint();
    }

    public void loadNewestProjects() {
        newestProjectContent.removeAll();
        int userId = UserSession.getId();
        List<ProjectDAO.NewestProjectDTO> projects = ProjectDAO.getNewestProjectsForUser(userId, 5);
        boolean hasProjects = false;
        
        if (projects != null && !projects.isEmpty()) {
            for (ProjectDAO.NewestProjectDTO proj : projects) {
                if (hasProjects) {
                    newestProjectContent.add(javax.swing.Box.createVerticalStrut(10));
                }
                newestProject projPanel = new newestProject(proj);
                projPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, projPanel.getPreferredSize().height));
                newestProjectContent.add(projPanel);
                hasProjects = true;
            }
        }
        
        if (!hasProjects) {
            javax.swing.JLabel noProjLabel = new javax.swing.JLabel("Tidak ada project.");
            noProjLabel.setFont(new java.awt.Font("Inter", 0, 14));
            noProjLabel.setForeground(new java.awt.Color(106, 114, 130));
            noProjLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
            newestProjectContent.add(noProjLabel);
        }
        
        newestProjectContent.revalidate();
        newestProjectContent.repaint();
    }

    public void loadNewestInvoices() {
        newestInvoiceContent.removeAll();
        int userId = UserSession.getId();
        List<ProjectDAO.NewestInvoiceDTO> invoices = ProjectDAO.getNewestInvoicesForUser(userId, 5);
        boolean hasInvoices = false;
        
        if (invoices != null && !invoices.isEmpty()) {
            for (ProjectDAO.NewestInvoiceDTO inv : invoices) {
                if (hasInvoices) {
                    newestInvoiceContent.add(javax.swing.Box.createVerticalStrut(10));
                }
                newestInvoice invPanel = new newestInvoice(inv);
                invPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, invPanel.getPreferredSize().height));
                newestInvoiceContent.add(invPanel);
                hasInvoices = true;
            }
        }
        
        if (!hasInvoices) {
            javax.swing.JLabel noInvLabel = new javax.swing.JLabel("Tidak ada invoice.");
            noInvLabel.setFont(new java.awt.Font("Inter", 0, 14));
            noInvLabel.setForeground(new java.awt.Color(106, 114, 130));
            noInvLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
            newestInvoiceContent.add(noInvLabel);
        }
        
        newestInvoiceContent.revalidate();
        newestInvoiceContent.repaint();
    }

    public void loadDashboardStats() {
        int userId = UserSession.getId();
        ProjectDAO.DashboardStatsDTO stats = ProjectDAO.getDashboardStats(userId);
        
        // 1. Project Aktif
        jLabel3.setText(String.valueOf(stats.getActiveProjectsCount()));
        jLabel27.setText(stats.getCompletedProjectsThisMonthCount() + " Selesai Bulan Ini");
        
        // 2. Pendapatan
        activeProjectLabel.setText(formatRevenue(stats.getPaidRevenue()));
        jLabel28.setText(formatRevenue(stats.getPendingRevenue()) + " Pending");
        
        // 3. Deadline Hari Ini
        revenueLabel.setText(String.valueOf(stats.getDeadlineTodayCount()));
        jLabel29.setText(stats.getDeadlineTomorrowCount() + " Besok");
        
        // 4. Total Invoice
        todayDeadline.setText(String.valueOf(stats.getTotalInvoicesCount()));
        jLabel30.setText(stats.getPendingInvoicesCount() + " Pending");
    }
    
    private String formatRevenue(double amount) {
        if (amount >= 1000000.0) {
            return String.format(java.util.Locale.US, "Rp %.1fjt", amount / 1000000.0);
        } else if (amount >= 1000.0) {
            return String.format(java.util.Locale.US, "Rp %.1frb", amount / 1000.0);
        } else {
            return "Rp " + (int)amount;
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
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        activeProjectLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        revenueLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        todayDeadline = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        deadlineReminderContent = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        newestProjectContent = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        newestInvoiceContent = new javax.swing.JPanel();

        setBackground(new java.awt.Color(248, 250, 252));

        jLabel1.setBackground(new java.awt.Color(15, 23, 42));
        jLabel1.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel1.setText("Dashboard");

        jLabel2.setBackground(new java.awt.Color(74, 85, 101));
        jLabel2.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(74, 85, 101));
        jLabel2.setText("Selamat datang kembali! Berikut ringkasan aktivitas Anda");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        jLabel3.setFont(new java.awt.Font("Inter", 1, 32)); // NOI18N
        jLabel3.setText("2");

        jLabel4.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        jLabel4.setText("Project Aktif");

        jPanel12.setBackground(new java.awt.Color(219, 234, 254));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/folderIcon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel27.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(106, 114, 130));
        jLabel27.setText("1 Selesai Bulan Ini");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addGap(20, 20, 20))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        activeProjectLabel.setFont(new java.awt.Font("Inter", 1, 32)); // NOI18N
        activeProjectLabel.setText("Rp 5.0jt");

        jLabel6.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        jLabel6.setText("Pendapatan");

        jPanel13.setBackground(new java.awt.Color(220, 252, 231));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/dolarIcon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel28.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(106, 114, 130));
        jLabel28.setText("Rp 16.0jt Pending");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel6)
                    .addComponent(activeProjectLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(activeProjectLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addGap(20, 20, 20))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        revenueLabel.setFont(new java.awt.Font("Inter", 1, 32)); // NOI18N
        revenueLabel.setText("1");

        jLabel8.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        jLabel8.setText("Deadline Hari Ini");

        jPanel14.setBackground(new java.awt.Color(254, 243, 199));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/clockIcon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel29.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(106, 114, 130));
        jLabel29.setText("1 Besok");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel8)
                    .addComponent(revenueLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(revenueLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addGap(20, 20, 20))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        todayDeadline.setFont(new java.awt.Font("Inter", 1, 32)); // NOI18N
        todayDeadline.setText("3");

        jLabel10.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        jLabel10.setText("Total Invoice");

        jPanel15.setBackground(new java.awt.Color(243, 232, 255));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digielancer/assets/docIcon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel30.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(106, 114, 130));
        jLabel30.setText("2 Pending");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel10)
                    .addComponent(todayDeadline))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todayDeadline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addGap(20, 20, 20))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        jLabel11.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel11.setText("Deadline Terdekat");

        deadlineReminderContent.setBackground(new java.awt.Color(255, 255, 255));
        deadlineReminderContent.setLayout(new javax.swing.BoxLayout(deadlineReminderContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(deadlineReminderContent);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        jLabel18.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel18.setText("Project Terbaru");

        newestProjectContent.setBackground(new java.awt.Color(255, 255, 255));
        newestProjectContent.setLayout(new javax.swing.BoxLayout(newestProjectContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(newestProjectContent);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        jLabel33.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel33.setText("Invoice Terbaru");

        newestInvoiceContent.setBackground(new java.awt.Color(255, 255, 255));
        newestInvoiceContent.setLayout(new javax.swing.BoxLayout(newestInvoiceContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane3.setViewportView(newestInvoiceContent);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activeProjectLabel;
    private javax.swing.JPanel deadlineReminderContent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel newestInvoiceContent;
    private javax.swing.JPanel newestProjectContent;
    private javax.swing.JLabel revenueLabel;
    private javax.swing.JLabel todayDeadline;
    // End of variables declaration//GEN-END:variables
}
