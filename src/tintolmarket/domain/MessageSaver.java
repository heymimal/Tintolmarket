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
	 * Escrever a mensagem
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Receber as mensagens do utilizador user
	 * 
	 * @param user	o utilizador
	 * @param br	bufferedReader
	 * @param messagePath	caminho da mensagem
	 * @return	a mensagem do user, null caso ocorra algum erro (duvidas!!!!!!!!!)
	 */
	public String getMensagensbyUser(String user, BufferedReader br, String messagePath) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbtemp = new StringBuilder();
		String check;
		try {
			while((check = br.readLine()) != null) {
				System.out.println("Reading this line: ");
				System.out.println(check);
				String[] temp = check.split(SEPARAR);
				for(String t:temp) {
					System.out.println(t);
				}
				if(temp[0].equals(user)) {
					System.out.println("USER FOUND");
					System.out.println(temp[1]+ ":" + temp[2]);
					sb.append(temp[2]+":"+temp[4]+ "\n");
				} else {
					sbtemp.append(check + "\n");
				}
				
			}
			FileWriter fw = new FileWriter(messagePath, false);
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println("ola \n" + sb.toString());
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
