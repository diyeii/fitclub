package com.fitclub.db;

import com.fitclub.model.Socio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDao {

    // ── INSERT ──────────────────────────────────────────────────────────────
    public boolean insertar(Socio socio) {
        String sql = "INSERT INTO socio (nombre, apellidos, email, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, socio.getName());
            pstmt.setString(2, socio.getSurname());
            pstmt.setString(3, socio.getEmail());
            pstmt.setString(4, socio.getNumber());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry (email único)
                System.out.println("  ✗ Ya existe un socio con ese email.");
            } else {
                System.out.println("  ✗ Error al insertar socio: " + e.getMessage());
            }
            return false;
        }
    }

    // ── SELECT ALL ──────────────────────────────────────────────────────────
    public List<Socio> listarTodos() {
        List<Socio> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellidos, email, telefono FROM socio ORDER BY apellidos, nombre";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al listar socios: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY ID ────────────────────────────────────────────────────────
    public Socio buscarPorId(int id) {
        String sql = "SELECT id, nombre, apellidos, email, telefono FROM socio WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al buscar socio: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────
    public boolean actualizar(Socio socio) {
        String sql = "UPDATE socio SET nombre=?, apellidos=?, email=?, telefono=? WHERE id=?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, socio.getName());
            pstmt.setString(2, socio.getSurname());
            pstmt.setString(3, socio.getEmail());
            pstmt.setString(4, socio.getNumber());
            pstmt.setInt(5, socio.getId());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("  ✗ Ya existe un socio con ese email.");
            } else {
                System.out.println("  ✗ Error al actualizar socio: " + e.getMessage());
            }
            return false;
        }
    }

    // ── DELETE ──────────────────────────────────────────────────────────────

    /**
     * Borra un socio SOLO si no tiene reservas activas.
     * Devuelve true si se borró, false en caso contrario.
     */
    public boolean borrar(int id) {
        // Comprobamos reservas activas
        String sqlCheck = "SELECT COUNT(*) FROM reserva WHERE socio_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pCheck = conn.prepareStatement(sqlCheck)) {

            pCheck.setInt(1, id);
            try (ResultSet rs = pCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("  ✗ No se puede borrar: el socio tiene reservas activas.");
                    return false;
                }
            }

            String sqlDel = "DELETE FROM socio WHERE id = ?";
            try (PreparedStatement pDel = conn.prepareStatement(sqlDel)) {
                pDel.setInt(1, id);
                return pDel.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("  ✗ Error al borrar socio: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ──────────────────────────────────────────────────────────────
    private Socio mapear(ResultSet rs) throws SQLException {
        return new Socio(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("email"),
                rs.getString("telefono")
        );
    }
}
