package tintolmarket.handlers;

import java.io.*;
import java.util.List;

import tintolmarket.domain.Mensagem;
import tintolmarket.domain.catalogs.CatalogoMensagem;
import tintolmarket.domain.catalogs.CatalogoWallet;
import tintolmarket.domain.catalogs.CatalogoWine;

/**
 * Handler das Mensagens
 * 
 * @author fc54446, fc54409, fc54933
 *
 */
public class MessageHandler {
	
	private CatalogoMensagem catMensagem;

	private List<String> allUsers;

	private String messagesPath;

	public MessageHandler(Object o,List<String> users, String mensagemPath) {
		this.catMensagem= CatalogoMensagem.getInstance(o);
		this.allUsers = users;
		this.messagesPath = mensagemPath;
	}

	/**
	 * Adicionar uma mensagem
	 * 
	 * @param from	quem envia a mensagem
	 * @param to	recetor da mensagem
	 * @param mensagem	a mensagem
	 * @return true se o user destinatario foi encontrado/existe e a mensagem foi guardado, false caso contrario
	 */
	public boolean addMensagem(String from, String to, byte[] mensagem) {

		boolean toExists = this.allUsers.contains(from);
		if(toExists){
			// Check file (?) -> verificar integridada
			synchronized (catMensagem){
				catMensagem.addMensagem(from,to,mensagem);
			}//Atualizar ficheiro que guarda o catalogo
		}
		updateMensagemFile(this.messagesPath);


		return toExists;
	}
	
	/**
	 * Le as mensagens do utilizador user
	 * 
	 * @param user	o utilizador
	 * @return	as mensagens, null caso ocorra algum erro
	 */
	public List<Mensagem> readMessagesbyUser(String user) {

		// Verificar Integridade
		List<Mensagem> lm;
		synchronized (catMensagem){
			lm =  catMensagem.getMensagensToUser(user);
		}
		//Atualizar Info do catalogo
		updateMensagemFile(this.messagesPath);
		return lm;

	}

	private boolean updateMensagemFile(String mensagempath) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(mensagempath, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(catMensagem.getCatMensagem());
			out.close();
			fileOut.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @return path do ficheiro das mensagens
	 */

	public void addUser(String user) {
		this.allUsers.add(user);
	}
}
