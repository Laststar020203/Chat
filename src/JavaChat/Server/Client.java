package JavaChat.Server;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.BrokenBarrierException;

import javax.xml.ws.handler.MessageContext;

enum SendMessageType {
	SERVER, CHAT, ERROR
};

public class Client {

	Socket socket;
	String nickName = "";
	String prefix = "";
	String serverSendMessage = "";
	Command command;

	public Client(Socket socket, String nickName) {
		this.socket = socket;
		this.nickName = nickName;
		command = new Command(this);

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

						System.out.println(
								"[메시지 수신 완료]" + socket.getRemoteSocketAddress() + Thread.currentThread().getName());

						String mesage = in.readUTF();
						String action = mesage.substring(0, 1);

						if (action.equals("/"))
							command(mesage.substring(1));
						else {
							if (prefix.equals(""))
								broadcastMessage(nickName + " : " + mesage, SendMessageType.CHAT);
							else
								broadcastMessage("[" + prefix + "] " + nickName + " : " + mesage, SendMessageType.CHAT);
						}
						/*
						 * String messege = new String(buffer, 0, length, "UTF-8"); for (Client client :
						 * Main.clients) { client.send }
						 */
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		};
		Main.threadPool.submit(thread);
	}

	private void command(String command) {
		StringTokenizer tokenizer = new StringTokenizer(command, " ");

		try {
			String comdline1 = tokenizer.nextToken();

			switch (this.command.switchCommand(comdline1, tokenizer, serverSendMessage)) {
			case BROADCAST:

				broadcastMessage(serverSendMessage, SendMessageType.SERVER);
				break;
			case PERSONAL:

				personalMessage(serverSendMessage, SendMessageType.SERVER);
				break;
			case NONE:

				personalMessage(serverSendMessage, SendMessageType.ERROR);
				break;
			}

			// sendMessage(serverSendMessage);
		} catch (NoSuchElementException nse) {
			
			personalMessage("명령어가 제대로 입력되지 않았습니다", SendMessageType.ERROR);
		} catch (Exception e) {
			
			e.printStackTrace();
			personalMessage("송신할 명령어를 분석하는 과정에서 알수없는 오류가 발생하였습니다", SendMessageType.ERROR);
		}
	}

	private void personalMessage(String message, SendMessageType type) {

		Runnable thread = new Runnable() {

			@Override
			public void run() {

				// TODO Auto-generated method stub
				byte[] buffer = new byte[512];

				try {
					switch (type) {
					case SERVER:
						Main.users.get(nickName).writeUTF("&" + message);
						break;
					case ERROR:
						Main.users.get(nickName).writeUTF("#" + message);
						break;
					}
					Main.users.get(nickName).flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Main.threadPool.submit(thread);

	}

	public void broadcastMessage(String message, SendMessageType type) {

		Runnable thread = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Iterator<String> i = Main.users.keySet().iterator();
				while (i.hasNext()) {
					try {
						String key = i.next();

						switch (type) {
						case SERVER:
							Main.users.get(key).writeUTF("&" + message);
							break;

						case CHAT:
							Main.users.get(key).writeUTF(message);
							break;
						case ERROR:
							Main.users.get(key).writeUTF("#" + message);
							break;
						}

						Main.users.get(key).flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread);

	}

}
