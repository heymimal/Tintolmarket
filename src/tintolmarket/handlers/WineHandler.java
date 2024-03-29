package tintolmarket.handlers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import tintolmarket.domain.catalogs.*;
import tintolmarket.domain.Wine;
import tintolmarket.app.server.Server;
import tintolmarket.domain.Wallet;

/**
 * Handler dos Vinhos
 * 
 * @author fc54446, fc54409, fc54933
 *
 */
public class WineHandler {
	private CatalogoWine catwine;
	private CatalogoWallet catwallet;
	private String wines;
	private String wallet;
	
	/**
	 * Construtor do Handler dos Vinhos
	 */
	public WineHandler() {
		this.catwine = CatalogoWine.getInstance(null);
		this.catwallet = CatalogoWallet.getInstance(null);
	}
	
	/**
	 * @param o1	object 1
	 * @param o2	object 2
	 * @param wines path para ficheiro dos wines
	 * @param wallet path para ficheiro das wallets
	 */
	public WineHandler(String wines,String wallet,Object o1,Object o2) {
		this.wines = wines;
		this.wallet = wallet;
		this.catwine = CatalogoWine.getInstance(o1);
		this.catwallet = CatalogoWallet.getInstance(o2);
	}
	
	/**
	 * @return catalogo de vinhos
	 */
	private List<Wine> getCatWine(){
		return this.catwine.getCatWine();
	}
	
	/**
	 * Adicionar vinho para venda
	 * 
	 * @param winename	nome do vinho
	 * @param winePath	caminho do vinho
	 * @return false se o vinho ja existe, true se nao existir e for bem adicionado
	 */
	public boolean addWine(String winename, String winePath) {
		boolean b = this.catwine.addWine(winename, winePath);
		if(b) {
			System.out.println("Vinho ainda não existia");
			this.updateWineFile(this.wines);
		}
		return b;
	}
	
	/**
	 * Adicionar wallet ao utilizador username
	 * 
	 * @param username	nome do utilizador 
	 * @return true se for adicionado com sucesso, false caso contrario (??)
	 */
	public boolean addWalletUser(String username) {
		boolean b= this.catwallet.addWallet(username);
		this.updateWalletFile(this.wallet);
		return b;
	}
	
	/**
	 * Venda de vinho
	 * 	
	 * @param winename	nome do vinho a ser vendido
	 * @param username	nome de quem quer vender o vinho
	 * @param quantity	quantidade a ser vendida
	 * @param price		preco pelo qual o vinho vai ser vendido
	 * @return 1 se tudo correr bem, 0 se houver diferencas no preco do vinho, -1 se o vinho nao existe
	 */
	public int sellWine(String winename,String username,int quantity, int price) {
		int n = this.catwine.sellWine(winename, username, quantity, price);
		if(n == 1) {
			this.updateWineFile(this.wines);
		}
		return n;
		
	}
	
	/**
	 * Informacoes sobre o vinho winename
	 * 	
	 * @param winename	nome do vinho
	 * @return array de string com todas as informacoes sobre o vinho e o caminho para a imagem associada
	 */
	public String[] viewWine(String winename) {
		return this.catwine.viewWine(winename);
	}
	
	
	/**
	 * Compra de vinho. Se a operacao corre de acordo com o esperado, altera as wallets dos utilizadores envolvidos na transacao
	 * 
	 * @param winename	nome do vinho a ser comprado
	 * @param seller	nome de quem vende o vinho
	 * @param user		utilizador que vai comprar o vinho
	 * @param quantity	quantidade de vinho que vai ser comprada
	 * @return 0 se o user nao existe, 1 caso a compra seja efetuada, -1 caso o vinho nao exista,
	 * -4 caso o cliente nao exista, -3 caso a quantidade seja superior e existente, -2 caso nao tenha saldo suficiente
	 */
	public int buyWine(String winename, String seller, String user, int quantity) {
		Wallet walletUser = getWalletUser(user);
		if(walletUser.getClass() == Wallet.class) {
			int walletValue = walletUser.getWallet();
			int change = this.catwine.buyWine(winename, seller, quantity, walletValue);
			if(change <= 0) {
				return change;
			}
			this.updateWineFile(this.wines);
			Wallet walletSeller = getWalletUser(seller);
			if(walletSeller.getClass() == Wallet.class) {
				int new_walletUser = walletValue - change;
				int new_walletSeller = walletSeller.getWallet() + change;
				walletSeller.changeWallet(new_walletSeller);
				walletUser.changeWallet(new_walletUser);
				this.catwallet.changeWallet(walletUser);
				this.catwallet.changeWallet(walletSeller);
				if(this.updateWalletFile(this.wallet)) {
					System.out.println("updated wallet file");
				}
				return 1;
			}
			
		}
		return 0; // user non_existent -> impossible
		
		
	}
	
	/**
	 * @param username	nome do utilizador
	 * @return valor da wallet do utilizador username
	 */
	public int getWallerUser(String username) {
		return this.catwallet.getWalletValue(username);
	}
	
	/**
	 * @param username	nome do utilizador
	 * @return	wallet do utilizador username
	 */
	private Wallet getWalletUser(String username) {
		return this.catwallet.getWalletUser(username);
	}
	
	/**
	 * Classificacao do vinho
	 * 
	 * @param winename	nome do vinho
	 * @param rating	classificacao atribuida
	 * @return true caso o vinho exista, false caso contrario
	 */
	public boolean classify(String winename,int rating) {
		boolean b =  this.catwine.rateWine(winename, rating);
		this.updateWineFile(this.wines);
		return b;
	}
	
	
	/**
	 * Update ao ficheiro com os vinhos
	 * 
	 * @param winepath	path do ficheiro dos wines
	 * @return true caso a atualizacao tenha sido bem sucedida, false caso contrario
	 */
	private boolean updateWineFile(String winepath) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(winepath, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.getCatWine());
			out.close();
			fileOut.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Update as wallets
	 * 
	 * @param walletpath	path do ficheiro wallet
	 * @return true caso a atualizacao tenha sido bem sucedida, false caso contrario
	 */
	private boolean updateWalletFile(String walletpath) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(walletpath, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.getCatWallet());
			out.close();
			fileOut.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	/**
	 * Catalogo de Wallets
	 * 
	 * @return lista de wallets
	 */
	private List<Wallet> getCatWallet(){
		return this.catwallet.getList();
	}

}
