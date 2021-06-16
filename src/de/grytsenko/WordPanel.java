package de.grytsenko;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolTip;

@SuppressWarnings("serial")
class WordPanel extends JButton {

	   public WordPanel() {
	      super();
	      setEnabled(false);
	      setBackground(Color.WHITE);
	      setFont(new Font("Arial", 0, 12));
	   }

	   @Override
	   public JToolTip createToolTip() {
	      return (new CustomToolTip(this));
	   }
	
	}

	@SuppressWarnings("serial")
	class CustomToolTip extends JToolTip {
	   public CustomToolTip(JComponent component) {
	      super();
	      setComponent(component);
	      setBackground(new Color(246,246,185));
	      setForeground(Color.BLACK);
	      setFont(this.getFont().deriveFont(Font.TRUETYPE_FONT, 16));
	      setOpaque(true);
	   }
	}
