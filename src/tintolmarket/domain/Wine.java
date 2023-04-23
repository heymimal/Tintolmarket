package tintolmarket.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Wine
 * 
 * @author fc54446, fc54409, fc54933
 *
 */
public class Wine implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String wineName;
	private String winePath;
	private double rating;
	private int counter;
	private List<Cliente> infoClientes;
	private int totalAmount;
	
	/**
	 * Construtor do vinho
	 * 
	 * @param wineName	nome do vinho
	 * @param winePath	caminho da imagem do vinho
	 */
	public Wine(String wineName, String winePath) {
		this.setWineName(wineName);
		this.winePath = winePath;
		this.infoClientes = new ArrayList<>();
		this.rating = 0; // 0
		this.counter = 0;
		this.setTotalAmount(0);
	}
	
	
	/**
	 * @param i	  quantidade do vinho
	 */
	private void setTotalAmount(int i) {
		this.totalAmount = i;
		
	}
	
	/**
	 * @param wineName	nome do vinho
	 */
	public void setWineName(String wineName) {
		this.wineName = wineName;
	}
	
	
	/**
	 * Atualiza a classificacao do vinho
	 * 
	 * @param rating	classificacao que vai ser atribuida
	 * @return true apos se fazer o rating ao vinho
	 */
	public boolean rateWine(int rating) {
		this.changeRating(rating);
		return true;
		
	}
	

	/**
	 * Altera a classificacao do vinho, incrementa o counter;
	 * 
	 * @param newRating	rating a adicionar ao vinho
	 */
	private void changeRating(int newRating) {
		if(this.rating != 0) {
			this.rating = ((this.rating*counter)+newRating)/(counter+1);
			this.counter++;
		}
		else {
			this.rating = newRating;
			this.counter++;
		}
		
		
	}

	/**
	 * @return quantidade total de unidades de vinho
	 */
	public int getTotalAmount() {
		return totalAmount;
	}
	
	
	/**
	 * Adiciona informaçao sobre um cliente vendedor de this.winename a this.infoClientes.
	 * 
	 * @param seller	nome do vendedor
	 * @param quantity		quantidade de vinho que esta a vender
	 * @param price			preco do vinho
	 * @return 1 se o cliente ja existe e o price eh igual ao anterior ou se nao existe e vai ser adicionado, 0 se existir 
	 * mas o price ja estabelecido for diferente do novo (nao e adicionado).
	 */
	public int addClientSeller(String seller, int quantity, int price) {
		Cliente c = new Cliente(seller, quantity, price);
		int index = this.infoClientes.lastIndexOf(c);
		if(index != -1) {
			Cliente n = this.infoClientes.get(index);
			if(n.getPrice() == price) {
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
			this.setTotalAmount(this.getTotalAmount() + quantity);
			return 1;
		}
	}
	
	/**
	 * Compra quantity unidades do vinho ao user clientName.
	 * 
	 * @param clientName	nome do cliente (que esta a vender)
	 * @param quantity		quantidade que se pretende comprar
	 * @param currentWallet		wallet do clientName
	 * 
	 * @return -4 se o cliente nao existe, -3 se pretende comprar uma quantidade maior que a existente,
	 * -2 se nao tiver dinheiro suficiente, e o custo total da compra caso tudo seja bem sucedido
	 */
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
	
	/**
	 * @return nome do vinho
	 */
	public String getWineName() {
		return this.wineName;
	}
	
	/**
	 * @return string com informacoes acerca do vinho, como o seu nome, classificao e unidades disponiveis, bem como dos clientes que
	 * pretendem vender esse vinho
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome: "+this.wineName);
		if(this.rating > 0) {
			sb.append("\nClassificação: "+this.rating);
		}
		
		if(this.totalAmount > 0) {
			sb.append("\nUnidades Disponiveis (Total): "+ this.totalAmount + "\n");
			for (Cliente c:this.infoClientes) {
				sb.append(c.toString());
			}
		}
		return sb.toString();
		
	}
	
	/**
	 * @return caminho da imagem do vinho
	 */
	public String getWinePath() {
		return this.winePath;
	}

	public List<Cliente> getClientes() {
		return this.infoClientes;
	}
}
