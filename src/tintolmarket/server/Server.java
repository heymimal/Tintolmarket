package tintolmarket.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {
	public static void main(String[] args) {
		System.out.println("servidor: main");
		Server server = new Server();
		server.startServer();
	}

	public void startServer (){
		ServerSocket sSoc = null;
        
		try {
			sSoc = new ServerSocket(12345);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
         
		while(true) {
			try {
				Socket inSoc = sSoc.accept();
				System.out.println("hello?");
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
		    }
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		}
		//sSoc.close();
	}


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("thread do server para cada cliente");
		}
 
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
}
