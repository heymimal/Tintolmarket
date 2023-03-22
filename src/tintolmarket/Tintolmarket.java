package tintolmarket;

import java.util.Scanner;

import tintolmarket.client.Client_stub;

/**
 * Classe Tintolmarket
 * 
 * @author fc54446, fc54409, fc54933
 */
public class Tintolmarket{
	public static Client_stub c; //Conechao
	
	public static void main(String[] args) {
		
	String username = "user3";
	String pass = "pass2";
	
	//Se nao recebemos pass, pedir
	c = new Client_stub(username, pass);
	if(!c.connect()) {
		System.out.println("erro");
	} else {
	System.out.println("passou sem errar");
	// depois de fazer conect, dar display das operacoes possiveis e comecar a fazer
	Scanner sc = new Scanner(System.in);
	System.out.println("Hello!");
	System.out.print("insere: ");
	boolean b = true;
	while(b) {
		System.out.print("Insere: ");
		String command = sc.next();
		System.out.println(command);
		switch(command) {
		case "a":
		case "add":{
			String winename = sc.next();
			String wineimage = sc.next();
			System.out.println("adding "+ winename + " with the image "+ wineimage);
			if(!c.addwine(winename,wineimage)) {
				System.out.println("Error");
			} else {
				System.out.println("Adicionado corretamente");
			}
			break;
		}
		case "s":
		case "sell":{
			String winename = sc.next();
			int winevalue = sc.nextInt();
			int winequantity = sc.nextInt();
			System.out.println("selling wop");
			System.out.println(winevalue);
			
			int resposta = c.sellwine(winename,winevalue,winequantity);
			if(resposta == -1) {
				System.out.println("Vinho não existe");
			} else if (resposta == 0){
				System.out.println("Vinho já tinha sido atribuido com um valor diferente pelo utilizador");
			} else if(resposta == 1) {
				System.out.println("Quantidades pretendidas adicionadas");
			}
			break;
		}
		case "v":
		case "view":{ //
			String winename = sc.next();
			System.out.println(c.view(winename));
			break;
		}
		case "b":
		case "buy":{
			String winename = sc.next();
			String wineseller = sc.next();
			int winequantity = sc.nextInt();
			int resposta = c.buy(winename,wineseller,winequantity);
			if(resposta == -4) {
				System.out.println("Vendedor não existe");
			} else if(resposta == -3) {
				System.out.println("Quantidade não disponivel");
			} else if(resposta == -2) {
				System.out.println("Utilizador não possui dinheiro suficiente");
			} else if(resposta == -1) {
				System.out.println("Vinho não existe!");
			} else if(resposta == 1) {
				System.out.println("Compra efetuada!");
			}
			break;
		}
		case "w":
		case "wallet":{
			System.out.println("Estás pobre");
			System.out.println(c.wallet()); 
			break;
		}
		case "c":
		case "classify":{
			String winename = sc.next();
			int stars = sc.nextInt();
			if (stars > 5 || stars < 1) {
				System.out.println("Estrelas têm que estar entre 1 e 5");
			}
			else {
				if(c.classify(winename, stars)) {
					System.out.println("Classificação guardada");
				} else {
					System.out.println("vinho não existe");
				}
			}
			break;
		}
		case "t":
		case "talk":{
			String usersend = sc.next();
			String message = sc.next();
			if(c.talk(usersend,message)) {
				System.out.println("Mensagem enviada");
			} else {
				System.out.println("Utilizador não existe");
			}
			break;
		}
		case "r":
		case "read":{
			System.out.println("Mensagem ");
			String mensagens = c.read();
			if(mensagens.equals(null)) {
				System.out.println("Não tens mensagens para ler");
			} else {
				System.out.println(mensagens);
			}
			break;
		}
		case "e":
			b = false;
			break;
		default:
			break;
	}
	}
	}
}

}
