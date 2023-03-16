package tintolmarket.domain.catalogs;

import java.util.ArrayList;
import java.util.List;

import tintolmarket.domain.Wallet;


public class CatalogoWallet {
	private List<Wallet> catWallet;
	
	
	private CatalogoWallet() {
		this.catWallet = new ArrayList<>();
	 }
	
	private CatalogoWallet(ArrayList<Wallet> aw) {
		this.catWallet = aw;
	}
	    
	private static CatalogoWallet INSTANCE;
	    
	public static CatalogoWallet getInstance(boolean n, Object o) {
		if(n) {
			CatalogoWallet.INSTANCE = new CatalogoWallet((ArrayList<Wallet>) o);
			return INSTANCE;
		} 
	     INSTANCE = new CatalogoWallet();
	     return INSTANCE;
	}
	
	public Wallet getWalletUser(String username) {
		for (Wallet w:catWallet) {
			if(w.equals(username)) {
				return w;
			}
		}
		return null;
	}
	public int getWalletValue(String username) {
		for (Wallet w:catWallet) {
			if(w.equals(username)) {
				return w.getWallet();
			}
		}
		return -1;
	}
	public void changeWallet(Wallet walletUser) {
		this.catWallet.remove(0);
		for(int i = 0; i < this.catWallet.size();i++) {
			if(this.catWallet.get(i).equals(walletUser)) {
				this.catWallet.remove(i);
				this.catWallet.add(walletUser);
			}
		}
	}
	public boolean addWallet(String username) {
		return this.catWallet.add(new Wallet(username));
	}


}
