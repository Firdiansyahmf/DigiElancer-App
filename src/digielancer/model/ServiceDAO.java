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
public class ServiceDAO {
    public static List<ServiceModel> getServicesForUser(int userId) {
        List<ServiceModel> serviceList = new ArrayList<>();
        
        // Adjust "service_name" if your actual database column is named differently (like "name" or "title")
        String query = "SELECT id, user_id, service_name FROM main_service WHERE user_id = ?";
        
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
             
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                ServiceModel service = new ServiceModel(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("service_name")
                );
                serviceList.add(service);
            }
        } catch (Exception e) {
            System.out.println("Error fetching services!");
            e.printStackTrace();
        }
        
        return serviceList;
    }

    public static ServiceModel getServiceById(int serviceId) {
        String query = "SELECT id, user_id, service_name FROM main_service WHERE id = ?";
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, serviceId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new ServiceModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("service_name")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching service by ID: " + serviceId);
            e.printStackTrace();
        }
        return null;
    }
}
