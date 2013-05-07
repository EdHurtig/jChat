package HurtigTechnologies.jChat.Server;

import java.net.InetAddress;
import java.util.Date;

public class User {
	private String name;
	private InetAddress ip;
	private Date bannedTime;
	private String password;
	
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
		return (new Date()).getTime() < bannedTime.getTime();
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
	
}
