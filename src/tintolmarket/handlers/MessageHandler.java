package tintolmarket.handlers;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import tintolmarket.domain.Mensagem;
import tintolmarket.domain.catalogs.CatalogoMensagem;

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
	private MessageDigest md;
	private byte[] msgDigest;


	public MessageHandler(Object o,List<String> users, String mensagemPath) throws NoSuchAlgorithmException {
		this.catMensagem= CatalogoMensagem.getInstance(o);
		this.allUsers = users;
		this.messagesPath = mensagemPath;
		this.md = MessageDigest.getInstance("SHA");
		this.msgDigest = null;
	}

	/**
	 * Adicionar uma mensagem
	 * 
	 * @param from	quem envia a mensagem
	 * @param to	recetor da mensagem
	 * @param mensagem	a mensagem
	 * @return true se o user destinatario foi encontrado/existe e a mensagem foi guardado, false caso contrario
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean addMensagem(String from, String to, byte[] mensagem) throws ClassNotFoundException, IOException {
		if(!msgIntegrity(this.messagesPath)) {
			System.out.println("Erro ao adicionar mensagem: ficheiro corrompido");
			System.exit(-1);
		}
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
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public List<Mensagem> readMessagesbyUser(String user) throws ClassNotFoundException, IOException {
		if(!msgIntegrity(this.messagesPath)) {
			System.out.println("Erro ao ler as mensagens: ficheiro corrompido");
			System.exit(-1);
		}
		List<Mensagem> lm;
		synchronized (catMensagem){
			lm =  catMensagem.getMensagensToUser(user);
		}
		//Atualizar Info do catalogo
		updateMensagemFile(this.messagesPath);
		return lm;

	}

	private boolean updateMensagemFile(String mensagempath) throws ClassNotFoundException {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(mensagempath, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(catMensagem.getCatMensagem());
			out.close();
			fileOut.close();
			this.msgDigest = this.md.digest(readFile(mensagempath));
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

	private byte[] readFile(String filepath) throws IOException, ClassNotFoundException {
		File f = new File(filepath);
		FileInputStream fis = new FileInputStream(filepath);
		byte[] data = new byte[(int) f.length()];
		fis.close();
		return data;
	}

	private boolean msgIntegrity(String msgpath) throws IOException, ClassNotFoundException {
		byte[] data = readFile(msgpath);
		if(this.msgDigest == null) {
			this.msgDigest = md.digest(data);
			return true;
		} else {
			return MessageDigest.isEqual(md.digest(data), this.msgDigest);
		}
	}
}
