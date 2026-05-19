package com.fitclub.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String url = "jdbc:mysql://localhost:3306/fitclub";
    private static final String user = "root";
    private static final String pass = "Sandia4you";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url,user,pass);
        }catch (ClassNotFoundException e){
            throw new SQLException("No se encontró el driver...", e);
        }
    }
}
