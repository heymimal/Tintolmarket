package tintolmarket.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import tintolmarket.domain.Wine;
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
			Object wineCat = null;
			Object walletCat = null;
			if(winesFile.createNewFile()) {
				System.out.println("winefile created");
				if(walletFile.createNewFile()) {
					System.out.println("Walletfile created");
					wh = new WineHandler();
				} else {
					//de-serialize object
					FileInputStream file = new FileInputStream(walletFile);
					if(file.available() > 0) {
						ObjectInputStream in = new ObjectInputStream(file);
						walletCat = in.readObject();
					}
					wh = new WineHandler(wineCat,walletCat);
				}
			} else {
				System.out.println("winefile already exists");
				FileInputStream file = new FileInputStream(winesFile);
				if(file.available() > 0) {
					ObjectInputStream in = new ObjectInputStream(file);
		            wineCat = (ArrayList<Wine>)in.readObject();
		            in.close();
				}
	            file.close();
	            if(walletFile.createNewFile()) {
	            	wh = new WineHandler(wineCat,walletCat);
	            } else {
	            	file = new FileInputStream(walletFile);
	            	if(file.available()>0) {
	            		ObjectInputStream in = new ObjectInputStream(file);
		            	walletCat = in.readObject();
	            	}
	            	wh = new WineHandler(wineCat,walletCat);
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
		wh.addWine("vinho2");
        System.out.println(wh.wtv().toString());
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
