package tintolmarket.domain;

import java.io.Serializable;


/**
 * Classe do Cliente
 * 
 *  @author fc54446, fc54409, fc54933
 *
 */
public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private int quantity;
	private int price;
	
	/**
	 * Construtor do Cliente
	 * 
	 * @param name		username do cliente
	 * @param quantity	quantidade de vinho que esta a vender
	 * @param price		o preco a que esta a vender o vinho
	 */
	public Cliente(String name, int quantity,int price) {
		this.username = name;
		this.quantity = quantity;
		this.price = price;
	}
	
	/**
	 * Construtor do Cliente - VERIFICAR SE ESTA A SER USADO EM ALGUM LADO
	 * 
	 * @param clientName	username do cliente
	 */
	public Cliente(String clientName) {
		this.username = clientName;
	}
	
	
	/**
	 *@return string com a informacao acerca do cliente
	 */
	public String toString() {
		return "Cliente: "+ this.username + " preco: " + this.price + " Unidades: " + this.quantity + "\n";
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
	
	
	/**
	 * @return username
	 */
	public String getName() {
		return this.username;
	}
	
	/**
	 * @return quantidade de vinho associado ao utilizador
	 */
	public int getQuantity() {
		return this.quantity;
	}
	
	/**
	 * @return preco do vinho associado ao utilizador
	 */
	public int getPrice() {
		return this.price;
	}
	
	/**
	 * @param quantity2		quantidade a ser associada
	 */
	public void setQuantity(int quantity2) {
		this.quantity +=quantity2;
		
	}
}
