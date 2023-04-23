package tintolmarket.domain.catalogs;

import java.util.ArrayList;
import java.util.List;

import tintolmarket.domain.Cliente;
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
	 
	//SINGLETON
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
	 * @param seller	nome do vendedor 
	 * @param quantity		quantidade de vinho a vender
	 * @param price		pre�o do vinho a vender
	 * @return	-1 se vinho nao existe, 0 caso precos sejam diferentes e 1 se a venda for efetuada
	 */
	public int sellWine(String winename,String seller, int quantity, int price) {
		synchronized (catWine) {
			for (Wine w:this.catWine) {
				if(w.getWineName().equals(winename)) {
					return w.addClientSeller(seller, quantity, price);
				}
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
	 * @param seller	nome do vendedor
	 * @param quantity		quantidade de vinho a ser comprada
	 * @param wallet		wallet do cliente
	 * @return -1 caso o vinho nao exista, -4 caso o cliente nao exista, -3 caso a quantidade desejada seja
	 * maior que a disponivel, -2 caso nao tenha dinheiro suficiente, caso contrario o custo total da compra
	 */
	public int buyWine(String winename,String seller,int quantity, int wallet) {
		synchronized (catWine) {
			for (Wine w:this.catWine) {
				if(w.getWineName().equals(winename)) {
					return w.buyWineSeller(seller, quantity, wallet);
				}
			}
			return -1;
		}
		
	}
	
	/**
	 * Adicao de vinho para venda
	 * 
	 * @param winename	nome do vinho
	 * @param winePath	path para a imagem associada
	 * @return false se o vinho ja existe, true caso contrario
	 */
	public boolean addWine(String winename, String winePath) {
		synchronized (catWine) {
			for(Wine w:this.catWine) {
				if(w.getWineName().equals(winename)) {
					return false;
				}
		}
		Wine w = new Wine(winename,winePath);
		this.catWine.add(w);
		
		return true;
		}
		
	}
	
	/**
	 * informacoes sobre o vinho winename
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

    public int getPriceWine(String winename, String wineseller) {
		synchronized (catWine){
			for(Wine w:this.catWine){
				if(w.getWineName().equals(winename)){
					for(Cliente c:w.getClientes()){
						if(c.getName().equals(wineseller)){
							return c.getPrice();
						}
					}
					break;
				}
			}
		}
		return 0;
	}
}
