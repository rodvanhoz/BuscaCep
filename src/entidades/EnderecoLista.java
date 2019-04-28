package entidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnderecoLista {

	private List<Endereco> enderecos = new ArrayList<>();

	private String cep;
	private String logradouro;
	private String cidade;
	private String uf;

	private char escolha;

	private String url;
	private StringBuilder response;

	public EnderecoLista(Endereco endereco) {

		if (endereco.getCep() != null) {
			this.cep = endereco.getCep();
			url = "https://viacep.com.br/ws/" + cep + "/json/";

			escolha = 'C';
		} else if ((endereco.getLogradouro() != null) && (endereco.getLocalidade() != null)
				&& (endereco.getUf() != null)) {
			this.logradouro = endereco.getLogradouro();
			this.cidade = endereco.getLocalidade();
			this.uf = endereco.getUf();

			url = "https://viacep.com.br/ws/" + uf + "/" + cidade + "/" + logradouro + "/json/";

			escolha = 'E';
		} else {
			System.out.println("Critério de busca não encontrado.");
			return;
		}

	}

	public void buscaJSon() throws IOException {
		URL url11 = new URL(url);
		URLConnection uconn = url11.openConnection();

		uconn.setRequestProperty("Content-Type", "text/json");
		uconn.setDoInput(true);
		uconn.setDoOutput(true);
		HttpURLConnection conn = (HttpURLConnection) uconn;
		conn.connect();
//		Object content = conn.getContent();

	//	InputStream st = (InputStream) content;
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		String inputLine;
		response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// chama o metodo para inserir os enderecos na lista de acordo com o tipo da pesquissa
		if (escolha == 'E') {
			mapperViaCepEnderecos();
		} else if (escolha == 'C') {
			mapperViaCep();
		}
	}

	public void addEndereco(Endereco endereco) {
		enderecos.add(endereco);
	}

	public void removeEndereco(Endereco endereco) {
		enderecos.remove(endereco);
	}

	private void mapperViaCepEnderecos() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		List<Endereco> e2 = mapper.readValue(new StringReader(response.toString()),
				new TypeReference<List<Endereco>>() {
				});

		for (Endereco y : e2) {
			addEndereco(y);
		}
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public Endereco localizaCep(String cep) {
		return enderecos.stream().filter(x -> x.getCep().equals(cep)).findFirst().orElse(null);
	}

	private void mapperViaCep() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Endereco e = mapper.readValue(new StringReader(response.toString()), Endereco.class);

		addEndereco(e);
	}
}
