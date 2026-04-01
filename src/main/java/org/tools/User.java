package org.tools;

import org.bdd_manager.UserManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {

    public static void createUser(String username, String first_name, String last_name, String password){
        UserManager.add_user(username, first_name, last_name, password);
    }
    public static void main(String[] args){
        Map<String, String> options = parseOptions(args);
        if (options.containsKey("--help") || options.containsKey("-h")) {
            printUsage();
            return;
        }

        String username = require(options, "--username");
        String firstName = require(options, "--first-name");
        String lastName = require(options, "--last-name");
        String password = options.get("--password");
        if (password == null || password.isBlank()) {
            password = promptPassword();
        }

        try {
            User.createUser(username, firstName, lastName, password);
            System.out.println("Utilisateur créé: " + username);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur: " + e.getMessage());
            System.exit(2);
        }
    }

    private static Map<String, String> parseOptions(String[] args) {
        Map<String, String> options = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            String token = args[i];
            if (token.equals("--help") || token.equals("-h")) {
                options.put(token, "true");
                continue;
            }
            if (!token.startsWith("--")) {
                System.err.println("Argument invalide: " + token);
                printUsage();
                System.exit(2);
            }
            if (i + 1 >= args.length) {
                System.err.println("Valeur manquante pour: " + token);
                printUsage();
                System.exit(2);
            }
            String value = args[++i];
            options.put(token, value);
        }
        return options;
    }

    private static String require(Map<String, String> options, String key) {
        String value = options.get(key);
        if (value == null || value.isBlank()) {
            System.err.println("Option obligatoire manquante: " + key);
            printUsage();
            System.exit(2);
        }
        return value;
    }

    private static String promptPassword() {
        try {
            if (System.console() != null) {
                char[] pwd = System.console().readPassword("Mot de passe: ");
                if (pwd == null || pwd.length == 0) {
                    System.err.println("Mot de passe vide");
                    System.exit(2);
                }
                return new String(pwd);
            }

            System.out.print("Mot de passe: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            String pwd = reader.readLine();
            if (pwd == null || pwd.isBlank()) {
                System.err.println("Mot de passe vide");
                System.exit(2);
            }
            return pwd;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void printUsage() {
        System.out.println("Utilisation :");
        System.out.println("  User --username <val> --first-name <val> --last-name <val> [--password <val>]");
    }
}
