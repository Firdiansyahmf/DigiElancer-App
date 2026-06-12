/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package digielancer.model;

import digielancer.main.KoneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ASUS
 */
public class ProjectDAO {
    public static java.util.List<ProjectModel> getProjectsForUser(int userId) {
        java.util.List<ProjectModel> list = new java.util.ArrayList<>();
        
        // Added the WHERE clause!
        String query = "SELECT id, client_name, deadline, is_active FROM PROJECT WHERE user_id = ?"; 
        
        try (java.sql.Connection conn = KoneksiDB.configDB();
             java.sql.PreparedStatement pst = conn.prepareStatement(query)) {
             
            // Inject the logged-in user's ID into the query
            pst.setInt(1, userId); 
            java.sql.ResultSet rs = pst.executeQuery();
             
            while (rs.next()) {
                list.add(new ProjectModel(
                    rs.getInt("id"),
                    rs.getString("client_name"),
                    rs.getDate("deadline").toString(),
                    rs.getBoolean("is_active")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error fetching user's projects!");
            e.printStackTrace();
        }
        return list;
    }
    
    public static void loadProjects(DefaultTableModel model) {
        
        // Clear any existing rows to prevent duplicates when reloading
        model.setRowCount(0); 
        
        // Write the exact SQL query
        // Ensure these columns match exactly what you want to show in your UI Table
        String query = "SELECT client_name, client_contact, deadline FROM PROJECT";
        
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
             
            while (rs.next()) {
                // Package the row data into an Object array
                Object[] row = {
                    rs.getString("client_name"),
                    rs.getString("client_contact"),
                    rs.getDate("deadline")
                };
                
                // Add the row to the virtual table model
                model.addRow(row);
            }
            
        } catch (Exception e) {
            System.out.println("Error loading projects!");
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // METHOD 2: Add a new project to the database
    // -------------------------------------------------------------
    public static boolean addProject(int userId, int serviceId, String clientName, String clientContact, String deadline) {
        String queryProject = "INSERT INTO PROJECT (user_id, main_service_id, client_name, client_contact, deadline, created_at) "
                            + "VALUES (?, ?, ?, ?, ?, NOW())";
        String queryBoard = "INSERT INTO board (project_id, board_name, description, is_completion_board, position_index) "
                          + "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = KoneksiDB.configDB();
            conn.setAutoCommit(false); // Use transaction
            
            int projectId = 0;
            try (PreparedStatement pstProject = conn.prepareStatement(queryProject, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                pstProject.setInt(1, userId);
                pstProject.setInt(2, serviceId);
                pstProject.setString(3, clientName);
                pstProject.setString(4, clientContact);
                pstProject.setString(5, deadline); // yyyy-MM-dd
                
                int rowsAffected = pstProject.executeUpdate();
                if (rowsAffected <= 0) {
                    conn.rollback();
                    return false;
                }
                
                try (ResultSet rs = pstProject.getGeneratedKeys()) {
                    if (rs.next()) {
                        projectId = rs.getInt(1);
                    }
                }
            }
            
            if (projectId == 0) {
                conn.rollback();
                return false;
            }
            
            try (PreparedStatement pstBoard = conn.prepareStatement(queryBoard)) {
                // Board 1: To-Do
                pstBoard.setInt(1, projectId);
                pstBoard.setString(2, "To-Do");
                pstBoard.setString(3, "Tugas yang belum dikerjakan");
                pstBoard.setInt(4, 0); // is_completion_board = false
                pstBoard.setInt(5, 1); // position_index = 1
                pstBoard.addBatch();
                
                // Board 2: In Progress
                pstBoard.setInt(1, projectId);
                pstBoard.setString(2, "In Progress");
                pstBoard.setString(3, "Tugas yang sedang dikerjakan");
                pstBoard.setInt(4, 0); // is_completion_board = false
                pstBoard.setInt(5, 2); // position_index = 2
                pstBoard.addBatch();
                
                // Board 3: Done
                pstBoard.setInt(1, projectId);
                pstBoard.setString(2, "Done");
                pstBoard.setString(3, "Tugas yang sudah selesai");
                pstBoard.setInt(4, 1); // is_completion_board = true
                pstBoard.setInt(5, 3); // position_index = 3
                pstBoard.addBatch();
                
                pstBoard.executeBatch();
            }
            
            conn.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error adding new project with boards!");
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ignored) {}
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (Exception ignored) {}
            }
        }
    }

    public static ProjectModel getProjectById(int projectId) {
        String query = "SELECT id, user_id, main_service_id, client_name, client_contact, deadline, is_active FROM PROJECT WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, projectId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new ProjectModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("main_service_id"),
                        rs.getString("client_name"),
                        rs.getString("client_contact"),
                        rs.getDate("deadline").toString(),
                        rs.getBoolean("is_active")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching project details for " + projectId);
            e.printStackTrace();
        }
        return null;
    }

    public static boolean editProject(int projectId, int serviceId, String clientName, String clientContact, String deadline) {
        String query = "UPDATE PROJECT SET main_service_id = ?, client_name = ?, client_contact = ?, deadline = ? WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, serviceId);
            pst.setString(2, clientName);
            pst.setString(3, clientContact);
            pst.setString(4, deadline);
            pst.setInt(5, projectId);
            
            int affected = pst.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            System.out.println("Error updating project " + projectId);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteProject(int projectId) {
        String queryTasks = "DELETE FROM task WHERE board_id IN (SELECT id FROM board WHERE project_id = ?)";
        String queryBoards = "DELETE FROM board WHERE project_id = ?";
        String queryProject = "DELETE FROM PROJECT WHERE id = ?";
        
        Connection conn = null;
        try {
            conn = KoneksiDB.configDB();
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstTasks = conn.prepareStatement(queryTasks)) {
                pstTasks.setInt(1, projectId);
                pstTasks.executeUpdate();
            }
            try (PreparedStatement pstBoards = conn.prepareStatement(queryBoards)) {
                pstBoards.setInt(1, projectId);
                pstBoards.executeUpdate();
            }
            try (PreparedStatement pstProject = conn.prepareStatement(queryProject)) {
                pstProject.setInt(1, projectId);
                int affected = pstProject.executeUpdate();
                if (affected > 0) {
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
            return false;
        } catch (Exception e) {
            System.out.println("Error deleting project " + projectId);
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ignored) {}
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (Exception ignored) {}
            }
        }
    }

    public static boolean setProjectActive(int projectId, boolean active) {
        String query = "UPDATE PROJECT SET is_active = ? WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setBoolean(1, active);
            pst.setInt(2, projectId);
            int affected = pst.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            System.out.println("Error updating project active status for " + projectId);
            e.printStackTrace();
            return false;
        }
    }

