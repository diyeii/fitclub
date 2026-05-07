package com.fitclub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class Reserva {
    private int id;
    private int socio_id;
    private int clase_id;
    private LocalDate localDate;

    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaBonita = (localDate !=null) ? localDate.format(formatter): "Sin fecha";
        return String.format("Reserva #%d | Socio ID: %d | Clase ID: %d | Realizada el: %s",
                id, socio_id, clase_id, fechaBonita);
    }
}
