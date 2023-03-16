package tintolmarket.handlers;

import java.util.List;

import tintolmarket.domain.catalogs.*;
import tintolmarket.domain.Wine;
import tintolmarket.domain.Wallet;

public class WineHandler {
	private CatalogoWine catwine;
	private CatalogoWallet catwallet;
	
	public WineHandler() {
		this.catwine = CatalogoWine.getInstance(false, null);
		this.catwallet = CatalogoWallet.getInstance(false, null);
	}
	
	public WineHandler(boolean n1, Object o1, boolean n2, Object o2) {
		this.catwine = CatalogoWine.getInstance(n1, o1);
		this.catwallet = CatalogoWallet.getInstance(n2, o2);
	}
	
	public List<Wine> wtv(){
		return this.catwine.getCatWine();
	}
	
	public boolean addWine(String winename) {
		return this.catwine.addWine(winename);
	}
	public boolean addWalletUser(String username) {
		return this.catwallet.addWallet(username);
	}
	
	public int sellWine(String winename,String username,int quantity, int price) {
		return this.catwine.sellWine(winename, username, quantity, price);
	}
	
	public String viewWine(String winename) {
		//TO DO
		return this.catwine.viewWine(winename);
	}

}
