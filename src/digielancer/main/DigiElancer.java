/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package digielancer.main;

import java.lang.Runnable;

/**
 *
 * @author ASUS
 */
public class DigiElancer {
    public static void main(String[] args) {
        
        // Optional: Test your database connection immediately on startup
        // DBConnection.getConnection();
        
        // Launch the UI safely
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Open the master dashboard
                new Navbar().setVisible(true);
            }
        });
    }
}
