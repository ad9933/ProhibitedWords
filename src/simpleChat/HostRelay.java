package simpleChat;

import java.util.*;
import java.io.*;
import java.net.*;

public class HostRelay extends Thread {
	
	hui.MainFrame window;
	
	DatagramSocket socket;
	User[] usrarr;
	
	HostRelay(DatagramSocket socket, User[] usrarr, hui.MainFrame window) {
		
		this.window = window;
		
		this.socket = socket;
		this.usrarr = usrarr;
	}
	
	public void run() {
		byte msg[] = new byte[ChatApp.MAX_BYTE];
		int usridx = 0;
		DatagramPacket inPacket = new DatagramPacket(msg, msg.length);
		Chat rec;
		
		while (true) {
			
			inPacket = new DatagramPacket(new byte[ChatApp.MAX_BYTE], ChatApp.MAX_BYTE);
			usridx = 0;
			
			try {
				socket.receive(inPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String datastr = new String(inPacket.getData());
			
			
			if (datastr.charAt(0) == PacketHeader.CLI_CHT) {
				rec = new Chat(ChatApp.getMessage(datastr));
				
				//��� ���������� �˾Ƴ��� �κ�
				for (User u : usrarr) {
					if (u.name.equals(rec.user) && u.ip.equals(inPacket.getAddress()))
						break;
					else
						usridx++;
				}
				
				if (usridx > User.current) {
					window.display("[*]" + inPacket.getAddress().toString() + " ���� ���� " + datastr + "�� ���õ�");
					System.out.println("[*]" + inPacket.getAddress().toString() + " ���� ���� " + datastr + "�� ���õ�");
				} else {
					
					//ä�ÿ� ��Ģ� ����ִ����� Ȯ���ϴ� �κ�
					
					if(rec.checkBw(usrarr[usridx].banwrd)) {
						
						window.display("[*]" + usrarr[usridx].name + "�� ��Ģ�� ���!");
						window.display("[*]" + Chat.bidx + "��° ���ڿ��� ��Ģ�� ���");
						System.out.println("[*]" + usrarr[usridx].name + "�� ��Ģ�� ���!");
						System.out.println("[*]" + Chat.bidx + "��° ���ڿ��� ��Ģ�� ���");
						
						usrarr[usridx].alive = false;
						
						ChatApp.hostNotify(socket, usrarr, ("[*]" + usrarr[usridx].name + "�� ��Ģ�� ���!"));
						ChatApp.hostNotify(socket, usrarr, ("[*]" + Chat.bidx + "��° ���ڿ��� ��Ģ�� ���"));
						
						/*
						byte[] dout = (PacketHeader.HOS_NOF + usrarr[usridx].name + "�� ��Ģ�� ���!").getBytes();
						for (int i = 1; i < User.current; i++) {
							DatagramPacket notifyPacket = new DatagramPacket(dout, dout.length, usrarr[i].ip, usrarr[i].port);
							
							try {
								socket.send(notifyPacket);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						}
						*/
						
						String tmp = "";
						
						for (int i = 0; i < User.current; i++) {
							if(usrarr[i].alive)
								tmp = tmp + usrarr[i].name + ", ";
						}
						
						window.display("[*]���� ������ : " + tmp);
						System.out.println("[*]���� ������ : " + tmp);
						ChatApp.hostNotify(socket, usrarr, ("[*]���� ������ : " + tmp));
						
						
						/*
						dout = (PacketHeader.HOS_NOF + "���� ������ : " + tmp).getBytes();
						for (int i = 1; i < User.current; i++) {
							DatagramPacket notifyPacket = new DatagramPacket(dout, dout.length, usrarr[i].ip, usrarr[i].port);
							
							try {
								socket.send(notifyPacket);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						}
						*/
						
					}
					
					//�ٸ� �����鿡�� �߰��ϴ� �κ�
					for (int i = 1; i < User.current; i++) {
						if (i != usridx) {
							byte[] out= rec.getHBytes();
							DatagramPacket relPacket = new DatagramPacket(out, out.length, usrarr[i].ip, usrarr[i].port);
							
							try {
								socket.send(relPacket);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					window.display(rec.displayString());
					rec.display();
				}
				
			} else {
					window.display("[*]" + inPacket.getAddress().toString() + " ���� ���� " + datastr + "�� ���õ�");
					System.out.println("[*]" + inPacket.getAddress().toString() + " ���� ���� " + datastr + "�� ���õ�");
					}
		}
		
	}
}
