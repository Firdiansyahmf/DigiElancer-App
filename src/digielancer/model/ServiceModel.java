/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package digielancer.model;

/**
 *
 * @author ASUS
 */
public class ServiceModel {
    private int id;
    private int userId;
    private String serviceName;

    public ServiceModel(int id, int userId, String serviceName) {
        this.id = id;
        this.userId = userId;
        this.serviceName = serviceName;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getServiceName() { return serviceName; }

    // CRITICAL: This dictates what text appears in the JComboBox
    @Override
    public String toString() {
        return this.serviceName;
    }
}
