package tintolmarket.handlers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import tintolmarket.domain.catalogs.*;
import tintolmarket.server.Server;
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
		boolean b = this.catwine.addWine(winename);
		if(b) {
			System.out.println("Vinho ainda não existia");
			this.updateWineFile(Server.wines);
		}
		return b;
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
				if(this.updateWalletFile(Server.wallet)) {
					System.out.println("updated");
				}
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
	private boolean updateWineFile(String winepath) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(winepath, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.wtv());
			out.close();
			fileOut.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
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
	private List<Wallet> getCatWallet(){
		return this.catwallet.getList();
	}

}
