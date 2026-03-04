package com.aluracursos.conversormonedasapi.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConversorMenu implements CommandLineRunner {

    private final ExchangeRateService service;

    public ConversorMenu(ExchangeRateService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("""
            
            ************************************
            Sea bienvenido al Conversor
            1) Dólar => Peso argentino
            2) Peso argentino => Dólar
            3) Dólar => Real brasileño
            4) Real brasileño => Dólar
            5) Dólar => Peso colombiano
            6) Peso colombiano => Dólar
            7) Salir
            Elija una opción válida:
            ************************************
            """);

            while (!scanner.hasNextInt()) {
                System.out.println("❌ Debes ingresar un número válido.");
                scanner.next();
            }
            opcion = scanner.nextInt();

            if (opcion < 1 || opcion > 7) {
                System.out.println("❌ Opción fuera de rango.");
                continue;
            }

            if (opcion == 7) break;

            double cantidad;

            while (true) {
                System.out.println("Ingrese el valor que deseas convertir:");

                if (scanner.hasNextDouble()) {
                    cantidad = scanner.nextDouble();

                    if (cantidad <= 0) {
                        System.out.println("❌ El valor debe ser mayor que cero.");
                        continue;
                    }

                    break;

                } else {
                    System.out.println("❌ Entrada inválida. Debes ingresar un número.");
                    scanner.next(); // limpia la entrada incorrecta
                }
            }

            String base = "";
            String destino = "";

            switch (opcion) {
                case 1 -> { base = "USD"; destino = "ARS"; }
                case 2 -> { base = "ARS"; destino = "USD"; }
                case 3 -> { base = "USD"; destino = "BRL"; }
                case 4 -> { base = "BRL"; destino = "USD"; }
                case 5 -> { base = "USD"; destino = "COP"; }
                case 6 -> { base = "COP"; destino = "USD"; }
                default -> System.out.println("Opción inválida");
            }
            if (base.isEmpty() || destino.isEmpty()) {
                System.out.println("Opción inválida.");
                continue;
            }

            double resultado = service.convertir(base, destino, cantidad);

            System.out.println("El valor "+ cantidad + " " + base + " corresponde al valor final de => " + resultado + " " + destino);

        } while (opcion != 7);

        System.out.println("Programa finalizado.");
    }
}