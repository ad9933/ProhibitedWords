package simpleChat;

import java.util.*;
import java.net.*;
import java.io.*;

public class ClientListen extends Thread {
	
	DatagramSocket socket;
	InetAddress servAddr;
	ui.MainFrame window;
	
	ClientListen(DatagramSocket socket, InetAddress servAddr, ui.MainFrame window) {
		this.socket = socket;
		this.servAddr = servAddr;
		this.window = window;
	}
	
	public void run() {
		DatagramPacket chatPacket;
		Chat inChat;
		byte[] data;
		
		while (true) {
			data = new byte[ChatApp.MAX_BYTE];
			chatPacket = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(chatPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String datastr = new String(data);
			
			if (datastr.charAt(0) == PacketHeader.HOS_CHT) {
				inChat = new Chat(ChatApp.getMessage(datastr));
				inChat.display();
				window.display(inChat.displayString());
			}
			
			//TODO : ������Ŷ�� ��������� ȣ��Ʈ���� Ȯ���ϴ� �κ��� �߰��Ұ�
			if (datastr.charAt(0) == PacketHeader.HOS_NOF && chatPacket.getAddress().equals(servAddr)) {
				System.out.println(ChatApp.getMessage(datastr));
				window.display(ChatApp.getMessage(datastr));
				
			}
		}
		
	}
}
