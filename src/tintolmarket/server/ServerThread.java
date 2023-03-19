package tintolmarket.server;
import tintolmarket.domain.*;
import tintolmarket.handlers.*;

//import tintolmarket.server.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	private Socket socket = null;
	private Operacao op;
	private WineHandler wh;
	private MessageHandler mh;
	//will have mutex for access to .txt files and possibly others

	public ServerThread(Socket inSoc, WineHandler wh, MessageHandler mh) {
		socket = inSoc;
		this.wh = wh;
		this.mh = mh;
		System.out.println("thread do server para cada cliente");
	}

	@Override
	public void run(){
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

			String user = null;
			String passwd = null;
			try {
				user = (String)inStream.readObject();
				passwd = (String)inStream.readObject();
				
				System.out.println("thread: depois de receber a password e o user");
				if (user.length() != 0){
					System.out.println("yes");
					System.out.println(user);
					System.out.println(passwd);
					FileReader fr = new FileReader(Server.users);
					BufferedReader br = new BufferedReader(fr);
					String check;
					boolean found = false;
					while((check = br.readLine()) != null) {
						System.out.println(check);
						String[] split = check.split(":");
						if(split[0].equals(user)) {
							System.out.println("test");
							found = true;
							if(split[1].equals(passwd)) {
								outStream.writeObject(new Boolean(true));
							} else {
								outStream.writeObject(new Boolean(false));
							}
						}
					}
					if(!found) {
						FileWriter fw = new FileWriter(Server.users, true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(user+":"+passwd);
						bw.newLine();
						wh.addWalletUser(user);
						outStream.writeObject(new Boolean(true));
						System.out.println("info added");
						bw.close();
						fw.close();
						this.wh.addWalletUser(user);
					}
					fr.close();
					br.close();
					//verificação
					outStream.writeObject(new Boolean(true));
				}
				while(true) {
					this.op = (Operacao)inStream.readObject();
					switch(this.op) {
					case ADD:{
						outStream.writeObject(true);
						System.out.println("Funcao ADD");
						String winename = (String) inStream.readObject();
						String wineimage = (String) inStream.readObject(); //not like this
						boolean resposta = wh.addWine(winename);
						outStream.writeObject(resposta);
						//send to user
						break;
					}case BUY:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						String wineseller = (String) inStream.readObject();
						int quantity = (int) inStream.readObject();
						wh.buyWine(winename, wineseller, user, quantity);
						// send to user
						break;
					}case CLASSIFY:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						int stars = (int) inStream.readObject();
						wh.classify(winename, stars);
						//send to user
						break;
					}case READ:{
						outStream.writeObject(true);
						mh.readMessagesbyUser(user);
						//send to user
						break;
					}case SELL:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						int value = (Integer) inStream.readObject();
						int quantity = (Integer) inStream.readObject();
						wh.sellWine(winename, user, quantity, quantity);
						//send to user
						break;
					}case TALK:
						outStream.writeObject(true);
						String to = (String) inStream.readObject();
						String message = (String) inStream.readObject();
						mh.addMensagem(user, to, message);
						//send to user
						break;
					case VIEW:
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						wh.viewWine(winename); //needs changes - image related
						//send to user
						break;
					case WALLET:
						outStream.writeObject(true);
						int wallet = wh.getWallerUser(user);
						outStream.writeObject(wallet);
						break;
					default:
						break;
					}
				}
				
			} catch (IOException e1) {
						e1.printStackTrace();
				}
		}catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		
	//refazer
	//este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
	//nao faz qualquer tipo de autenticacao
	
		
	}
	
}



