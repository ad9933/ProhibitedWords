package simpleChat;

import java.util.*;
import java.net.*;
import java.io.*;

public class User {
	
	public static int current = 0;
	
	User(String usrnme) {
		name = usrnme;
		try {
			ip = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		alive = true;
		current++;
	}
	
	User(String usrnme, InetAddress addr, int portnum) {
		name = usrnme;
		ip = addr;
		port = portnum;
		alive = true;
		current++;
	}
	
	void display() {
		System.out.println(ip.toString()+ "\t\t\t" + name);
	}
	
	void displayBW() {
		System.out.println(name + "의 금칙어 : " + banwrd);
	}
	
	public String name;
	public String banwrd;
	public InetAddress ip;
	public int port;
	public boolean alive;

}
