

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendThread extends Thread{

	Connection c;
	String messageToSend;
	public SendThread(Connection c,String messageToSend)
	{
		this.c=c;
		this.messageToSend=messageToSend;
	}
	
	public void run()
	{
		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		    byte[] buffer = new byte[1000];
			InetAddress hostAddress = c.getIP();
			 buffer = messageToSend.getBytes();
			DatagramPacket out = new DatagramPacket(buffer, buffer.length, hostAddress, 4001);
			socket.send(out);
			
			socket.close();
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		socket.close();
	}
}
