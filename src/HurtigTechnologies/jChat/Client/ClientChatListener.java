package HurtigTechnologies.jChat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientChatListener extends ServerSocket implements Runnable {
	public ClientChatListener(int port) throws IOException {
		super(port);
	}
	
	@Override
	public void run() {
		while (true) {
			try {				
				Socket client = this.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream(),true);

				ArrayList<String> content = new ArrayList<String>();
				String line = null;
				while ((line = in.readLine()).length() != 0) {
					content.add(line);
				}
				if (content.size() == 0) {
					System.err.println("Client sent request with no lines");
				}
				String[] headers = content.get(0).split(" ");
				if (headers.length != 2) {
					System.err.println("Client sent request header line that has more than 1 space");
				}
				if (headers[1].equals("POST")) {
					for (int i = 1; i < content.size(); i++)
						System.out.println(content.get(i));
				} else {
					System.err.println("Server sent request with unknown header command: '" + content.get(0) + "'");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}
