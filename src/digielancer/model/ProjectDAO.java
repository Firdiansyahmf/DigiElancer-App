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
        
        // The SQL Insert statement using NOW() for the created_at timestamp
        String query = "INSERT INTO PROJECT (user_id, main_service_id, client_name, client_contact, deadline, created_at) "
                     + "VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
             
            pst.setInt(1, userId);
            pst.setInt(2, serviceId);
            pst.setString(3, clientName);
            pst.setString(4, clientContact);
            pst.setString(5, deadline); // This must arrive in "yyyy-MM-dd" format!
            
            // Execute the insert. If it affects 1 or more rows, it was successful.
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.out.println("Error adding new project!");
            e.printStackTrace();
            return false;
        }
    }
}
