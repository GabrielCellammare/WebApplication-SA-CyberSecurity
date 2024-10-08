package application.util.customMessage;

import java.awt.Dimension;
import javax.annotation.concurrent.ThreadSafe;
import javax.swing.JLabel;
import javax.swing.JPanel;

@ThreadSafe
final class SecureJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;

	SecureJPanel(String message) {
		this.label = new JLabel(message);
		add(label);
	}

	// Sovrascrivere il metodo add per loggare o impedire l'aggiunta di componenti non autorizzati.
	@Override
	public void addNotify() {
		super.addNotify();
	}

	// Sovrascrivere il metodo remove per loggare o impedire la rimozione di componenti.
	@Override
	public void removeNotify() {
		super.removeNotify();
	}

	// Sovrascrivere il metodo getPreferredSize per evitare manipolazioni delle dimensioni del pannello.
	@Override
	public Dimension getPreferredSize() {
		Dimension preferredSize = super.getPreferredSize();
		int width = (int) preferredSize.getWidth() + 20; 
		int height = (int) preferredSize.getHeight() + 20;
		return new Dimension(width, height);
	}
}
