package simpleChat;

public class PacketHeader {
	final static char CLI_REG = 'r';	//클라이언트가 등록할때 보내는 헤더
	final static char HOS_REG = 'R';	//클라이언트 등록에 대한 응답 - 성공

	final static char HOS_SRT = 'S';	//모든 인원 모인후 시작할때 호스트가 보내는 패킷
	
	final static char HOS_BWR = 'W';	//호스트가 금칙어 보내라고 할때 보내는 패킷
	final static char CLI_BWR = 'w';	//클라이언트가 금칙어 보내는 패킷
	
	final static char CLI_CHT = 'c';	//클라이언트가 채팅을 보내는 패킷
	final static char HOS_CHT = 'C';	//호스트가 채팅을 보내는 패킷
	
	final static char HOS_NOF = 'N';	//호스트의 공지패킷
	
}
