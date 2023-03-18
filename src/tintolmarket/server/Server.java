package tintolmarket.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import tintolmarket.domain.catalogs.MessageHandler;
import tintolmarket.handlers.WineHandler;



public class Server {
	
	public static final String users = "users.txt";
	public static final String wines = "wines.txt";
	public static final String wallet = "wallet.txt";
	public static final String messages = "messages.txt";
	
	public static void main(String[] args) {
		System.out.println("servidor: main tintol");
		Server server = new Server();
		server.startServer();
	}

	public void startServer (){ //will receive values
		ServerSocket sSoc = null;
		WineHandler wh = null;
		MessageHandler mh = new MessageHandler(users,messages);
        
		try {
			sSoc = new ServerSocket(12345);
			// file creation 
			File winesFile = new File(wines);
			File messagesFile = new File(messages);
			File usersFile = new File(users);
			File walletFile = new File(wallet);
			if(winesFile.createNewFile()) {
				if(walletFile.createNewFile()) {
					wh = new WineHandler();
				} else {
					//de-serialize object
					FileInputStream file = new FileInputStream(walletFile);
		            ObjectInputStream in = new ObjectInputStream(file);
					wh = new WineHandler(false,null,true,in.readObject());
				}
			} else {
				FileInputStream file = new FileInputStream(winesFile);
	            ObjectInputStream in = new ObjectInputStream(file);
	            Object catWine = in.readObject();
	            in.close();
	            file.close();
	            if(walletFile.createNewFile()) {
	            	wh = new WineHandler(true,catWine,false,null);
	            } else {
	            	file = new FileInputStream(walletFile);
	            	in = new ObjectInputStream(file);
	            	Object catWallet = in.readObject();
	            	wh = new WineHandler(true,catWine,true,catWallet);
	            }
			}
			
			if(usersFile.createNewFile()) {
				System.out.println("users file created");
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
		while(true) { // change for multiple clients
			try {
				Socket inSoc = sSoc.accept();
				System.out.println("Connection Established");
				ServerThread newServerThread = new ServerThread(inSoc,wh,mh);
				newServerThread.start();
		    }
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		}
	}
}
