package com.aluracursos.conversormonedasapi.controller;
import com.aluracursos.conversormonedasapi.service.ExchangeRateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversor")
public class ConversorController {

    private final ExchangeRateService service;

    public ConversorController(ExchangeRateService service) {
        this.service = service;
    }

    @GetMapping
    public String convertir(
            @RequestParam String base,
            @RequestParam String destino,
            @RequestParam double cantidad
    ) throws Exception {

        double resultado = service.convertir(base, destino, cantidad);

        return "Resultado: " + resultado + " " + destino;
    }
}
