package com.fitclub.app;

import com.fitclub.db.ClaseDao;
import com.fitclub.db.ReservaDao;
import com.fitclub.db.SocioDao;
import com.fitclub.model.Reserva;
import com.fitclub.model.Socio;

import java.util.List;
import java.util.Scanner;

public class MenuReservas {

    private final ReservaDao reservaDao = new ReservaDao();
    private final SocioDao   socioDao   = new SocioDao();
    private final ClaseDao   claseDao   = new ClaseDao();

    public void mostrar(Scanner sc) {
        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("  RESERVAS");
            System.out.println("========================================");
            System.out.println("  1) Reservar plaza en una clase");
            System.out.println("  2) Cancelar una reserva");
            System.out.println("  3) Ver reservas de un socio");
            System.out.println("  4) Ver clases con plazas libres");
            System.out.println("  0) Volver al menú principal");
            System.out.println("----------------------------------------");
            System.out.print("Elige una opción: ");
            opcion = MenuSocios.leerEntero(sc);

            switch (opcion) {
                case 1 -> reservar(sc);
                case 2 -> cancelar(sc);
                case 3 -> verReservasSocio(sc);
                case 4 -> verClasesConPlazas();
                case 0 -> System.out.println("  Volviendo...");
                default -> System.out.println("  Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ── RESERVAR ─────────────────────────────────────────────────────────────
    private void reservar(Scanner sc) {
        // Mostrar socios
        System.out.println("\n--- Socios disponibles ---");
        socioDao.listarTodos().forEach(s -> System.out.println("  " + s));
        System.out.print("  ID del socio (0 para cancelar): ");
        int socioId = MenuSocios.leerEntero(sc);
        if (socioId == 0) return;

        if (socioDao.buscarPorId(socioId) == null) {
            System.out.println("  ✗ No existe un socio con ese ID.");
            return;
        }

        // Mostrar clases con plazas
        System.out.println("\n--- Clases futuras con plazas libres ---");
        List<Object[]> clases = claseDao.listarFuturasConPlazas();
        if (clases.isEmpty()) {
            System.out.println("  No hay clases disponibles con plazas libres.");
            return;
        }
        clases.forEach(fila -> {
            com.fitclub.model.Clase c = (com.fitclub.model.Clase) fila[0];
            int libres = (int) fila[1];
            System.out.printf("  %s | %d/%d plazas libres%n", c, libres, c.getMax_aforo());
        });

        System.out.print("  ID de la clase (0 para cancelar): ");
        int claseId = MenuSocios.leerEntero(sc);
        if (claseId == 0) return;

        if (reservaDao.reservar(socioId, claseId)) {
            System.out.println("  ✓ Reserva realizada con éxito.");
        }
    }

    // ── CANCELAR ─────────────────────────────────────────────────────────────
    private void cancelar(Scanner sc) {
        // Mostrar socios para elegir
        System.out.println("\n--- Socios ---");
        socioDao.listarTodos().forEach(s -> System.out.println("  " + s));
        System.out.print("  ID del socio (0 para cancelar): ");
        int socioId = MenuSocios.leerEntero(sc);
        if (socioId == 0) return;

        Socio socio = socioDao.buscarPorId(socioId);
        if (socio == null) {
            System.out.println("  ✗ No existe un socio con ese ID.");
            return;
        }

        // Mostrar sus reservas
        List<Object[]> reservas = reservaDao.listarPorSocio(socioId);
        if (reservas.isEmpty()) {
            System.out.println("  El socio no tiene reservas activas.");
            return;
        }

        System.out.printf("\n--- Reservas de %s %s ---%n", socio.getName(), socio.getSurname());
        reservas.forEach(fila -> {
            Reserva r = (Reserva) fila[0];
            String claseNombre = (String) fila[1];
            String claseFecha  = (String) fila[2];
            System.out.printf("  [%d] Clase: %-20s | Fecha clase: %s | Reservada el: %s%n",
                    r.getId(), claseNombre, claseFecha, r.getLocalDate());
        });

        System.out.print("  ID de la reserva a cancelar (0 para cancelar): ");
        int reservaId = MenuSocios.leerEntero(sc);
        if (reservaId == 0) return;

        // Verificamos que la reserva pertenece a este socio
        Reserva reserva = reservaDao.buscarPorId(reservaId);
        if (reserva == null || reserva.getSocio_id() != socioId) {
            System.out.println("  ✗ Reserva no encontrada o no pertenece a este socio.");
            return;
        }

        System.out.print("  ¿Seguro que quieres cancelar esta reserva? (s/n): ");
        String conf = sc.nextLine().trim().toLowerCase();
        if (!conf.equals("s")) {
            System.out.println("  Operación cancelada.");
            return;
        }

        if (reservaDao.cancelar(reservaId)) {
            System.out.println("  ✓ Reserva cancelada correctamente.");
        }
    }

    // ── VER RESERVAS DE UN SOCIO ─────────────────────────────────────────────
    private void verReservasSocio(Scanner sc) {
        System.out.println("\n--- Socios ---");
        socioDao.listarTodos().forEach(s -> System.out.println("  " + s));
        System.out.print("  ID del socio (0 para cancelar): ");
        int socioId = MenuSocios.leerEntero(sc);
        if (socioId == 0) return;

        Socio socio = socioDao.buscarPorId(socioId);
        if (socio == null) {
            System.out.println("  ✗ No existe un socio con ese ID.");
            return;
        }

        List<Object[]> reservas = reservaDao.listarPorSocio(socioId);
        if (reservas.isEmpty()) {
            System.out.printf("  %s %s no tiene reservas.%n", socio.getName(), socio.getSurname());
            return;
        }

        System.out.printf("\n--- Reservas de %s %s ---%n", socio.getName(), socio.getSurname());
        reservas.forEach(fila -> {
            Reserva r = (Reserva) fila[0];
            String claseNombre = (String) fila[1];
            String claseFecha  = (String) fila[2];
            System.out.printf("  [Reserva #%d] %-20s | Fecha: %s%n",
                    r.getId(), claseNombre, claseFecha);
        });
    }

    // ── VER CLASES CON PLAZAS ────────────────────────────────────────────────
    private void verClasesConPlazas() {
        List<Object[]> lista = claseDao.listarFuturasConPlazas();
        if (lista.isEmpty()) {
            System.out.println("\n  No hay clases futuras con plazas disponibles.");
            return;
        }
        System.out.println("\n--- Clases con plazas libres ---");
        lista.forEach(fila -> {
            com.fitclub.model.Clase c = (com.fitclub.model.Clase) fila[0];
            int libres = (int) fila[1];
            System.out.printf("  %s | %d plazas libres de %d%n", c, libres, c.getMax_aforo());
        });
    }
}
