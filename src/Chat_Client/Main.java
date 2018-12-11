package Chat_Client;
	
import java.awt.Button;
import java.awt.TextField;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class Main extends Application {
	
	Socket socket;
	TextArea textArea;
	
	//Ŭ���̾�Ʈ ���� �޼ҵ� �Դϴ�.
	public void startClient(String IP , int port) {
		Thread thread = new Thread() {
			public void run() {
				
				try {
					socket = new Socket(IP, port);
					receive(); //�ʱ�ȭ�� �����κ��� ��� �޽����� �ޱ� ����
					
				} catch (Exception e) {
					// TODO: handle exception
					if(!socket.isClosed()) {
						stopClient();
						System.out.println("[���� ���� ����]");
						Platform.exit();
					}
				}
			};
		};
		thread.start();
	}
	//Ŭ���̾�Ʈ ���α׷� ���� �޼ҵ��Դϴ�.
	public void stopClient() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//�����κ��� �޽����� ���޹޴� �޼ҵ��Դϴ�.
	public void receive() {
		while (true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[512];
				int lengh = in.read(buffer);
				if(lengh == -1) throw new IOException();
				String meString = new String(buffer, 0, lengh, "UTF-8");
				Platform.runLater(()->{
					//System.out.println(meString);
					textArea.appendText(meString);
				});
			} catch (Exception e) {
				// TODO: handle exception
				stopClient();
				break;
			}
		}
	}
	//�����κ��� �޽����� �����ϴ� �޼ҵ��Դϴ�.
	public void send(String message) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
					
				} catch (Exception e) {
					// TODO: handle exception
					stopClient();
				}
			}
		};
		thread.start();
	}
	
	//������ ���α׷��� ���۽�Ű�� �޼ҵ� �Դϴ�.
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		HBox hBox = new HBox(); //Borderpene �ȿ� �ϳ��� ���̾ƿ��� �߰�
		hBox.setSpacing(5); //�ణ�� ������ �߰�
		
		javafx.scene.control.TextField userName = new javafx.scene.control.TextField();
		userName.setPrefWidth(150); //�ʺ�����
		userName.setPromptText("�г����� �Է��ϼ���"); //�� �޽����� ���̰Բ� ��
		HBox.setHgrow(userName, Priority.ALWAYS); //Hobx ���ο��� �ش� textfield�� ����� �� �ְԲ� �� �� �ƴ϶� �ʺ�ũ�� ���ߴ°Ŷ� ��������
		
		javafx.scene.control.TextField ipText = new javafx.scene.control.TextField("127.0.0.1");
		javafx.scene.control.TextField portText = new javafx.scene.control.TextField("9876");
		
		portText.setPrefWidth(80);
		
		hBox.getChildren().addAll(userName , ipText , portText); //hBox ���ο� �� ���� �ؽ�Ʈ�ʵ尡 �߰��ɼ� �ֵ��� ��
		root.setTop(hBox);
		
		textArea = new TextArea();
		textArea.setEditable(false); //textAreat�� �����ɼ� ���� ��
		root.setCenter(textArea);
		
		javafx.scene.control.TextField input = new javafx.scene.control.TextField();
		input.setPrefWidth(Double.MAX_VALUE);
		input.setDisable(true); //���� �ϱ� �������� �޽����� �����Ҽ� ������ ��
		input.setOnAction(event ->{
			send(userName.getText() + ": "+ input.getText() + "\n" );
			input.setText("");
			input.requestFocus(); //�ٽ� �޽����� �����Ҽ� �ֵ��� ��
		}); //���͹�ư�� ��������
		
		javafx.scene.control.Button sendBtn = new javafx.scene.control.Button("������");
		sendBtn.setDisable(true);
		
		sendBtn.setOnAction(event ->{
			send(userName.getText() + ": "+ input.getText() + "\n" );
			input.setText("");
			input.requestFocus(); //�ٽ� �޽����� �����Ҽ� �ֵ��� �� �״ϱ� ä���ʿ� Ŀ���� �Լ� ������
		});//��ư�� ��������
		
		javafx.scene.control.Button connectionButton = new javafx.scene.control.Button("�����ϱ�");
		connectionButton.setOnAction(event ->{
			if(connectionButton.getText().equals("�����ϱ�")) {
				int port = 9876;
				try {
					port = Integer.parseInt(portText.getText());
				} catch (Exception e) {
					// TODO: handle exception
				}
				startClient(ipText.getText(), port);
				Platform.runLater(()->{
					textArea.appendText("[ä�ù� ����]\n");
				});
				connectionButton.setText("�����ϱ�");
				input.setDisable(false);
				sendBtn.setDisable(false); //����ڰ� ��ư�� �������� ó���Ҽ� �ֵ��� false�� �ٲ���
				input.requestFocus();
				
			}else {
				stopClient();
				Platform.runLater(()->{
					textArea.appendText(" [ä�ù� ����] \n");
				});
				connectionButton.setText("�����ϱ�");
				input.setDisable(true);
				sendBtn.setDisable(true); //�Է� �� �� ��ư�� ���� �� ������ ��
			}
		});
		BorderPane pane = new BorderPane();
		pane.setLeft(connectionButton);
		pane.setCenter(input);
		pane.setRight(sendBtn);
		
		root.setBottom(pane);
		Scene scene = new Scene(root , 400 ,400);
		primaryStage.setTitle("[ä�� ���α׷�]");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e ->{
			stopClient();
		});
		primaryStage.show();
		
		connectionButton.requestFocus();
	}
	
	//���α׷��� ������ �Դϴ�.
	public static void main(String[] args) {
		launch(args);
	}
}
