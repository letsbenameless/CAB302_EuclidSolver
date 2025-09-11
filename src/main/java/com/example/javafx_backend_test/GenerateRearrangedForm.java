package com.example.javafx_backend_test;

import java.util.concurrent.ThreadLocalRandom;

public class GenerateRearrangedForm {

    public static String from(String expr, String answer) {
        String left = "a";
        String right = "(" + expr + ")";
        double aVal = Double.parseDouble(answer);

        int steps = ThreadLocalRandom.current().nextInt(2, 4); // 2–3 steps

        for (int i = 0; i < steps; i++) {
            int op = ThreadLocalRandom.current().nextInt(7); // 0 to 6

            switch (op) {
                case 0 -> { // Add constant
                    int k = rand(1, 10);
                    left = new StringBuilder().append("(").append(left).append(") + (").append(k).append(")").toString();
                    right = new StringBuilder().append("(").append(right).append(") + (").append(k).append(")").toString();
                }
                case 1 -> { // Subtract constant
                    int k = rand(1, 10);
                    left = new StringBuilder().append("(").append(left).append(") - (").append(k).append(")").toString();
                    right = new StringBuilder().append("(").append(right).append(") - (").append(k).append(")").toString();
                }
                case 2 -> { // Multiply
                    int k = rand(2, 4);
                    left = new StringBuilder().append("(").append(k).append(") * (").append(left).append(")").toString();
                    right = new StringBuilder().append("(").append(k).append(") * (").append(right).append(")").toString();
                }
                case 3 -> { // Divide
                    int k = rand(2, 4);
                    left = new StringBuilder().append("(").append(left).append(") / (").append(k).append(")").toString();
                    right = new StringBuilder().append("(").append(right).append(") / (").append(k).append(")").toString();
                }
                case 4 -> { // Square
                    left = new StringBuilder().append("(").append(left).append(") ^ (2)").toString();
                    right = new StringBuilder().append("(").append(right).append(") ^ (2)").toString();
                }
                case 5 -> { // Square root (only if aVal ≥ 0)
                    if (aVal >= 0) {
                        left = new StringBuilder().append("sqrt(").append(left).append(")").toString();
                        right = new StringBuilder().append("sqrt(").append(right).append(")").toString();
                    }
                }
            }
            // OPTIONAL: simplify parts of the right expression with Math.js (stub)
            // right = simplifyOnePiece(right);
        }

        return left + " = " + right;
    }

    private static int rand(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // (Optional future expansion for simplification via Math.js)
    /*
    private static String simplifyOnePiece(String expr) {
        // hook into Math.js here if needed
        return expr;
    }
    */
}