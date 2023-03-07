package tintolmarket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	private String username;
	private String pass;
	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client(String username, String pass) {
		//criar estruturas para conexão com servidor
		//enviar username e pass para servidor
		setUsername(username);
		setPass(pass);
		clientSocket = null;
		
	}
	
	public Boolean connect() {
		try {
			this.clientSocket = new Socket(InetAddress.getLocalHost(),12345);
			this.out = new ObjectOutputStream(clientSocket.getOutputStream());
			this.in = new ObjectInputStream(clientSocket.getInputStream());
			
			this.out.writeObject(this.username);
			this.out.writeObject(this.pass);
			
			Boolean b = (Boolean) this.in.readObject();
			return b;
		}catch (IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		return null;
	}
	
	public void addwine(String winename, String wineimage) { //throws para ver + void -> String
		try {
			this.out.writeObject("add");
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				this.out.writeObject(wineimage); // NÃO É ISTO
				
				// devolve erro se já existir
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public void sellwine(String winename, int winevalue, int winequantity) {
		try {
			this.out.writeObject("sell");
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				this.out.writeObject(winevalue); 
				this.out.writeObject(winequantity);
				// devolve erro se nao existir o vinho
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	public void read() {
		// LER MENSAGENS
	}

	public void talk(String username2, String message) {
		// MANDAR MENSAGENS
		
	}

	public void classify(String winename, int stars) {
		try {
			this.out.writeObject("classify");
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				this.out.writeObject(stars); 
				
				// devolve erro se não existir
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	public void wallet() {
		// PARA O SERVIDOR - LATER
		
	}

	public void buy(String winename, String wineseller, int winequantity) {
		try {
			this.out.writeObject("buy");
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				this.out.writeObject(wineseller);
				this.out.writeObject(winequantity);
				
				// devolve erro se vinho não existir, se wallet atual < valor, quantidade tem de existir
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	public void view(String winename) {
		try {
			this.out.writeObject("view");
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				
				// devolve erro se não existir
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	public String getUsername() {
		return username;
	}

	private void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	private void setPass(String pass) {
		this.pass = pass;
	}

	

}
