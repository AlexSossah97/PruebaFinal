package com.example.pruebafinal.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class CalculadoraFechaController {

    private static final Logger logger = LoggerFactory.getLogger(CalculadoraFechaController.class);

    @Value("${api.url}") //url de la api desde la propierties
    private String apiUrl;

    private final WebClient webClient;

    public CalculadoraFechaController(WebClient.Builder webClientBuilder) {
        logger.info(apiUrl);
        this.webClient = webClientBuilder.baseUrl("https://mindicador.cl/").build();
    }

    @GetMapping("/")
    public String mostrarPagina(Model model) {

        return "index";
    }

    @PostMapping("/calcular")
    public String calcular(Model model) {

        logger.info(apiUrl);
        Double resultado = traerValorAlDia();
        model.addAttribute("resultado", resultado);
        return "index";
    }

    @ResponseBody
    public Double traerValorAlDia() {
        try {

            LocalDate fechaHoy = LocalDate.now();
            logger.info("Datos fecha hoy: " + fechaHoy);
            String fechaHoyStr = fechaHoy.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            //Realiza la llamada a la API
            String response = webClient.get()
                .uri("/api/uf/{fechahoy}",fechaHoyStr) //ruta del end point de la api
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            logger.info(response);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            //extrae el valor del JSON
            double valor = jsonNode.path("serie").get(0).path("valor").asDouble();
            
            return valor;
        } catch (Exception e) {
            logger.error("Error al obtener el valor al día", e);
            return 0.0; 
        }
    }
}