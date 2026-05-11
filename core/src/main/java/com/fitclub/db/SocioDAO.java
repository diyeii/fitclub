package com.fitclub.db;

import com.fitclub.model.Socio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SocioDAO {

    public void insertar(Socio socio) {
        String sql = "INSERT INTO socio (nombre, apellidos, email) VALUES (?, ?, ?)";

        // Fíjate que aquí usamos ConexionBd (con 'd' minúscula) como tu archivo
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Si en tu clase Socio los métodos están en español, usa estos:
            pstmt.setString(1, socio.getName());
            pstmt.setString(2, socio.getSurname());
            pstmt.setString(3, socio.getEmail());

            pstmt.executeUpdate();
            System.out.println("Socio guardado con éxito");

        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
}