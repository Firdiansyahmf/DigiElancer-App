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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
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
