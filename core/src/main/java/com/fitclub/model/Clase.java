package com.fitclub.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Clase {
    private int id;
    private String name;
    private String coach;
    private LocalDateTime localDateTime;
    private int max_aforo;

    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaBonita = (localDateTime !=null) ? localDateTime.format(formatter): "Sin fecha";
        return String.format("[%d] %-20s | Prof: %-15s | Horario: %s | Aforo Máx: %d",
                id, name, coach, fechaBonita, max_aforo);
    }

}
