package hui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import simpleChat.Chat;

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
	String myName;
	
	SendPanel(DatagramSocket socket, MainFrame mainFrame) {
		
		this.socket = socket;
		this.mainFrame = mainFrame;
		
		this.myName = mainFrame.chatApp.myname;
		
		setLayout(new FlowLayout());
		
		textFieldArea = new TextFieldArea();
		sendBtn = new SendBtn();
		
		add(textFieldArea);
		add(sendBtn);
		
	}
	
	void sendTextField() {
		simpleChat.Chat chat = new simpleChat.Chat(textFieldArea.textField.getText(), mainFrame.chatApp.usrarr[0].name);
		
		if(chat.checkBw(mainFrame.chatApp.usrarr[0].banwrd)) {
			
			mainFrame.chatApp.usrarr[0].alive = false;
			
			mainFrame.display("[*]" + mainFrame.chatApp.usrarr[0].name + "의 금칙어 사용!");
			mainFrame.display("[*]" + simpleChat.Chat.bidx + "번째 문자에서 금칙어 사용");
			
			simpleChat.ChatApp.hostNotify(socket, mainFrame.chatApp.usrarr, ("[*]" + mainFrame.chatApp.usrarr[0].name + "의 금칙어 사용!"));
			simpleChat.ChatApp.hostNotify(socket, mainFrame.chatApp.usrarr, ("[*]" + simpleChat.Chat.bidx + "번째 문자에서 금칙어 사용"));
			
			String tmp = "[*]남은 생존자 : ";
			
			for (int i = 0; i < simpleChat.User.current; i++) {
				if (mainFrame.chatApp.usrarr[i].alive) {
					tmp += (mainFrame.chatApp.usrarr[i].name + ", ");
				}
			}
			
			simpleChat.ChatApp.hostNotify(socket, mainFrame.chatApp.usrarr, tmp);
			mainFrame.display(tmp);
			
			byte[] msg = chat.getHBytes();
			for (int i = 1; i < simpleChat.User.current; i++) {
				DatagramPacket chatPacket = new DatagramPacket(msg, msg.length, mainFrame.chatApp.usrarr[i].ip, mainFrame.chatApp.usrarr[i].port);
				try {
					socket.send(chatPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		} else {
			byte[] msg = chat.getHBytes();
			for (int i = 1; i < simpleChat.User.current; i++) {
				DatagramPacket chatPacket = new DatagramPacket(msg, msg.length, mainFrame.chatApp.usrarr[i].ip, mainFrame.chatApp.usrarr[i].port);
				try {
					socket.send(chatPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		mainFrame.display(chat.displayString());
		
	}
	
}
