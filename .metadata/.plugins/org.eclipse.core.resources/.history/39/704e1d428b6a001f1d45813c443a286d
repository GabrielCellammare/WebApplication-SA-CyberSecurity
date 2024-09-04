package application.util.customMessage;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

final class Label extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JLabel label;

	protected Label(String message) {
        this.label = new JLabel(message);
        add(label);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        int width = (int) preferredSize.getWidth() + 20; 
        int height = (int) preferredSize.getHeight() + 20;
        return new Dimension(width, height);
    }
}
