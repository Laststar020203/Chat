package JavaChat.Server;

import java.util.Iterator;
import java.util.StringTokenizer;

public class Command {

	private Command() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public static void switchCommand(String comdline1 , StringTokenizer tokenizer , String serverSendMessage) throws Exception{
		
		
		
		switch (comdline1.toLowerCase()) {
		case "help":
			Command.helpAction(serverSendMessage);
			
		case "setPrefix":
			
			Command.setInfo(Info.PREFIX, tokenizer , serverSendMessage);
				
			break;
		case "setnickname":
			
			Command.setInfo(Info.NICKNAME, tokenizer , serverSendMessage);
			
			break;
		case "players":
			
			serverSendMessage = Main.clients.size()+"";
			
			break;
		case "playerlist":
			
			Command.playerListInfo(serverSendMessage);
			
			break;
		case "vote":
			Command.voteAction(tokenizer);
			break;
		case "luckey":
			
			break;
		case "rank":
			break;
		case "y":
			if(!Main.isVoteTime) return;
			Main.yes++;
			break;
		case "n":
			if(!Main.isVoteTime) return;
			Main.no++;
			break;
		default:
			break;
		}
		

	}
	
	
	public static void helpAction(String message) {
		final String helpMessage = 
				"/help ��ɾ���� ������ �� �� �ֽ��ϴ�"+
				"/setprefix (�ٲ��̸�) (����) Īȣ�� �����Ͻ� �� �ֽ��ϴ�."+
				"/setnickname (�ٲ��̸�) (����) �г����� �����Ͻ� �� �ֽ��ϴ�."+
				"/players ���� ä�ù濡 �ִ� �ο� ���� Ȯ���� �� �ֽ��ϴ�."+
				"/playerlist ���� ä�ù濡 �ִ� �������� ������ Ȯ���� �� �ֽ��ϴ�"+
				"/vote (��ǥ����) ä�ù濡�� ��ǥ�� �����Ҽ� �ֽ��ϴ�."+
				"/luckey ������ �����մϴ�."+
				"/rank �ڽ��� ���� ��ŷ�� �� �� �ֽ��ϴ�.";
		message = helpMessage;
	}
	
	public static void setInfo(Info info , StringTokenizer tokenizer , String message) {
		String comdline2 = tokenizer.nextToken();
		String comdline3 = tokenizer.nextToken();
		switch (info) {
		case NICKNAME:
		message = "$0000"+"&"+comdline2+"&"+comdline3;
			
		case PREFIX:
		message =  "$0001"+"&"+comdline2+"&"+comdline3;
	
		}
		
		
	}
	public static void playerListInfo(String message) {
		String nameslist = "";
		Iterator<String> nickNames = Main.users.keySet().iterator();
		while (nickNames.hasNext()) {
			nameslist += nickNames.next() + ",";
		}
		message = nameslist;
	}
	
	
	
	public static void voteAction(StringTokenizer tokenizer) {
		if(Main.isVoteTime) {
			try {
				Client.broadcastMessage("���� ��ǥ�� ��û���ּ���");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		String comdline2 = tokenizer.nextToken();
		Client.broadcastMessage("��ǥ�� ���۵Ǿ����ϴ� !! \n ����:"
				+comdline2+"\n"
				+"������ \\y �ݴ�� \\n �Է����ּ���!");
		Main.yes = 0;
		Main.no = 0;
		
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					int yes = Main.yes;
					int no = Main.no;
					if(yes + no >= Main.clients.size()) {
						Client.broadcastMessage("[��ǥ ���]\n"+
									"����:"+yes+"  �ݴ� :"+no+"\n"+
									(yes == no) != null ? "���� �Դϴ�!" : (yes > no) ? "���� �¸�!" : "�ݴ� �¸�!");
					}
					break;
				}
			}
		};
		Main.threadPool.submit(thread);
	
	}	

}
