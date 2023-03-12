package tintolmarket.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	private Socket socket = null;
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
					outStream.writeObject(new Boolean(true));
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


