package JavaChat.Client;

import java.awt.SecondaryLoop;
import java.util.StringTokenizer;

enum FixInfo {
	PREFIX, NICKNAME
};
enum RecieveMessageType{
	SERVER , CHAT
};

public class ReceiveCommand {

	Main client;

	public ReceiveCommand(Main main) {
		// TODO Auto-generated constructor stub
		this.client = main;

	}

	public RecieveMessageType checkCommand(String message) {
		String action = message.substring(0, 1);
		String fixMessage = message.substring(1);
		try {
			switch (action) {
			case "&":
				StringTokenizer tokenizer = new StringTokenizer(message, "&");
				String comdline1 = tokenizer.nextToken();
				swithCommand(comdline1, tokenizer, fixMessage);
				
				return RecieveMessageType.SERVER;
				
			case "#":
				
				client.sendMessage = "[����]" + message +"\n";
				
				return RecieveMessageType.SERVER;
				
			default:
				return RecieveMessageType.CHAT;
				

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			client.sendMessage = "[����] ���ŵ� ��ɾ �м��ϴ� �������� �˼� ���� ������ �߻��Ͽ����ϴ� \n";
			
			return RecieveMessageType.SERVER;

		}
		/*
		if(client.prefix.equals("")) return client.nickName + " : "+message;
		else return "["+client.prefix+"] "+client.nickName + " : "+message;
		 */
	}

	public void swithCommand(String comdline1, StringTokenizer tokenizer, String message) throws Exception {

		String comdline2;
		String comdline3;
		// TextArea ���� �� ��� �����ϴ��� ���� ��� ����
		switch (comdline1) {
		case "0000":
			 comdline2 = tokenizer.nextToken();
			 comdline3 = tokenizer.nextToken();
			setInfo(comdline2, comdline3, FixInfo.NICKNAME);
			client.sendMessage = "[�����˸�] ����Ǿ����ϴ�! \n";
			break;
		case "0001":
			 comdline2 = tokenizer.nextToken();
			 comdline3 = tokenizer.nextToken();
			setInfo(comdline2, comdline3, FixInfo.PREFIX);
			client.sendMessage = "[�����˸�] ����Ǿ����ϴ�! \n";
			break;

		default:
			client.sendMessage = "[�����˸�] " + message +"\n";
			break;
		}

	

	}

	public void setInfo(String name, String color, FixInfo info) throws Exception {
		// color�� TextArea ���� �� ��� �����ϴ��� ���� ��� ����
		switch (info) {
		case NICKNAME:

			client.nickName = name;
			break;
		case PREFIX:

			client.prefix = name;

			break;
		}

	}

}
