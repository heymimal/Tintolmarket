package tintolmarket.domain;

import java.io.Serializable;

public class Wallet implements Serializable {
	private static final int DEFAULT_VALUE = 200;
	private String username;
	private int walletValue;
	
	public Wallet(String username) {
		this.setUsername(username);
		this.setWallet(DEFAULT_VALUE);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getWallet() {
		return walletValue;
	}

	public void setWallet(int wallet) {
		this.walletValue = wallet;
	}
	
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
