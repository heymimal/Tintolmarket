package tintolmarket.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import tintolmarket.domain.Operacao;

public class Client_stub {
	private String username;
	private String pass;
	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client_stub(String username, String pass) {
		//criar estruturas para conexão com servidor
		//enviar username e pass para servidor
		setUsername(username);
		setPass(pass);
		clientSocket = null;
		
	}
	
	public Boolean connect() {//will receive values
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
	
	public boolean addwine(String winename, String wineimage) { //throws para ver + void -> String
		try {
			File file = new File(wineimage);
			if(file.exists()) {
				System.out.println("Existe");
				this.out.writeObject(Operacao.ADD);
				boolean b = (boolean) this.in.readObject();
				if (b) {
					System.out.println("pedido reconhecido");
					this.out.writeObject(winename);
					this.out.writeObject(wineimage);
					boolean resposta = (boolean) this.in.readObject();
					if(resposta) {
				        long length = file.length();
				        this.out.writeObject(length);
				        byte[] bytes = new byte [1024];
				        InputStream inF = new FileInputStream(file);
				        int count;
				        while ((count = inF.read(bytes)) > 0) {
				        	System.out.println(count);
				            this.out.write(bytes, 0, count);
				            this.out.flush();
				        }
				        System.out.println("hello");
				        inF.close();
				        boolean resposta2 = (boolean) this.in.readObject();
				        System.out.println("resposta chegou");
				        return resposta2;
					}
					return resposta;
					
					// devolve erro se já existir
				} else {
					System.out.println("erro no servidor");
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
		
	}

	public int sellwine(String winename, int winevalue, int winequantity) {
		try {
			this.out.writeObject(Operacao.SELL);
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				this.out.writeObject(winevalue); 
				this.out.writeObject(winequantity);
				int resposta = (Integer) this.in.readObject();
				return resposta;
				// devolve erro se nao existir o vinho
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return -2;
	}

	public String read() {
		try {
			this.out.writeObject(Operacao.TALK);
			boolean b = (boolean) this.in.readObject();
			if(b) {
				String s = (String)this.in.readObject();
				return s;
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
}

	public boolean talk(String username2, String message) {
		// MANDAR MENSAGENS
		try {
			this.out.writeObject(Operacao.TALK);
			boolean b = (boolean) this.in.readObject();
			if(b) {
				this.out.writeObject(username2);
				this.out.writeObject(message);
				
				boolean b2 = (boolean)this.in.readObject();
				return b2;
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}

	public boolean classify(String winename, int stars) {
		try {
			this.out.writeObject(Operacao.CLASSIFY);
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				this.out.writeObject(stars); 
				boolean resposta = (boolean) this.in.readObject();
				return resposta;
				
				// devolve erro se não existir
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
		
		
	}

	public int wallet() {
		try {
			this.out.writeObject(Operacao.WALLET);
			boolean b = (boolean) this.in.readObject();
			if(b) {
				int i = (Integer)this.in.readObject();
				return i;
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
		
	}

	public int buy(String winename, String wineseller, int winequantity) {
		try {
			this.out.writeObject(Operacao.BUY);
			boolean b = (boolean) this.in.readObject();
			if (b) {
				System.out.println("pedido reconhecido");
				this.out.writeObject(winename);
				this.out.writeObject(wineseller);
				this.out.writeObject(winequantity);
				
				int resposta = (Integer)this.in.readObject();
				return resposta;
				
				// devolve erro se vinho não existir, se wallet atual < valor, quantidade tem de existir
			} else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -5;
		
		
	}

	public String view(String winename) { // TO CHANGE
		try {
			this.out.writeObject(Operacao.VIEW);
			boolean b = true;
			System.out.println(b);
			if (b) {
				System.out.println("pedido reconhecido");
				System.out.println(winename);
				this.out.writeObject(winename);
				
				
				
					String info = (String) this.in.readObject();
					System.out.println(info);
					String extensao = (String) this.in.readObject();
					System.out.println(extensao);
					long tamanho = (long) this.in.readObject();
					System.out.println(tamanho);
					byte[] bytes = new byte[1024];
					String newPath = "view_"+winename+"."+extensao;
					File newImage = new File(newPath);
					newImage.createNewFile();
					OutputStream outStreamImg = new FileOutputStream(newImage);
			        int count;
			        long total = 0;
			        while ( (total < tamanho) && (count = this.in.read(bytes)) > 0 ) {
			        	System.out.println("tamanho antes: "+total);
			        	total += count;
			        	//System.out.println(total);
			        	//System.out.println((float)((total/tamanho)*100) + "%");
			        	
			            outStreamImg.write(bytes, 0, count);
			            System.out.println("tamanho depois: "+total);
			            System.out.println(tamanho);
			        }
			        /*
			         * byte[] bytes = new byte[16*1024];
							File newImage = new File(winepath);
							newImage.createNewFile();
							OutputStream outStreamImg = new FileOutputStream(newImage);
					        int count;
					        while ((count = inStream.read(bytes)) > 0) {
					            outStreamImg.write(bytes, 0, count);
					        }
					        outStreamImg.close();
					        outStream.writeObject(true);
			         */
			        System.out.println("fora cl");
			        outStreamImg.close();
			        System.out.println(newImage.getAbsolutePath());
			        System.out.println(newImage.getPath());
			        return info + "\n Imagem do vinho encontra-se em: "+ newPath;
				}
				
				// devolve erro se não existir
			else {
				System.out.println("erro no servidor");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "Erro";
		
		
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
