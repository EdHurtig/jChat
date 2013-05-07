package HurtigTechnologies.jChat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.ws.Response;

import HurtigTechnologies.Utils;
import HurtigTechnologies.jChat.Server.JChatServer;

public class JChatClient {

	public static final String VERSION = "JCHATCLIENT/1.0";

	static InetAddress remoteServerIP;
	static int remoteServerPort;
	static String username;
	static String password;
	static BufferedReader in;
	static PrintWriter out;

	public static void main(String[] args) {
		try {
			String userInput = null;
			do {
				userInput = Utils.readLine("Listen to port: ");
				remoteServerPort = Integer.parseInt(userInput);

				userInput = Utils.readLine("Connect to Server: ");
				remoteServerIP = InetAddress.getByName(userInput);
			} while (!checkServerSettings());

			do {
				username = Utils.readLine("Enter Username");
				password = Utils.readLine("Enter Password");
			} while (!attemptRegister());

			System.out.println("REGISTERED - STARTING LISTENER");
			/** Start the Chat Listener **/
			ClientChatListener ccl;
			ccl = new ClientChatListener(remoteServerPort);
			Thread t = new Thread(ccl);

			t.start(); // Start the Listener Thread, it listens for any info
						// from the server

			do {
				userInput = Utils.readLine("Post");
				post(userInput);

			} while (!userInput.equals("exit"));

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private static void post(String message) throws IOException {
		Socket request = new Socket();
		try {
			request.connect(new InetSocketAddress(remoteServerIP,
					remoteServerPort), 5000);
		} catch (SocketTimeoutException e) {
			System.out.println("Connection Timeout");
		}

		try {
			in = new BufferedReader(new InputStreamReader(
					request.getInputStream()));

			out = new PrintWriter(request.getOutputStream(), true);
			out.println(VERSION + " POST");
			out.println(username);
			out.println(password);
			out.println(message);
			out.println();
			System.out.println("Sent Connection Info... ");
		} catch (IOException e) {
			System.err.println("FAILED TO Established Input Streams... ");
		}

		String line;
		int lineNum = 1;
		///test
		while (request.isConnected()) {
			// reading all lines from request
			try {
				ArrayList<String> response = new ArrayList<String>();
				System.out.println("Reading Input... ");

				while ((line = in.readLine()).length() != 0) {
					response.add(line);
					//System.out.println("Read Line... ");

				}
				System.out.println("Reading DONE!... ");

				for (String l : response)
					System.out.println(l);

				if (response.get(0).split(" ")[1].equals("200")) {
					System.out.println("Success!");
					request.close();
				}
				System.err.println("Failed to Register Username " + username);
				request.close();
			} catch (IOException e) {
				System.err.println("FAILED to read... ");

				break;
			}
		}		
	}

	private static boolean attemptRegister() throws IOException {
		System.out.println("Checking connection to the server: "
				+ remoteServerIP + ":" + remoteServerPort + "... ");

		Socket request = new Socket();
		try {
			request.connect(new InetSocketAddress(remoteServerIP,
					remoteServerPort), 5000);
		} catch (SocketTimeoutException e) {
			System.out.println("Connection Timeout");
			return false;
		}
		System.out.println("Connection Established... ");

		try {
			in = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			System.out.println("Established Input Stream... ");

			out = new PrintWriter(request.getOutputStream(), true);
			out.println(VERSION + " REGISTER");
			out.println(username);
			out.println(password);
			out.println();
			System.out.println("Sent Connection Info... ");

		} catch (IOException e) {
			System.err.println("FAILED TO Established Input Streams... ");

			return false;
		}

		String line;
		int lineNum = 1;

		while (request.isConnected()) {
			// reading all lines from request
			try {
				ArrayList<String> response = new ArrayList<String>();
				System.out.println("Reading Input... ");

				while ((line = in.readLine()).length() != 0) {
					response.add(line);
					System.out.println("Read Line... ");

				}
				System.out.println("Reading DONE!... ");

				for (String l : response)
					System.out.println(l);

				if (response.get(0).split(" ")[1].equals("200")) {
					System.out.println("Success!");
					request.close();
					return true;
				}
				System.out.println("Failed to Register Username " + username);
				request.close();
				return false;

			} catch (IOException e) {
				System.err.println("FAILED to read... ");

				break;
			}
		}
		return false;
	}

	private static boolean checkServerSettings() throws IOException {
		System.out.println("Checking connection to the server: "
				+ remoteServerIP + ":" + remoteServerPort + "... ");

		Socket request = new Socket();
		try {
			request.connect(new InetSocketAddress(remoteServerIP,
					remoteServerPort), 5000);
		} catch (SocketTimeoutException e) {
			System.out.println("Connection Timeout");
			return false;
		}
		System.out.println("Connection Established... ");

		try {
			in = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			System.out.println("Established Input Stream... ");

			out = new PrintWriter(request.getOutputStream(), true);
			out.println(VERSION + " CONNECT");
			out.println();
			System.out.println("Sent Connection Info... ");

		} catch (IOException e) {
			System.err.println("FAILED TO Established Input Streams... ");

			return false;
		}

		String line;
		int lineNum = 1;

		while (request.isConnected()) {
			// reading all lines from request
			try {
				ArrayList<String> response = new ArrayList<String>();
				System.out.println("Reading Input... ");

				while ((line = in.readLine()).length() != 0) {
					response.add(line);
					System.out.println("Read Line... ");

				}
				System.out.println("Reading DONE!... ");

				for (String l : response)
					System.out.println(l);

				if (response.get(0).split(" ")[1].equals("200")) {
					System.out.println("Success!");
					request.close();
					return true;
				}
				System.out
						.println("Connected but server is not opperational, please choose another one");
				return false;

			} catch (IOException e) {
				System.err.println("FAILED to read... ");

				break;
			}
		}
		return false;

	}
}
