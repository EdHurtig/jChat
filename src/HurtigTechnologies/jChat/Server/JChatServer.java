package HurtigTechnologies.jChat.Server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JChatServer {
	public static final String VERSION = "JCHATSERVER/1.1";
	public static final boolean DEBUG = true;
	public static final String COMMENT = "#";
	public static final String PROMPT = ">>";
	public static final String ERROR = "!";
	
	private static ServerStatus status;

	public static final int MAX_NAME_LENGTH = 15;

	public static ArrayList<Thread> threadRegistry = new ArrayList<Thread>();
	public static int lastRequestID = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setStatus(ServerStatus.Started);
		logDebug("JChat Server Started");
		try {
			ChatListener cl = new ChatListener("Demo Room", 8787);
			logDebug("Registering Thread for Chat Listener");

			Thread t = new Thread(cl);
			threadRegistry.add(t);
			t.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ServerStatus getStatus() {
		return status;
	}

	public static void setStatus(ServerStatus status) {
		JChatServer.status = status;
	}

	public static void logError(String message) {
		SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		System.err.println("[" + timestamp.format(new Date()) + "] " + message);
	}
	
	public static void logDebug(String message) {
		if (DEBUG) {
			SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			System.out.println('[' + timestamp.format(new Date()) + "] Thread: " + Thread.currentThread().getId() + " > " + message);
		}
	}
	
	public static void logInfo(String message) {
		SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		System.out.println('[' + timestamp.format(new Date()) + "] " + message);
	}
}
