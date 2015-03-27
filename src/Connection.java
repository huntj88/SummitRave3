

import java.net.InetAddress;

public class Connection {

	private	int port;
	private InetAddress ip;
	private String username;
	
	public Connection(InetAddress ip,int port,String username)
	{
		this.ip=ip;
		this.port=port;
		this.username=username;
	}
	
	public InetAddress getIP()
	{
		return ip;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public String getUsername()
	{
		return username;
	}
}
