package HurtigTechnologies.jChat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;

public class OutgoingRequest extends Thread implements Runnable {

	public OutgoingRequest(User user, ChatListener server, String content) {
		setUser(user);
		setListener(server);
		getContent().clear();
		getContent().add(content);
	}
	
	public OutgoingRequest(User user, ChatListener server, ArrayList<String> content) {
		setUser(user);
		setListener(server);
		setContent(content);
	}
	private int requestID = JChatServer.lastRequestID++;
	private boolean success = false; // for the future
	private Socket client;
	private User user;
	private ChatListener server;
	private BufferedReader in;
	private PrintWriter out;
	private ArrayList<String> response = new ArrayList<String>();
	private ArrayList<String> content = new ArrayList<String>();
	@Override
	public void run() {
		try {
			client = new Socket(user.getIp(),server.getLocalPort());
			JChatServer.logDebug("Sending Message to '" + content.toString()  + "'" + user.getName());
			
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(),true);
			out.println(JChatServer.VERSION + " POST");

			for (String line : content) {
				out.println(line);
			}
			out.println();
			String line = null;
			while ((line = in.readLine()) != null) {
				response.add(line);
			}
			
			if (response.size() == 0) {
				JChatServer.logError(" POST to user " + user.getName() + " returned nothing in response");
				return;
			}
			
			
			
			String[] headers = content.get(0).split(" ");
			if (headers.length != 3) {
				out.println(JChatServer.VERSION + " 400 BAD_REQUEST");
				JChatServer.logError("Response from User " + user.getName() + ": Expecting first line to be formated in '<version> <statuscode> <statustext>' format with ONLY 2 space");
				close();
				return;
			}
			
			if (headers[1].equals("200"))
			{
				success = true;
			} else {
				success = false;
			}
			
			close();
			JChatServer.logDebug("Closed Outging Request with ID" + getRequestID());
			user.clearFails();
			/** END Request **/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			user.addNumFails();
			e.printStackTrace();
		}
	}

	public boolean isAuthenticated(InetAddress address, String username, String password) {
		for (User user : server.getUsers()) {
			if (address.equals(user.getIp()) && username.equals(user.getName()) && password.equals(user.getPassword())) {
				if (!user.isBanned())
					return true;
			}
		}
		return false;
	}
	public void close() throws IOException {
		in.close();
		client.close();
	}
	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public ChatListener getListener() {
		return server;
	}

	public void setListener(ChatListener listener) {
		this.server = listener;
	}
	
	public ArrayList<String> getContent() {
		return content;
	}

	public void setContent(ArrayList<String> content) {
		this.content = content;
	}

	public ArrayList<String> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<String> response) {
		this.response = response;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

}
