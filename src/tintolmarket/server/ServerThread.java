package tintolmarket.server;
import tintolmarket.domain.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	private Socket socket = null;
	private Operacao op;
	//will have mutex for access to .txt files and possibly others

	public ServerThread(Socket inSoc) {
		socket = inSoc;
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
					//verificação
					outStream.writeObject(new Boolean(true));
				}
				while(true) {
					this.op = (Operacao)inStream.readObject();
					switch(this.op) {
					case ADD:
						System.out.println("Funcao ADD");
						String winename = (String) inStream.readObject();
						String wineimage = (String) inStream.readObject();
						File winefile = new File("wines.txt");
						winefile.createNewFile();
						FileReader fr = new FileReader(winefile);
						BufferedReader br = new BufferedReader(fr);
						FileWriter fw = new FileWriter(winefile, true);
						BufferedWriter bw = new BufferedWriter(fw);
						String check;
						Boolean found = false;
						while((check = br.readLine()) != null) {
							String[] wineInfo = check.split(":");
							if(wineInfo[0].equals(winename)) {
								found = true;
								System.out.println("Vinho já tinha sido adicionado");
								break;
							}
						}
						if(!found) {
							bw.write(winename+":"+" "+":");
							bw.newLine();
							System.out.println("Vinho adicionado!");
						}
						break;
					case BUY:
						break;
					case CLASSIFY:
						break;
					case READ:
						break;
					case SELL:
						break;
					case TALK:
						break;
					case VIEW:
						break;
					case WALLET:
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


