package aplicacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import entidades.Endereco;

public class Program {

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		System.setProperty("file.encoding", "UTF-8");
		
		System.out.print("Digite um cep: ");
		int cep = sc.nextInt();
		

		String url = "https://viacep.com.br/ws/" + Integer.toString(cep) + "/json/";

		URL url11 = new URL(url);
		URLConnection uconn = url11.openConnection();

		uconn.setRequestProperty("Content-Type", "text/json");
		uconn.setDoInput(true);
		uconn.setDoOutput(true);
		HttpURLConnection conn = (HttpURLConnection) uconn;
		conn.connect();
		Object content = conn.getContent();

		InputStream st = (InputStream) content;
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		ObjectMapper mapper = new ObjectMapper();
        Endereco e = mapper.readValue(new StringReader(response.toString()), Endereco.class);
        
        System.out.println(e.toString());
        
        
        sc.close();
        
	}

}
