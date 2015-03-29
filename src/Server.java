import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JPanel implements Runnable, ActionListener{
	
	protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";
    
	public Server()
	{	
		super(new GridBagLayout());
		
		new Thread(this).start();
		 
        textField = new JTextField(20);
        textField.addActionListener(this);
 
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
 
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int PORT = 4000;
	    byte[] buf = new byte[1000];
	    DatagramPacket packet = new DatagramPacket(buf, buf.length);
	    DatagramSocket socket = null; 
	    ArrayList<Connection> connections = new ArrayList<Connection>();
		
	    try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   
	    
	    //System.out.println("Server started");
	    
	    while(true)
	    {
	    	try {
				socket.setSoTimeout(280000000);
				socket.receive(packet);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
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
	    		addToLog(data[1] + " has Logged in");
	    		
	    		sendToAll(connections, packet);
	    	}
	    	else if(data[0].equals("Logout"))
	    	{
	    		Iterator<Connection> i = connections.iterator();
	    		while (i.hasNext())
	    		{
	    			
	    			if(i.next().getUsername().equals(data[1]))
	    			{
	    				i.remove();
	    				addToLog(data[1] + " has Logged out");
	    				break;
	    			}
	    		}
	    		
	    		sendToAll(connections, packet);
	    	}
	    	else if(data[0].equals("Health")) //test to make sure correct values. just change the text
	    	{
	    		addToLog(data[0]);
	    		addToLog(data[1]);
	    		addToLog(data[2]);
	    		sendToAll(connections, packet);
	    	}
	    	else
	    	{
	    		addToLog(data[0]);
	    		sendToAll(connections, packet);
	    	}
	    }
	}
	
	public void sendToAll(ArrayList<Connection> connections,DatagramPacket packet)
	{
		for(Connection test : connections)
		{
			new SendThread(test, new String(packet.getData(), 0, packet.getLength())).start();
		}
	}
	
	public void addToLog(String add)
	{
        textArea.append(add + newline);
        textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		String text = textField.getText();
        textArea.append(text + newline);
        textField.selectAll();
 
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
		
	}

}
