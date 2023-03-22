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
public class Client_stub {
	private String username;
	private String pass;
	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	/**
	 * Construtor do Client_stub
	 * 
	 * @param username
	 * @param pass
	 */
	public Client_stub(String username, String pass) {
		//criar estruturas para conexão com servidor
		//enviar username e pass para servidor
		setUsername(username);
		setPass(pass);
		clientSocket = null;
		
	}
	
	/**
	 * Conexao do Cliente
	 * @return true se conectar, false se nao conectar, null se houver algum erro/excecao
	 */
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

	/**
	 * Funcao para comunicar com o servidor acerca da operacao de ler as mensagens recebidas.
	 * 
	 * @return da mensagem recebida s, null em caso de erro.
	 */
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
	
	
	/**
	 * Funcao para comunicar com o servidor acerca da operacao de ver as informacoes associadas ao vinho winename.
	 * 
	 * @param winename	o vinho a ser visto
	 * @return a informacao sobre o vinho e o caminho para a sua imagem, 'Erro' caso ocorra algum erro
	 */
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
