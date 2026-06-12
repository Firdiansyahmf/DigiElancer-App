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
        String query = "SELECT t.id, t.board_id, t.title, t.description, t.deadline, t.priority " +
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
                    rs.getString("description"),
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

    public static List<BoardModel> getBoardsForProject(int projectId) {
        List<BoardModel> boardList = new ArrayList<>();
        String query = "SELECT id, project_id, board_name, description, is_completion_board " +
                       "FROM board WHERE project_id = ? ORDER BY position_index ASC";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, projectId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    boardList.add(new BoardModel(
                        rs.getInt("id"),
                        rs.getInt("project_id"),
                        rs.getString("board_name"),
                        rs.getString("description"),
                        rs.getBoolean("is_completion_board")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching boards for project " + projectId);
            e.printStackTrace();
        }
        return boardList;
    }

    public static boolean addBoard(int projectId, String boardName, String description) {
        String query = "INSERT INTO board (project_id, board_name, description, is_completion_board, position_index) " +
                       "SELECT ?, ?, ?, 0, COALESCE(MAX(position_index), -1) + 1 FROM board WHERE project_id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, projectId);
            pst.setString(2, boardName);
            pst.setString(3, description);
            pst.setInt(4, projectId);
            
            int affected = pst.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            System.out.println("Error adding board for project " + projectId);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addTask(int boardId, String title, String description, String deadline, String priority) {
        String query = "INSERT INTO task (board_id, title, description, deadline, priority, created_at) " +
                       "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, boardId);
            pst.setString(2, title);
            pst.setString(3, description);
            pst.setString(4, deadline);
            pst.setString(5, priority);
            
            int affected = pst.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            System.out.println("Error adding task for board " + boardId);
            e.printStackTrace();
            return false;
        }
    }

    public static BoardModel getBoardById(int boardId) {
        String query = "SELECT id, project_id, board_name, description, is_completion_board FROM board WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, boardId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new BoardModel(
                        rs.getInt("id"),
                        rs.getInt("project_id"),
                        rs.getString("board_name"),
                        rs.getString("description"),
                        rs.getBoolean("is_completion_board")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching board by ID: " + boardId);
            e.printStackTrace();
        }
        return null;
    }

    public static boolean editBoard(int boardId, String boardName, String description) {
        String query = "UPDATE board SET board_name = ?, description = ? WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, boardName);
            pst.setString(2, description);
            pst.setInt(3, boardId);
            
            int affected = pst.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            System.out.println("Error updating board " + boardId);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteBoard(int boardId) {
        String queryTasks = "DELETE FROM task WHERE board_id = ?";
        String queryBoard = "DELETE FROM board WHERE id = ?";
        
        Connection conn = null;
        try {
            conn = KoneksiDB.configDB();
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstTasks = conn.prepareStatement(queryTasks)) {
                pstTasks.setInt(1, boardId);
                pstTasks.executeUpdate();
            }
            try (PreparedStatement pstBoard = conn.prepareStatement(queryBoard)) {
                pstBoard.setInt(1, boardId);
                int affected = pstBoard.executeUpdate();
                if (affected > 0) {
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
            return false;
        } catch (Exception e) {
            System.out.println("Error deleting board " + boardId);
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

    public static TaskModel getTaskById(int taskId) {
        String query = "SELECT id, board_id, title, description, deadline, priority FROM task WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, taskId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new TaskModel(
                        rs.getInt("id"),
                        rs.getInt("board_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        String.valueOf(rs.getDate("deadline")),
                        rs.getString("priority")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching task by ID: " + taskId);
            e.printStackTrace();
        }
        return null;
    }

    public static boolean editTask(int taskId, int boardId, String title, String description, String deadline, String priority) {
        String query = "UPDATE task SET board_id = ?, title = ?, description = ?, deadline = ?, priority = ? WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, boardId);
            pst.setString(2, title);
            pst.setString(3, description);
            pst.setString(4, deadline);
            pst.setString(5, priority);
            pst.setInt(6, taskId);
            
            int affected = pst.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            System.out.println("Error updating task " + taskId);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTask(int taskId) {
        String query = "DELETE FROM task WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, taskId);
            int affected = pst.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            System.out.println("Error deleting task " + taskId);
            e.printStackTrace();
            return false;
        }
    }

    public static List<TaskModel> getUpcomingTasksForUser(int userId) {
        List<TaskModel> taskList = new ArrayList<>();
        String query = "SELECT t.id, t.board_id, t.title, t.description, t.deadline, t.priority " +
                       "FROM task t " +
                       "JOIN board b ON t.board_id = b.id " +
                       "JOIN project p ON b.project_id = p.id " +
                       "WHERE p.user_id = ? AND b.is_completion_board = 0 AND LOWER(b.board_name) != 'done'";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    taskList.add(new TaskModel(
                        rs.getInt("id"),
                        rs.getInt("board_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        String.valueOf(rs.getDate("deadline")),
                        rs.getString("priority")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching upcoming tasks for user: " + userId);
            e.printStackTrace();
        }
        return taskList;
    }
}

