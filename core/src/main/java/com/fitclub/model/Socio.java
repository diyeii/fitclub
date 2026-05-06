package com.fitclub.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socio {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String number;

}
