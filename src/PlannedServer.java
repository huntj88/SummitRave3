

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;

public class PlannedServer {

	
	public static void main(String[] args) throws Exception {
	    int PORT = 4000;
	    byte[] buf = new byte[1000];
	    DatagramPacket packet = new DatagramPacket(buf, buf.length);
	    DatagramSocket socket = new DatagramSocket(PORT);
	    ArrayList<Connection> connections = new ArrayList<Connection>();
	    //System.out.println("Server started");
	    
	    while(true)
	    {
	    	socket.setSoTimeout(280000000);
	    	socket.receive(packet);
	    	//System.out.println(connections.size());
	    	
	    	String[] data = new String(packet.getData(), 0, packet.getLength()).split(" ");
	    	
	    	if(data[0].equals("Login"))
	    	{
	    		boolean add=true;
	    		for(Connection test : connections)
	    		{
	    			if(test.getIP()==packet.getAddress())
	    			add=false;
	    		}
	    		if(add)
	    		connections.add(new Connection(packet.getAddress(), packet.getPort(),data[1]));
	    		
	    		System.out.println(data[1] + " has Logged in");
	    	}
	    	else if(data[0].equals("Logout"))
	    	{
	    		Iterator<Connection> i = connections.iterator();
	    		while (i.hasNext())
	    		{
	    			
	    			if(i.next().getUsername().equals(data[1]))
	    			{
	    				System.out.println(connections.size());
	    				i.remove();
	    				System.out.println(data[1] + " has Logged out");
	    				System.out.println(connections.size());
	    			}
	    		}
	    		
	    	}
	    	else if(data[0].equals("Move"))
	    	{
	    		System.out.println("data[0] is packet");
	    	}
	    	else
	    	{
	    		System.out.println("From Client: "+new String(packet.getData(), 0, packet.getLength()));
	    		
	    		//send to everyone else in the connections array
	    		for(Connection test : connections)
	    		{
	    			new SendThread(test, new String(packet.getData(), 0, packet.getLength())).start();
	    		}
	    		
	    	
	    	}
	    }
	}
	
}
