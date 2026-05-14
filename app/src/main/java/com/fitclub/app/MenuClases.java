package com.fitclub.app;

import com.fitclub.db.ClaseDao;
import com.fitclub.model.Clase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuClases {

    private final ClaseDao dao = new ClaseDao();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void mostrar(Scanner sc) {
        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("  CLASES");
            System.out.println("========================================");
            System.out.println("  1) Listar todas las clases");
            System.out.println("  2) Ver clases con plazas libres");
            System.out.println("  3) Dar de alta una clase");
            System.out.println("  4) Modificar una clase");
            System.out.println("  5) Borrar una clase");
            System.out.println("  0) Volver al menú principal");
            System.out.println("----------------------------------------");
            System.out.print("Elige una opción: ");
            opcion = MenuSocios.leerEntero(sc);

            switch (opcion) {
                case 1 -> listar();
                case 2 -> listarConPlazas();
                case 3 -> alta(sc);
                case 4 -> modificar(sc);
                case 5 -> borrar(sc);
                case 0 -> System.out.println("  Volviendo...");
                default -> System.out.println("  Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ── LISTAR ──────────────────────────────────────────────────────────────
    void listar() {
        List<Clase> clases = dao.listarTodas();
        if (clases.isEmpty()) {
            System.out.println("  No hay clases registradas.");
            return;
        }
        System.out.println("\n--- Lista de clases ---");
        clases.forEach(c -> System.out.println("  " + c));
    }

    // ── LISTAR CON PLAZAS ────────────────────────────────────────────────────
    private void listarConPlazas() {
        List<Object[]> lista = dao.listarFuturasConPlazas();
        if (lista.isEmpty()) {
            System.out.println("  No hay clases futuras con plazas disponibles.");
            return;
        }
        System.out.println("\n--- Clases futuras con plazas libres ---");
        for (Object[] fila : lista) {
            Clase c = (Clase) fila[0];
            int libres = (int) fila[1];
            System.out.printf("  %s | %d plazas libres de %d%n", c, libres, c.getMax_aforo());
        }
    }

    // ── ALTA ─────────────────────────────────────────────────────────────────
    private void alta(Scanner sc) {
        System.out.println("\n--- Nueva clase ---");
        System.out.print("  Nombre de la clase  : ");
        String nombre = sc.nextLine().trim();
        System.out.print("  Profesor/a          : ");
        String profesor = sc.nextLine().trim();
        LocalDateTime fechaHora = pedirFechaHora(sc);
        if (fechaHora == null) return;
        System.out.print("  Aforo máximo        : ");
        int aforo = MenuSocios.leerEntero(sc);

        if (nombre.isBlank() || profesor.isBlank()) {
            System.out.println("  ✗ Nombre y profesor son obligatorios.");
            return;
        }
        if (aforo <= 0) {
            System.out.println("  ✗ El aforo debe ser mayor que 0.");
            return;
        }

        Clase clase = new Clase(0, nombre, profesor, fechaHora, aforo);
        if (dao.insertar(clase)) {
            System.out.println("  ✓ Clase creada correctamente.");
        }
    }

    // ── MODIFICAR ────────────────────────────────────────────────────────────
    private void modificar(Scanner sc) {
        listar();
        System.out.print("\n  ID de la clase a modificar (0 para cancelar): ");
        int id = MenuSocios.leerEntero(sc);
        if (id == 0) return;

        Clase clase = dao.buscarPorId(id);
        if (clase == null) {
            System.out.println("  ✗ No existe una clase con ese ID.");
            return;
        }

        System.out.println("  (Deja en blanco / 0 para conservar el valor actual)");
        System.out.printf("  Nombre     [%s]: ", clase.getName());
        String nombre = sc.nextLine().trim();
        System.out.printf("  Profesor   [%s]: ", clase.getCoach());
        String profesor = sc.nextLine().trim();
        System.out.printf("  Fecha/hora [%s] (dd/MM/yyyy HH:mm, Enter para conservar): ",
                clase.getLocalDateTime().format(FMT));
        String fechaStr = sc.nextLine().trim();
        System.out.printf("  Aforo máx  [%d] (0 para conservar): ", clase.getMax_aforo());
        int aforo = MenuSocios.leerEntero(sc);

        if (!nombre.isBlank()) clase.setName(nombre);
        if (!profesor.isBlank()) clase.setCoach(profesor);
        if (!fechaStr.isBlank()) {
            try {
                clase.setLocalDateTime(LocalDateTime.parse(fechaStr, FMT));
            } catch (DateTimeParseException e) {
                System.out.println("  ✗ Formato de fecha inválido. Conservando la fecha anterior.");
            }
        }
        if (aforo > 0) clase.setMax_aforo(aforo);

        if (dao.actualizar(clase)) {
            System.out.println("  ✓ Clase actualizada correctamente.");
        }
    }

    // ── BORRAR ───────────────────────────────────────────────────────────────
    private void borrar(Scanner sc) {
        listar();
        System.out.print("\n  ID de la clase a borrar (0 para cancelar): ");
        int id = MenuSocios.leerEntero(sc);
        if (id == 0) return;

        Clase clase = dao.buscarPorId(id);
        if (clase == null) {
            System.out.println("  ✗ No existe una clase con ese ID.");
            return;
        }

        System.out.printf("  ¿Seguro que quieres borrar la clase '%s'? Esto cancelará todas sus reservas. (s/n): ",
                clase.getName());
        String conf = sc.nextLine().trim().toLowerCase();
        if (!conf.equals("s")) {
            System.out.println("  Operación cancelada.");
            return;
        }

        if (dao.borrar(id)) {
            System.out.println("  ✓ Clase borrada correctamente.");
        }
    }

    // ── UTILIDAD ─────────────────────────────────────────────────────────────
    private LocalDateTime pedirFechaHora(Scanner sc) {
        System.out.print("  Fecha y hora (dd/MM/yyyy HH:mm): ");
        String input = sc.nextLine().trim();
        try {
            return LocalDateTime.parse(input, FMT);
        } catch (DateTimeParseException e) {
            System.out.println("  ✗ Formato de fecha inválido. Usa: dd/MM/yyyy HH:mm");
            return null;
        }
    }
}
