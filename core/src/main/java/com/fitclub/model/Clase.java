package com.fitclub.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Clase {
    private int id;
    private String name;
    private String coach;
    private LocalDateTime localDateTime;
    private int max_aforo;


}
