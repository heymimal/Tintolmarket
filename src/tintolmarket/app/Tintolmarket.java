package tintolmarket.app;

import java.util.Scanner;

import tintolmarket.app.client.Client_stub;

/**
 * Classe Tintolmarket
 * 
 * @author fc54446, fc54409, fc54933
 */
public class Tintolmarket{
	public static Client_stub c; //Conechao
	
	public static void main(String[] args) {
	
		String address;
		String username = null;
		String password;
		Scanner sc = new Scanner(System.in);
		
	if(args.length == 2) {
		address = args[0];
		username = args[1];
		System.out.print("Por favor insira a sua password:");
		password = sc.next();
		c = new Client_stub(address,username, password);
		
	} else if(args.length == 3) {
		address = args[0];
		username = args[1];
		password = args[2];
		c = new Client_stub(address,username, password);
	}
	
	//Se nao recebemos pass, pedir
	
	if(!c.connect()) {
		System.out.println("Erro ao conectar-se ao servidor. Tente outra vez.");
		System.out.println("A desligar...");
	} else {
	System.out.println("Conexao estabelecida!");
	printMenu();
	boolean b = true;
	while(b) {
		System.out.print("Insere: ");
		String command = sc.next();
			switch(command) {
			case "a":
			case "add":{
				String winename = sc.next();
				String wineimage = sc.next();
				System.out.println("A adicionar "+ winename + " com a imagem "+ wineimage);
				if(!c.addwine(winename,wineimage)) {
					System.out.println("Erro a adicionar a imagem/ Vinho já existe.");
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
				System.out.println("Vender "+ winequantity + " unidades a "+ winevalue + " do vinho "+ winename);
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
				//check if wineseller == username
				int winequantity = sc.nextInt();
				if(wineseller.equals(username)) {
					System.out.println("Nao podes comprar a ti mesmo.");
				} else {
					int resposta = c.buy(winename,wineseller,winequantity);
					if(resposta == -4) {
						System.out.println("Vendedor nao existe.");
					} else if(resposta == -3) {
						System.out.println("Quantidade nao disponivel.");
					} else if(resposta == -2) {
						System.out.println("Utilizador nao possui dinheiro suficiente.");
					} else if(resposta == -1) {
						System.out.println("Vinho nao existe!");
					} else if(resposta == 1) {
						System.out.println("Compra efetuada!");
					}
				}
				break;
			}
			case "w":
			case "wallet":{
				System.out.println("Carteira: "+ c.wallet()); 
				break;
			}
			case "c":
			case "classify":{
				String winename = sc.next();
				int stars = sc.nextInt();
				if (stars > 5 || stars < 1) {
					System.out.println("Estrelas tem que estar entre 1 e 5!");
				}
				else {
					if(c.classify(winename, stars)) {
						System.out.println("Classificacao guardada.");
					} else {
						System.out.println("Vinho nao existe.");
					}
				}
				break;
			}
			case "t":
			case "talk":{
				String usersend = sc.next();
				String message = sc.nextLine();
				if(c.talk(usersend,message)) {
					System.out.println("Mensagem enviada");
				} else {
					System.out.println("Utilizador nao existe");
				}
				break;
			}
			case "r":
			case "read":{
				String mensagens = c.read();
				System.out.println(mensagens);
				break;
			}
			default:
				break;
			}
	}
	}
}

	private static void printMenu() {
		/*
		 * 
		 */
		System.out.println("Menu:");
		System.out.println("Todas as operacoes que se sequem podem ser efetuadas escrevendo o nome por completo, ou a primeira letra.");
		System.out.println();
		System.out.println("Operaçoes:");
		System.out.println("- add <wine> <image> : Adiciona um vinho com nome wine e com a imagem image associada");
		System.out.println("- sell <wine> <value> <quantity> : coloca à venda o número indicado por quantity de\n"
				+ "unidades do vinho wine pelo valor value.");
		System.out.println("- view <wine> - obtém as informações associadas ao vinho identificado por wine,\n"
				+ "nomeadamente a imagem associada, a classificação média e, caso existam unidades do\n"
				+ "vinho disponíveis para venda, a indicação do utilizador que as disponibiliza, o preço e a\n"
				+ "quantidade disponível.");
		System.out.println("- buy <wine> <seller> <quantity> - compra quantity unidades do vinho wine ao utilizador\n"
				+ "seller. O número de unidades deve ser removido da quantidade disponível e deve ser\n"
				+ "transferido o valor correspondente à compra da conta do comprador para o vendedor.");
		System.out.println("- wallet - obtém o saldo atual da carteira.");
		System.out.println("- classify <wine> <stars> - atribui ao vinho wine uma classificação de 1 a 5, indicada por stars.");
		System.out.println("- talk <user> <message> - permite enviar uma mensagem privada ao utilizador user.");
		System.out.println("- read - permite ler as novas mensagens recebidas.");
	}

}
