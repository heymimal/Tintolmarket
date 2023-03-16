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
	public boolean rateWine(int rating) {
		this.changeRating(rating);
		return true;
		
	}
	

	private void changeRating(int rating2) {
		if(this.rating != -1) {
			this.rating = (this.rating + rating2)/2;
		}
		else this.rating = rating2;
		
	}

	public int getTotalAmount() {
		return totalAmount;
	}
}
