package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "duoc131@";

    private static Connection instancia;

    private DatabaseConnection() {
    }

    //Metodo para obtener la conexion a la base de datos
    public static synchronized Connection getInstance() throws SQLException {

        if (instancia == null || instancia.isClosed()) {
            instancia = DriverManager.getConnection(URL, USUARIO, PASSWORD);

            System.out.println("Conexión exitosa a la base de datos.");
        }

        return instancia;
    }

}