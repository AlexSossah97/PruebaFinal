package com.example.pruebafinal.DTO;

import java.time.LocalDate;

public class MesDTO {
    private int year;
    private int month;

    public MesDTO() {
    }

    public MesDTO(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {

        return year;
    }

    public void setYear(int year) {

        this.year = year;
    }

    public int getMonth() {

        return month;
    }

    public void setMonth(int month) {

        this.month = month;
    }

    public LocalDate getPrimerDiaDelMes() {

        return LocalDate.of(year, month, 1);
    }

    public LocalDate getUltimoDiaDelMes() {
        return LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth());
    }
}
