// jar -cfvm Blackjack.jar Blackjack.mf *
// jar -cfvm Blackjack.jar Blackjack.mf *.class card_images Cards/*.class Players/*.class
package blackjack;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;


public class Blackjack
{
    public static void main(final String[] args)
    {
        try
        {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.setLookAndFeel(new FlatLightLaf());



             UIManager.put("Button.arc", 999);                       // bottoni rotondi
            UIManager.put("Button.background", "#E0E0E0");            // colore di sfondo
            UIManager.put("Button.foreground", "#333333");            // colore del testo
            UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
            UIManager.put("Button.focusWidth", 0);                    // rimuove il bordo focus
            // Puoi aggiungere ulteriori proprietà di FlatLaf per altri componenti se vuoi.
        }
        catch (final Exception e)
        {
            System.out.println(e);
        }
        
        /**
         * I think that an application should adopt the same look and feel as 
         * the system it's running on. This is what a user expects from
         * an application. It's great that Java is cross platform and all,
         * but the end-user doesn't care if a program is written in Java
         * or not.
         * So, I force the application to use the system look and feel. If
         * the look and feel can't be found, it'll use metal anyway as a last
         * resort.
         */
        
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        // final AppWindow window = new AppWindow();      
        javax.swing.SwingUtilities.invokeLater(() -> {
            final AppWindow window = new AppWindow();  // Supponendo che AppWindow sia la tua finestra principale
            window.setVisible(true);
        });
    }
}