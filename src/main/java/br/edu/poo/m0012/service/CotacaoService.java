package br.edu.poo.m0012.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.cdimascio.dotenv.Dotenv;

public class CotacaoService {

    public BigDecimal buscarCotacaoDolar() throws IOException, InterruptedException {
        String url = Dotenv.configure().ignoreIfMissing().load()
                .get("API_COTACAO_URL", "https://economia.awesomeapi.com.br/json/last/USD-BRL");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> resposta =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (resposta.statusCode() != 200) {
            throw new IOException("A API respondeu com status " + resposta.statusCode());
        }

        JsonObject raiz = JsonParser.parseString(resposta.body()).getAsJsonObject();
        String bid = raiz.getAsJsonObject("USDBRL").get("bid").getAsString();
        return new BigDecimal(bid);
    }
}