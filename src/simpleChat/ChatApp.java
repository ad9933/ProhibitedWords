package simpleChat;

import java.util.*;
import java.net.*;
import java.io.*;

public class ChatApp {
	
	static final int CHATBUFF = 30;
	static final int MAX_USR = 10;
	static final int MAX_BYTE = 200;
	
	public static boolean isGUI = true;
	public static boolean debugMode = false;
	
	public User[] usrarr = new User[MAX_USR];
	Chat[] chatBuffer = new Chat[CHATBUFF];
	static Scanner scanner = new Scanner(System.in);
	public String myname = null;
	int cbLen = 0;
	int usrlen;
	
	static String getMessage(String str) {
		int i = 0;
		
		while (str.charAt(i) != 0) {
			i++;
		}
		
		return str.substring(1, i);
	}
	
	static String getInput() {					//�Է��� refresh �� �Ұ�		
		String input = scanner.nextLine();
		
		return input;
	}
	
	public static void hostNotify(DatagramSocket socket, User[] usrarr, String msg) {
		byte[] data = (PacketHeader.HOS_NOF + msg).getBytes();
		
		for (int i = 1; i < User.current; i++) {
			DatagramPacket notify = new DatagramPacket(data, data.length, usrarr[i].ip, usrarr[i].port);
			
			try {
				socket.send(notify);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void f_dispusr() {
		System.out.println("[*]�ٸ���� ��ٸ����� ...");
		System.out.println("IP\t\t\t\t�̸�");
		
		for (int i = 0; i < User.current; i++) {
			usrarr[i].display();
		}
		
		for (int i = 0; i < CHATBUFF - (3 + User.current); i++)
			System.out.println();
		
	}
	
	void f_refresh() {
		for (int i = 0; i < (CHATBUFF + 1); i++)
			System.out.println();
	}
	
	void refresh() {
		for (int i = 0; i < 30; i++) 
			chatBuffer[i].display();
		
		System.out.print("�Է� >>");
		
	}
	
	void hSendChat(DatagramSocket socket) {
		DatagramPacket outPacket = null;
		byte msg[] = new byte[MAX_BYTE];
		Chat hcht = null;
		
		System.out.print(">>");
		hcht = new Chat(scanner.nextLine(), usrarr[0].name);
		
		if(hcht.checkBw(usrarr[0].banwrd)) {
			System.out.println("[*]" + usrarr[0].name + "�� ��Ģ�� ���!");
			System.out.println("[*]" + Chat.bidx + "��° ���ڿ��� ��Ģ�� ���");
			
			hostNotify(socket, usrarr, ("[*]" + usrarr[0].name + "�� ��Ģ�� ���!"));
			hostNotify(socket, usrarr, ("[*]" + Chat.bidx + "��° ���ڿ��� ��Ģ�� ���"));
			
			usrarr[0].alive = false;
		}
		
		msg = hcht.getHBytes();
		for (int i = 1; i < User.current; i++) {
			outPacket = new DatagramPacket(msg, msg.length, usrarr[i].ip, usrarr[i].port);
			try {
				socket.send(outPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	void relChat(DatagramSocket socket) {
		byte msg[] = new byte[MAX_BYTE];
		int usridx = 0;
		DatagramPacket inPacket = new DatagramPacket(msg, msg.length);
		Chat rec;
		
		try {
			socket.receive(inPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String datastr = new String(inPacket.getData());
		
		
		if (datastr.charAt(0) == PacketHeader.CLI_CHT) {
			rec = new Chat(getMessage(datastr));
			
			for (User u : usrarr) {
				if (u.name.equals(rec.user) && u.ip.equals(inPacket.getAddress()))
					break;
				else
					usridx++;
			}
			
			if (usridx > User.current) {
				System.out.println("[*]" + inPacket.getAddress().toString() + " ���� ���� " + datastr + "�� ���õ�");
				return;
			} else {
				for (int i = 1; i < User.current; i++) {
					if (i != usridx) {
						byte[] out= rec.getHBytes();
						DatagramPacket relPacket = new DatagramPacket(out, out.length, usrarr[i].ip, usrarr[i].port);
						
						try {
							socket.send(relPacket);
						} catch (IOException e) {
							e.printStackTrace();
						}
						rec.display();
						
					} else
						rec.display();
				}
			}
			
		} else {
			System.out.println("[*]" + inPacket.getAddress().toString() + " ���� ���� " + datastr + "�� ���õ�");
		}
		
	}
	
	void getBw(DatagramSocket socket) {
		byte msg[] = new byte[MAX_BYTE];
		int usridx = 1;
		boolean gotIP = false;
		String str;
		DatagramPacket inPacket = new DatagramPacket(msg, msg.length);
		
		while (usridx != User.current) {
			
			gotIP = false;
			
			try {
				socket.receive(inPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			str = new String(inPacket.getData());
			
			if (str.charAt(0) != PacketHeader.CLI_BWR) {
				System.out.println("[*]��Ģ�� �޴µ��� �����߻�!");
				System.out.println("[*]ip " + inPacket.getAddress().toString() + " ���� ����");
				System.out.println("[*]��Ŷ  : " + str);
				System.exit(1);
			}
			
			for (int i = 1; i < User.current; i++) {
				if (inPacket.getAddress().equals(usrarr[i].ip)) {
					if (i == User.current - 1) {
						usrarr[0].banwrd = getMessage(str);
						gotIP = true;
						usridx++;
					} else {
						usrarr[i + 1].banwrd = getMessage(str);
						gotIP = true;
						usridx++;
					}
				}
			}
			
			if (!gotIP) {
				System.out.println("[*]ip " + inPacket.getAddress().toString() + " ���� ����");
				System.out.println("[*]��Ŷ  : " + str + " �� ���õ�");
			}
			
		}
		
		System.out.println("\n[*]��Ģ�� ��� �Ϸ�");
		
	}
	
	void addUsr(DatagramSocket socket) {
		byte[] msg = new byte[MAX_BYTE];
		String res = new String("" + PacketHeader.HOS_REG);
		
		DatagramPacket inPacket = new DatagramPacket(msg, msg.length);
		
		try {
			socket.receive(inPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if ((new String(msg)).charAt(0) == PacketHeader.CLI_REG)
		{
			usrarr[User.current] = new User(getMessage(new String(inPacket.getData())), inPacket.getAddress(), inPacket.getPort());
			DatagramPacket response = new DatagramPacket(res.getBytes(), res.getBytes().length, inPacket.getAddress(), inPacket.getPort());
			try {
				socket.send(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void sendStartp(DatagramSocket socket) {
		String msgstr = new String(PacketHeader.HOS_SRT + "");
		byte[] msg = msgstr.getBytes();
		
		DatagramPacket stPacket;
		
		for (int i = 1; i < User.current; i++) {
			stPacket = new DatagramPacket(msg, msg.length, usrarr[i].ip, usrarr[i].port);
			try {
				socket.send(stPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void reqBw(DatagramSocket socket) {
		String msgstr;
		byte[] msg;
		DatagramPacket reqPacket;
		
		for (int i = 1; i < User.current; i++) {
			
			if (i != (User.current - 1)) {
				msgstr = PacketHeader.HOS_BWR + usrarr[i + 1].name;
				msg = msgstr.getBytes();
				
				reqPacket = new DatagramPacket(msg, msg.length, usrarr[i].ip, usrarr[i].port);
				try {
					socket.send(reqPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				msgstr = "" + PacketHeader.HOS_BWR + usrarr[0].name;
				msg = msgstr.getBytes();
				
				reqPacket = new DatagramPacket(msg, msg.length, usrarr[i].ip, usrarr[i].port);
				try {
					socket.send(reqPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	//if else ��
		}
		
	}
	
	//Ŭ���̾�Ʈ �޼���
	
	void recChat(DatagramSocket socket) {
		DatagramPacket chatPacket;
		Chat inChat;
		byte[] data = new byte[MAX_BYTE];
		
		chatPacket = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(chatPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String datastr = new String(data);
		
		if (datastr.charAt(0) == PacketHeader.HOS_CHT) {
			inChat = new Chat(getMessage(datastr));
			inChat.display();
		}
		
	}
	
	void sendChat(DatagramSocket socket, InetAddress server) {
		DatagramPacket chatPacket = null;
		byte[] data = new byte[MAX_BYTE];
		
		System.out.print(">>");
		String input = getInput();
		
		Chat current = new Chat(input, myname);
		data = current.getBytes();
		chatPacket = new DatagramPacket(data, data.length, server, 7777);
		
		try {
			socket.send(chatPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		current.display();
	}
	
	void sendBw(DatagramSocket socket, InetAddress server) {
		byte[] msg = new byte[MAX_BYTE];
		int i = 0;
		String msgstr;
		String bw;
		
		DatagramPacket inPacket, outPacket;
		
		inPacket = new DatagramPacket(msg, msg.length);
		try {
			socket.receive(inPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		msgstr = new String(inPacket.getData());
		if (!(msgstr.charAt(0) == PacketHeader.HOS_BWR)) {
			System.out.println("[*]��Ģ�� �������� �����߻�");
			System.exit(1);
		}
			
		
		String usrnme = "";
		
		while (msgstr.substring(1).charAt(i) != 0) {
			usrnme += msgstr.substring(1).charAt(i);
			i++;
		}
		
		System.out.print(usrnme + " �� ��Ģ� �Է��Ͻʽÿ� >>");
		bw = getInput();
		msgstr = PacketHeader.CLI_BWR + bw;
		
		msg = msgstr.getBytes();
		outPacket = new DatagramPacket(msg, msg.length, server, 7777);
		
		try {
			socket.send(outPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	boolean register(DatagramSocket socket, InetAddress server) {
		System.out.print("�̸� �Է�>>");
		String name = getInput();
		myname = name;
		
		name = PacketHeader.CLI_REG + name;
		
		byte[] namearr = name.getBytes();
		byte[] res = new byte[MAX_BYTE];
		
		DatagramPacket namePacket = new DatagramPacket(namearr, namearr.length, server, 7777);
		DatagramPacket resPacket = new DatagramPacket(res, res.length);
		
		try {
			socket.send(namePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			socket.receive(resPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String respstr = new String(resPacket.getData());
		
		if (respstr.charAt(0) == PacketHeader.HOS_REG)
			return true;
		else return false;
		
	}
	
	void wait_forplay(DatagramSocket socket) {
		byte[] msg = new byte[MAX_BYTE];
		boolean wait = true;
		
		DatagramPacket hosrply = new DatagramPacket(msg, msg.length);
		
		while (wait) {
			try {
				socket.receive(hosrply);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (((new String(hosrply.getData())).charAt(0)) == PacketHeader.HOS_SRT) {
				wait = false;
			}
			
		}
		
		
	}
	
	void client() {
		DatagramSocket socket = null;
		InetAddress serveraddr = null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		System.out.print("�����ּ� �Է�>>");
		try {
		serveraddr = InetAddress.getByName(getInput());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if (register(socket, serveraddr)) {
			System.out.println("[*]��ϼ���. �ٸ������ ��ٸ����� ...");
		} else {
			System.out.println("[*]��Ͻ���... �����߻�");
			System.exit(1);
		}
		
		wait_forplay(socket);
		System.out.println("[*]�ο� ��� ����");
		
		sendBw(socket, serveraddr);
		
		System.out.println("\n[*]��Ģ�� ������ ��ٸ�����...");
		wait_forplay(socket);
		System.out.println("\n[*]��Ģ�� ��� ��");
		
			ui.MainFrame window = new ui.MainFrame(socket, serveraddr, myname);
		
		ClientListen listenThread = new ClientListen(socket, serveraddr, window);
		listenThread.start();
		
		if(!isGUI) {
			while(true) {
				sendChat(socket, serveraddr);
			}
		}
		
		
	}
	
	void host() throws IOException {
		DatagramSocket socket = new DatagramSocket(7777);
		
		System.out.print("���� �̸� �Է� >>");
		String myName = getInput();
		usrarr[0] = new User(myName);
		
		System.out.print("�ο� �Է� >>");
		usrlen = Integer.parseInt(getInput());
		
		while (usrlen > User.current) {
			f_dispusr();
			System.out.println("[*]" + User.current + "��° ���� ��ٸ��� ��...");
			addUsr(socket);
			System.out.println("[*]" + User.current + "��° ���� ��� ����");
		}
		
			f_dispusr();
			sendStartp(socket);
			System.out.println("[*]��Ģ�� �Է½���.");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			reqBw(socket);
			
			//��Ģ�� ����
			getBw(socket);
			System.out.print(usrarr[1].name + "�� ��Ģ�� �Է� >>");
			usrarr[1].banwrd = getInput();
			//��Ģ�� ���� ��
			
			
			
			//��Ģ�� ������ �޾ҳ� üũ��. ����� �ɼ�
			if (debugMode) {
				for (int i = 0; i < User.current; i++)
					usrarr[i].displayBW();
				
			}
			//---����׿� ��---
			
			sendStartp(socket);
			
			//����� �ɼ�
			if (debugMode) { ChatApp.hostNotify(socket, usrarr, "[*]����� ��� - ��Ģ�� ��� ǥ�õ�"); }
			//---����� �ɼ�---
			
			//TODO: CLI��嵵 �����
			if (isGUI) {
				hui.MainFrame window = new hui.MainFrame(this, socket);
				
				HostRelay relayThread = new HostRelay(socket, usrarr, window);
				relayThread.start();
				
			} else {
				while (true)
					hSendChat(socket);
			}
			
	}

	public static void main(String[] args) {
		ChatApp ca = new ChatApp();
		
		for (int i = 0; i < args.length; i++) {
			
			if(args[i].equals("-debug"))
				ChatApp.debugMode = true;
			
		}
		
		//TODO: GUI������� CLI������� Ȯ���ϴ� ���� ������
		
		System.out.println("=====Honey's ��Ģ�����=====");
		System.out.println("=====�ɼ��� �����Ͻʽÿ�=====");
		System.out.println("1. ����");
		System.out.println("2. Ŭ���̾�Ʈ");
		System.out.print("�Է� >>");
		int sel = 0;
		
		try {
			sel = Integer.parseInt(scanner.nextLine());
		} catch (InputMismatchException e) {
			System.out.println("[*]�߸��� �Է�. �����ٶ�.");
			System.exit(1);
		}
		
		if (sel == 1) {		//���� ��

			try {
				ca.host();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		} else if(sel ==2) {	//Ŭ���̾�Ʈ ��
			
			ca.client();
			
		} else {
			System.out.println("[*]�߸��� �Է�. �����ٶ�.");
		}
		
		scanner.close();

	}

}
