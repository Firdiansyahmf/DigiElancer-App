/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package digielancer.model;

/**
 *
 * @author ASUS
 */
public class TaskModel {
    private int id;
    private int boardId; // 1 = To-Do, 2 = In Progress, 3 = Done
    private String title;
    private String deadline;
    private String priority;

    public TaskModel(int id, int boardId, String title, String deadline, String priority) {
        this.id = id;
        this.boardId = boardId;
        this.title = title;
        this.deadline = deadline;
        this.priority = priority;
    }

    // Getters
    public int getId() { return id; }
    public int getBoardId() { return boardId; }
    public String getTitle() { return title; }
    public String getDeadline() { return deadline; }
    public String getPriority() { return priority; }
}
