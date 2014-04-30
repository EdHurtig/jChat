package HurtigTechnologies.jChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The Listener for requests from clients for a given Chat
 * 
 * This class represents a single chat-room
 * 
 * @author Edward Hurtig <hurtige@ccs.neu.edu>
 * @version Apr 29, 2014
 */
public class ChatListener extends ServerSocket implements Runnable {

    /**
     * Constructs a new Listener on the given port with the given Chat Name
     * 
     * @param name
     * @param port
     * @throws IOException
     */
    public ChatListener(String name, int port) throws IOException {
        super(port);

        setChatName(name);
    }

    /**
     * The name of the chat that this listener is responsible for
     */
    private String chatName;

    /**
     * The list of users in this chat
     */
    private ArrayList<User> users = new ArrayList<User>();

    /**
     * Starts the chat-room listener
     */
    @Override
    public void run() {
        JChatServer.logDebug("ChatListener Thread Started");

        Socket client = null;
        while (JChatServer.getStatus() == ServerStatus.Started) {
            try {
                client = this.accept();

                IncommingRequest r = new IncommingRequest(client, this);
                JChatServer
                        .logDebug("Attempting to register thread for requestID "
                                  + r.getRequestID());

                Thread t = new Thread(r);
                JChatServer.threadRegistry.add(t);
                t.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message to all the users registered for this chat-room
     * 
     * @param message
     *            The message to send to all users
     * @return true if the message was valid and sent, otherwise false
     */
    public boolean post(String message) {
        if (message.length() > 250) {
            return false;
        }

        for (User user : users) {
            OutgoingRequest r = new OutgoingRequest(user, this, message);

            Thread t = new Thread(r);
            JChatServer.threadRegistry.add(t);
            t.start();
        }
        return true;
    }

    /* GETTERS & SETTERS */

    /**
     * Gets the name of this chat room
     * 
     * @return The Chat name
     */
    public String getChatName() {
        return chatName;
    }

    /**
     * Sets the Chat-room name
     * 
     * @param chatName
     *            The new name of the chat room
     */
    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    /**
     * Gets the list of users in this chat room
     * 
     * @return The List of users currently in this chat room
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Sets the List of Users in the Chat Room
     * 
     * @param users
     *            The new List of Users
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
