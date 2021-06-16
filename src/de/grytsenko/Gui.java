package de.grytsenko;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class Gui extends JFrame {
	
	Gui() {
		super();
		this.setTitle("PK1 - Project"); //Window title
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setPreferredSize(new Dimension(1314, 700));
		
		frameLayout =  new BorderLayout();
		gridBagLayout = new GridBagLayout();
		panel = new JPanel();
		scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //Table can be scrolled in both dimensions
		scroll.getVerticalScrollBar().setUnitIncrement(12);
		
		cells = new ArrayList<List<WordPanel>>();
		
		gridConstraints = new GridBagConstraints();
		
		this.setLayout(frameLayout);
		panel.setLayout(gridBagLayout);
		
		textArea = new JTextArea(2, 0);

		this.add(textArea, BorderLayout.NORTH);
		textArea.setEditable(false);
		this.add(scroll);
		
		btnDimension = new Dimension(128, 80);
		
		ToolTipManager.sharedInstance().setInitialDelay(500);  //Context information delay
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		
		UIManager.put("Button.disabledText", Color.BLACK);
	}
	
	private JScrollPane scroll;
	private GridBagConstraints gridConstraints;
	private BorderLayout frameLayout;
	private GridBagLayout gridBagLayout;
	private JPanel panel;
	private List<List<WordPanel>> cells;
	private Dimension btnDimension;
	private JTextArea textArea;

	
	//Fill table with data
    public void fillTable(TableData tableData) {
    	for (int i = 0; i < tableData.getCellTable().size(); i ++) {
    		cells.add(new ArrayList<WordPanel>());
    		for (int j = 0; j < tableData.getCellTable().get(i).size(); j++) {
    			
    			cells.get(i).add(new WordPanel()); //Make new cell
    			cells.get(i).get(j).setToolTipText(tableData.getCellTable().get(i).get(j).getContextString()); //Add context data

    			cells.get(i).get(j).setBackground(calculateColor(tableData.getCellTable().get(i).get(j).getNumberOfWords(), 
    					tableData.getNumberOfMoustRepeatedNoun())); //Calculate and set cell color
    			
    			cells.get(i).get(j).setPreferredSize(btnDimension);
    			
    			gridConstraints.gridx = i; //cell position y
    			gridConstraints.gridy = j; //cell position x
    			
    			panel.add(cells.get(i).get(j), gridConstraints); //Add cell
    			
    			//Set text
    			cells.get(i).get(j).setText(tableData.getCellTable().get(i).get(j).getWord()  + " (" + tableData.getCellTable().get(i).get(j).getNumberOfWords() + ")");
    		}
    		this.pack();
    		textArea.setText(generateInfo(tableData)); //Display topFive Words and other information
    	}
    }

    private String generateInfo(TableData tableData) {
    	String info = new String();
    	
    	for (String str : tableData.getTopFiveSorted()) {
    		info += str;
    	}
    	
    	info += "Words per column: " + tableData.getPartSize();
		return info;
	}

    //Calculate color of a cell
	private Color calculateColor(int value, int maxValue) {	
    	float percent = value / (float)maxValue;
    	
        if (percent < 0 || percent > 1) return Color.BLACK;

        int r, g;
        
        if (percent < 0.5) {
            g = 255;
            r = (int)(255 * percent / 0.5); //closer to 1.0, closer to green (0,255,0)
        }
        else {
            r = 255;
            g = 255 - (int)(255 * (percent - 0.5) / 0.5); //closer to 0.5, closer to yellow (255,255,0)
        }
        return new Color(r, g, 0);
	}

}
