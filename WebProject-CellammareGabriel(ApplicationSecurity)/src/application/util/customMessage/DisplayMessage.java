package application.util.customMessage;

import javax.swing.JFrame;



public class DisplayMessage{

    public static void showPanel(String message) {
        // Crea una finestra e aggiungi il pannello
        JFrame frame = new JFrame("MESSAGGIO DAL SITO WEB: ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Label panel = new Label(message);
        frame.getContentPane().add(panel);
        
        frame.pack(); 
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);

        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frame.setVisible(false);
            }
        });
    }
}
