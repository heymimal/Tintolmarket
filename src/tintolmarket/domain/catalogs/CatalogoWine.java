package tintolmarket.domain.catalogs;

import java.util.ArrayList;
import java.util.List;

import tintolmarket.domain.Wine;


public class CatalogoWine {
	private List<Wine> catWine;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

}
