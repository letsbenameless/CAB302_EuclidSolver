package com.CAB302_EuclidSolver.model.question;

// userProblemHandler.java
// Purpose: Bridge between backend logic and a web front-end (MathJax/KaTeX).
// - No console I/O
// - Returns LaTeX for display and offers an answer checker

public class userProblemHandler {

    private static final double DEFAULT_TOL = 1e-3;

    // Data object the front end can consume (JSON-ify if you like)
    public static class QuestionDTO {
        public final String equationLatex;  // e.g., "a = \frac{( ... )}{...}"
        public final String correctAnswerStr;
        public final double correctAnswer;
        public final double tolerance;      // default 1e-3
        public final String rawExpr;        // original arithmetic expr (for debugging)
        public final String rearrangedRaw;  // "a = ( ... )" before LaTeX conversion

        public QuestionDTO(String equationLatex, String correctAnswerStr, double correctAnswer,
                           double tolerance, String rawExpr, String rearrangedRaw) {
            this.equationLatex = equationLatex;
            this.correctAnswerStr = correctAnswerStr;
            this.correctAnswer = correctAnswer;
            this.tolerance = tolerance;
            this.rawExpr = rawExpr;
            this.rearrangedRaw = rearrangedRaw;
        }
    }

    /**
     * Create a new question:
     * - Uses problemGenerator to get (expr, answer)  → e.g., "(...)+(...)", "42"
     * - Uses GenerateRearrangedForm to get "a = ( ... )"
     * - Converts that to LaTeX via latexParser for front-end rendering
     */
    public static QuestionDTO nextQuestion() {
        String[] pair = problemGenerator.generate(); // [expr, answer]
        String expr = pair[0];
        String answerStr = pair[1];
        double answer = Double.parseDouble(answerStr);

        // Rearrange to "a = ( ... )"
        String rearranged = GenerateRearrangedForm.from(expr, answerStr);

        // Convert the entire equation to LaTeX with the parser (handles whitespace, ops, sqrt, ^, frac, etc.)
        String equationLatex = normalizeLatex(latexParser.toLatex(rearranged));

        double tolerance = DEFAULT_TOL;

        return new QuestionDTO(equationLatex, answerStr, answer, tolerance, expr, rearranged);
    }

    /**
     * Validate the user's answer string with a numeric tolerance.
     */
    public static boolean checkAnswer(String userInput, double correctAnswer, double tolerance) {
        if (userInput == null) return false;
        userInput = userInput.trim();
        // Optional: allow comma decimal
        userInput = userInput.replace(',', '.');
        try {
            double userVal = Double.parseDouble(userInput);
            return Math.abs(userVal - correctAnswer) <= tolerance;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Ensure we don't accidentally emit double backslashes like "\\cdot" when we want "\cdot"
    private static String normalizeLatex(String s) {
        if (s == null) return "";
        // collapse any double backslashes to single
        return s.replace("\\\\", "\\").trim();
    }
}
