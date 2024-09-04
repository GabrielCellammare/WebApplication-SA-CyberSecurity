package application.util.customMessage;

import javax.swing.JFrame;



public final class DisplayMessage{

	public static void showPanel(String message) {
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
	}
}
