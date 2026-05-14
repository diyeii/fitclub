package com.fitclub.app;

import com.fitclub.db.SocioDao;
import com.fitclub.model.Socio;

import java.util.List;
import java.util.Scanner;

public class MenuSocios {

    private final SocioDao dao = new SocioDao();

    public void mostrar(Scanner sc) {
        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("  SOCIOS");
            System.out.println("========================================");
            System.out.println("  1) Listar todos los socios");
            System.out.println("  2) Dar de alta un socio");
            System.out.println("  3) Modificar un socio");
            System.out.println("  4) Borrar un socio");
            System.out.println("  0) Volver al menú principal");
            System.out.println("----------------------------------------");
            System.out.print("Elige una opción: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1 -> listar();
                case 2 -> alta(sc);
                case 3 -> modificar(sc);
                case 4 -> borrar(sc);
                case 0 -> System.out.println("  Volviendo...");
                default -> System.out.println("  Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ── LISTAR ──────────────────────────────────────────────────────────────
    private void listar() {
        List<Socio> socios = dao.listarTodos();
        if (socios.isEmpty()) {
            System.out.println("  No hay socios registrados.");
            return;
        }
        System.out.println("\n--- Lista de socios ---");
        socios.forEach(s -> System.out.println("  " + s));
    }

    // ── ALTA ─────────────────────────────────────────────────────────────────
    private void alta(Scanner sc) {
        System.out.println("\n--- Nuevo socio ---");
        System.out.print("  Nombre    : ");
        String nombre = sc.nextLine().trim();
        System.out.print("  Apellidos : ");
        String apellidos = sc.nextLine().trim();
        System.out.print("  Email     : ");
        String email = sc.nextLine().trim();
        System.out.print("  Teléfono  : ");
        String telefono = sc.nextLine().trim();

        if (nombre.isBlank() || apellidos.isBlank() || email.isBlank()) {
            System.out.println("  ✗ Nombre, apellidos y email son obligatorios.");
            return;
        }

        Socio socio = new Socio(0, nombre, apellidos, email, telefono);
        if (dao.insertar(socio)) {
            System.out.println("  ✓ Socio dado de alta correctamente.");
        }
    }

    // ── MODIFICAR ────────────────────────────────────────────────────────────
    private void modificar(Scanner sc) {
        listar();
        System.out.print("\n  ID del socio a modificar (0 para cancelar): ");
        int id = leerEntero(sc);
        if (id == 0) return;

        Socio socio = dao.buscarPorId(id);
        if (socio == null) {
            System.out.println("  ✗ No existe un socio con ese ID.");
            return;
        }

        System.out.println("  (Deja en blanco para conservar el valor actual)");
        System.out.printf("  Nombre    [%s]: ", socio.getName());
        String nombre = sc.nextLine().trim();
        System.out.printf("  Apellidos [%s]: ", socio.getSurname());
        String apellidos = sc.nextLine().trim();
        System.out.printf("  Email     [%s]: ", socio.getEmail());
        String email = sc.nextLine().trim();
        System.out.printf("  Teléfono  [%s]: ", socio.getNumber());
        String telefono = sc.nextLine().trim();

        if (!nombre.isBlank()) socio.setName(nombre);
        if (!apellidos.isBlank()) socio.setSurname(apellidos);
        if (!email.isBlank()) socio.setEmail(email);
        if (!telefono.isBlank()) socio.setNumber(telefono);

        if (dao.actualizar(socio)) {
            System.out.println("  ✓ Socio actualizado correctamente.");
        }
    }

    // ── BORRAR ───────────────────────────────────────────────────────────────
    private void borrar(Scanner sc) {
        listar();
        System.out.print("\n  ID del socio a borrar (0 para cancelar): ");
        int id = leerEntero(sc);
        if (id == 0) return;

        Socio socio = dao.buscarPorId(id);
        if (socio == null) {
            System.out.println("  ✗ No existe un socio con ese ID.");
            return;
        }

        System.out.printf("  ¿Seguro que quieres borrar a %s %s? (s/n): ", socio.getName(), socio.getSurname());
        String conf = sc.nextLine().trim().toLowerCase();
        if (!conf.equals("s")) {
            System.out.println("  Operación cancelada.");
            return;
        }

        if (dao.borrar(id)) {
            System.out.println("  ✓ Socio borrado correctamente.");
        }
    }

    // ── UTILIDAD ─────────────────────────────────────────────────────────────
    static int leerEntero(Scanner sc) {
        while (true) {
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.print("  Introduce un número válido: ");
            }
        }
    }
}
