package org.bdd_manager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {

    public static void add_user(String username, String first_name, String last_name, String password){
        add_user(username, first_name, last_name, password, false);
    }

    public static void add_admin(String username, String first_name, String last_name, String password){
        add_user(username, first_name, last_name, password, true);
    }

    public static void add_user(String username, String first_name, String last_name, String password, boolean isAdmin){
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Impossible d'obtenir une connexion à la base de données");
        }

        validatePassword(password);

        String hashedPwd = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sqlRequest = "INSERT INTO users (username, first_name, last_name, pwd, is_admin) VALUES(?, ?, ?, ?, ?)";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, username);
            pstmt.setString(2, first_name);
            pstmt.setString(3, last_name);
            pstmt.setString(4, hashedPwd);
            pstmt.setBoolean(5, isAdmin);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Mot de passe manquant");
        }
        if (password.length() < 12) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 12 caractères");
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isWhitespace(c)) {
                throw new IllegalArgumentException("Le mot de passe ne doit pas contenir d'espaces");
            }
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        if (!hasLower || !hasUpper || !hasDigit || !hasSpecial) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins une minuscule, une majuscule, un chiffre et un caractère spécial");
        }
    }

    public static boolean connexion(String username, String password) {

        Connection connection = DatabaseConnection.getConnection();
        String sql_request = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql_request);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String pwd = rs.getString("pwd");

                if (BCrypt.checkpw(password, pwd)) {
                    return true;
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
