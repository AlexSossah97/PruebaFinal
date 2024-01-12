package com.example.pruebafinal.DTO;
import java.time.LocalDate;


public class CalculadoraForm {
    private LocalDate fecha;
    private String moneda;

    public LocalDate getFecha() {

        return fecha;
    }

    public void setFecha(LocalDate fecha) {

        this.fecha = fecha;
    }

    public String getMoneda() {

        return moneda;
    }

    public void setMoneda(String moneda) {

        this.moneda = moneda;
    }


}