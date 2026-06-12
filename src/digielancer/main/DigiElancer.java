/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package digielancer.main;

import com.formdev.flatlaf.FlatLightLaf;
import java.lang.Runnable;
import javax.swing.UIManager;

/**
 *
 * @author ASUS
 */
public class DigiElancer {
    public static void main(String[] args) {
        
        FlatLightLaf.setup();
        
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        
        // splashh
        java.awt.EventQueue.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
            splash.startLoading();
        });
    }
}
