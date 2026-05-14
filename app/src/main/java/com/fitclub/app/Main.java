package com.fitclub.app;

import com.fitclub.db.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Comprobamos la conexión antes de arrancar
        System.out.println("  Conectando con la base de datos...");
        try (Connection test = ConexionBD.getConnection()) {
            System.out.println("  ✓ Conexión establecida correctamente.\n");
        } catch (SQLException e) {
            System.out.println("  ✗ No se pudo conectar a la base de datos: " + e.getMessage());
            System.out.println("  Asegúrate de que el contenedor Docker está arriba (cd docker && docker compose up -d)");
            return;
        }

        Scanner sc = new Scanner(System.in);
        MenuSocios menuSocios = new MenuSocios();
        MenuClases menuClases = new MenuClases();
        MenuReservas menuReservas = new MenuReservas();

        int opcion;
        do {
            System.out.println("========================================");
            System.out.println("           💪  FITCLUB  💪              ");
            System.out.println("========================================");
            System.out.println("  1) Socios");
            System.out.println("  2) Clases");
            System.out.println("  3) Reservas");
            System.out.println("  0) Salir");
            System.out.println("----------------------------------------");
            System.out.print("Elige una opción: ");

            opcion = MenuSocios.leerEntero(sc);

            switch (opcion) {
                case 1 -> menuSocios.mostrar(sc);
                case 2 -> menuClases.mostrar(sc);
                case 3 -> menuReservas.mostrar(sc);
                case 0 -> System.out.println("\n¡Hasta luego! 👋\n");
                default -> System.out.println("\n  Opción no válida. Inténtalo de nuevo.\n");
            }

        } while (opcion != 0);

        sc.close();
    }
}
