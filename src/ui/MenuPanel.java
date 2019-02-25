package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel {
	
	//엔터키로 전송 버튼
	static class EnterToSendBtn extends JButton {
		
		static boolean enterToSend = true;
		
		static class ETSActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				
				if(enterToSend) {
					btn.setText("엔터키로 전송 켜기");
					enterToSend = false;
				} else {
					btn.setText("엔터키로 전송 끄기");
					enterToSend = true;
				}
				
			}
		}
		
		EnterToSendBtn() {
			addActionListener(new ETSActionListener());
			setText("엔터키로 전송 끄기");
		}
		
	}
	//엔터키로 전송 버튼 끝
	
	EnterToSendBtn enterToSendBtn;
	ScrollBtn enableScrollBtn;
	
	MenuPanel() {
		setLayout(new GridLayout(1, 3, 0, 0));
		
		enableScrollBtn = new ScrollBtn();
		enterToSendBtn = new EnterToSendBtn();
		
		add(enterToSendBtn);
		add(enableScrollBtn);
	}
	
}