    public static class NewestProjectDTO {
        private int id;
        private String clientName;
        private String serviceName;
        private String deadline;
        private boolean isActive;

        public NewestProjectDTO(int id, String clientName, String serviceName, String deadline, boolean isActive) {
            this.id = id;
            this.clientName = clientName;
            this.serviceName = serviceName;
            this.deadline = deadline;
            this.isActive = isActive;
        }

        public int getId() { return id; }
        public String getClientName() { return clientName; }
        public String getServiceName() { return serviceName; }
        public String getDeadline() { return deadline; }
        public boolean isActive() { return isActive; }
    }

    public static java.util.List<NewestProjectDTO> getNewestProjectsForUser(int userId, int limit) {
        java.util.List<NewestProjectDTO> list = new java.util.ArrayList<>();
        String query = "SELECT p.id, p.client_name, p.deadline, p.is_active, s.service_name " +
                       "FROM PROJECT p " +
                       "LEFT JOIN main_service s ON p.main_service_id = s.id " +
                       "WHERE p.user_id = ? " +
                       "ORDER BY p.id DESC LIMIT ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setInt(2, limit);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new NewestProjectDTO(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("service_name") != null ? rs.getString("service_name") : "No Service",
                        String.valueOf(rs.getDate("deadline")),
                        rs.getBoolean("is_active")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching newest projects!");
            e.printStackTrace();
        }
        return list;
    }

    public static class NewestInvoiceDTO {
        private int id;
        private String invoiceNumber;
        private double totalAmount;
        private String status;
        private String generatedDate;
        private String clientName;
        private String serviceName;

        public NewestInvoiceDTO(int id, String invoiceNumber, double totalAmount, String status, String generatedDate, String clientName, String serviceName) {
            this.id = id;
            this.invoiceNumber = invoiceNumber;
            this.totalAmount = totalAmount;
            this.status = status;
            this.generatedDate = generatedDate;
            this.clientName = clientName;
            this.serviceName = serviceName;
        }

        public int getId() { return id; }
        public String getInvoiceNumber() { return invoiceNumber; }
        public double getTotalAmount() { return totalAmount; }
        public String getStatus() { return status; }
        public String getGeneratedDate() { return generatedDate; }
        public String getClientName() { return clientName; }
        public String getServiceName() { return serviceName; }
    }

    public static java.util.List<NewestInvoiceDTO> getNewestInvoicesForUser(int userId, int limit) {
        java.util.List<NewestInvoiceDTO> list = new java.util.ArrayList<>();
        String query = "SELECT i.id, i.invoice_number, i.total_amount, i.status, i.generated_date, p.client_name, ms.service_name " +
                       "FROM invoice i " +
                       "JOIN project p ON i.project_id = p.id " +
                       "LEFT JOIN main_service ms ON p.main_service_id = ms.id " +
                       "WHERE p.user_id = ? " +
                       "ORDER BY i.id DESC LIMIT ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setInt(2, limit);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new NewestInvoiceDTO(
                        rs.getInt("id"),
                        rs.getString("invoice_number"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        String.valueOf(rs.getDate("generated_date")),
                        rs.getString("client_name"),
                        rs.getString("service_name") != null ? rs.getString("service_name") : "No Service"
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching newest invoices!");
            e.printStackTrace();
        }
        return list;
    }

    public static class DashboardStatsDTO {
        private int activeProjectsCount;
        private int completedProjectsThisMonthCount;
        private double paidRevenue;
        private double pendingRevenue;
        private int deadlineTodayCount;
        private int deadlineTomorrowCount;
        private int totalInvoicesCount;
        private int pendingInvoicesCount;

        public DashboardStatsDTO(int activeProjectsCount, int completedProjectsThisMonthCount, double paidRevenue, double pendingRevenue, int deadlineTodayCount, int deadlineTomorrowCount, int totalInvoicesCount, int pendingInvoicesCount) {
            this.activeProjectsCount = activeProjectsCount;
            this.completedProjectsThisMonthCount = completedProjectsThisMonthCount;
            this.paidRevenue = paidRevenue;
            this.pendingRevenue = pendingRevenue;
            this.deadlineTodayCount = deadlineTodayCount;
            this.deadlineTomorrowCount = deadlineTomorrowCount;
            this.totalInvoicesCount = totalInvoicesCount;
            this.pendingInvoicesCount = pendingInvoicesCount;
        }

        public int getActiveProjectsCount() { return activeProjectsCount; }
        public int getCompletedProjectsThisMonthCount() { return completedProjectsThisMonthCount; }
        public double getPaidRevenue() { return paidRevenue; }
        public double getPendingRevenue() { return pendingRevenue; }
        public int getDeadlineTodayCount() { return deadlineTodayCount; }
        public int getDeadlineTomorrowCount() { return deadlineTomorrowCount; }
        public int getTotalInvoicesCount() { return totalInvoicesCount; }
        public int getPendingInvoicesCount() { return pendingInvoicesCount; }
    }

    public static DashboardStatsDTO getDashboardStats(int userId) {
        int activeProjects = 0;
        int completedProjects = 0;
        double paidRev = 0;
        double pendingRev = 0;
        int dueToday = 0;
        int dueTomorrow = 0;
        int totalInvoices = 0;
        int pendingInvoices = 0;

        try (Connection conn = KoneksiDB.configDB()) {
            // 1. activeProjects
            String q1 = "SELECT COUNT(*) FROM PROJECT WHERE user_id = ? AND is_active = 1";
            try (PreparedStatement pst = conn.prepareStatement(q1)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) activeProjects = rs.getInt(1);
                }
            }

            // 2. completedProjects this month
            String q2 = "SELECT COUNT(*) FROM PROJECT WHERE user_id = ? AND is_active = 0 AND MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())";
            try (PreparedStatement pst = conn.prepareStatement(q2)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) completedProjects = rs.getInt(1);
                }
            }

            // 3. paidRevenue
            String q3 = "SELECT COALESCE(SUM(i.total_amount), 0) FROM invoice i JOIN project p ON i.project_id = p.id WHERE p.user_id = ? AND (LOWER(i.status) = 'paid' OR LOWER(i.status) = 'lunas' OR LOWER(i.status) = 'selesai')";
            try (PreparedStatement pst = conn.prepareStatement(q3)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) paidRev = rs.getDouble(1);
                }
            }

            // 4. pendingRevenue
            String q4 = "SELECT COALESCE(SUM(i.total_amount), 0) FROM invoice i JOIN project p ON i.project_id = p.id WHERE p.user_id = ? AND LOWER(i.status) = 'pending'";
            try (PreparedStatement pst = conn.prepareStatement(q4)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) pendingRev = rs.getDouble(1);
                }
            }

            // 5. deadlineToday
            String q5 = "SELECT COUNT(*) FROM task t JOIN board b ON t.board_id = b.id JOIN project p ON b.project_id = p.id WHERE p.user_id = ? AND b.is_completion_board = 0 AND LOWER(b.board_name) != 'done' AND t.deadline = CURRENT_DATE()";
            try (PreparedStatement pst = conn.prepareStatement(q5)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) dueToday = rs.getInt(1);
                }
            }

            // 6. deadlineTomorrow
            String q6 = "SELECT COUNT(*) FROM task t JOIN board b ON t.board_id = b.id JOIN project p ON b.project_id = p.id WHERE p.user_id = ? AND b.is_completion_board = 0 AND LOWER(b.board_name) != 'done' AND t.deadline = DATE_ADD(CURRENT_DATE(), INTERVAL 1 DAY)";
            try (PreparedStatement pst = conn.prepareStatement(q6)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) dueTomorrow = rs.getInt(1);
                }
            }

            // 7. totalInvoices
            String q7 = "SELECT COUNT(*) FROM invoice i JOIN project p ON i.project_id = p.id WHERE p.user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(q7)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) totalInvoices = rs.getInt(1);
                }
            }

            // 8. pendingInvoices
            String q8 = "SELECT COUNT(*) FROM invoice i JOIN project p ON i.project_id = p.id WHERE p.user_id = ? AND LOWER(i.status) = 'pending'";
            try (PreparedStatement pst = conn.prepareStatement(q8)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) pendingInvoices = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            System.out.println("Error fetching dashboard statistics!");
            e.printStackTrace();
        }

        return new DashboardStatsDTO(activeProjects, completedProjects, paidRev, pendingRev, dueToday, dueTomorrow, totalInvoices, pendingInvoices);
    }
}

