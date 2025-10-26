package com.CAB302_EuclidSolver.model.question;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class problemGenerator {

    // ─────────────────────────────────────────────
    // Public interface
    // ─────────────────────────────────────────────

    // Default (backward-compatible): hard difficulty
    public static String[] generate() {
        return generate("hard");
    }

    // New overload: specify difficulty
    public static String[] generate(String difficulty) {
        String expr;
        String answer;

        while (true) {
            try {
                expr = generateBracketedExpression(difficulty);
                String encodedExpr = URLEncoder.encode(expr, "UTF-8");
                String apiUrl = "https://api.mathjs.org/v4/?expr=" + encodedExpr;

                HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                answer = reader.readLine();
                reader.close();

                // Reject invalid or extreme values
                if (answer == null || answer.equalsIgnoreCase("Infinity") || answer.equalsIgnoreCase("NaN")) {
                    continue;
                }

                // Check for too many decimal places
                if (answer.contains(".")) {
                    String[] parts = answer.split("\\.");
                    if (parts.length > 1 && parts[1].length() > 3) {
                        continue;
                    }
                }

                double numericAnswer = Double.parseDouble(answer);
                if (Math.abs(numericAnswer) > 50000) {
                    System.out.println("⚠️ Answer too large: " + expr + " = " + numericAnswer);
                    continue;
                }

                return new String[]{expr, answer};

            } catch (Exception e) {
                // Retry on any error
            }
        }
    }

    // ─────────────────────────────────────────────
    // Expression generation logic
    // ─────────────────────────────────────────────

    private static String generateBracketedExpression(String difficulty) {
        String[] ops;
        int numGroups;
        int termsInGroupMax;

        switch (difficulty.toLowerCase()) {
            case "easy" -> {
                ops = new String[]{"+", "-"}; // only basic operations
                numGroups = ThreadLocalRandom.current().nextInt(1, 3); // 1–2 groups
                termsInGroupMax = 2; // fewer terms
            }
            case "hard" -> {
                ops = new String[]{"+", "-", "*", "/", "^"}; // full set including power
                numGroups = ThreadLocalRandom.current().nextInt(2, 4); // 2–3 groups
                termsInGroupMax = 3;
            }
            default -> {
                ops = new String[]{"+", "-", "*", "/"}; // medium by default
                numGroups = 2;
                termsInGroupMax = 3;
            }
        }

        List<String> groups = new ArrayList<>();

        for (int g = 0; g < numGroups; g++) {
            int termsInGroup = ThreadLocalRandom.current().nextInt(2, termsInGroupMax + 1);
            StringBuilder group = new StringBuilder();

            for (int i = 0; i < termsInGroup; i++) {
                String term = randomTerm(difficulty);
                group.append(term);

                if (i < termsInGroup - 1) {
                    String op = ops[ThreadLocalRandom.current().nextInt(ops.length)];
                    group.append(op);
                }
            }

            groups.add("(" + group + ")");
        }

        // Join all groups with random ops
        StringBuilder fullExpr = new StringBuilder(groups.get(0));
        for (int i = 1; i < groups.size(); i++) {
            String op = ops[ThreadLocalRandom.current().nextInt(ops.length)];
            fullExpr.append(" ").append(op).append(" ").append(groups.get(i));
        }

        return fullExpr.toString();
    }

    // ─────────────────────────────────────────────
    // Random term generator
    // ─────────────────────────────────────────────

    private static String randomTerm(String difficulty) {
        boolean useSqrt = difficulty.equalsIgnoreCase("hard") && Math.random() < 0.3;
        int upperBound = difficulty.equalsIgnoreCase("easy") ? 10 : 20; // smaller numbers for easy mode
        int val = ThreadLocalRandom.current().nextInt(2, upperBound);
        return useSqrt ? "sqrt(" + (val * val) + ")" : String.valueOf(val);
    }
}