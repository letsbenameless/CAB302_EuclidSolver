package com.example.javafx_backend_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

/**
 * A small quiz engine that front-ends can drive explicitly.
 * - Call nextQuestion() to advance (e.g., from a "Next" button).
 * - Call submitAnswer(...) to score the current question.
 * - Use getScoreKeeper() and getResults() for summaries/leaderboards.
 *
 * Backward-compatible: includes a minimal console main that pauses for Enter
 * like a "Next question" button instead of auto-advancing.
 */
public class quizGenerator {

    // Immutable record of what happened for each question.
    public static class QuestionResult {
        public final String expr;            // original arithmetic expression
        public final String rearranged;      // "a = ( ... )" before LaTeX
        public final String equationLatex;   // LaTeX (for UI renderers)
        public final double correctAnswer;
        public final boolean correct;
        public final String userAnswerRaw;

        QuestionResult(
                String expr,
                String rearranged,
                String equationLatex,
                double correctAnswer,
                boolean correct,
                String userAnswerRaw
        ) {
            this.expr = expr;
            this.rearranged = rearranged;
            this.equationLatex = equationLatex;
            this.correctAnswer = correctAnswer;
            this.correct = correct;
            this.userAnswerRaw = userAnswerRaw;
        }
    }

    // -------- Engine state (UI drives this class) --------
    private final int NUM_QUESTIONS;
    private final List<userProblemHandler.QuestionDTO> questions;
    private final List<QuestionResult> results = new ArrayList<>();
    private final scoreKeeper scoreKeeper;
    private int currentIndex = -1;

    private static final int PRELOAD_COUNT = 3;

    public quizGenerator(int numQuestions) {
        this.NUM_QUESTIONS = numQuestions;
        this.scoreKeeper = new scoreKeeper(numQuestions);

        // Pre-generate all questions so the UI can preload images, etc.
        questions = new ArrayList<>(NUM_QUESTIONS);
        generateUpTo(PRELOAD_COUNT);
    }

    // ------ UI-friendly API ------

    /** True if there is another question after the current one. */
    public boolean hasNextQuestion() {
        return (currentIndex + 1) < NUM_QUESTIONS;
    }

    /**
     * Advance to the next question (like a "Next question" button).
     * @return the new current QuestionDTO
     */
    public userProblemHandler.QuestionDTO nextQuestion() {
        if (!hasNextQuestion()) {
            throw new IllegalStateException("No more questions.");
        }
        currentIndex++;
        generateUpTo(currentIndex + 1 + PRELOAD_COUNT);
        return questions.get(currentIndex);
    }

    /** Return the current question object (null if nextQuestion() hasn't been called yet). */
    public userProblemHandler.QuestionDTO getCurrentQuestion() {
        if (currentIndex < 0 || currentIndex >= questions.size()) return null;
        return questions.get(currentIndex);
    }

    /**
     * Check the user's answer against the current question.
     * Records the result into ScoreKeeper and results list.
     * @return true if correct, false if incorrect
     */
    public boolean submitAnswer(String userInput) {
        var q = requireCurrent();
        boolean correct = userProblemHandler.checkAnswer(userInput, q.correctAnswer, q.tolerance);

        results.add(new QuestionResult(
                q.rawExpr,
                q.rearrangedRaw,
                q.equationLatex,
                q.correctAnswer,
                correct,
                userInput
        ));
        scoreKeeper.record(correct);
        return correct;
    }

    /** Save a PNG of the CURRENT question's LaTeX (handy for preloading into your UI). */
    public void saveCurrentQuestionPng(File out, int dpi) throws Exception {
        var q = requireCurrent();
        latexImage.saveLatexPng(q.equationLatex, out, dpi);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTotalQuestions() {
        return NUM_QUESTIONS;
    }

    public userProblemHandler.QuestionDTO peekNextQuestion() {
        int nextIndex = currentIndex + 1;
        if (nextIndex >= NUM_QUESTIONS) return null;
        generateUpTo(nextIndex + 1 + PRELOAD_COUNT);
        return questions.get(nextIndex);
    }

    /** Peek at a specific question index without advancing. */
    public userProblemHandler.QuestionDTO peekQuestion(int index) {
        if (index < 0 || index >= NUM_QUESTIONS) return null;
        generateUpTo(index + 1);
        return questions.get(index);
    }

    public scoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }

    public List<QuestionResult> getResults() {
        return results;
    }

    private userProblemHandler.QuestionDTO requireCurrent() {
        var q = getCurrentQuestion();
        if (q == null) throw new IllegalStateException("Call nextQuestion() before submitting an answer.");
        return q;
    }

    private void generateUpTo(int targetCount) {
        int target = Math.min(targetCount, NUM_QUESTIONS);
        while (questions.size() < target) {
            questions.add(userProblemHandler.nextQuestion());
        }
    }

    // --- Utility for pretty-printing answers like before ---
    private static String trimAnswer(double ans) {
        return Math.abs(ans - Math.rint(ans)) < 1e-12
                ? String.valueOf((long) Math.rint(ans))
                : String.format("%.3f", ans);
    }

    // ---------------- Minimal console harness (optional) ----------------
    // This keeps your old "run it from terminal" flow, but now waits for Enter
    // between questions—mimicking a "Next question" button.
    public static void main(String[] args) {
        final int NUM_QUESTIONS = 10;
        quizGenerator engine = new quizGenerator(NUM_QUESTIONS);
        Scanner scanner = new Scanner(System.in);

        System.out.println("All questions ready. Press Enter to begin...");
        scanner.nextLine();

        while (engine.hasNextQuestion()) {
            var q = engine.nextQuestion();

            System.out.println("\nQ" + (engine.getCurrentIndex() + 1) + ": Based on the following equation, solve for a.");
            System.out.println("   " + q.rearrangedRaw);

            try {
                String fileName = String.format("question_%02d.png", engine.getCurrentIndex() + 1);
                File out = new File(fileName);
                engine.saveCurrentQuestionPng(out, 220);
                System.out.println("   [Saved equation image: " + out.getAbsolutePath() + "]");
            } catch (Exception e) {
                System.out.println("   [Couldn’t render image: " + e.getMessage() + "]");
            }

            System.out.print("\nYour answer for a: ");
            String userInput = scanner.nextLine();

            boolean correct = engine.submitAnswer(userInput);
            if (correct) {
                System.out.println("Correct!");
            } else {
                System.out.println("Incorrect. The correct answer is " + trimAnswer(q.correctAnswer));
            }
            System.out.println("Score: " + engine.getScoreKeeper().getCorrect() + "/" + engine.getScoreKeeper().getAttempted());

            if (engine.hasNextQuestion()) {
                System.out.print("\nPress Enter for next question...");
                scanner.nextLine();
            }
        }

        // --- Final summary ---
        System.out.println("\n=== Quiz Complete ===");
        System.out.println("Final Score: " + engine.getScoreKeeper().summary());

        System.out.println("\nReview:");
        List<QuestionResult> results = engine.getResults();
        for (int i = 0; i < results.size(); i++) {
            QuestionResult r = results.get(i);
            System.out.printf("Q%d: %s  ->  %s  |  Answer: %s  |  %s%n",
                    i + 1, r.expr, r.rearranged, trimAnswer(r.correctAnswer), r.correct ? "✓" : "✗");
        }
    }
}
