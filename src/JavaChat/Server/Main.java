package JavaChat.Server;
	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();
	public static Map<String, DataOutputStream> users = new HashMap<String, DataOutputStream>();
	public static Map<String, Integer> userScores = new HashMap<String , Integer>();
	public static boolean isVoteTime;
	public static int yes = 0, no = 0;
	
	//DataOutputStream�� �Է��� ������ ���������� ����ڿ��� ������ ���ؼ�
	
	ServerSocket serverSocket;
	
	void startServer(String IP , int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if(!serverSocket.isClosed()) {
				stopServer();
			}
		}
		
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Socket socket = serverSocket.accept();
						DataInputStream in = new DataInputStream(socket.getInputStream());
						DataOutputStream out = new DataOutputStream(socket.getOutputStream());
						String nickname = in.readUTF();
						
						users.put(nickname, out);
						userScores.put(nickname, 0);
						
						clients.add(new Client(socket , nickname));
						System.out.println("[Ŭ���̾�Ʈ ����]"+
											socket.getRemoteSocketAddress()+
											Thread.currentThread().getName());
						
					} catch (Exception e) {
						// TODO: handle exception
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
		
		
	}
	
	void stopServer() {
		try {
			Iterator<Client> iterator = clients.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
