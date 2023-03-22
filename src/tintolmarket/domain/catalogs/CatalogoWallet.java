package tintolmarket.domain.catalogs;

import java.util.ArrayList;
import java.util.List;

import tintolmarket.domain.Wallet;


/**
 * Catalogo de Wallets
 * 
 * @author fc54446, fc54409, fc54933
 *
 */
public class CatalogoWallet {
	private List<Wallet> catWallet;
	
	private CatalogoWallet() {
		this.catWallet = new ArrayList<>();
	 }
	
	private CatalogoWallet(ArrayList<Wallet> aw) {
		this.catWallet = aw;
	}
	    
	private static CatalogoWallet INSTANCE;
	    
	/**
	 * @param o ????????
	 * @return
	 */
	public static CatalogoWallet getInstance( Object o) {
		if(o != null) {
			CatalogoWallet.INSTANCE = new CatalogoWallet((ArrayList<Wallet>) o);
			return INSTANCE;
		} 
	     INSTANCE = new CatalogoWallet();
	     return INSTANCE;
	}
	
	/**
	 * Wallet do utilizador username
	 * 
	 * @param username	utilizador
	 * @return wallet do utilizador, null caso essa wallet nao exista
	 */
	public Wallet getWalletUser(String username) {
		synchronized (catWallet) {
			for (Wallet w:catWallet) {
			if(w.equals(username)) {
				return w;
			}
		}
		return null;
		}
		
	}
	
	
	/**
	 * Valor da Wallet do utilizador username
	 * 
	 * @param username	utilizador
	 * @return	-1 se o username nao tem wallet (?) associada, caso contrario o valor da sua wallet
	 */
	public int getWalletValue(String username) {
		synchronized (catWallet) {
		for (Wallet w:catWallet) {
			if(w.equals(username)) {
				return w.getWallet();
			}
		}
		return -1;	
		}
		
	}
	
	
	/**
	 * Atualizacao do valor da wallet
	 * 
	 * @param walletUser	utilizador associado a wallet
	 */
	public void changeWallet(Wallet walletUser) {
		synchronized (catWallet) {
			for(int i = 0; i < this.catWallet.size();i++) {
				if(this.catWallet.get(i).equals(walletUser)) {
					this.catWallet.remove(i);
					this.catWallet.add(walletUser);
					break;
				}
			}
		}
		
	}
	
	
	/**
	 * Adicionar wallet a user (?)
	 * 
	 * @param username	utilizador ao qual se vai associar a wallet
	 * @return true se for efetuado com sucesso, false caso contrario
	 */
	public boolean addWallet(String username) {
		synchronized (catWallet) {
			return this.catWallet.add(new Wallet(username));
		}
		
	}

	/**
	 * 
	 * @return lista de wallets/catalogo de wallets
	 */
	public List<Wallet> getList() {
		synchronized (catWallet) {
			return this.catWallet;
		}
		
	}


}
