package tintolmarket.domain;

import java.io.Serializable;

/**
 * Classe da Wallet
 * 
 *  @author fc54446, fc54409, fc54933
 *
 */
public class Wallet implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_VALUE = 200;
	private String username;
	private int walletValue;
	
	/**
	 * Construtor da wallet, define o valor inicial como DEFAULT_VALUE
	 * @param username	username associado
	 * 
	 */
	public Wallet(String username) {
		this.setUsername(username);
		this.setWallet(DEFAULT_VALUE);
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username	nome do user
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return valor da wallet
	 */
	public int getWallet() {
		return walletValue;
	}

	/**
	 * @param wallet	wallet do user
	 */
	public void setWallet(int wallet) {
		this.walletValue = wallet;
	}
	
	/**
	 * Altera o valor da wallet to user
	 * @param value		novo valor da wallet
	 */
	public void changeWallet(int value) {
		this.walletValue = value;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
		    return false;
		
		if(o.getClass() == String.class) {
			return this.getUsername().equals(o);
		}

		if (this.getClass() != o.getClass())
		    return false;
		
		Wallet w = (Wallet) o;
		return this.getUsername().equals(w.getUsername());
		
	}

}
