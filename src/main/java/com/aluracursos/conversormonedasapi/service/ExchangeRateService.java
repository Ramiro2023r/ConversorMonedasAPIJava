package com.aluracursos.conversormonedasapi.service;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ExchangeRateService {

    private static final String API_KEY = "d4de3f9a163635b455e031d8";

    public double obtenerTasa(String base, String destino) throws IOException, InterruptedException {

        String url = "https://v6.exchangerate-api.com/v6/"
                + API_KEY + "/pair/" + base + "/" + destino;

        HttpClient cliente = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> respuesta =
                cliente.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = JsonParser
                .parseString(respuesta.body())
                .getAsJsonObject();

        return json.get("conversion_rate").getAsDouble();
    }

    public double convertir(String base, String destino, double cantidad)
            throws IOException, InterruptedException {

        double tasa = obtenerTasa(base, destino);
        return cantidad * tasa;
    }
}