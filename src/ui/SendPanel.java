package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import java.net.*;

public class SendPanel extends JPanel {
	
	//텍스트 입력부분 시작
	class TextFieldArea extends JScrollPane {
		
		TextField textField;
		
		class TextField extends JTextArea {
			
			//엔터키 치면 전송
			class EnterKeyListener implements KeyListener {
				
				public void keyTyped(KeyEvent e) {}
				
				public void keyPressed(KeyEvent e) {
					if (MenuPanel.EnterToSendBtn.enterToSend) {
						//엔터키 쳤을때 실행될 문장
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
			//엔터키 치면 전송 끝
			
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
	//텍스트 입력부분 끝
	
	//전송버튼
	class SendBtn extends JButton {
		
		class SendActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				sendTextField();
				textFieldArea.textField.setText(""); 
				
				}
		}
		
		SendBtn() { 
			super("보내기");
			addActionListener(new SendActionListener());
		}
	}
	//버튼선언 끝
	
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
