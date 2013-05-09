package HurtigTechnologies.jChat.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

public class IncommingRequest extends Thread implements Runnable {

	public IncommingRequest(Socket client, ChatListener listener) {
		setClient(client);
		setListener(listener);
	}
	private int requestID = JChatServer.lastRequestID++;
	private Socket client;
	private ChatListener server;
	private BufferedReader in;
	private PrintWriter out;
	private ArrayList<String> content;
	@Override
	public void run() {
		try {
			
			
			
			JChatServer.logDebug("Registered thread for requestID " + getRequestID());
			content = new ArrayList<String>();

			
			 try {
					client.setSoTimeout(10000);
				} catch (SocketException e) {
					// TODO: pull from config
					JChatServer.logError("Socket Timeout Set failed for socket on port " + client.getLocalPort() + ": " + e.getMessage());
					return;
				} 
		         
		     
				
			 JChatServer.logDebug("Connection Established for with id " + requestID);
				
				try{
					in = new BufferedReader(new InputStreamReader(
							client.getInputStream()));
					out = new PrintWriter(client.getOutputStream(), 
							true);
					JChatServer.logDebug("Established in and out streams for request " + requestID);
				} catch (IOException e) {
					JChatServer.logError("Read Failed for streams from request " + requestID + " Thread: " + Thread.currentThread().getId() );
					return;
				}
		
				String line;
				int lineNum = 1;
				
				while(client.isConnected()){
					// reading all lines from request
				try{
					while ((line = in.readLine()).length() != 0) {
						content.add(line);
						JChatServer.logDebug("Reading Line " + lineNum++ + " from request: " + requestID + ": " + line);
					}
					JChatServer.logDebug("Done Reading a total of  " + lineNum++ + " lines from request: " + requestID);

				} catch (IOException e) {
					JChatServer.logDebug("IOError While Reading line: '" + lineNum + "'");
					e.printStackTrace();

					break;
				}
					
					
					
					
					
					
					
					/** Do other stuff here **/
					
					
				
//					out = new PrintWriter(client.getOutputStream(),true);
//
//					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
//					String line2 = null;
//					while ((line2 = in.readLine()) != null) {
//						content.add(line2);
//					}
					JChatServer.logDebug(">> Content Loaded");
					for (String s : content)
						JChatServer.logDebug("\t>> " + s);

					if (content.size() < 1) {
						JChatServer.logDebug("Content Size is 0");
						out.println(JChatServer.VERSION + " 400 BAD_REQUEST");
						out.println(JChatServer.ERROR + "ERROR Expecting at least 1 line of input");
						close();
						return;
					}
					
					String[] headers = content.get(0).split(" ");
					if (headers.length != 2) {
						JChatServer.logDebug("Header is bad");

						out.println(JChatServer.VERSION + " 400 BAD_REQUEST");
						out.println(JChatServer.ERROR + "ERROR Expecting first line to be formated in '<version> <command>' format with ONLY 1 space");
						close();
						return;
					}

					if (headers[1].equals("POST")) {
						JChatServer.logDebug("Header is POST");

						if (content.size() != 4) { 
							out.println(JChatServer.VERSION + " 403 FORBIDDEN");
							out.println(JChatServer.ERROR + "Expecting 4 lines of input for post command: header, username, password, and message");
							close();
							return;
						}
						if (isAuthenticated(client.getInetAddress(),content.get(1), content.get(2))) {
							server.post(getUserForClient(client).getName() + "> " + content.get(3));
							out.println(JChatServer.VERSION + " 200 OK");
							close();
							return;
						} else {
							out.println(JChatServer.VERSION + " 401 ACCESS_DENIED");
							out.println(JChatServer.ERROR + "You are not authorized");
							close();
							return;
						}
					} else if (headers[1].equals("REGISTER")) {
						JChatServer.logDebug("Header is Register");

						if (content.size() < 2) {
							out.println(JChatServer.VERSION + " 400 BAD_REQUEST");
							out.println(JChatServer.ERROR + "ERROR Expecting 3 lines of input for a registration request: header, username, password");
							close();
							return;
						}
						String username = content.get(1);
						String password = content.get(2);
						if (username.length() > JChatServer.MAX_NAME_LENGTH) {
							out.println(JChatServer.VERSION + " 403 FORBIDDEN");
							out.println(JChatServer.ERROR + "The username: " + username + " is " + (username.length() > 100 ? "WAY" : "a bit")  + " too long. The Max username length is " + JChatServer.MAX_NAME_LENGTH + " Characters");
							close();
							return;
						}
						
						for (User user : server.getUsers()) {
							if (user.getName().equals(username)) {
								out.println(JChatServer.VERSION + " 403 FORBIDDEN");
								out.println(JChatServer.ERROR + "The username: " + username + "is already in use");
								close();
								return;
							}
						}
						User newUser = new User();
						newUser.setName(username);
						newUser.setPassword(password);
						newUser.setIp(client.getInetAddress());
						server.getUsers().add(newUser);
						
						out.println(JChatServer.VERSION + " 200 OK");
						out.println(JChatServer.COMMENT + "Welcome to the " + server.getChatName() + " chat server");
						close();
						return;
					} else if (headers[1].equals("CONNECT")) {
						out.println(JChatServer.VERSION + " 200 OK");
						JChatServer.logDebug("Printed Connection Response: " + JChatServer.VERSION + " 200 OK");
						close();
						return;
					} else {
						JChatServer.logDebug("Exiting Beacuse Command Not Understood");
						out.println(JChatServer.VERSION + " 400 BAD_REQUEST");
						out.println(JChatServer.ERROR + "ERROR: Command not Understood");
						close();
						return;
					}
					
					
					/** END Request **/
					
					
					
					
				
				}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private User getUserForClient(Socket client) {
		for (User user : server.getUsers()) {
			if (client.getInetAddress().equals(user.getIp())) {
				return user;
			}
		}
		return null;
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
		out.println();
		//out.close();
		//in.close();
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

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

}
