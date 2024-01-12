package com.example.pruebafinal.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.pruebafinal.DTO.MesDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CalculadoraMesController {

    private static final Logger logger = LoggerFactory.getLogger(CalculadoraMesController.class);

    @Value("${api.url}") //url desde la propierties
    private String apiUrl;
    private final WebClient webClient;

    public CalculadoraMesController(WebClient.Builder webClientBuilder) {
        logger.info(apiUrl);
        this.webClient = webClientBuilder.baseUrl("https://mindicador.cl/").build();
    }

    @GetMapping("/fecha")
    public String mostrarPaginaFecha(Model model) {
        model.addAttribute("mesDTO", new MesDTO());
        return "index";
    }

    @PostMapping("/calcularMes")
    public String calcularMes(@ModelAttribute MesDTO mesDTO, Model model) {
        LocalDate primerDia = mesDTO.getPrimerDiaDelMes();
        LocalDate ultimoDia = mesDTO.getUltimoDiaDelMes();
    
        //Obtiene las cantidades diarias para el valor del dolar
        double[] cantidadesDiarias = obtenerValoresDiarios(primerDia, ultimoDia);
    
        //Realiza los cálculos y agrega los resultados al modelo
        double porcentajeVariacion = calcularPorcentajeVariacion(cantidadesDiarias);
        double promedio = calcularPromedio(cantidadesDiarias);
        double moda = calcularModa(cantidadesDiarias);
    
        //agrega los resultados al modelo para mostrar en la misma página
        model.addAttribute("primerDia", primerDia.format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("ultimoDia", ultimoDia.format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("porcentajeVariacion", porcentajeVariacion);
        model.addAttribute("promedio", promedio);
        model.addAttribute("moda", moda);
    
        model.addAttribute("mesDTO", mesDTO);
    
        return "index";
    }
    

    private double[] obtenerValoresDiarios(LocalDate primerDia, LocalDate ultimoDia) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            //Inicializa el array para almacenar los valores diarios
            int dias = (int) primerDia.until(ultimoDia.plusDays(1)).getDays();
            double[] valoresDiarios = new double[dias];

            for (int i = 0; i < dias; i++) {
                //Contruye uri de la api con la fecha actual 
                String fechaActual = primerDia.plusDays(i).format(formatter);

                //Realiza la llamada a la API 
                JsonNode jsonNode = webClient.get()
                        .uri(apiUrl, fechaActual)
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .block();

                //extrae el valor deel dolar y lo almacena en un array
                double valorDolar = jsonNode.path("serie").get(0).path("valor").asDouble();
                valoresDiarios[i] = valorDolar;
            }

            return valoresDiarios;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los valores diarios", e);
        }
    }

    private double calcularPorcentajeVariacion(double[] cantidadesDiarias) {
        //Calcula el porcentaje de variacion
        double valorInicial = cantidadesDiarias[0];
        double valorFinal = cantidadesDiarias[cantidadesDiarias.length - 1];

        return ((valorFinal - valorInicial) / valorInicial) * 100;
    }

    private double calcularPromedio(double[] cantidadesDiarias) {
        //Calcula el promedio
        double suma = 0;
        for (double valor : cantidadesDiarias) {
            suma += valor;
        }

        return suma / cantidadesDiarias.length;
    }

    private double calcularModa(double[] cantidadesDiarias) {
        //calcula la moda 
        Map<Double, Integer> conteo = new HashMap<>();

        for (double valor : cantidadesDiarias) {
            conteo.put(valor, conteo.getOrDefault(valor, 0) + 1);
        }

        //Encuentra el valor que mas se repite (moda)
        double moda = -1;
        int maxRepet = 0;
        for (Map.Entry<Double, Integer> entry : conteo.entrySet()) {
            if (entry.getValue() > maxRepet) {
                moda = entry.getKey();
                maxRepet = entry.getValue();
            }
        }

        return moda;
    }
}
