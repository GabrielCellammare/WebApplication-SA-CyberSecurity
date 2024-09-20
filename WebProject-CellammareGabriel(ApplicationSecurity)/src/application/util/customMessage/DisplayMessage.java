package application.util.customMessage;

import javax.annotation.concurrent.ThreadSafe;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


@ThreadSafe
public final class DisplayMessage{

	//Metodo che prende in input il messaggio che verrà visualizzato in un Panel con una Label
	public static void showPanel(String message) {
	    SwingUtilities.invokeLater(() -> {
	        // Crea una finestra e aggiungi il pannello
	        SecureJFrame frame = new SecureJFrame("MESSAGGIO WEB APP");
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	        SecureJPanel panel = new SecureJPanel(message);
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
	    });
	}

}
