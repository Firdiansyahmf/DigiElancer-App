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
        
        // look feell
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // splashh
        java.awt.EventQueue.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
            splash.startLoading();
        });
    }
}
