package hui;

import javax.swing.*;
import java.awt.*;
import java.net.*;

public class MainFrame extends JFrame {
	
	simpleChat.ChatApp chatApp;
	
	DatagramSocket socket;
	
	Container pane = null;
	TextDisplay chatArea = null;
	MenuPanel menuPanel = null;
	SendPanel sendPanel = null;
	
	public MainFrame(simpleChat.ChatApp chatApp, DatagramSocket socket) {
		
		this.chatApp = chatApp;
		
		this.socket = socket;
		
		chatArea = new TextDisplay();
		menuPanel = new MenuPanel();
		sendPanel = new SendPanel(socket, this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 750);
		setTitle("Honey's 금칙어 게임 : " + chatApp.usrarr[0].name);
		setLayout(new GridLayout(3, 1, 0, 0));
		
		pane = getContentPane();
		
		pane.add(chatArea);
		
		pane.add(menuPanel);
		
		pane.add(sendPanel);
		
		setVisible(true);
		
	}
	
	public void display(String str) {chatArea.display(str);}
	
}
