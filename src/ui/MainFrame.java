package ui;

import javax.swing.*;
import java.awt.*;
import java.net.*;

public class MainFrame extends JFrame {
	
	Container pane = null;
	TextDisplay chatArea = null;
	MenuPanel menuPanel = null;
	SendPanel sendPanel = null;
	
	DatagramSocket socket;
	InetAddress server;
	
	public MainFrame(DatagramSocket socket, InetAddress server, String myName) {
		
		this.socket = socket;
		this.server = server;
		
		chatArea = new TextDisplay();
		menuPanel = new MenuPanel();
		sendPanel = new SendPanel(socket, server, myName, this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 750);
		setTitle("Honey's 금칙어 게임 : " + myName);
		setLayout(new GridLayout(3, 1, 0, 0));
		
		pane = getContentPane();
		
		pane.add(chatArea);
		
		pane.add(menuPanel);
		
		pane.add(sendPanel);
		
		setVisible(true);
		
	}
	
	public void display(String str) {chatArea.display(str);}
	
}
