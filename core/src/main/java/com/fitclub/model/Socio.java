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

    @Override
    public String toString(){
        return String.format("[%d] %s %s - %s tlfn:%s", id, name, surname, email, number);
    }
}
