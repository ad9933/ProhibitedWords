package simpleChat;

public class PacketHeader {
	final static char CLI_REG = 'r';	//Ŭ���̾�Ʈ�� ����Ҷ� ������ ���
	final static char HOS_REG = 'R';	//Ŭ���̾�Ʈ ��Ͽ� ���� ���� - ����

	final static char HOS_SRT = 'S';	//��� �ο� ������ �����Ҷ� ȣ��Ʈ�� ������ ��Ŷ
	
	final static char HOS_BWR = 'W';	//ȣ��Ʈ�� ��Ģ�� ������� �Ҷ� ������ ��Ŷ
	final static char CLI_BWR = 'w';	//Ŭ���̾�Ʈ�� ��Ģ�� ������ ��Ŷ
	
	final static char CLI_CHT = 'c';	//Ŭ���̾�Ʈ�� ä���� ������ ��Ŷ
	final static char HOS_CHT = 'C';	//ȣ��Ʈ�� ä���� ������ ��Ŷ
	
	final static char HOS_NOF = 'N';	//ȣ��Ʈ�� ������Ŷ
	
}
