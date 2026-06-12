/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package digielancer.model;

import digielancer.main.KoneksiDB;

/**
 *
 * @author ASUS
 */
public class ProjectModel {
    private int id;
    private int userId;
    private int mainServiceId;
    private String clientName;
    private String clientContact;
    private String deadline;
    private boolean isActive;

    public ProjectModel(int id, int userId, int mainServiceId, String clientName, String clientContact, String deadline, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.mainServiceId = mainServiceId;
        this.clientName = clientName;
        this.clientContact = clientContact;
        this.deadline = deadline;
        this.isActive = isActive;
    }

    public ProjectModel(int id, String clientName, String deadline, boolean isActive) {
        this.id = id;
        this.clientName = clientName;
        this.deadline = deadline;
        this.isActive = isActive;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getMainServiceId() { return mainServiceId; }
    public String getClientName() { return clientName; }
    public String getClientContact() { return clientContact; }
    public String getDeadline() { return deadline; }
    public boolean isActive() { return isActive; }
}
