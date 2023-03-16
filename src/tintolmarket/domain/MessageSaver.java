package tintolmarket.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MessageSaver {
private static final String SEPARAR = "/*-*/";
	
	public void addMensagem(String from, String to, String mensagem, BufferedWriter bw) {
		try {
			bw.write(to + SEPARAR + from + SEPARAR + mensagem);
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
