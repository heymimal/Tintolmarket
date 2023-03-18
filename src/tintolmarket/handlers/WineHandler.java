package tintolmarket.handlers;

import java.util.List;

import tintolmarket.domain.catalogs.*;
import tintolmarket.domain.Wine;
import tintolmarket.domain.Wallet;

public class WineHandler {
	private CatalogoWine catwine;
	private CatalogoWallet catwallet;
	
	public WineHandler() {
		this.catwine = CatalogoWine.getInstance(null);
		this.catwallet = CatalogoWallet.getInstance(null);
	}
	
	public WineHandler(Object o1,Object o2) {
		this.catwine = CatalogoWine.getInstance(o1);
		this.catwallet = CatalogoWallet.getInstance(o2);
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
	public int buyWine(String winename, String seller, String user, int quantity) {
		Wallet walletUser = getWalletUser(user);
		if(walletUser.getClass() == Wallet.class) {
			int wallet = walletUser.getWallet();
			int change = this.catwine.buyWine(winename, seller, quantity, wallet);
			if(change <= 0) {
				return change;
			}
			System.out.println(change);
			Wallet walletSeller = getWalletUser(seller);
			if(walletSeller.getClass() == Wallet.class) {
				int new_walletUser = wallet - change;
				int new_walletSeller = walletSeller.getWallet() + change;
				walletSeller.changeWallet(new_walletSeller);
				walletUser.changeWallet(new_walletUser);
				this.catwallet.changeWallet(walletUser);
				this.catwallet.changeWallet(walletSeller);
				return 1;
			}
			
		}
		return 0; // user non_existent -> impossible
		
		
	}
	
	public int getWallerUser(String username) {
		return this.catwallet.getWalletValue(username);
	}
	
	private Wallet getWalletUser(String username) {
		return this.catwallet.getWalletUser(username);
	}
	
	public boolean classify(String winename,int rating) {
		return this.catwine.rateWine(winename, rating);
	}

}
