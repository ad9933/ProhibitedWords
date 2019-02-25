package simpleChat;

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Chat {
	
	public Chat(String msg, String sender) {
		user = sender;
		data = msg;
		SimpleDateFormat sdf = new SimpleDateFormat("[hh:mm:ss]");	//10개 문자
		time = sdf.format(new Date());
	}
	
	Chat(String data) {
		int i = 11;
		
		time = data.substring(0, 10);
		
		while (data.charAt(i) != ':')
			i++;
		user = data.substring(11, i - 1);
		this.data = data.substring(i + 2);
	}
	
	public byte[] getBytes() {							//9		10		11
		return (new String(PacketHeader.CLI_CHT + time + " " + user + " : " + data)).getBytes();
	}	//								0		10		11		12
	
	public byte[] getHBytes() {
		return (new String(PacketHeader.HOS_CHT + time + " " + user + " : " + data)).getBytes();
	}
	
	void display() {
		System.out.println(time + " " + user + " : " + data);
	}
	
	public String displayString() {
		return (time + " " + user + " : " + data);
	}
	
	public boolean checkBw(String banw) {		//bidx에 몇번째 문자에 금칙어가 있는지를 저장해줌.
		
		bidx = 0;
		String input = data;
		
		int bstrlen = banw.length();
		int istrlen = input.length();
		int idx = 0;
		
		if (bstrlen > istrlen)
			return false;
		if (bstrlen == istrlen) {
			if (input.equals(banw))
				return true;
			else
				return false;
		}
		
		for (int i = 0; i < (istrlen - bstrlen + 1); i++) {
			for (int j = 0; j < bstrlen; j++) {
				if (input.charAt(i + j) == banw.charAt(j))
					idx++;
			}
			
			if (idx != bstrlen)
				idx = 0;
			else {
				bidx = i;
				break;
			}
		}
		
		if (idx == bstrlen)
			return true;
		else
			return false;
	}
	
	public static int bidx;
	
	public String data;
	String time;
	public String user;
	
}
