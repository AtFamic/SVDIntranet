package test;

import model.ChatworkAPI;

public class ChatworkAPI_Test {
	public static void main(String[] args) {
		ChatworkAPI.sendMessage("メッセージテストです。\r\nこの前に改行あり", 141121479);
	}
}
