package ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ScrollBtn extends JButton {
	
	class EnableScrollActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton eventBtn = (JButton)e.getSource();
			
			if (TextDisplay.scrollEnabled) {
				eventBtn.setText("자동 스크롤 켜기");
				TextDisplay.scrollEnabled = false;
				
			} else {
				eventBtn.setText("자동 스크롤 끄기");
				TextDisplay.scrollEnabled = true;
			}
				
		}
	}
	
	ScrollBtn() {
		super.setText("자동 스크롤 끄기");
		super.addActionListener(new EnableScrollActionListener());
		
	}
}
