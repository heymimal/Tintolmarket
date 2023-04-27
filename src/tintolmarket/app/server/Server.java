package tintolmarket.app.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import tintolmarket.app.security.ServerSecurity;
import tintolmarket.domain.Mensagem;
import tintolmarket.domain.Wine;
import tintolmarket.handlers.BlockchainHandler;
import tintolmarket.handlers.MessageHandler;
import tintolmarket.handlers.WineHandler;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;


/**
 * Classe Server
 * @author fc54446, fc54409, fc54933
 *
 */
public class Server {
	
	private static final int PORT_DEFAULT = 12345;
	public static final String users = "users.txt";
	public static final String wines = "wines.ser";
	public static final String wallet = "wallet.ser";
	public static final String messages = "messages.ser";

	public static ServerSecurity auth;
	
	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.keyStore"
				,
				"keystore.server");
		System.setProperty("javax.net.ssl.keyStorePassword"
				, "keypass");

		System.out.println("servidor: Tintolmarket");
		Server server = new Server();

		String port;
		String passCifra = null;
		String keyStore = null;
		String passKeyStore = null;

		try {
			if(args.length == 4){
				port = args[0];
				passCifra = args[1];
				keyStore = args[2];
				passKeyStore = args[3];
				auth = new ServerSecurity(passCifra, keyStore, passKeyStore,wines,wallet,messages);
				server.startServer(Integer.parseInt(args[0]));
			}else{
				System.out.println("Comando para correr servidor estah incorreto. \n" +
						"Para correr corretamente eh necessario : <port> <password-cifra> <keystore> <password-keystore>. \n" +
						"Por exemplo : 12345 passDeExemplo nomeKeyStore passKeyStore");
			}

		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * Start the server
	 * @throws NoSuchAlgorithmException
	 */
	public void startServer (int PORT) throws NoSuchAlgorithmException{ //will receive values
		//ServerSocket sSoc = null;

		ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
		SSLServerSocket ss = null;

		WineHandler wh = null;
		MessageHandler mh = null;
		BlockchainHandler bh = null;
		System.out.println(PORT);
        
		try {
			//sSoc = new ServerSocket(PORT);
			ss = (SSLServerSocket) ssf.createServerSocket(PORT);

			File winesFile = new File(wines);
			File messagesFile = new File(messages);
			File usersFile = new File(users);
			File walletFile = new File(wallet);
			Object wineCat = null;
			Object walletCat = null;
			Object mensagemCat = null;
			List<String> allUsers = new ArrayList<>();

			if(messagesFile.createNewFile()) {
				System.out.println("Messages file created");
			}else {
				FileInputStream file = new FileInputStream(messagesFile);
				if(file.available() > 0) {
					ObjectInputStream in = new ObjectInputStream(file);
					mensagemCat = (ArrayList<Mensagem>)in.readObject();
					in.close();
				}
				file.close();
			}

			if(usersFile.createNewFile()) {
				auth.encryptUsers(null);
				System.out.println("users file created");
			} else {
				allUsers = auth.getAllUsers();
			}

			if(messagesFile.createNewFile()) {
				System.out.println("Messages file created");
			}
			mh = new MessageHandler(mensagemCat,allUsers,messages);
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
			bh = new BlockchainHandler(auth);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		while(true) { // change for multiple clients
			try {

				Socket inSoc = ss.accept();
				System.out.println("Connection Established");
				new ServerThread(inSoc,wh,mh,bh,auth).start();
				//newServerThread.start();
		    }
		    catch (IOException e) {
		        //e.printStackTrace();
		    	System.out.println("a client disconnected");
		    }
		    
		}
	}
}
