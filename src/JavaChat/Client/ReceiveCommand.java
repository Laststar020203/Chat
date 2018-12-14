package JavaChat.Client;

import java.awt.SecondaryLoop;
import java.util.StringTokenizer;



enum FixInfo {PREFIX , NICKNAME};

public class ReceiveCommand {

	Main client;
	
	
	public ReceiveCommand(Main main) {
		// TODO Auto-generated constructor stub
		this.client = main;
		
	}
	
	
	public void checkCommand(String message) {
		String action = message.substring(0, 1);
		try {
			switch (action) {
			case "&":
				StringTokenizer tokenizer = new StringTokenizer(message, "&");
				String comdline1 = tokenizer.nextToken();
				
				swithCommand(comdline1, tokenizer , message);
				
				break;

			default:
				
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			try {
				
			} catch (NullPointerException e2) {
				// TODO: handle exception
				
				message = "[����] Ŀ����� ����� �Էµ��� �ʾҽ��ϴ�!!";
			} catch (Exception e2) {
				
				e2.printStackTrace();
				message = "[����] �˼� ���� ����!!";
			}
		
		}
		
		
	}
	
	public void swithCommand(String comdline1 , StringTokenizer tokenizer , String message) throws Exception {
		
		
			String comdline2 = tokenizer.nextToken();
			String comdline3 = tokenizer.nextToken();
			//TextArea ���� �� ��� �����ϴ��� ���� ��� ����
			switch (comdline1) {
			case "0000":
				setInfo(comdline2, comdline3, FixInfo.NICKNAME);
				message = "[�����˸�] ����Ǿ����ϴ�!";
				break;
			case "0001":
				setInfo(comdline2, comdline3, FixInfo.PREFIX);
				message = "[�����˸�] ����Ǿ����ϴ�!";
				break;

			default:
				break;
			}
		
		
	}
	
	public void setInfo(String name, String color , FixInfo info) throws Exception{
		//color�� TextArea ���� �� ��� �����ϴ��� ���� ��� ����
		switch(info) {
		case NICKNAME:		
			
			client.nickName = name;
			break;
		case PREFIX:
			
			client.prefix = name;
		
			break;
		}
		
	}
		
}
