package org.bdd_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/security";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            // Charger le driver MySQL (optionnel en JDBC 4+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Retourner un objet Connection
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC MySQL introuvable !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base !");
            e.printStackTrace();
        }

        return null; // si la connexion échoue
    }
}