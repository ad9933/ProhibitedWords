package ui;

import javax.swing.*;
import java.awt.*;

public class TextDisplay extends JScrollPane{
	
	static final int SCROLLBAR_INCREMENT = 18;
	
	static boolean scrollEnabled = true;
	
	int linenum = 0;
	int scrollValue = 0;
	
	JTextArea messageBoard = null;
	JScrollBar scrollBar;
	
	public TextDisplay() {
		
		super();
		
		messageBoard = new JTextArea(10, 40);
		messageBoard.setEditable(false);
		
		setViewportView(messageBoard);
		scrollBar = getVerticalScrollBar();
		
	}
	
	public void display(String str) {
		if(linenum > 10) {
			scrollValue += TextDisplay.SCROLLBAR_INCREMENT;
			
			if (scrollEnabled)
				scrollBar.setValue(scrollValue);
			
		} else { linenum++; }
		
		messageBoard.append(str + '\n');
	}
}
