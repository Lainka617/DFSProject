package main.java.dfs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection {
	private BufferedReader inStream = null;
	private DataOutputStream outStream = null;
	
	private Socket socket = null;
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		
		try {
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("ERROR creating read/write streams for client");
		}
	}

	/*Receives an arbitrarily length message*/
	public NetworkMessage getMessage() {
				

		try {
			NetworkMessage message = new NetworkMessage();
			StringBuilder data = new StringBuilder();
			
			// read header until the first /r/n/
			String first = inStream.readLine();
			message.header = first;
			System.out.println("after first message: "+ first);
			// squash the second /r/n line that separates data length from data.
			String second = inStream.readLine();
			System.out.println("after second message : "+ second);
			//data.append(inStream.readLine());
			
			System.out.println("after third message : ");

			while(inStream.ready()) {
				char character = (char) inStream.read();
				System.out.println("after chars : "+ character);
				data.append(character);
			}
			
			message.data = data.toString();
			System.out.println(message.toString());
			return message;
			
		} catch (SocketException e) {					
			System.out.println("pipe broken");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

						
		return null;
	}
	
	public String getData() {
		StringBuilder data = new StringBuilder();
		
		try {
			while(inStream.ready()) {
				char character = (char) inStream.read();
				data.append(character);
			}
		} catch (IOException e) {
			System.out.println("IO error occured when trying to read extra data from connection.");
		}
		
		return data.toString();
	}
	
	public void sendMessage(String message) throws IOException {
		//outStream = new DataOutputStream(socket.getOutputStream());
		
		outStream.write(message.getBytes());
		outStream.flush();
		outStream.flush();
		System.out.println("response: " + new String(message.getBytes()));
	}
}
