package aplicacao;

import java.io.IOException;
import java.util.Scanner;

import entidades.Endereco;
import entidades.EnderecoLista;

public class Program {

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		System.setProperty("file.encoding", "UTF-8");
		
		System.out.print("Deseja procurar por CEP ou endereço? (C/E): ");
		char escolha = sc.next().charAt(0);
		
		Endereco e = new Endereco();

		if (escolha == 'C') {
			System.out.print("Digite um cep: ");
			int cep = sc.nextInt();
			
			e.setCep(String.format("%08d", cep));
		}
		else if (escolha == 'E') {
			sc.nextLine();
			System.out.print("Digite o endereço ou parte: ");
			String endereco = sc.nextLine();
			
			System.out.print("Digite a cidade: ");
			String cidade = sc.nextLine();
			
			System.out.print("Digite a UF: ");
			String uf = sc.nextLine();
			
			e.setLogradouro(endereco);
			e.setLocalidade(cidade);
			e.setUf(uf);
		}
		else {
			System.out.println("Escolha inválida: " + escolha);
			
			sc.close();
			return;
		}
		
		EnderecoLista el = new EnderecoLista(e);
		el.buscaJSon();
        
		for (Endereco end : el.getEnderecos()) {
			System.out.println(end.toString());
		}
		
		if (el.localizaCep("14085-100") != null) {
			System.out.println("CEP ENCONTRADO!");
			System.out.println(el.localizaCep("14085-100").toString());
		}
		
        sc.close();
        
	}

}
