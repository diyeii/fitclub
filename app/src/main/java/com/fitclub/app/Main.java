package com.fitclub.app;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("========================================");
            System.out.println(" FITCLUB ");
            System.out.println("========================================");
            System.out.println("  1) Gestionar socios");
            System.out.println("  2) Gestionar clases");
            System.out.println("  3) Gestionar reservas");
            System.out.println("  0) Salir");
            System.out.println("----------------------------------------");
            System.out.print("Elige una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> System.out.println("\n>> Gestionar clientes (pendiente de implementar)\n");
                case 2 -> System.out.println("\n>> Gestionar productos (pendiente de implementar)\n");
                case 3 -> System.out.println("\n>> Gestionar pedidos (pendiente de implementar)\n");
                case 0 -> System.out.println("\n¡Hasta luego!\n");
                default -> System.out.println("\nOpción no válida. Inténtalo de nuevo.\n");
            }

        } while (opcion != 0);
    }
}
