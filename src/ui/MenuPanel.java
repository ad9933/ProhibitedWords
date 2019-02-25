package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel {
	
	//����Ű�� ���� ��ư
	static class EnterToSendBtn extends JButton {
		
		static boolean enterToSend = true;
		
		static class ETSActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				
				if(enterToSend) {
					btn.setText("����Ű�� ���� �ѱ�");
					enterToSend = false;
				} else {
					btn.setText("����Ű�� ���� ����");
					enterToSend = true;
				}
				
			}
		}
		
		EnterToSendBtn() {
			addActionListener(new ETSActionListener());
			setText("����Ű�� ���� ����");
		}
		
	}
	//����Ű�� ���� ��ư ��
	
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
