package tintolmarket.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import tintolmarket.domain.MessageSaver;

/**
 * Handler das Mensagens
 * 
 * @author fc54446, fc54409, fc54933
 *
 */
public class MessageHandler {
	
	//private String usersPath;
	private String messagesPath;
	private MessageSaver mst;

	private List<String> allUsers;
	
	/**
	 * Construtor do Handler
	 * 
	 * @param usersPath		path do ficheiro dos users
	 * @param messagesPath	path do ficheiro das mensagens
	 */
	public MessageHandler(String usersPath, String messagesPath,List<String> allUsers) {
		//this.setUsersPath(usersPath);
		this.setMessagesPath(messagesPath);
		this.mst = new MessageSaver();
		this.allUsers = allUsers;
	}
	
	
	/**
	 * Adicionar uma mensagem
	 * 
	 * @param from	quem envia a mensagem
	 * @param to	recetor da mensagem
	 * @param mensagem	a mensagem
	 * @return true se o user destinatario foi encontrado/existe e a mensagem foi guardado, false caso contrario
	 */
	public boolean addMensagem(String from, String to, String mensagem) {

		boolean toExists = this.allUsers.contains(from);
		try{
			if(toExists){
				FileWriter fw = new FileWriter(this.messagesPath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				synchronized (mst) {
					mst.addMensagem(from, to, mensagem, bw);
					bw.close();
					fw.close();
				}

			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return toExists;
	}
	
	/**
	 * Le as mensagens do utilizador user
	 * 
	 * @param user	o utilizador
	 * @return	as mensagens, null caso ocorra algum erro
	 */
	public String readMessagesbyUser(String user) {
		FileReader fr;
		try {
			fr = new FileReader(this.messagesPath);
			BufferedReader br = new BufferedReader(fr);
			synchronized (mst) {
				return mst.getMensagensbyUser(user, br, this.messagesPath);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * @return path do ficheiro das mensagens
	 */
	public String getMessagesPath() {
		return messagesPath;
	}
	/**
	 * @param messagesPath	path do ficheiro das mensagens
	 */
	public void setMessagesPath(String messagesPath) {
		this.messagesPath = messagesPath;
	}


	public void addUser(String user) {
		this.allUsers.add(user);
	}
}
