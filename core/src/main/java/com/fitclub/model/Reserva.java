package com.fitclub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class Reserva {
    private int id;
    private int socio_id;
    private int clase_id;
    private LocalDate localDate;
}
