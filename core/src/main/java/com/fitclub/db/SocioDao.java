package com.fitclub.db;

import com.fitclub.model.Socio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SocioDao {

    public void insertar(Socio socio) {
        String sql = "INSERT INTO socio (nombre, apellidos, email) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


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