package JavaChat.Server;

import java.util.Iterator;
import java.util.StringTokenizer;

enum SendType {
	BROADCAST, PERSONAL, NONE
};

public class Command {

	private Command() {
		// TODO Auto-generated constructor stub
	}

	// sendMessage�� ������ �Ұ��� 1 broadcast�� �������Ұ��� 0 �������� -1
	public static SendType switchCommand(String comdline1, StringTokenizer tokenizer, String serverSendMessage,
			Client client) throws Exception {

		switch (comdline1.toLowerCase()) {
		case "help":
			client.serverSendMessage = helpAction();

			return SendType.PERSONAL;

		case "setprefix":

			client.serverSendMessage = setInfo(Info.PREFIX, tokenizer, serverSendMessage);

			return SendType.PERSONAL;

		case "setnickname":

			client.serverSendMessage = setInfo(Info.NICKNAME, tokenizer, serverSendMessage);

			return SendType.PERSONAL;
		case "players":

			client.serverSendMessage = Main.clients.size() + "";

			return SendType.PERSONAL;
		case "playerlist":

			client.serverSendMessage = playerListInfo(serverSendMessage);

			return SendType.PERSONAL;
		case "vote":
			voteAction(tokenizer);

			return SendType.NONE;
		case "luckey":

			return SendType.PERSONAL;
		case "rank":

			return SendType.PERSONAL;
		case "y":
			if (!Main.isVoteTime)
				return SendType.NONE;
			Main.yes++;
			client.serverSendMessage = "���� ����!";
			return SendType.PERSONAL;
		case "n":
			if (!Main.isVoteTime)
				return SendType.NONE;
			Main.no++;
			client.serverSendMessage = "�ݴ� ����!";

			return SendType.PERSONAL;
		default:
			client.serverSendMessage = "���� ����� �� ���� ��ɾ��Դϴ�!\n";
			return SendType.NONE;
		}

	}

	public static String helpAction() {
		final String helpMessage = "\n/help ��ɾ���� ������ �� �� �ֽ��ϴ�\n" + "/setprefix (�ٲ��̸�) (����) Īȣ�� �����Ͻ� �� �ֽ��ϴ�.\n"
				+ "/setnickname (�ٲ��̸�) (����) �г����� �����Ͻ� �� �ֽ��ϴ�.\n" + "/players ���� ä�ù濡 �ִ� �ο� ���� Ȯ���� �� �ֽ��ϴ�.\n"
				+ "/playerlist ���� ä�ù濡 �ִ� �������� ������ Ȯ���� �� �ֽ��ϴ�\n" + "/vote (��ǥ����) ä�ù濡�� ��ǥ�� �����Ҽ� �ֽ��ϴ�.\n"
				+ "/luckey ������ �����մϴ�.\n" + "/rank �ڽ��� ���� ��ŷ�� �� �� �ֽ��ϴ�";

		return helpMessage;

	}

	public static String setInfo(Info info, StringTokenizer tokenizer, String message) throws Exception {

		String comdline2 = tokenizer.nextToken();
		String comdline3 = tokenizer.nextToken();
		switch (info) {
		case NICKNAME:
			message = "0000" + "&" + comdline2 + "&" + comdline3;

		case PREFIX:
			message = "0001" + "&" + comdline2 + "&" + comdline3;

		}

		return message;

	}

	public static String playerListInfo(String message) {
		String nameslist = "";
		Iterator<String> nickNames = Main.users.keySet().iterator();
		while (nickNames.hasNext()) {
			nameslist += nickNames.next() + ",";
		}
		message = nameslist;

		return message;
	}

	public static void voteAction(StringTokenizer tokenizer) throws Exception {

		if (Main.isVoteTime)
			Client.broadcastMessage("���� ��ǥ�� ��û���ּ���" , SendMessageType.SERVER);

		String comdline2 = tokenizer.nextToken();
		Client.broadcastMessage("��ǥ�� ���۵Ǿ����ϴ� !! \n ����:" + comdline2 + "\n" + "������ \\y �ݴ�� \\n �Է����ּ���!" , SendMessageType.SERVER);
		Main.yes = 0;
		Main.no = 0;

		Runnable thread = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					int yes = Main.yes;
					int no = Main.no;
					if (yes + no >= Main.clients.size()) {
						Client.broadcastMessage(
								"����:" + yes + "  �ݴ� :" + no + "\n" + (yes == no) != null ? "���� �Դϴ�!"
										: (yes > no) ? "���� �¸�!" : "�ݴ� �¸�!" , SendMessageType.SERVER);
					}
					break;
				}
			}
		};
		Main.threadPool.submit(thread);

	}

}
