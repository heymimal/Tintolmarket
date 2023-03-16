package tintolmarket.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Wine implements Serializable {
	private static final long serialVersionUID = 1L;
	private String wineName;
	private double rating;
	private List<Cliente> infoClientes;
	private int totalAmount;
	
	public Wine(String wineName) {
		this.setWineName(wineName);
		this.infoClientes = new ArrayList<>();
		this.rating = -1;
		this.setTotalAmount(0);
	}
	private void setTotalAmount(int i) {
		this.totalAmount = i;
		
	}
	public void setWineName(String wineName) {
		this.wineName = wineName;
	}
}
