package tintolmarket.domain.catalogs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import tintolmarket.domain.MessageSaver;

public class MessageHandler {
	private String usersPath;
	private String messagesPath;
	private MessageSaver mst;
	
	public MessageHandler(String usersPath, String messagesPath) {
		this.setUsersPath(usersPath);
		this.setMessagesPath(messagesPath);
		this.mst = new MessageSaver();
	}
	public boolean addMensagem(String from, String to, String mensagem) {
		FileReader fr;
		try {
			fr = new FileReader(this.usersPath);
			BufferedReader br = new BufferedReader(fr);
			String check;
			boolean found = false;
			while((check = br.readLine())!= null) {
				String[] splt = check.split(":");
				if(splt[0].equals(to)) {
					FileWriter fw = new FileWriter(this.messagesPath, true);
					BufferedWriter bw = new BufferedWriter(fw);
					mst.addMensagem(from, to, mensagem, bw);
					bw.close();
					fw.close();
					found = true;
					break;
				}
			}
			br.close();
			fr.close();
			return found;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	public String readMessagesbyUser(String user) {
		FileReader fr;
		try {
			fr = new FileReader(this.messagesPath);
			BufferedReader br = new BufferedReader(fr);
			return mst.getMensagensbyUser(user, br, this.messagesPath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public String getMessagesPath() {
		return messagesPath;
	}
	public void setMessagesPath(String messagesPath) {
		this.messagesPath = messagesPath;
	}
	public String getUsersPath() {
		return usersPath;
	}
	public void setUsersPath(String usersPath) {
		this.usersPath = usersPath;
	}


}
