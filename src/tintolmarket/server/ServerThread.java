package tintolmarket.server;
import tintolmarket.domain.*;

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
					case ADD:{
						outStream.writeObject(true);
						System.out.println("Funcao ADD");
						String winename = (String) inStream.readObject();
						String wineimage = (String) inStream.readObject();
						if(wineExists(winename, Server.wines)) {
							System.out.println("Vinho já tinha sido adicionado");
						}else {
							FileWriter fw = new FileWriter(Server.wines, true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(winename+":"+" "+":");
							bw.newLine();
							bw.close();
							fw.close();
							System.out.println("Vinho adicionado!");
						}
						break;
					}case BUY:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						String wineseller = (String) inStream.readObject();
						int quantity = (int) inStream.readObject();
						String check;
						File winefile = new File("wines.txt");
						FileReader fr = new FileReader(winefile);
						BufferedReader br = new BufferedReader(fr);
						FileWriter fw = new FileWriter(winefile, true);
						BufferedWriter bw = new BufferedWriter(fw);
						boolean found = false;
						//Falta tratar de mudanças de estado
						while((check = br.readLine()) != null) {
							String[] wineInfo = check.split(":");
							if(wineInfo[0].equals(winename)) {
								found = true;
								String[] userinfo = wineInfo[2].split(" ");
								for(String xd:userinfo) {
									String[] uservalues = xd.split("-");
									if(uservalues[0].equals(wineseller)) {
										if(Integer.valueOf(uservalues[1]) < quantity) {
											System.out.println("Não há unidades suficientes neste comprador");
										} else {
											int custo = quantity*Integer.valueOf(uservalues[2]);
											System.out.println("checking wallet");
											int walletValue = wallet(user,"wallet.txt");
											if(walletValue >= custo) {
												System.out.println("Compra pode ser efetuada");
											} else {
												System.out.println("Falta de saldo");
											}
											
										}
									}
								}
								break;
							} else {
								System.out.println("Vinho não existe");
							}
						}
						break;
					}case CLASSIFY:{
						outStream.writeObject(true);
						String winename = (String) inStream.readObject();
						int stars = (int) inStream.readObject();
						// Check if exists
						boolean found = wineExists(winename, "wines.txt");
						if(!found) {
							System.out.println("Vinho não existe");
						} else {
							// modificar ficheiro
						}
						break;
					}case READ:
						break;
					case SELL:
						break;
					case TALK:
						break;
					case VIEW:
						break;
					case WALLET:
						outStream.writeObject(true);
						int wallet = wallet(user,"wallet.txt");
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
	public int wallet(String user,String filepath) {
		File wallet = new File(filepath);
		FileReader wfr = null;
		try {
			wfr = new FileReader(wallet);
			BufferedReader wbr = new BufferedReader(wfr);
			Boolean found = false;
			String userwallet;
			while((userwallet = wbr.readLine()) != null) {
				String[] uw = userwallet.split(":");
				if(uw[0].equals(user)){
					return Integer.valueOf(uw[1]);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
		
	}
	public boolean wineExists(String winename,String filepath) {
		File winefile = new File("wines.txt");
		try {
			winefile.createNewFile();
			FileReader fr = new FileReader(filepath);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(winefile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String check;
			while((check = br.readLine()) != null) {
				String[] wineInfo = check.split(":");
				if(wineInfo[0].equals(winename)) {
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}



