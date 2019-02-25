package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import java.net.*;

public class SendPanel extends JPanel {
	
	//�ؽ�Ʈ �Էºκ� ����
	class TextFieldArea extends JScrollPane {
		
		TextField textField;
		
		class TextField extends JTextArea {
			
			//����Ű ġ�� ����
			class EnterKeyListener implements KeyListener {
				
				public void keyTyped(KeyEvent e) {}
				
				public void keyPressed(KeyEvent e) {
					if (MenuPanel.EnterToSendBtn.enterToSend) {
						//����Ű ������ ����� ����
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							
							sendTextField();
							
							textFieldArea.textField.setText("");
						}
						
					}
				}
				
				public void keyReleased(KeyEvent e) {
					if (MenuPanel.EnterToSendBtn.enterToSend) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							textFieldArea.textField.setText("");
						}
						
					}
				}
				
			}
			//����Ű ġ�� ���� ��
			
			TextField() {
				super(10, 40);
				addKeyListener(new EnterKeyListener());
				
			}
		}
		
		TextFieldArea() {
			super();
			
			textField = new TextField();
			setViewportView(textField);
		}
		
	}
	//�ؽ�Ʈ �Էºκ� ��
	
	//���۹�ư
	class SendBtn extends JButton {
		
		class SendActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				sendTextField();
				textFieldArea.textField.setText(""); 
				
				}
		}
		
		SendBtn() { 
			super("������");
			addActionListener(new SendActionListener());
		}
	}
	//��ư���� ��
	
	TextFieldArea textFieldArea;
	SendBtn sendBtn;
	
	MainFrame mainFrame;
	
	DatagramSocket socket;
	InetAddress server;
	String myName;
	
	SendPanel(DatagramSocket socket, InetAddress server, String myName, MainFrame mainFrame) {
		
		this.socket = socket;
		this.server = server;
		this.myName = myName;
		this.mainFrame = mainFrame;
		
		setLayout(new FlowLayout());
		
		textFieldArea = new TextFieldArea();
		sendBtn = new SendBtn();
		
		add(textFieldArea);
		add(sendBtn);
		
	}
	
	void sendTextField() {
		simpleChat.Chat chat = new simpleChat.Chat(textFieldArea.textField.getText(), myName);
		byte[] data = chat.getBytes();
		
		DatagramPacket chatPacket = new DatagramPacket(data, data.length, server, 7777);
		try {
			socket.send(chatPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mainFrame.display(chat.displayString());
		
	}
	
}
