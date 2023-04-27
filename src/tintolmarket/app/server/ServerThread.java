package tintolmarket.app.server;
import tintolmarket.app.security.Macs;
import tintolmarket.app.security.ServerSecurity;
import tintolmarket.domain.blockchain.Transaction;
import tintolmarket.domain.*;
import tintolmarket.handlers.*;

//import tintolmarket.server.*;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Classe ServerThread
 * 
 *  @author fc54446, fc54409, fc54933
 *
 */
public class ServerThread extends Thread {

	private Socket socket = null;
	private Operacao op;
	private WineHandler wh;
	private MessageHandler mh;

	private BlockchainHandler bh;

	private ServerSecurity auth;
	//will have mutex for access to .txt files and possibly others

	/**
     * Construtor do ServerThread
     *
     * @param inSoc socket
     * @param wh    handler dos vinhos
     * @param mh    handler das mensagens
     * @param auth
     */
	public ServerThread(Socket inSoc, WineHandler wh, MessageHandler mh,BlockchainHandler bh, ServerSecurity auth) {
		socket = inSoc;
		this.wh = wh;
		this.mh = mh;
		this.bh = bh;
		this.auth = auth;
		System.out.println("thread do server para cada cliente");
	}

	@Override
	public void run(){
		boolean connected = false;
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

			String user = null;
			try {
				user = (String)inStream.readObject();
				if (user.length() != 0){
					outStream.writeObject(true);
					boolean[] isconnected = auth.serverAutenticate(outStream,inStream,user);
					boolean checkAuth = isconnected[0];
					boolean found = isconnected[1];
					if(checkAuth &&!found) {
						this.wh.addWalletUser(user);
						this.mh.addUser(user);
					}
					outStream.writeObject(checkAuth);
					connected = checkAuth;
				}
				if(connected){
					System.out.println("Connection established!");
				}
				while(connected) {
					System.out.println("Waiting for op...");
					this.op = (Operacao)inStream.readObject();
					System.out.println("Operarion: "+ this.op);
					switch(this.op) {
						case ADD:{
							if(!auth.fileIntegrity(Macs.WINES)) {
								System.out.println("Ficheiro dos wines corrupto");
								System.exit(-1);
							}
							outStream.writeObject(true);
							String winename = (String) inStream.readObject();
							String temp = (String) inStream.readObject();
							String winepath= "server_"+winename+"."+temp.split("[.]")[1];
							boolean resposta = wh.addWine(winename,winepath);
							outStream.writeObject(resposta);
							if(resposta) {
								byte[] bytes = new byte[8*1024];
								File newImage = new File(wh.getVinhosFolder(),winepath);
								newImage.createNewFile();
								long length = (long) inStream.readObject();
								OutputStream outStreamImg = new FileOutputStream(newImage);
								int count;
								long total = 0;
								while ( (total < length) && (count = inStream.read(bytes)) > 0) {
									outStreamImg.write(bytes, 0, count);
									total+=count;
								}
								outStreamImg.close();
								outStream.writeObject(true);
							}
							auth.updateMacFile(Macs.WINES);
							//send to user
							break;
						}case BUY:{
							if(!auth.fileIntegrity(Macs.WINES)) {
								System.out.println("Ficheiro dos wines corrupto");
								System.exit(-1);
							}
							if(!auth.fileIntegrity(Macs.WALLET)) {
								System.out.println("Ficheiro das wallets corrupto");
								System.exit(-1);
							}
							outStream.writeObject(true);
							String winename = (String) inStream.readObject();
							String wineseller = (String) inStream.readObject();
							int value = wh.getPriceWine(winename,wineseller);
							outStream.writeObject(value);
							int quantity = (int) inStream.readObject();
							Tipo t = (Tipo) inStream.readObject();
							byte[] signature = (byte[]) inStream.readObject();
							boolean b = auth.verificaAssinatura(winename,value,quantity,signature,user,t);
							if(b){
								int resposta = wh.buyWine(winename, wineseller, user, quantity);
								if(resposta == 1){
									Transaction tr = new Transaction(winename, quantity, value, user, t);
									tr.setSignature(signature);
									bh.addTransaction(tr);
									// criar objeto transacao e operacoes respetivas da blockchain
								}

								outStream.writeObject(resposta);
							} else {
								outStream.writeObject(false);
							}
							auth.updateMacFile(Macs.WINES);
							auth.updateMacFile(Macs.WALLET);
							break;
						}case CLASSIFY:{
							if(!auth.fileIntegrity(Macs.WINES)) {
								System.out.println("Ficheiro dos wines corrupto");
								System.exit(-1);
							}
							outStream.writeObject(true);
							String winename = (String) inStream.readObject();
							int stars = (int) inStream.readObject();
							boolean resposta  = wh.classify(winename, stars);
							outStream.writeObject(resposta);
							auth.updateMacFile(Macs.WINES);
							//send to user
							break;
						}case READ:{
							if(!auth.fileIntegrity(Macs.MESSAGES)) {
								System.out.println("Ficheiro das messages corrupto");
								System.exit(-1);
							}
							outStream.writeObject(true);
							List<Mensagem> mensagesLer = mh.readMessagesbyUser(user);
							outStream.writeObject(mensagesLer);
							auth.updateMacFile(Macs.MESSAGES);
							//send to user
							break;
						}case SELL:{
							if(!auth.fileIntegrity(Macs.WINES)) {
								System.out.println("Ficheiro dos wines corrupto");
								System.exit(-1);
							}
							outStream.writeObject(true);
							String winename = (String) inStream.readObject();
							int value = (Integer) inStream.readObject();
							int quantity = (Integer) inStream.readObject();
							Tipo t = (Tipo) inStream.readObject();
							byte signature[] = (byte[]) inStream.readObject( );
							boolean b = auth.verificaAssinatura(winename,value,quantity,signature,user,t);
							if(b){
								int resposta = wh.sellWine(winename, user, quantity, value);
								if(resposta==1){
									// criar objeto transacao e operacoes respetivas da blockchain
									Transaction tr = new Transaction(winename, quantity, value, user, t);
									tr.setSignature(signature);
									bh.addTransaction(tr);
								}
								outStream.writeObject(resposta);
							} else {
								outStream.writeObject(-5);
							}
							auth.updateMacFile(Macs.WINES);
							//send to user
							break;
						}case TALK:{
							if(!auth.fileIntegrity(Macs.MESSAGES)) {
								System.out.println("Ficheiro das messages corrupto");
								System.exit(-1);
							}
							outStream.writeObject(true);
							String to = (String) inStream.readObject();
							byte[] message = (byte[]) inStream.readObject();
							boolean resposta = mh.addMensagem(user, to, message);
							outStream.writeObject(resposta);
							auth.updateMacFile(Macs.MESSAGES);
							//send to user
							break;
						}case VIEW:{
							String winename = (String) inStream.readObject();
							String [] vervinho = wh.viewWine(winename);
							if(vervinho!= null) {
								outStream.writeObject(true);
								outStream.writeObject(vervinho[0]);
								String[] temp = vervinho[1].split("[.]");
								outStream.writeObject(temp[1]);
								File file = new File(wh.getVinhosFolder(),vervinho[1]);
								long length = file.length();
								outStream.writeObject(length);
								byte[] bytes = new byte [1024];
								InputStream inF = new FileInputStream(file);
								int count;
								while ((count = inF.read(bytes)) > 0) {
									outStream.write(bytes, 0, count);
									outStream.flush();
								}
								inF.close();
							} else {
								outStream.writeObject(false);
							}
							//send to user*/
							break;
						}case WALLET: {
							outStream.writeObject(true);
							int wallet = wh.getWallerUser(user);
							outStream.writeObject(wallet);
							break;
						}case LIST: {
							outStream.writeObject(true);
							String transacoes =bh.list();
							outStream.writeObject(transacoes);
							break;
						}default:
							break;
					}
				}

			} catch (IOException e1) {
				connected = false;
			} catch (NoSuchPaddingException e) {
				throw new RuntimeException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (ClassNotFoundException | IOException e1) {
			connected = false;
		}
		System.out.println("Disconnecting...");
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//refazer
		//este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
		//nao faz qualquer tipo de autenticacao


	}

}



