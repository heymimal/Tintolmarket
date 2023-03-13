package tintolmarket;

import java.util.Scanner;

import tintolmarket.client.Client_stub;

public class Tintolmarket{
	public static Client_stub c; //Conechao
	
	public static void main(String[] args) {
		
	String username = "user";
	String pass = "pass";
	
	//Se nao recebemos pass, pedir
	c = new Client_stub(username, pass);
	if(!c.connect()) {
		System.out.println("erro");
	} else {
	System.out.println("passou sem errar");
	// depois de fazer conect, dar display das operações possiveis e começar a fazer
	Scanner sc = new Scanner(System.in);
	System.out.println("Hello!");
	System.out.print("insere: ");
	boolean b = true;
	while(b) {
		String command = sc.next();
		System.out.println(command);
		switch(command) {
		case "a":
		case "add":{
			String winename = sc.next();
			String wineimage = sc.next();
			System.out.println("adding "+ winename + "with the image "+ wineimage);
			c.addwine(winename,wineimage);
			break;
		}
		case "s":
		case "sell":{
			String winename = sc.next();
			int winevalue = sc.nextInt();
			int winequantity = sc.nextInt();
			System.out.println("selling wop");
			System.out.println(winevalue);
			
			c.sellwine(winename,winevalue,winequantity);
			break;
		}
		case "v":
		case "view":{
			String winename = sc.next();
			c.view(winename);
			break;
		}
		case "b":
		case "buy":{
			String winename = sc.next();
			String wineseller = sc.next();
			int winequantity = sc.nextInt();
			c.buy(winename,wineseller,winequantity);
			break;
		}
		case "w":
		case "wallet":{
			System.out.println("Estás pobre");
			c.wallet();
			break;
		}
		case "c":
		case "classify":{
			String winename = sc.next();
			int stars = sc.nextInt();
			if (stars > 5 || stars < 1) {
				System.out.println("Estrelas entre 1 e 5");
			}
			c.classify(winename,stars);
			break;
		}
		case "t":
		case "talk":{
			String usersend = sc.next();
			String message = sc.next();
			c.talk(usersend,message);
			break;
		}
		case "r":
		case "read":{
			System.out.println("Mensagem ");
			c.read();
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
