package tintolmarket.app.client;

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
/**
 * Classe do Client_stub
 * 
 * @author fc54446, fc54409, fc54933
 */
public class ClientConector {
	private final String PORT_DEFAULT = "12345";
	private String username;
	private String pass;
	private String[] address;
	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	/**
	 * Construtor do Client_stub
	 * 
	 * @param username
	 * @param pass
	 * @param password 
	 */
	public ClientConector(String address, String username, String password) {
		//criar estruturas para conexão com servidor
		//enviar username e pass para servidor
		this.address = setAddress(address);
		setUsername(username);
		setPass(password);
		clientSocket = null;
		
	}
	
	private String[] setAddress(String address) {
		// TODO Auto-generated method stub
		String[] returnv = new String [2];
		if(address.contains(":")) {
			
			String [] addSplit = address.split(":");
			returnv[0] = addSplit[0];
			if(addSplit.length == 2) {
				if(addSplit[1].length() == 5) {
					returnv[1] = addSplit[1];
;				}
			} else {
				returnv[1] = this.PORT_DEFAULT;
			}
		}
		return returnv;
	}

	/**
	 * Conexao do Cliente
	 * @return true se conectar, false se nao conectar, null se houver algum erro/excecao
	 */
	public Boolean connect() {//will receive values
		try {
			
			this.clientSocket = new Socket(this.address[0],Integer.parseInt(this.address[1]));
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
	
	
	/**
	 * Funcao para comunicar com o servidor acerca da operacao de adicionar vinho.
	 * 
	 * @param winename	  nome do vinho
	 * @param wineimage	   imagem (caminho da imagem) do vinho	
	 * @return true se o vinho for bem adicionado e imagem bem transferida, false caso aconteca um erro ou o vinho ja existir
	 */
	public boolean addwine(String winename, String wineimage) { //throws para ver + void -> String
		try {
			File file = new File(wineimage);
			if(file.exists()) {
				this.out.writeObject(Operacao.ADD);
				boolean b = (boolean) this.in.readObject();
				if (b) {
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
				            this.out.write(bytes, 0, count);
				            this.out.flush();
				        }
				        inF.close();
				        boolean resposta2 = (boolean) this.in.readObject();
				        return resposta2;
					}
					return resposta;
					
					// devolve erro se já existir
				} else {
					System.out.println("Erro no servidor.");
				}
			}
			System.out.println("Ficheiro nao existe.");
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
		
	}

	/**
	 * Funcao para comunicar com o servidor acerca da operacao de vender vinho.
	 * 
	 * @param winename	  nome do vinho
	 * @param winevalue		preco do vinho a vender		
	 * @param winequantity	  quantidade a vender do vinho
	 * 
	 * @return	-1 se o vinho nao existe, 0 se existir mas com um preco diferente, 1 caso tenha sido realizado com sucesso, 
	 * -2 em caso de erro
	 */
	public int sellwine(String winename, int winevalue, int winequantity) {
		try {
			this.out.writeObject(Operacao.SELL);
			boolean b = (boolean) this.in.readObject();
			if (b) {
				this.out.writeObject(winename);
				this.out.writeObject(winevalue); 
				this.out.writeObject(winequantity);
				int resposta = (Integer) this.in.readObject();
				return resposta;
				// devolve erro se nao existir o vinho
			} else {
				System.out.println("Erro no servidor.");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return -2;
	}

	/**
	 * Funcao para comunicar com o servidor acerca da operacao de ler as mensagens recebidas.
	 * 
	 * @return da mensagem recebida s, null em caso de erro.
	 */
	public String read() {
		try {
			this.out.writeObject(Operacao.READ);
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

	/**
	 * Funcao para comunicar com o servidor acerca da operacao de enviar mensagem para o utilizador.
	 * 
	 * @param username2		utilizador para o qual se vai enviar a mensagem
	 * @param message		a mensagem enviada
	 * 
	 * @return true se a mensagem for bem enviada, false caso contrario
	 */
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

	/**
	 * Funcao para comunicar com o servidor acerca da operacao de avaliar/classificar os vinhos.
	 * 
	 * @param winename		nome do vinho a ser classificado
	 * @param stars			classificacao atribuida
	 * @return	true se a classificacao for bem efetuada, false caso contrario
	 */
	public boolean classify(String winename, int stars) {
		try {
			this.out.writeObject(Operacao.CLASSIFY);
			boolean b = (boolean) this.in.readObject();
			if (b) {
				this.out.writeObject(winename);
				this.out.writeObject(stars); 
				boolean resposta = (boolean) this.in.readObject();
				return resposta;
				
				// devolve erro se não existir
			} else {
				System.out.println("Erro no servidor.");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
		
		
	}

	/**Funcao para comunicar com o servidor acerca da operacao de consultar a waller/carteira de um utilizador.
	 * 
	 * @return o valor da wallet, -1 em caso de algum erro
	 */
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

	/**
	 * Funcao para comunicar com o servidor acerca da operacao de comprar unidades de vinho a dado utilizador.
	 * 
	 * @param winename		vinho a ser comprado
	 * @param wineseller	vendedor do vinho
	 * @param winequantity		quantidade de vinho a ser comprada
	 * 
	 * @return -4 se o wineseller nao existe, -3 se a winequantity nao esta disponivel, -2 se o utilizador comprador nao possui saldo
	 * suficiente, -1 se o vinho winename nao existe, 1 caso a compra seja efetuada, -5 caso ocorra algum erro.
	 */
	public int buy(String winename, String wineseller, int winequantity) {
		try {
			this.out.writeObject(Operacao.BUY);
			boolean b = (boolean) this.in.readObject();
			if (b) {
				this.out.writeObject(winename);
				this.out.writeObject(wineseller);
				this.out.writeObject(winequantity);
				
				int resposta = (Integer)this.in.readObject();
				return resposta;
				
				// devolve erro se vinho não existir, se wallet atual < valor, quantidade tem de existir
			} else {
				System.out.println("Erro no servidor.");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -5;
		
		
	}
	
	
	/**
	 * Funcao para comunicar com o servidor acerca da operacao de ver as informacoes associadas ao vinho winename.
	 * 
	 * @param winename	o vinho a ser visto
	 * @return a informacao sobre o vinho e o caminho para a sua imagem, 'Erro' caso ocorra algum erro
	 */
	public String view(String winename) { // TO CHANGE
		try {
			this.out.writeObject(Operacao.VIEW);
			this.out.writeObject(winename);
			String info = (String) this.in.readObject();
			String extensao = (String) this.in.readObject();
			long tamanho = (long) this.in.readObject();
			byte[] bytes = new byte[1024];
			String newPath = "view_"+winename+"."+extensao;
			File newImage = new File(newPath);
			newImage.createNewFile();
			OutputStream outStreamImg = new FileOutputStream(newImage);
	        int count;
	        long total = 0;
	        while ( (total < tamanho) && (count = this.in.read(bytes)) > 0 ) {
	        	total += count;
	            outStreamImg.write(bytes, 0, count);
	        }
	        outStreamImg.close();
	        System.out.println("Abs:" + newImage.getAbsolutePath());
	        System.out.println("normal "+newImage.getPath());
	        return info + "\nImagem do vinho encontra-se em: "+ newPath + "\n";
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "Erro";
		
		
	}

	
	/*public String getUsername() {
		return username;
	}*/

	/**
	 * @param username
	 */
	private void setUsername(String username) {
		this.username = username;
	}

	/*public String getPass() {
		return pass;
	} */

	/**
	 * @param pass
	 */
	private void setPass(String pass) {
		this.pass = pass;
	}

	

}
