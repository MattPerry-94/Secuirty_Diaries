package org.tools;

import org.bdd_manager.UserManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminTool {

    public static void main(String[] args) {
        Map<String, String> options = parseOptions(args);
        if (options.containsKey("--help") || options.containsKey("-h")) {
            printUsage();
            return;
        }

        String username = getOrPrompt(options, "--username", "Nom d'utilisateur: ", false);
        String firstName = getOrPrompt(options, "--first-name", "Prénom: ", false);
        String lastName = getOrPrompt(options, "--last-name", "Nom: ", false);
        String password = getOrPrompt(options, "--password", "Mot de passe: ", true);

        try {
            UserManager.add_admin(username, firstName, lastName, password);
            System.out.println("Admin créé: " + username);
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

    private static String getOrPrompt(Map<String, String> options, String key, String prompt, boolean secret) {
        try {
            String value = options.get(key);
            if (value != null && !value.isBlank()) {
                return value;
            }

            if (secret && System.console() != null) {
                char[] input = System.console().readPassword(prompt);
                if (input == null || input.length == 0) {
                    System.err.println("Valeur manquante pour: " + key);
                    System.exit(2);
                }
                return new String(input);
            }

            System.out.print(prompt);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            String input = reader.readLine();
            if (input == null || input.isBlank()) {
                System.err.println("Valeur manquante pour: " + key);
                System.exit(2);
            }
            return input;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void printUsage() {
        System.out.println("Utilisation :");
        System.out.println("  AdminTool --username <val> --first-name <val> --last-name <val> [--password <val>]");
    }
}
