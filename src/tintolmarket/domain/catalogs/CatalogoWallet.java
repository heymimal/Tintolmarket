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


}
