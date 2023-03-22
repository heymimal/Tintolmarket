package tintolmarket.domain.catalogs;

import java.util.ArrayList;
import java.util.List;

import tintolmarket.domain.Wine;


/**
 * Classe do Catalogo de Vinhos
 * 
 * @author fc54446, fc54409, fc54933
 *
 */
public class CatalogoWine {
	private List<Wine> catWine;

	private CatalogoWine() {
		this.catWine = new ArrayList<>();
	 }
	
	
	private CatalogoWine(ArrayList<Wine> wl) {
		this.catWine = wl;
	}
	    
	private static CatalogoWine INSTANCE;
	
	/**
	 * @return catalogo de vinhos
	 */
	public List<Wine> getCatWine(){
		return this.catWine;
		
	}
	
	public String toString() {
		return "teste";
	}
	    
	/**
	 * @param o ???
	 * @return
	 */
	public static CatalogoWine getInstance(Object o) {
		if(INSTANCE == null) {
			if(o!=null) {
				CatalogoWine.INSTANCE = new CatalogoWine((ArrayList<Wine>) o);
				return INSTANCE;
			} 
		     INSTANCE = new CatalogoWine();
		     return INSTANCE;
		}
		return INSTANCE;
		
	}
	
	/**
	 * Vender vinho
	 * 
	 * @param winename	vinho a vender
	 * @param clientName	nome do vendedor (?)
	 * @param quantity		quantidade de vinho a vender
	 * @param price		preço do vinho a vender
	 * @return	-1 se vinho nao existe, 0 caso precos sejam diferentes e 1 se a venda for efetuada
	 */
	public int sellWine(String winename,String clientName, int quantity, int price) {
		synchronized (catWine) {
			for (Wine w:this.catWine) {
				if(w.getWineName().equals(winename)) {
					System.out.println("wine found");
					return w.addClientSeller(clientName, quantity, price);
				}
			}
			for(Wine w:this.catWine) {
				System.out.println(w.toString());
			}
			return -1;
		}
		
	}
	
	/**
	 * Avaliar vinho
	 * 
	 * @param winename	nome do vinho a ser avaliado
	 * @param rating	classificacao associada ao vinho
	 * @return true caso o vinho exista, false caso contrario
	 */
	public boolean rateWine(String winename,int rating) {
		synchronized (catWine) {
			for (Wine w:this.catWine) {
				if(w.getWineName().equals(winename)) {
					return w.rateWine(rating);
				}
			}
			return false;	
		}
		
	}
	/**
	 * Compra de vinho
	 * 
	 * @param winename	nome do vinho a ser comprado
	 * @param clientname	nome do cliente que compra o vinho (?)
	 * @param quantity		quantidade de vinho a ser comprada
	 * @param wallet		wallet do cliente
	 * @return -1 caso o vinho nao exista, -4 caso o cliente nao exista, -3 caso a quantidade desejada seja
	 * maior que a disponivel, -2 caso nao tenha dinheiro suficiente, caso contrario o custo total da compra
	 */
	public int buyWine(String winename,String clientname,int quantity, int wallet) {
		synchronized (catWine) {
			for (Wine w:this.catWine) {
				if(w.getWineName().equals(winename)) {
					return w.buyWineSeller(clientname, quantity, wallet);
				}
			}
			return -1;
		}
		
	}
	
	/**
	 * Adicao de vinho para venda
	 * 
	 * @param winename	nome do vinho
	 * @param winePath	path do vinho (sua imagem)
	 * @return false se o vinho ja existe, true caso contrario
	 */
	public boolean addWine(String winename, String winePath) {
		synchronized (catWine) {
			for(Wine w:this.catWine) {
			if(w.getWineName().equals(winename)) {
				System.out.println("vinho jÃ¡ existente"); //
				return false;
			}
		}
		Wine w = new Wine(winename,winePath);
		this.catWine.add(w);
		
		return true;
		}
		
	}
	
	/**
	 * Vista de informacoes sobre o vinho
	 * 
	 * @param winename	nome do vinho
	 * @return de informacoes sobre o vinho, null caso nao exista
	 */
	public String[] viewWine(String winename) {
		String [] con = new String[2];
		synchronized (catWine) {
			for(Wine w:this.catWine) {
				if(w.equals(winename)) {
					con[0] = w.toString();
					con[1] = w.getWinePath();
					return con;
				}
			}
			return null;
		}
	}

}
