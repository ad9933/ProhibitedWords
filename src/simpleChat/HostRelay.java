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
				
				//몇번 유저인지를 알아내는 부분
				for (User u : usrarr) {
					if (u.name.equals(rec.user) && u.ip.equals(inPacket.getAddress()))
						break;
					else
						usridx++;
				}
				
				if (usridx > User.current) {
					window.display("[*]" + inPacket.getAddress().toString() + " 에서 보낸 " + datastr + "가 무시됨");
					System.out.println("[*]" + inPacket.getAddress().toString() + " 에서 보낸 " + datastr + "가 무시됨");
				} else {
					
					//채팅에 금칙어가 들어있는지를 확인하는 부분
					
					if(rec.checkBw(usrarr[usridx].banwrd)) {
						
						window.display("[*]" + usrarr[usridx].name + "의 금칙어 사용!");
						window.display("[*]" + Chat.bidx + "번째 문자에서 금칙어 사용");
						System.out.println("[*]" + usrarr[usridx].name + "의 금칙어 사용!");
						System.out.println("[*]" + Chat.bidx + "번째 문자에서 금칙어 사용");
						
						usrarr[usridx].alive = false;
						
						ChatApp.hostNotify(socket, usrarr, ("[*]" + usrarr[usridx].name + "의 금칙어 사용!"));
						ChatApp.hostNotify(socket, usrarr, ("[*]" + Chat.bidx + "번째 문자에서 금칙어 사용"));
						
						/*
						byte[] dout = (PacketHeader.HOS_NOF + usrarr[usridx].name + "의 금칙어 사용!").getBytes();
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
						
						window.display("[*]남은 생존자 : " + tmp);
						System.out.println("[*]남은 생존자 : " + tmp);
						ChatApp.hostNotify(socket, usrarr, ("[*]남은 생존자 : " + tmp));
						
						
						/*
						dout = (PacketHeader.HOS_NOF + "남은 생존자 : " + tmp).getBytes();
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
					
					//다른 유저들에게 중계하는 부분
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
					window.display("[*]" + inPacket.getAddress().toString() + " 에서 보낸 " + datastr + "가 무시됨");
					System.out.println("[*]" + inPacket.getAddress().toString() + " 에서 보낸 " + datastr + "가 무시됨");
					}
		}
		
	}
}
