package com.example.pruebafinal.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class YearController {

    private final CalculadoraMesController calculadoraMesController;
    private final WebClient webClient;

    public YearController(CalculadoraMesController calculadoraMesController, WebClient.Builder webClientBuilder) {
        this.calculadoraMesController = calculadoraMesController;
        this.webClient = webClientBuilder.baseUrl("https://mindicador.cl/api").build();
    }
}
