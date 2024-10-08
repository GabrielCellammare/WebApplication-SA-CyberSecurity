package application.util.customMessage;

import javax.annotation.concurrent.ThreadSafe;
import javax.swing.JFrame;
import java.awt.event.WindowListener;

@ThreadSafe
final class SecureJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SecureJFrame(String title) {
		super(title);
		// Impostazioni di default di chiusura
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	// Sovrascrivere setVisible per loggare o bloccare tentativi di manipolazione.
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	// Sovrascrivere dispose per loggare o bloccare tentativi di manipolazione.
	@Override
	public void dispose() {
		super.dispose();
	}

	// Sovrascriviere metodi chiave 
	@Override
	public void addWindowListener(WindowListener l) {
		super.addWindowListener(l);
	}
}
