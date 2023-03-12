package tintolmarket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {
	public static void main(String[] args) {
		System.out.println("servidor: main");
		Server server = new Server();
		server.startServer();
	}

	public void startServer (){ //will receive values
		ServerSocket sSoc = null;
        
		try {
			sSoc = new ServerSocket(12345);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
         
		while(true) { // change for multiple clients
			try {
				Socket inSoc = sSoc.accept();
				System.out.println("Connection Established");
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
		    }
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		}
	}
}
