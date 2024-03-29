package tintolmarket.app.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import tintolmarket.domain.Wine;
import tintolmarket.handlers.MessageHandler;
import tintolmarket.handlers.WineHandler;



/**
 * Classe Server
 * @author fc54446, fc54409, fc54933
 *
 */
public class Server {
	
	private static final int PORT_DEFAULT = 12345;
	public static final String users = "users.txt";
	public static final String wines = "wines.txt";
	public static final String wallet = "wallet.txt";
	public static final String messages = "messages.txt";
	
	public static void main(String[] args) {
		System.out.println("servidor: main tintol");
		Server server = new Server();
		try {
			if(args.length != 0) {
				server.startServer(Integer.parseInt(args[0]));
			} else {
				server.startServer(PORT_DEFAULT);
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * Start the server
	 */
	public void startServer (int PORT){ //will receive values
		ServerSocket sSoc = null;
		WineHandler wh = null;
		MessageHandler mh = new MessageHandler(users,messages);
		System.out.println(PORT);
        
		try {
			sSoc = new ServerSocket(PORT);
			File winesFile = new File(wines);
			File messagesFile = new File(messages);
			File usersFile = new File(users);
			File walletFile = new File(wallet);
			Object wineCat = null;
			Object walletCat = null;
			if(winesFile.createNewFile()) {
				System.out.println("winefile created");
				if(walletFile.createNewFile()) {
					System.out.println("walletfile created");
					wh = new WineHandler(Server.wines,Server.wallet, null, null);
				} else {
					//de-serialize object
					FileInputStream file = new FileInputStream(walletFile);
					if(file.available() > 0) {
						ObjectInputStream in = new ObjectInputStream(file);
						walletCat = in.readObject();
						in.close();
					}
					wh = new WineHandler(Server.wines,Server.wallet, wineCat,walletCat);
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
	            	wh = new WineHandler(Server.wines,Server.wallet, wineCat,walletCat);
	            } else {
	            	file = new FileInputStream(walletFile);
	            	if(file.available()>0) {
	            		ObjectInputStream in = new ObjectInputStream(file);
		            	walletCat = in.readObject();
		            	in.close();
	            	}
	            	wh = new WineHandler(Server.wines,Server.wallet, wineCat,walletCat);
	            }
			}
			
			if(usersFile.createNewFile()) {
				System.out.println("users file created");
			}
			if(messagesFile.createNewFile()) {
				System.out.println("Messages file created");
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
				new ServerThread(inSoc,wh,mh).start();
				//newServerThread.start();
		    }
		    catch (IOException e) {
		        //e.printStackTrace();
		    	System.out.println("a client disconnected");
		    }
		    
		}
	}
}
