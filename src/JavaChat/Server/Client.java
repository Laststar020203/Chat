package JavaChat.Server;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.BrokenBarrierException;



enum Info {PREFIX , NICKNAME};

public class Client {
	
	Socket socket;
	String nickName;

	
	
	public Client(Socket socket , String nickName) {
		this.socket = socket;
		this.nickName = nickName;
		
		receive();
	}

	private void receive() {
		// TODO Auto-generated method stub
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						DataInputStream in = new DataInputStream(socket.getInputStream());
						byte[] buffer = new byte[512];
						
						char action = (char)in.read();
						
						int length = in.read(buffer);
						if(length == -1) new IOException();
						
						System.out.println("[�޽��� ���� �Ϸ�]"+
								socket.getRemoteSocketAddress() +
								Thread.currentThread().getName());

						String mesage = new String(buffer , 0 , length , "UTF-8");
						
						if(action == '/') command(mesage);
						else sendMessage(action + mesage);
						
						/*
						String messege = new String(buffer, 0, length, "UTF-8");
						for (Client client : Main.clients) {
							client.send
						}
						*/
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		};
		Main.threadPool.submit(thread);
	}
	
	private void command(String command) {
		StringTokenizer tokenizer = new StringTokenizer(command, " ");
		String serverSendMessage = "";
		try {	
			String comdline1 = tokenizer.nextToken();
			
			switch (comdline1.toLowerCase()) {
			case "help":
				serverSendMessage = helpAction();
				
			case "setPrefix":
				
				serverSendMessage = setInfo(Info.PREFIX, tokenizer);
					
				break;
			case "setnickname":
				
				serverSendMessage = setInfo(Info.NICKNAME, tokenizer);
				
				break;
			case "players":
				
				serverSendMessage = Main.clients.size()+"";
				
				break;
			case "playerlist":
				
				serverSendMessage = playerListInfo();
				
				break;
			case "vote":
				voteAction(tokenizer);
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
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			try {
				
			} catch (NullPointerException e2) {
				// TODO: handle exception
			} catch (Exception e3) {
				
			}
		}
		
		
		
	}
	
	
	private void sendMessage(String message) {
		
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				
				// TODO Auto-generated method stub
				byte[] buffer = new byte[512];
				
				try {
					Main.users.get(nickName).writeUTF(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Main.threadPool.submit(thread);
		
	}
	
	private void broadcastMessage(String message){
		
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Iterator<String> i = Main.users.keySet().iterator();
				while(i.hasNext()) {
					try {
						Main.users.get(i).writeUTF(message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread);
	
	}
	
	
	
	private String helpAction() {
		final String helpMessage = 
				"/help ��ɾ���� ������ �� �� �ֽ��ϴ�"+
				"/setprefix (�ٲ��̸�) (����) Īȣ�� �����Ͻ� �� �ֽ��ϴ�."+
				"/setnickname (�ٲ��̸�) (����) �г����� �����Ͻ� �� �ֽ��ϴ�."+
				"/players ���� ä�ù濡 �ִ� �ο� ���� Ȯ���� �� �ֽ��ϴ�."+
				"/playerlist ���� ä�ù濡 �ִ� �������� ������ Ȯ���� �� �ֽ��ϴ�"+
				"/vote (��ǥ����) ä�ù濡�� ��ǥ�� �����Ҽ� �ֽ��ϴ�."+
				"/luckey ������ �����մϴ�."+
				"/rank �ڽ��� ���� ��ŷ�� �� �� �ֽ��ϴ�.";
		return helpMessage;
	}
	
	private String setInfo(Info info , StringTokenizer tokenizer) {
		String comdline2 = tokenizer.nextToken();
		String comdline3 = tokenizer.nextToken();
		switch (info) {
		case NICKNAME:
		return "$0000"+"&"+comdline2+"&"+comdline3;
			
		case PREFIX:
		return "$0001"+"&"+comdline2+"&"+comdline3;
	
		}
		return null;
		
	}
	private String playerListInfo() {
		String nameslist = "";
		Iterator<String> nickNames = Main.users.keySet().iterator();
		while (nickNames.hasNext()) {
			nameslist += nickNames.next() + ",";
		}
		return nameslist;
	}
	
	
	
	private void voteAction(StringTokenizer tokenizer) {
		if(Main.isVoteTime) {
			try {
				broadcastMessage("���� ��ǥ�� ��û���ּ���");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		String comdline2 = tokenizer.nextToken();
		broadcastMessage("��ǥ�� ���۵Ǿ����ϴ� !! \n ����:"
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
						broadcastMessage("[��ǥ ���]\n"+
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
