package HurtigTechnologies.jChat.Server;

import java.net.InetAddress;
import java.util.Date;

public class User {
	private String name;
	private InetAddress ip;
	private Date bannedTime;
	private String password;
	private int numFails = 0;
	private ChatListener chatroom;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress inetAddress) {
		this.ip = inetAddress;
	}
	public boolean isBanned() {
		return (bannedTime != null && (new Date()).getTime() < bannedTime.getTime());
	}
	
	public void ban(Date time) {
		this.bannedTime = time;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getNumFails() {
		return numFails;
	}
	public void clearFails() {
		this.numFails = 0;
	}
	public void addNumFails() {
		this.numFails++;
		if (numFails > 2) remove();
	}
	public void setChatListener(ChatListener c) {
		this.chatroom = c;
	}
	public void remove() {
		JChatServer.logDebug("Removing User: " + getName() + " = " +  this.chatroom.getUsers().get(this.chatroom.getUsers().indexOf(this)).getName());
		this.chatroom.getUsers().remove(this.chatroom.getUsers().indexOf(this));
	}
	
}
