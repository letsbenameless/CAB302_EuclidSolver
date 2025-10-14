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

    public static String[] generate() {
        String expr;
        String answer;

        while (true) {
            try {
                expr = generateBracketedExpression();
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

                return new String[]{expr, answer}; // ✅ Return expression and answer

            } catch (Exception e) {
                // Retry on any error
            }
        }
    }

    private static String generateBracketedExpression() {
        String[] ops = {"+", "-", "*", "/", "^"};
        int numGroups = ThreadLocalRandom.current().nextInt(2, 4); // 2–3 grouped sub-expressions

        List<String> groups = new ArrayList<>();

        for (int g = 0; g < numGroups; g++) {
            int termsInGroup = ThreadLocalRandom.current().nextInt(2, 4); // 2–3 terms per group
            StringBuilder group = new StringBuilder();

            for (int i = 0; i < termsInGroup; i++) {
                String term = randomTerm();
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

    private static String randomTerm() {
        boolean useSqrt = Math.random() < 0.3;
        int val = ThreadLocalRandom.current().nextInt(2, 11); // max √100
        return useSqrt ? "sqrt(" + (val * val) + ")" : String.valueOf(val);
    }
}