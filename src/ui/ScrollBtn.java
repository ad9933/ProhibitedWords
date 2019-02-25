package ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ScrollBtn extends JButton {
	
	class EnableScrollActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton eventBtn = (JButton)e.getSource();
			
			if (TextDisplay.scrollEnabled) {
				eventBtn.setText("�ڵ� ��ũ�� �ѱ�");
				TextDisplay.scrollEnabled = false;
				
			} else {
				eventBtn.setText("�ڵ� ��ũ�� ����");
				TextDisplay.scrollEnabled = true;
			}
				
		}
	}
	
	ScrollBtn() {
		super.setText("�ڵ� ��ũ�� ����");
		super.addActionListener(new EnableScrollActionListener());
		
	}
}
