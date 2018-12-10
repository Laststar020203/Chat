package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	Socket socket;
	
	public Client(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		receive();
	}
	//Ŭ���̾�Ʈ�κ��� �޽����� ���� �޴� �޼ҵ�
	public void receive() {
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						int length = in.read(buffer); //read�Լ��� ���� ������ ����
						while(length == -1) throw new IOException();
						System.out.println("[�޽��� ���� ����] "
								+socket.getRemoteSocketAddress()
								+" : "+Thread.currentThread().getName()); //�޽����� ���� Ŭ���̾�Ʈ �ּ�
						String messege = new String(buffer, 0, length, "UTF-8");
						for (Client client : Main.clients) {
							client.send(messege);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					try {
						System.out.println("[�޽��� ���� ����]"
								+socket.getRemoteSocketAddress()
								+" : "+Thread.currentThread().getName());
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		};
		Main.threadPool.submit(thread); //�����带 ������Ǯ�� ����
		
	}
	//Ŭ���̾�Ʈ���� �޽����� �����ϴ� �޼ҵ�
	public void send(String messeage) {
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = messeage.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				
				} catch (Exception e) {
					// TODO: handle exception
					try {
						
						System.out.println("[�޽��� �۽� ����"
								+socket.getRemoteSocketAddress()
								+" : "+Thread.currentThread().getName());
						Main.clients.remove(Client.this);
						socket.close();
						
					} catch (Exception e2) {
						// TODO: handle exception
						
					}
				}
				
			}
		};
		
	}

}
