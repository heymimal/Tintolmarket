package tintolmarket.domain.catalogs;

import java.util.ArrayList;
import java.util.List;

import tintolmarket.domain.Wine;


public class CatalogoWine {
	private List<Wine> catWine;

	private CatalogoWine() {
		this.catWine = new ArrayList<>();
	 }
	private CatalogoWine(ArrayList<Wine> wl) {
		this.catWine = wl;
	}
	    
	private static CatalogoWine INSTANCE;
	
	public List<Wine> getCatWine(){
		return this.catWine;
		
	}
	    
	public static CatalogoWine getInstance(boolean n, Object o) {
		if(INSTANCE == null) {
			if(n) {
				CatalogoWine.INSTANCE = new CatalogoWine((ArrayList<Wine>) o);
				return INSTANCE;
			} 
		     INSTANCE = new CatalogoWine();
		     return INSTANCE;
		}
		return INSTANCE;
		
	}
	
	public int sellWine(String winename,String clientName, int quantity, int price) {
		for (Wine w:this.catWine) {
			if(w.getWineName().equals(winename)) {
				return w.addClientSeller(clientName, quantity, price);
			}
		}
		for(Wine w:this.catWine) {
			System.out.println(w.toString());
		}
		return -1;
	}
	
	public boolean rateWine(String winename,int rating) {
		for (Wine w:this.catWine) {
			if(w.getWineName().equals(winename)) {
				return w.rateWine(rating);
			}
		}
		return false;
	}
	public int buyWine(String winename,String clientname,int quantity, int wallet) {
		for (Wine w:this.catWine) {
			if(w.getWineName().equals(winename)) {
				return w.buyWineSeller(clientname, quantity, wallet);
			}
		}
		return -1;
	}
	
	public boolean addWine(String winename) {
		for(Wine w:this.catWine) {
			if(w.getWineName().equals(winename)) {
				System.out.println("vinho já existente"); //
				return false;
			}
		}
		Wine w = new Wine(winename);
		this.catWine.add(w);
		return true;
	}
	
	public String viewWine(String winename) {
		for(Wine w:this.catWine) {
			if(w.equals(winename)) {
				return w.toString();
			}
		}
		return "";
	}

}
