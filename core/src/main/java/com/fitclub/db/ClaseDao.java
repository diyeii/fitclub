package com.fitclub.db;

import com.fitclub.model.Clase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClaseDao {

    // ── INSERT ──────────────────────────────────────────────────────────────
    public boolean insertar(Clase clase) {
        String sql = "INSERT INTO clase (nombre, profesor, fecha_hora, aforo_max) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clase.getName());
            pstmt.setString(2, clase.getCoach());
            pstmt.setTimestamp(3, Timestamp.valueOf(clase.getLocalDateTime()));
            pstmt.setInt(4, clase.getMax_aforo());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("  ✗ Error al insertar clase: " + e.getMessage());
            return false;
        }
    }

    // ── SELECT ALL ──────────────────────────────────────────────────────────
    public List<Clase> listarTodas() {
        List<Clase> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, profesor, fecha_hora, aforo_max FROM clase ORDER BY fecha_hora";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al listar clases: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY ID ────────────────────────────────────────────────────────
    public Clase buscarPorId(int id) {
        String sql = "SELECT id, nombre, profesor, fecha_hora, aforo_max FROM clase WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al buscar clase: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────
    public boolean actualizar(Clase clase) {
        String sql = "UPDATE clase SET nombre=?, profesor=?, fecha_hora=?, aforo_max=? WHERE id=?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clase.getName());
            pstmt.setString(2, clase.getCoach());
            pstmt.setTimestamp(3, Timestamp.valueOf(clase.getLocalDateTime()));
            pstmt.setInt(4, clase.getMax_aforo());
            pstmt.setInt(5, clase.getId());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("  ✗ Error al actualizar clase: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ──────────────────────────────────────────────────────────────
    public boolean borrar(int id) {
        String sql = "DELETE FROM clase WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("  ✗ Error al borrar clase: " + e.getMessage());
            return false;
        }
    }

    // ── CLASES FUTURAS CON PLAZAS LIBRES ────────────────────────────────────
    /**
     * Devuelve las clases cuya fecha_hora > ahora y que aún tienen plazas libres.
     * Cada elemento del array: [Clase, Integer plazasLibres]
     */
    public List<Object[]> listarFuturasConPlazas() {
        List<Object[]> resultado = new ArrayList<>();
        String sql = """
                SELECT c.id, c.nombre, c.profesor, c.fecha_hora, c.aforo_max,
                       (c.aforo_max - COUNT(r.id)) AS plazas_libres
                FROM clase c
                LEFT JOIN reserva r ON c.id = r.clase_id
                WHERE c.fecha_hora > NOW()
                GROUP BY c.id, c.nombre, c.profesor, c.fecha_hora, c.aforo_max
                HAVING plazas_libres > 0
                ORDER BY c.fecha_hora
                """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Clase clase = mapear(rs);
                int plazasLibres = rs.getInt("plazas_libres");
                resultado.add(new Object[]{clase, plazasLibres});
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al listar clases con plazas: " + e.getMessage());
        }
        return resultado;
    }

    // ── CONTAR RESERVAS DE UNA CLASE ────────────────────────────────────────
    public int contarReservas(int claseId) {
        String sql = "SELECT COUNT(*) FROM reserva WHERE clase_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, claseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al contar reservas: " + e.getMessage());
        }
        return 0;
    }

    // ── HELPER ──────────────────────────────────────────────────────────────
    private Clase mapear(ResultSet rs) throws SQLException {
        return new Clase(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("profesor"),
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getInt("aforo_max")
        );
    }
}
