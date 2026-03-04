package com.aluracursos.conversormonedasapi.service;

import com.google.gson.Gson;
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

    private final HttpClient cliente = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public double obtenerTasa(String base, String destino)
            throws IOException, InterruptedException {

        String url = "https://v6.exchangerate-api.com/v6/"
                + API_KEY + "/pair/" + base + "/" + destino;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> respuesta =
                cliente.send(request, HttpResponse.BodyHandlers.ofString());

        //  Verificar código HTTP
        if (respuesta.statusCode() != 200) {
            throw new RuntimeException("Error HTTP: " + respuesta.statusCode()
                    + " - " + respuesta.body());
        }

        // Verificar que la respuesta no esté vacía
        if (respuesta.body() == null || respuesta.body().isEmpty()) {
            throw new RuntimeException("La API devolvió una respuesta vacía.");
        }

        JsonObject json;

        try {
            json = JsonParser
                    .parseString(respuesta.body())
                    .getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException("La respuesta no es un JSON válido: "
                    + respuesta.body());
        }

        //  Verificar si la API devolvió error
        if (json.has("result") && json.get("result").getAsString().equals("error")) {
            String tipoError = json.has("error-type")
                    ? json.get("error-type").getAsString()
                    : "Error desconocido";
            throw new RuntimeException("Error en API: " + tipoError);
        }

        //  Verificar que exista conversion_rate
        if (!json.has("conversion_rate")) {
            throw new RuntimeException("La respuesta no contiene conversion_rate.");
        }

        return json.get("conversion_rate").getAsDouble();
    }

    public double convertir(String base, String destino, double cantidad)
            throws IOException, InterruptedException {

        if (base == null || destino == null || base.isEmpty() || destino.isEmpty()) {
            throw new IllegalArgumentException("Monedas inválidas.");
        }

        double tasa = obtenerTasa(base, destino);
        return cantidad * tasa;
    }
}