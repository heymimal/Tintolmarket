package tintolmarket.domain;

import java.io.Serializable;

public class Wine implements Serializable {
	private String name;
	//private Client[] clients;
	// quantity value
	//private double rating; 
	//maybe serialize to file data, idk
	public Wine(String winename) {
		this.name = winename;
	}
}
