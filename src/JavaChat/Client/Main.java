package JavaChat.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class Main extends Application {

	Socket socket;
	TextArea textArea;
	DataOutputStream out;
	ReceiveCommand command;
	

	String prefix = "";
	String nickName = "";

	private void startClient(String IP, int port, String name) {

		command = new ReceiveCommand(this);
		nickName = name;

		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(IP, port);
					out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF(name);

					out.flush();

					receive();
				} catch (ConnectException ce) {
					
					
					textArea.appendText("[���� ���� ����] ������ �����ֽ��ϴ�.  \n");
					
					
				} catch (Exception e) {
					// TODO: handle exception
					
					if (socket != null && !socket.isClosed()) {
						textArea.appendText("[���� ���� ����] �˼� ���� ������ ������ �����Ͽ����ϴ�.");
						stopClient();
						Platform.exit();
					}
		
				}

			};
		};

		thread.start();
	}

	protected void receive() {
		// TODO Auto-generated method stub
		while (true) {
			try {

				DataInputStream in = new DataInputStream(socket.getInputStream());
				String receiveMessage = in.readUTF();
				
				
				System.out.println("Client receive : "+receiveMessage);
				
				Platform.runLater(() -> {
					textArea.appendText(command.checkCommand(receiveMessage));
				});

			} catch (Exception e) {
				// TODO: handle exception
				stopClient();
			}
		}
	}

	private void stopClient() {
		try {

			if (socket != null && !socket.isClosed()) {
				socket.isClosed();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void send(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {

					out.writeUTF(message);
					out.flush();

				} catch (Exception e) {
					// TODO: handle exception
					stopClient();
				}

			};
		};
		thread.start();
	}

	@Override
	public void start(Stage primaryStage) {

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));

		HBox xBox = new HBox();
		xBox.setSpacing(5);

		TextField thField = new TextField();
		thField.setPrefWidth(150);
		thField.setPromptText("�г����� �Է��Ͻÿ�");
		HBox.setHgrow(thField, Priority.ALWAYS);

		TextField serverIP = new TextField("127.0.0.1");
		TextField serverPort = new TextField("9876");

		serverPort.setMaxHeight(80);
		xBox.getChildren().addAll(thField, serverIP, serverPort);

		root.setTop(xBox);

		textArea = new TextArea();
		textArea.setEditable(false);
		root.setCenter(textArea);

		TextField input = new TextField();
		input.setMaxWidth(Double.MAX_VALUE);
		input.setDisable(true);
		input.setOnAction(event -> {
			send(input.getText());
			input.setText("");
			input.requestFocus();
		});

		Button sendBtn = new Button("������");
		sendBtn.setDisable(true);

		sendBtn.setOnAction(event -> {
			send(input.getText());
			input.setText("");
			input.requestFocus();
		});

		Button connectionButton = new Button("�����ϱ�");
		connectionButton.setOnAction(event -> {
			if (connectionButton.getText().equals("�����ϱ�")) {
				int port = 9876;
				try {
					port = Integer.parseInt(serverPort.getText());
				} catch (Exception e) {
					// TODO: handle exception
				}
				startClient(serverIP.getText().toString(), port, thField.getText().toString());
				Platform.runLater(() -> {
					textArea.appendText("[ä�ù� ���� ��û��]\n");

				});

				connectionButton.setText("�����ϱ�");
				sendBtn.setDisable(false);
				input.setDisable(false);
				input.requestFocus();

			} else {
				stopClient();
				Platform.runLater(() -> {
					textArea.appendText("[ä�ù� ����]\n");
				});
				connectionButton.setText("�����ϱ�");
				sendBtn.setDisable(true);
				input.setDisable(true);
			}
		});

		BorderPane bottomRoot = new BorderPane();
		bottomRoot.setLeft(connectionButton);
		bottomRoot.setCenter(input);
		bottomRoot.setRight(sendBtn);

		root.setBottom(bottomRoot);

		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[ä�ù� Ŭ���̾�Ʈ]");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> {
			stopClient();
		});
		primaryStage.show();

		connectionButton.requestFocus();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
