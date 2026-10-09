package main;

import util.DatabaseConnection;
import vista.VentanaLogin;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            DatabaseConnection.getInstance();

        } catch (Exception e) {
            System.out.println("No se pudo conectar a la base de datos.");

            return;
        }

        SwingUtilities.invokeLater(() -> {

            VentanaLogin ventana = new VentanaLogin();

            ventana.setVisible(true);
        });
    }
}
