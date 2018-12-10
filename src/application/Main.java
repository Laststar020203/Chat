package application;
	
import java.awt.Button;
import java.awt.Font;
import java.awt.TextArea;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static ExecutorService threadPool; //�������� �����带 ȿ�������� �ٷ絵�� ���ִ� Ŭ����
	public static Vector<Client> clients = new Vector<Client>();
	
	ServerSocket serverSocket;
	
	//������ �������Ѽ� Ŭ���̾�Ʈ�� ������ ��ٸ��� �޼ҵ��Դϴ�.
	public void startServer(String IP, int port) {
		try {
		serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(IP, port));
		}catch (Exception e) {
		
			e.printStackTrace();
			if (!serverSocket.isClosed()) {
			 stopServer();
				
			}
			return;
			
		}
		//Ŭ���̾�Ʈ�� �����Ҽ� ���������� ��ٸ��� �������Դϴ�.
		Runnable thraed = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println("[Ŭ���̾�Ʈ ����]"
								+socket.getRemoteSocketAddress()
								+" : "+Thread.currentThread().getName());
					} catch (Exception e) {
						// TODO: handle exception
						if (!serverSocket.isClosed()) {
							stopServer();
							
						}
						break;
					}
				}
			}
		};
		threadPool = Executors.newCachedThreadPool(); //������ �ʱ�ȭ
		threadPool.submit(thraed);
		
		}
	// ������ �۵��� ������Ű�� �޼ҵ��Դϴ�.
	public void stopServer() {
		try {
			//���� �۵����� ��� ���� ����
			Iterator<Client> iterator = clients.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			//���� ���� �ݱ�
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			//������ Ǯ �����ϱ�\
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	//UI�� �����ϰ�, ���������� ���α׷��� ���۽�Ų�� �޼ҵ��Դϴ�.
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea();
		textArea.setEditable(false);
		textArea.setFont(new javafx.scene.text.Font("Serif", 15));
		root.setCenter(textArea);
		
		
		
		javafx.scene.control.Button toggleButton = new javafx.scene.control.Button();
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1,0,0,0));
		root.setBottom(toggleButton);
		
		String IP = "127.0.0.1";
		int port = 9876;
		
		toggleButton.setOnAction(event ->{
			if(toggleButton.getText().equals("�����ϱ�")) {
				startServer(IP, port);
				Platform.runLater(()->{
					String messege = String.format("[���� ����]\n", IP, port);
				});
			}
		});
		
		
		
	}
	
	//���α׷��� �������Դϴ�.
	public static void main(String[] args) {
		launch(args);
	}
}
