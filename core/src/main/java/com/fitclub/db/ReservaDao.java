package com.fitclub.db;

import com.fitclub.model.Clase;
import com.fitclub.model.Reserva;
import com.fitclub.model.Socio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDao {

    // ── RESERVAR PLAZA ───────────────────────────────────────────────────────

    /**
     * Crea una reserva aplicando todas las reglas de negocio:
     * 1. La clase no puede haber pasado.
     * 2. El socio no puede tener ya esa clase reservada.
     * 3. Debe quedar al menos una plaza libre.
     *
     * @return true si se creó, false si alguna validación falla.
     */
    public boolean reservar(int socioId, int claseId) {
        // ── 1. Comprobamos que la clase existe y no ha pasado ────────────────
        String sqlClase = "SELECT fecha_hora, aforo_max FROM clase WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pClase = conn.prepareStatement(sqlClase)) {

            pClase.setInt(1, claseId);
            try (ResultSet rs = pClase.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("  ✗ La clase indicada no existe.");
                    return false;
                }
                Timestamp fechaHora = rs.getTimestamp("fecha_hora");
                if (fechaHora.toLocalDateTime().isBefore(java.time.LocalDateTime.now())) {
                    System.out.println("  ✗ No se puede reservar una clase que ya ha pasado.");
                    return false;
                }
                int aforoMax = rs.getInt("aforo_max");

                // ── 2. El socio no ha reservado ya esa clase ────────────────────
                String sqlDup = "SELECT COUNT(*) FROM reserva WHERE socio_id = ? AND clase_id = ?";
                try (PreparedStatement pDup = conn.prepareStatement(sqlDup)) {
                    pDup.setInt(1, socioId);
                    pDup.setInt(2, claseId);
                    try (ResultSet rsDup = pDup.executeQuery()) {
                        if (rsDup.next() && rsDup.getInt(1) > 0) {
                            System.out.println("  ✗ El socio ya tiene esta clase reservada.");
                            return false;
                        }
                    }
                }

                // ── 3. Comprobamos aforo ─────────────────────────────────────────
                String sqlAforo = "SELECT COUNT(*) FROM reserva WHERE clase_id = ?";
                try (PreparedStatement pAforo = conn.prepareStatement(sqlAforo)) {
                    pAforo.setInt(1, claseId);
                    try (ResultSet rsAforo = pAforo.executeQuery()) {
                        if (rsAforo.next() && rsAforo.getInt(1) >= aforoMax) {
                            System.out.println("  ✗ La clase está llena, no quedan plazas.");
                            return false;
                        }
                    }
                }

                // ── 4. Insertamos la reserva ─────────────────────────────────────
                String sqlIns = "INSERT INTO reserva (socio_id, clase_id, fecha_reserva) VALUES (?, ?, NOW())";
                try (PreparedStatement pIns = conn.prepareStatement(sqlIns)) {
                    pIns.setInt(1, socioId);
                    pIns.setInt(2, claseId);
                    pIns.executeUpdate();
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("  ✗ Error al reservar: " + e.getMessage());
            return false;
        }
    }

    // ── CANCELAR RESERVA ────────────────────────────────────────────────────
    public boolean cancelar(int reservaId) {
        String sql = "DELETE FROM reserva WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservaId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("  ✗ Error al cancelar reserva: " + e.getMessage());
            return false;
        }
    }

    // ── RESERVAS DE UN SOCIO ────────────────────────────────────────────────

    /**
     * Devuelve la lista de reservas de un socio enriquecida con el nombre de la clase.
     * Cada elemento del array: [Reserva, String nombreClase, String fechaHoreClase]
     */
    public List<Object[]> listarPorSocio(int socioId) {
        List<Object[]> resultado = new ArrayList<>();
        String sql = """
                SELECT r.id, r.socio_id, r.clase_id, r.fecha_reserva,
                       c.nombre AS clase_nombre, c.fecha_hora AS clase_fecha
                FROM reserva r
                JOIN clase c ON r.clase_id = c.id
                WHERE r.socio_id = ?
                ORDER BY c.fecha_hora
                """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, socioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva(
                            rs.getInt("id"),
                            rs.getInt("socio_id"),
                            rs.getInt("clase_id"),
                            rs.getTimestamp("fecha_reserva").toLocalDateTime().toLocalDate()
                    );
                    String claseNombre = rs.getString("clase_nombre");
                    String claseFecha = rs.getTimestamp("clase_fecha")
                            .toLocalDateTime()
                            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    resultado.add(new Object[]{reserva, claseNombre, claseFecha});
                }
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al listar reservas del socio: " + e.getMessage());
        }
        return resultado;
    }

    // ── BUSCAR RESERVA POR ID ───────────────────────────────────────────────
    public Reserva buscarPorId(int id) {
        String sql = "SELECT id, socio_id, clase_id, fecha_reserva FROM reserva WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Reserva(
                            rs.getInt("id"),
                            rs.getInt("socio_id"),
                            rs.getInt("clase_id"),
                            rs.getTimestamp("fecha_reserva").toLocalDateTime().toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("  ✗ Error al buscar reserva: " + e.getMessage());
        }
        return null;
    }
}
