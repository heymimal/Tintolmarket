package tintolmarket.domain;

import java.io.Serializable;


public class Cliente implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private int quantity;
	private int price;
	
	public Cliente(String name, int quantity,int price) {
		this.username = name;
		this.quantity = quantity;
		this.price = price;
	}
	public Cliente(String clientName) {
		this.username = clientName;
	}
	public String toString() {
		return this.username + " " + this.price + " " + this.quantity + "\n";
	}
	@Override
	public boolean equals(Object o) {
		if (o == null)
		    return false;
		
		if(o.getClass() == String.class) {
			return this.username.equals(o);
		}

		if (this.getClass() != o.getClass())
		    return false;

		Cliente c = (Cliente) o;
		return this.username.equals(c.username);
	}
	public String getName() {
		return this.username;
	}
	public int getQuantity() {
		return this.quantity;
	}
	public int getPrice() {
		return this.price;
	}
	public void setQuantity(int quantity2) {
		this.quantity +=quantity2;
		
	}
}
