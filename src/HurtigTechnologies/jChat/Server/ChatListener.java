package HurtigTechnologies.jChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.lang.*;
public class ChatListener extends ServerSocket implements Runnable {
	
	public ChatListener(String name, int port) throws IOException {
		super(port);

		setChatName(name);
	}
	
	private String chatName;
	
	private ArrayList<User> users = new ArrayList<User>();

	
	@Override
	public void run() {
		JChatServer.logDebug("ChatListener Thread Started");

		Socket client = null;
		while (JChatServer.getStatus() == ServerStatus.Started) {
			try {
				client = this.accept();
			
				IncommingRequest r = new IncommingRequest(client,this);
				JChatServer.logDebug("Attempting to register thread for requestID " + r.getRequestID());

				Thread t = new Thread(r);
				JChatServer.threadRegistry.add(t);
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean post(String message) {
		for (User user : users) {
			OutgoingRequest r = new OutgoingRequest(user,this,message);
			
			Thread t = new Thread(r);
			JChatServer.threadRegistry.add(t);
			t.start();
		}
		return true;
	}

	/** GETTERS & SETTERS **/

	public String getChatName() {
		return chatName;
	}


	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}


	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
}
