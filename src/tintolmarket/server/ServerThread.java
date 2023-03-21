package tintolmarket.server;
import tintolmarket.domain.*;
import tintolmarket.handlers.*;

//import tintolmarket.server.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
		boolean connected;
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
				}
				//this.socket.isConnected()
				connected = true;
				while(connected) {
					System.out.println("Waiting for op...");
					this.op = (Operacao)inStream.readObject();
					System.out.println(this.op);
					switch(this.op) {
					
					case ADD:{
						outStream.writeObject(true);
						System.out.println("Funcao ADD");
						String winename = (String) inStream.readObject();
						String winepath= "server_"+(String) inStream.readObject();
						boolean resposta = wh.addWine(winename,winepath);
						outStream.writeObject(resposta);
						if(resposta) {
							byte[] bytes = new byte[8*1024];
							File newImage = new File(winepath);
							newImage.createNewFile();
							long length = (long) inStream.readObject();
							OutputStream outStreamImg = new FileOutputStream(newImage);
					        int count;
					        long total = 0;
					        //(total+1023 < length) &&
					        while ( (total < length) && (count = inStream.read(bytes)) > 0) {
					        	System.out.println("total antes: "+total);
					            outStreamImg.write(bytes, 0, count);
					            total+=count;
					            System.out.println("total depois: "+total);
					            System.out.println("Lenght total "+length );
					        }
					        System.out.println("sai");
					        outStreamImg.close();
					        outStream.writeObject(true);
						}
						//send to user
						break;
					}case BUY:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						String wineseller = (String) inStream.readObject();
						int quantity = (int) inStream.readObject();
						int resposta = wh.buyWine(winename, wineseller, user, quantity);
						outStream.writeObject(resposta);
						// send to user
						break;
					}case CLASSIFY:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						int stars = (int) inStream.readObject();
						boolean resposta  = wh.classify(winename, stars);
						outStream.writeObject(resposta);
						//send to user
						break;
					}case READ:{
						outStream.writeObject(true);
						String mensagesLer = mh.readMessagesbyUser(user);
						outStream.writeObject(mensagesLer);
						//send to user
						break;
					}case SELL:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						int value = (Integer) inStream.readObject();
						int quantity = (Integer) inStream.readObject();
						int resposta = wh.sellWine(winename, user, quantity, quantity);
						outStream.writeObject(resposta);
						
						//send to user
						break;
					}case TALK:{
						outStream.writeObject(true);
						String to = (String) inStream.readObject();
						String message = (String) inStream.readObject();
						boolean resposta = mh.addMensagem(user, to, message);
						outStream.writeObject(resposta);
						//send to user
						break;
					}case VIEW:{
						String winename = (String) inStream.readObject();
						String [] vervinho = wh.viewWine(winename); //needs changes - image related
						if(vervinho!= null) {
							outStream.writeObject(vervinho[0]);
							System.out.println(vervinho[0]);
							System.out.println(vervinho[1]);
							String[] temp = vervinho[1].split("[.]");
							outStream.writeObject(temp[1]);
							File file = new File(vervinho[1]);
							long length = file.length();
							outStream.writeObject(length);
					        byte[] bytes = new byte [1024];
					        InputStream inF = new FileInputStream(file);
					        int count;
					        
					        while ((count = inF.read(bytes)) > 0) {
					            outStream.write(bytes, 0, count);
					            outStream.flush();
					        }
					        System.out.println("fora");
					        inF.close();
						} else {
							outStream.writeObject("Erro");
						}
						//send to user*/
						break;
					}case WALLET:{
						outStream.writeObject(true);
						int wallet = wh.getWallerUser(user);
						outStream.writeObject(wallet);
						break;
					}default:
						break;
					}
				}
				
			} catch (IOException e1) {
						//e1.printStackTrace();
						connected = false;
				}
		}catch (ClassNotFoundException | IOException e1) {
			//e1.printStackTrace();
			connected = false;
		}
		System.out.println("Disconnecting...");
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//refazer
	//este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
	//nao faz qualquer tipo de autenticacao
	
		
	}
	
}



