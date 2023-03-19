package tintolmarket.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Wine implements Serializable {
	private static final long serialVersionUID = 1L;
	private String wineName;
	private double rating;
	private int counter;
	private List<Cliente> infoClientes;
	private int totalAmount;
	
	public Wine(String wineName) {
		this.setWineName(wineName);
		this.infoClientes = new ArrayList<>();
		this.rating = -1; // 0
		this.counter = 0;
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
			this.rating = ((this.rating*counter)+rating2)/counter+1;
			this.counter++;
		}
		else this.rating = rating2;
		this.counter++;
		
	}

	public int getTotalAmount() {
		return totalAmount;
	}
	public int addClientSeller(String clientName, int quantity, int price) {
		Cliente c = new Cliente(clientName, quantity, price);
		int index = this.infoClientes.lastIndexOf(c);
		if(index != -1) {
			Cliente n = this.infoClientes.get(index);
			if(n.getPrice() == price) {
				System.out.println("Price is equal, adding up the quantity");
				n.setQuantity(quantity);
				this.setTotalAmount(this.getTotalAmount() + quantity);
				this.infoClientes.remove(index);
				this.infoClientes.add(index, n);
				return 1;
				
			} else {
				return 0;
			}
		}else {
			this.infoClientes.add(c);
			System.out.println("Adicionar");
			//System.out.println(infoClientes.toString());
			this.setTotalAmount(this.getTotalAmount() + quantity);
			return 1;
		}
	}
	
	public int buyWineSeller(String clientName, int quantity, int currentWallet) {
		Cliente c = new Cliente(clientName);
		int index = this.infoClientes.lastIndexOf(c);
		if(index == -1) {
			return -4;
		} else {
			Cliente d = this.infoClientes.get(index);
			if(d.getQuantity() < quantity) {
				return -3;
			} else {
				int totalCost = quantity * d.getPrice();
				if(currentWallet >= totalCost) {
					this.totalAmount -=quantity;
					d.setQuantity(-quantity);
					this.infoClientes.remove(index);
					this.infoClientes.add(index, d);
					return totalCost;
				} else {
					return -2;
				}
			}
		}
	}
	public boolean equals(Object o) {
		if (o == null)
		    return false;
		
		if(o.getClass() == String.class) {
			return this.getWineName().equals(o);
		}

		if (this.getClass() != o.getClass())
		    return false;
		
		Wine w = (Wine) o;
		return this.getWineName().equals(w.getWineName());
	}
	
	public String getWineName() {
		return this.wineName;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Cliente c:this.infoClientes) {
			sb.append(c.toString());
		}
		return wineName + " " + rating + " " + sb.toString();
		
	}
}
