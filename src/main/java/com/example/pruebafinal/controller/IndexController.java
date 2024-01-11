package com.example.pruebafinal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final WebClient webClient;

    public IndexController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://mindicador.cl/").build();
    }



    @GetMapping("/llamar-api")
    @ResponseBody
    public String llamarAPI() {
        String response = webClient.get()
                .uri("/api") //ruta del endponint
                .retrieve()
                .bodyToMono(String.class)
                .block();

        logger.info("Llamando al API");
        return response;
    }
}
