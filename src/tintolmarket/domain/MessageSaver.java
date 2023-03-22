package tintolmarket.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe MessageServer
 * 
 * @author fc54446, fc54409, fc54933
 *
 */
public class MessageSaver {
	
private static final String SEPARAR = "/*-*/";
	
	/**
	 * Escreve a mensagem para o ficheiro das mensagens
	 * 
	 * @param from	quem envia a mensagem
	 * @param to	recetor da mensagem
	 * @param mensagem	  a mensagem
	 * @param bw	bufferedWriter
	 */
	public void addMensagem(String from, String to, String mensagem, BufferedWriter bw) {
		try {
			bw.write(to + SEPARAR + from + SEPARAR + mensagem);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recebe as mensagens encaminhadas para o utilizador user do ficheiro das mensagens. Apaga as mensagens recebidas do ficheiro.
	 * 
	 * @param user	o utilizador
	 * @param br	bufferedReader
	 * @param messagePath	caminho do ficheiro das mensages
	 * @return	mensagens encaminhadas ao utilizador
	 */
	public String getMensagensbyUser(String user, BufferedReader br, String messagePath) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbtemp = new StringBuilder();
		String check;
		try {
			while((check = br.readLine()) != null) {
				String[] temp = check.split(SEPARAR);
				if(temp[0].equals(user)) {
					sb.append(temp[2]+":"+temp[4]+ "\n");
				} else {
					sbtemp.append(check + "\n");
				}
				
			}
			FileWriter fw = new FileWriter(messagePath, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sbtemp.toString());
			bw.close();
			fw.close();
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

}
