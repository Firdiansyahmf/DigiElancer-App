/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package digielancer.model;

import digielancer.main.KoneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class TaskDAO {
    // This fetches all tasks for a specific project
    public static List<TaskModel> getTasksForProject(int projectId) {
        List<TaskModel> taskList = new ArrayList<>();
        
        // Ensure your table and column names exactly match your database
        String query = "SELECT t.id, t.board_id, t.title, t.deadline, t.priority " +
               "FROM TASK t " +
               "JOIN BOARD b ON t.board_id = b.id " +
               "WHERE b.project_id = ?";
        
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
             
            pst.setInt(1, projectId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                TaskModel task = new TaskModel(
                    rs.getInt("id"),
                    rs.getInt("board_id"),
                    rs.getString("title"),
                    // Using String.valueOf() to safely handle null dates
                    String.valueOf(rs.getDate("deadline")), 
                    rs.getString("priority")
                );
                taskList.add(task);
            }
        } catch (Exception e) {
            System.out.println("Error fetching tasks for project " + projectId);
            e.printStackTrace();
        }
        
        return taskList;
    }
}
