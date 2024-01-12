package com.example.pruebafinal.DTO;

import java.time.LocalDate;

public class ResultadosMes {

    private LocalDate primerDia;
    private LocalDate ultimoDia;
    private double promedio;
    private double porcentajeVariacion;

    public LocalDate getPrimerDia() {

        return primerDia;
    }

    public void setPrimerDia(LocalDate primerDia) {

        this.primerDia = primerDia;
    }

    public LocalDate getUltimoDia() {

        return ultimoDia;
    }

    public void setUltimoDia(LocalDate ultimoDia) {

        this.ultimoDia = ultimoDia;
    }

    public double getPromedio() {

        return promedio;
    }

    public void setPromedio(double promedio) {

        this.promedio = promedio;
    }

    public double getPorcentajeVariacion() {

        return porcentajeVariacion;
    }

    public void setPorcentajeVariacion(double porcentajeVariacion) {

        this.porcentajeVariacion = porcentajeVariacion;
    }

}
