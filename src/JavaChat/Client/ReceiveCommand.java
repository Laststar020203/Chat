package JavaChat.Client;

import java.awt.SecondaryLoop;
import java.util.StringTokenizer;

enum RecieveMessageType {
	SERVER, CHAT
};

public class ReceiveCommand {

	Main client;

	public ReceiveCommand(Main main) {
		// TODO Auto-generated constructor stub
		this.client = main;

	}

	public String checkCommand(String message) {
	
		String action = message.substring(0, 1);
		String fixMessage = message.substring(1);
		
		try {
			switch (action) {
			case "&":

				return "[�����˸�]" + fixMessage + "\n";

			case "#":

				return "[����]" + fixMessage + "\n";

			default:

				return message + "\n";

			}

			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "[����] ���ŵ� ��ɾ �м��ϴ� �������� �˼� ���� ������ �߻��Ͽ����ϴ� \n";

		}
		/*
		 * if(client.prefix.equals("")) return client.nickName + " : "+message; else
		 * return "["+client.prefix+"] "+client.nickName + " : "+message;
		 */
	}

}
